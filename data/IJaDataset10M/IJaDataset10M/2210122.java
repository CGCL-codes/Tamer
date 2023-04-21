package de.glossmaker.gloss.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import de.glossmaker.gloss.datastructure.GlossItem;
import de.glossmaker.gloss.datastructure.GlossItems;
import de.glossmaker.gloss.observer.GlossItemsChangePublisher;
import de.glossmaker.gloss.undo.UndoGlossStack;
import de.glossmaker.undo.UndoableItem;

/**
 * 
 * @author Markus Flingelli
 *
 */
public class UndoListener implements PropertyChangeListener {

    private static final Logger mLogger = Logger.getLogger(UndoListener.class);

    private static UndoListener mUndoListener = null;

    private UndoGlossStack mUndoStack = null;

    private GlossItemsChangePublisher mGlossItemsChangePublisher = null;

    private UndoListener(GlossItems items) {
        mUndoStack = UndoGlossStack.getInstance();
        mGlossItemsChangePublisher = GlossItemsChangePublisher.getInstance(items);
    }

    public static UndoListener getInstance(GlossItems items) {
        if (mUndoListener == null) {
            mUndoListener = new UndoListener(items);
        }
        return mUndoListener;
    }

    public int undoSize() {
        return mUndoStack.undoSize();
    }

    public int redoSize() {
        return mUndoStack.redoSize();
    }

    private void undoHeadingChanged(UndoableItem item, boolean isRedo) {
        @SuppressWarnings("unchecked") ArrayList<Object> oldValues = (ArrayList<Object>) item.getOldValue();
        if (isRedo == true) {
            mGlossItemsChangePublisher.setHeading((GlossItem) oldValues.get(1), (String) item.getNewValue());
        } else {
            mGlossItemsChangePublisher.setHeading((GlossItem) oldValues.get(1), (String) oldValues.get(0));
        }
    }

    private void undoKeyChanged(UndoableItem item, boolean isRedo) {
        @SuppressWarnings("unchecked") ArrayList<Object> oldValues = (ArrayList<Object>) item.getOldValue();
        if (isRedo == true) {
            mGlossItemsChangePublisher.setKey((GlossItem) oldValues.get(1), (String) item.getNewValue());
        } else {
            mGlossItemsChangePublisher.setKey((GlossItem) oldValues.get(1), (String) oldValues.get(0));
        }
    }

    @SuppressWarnings("unchecked")
    private void undoWordChanged(UndoableItem item, boolean isRedo) {
        ArrayList<Object> oldValues = (ArrayList<Object>) item.getOldValue();
        if (isRedo == true) {
            mGlossItemsChangePublisher.setWord((GlossItem) oldValues.get(1), (String) item.getNewValue(), false);
        } else {
            mGlossItemsChangePublisher.setWord((GlossItem) oldValues.get(1), (String) oldValues.get(0), false);
        }
    }

    @SuppressWarnings("unchecked")
    private void undoPerAutoUpdateChanged(UndoableItem item, boolean isRedo) {
        ArrayList<Object> oldValues = (ArrayList<Object>) item.getOldValue();
        boolean isAutoUpdate = (Boolean) oldValues.get(2);
        if (isRedo == true) {
            GlossItem newItem = (GlossItem) oldValues.get(4);
            mGlossItemsChangePublisher.setWord((GlossItem) oldValues.get(1), newItem.getWord(), isAutoUpdate);
        } else {
            GlossItem oldItem = (GlossItem) oldValues.get(3);
            mGlossItemsChangePublisher.setWord((GlossItem) oldValues.get(1), oldItem.getWord(), isAutoUpdate);
        }
    }

    private void undoDefinitionChanged(UndoableItem item, boolean isRedo) {
        @SuppressWarnings("unchecked") ArrayList<Object> oldValues = (ArrayList<Object>) item.getOldValue();
        if (isRedo == true) {
            mGlossItemsChangePublisher.setDefinition((GlossItem) oldValues.get(1), (String) item.getNewValue());
        } else {
            mGlossItemsChangePublisher.setDefinition((GlossItem) oldValues.get(1), (String) oldValues.get(0));
        }
    }

    private void undoAddItem(UndoableItem item, boolean isRedo) {
        if (isRedo == true) {
            mGlossItemsChangePublisher.addGlossItem((GlossItem) item.getNewValue());
        } else {
            mGlossItemsChangePublisher.removeGlossItem((GlossItem) item.getNewValue());
        }
    }

    private void undoRemoveItem(UndoableItem item, boolean isRedo) {
        if (isRedo == true) {
            mGlossItemsChangePublisher.removeGlossItem((GlossItem) item.getNewValue());
        } else {
            mGlossItemsChangePublisher.addGlossItem((GlossItem) item.getNewValue());
        }
    }

    private void undoImportFile(UndoableItem item, boolean isRedo) {
        mGlossItemsChangePublisher.clear();
        if (isRedo == true) {
            mGlossItemsChangePublisher.importGlossItems((GlossItems) item.getNewValue());
        } else {
            mGlossItemsChangePublisher.importGlossItems((GlossItems) item.getOldValue());
        }
    }

