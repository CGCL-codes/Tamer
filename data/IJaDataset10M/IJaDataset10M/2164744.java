package testingapplication.tabbedPanel;

import com.softaspects.jsf.component.base.event.AjaxEvent;
import com.softaspects.jsf.component.base.listener.AjaxEventListener;
import com.softaspects.jsf.component.tabbedPanel.TabbedPanel;
import com.softaspects.jsf.renderer.tabbedPanel.TabbedPanelItem;
import javax.faces.event.AbortProcessingException;
import java.io.IOException;

public class TabbedPanelAjaxEventListener implements AjaxEventListener {

    public void processAjaxEvent(AjaxEvent event) throws AbortProcessingException {
        try {
            TabbedPanel tp = (TabbedPanel) event.getComponent();
            int sel = tp.getListSelectionModel().getFirstSelection();
            TabbedPanelItem tab = (TabbedPanelItem) tp.getListDataModel().getValuetAt(sel);
            event.getResponse().getWriter().write(tab.getText());
        } catch (IOException e) {
            throw new AbortProcessingException(e);
        }
    }
}
