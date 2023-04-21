package dk.simonvogensen.uirecorder.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.Event;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * @author $LastChangedBy:$ $LastChangedDate:$
 * @version $Revision:$
 */
public class RecordableButton extends Button implements RecordableWidget {

    public RecordableButton(String html, ClickHandler clickHandler) {
        super(html, clickHandler);
        addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (Utils.recordConfig == Utils.RecordConfig.RECORD) {
                    Utils.addRecordItem(RecordableButton.this, event.getRelativeElement(), event);
                    System.out.println("recorded:" + event.getRelativeElement().getId());
                }
            }
        });
    }

    public String generateId() {
        return Utils.generateId(this);
    }

    public String getInputToGeneratedId() {
        return getText();
    }
}
