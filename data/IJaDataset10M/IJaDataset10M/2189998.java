package net.sf.joafip.service.rel400;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.kvstore.service.HeapException;
import net.sf.joafip.kvstore.service.IHeapDataManager;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.service.AbstractCopyRuntime;
import net.sf.joafip.service.FilePersistence;
import net.sf.joafip.service.FilePersistenceBuilder;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;
import net.sf.joafip.service.IExclusiveDataAccessSession;
import net.sf.joafip.service.JoafipMutex;
import net.sf.joafip.store.entity.garbage.ReferenceLinkEntry;
import net.sf.joafip.store.service.IGarbageListener;
import net.sf.joafip.store.service.garbage.GarbageDataIntegrityChecker;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableException;
import net.sf.joafip.store.service.objectfortest.Bob1;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.proxy.IInstanceFactory;
import net.sf.joafip.store.service.proxy.ProxyException;
import net.sf.joafip.store.service.proxy.ProxyManager2;

@NotStorableClass
@StorableAccess
public class TestRuntime310Garbage extends AbstractCopyRuntime implements IGarbageListener {

    private static final JoafipLogger _LOG = JoafipLogger.getLogger(TestRuntime310Garbage.class);

    private GarbageDataIntegrityChecker garbageDataIntegrityChecker;

    private FilePersistence filePersistence;

    private boolean garbaged;

    private boolean waitGarbaged;

    private JoafipMutex joafipMutex;

    public TestRuntime310Garbage() throws TestException {
        super();
    }

