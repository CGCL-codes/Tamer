package threading;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;
import model.CardBatches;
import model.PaukerCore;
import util.Callback;
import util.Toolbox;
import beans.Batch;
import beans.Card;
import controller.importexport.ImportExportController;
import de.enough.polish.util.ArrayList;

/**
 * Load/Save the model (cards) from/into RMS.
 *
 * TODO We can improve performance significantly by allocating ArrayLists of the
 * needed size in Batch - we just need to store the sizes of the individual
 * batches. Ex.: it can improve time from 121 to 93 sec.
 *
 * @author Markus Brosch (markus[at]brosch[dot]net)
 */
public class RmsSaveLoad extends BackgroundTask {

    public static class RmsSaveLoadBuilder {

        private RmsSaveLoad saveLoad;

        private RmsSaveLoadBuilder() {
        }

        public RmsSaveLoadBuilder newSaver(final ImportExportController importExportController, final ViewNavigation navigation, final PaukerCore model, final String lessonName) {
            saveLoad = new RmsSaveLoad(importExportController, navigation, true, model, lessonName);
            return this;
        }

        public RmsSaveLoadBuilder newLoader(final ImportExportController importExportController, final ViewNavigation navigation, final PaukerCore model, final String lessonName) {
            saveLoad = new RmsSaveLoad(importExportController, navigation, false, model, lessonName);
            return this;
        }

        public RmsSaveLoadBuilder withRunInBackgroundAllowed(final boolean allowed) {
            saveLoad.runInBackgroundAllowed = allowed;
            return this;
        }

        /**
         * [Optional] Register an action that should be executed after this task finishes completely.
         */
        public RmsSaveLoadBuilder withOnFinishAction(final Callback finishAction) {
            saveLoad.setFinishFollowCallback(new FollowOnTaskCallback() {

                public BackgroundTask executeAndPrepareFollowOnTask() {
                    finishAction.execute();
                    return null;
                }
            });
            return this;
        }

        /**
         * [Optional] Register an action that should be executed after this task finishes completely.
         */
        public RmsSaveLoadBuilder withFollowOnTask(final FollowOnTaskCallback finishAction) {
            saveLoad.setFinishFollowCallback(finishAction);
            return this;
        }

        public RmsSaveLoad build() {
            return saveLoad;
        }
    }

    private final boolean _save;

    private final PaukerCore _model;

    private String taskName = "Lesson Save/Load";

    /** Is the task interrupted by another one (should it stop)? */
    private boolean interrupted = false;

    private boolean runInBackgroundAllowed = true;

    /** The name of the lesson being loaded/stored */
    private final String lessonName;

    /**
     * True if only some cards from the beginning has been loaded and we're going
     * to save into the same lesson. Hence we will just replace the records
     * loaded so far.
     * The number of loaded cards should be in cardsCnt.
     */
    private boolean savePartiallyLoaded = false;

    /** Number of cards loaded so far. */
    private int loadedCardsCnt;

    private FollowOnTaskCallback finishFollowCallback;

    private BackgroundTask followOnTask;

    public static RmsSaveLoadBuilder builder() {
        return new RmsSaveLoadBuilder();
    }

    /**
     * @param importExportController The current (and only) MiniPauker controller
     * @param navigation see {@link BackgroundTask#BackgroundTask(ViewNavigation)}
     * @param save If true do save, if false do load
     * @param name a lesson name used to derive the the record store id to use
     * @param model The current (and only) MiniPauker model (to add cards to/get from)
     * @param lessonName2
     */
    private RmsSaveLoad(final ImportExportController importExportController, final ViewNavigation navigation, final boolean save, final PaukerCore model, final String lessonName) {
        super(navigation);
        if (lessonName == null) {
            throw new IllegalArgumentException("The argument lessonName: String may not be null. Saving:" + save);
        }
        _save = save;
        runInBackgroundAllowed = (save == false);
        _model = model;
        taskName = _save ? "Lesson Save" : "Lesson Load";
        this.lessonName = lessonName;
    }

    public void runLongTask() throws Exception {
        final String recordStoreDescriptor = lessonName + Toolbox.PAUKEREXT;
        if (_save) {
            saveCardsToRms(recordStoreDescriptor);
            _model.resetHasUnsavedChanges();
            System.err.println("RmsS.Save-exiting at " + Toolbox.getCurrentTimestamp() + "; model.changed reset: " + _model.isHasUnsavedChanges());
        } else {
            loadCardsFromRms(recordStoreDescriptor);
            System.err.println("RmsS.Load-exiting;loaded " + loadedCardsCnt + " at " + Toolbox.getCurrentTimestamp() + "; model.chnged:" + _model.isHasUnsavedChanges());
        }
        invokeOnFinishActionIfSet();
    }

    /**
     * Registers the {@link #finishFollowCallback} to be executed AFTER this task
     * finishes so that, for example, it could start a new task.
     */
    private void invokeOnFinishActionIfSet() {
        if (this.getFinishFollowCallback() != null) {
            final BackgroundTask nextTask = getFinishFollowCallback().executeAndPrepareFollowOnTask();
            if (nextTask != null) {
                System.err.println("invokeOnFinishActionIfSet: invoking the follow-on task " + nextTask);
                this.registerFollowOnTask(nextTask);
            }
        }
    }

