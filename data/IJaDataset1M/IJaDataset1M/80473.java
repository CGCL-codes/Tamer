package dk.simonvogensen.uirecorder.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author $LastChangedBy:$ $LastChangedDate:$
 * @version $Revision:$
 */
public class RecordableHorizontalPanel extends HorizontalPanel {

    @Override
    public void add(Widget widget) {
        super.add(widget);
        Utils.add(this, widget);
    }
}
