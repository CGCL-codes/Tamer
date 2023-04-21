package gov.nih.niaid.bcbb.nexplorer3.client;

import java.util.Iterator;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractImagePanel extends AbsolutePanel implements LoadHandler {

    Image img = new Image();

    protected Event.NativePreviewHandler preventDefaultMouseEvents = new Event.NativePreviewHandler() {

        public void onPreviewNativeEvent(NativePreviewEvent event) {
            if (event.getTypeInt() == Event.ONMOUSEDOWN || event.getTypeInt() == Event.ONMOUSEMOVE) {
                event.getNativeEvent().preventDefault();
            }
        }
    };

    public AbstractImagePanel() {
        img.addLoadHandler(this);
        add(img, 0, 0);
    }

    public abstract void update(ViewDataClient vd);

    public void initializeArea() {
        Iterator<Widget> it = iterator();
        while (it.hasNext()) {
            Widget w = it.next();
            if (w != null && !(w instanceof Image)) it.remove();
        }
    }
}