    public TestRuntime310Garbage(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        copyFile();
        garbageDataIntegrityChecker = GarbageDataIntegrityChecker.getInstance();
        final FilePersistenceBuilder builder = new FilePersistenceBuilder();
        builder.setPathName(path.getPath());
        builder.setProxyMode(true);
        builder.setRemoveFiles(false);
        builder.setGarbageManagement(true);
        builder.setCrashSafeMode(false);
        filePersistence = (FilePersistence) builder.build();
        joafipMutex = filePersistence.getMutex();
        filePersistence.addToNotCheckMethod(Bob1.class);
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            filePersistence.close();
        } catch (final Exception e) {
        }
        filePersistence = null;
        joafipMutex = null;
        garbageDataIntegrityChecker = null;
        super.tearDown();
    }

    protected void copyFile() throws IOException {
        copyFile("../joafip/runtime310garbage/all.flag", path);
        copyFile("../joafip/runtime310garbage/backup.flag", path);
        copyFile("../joafip/runtime310garbage/data.flag", path);
        copyFile("../joafip/runtime310garbage/store.bak", path);
        copyFile("../joafip/runtime310garbage/store.data", path);
    }

    public void testGarbageKeepDetachedObject() throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException, ProxyException, TestException, ObjectIOException, ObjectIODataCorruptedException, HeapException, FilePersistenceTooBigForSerializationException, HeapRecordableException {
        final IExclusiveDataAccessSession session = filePersistence.createExclusiveDataAccessSession();
        session.open();
        garbageDataIntegrityChecker.check(filePersistence);
        final IInstanceFactory instanceFactory = session.getInstanceFactory();
        Bob1 bob1 = Bob1.newInstance(instanceFactory, true);
        bob1.setVal(10);
        session.save();
        assertTrue("must be unloaded", ProxyManager2.isUnloaded(bob1));
        final DataRecordIdentifier dataRecordIdentifier = filePersistence.getCurrentDataRecordIdentifierAssociatedToObject(bob1);
        assertNotNull("bob1 must be saved as a detached object", dataRecordIdentifier);
        garbageDataIntegrityChecker.check(filePersistence);
        assertAttachedState(dataRecordIdentifier, false);
        assertTrue("bob1 #" + dataRecordIdentifier + " must be a garbage candidate", garbageDataIntegrityChecker.isKnownCandidate(dataRecordIdentifier));
        assertFalse("bob1 #" + dataRecordIdentifier + "must not be to garbage", garbageDataIntegrityChecker.isKnownToGarbage(dataRecordIdentifier));
        filePersistence.garbageSweep();
        garbageDataIntegrityChecker.check(filePersistence);
        assertAttachedState(dataRecordIdentifier, false);
        assertFalse("bob1 #" + dataRecordIdentifier + " must not be a garbage candidate", garbageDataIntegrityChecker.isKnownCandidate(dataRecordIdentifier));
        assertFalse("bob1 #" + dataRecordIdentifier + "must not be to garbage", garbageDataIntegrityChecker.isKnownToGarbage(dataRecordIdentifier));
        bob1 = (Bob1) filePersistence.createObjectReadingInStoreOrGetExisting(dataRecordIdentifier);
        assertEquals("bad value", 10, bob1.getVal());
        session.close();
        garbageDataIntegrityChecker.check(filePersistence);
        assertAttachedState(dataRecordIdentifier, false);
        filePersistence.garbageSweep();
        garbageDataIntegrityChecker.check(filePersistence);
        session.open();
        assertDeleted(dataRecordIdentifier);
    }

    public void testGarbageInBackground() throws FilePersistenceException, InterruptedException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException, ProxyException, TestException, ObjectIOException, ObjectIODataCorruptedException, FilePersistenceTooBigForSerializationException, HeapException {
        filePersistence.setGarbageListener(this);
        final IExclusiveDataAccessSession session = filePersistence.createExclusiveDataAccessSession();
        session.open();
        final IInstanceFactory instanceFactory = session.getInstanceFactory();
        final Bob1 bob1 = Bob1.newInstance(instanceFactory, true);
        bob1.setVal(10);
        session.save();
        final DataRecordIdentifier dataRecordIdentifier = filePersistence.getCurrentDataRecordIdentifierAssociatedToObject(bob1);
        assertNotNull("bob1 must be saved as a detached object", dataRecordIdentifier);
        garbageDataIntegrityChecker.check(filePersistence);
        assertAttachedState(dataRecordIdentifier, false);
        backgroundGarbageSweep();
        assertTrue("must be unloaded", ProxyManager2.isUnloaded(bob1));
        assertEquals("bad value", 10, bob1.getVal());
        session.close();
        garbageDataIntegrityChecker.check(filePersistence);
        assertAttachedState(dataRecordIdentifier, false);
        backgroundGarbageSweep();
        garbageDataIntegrityChecker.check(filePersistence);
        session.open();
        assertDeleted(dataRecordIdentifier);
        final Integer integer = (Integer) session.getObject("K0");
        assertNotNull("must have value", integer);
        assertEquals("bad balue", 0, integer.intValue());
        session.close();
    }

    private void backgroundGarbageSweep() throws FilePersistenceException, InterruptedException {
        synchronized (joafipMutex) {
            garbaged = false;
            filePersistence.enableBackgroundGarbageSweep(0);
            if (filePersistence.getNumberOfGarbageCandidate() + filePersistence.getNumberOfToGarbage() != 0) {
                while (!garbaged) {
                    waitGarbaged = true;
                    joafipMutex.doWait();
                    _LOG.debug("wait end");
                }
            }
            filePersistence.disableBackgroundGarbageSweep();
        }
    }

    @Override
    public void notifyGarbaged(final int leftGarbageCandidate, final int leftToGarbage, final int numberOfGarbaged) {
        garbaged = leftGarbageCandidate == 0 && leftToGarbage == 0;
        _LOG.debug("candidate " + leftGarbageCandidate + ", to garbage " + leftToGarbage);
        synchronized (joafipMutex) {
            if (garbaged && waitGarbaged) {
                joafipMutex.doNotifyAll();
                waitGarbaged = false;
            }
        }
    }

    public void testGarbageSweepOutOfSession() throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException, ProxyException, ObjectIOException, ObjectIODataCorruptedException, TestException, FilePersistenceTooBigForSerializationException, HeapException {
        final IExclusiveDataAccessSession session = filePersistence.createExclusiveDataAccessSession();
        session.open();
        final IInstanceFactory instanceFactory = session.getInstanceFactory();
        Bob1 bob1 = Bob1.newInstance(instanceFactory, true);
        bob1.setVal(10);
        session.save();
        final DataRecordIdentifier dataRecordIdentifier = filePersistence.getCurrentDataRecordIdentifierAssociatedToObject(bob1);
        assertNotNull("bob1 must be saved as a detached object", dataRecordIdentifier);
        garbageDataIntegrityChecker.check(filePersistence);
        assertAttachedState(dataRecordIdentifier, false);
        filePersistence.garbageSweep();
        assertTrue("must be unloaded", ProxyManager2.isUnloaded(bob1));
        assertEquals("bad value", 10, bob1.getVal());
        bob1 = null;
        _LOG.debug("session close");
        session.close();
        garbageDataIntegrityChecker.check(filePersistence);
        assertAttachedState(dataRecordIdentifier, false);
        garbageDataIntegrityChecker.check(filePersistence);
        _LOG.debug("garbage sweep");
        filePersistence.garbageSweep();
        session.open();
        assertDeleted(dataRecordIdentifier);
        final Integer integer = (Integer) session.getObject("K0");
        assertNotNull("must have value", integer);
        assertEquals("bad balue", 0, integer.intValue());
        session.close();
    }

    private void assertDeleted(final DataRecordIdentifier dataRecordIdentifier) throws FilePersistenceException, ObjectIOException, ObjectIODataCorruptedException, HeapException {
        final IHeapDataManager dataManager = filePersistence.getDataManager();
        assertFalse("must not found data record " + dataRecordIdentifier, dataManager.hasDataRecord(dataRecordIdentifier));
    }

    private void assertAttachedState(final DataRecordIdentifier dataRecordIdentifier, final boolean attached) throws TestException {
        final boolean detachedAccordingToStorage = garbageDataIntegrityChecker.isDetachedAccordingToStorage(dataRecordIdentifier);
        final boolean attachedAccordingToStorage = garbageDataIntegrityChecker.isAttachedAccordingToStorage(dataRecordIdentifier);
        final boolean detachedAccordingToLinkInfo = garbageDataIntegrityChecker.isDetachedAccordingToLinkInfo(dataRecordIdentifier);
        final boolean attachedAccordingToLinkInfo = garbageDataIntegrityChecker.isAttachedAccordingToLinkInfo(dataRecordIdentifier);
        if (attachedAccordingToLinkInfo != attached || detachedAccordingToLinkInfo == attached || attachedAccordingToStorage != attached || detachedAccordingToStorage == attached) {
            final StringBuilder builder = new StringBuilder();
            builder.append(dataRecordIdentifier);
            if (attached) {
                builder.append(" must be attached: detached for storage=");
            } else {
                builder.append(" must be detached: detached for storage=");
            }
            builder.append(detachedAccordingToStorage);
            builder.append(" (");
            builder.append(detachedAccordingToStorage != attached ? "ok" : "ko");
            builder.append("), detached for link info=");
            builder.append(detachedAccordingToLinkInfo);
            builder.append(" (");
            builder.append(detachedAccordingToLinkInfo != attached ? "ok" : "ko");
            builder.append("), attached for storage=");
            builder.append(attachedAccordingToStorage);
            builder.append(" (");
            builder.append(attachedAccordingToStorage == attached ? "ok" : "ko");
            builder.append(", attached for link info=");
            builder.append(attachedAccordingToLinkInfo);
            builder.append(" (");
            builder.append(attachedAccordingToLinkInfo == attached ? "ok" : "ko");
            builder.append(')');
            if (!attached) {
                List<ReferenceLinkEntry> list = garbageDataIntegrityChecker.getAttachedToRootLinkFromLinkInfo();
                builder.append("\nattached to root from link info\n");
                for (final ReferenceLinkEntry link : list) {
                    final Set<DataRecordIdentifier> referencedSet = link.getReferencedSet();
                    for (final DataRecordIdentifier referenced : referencedSet) {
                        builder.append(link.getDataRecordIdentifier());
                        builder.append(" -> ");
                        builder.append(referenced);
                        builder.append('\n');
                    }
                }
                list = garbageDataIntegrityChecker.getAttachedToRootLinkFromStorage();
                builder.append("\nattached to root from storage\n");
                for (final ReferenceLinkEntry link : list) {
                    final Set<DataRecordIdentifier> referencedSet = link.getReferencedSet();
                    for (final DataRecordIdentifier referenced : referencedSet) {
                        builder.append(link.getDataRecordIdentifier());
                        builder.append(" -> ");
                        builder.append(referenced);
                        builder.append('\n');
                    }
                }
            }
            fail(builder.toString());
        }
    }
}