    /**
     * Save cards into a record store.
     * <p>
     * NOTE: It's possible to interrup a load with a save if they both work on the
     * same record store.
     * </p>
     * @param recordStoreDescriptor
     * @param rs
     * @throws Exception
     */
    private void saveCardsToRms(final String recordStoreDescriptor) throws Exception {
        System.err.println("RmsSave:starting for '" + recordStoreDescriptor + "';cardsCnt:" + loadedCardsCnt);
        RecordStore recordStore = null;
        try {
            recordStore = trySaveCardsToRms(recordStoreDescriptor);
        } catch (Exception e) {
            try {
                RecordStore.deleteRecordStore(recordStoreDescriptor);
            } catch (Exception ee) {
            }
            throw e;
        } finally {
            try {
                recordStore.closeRecordStore();
            } catch (RecordStoreException e) {
            }
        }
    }

    private RecordStore trySaveCardsToRms(final String recordStoreDescriptor) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException, RecordStoreNotOpenException, IOException, InvalidRecordIDException {
        if (!savePartiallyLoaded) {
            try {
                RecordStore.deleteRecordStore(recordStoreDescriptor);
            } catch (RecordStoreNotFoundException e) {
            }
        }
        final RecordStore recordStore = RecordStore.openRecordStore(recordStoreDescriptor, true);
        RecordEnumeration existingRecords = null;
        int storedCardsCnt = 0;
        if (savePartiallyLoaded) {
            existingRecords = recordStore.enumerateRecords(null, null, false);
        }
        final Batch[] batches = _model.getPersistentBatches().getAllCards();
        for (int i = 0; i < batches.length; i++) {
            final Batch batch = batches[i];
            storedCardsCnt += saveBatchToRms(batch, recordStore, existingRecords);
        }
        if (savePartiallyLoaded && loadedCardsCnt >= 0) {
            while (--loadedCardsCnt >= 0 && existingRecords.hasNextElement()) {
                int recordId = existingRecords.nextRecordId();
                recordStore.deleteRecord(recordId);
            }
        }
        System.err.println("Rms.Save:saved # cards:" + storedCardsCnt);
        recordStore.closeRecordStore();
        return recordStore;
    }

    private int saveBatchToRms(final Batch batch, final RecordStore recordStore, final RecordEnumeration existingRecords) throws IOException, InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException, RecordStoreFullException {
        int storedCardsCnt = 0;
        final ArrayList cards = batch.getCards();
        for (int cardIdx = 0; cardIdx < cards.size(); cardIdx++) {
            final Card card = (Card) cards.get(cardIdx);
            final byte[] data = serializeCard(card);
            if (savePartiallyLoaded) {
                if (cardIdx == 0) {
                    System.err.println("saveCardsToRms: Saving to a partially loaded lesson, replacing " + loadedCardsCnt + " already loaded records");
                }
                if (--loadedCardsCnt >= 0) {
                    if (!existingRecords.hasNextElement()) {
                        throw new IllegalStateException("There should be a record to replace but isn't; cardsCnt=" + loadedCardsCnt);
                    } else {
                        int recordId = existingRecords.nextRecordId();
                        recordStore.setRecord(recordId, data, 0, data.length);
                    }
                } else {
                    recordStore.addRecord(data, 0, data.length);
                }
            } else {
                recordStore.addRecord(data, 0, data.length);
            }
            ++storedCardsCnt;
        }
        return storedCardsCnt;
    }

