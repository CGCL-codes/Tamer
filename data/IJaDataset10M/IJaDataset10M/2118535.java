package at.jku.semwiq.webapp.ice.handler;

import javax.faces.event.AbortProcessingException;
import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.component.paneltabset.TabChangeListener;

public class AdministrationUserHandler implements TabChangeListener {

    private String selectedIndex = "0";

    public String getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(String selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = String.valueOf(selectedIndex);
    }

    public int getFocusIndex() {
        return Integer.parseInt(selectedIndex);
    }

    public void setFocusIndex(int index) {
        selectedIndex = String.valueOf(index);
    }

    /**
     * Called when the table binding's tab focus changes.
     *
     * @param tabChangeEvent used to set the tab focus.
     * @throws AbortProcessingException An exception that may be thrown by event
     *                                  listeners to terminate the processing of the current event.
     */
    public void processTabChange(TabChangeEvent tabChangeEvent) throws AbortProcessingException {
    }
}