    private void undoRemoveHeading(UndoableItem item, boolean isRedo) {
        if (isRedo == true) {
            GlossItems items = (GlossItems) item.getOldValue();
            GlossItem glossItem = items.get(0);
            mGlossItemsChangePublisher.removeGlossItemHeading(glossItem.getHeading());
        } else {
            mGlossItemsChangePublisher.importGlossItems((GlossItems) item.getOldValue());
        }
    }

    private void undoAction(UndoableItem item, boolean isRedo) {
        mGlossItemsChangePublisher.removeUndoListener();
        if (item.getPropertyName().equals(EActionCommands.GLOSS_ITEM_ADDED.toString())) {
            undoAddItem(item, isRedo);
        } else if (item.getPropertyName().equals(EActionCommands.GLOSS_ITEM_REMOVED.toString())) {
            undoRemoveItem(item, isRedo);
        } else if (item.getPropertyName().equals(EActionCommands.GLOSS_ITEM_HEADING_REMOVED.toString())) {
            undoRemoveHeading(item, isRedo);
        } else if (item.getPropertyName().equals(EActionCommands.GLOSS_ITEM_DEFINITION_CHANGED.toString())) {
            undoDefinitionChanged(item, isRedo);
        } else if (item.getPropertyName().equals(EActionCommands.GLOSS_ITEM_HEADING_CHANGED.toString())) {
            undoHeadingChanged(item, isRedo);
        } else if (item.getPropertyName().equals(EActionCommands.GLOSS_ITEM_KEY_CHANGED.toString())) {
            undoKeyChanged(item, isRedo);
        } else if (item.getPropertyName().equals(EActionCommands.GLOSS_ITEM_WORD_CHANGED.toString())) {
            undoWordChanged(item, isRedo);
        } else if (item.getPropertyName().equals(EActionCommands.FILE_IMPORTED.toString())) {
            undoImportFile(item, isRedo);
        } else if (item.getPropertyName().equals(EActionCommands.GLOSS_ITEM_PER_AUTO_UPDATE_CHANGED.toString())) {
            undoPerAutoUpdateChanged(item, isRedo);
        }
        mGlossItemsChangePublisher.addUndoListener();
    }

    public void undo() {
        if (mUndoStack.undoSize() > 0) {
            UndoableItem item = mUndoStack.popUndo();
            undoAction(item, false);
            mUndoStack.pushRedo(item);
            mGlossItemsChangePublisher.fireUndoOrRedo(item.getPropertyName());
        }
    }

    public void redo() {
        if (mUndoStack.redoSize() > 0) {
            UndoableItem item = mUndoStack.popRedo();
            undoAction(item, true);
            mUndoStack.pushUndo(item);
            mGlossItemsChangePublisher.fireUndoOrRedo(item.getPropertyName());
        }
    }

    private boolean isEventEquals(PropertyChangeEvent event, EActionCommands command) {
        return event.getPropertyName().equals(command.toString());
    }

    private void undoProductSave(PropertyChangeEvent event) {
        if (isEventEquals(event, EActionCommands.GLOSS_ITEM_ADDED) || isEventEquals(event, EActionCommands.GLOSS_ITEM_REMOVED) || isEventEquals(event, EActionCommands.GLOSS_ITEM_HEADING_REMOVED) || isEventEquals(event, EActionCommands.GLOSS_ITEM_DEFINITION_CHANGED) || isEventEquals(event, EActionCommands.GLOSS_ITEM_HEADING_CHANGED) || isEventEquals(event, EActionCommands.GLOSS_ITEM_KEY_CHANGED) || isEventEquals(event, EActionCommands.GLOSS_ITEM_WORD_CHANGED) || isEventEquals(event, EActionCommands.FILE_IMPORTED) || isEventEquals(event, EActionCommands.GLOSS_ITEM_PER_AUTO_UPDATE_CHANGED)) {
            UndoableItem item = new UndoableItem();
            item.setOldValue(event.getOldValue());
            item.setNewValue(event.getNewValue());
            item.setPropertyName(event.getPropertyName());
            mUndoStack.pushUndo(item);
            String oldValue = null;
            String newValue = null;
            if (event.getOldValue() != null) {
                oldValue = event.getOldValue().toString();
            }
            if (event.getNewValue() != null) {
                newValue = event.getNewValue().toString();
            }
            mLogger.debug("undoProductSave(): Add object to undo stack. " + "Event=" + event.getPropertyName() + ", old=" + oldValue + ", new=" + newValue);
        }
        if (event.getPropertyName().equals("file_opened") || event.getPropertyName().equals("file_saved") || event.getPropertyName().equals("new_product_created")) {
            mUndoStack.reset();
        }
    }

    public boolean isContentChanged() {
        boolean result = true;
        if (mUndoStack.undoSize() == 0) {
            result = false;
        }
        return result;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        undoProductSave(event);
    }
}
