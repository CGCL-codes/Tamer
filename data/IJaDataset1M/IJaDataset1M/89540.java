package ps.client.gui.dialog.trigger;

import javax.swing.DefaultListModel;
import ps.server.trigger.TriggerEntry;

@SuppressWarnings("serial")
public class TriggerListModel extends DefaultListModel {

    private static final String NEW_TRIGGER_TITLE = "Neuer Trigger";

    public void addTrigger(TriggerEntry entry) {
        int insertIndex = size();
        for (int i = 0; i < getSize(); i++) {
            if (entry.getTitle().compareTo(getTrigger(i).getTitle()) < 0) {
                insertIndex = i;
                break;
            }
        }
        insertElementAt(entry, insertIndex);
    }

    public TriggerEntry addNewTrigger() {
        String title = NEW_TRIGGER_TITLE;
        for (int i = 1; containsTitle(title); i++) {
            title = NEW_TRIGGER_TITLE + " (" + i + ")";
        }
        TriggerEntry entry = new TriggerEntry();
        entry.setTitle(title);
        addTrigger(entry);
        return entry;
    }

    public void refreshTrigger(TriggerEntry entry) {
        removeElement(entry);
        addTrigger(entry);
    }

    public TriggerEntry getTrigger(int index) {
        return (TriggerEntry) getElementAt(index);
    }

    public TriggerEntry[] getAllTrigger() {
        TriggerEntry[] ret = new TriggerEntry[getSize()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (TriggerEntry) getElementAt(i);
        }
        return ret;
    }

    public boolean containsTitle(String title) {
        for (int i = 0; i < getSize(); i++) {
            if (getTrigger(i).getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }
}