    private byte[] serializeCard(Card card) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(card.getBatchNumber());
        dos.writeLong(card.getTime());
        dos.writeUTF(card.getA());
        dos.writeUTF(card.getB());
        dos.flush();
        dos.close();
        baos.flush();
        byte[] data = baos.toByteArray();
        return data;
    }

    /**
     * Load cards from a record store.
     * <p>
     * NOTE: Loading may be stopped by a save if they both work on the same
     * record store.
     * </p>
     * @param recordStoreDescriptor
     * @throws Exception
     */
    private final void loadCardsFromRms(final String recordStoreDescriptor) throws Exception {
        RecordStore rs = null;
        try {
            System.err.println("RmsS.Load-starting,model init:\t" + Toolbox.getCurrentTimestamp());
            System.err.println("RmsS.Load-open r.store:\t" + Toolbox.getCurrentTimestamp());
            rs = RecordStore.openRecordStore(recordStoreDescriptor, false);
            System.err.println("RmsS.Load-enum.records:\t" + Toolbox.getCurrentTimestamp());
            RecordEnumeration re = rs.enumerateRecords(null, null, false);
            System.err.println("RmsS.Load-reading elms:\t" + Toolbox.getCurrentTimestamp());
            final int batchSize = 100;
            loadedCardsCnt = 0;
            boolean wasInBackground = isInBackground();
            ArrayList cards = new ArrayList(batchSize);
            _model.importLesson(lessonName, CardBatches.newSaved(cards));
            while ((re.hasNextElement() && !interrupted)) {
                byte[] nextCardRecord = re.nextRecord();
                final Card restoredCard = deserializeCard(nextCardRecord);
                cards.add(restoredCard);
                loadedCardsCnt++;
                if (cards.size() == batchSize || !re.hasNextElement()) {
                    _model.getPersistentBatches().addCards(cards);
                    cards.clear();
                    notifyBatchLoaded(batchSize);
                    if (!wasInBackground) {
                        wasInBackground = true;
                        switchToBackground();
                    }
                }
            }
            if (interrupted) {
                cleanUpWhenInterrupted(cards);
            }
            System.err.println("RmsS.Load-done reading " + loadedCardsCnt + " records at " + Toolbox.getCurrentTimestamp());
            re.destroy();
        } catch (Exception e) {
            _model.getPersistentBatches().reset();
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.closeRecordStore();
                }
            } catch (RecordStoreException e) {
            }
        }
    }

    private void notifyBatchLoaded(final int batchSize) {
        System.out.println("RmsLoad:adding cards " + loadedCardsCnt + "x" + batchSize);
        displayProgress(loadedCardsCnt + " cards");
    }

    private Card deserializeCard(byte[] nextCardRecord) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(nextCardRecord));
        final int batchNr = dis.readInt();
        final long time = dis.readLong();
        final String a = dis.readUTF();
        final String b = dis.readUTF();
        final Card restoredCard = new Card(batchNr, time, a, b);
        dis.close();
        return restoredCard;
    }

    private void cleanUpWhenInterrupted(ArrayList cards) {
        System.err.println("RmsS.Load-interrupted,adding last cards:" + cards.size() + " from total " + loadedCardsCnt);
        _model.getPersistentBatches().addCards(cards);
        cards.clear();
        BackgroundTask replacement = getReplacement();
        if (replacement != null && replacement instanceof RmsSaveLoad) {
            ((RmsSaveLoad) replacement).setCardsCnt(this.loadedCardsCnt);
        }
    }

    public String getTaskName() {
        return taskName;
    }

    /** Number of cards loaded so far. */
    protected void setCardsCnt(int cnt) {
        this.loadedCardsCnt = cnt;
    }

    /** Is this Save task replacing a Load task on the same store? */
    protected void setSavePartiallyLoaded(boolean partiallLoaded) {
        this.savePartiallyLoaded = partiallLoaded;
    }

    /** Can this task be replaced with the given one? */
    public boolean replacableBy(BackgroundTask task) {
        return isSaveReplacingCurrentLoadOnSameStore(task);
    }

    private boolean isSaveReplacingCurrentLoadOnSameStore(BackgroundTask task) {
        if (!(task instanceof RmsSaveLoad)) {
            return false;
        } else {
            RmsSaveLoad saveOrLoad = (RmsSaveLoad) task;
            return !this._save && saveOrLoad._save && lessonName.equals(saveOrLoad.lessonName);
        }
    }

    /**
     * Replace this Load task by a Save on the same record store.
     * If only some cards from the beginning of the store have been loaded so far,
     * we don't need to wait to load the others and can just save those loaded so
     * for, replacing their originals.
     *
     * @throws IllegalStateException If this task can't be interrupted with the given one.
     * @see BackgroundTask#registerReplacement(BackgroundTask)
     * @see BackgroundTask#registerReplacementHook(BackgroundTask)
     */
    protected void registerReplacementHook(BackgroundTask task) throws IllegalStateException {
        if (task == followOnTask) {
            registerFollowOnReplacement(task);
        } else {
            tryRegisterImmedateReplacement(task);
        }
    }

    private void tryRegisterImmedateReplacement(BackgroundTask task) {
        RmsSaveLoad save = (RmsSaveLoad) task;
        if (!replacableBy(save)) {
            throw new IllegalStateException("This task " + this + " can't be replaced by the given one " + save);
        }
        System.err.println("registerReplacementHook:starting for " + task);
        save.setSavePartiallyLoaded(true);
        this.interrupted = true;
        save.setCardsCnt(this.loadedCardsCnt);
    }

    private void registerFollowOnReplacement(BackgroundTask task) {
        System.err.println("registerReplacementHook:registered follow-on task " + task);
    }

    private void registerFollowOnTask(final BackgroundTask nextTask) {
        this.followOnTask = nextTask;
        super.registerReplacement(nextTask);
    }

    protected boolean runInBackgroundAllowed() {
        return runInBackgroundAllowed;
    }

    private void setFinishFollowCallback(FollowOnTaskCallback finishFollowCallback) {
        if (this.finishFollowCallback != null) {
            throw new IllegalStateException("It is not possible to set the FinishFollowCallback when " + "there is one set already. This: " + this);
        }
        this.finishFollowCallback = finishFollowCallback;
    }

    private FollowOnTaskCallback getFinishFollowCallback() {
        return finishFollowCallback;
    }

    public String toString() {
        return getClass() + "[" + getTaskName() + " for '" + lessonName + "']";
    }
}
