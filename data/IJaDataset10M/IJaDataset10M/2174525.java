package org.onemind.swingweb.client.gwt.ui;

import org.onemind.swingweb.client.core.AbstractClient;
import org.onemind.swingweb.client.dom.DomNode;
import org.onemind.swingweb.client.gwt.GwtClient;
import org.onemind.swingweb.client.gwt.widget.*;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;

public class FileDialogUIHandler extends DialogUIHandler implements ChooserListener {

    public FileDialogUIHandler(AbstractClient client) {
        super(client);
    }

    /** 
     * {@inheritDoc}
     */
    protected Object updateUI(Object com, DomNode element) {
        FileDialog dialog = (FileDialog) com;
        String title = element.getAttribute("title");
        if (title != null) {
            dialog.setTitle(title);
        }
        return super.updateUI(com, element);
    }

    protected void handleChildren(AbstractClient rootHandler, IContainer c, DomNode element) {
    }

    /** 
     * {@inheritDoc}
     */
    public void frameClosed(Widget sender) {
        handleEvent(sender, "close");
    }

    /** 
     * {@inheritDoc}
     */
    public void frameMaximized(Widget sender) {
        handleEvent(sender, "maximize");
    }

    /** 
     * {@inheritDoc}
     */
    public void frameMinimized(Widget sender) {
        handleEvent(sender, "minimize");
    }

    public void postEvent(Object source, Object data) {
        postEvent(source, data, true);
    }

    /** 
     * {@inheritDoc}
     */
    protected void registerListeners(Object com) {
        ((FileDialog) com).getChooser().addChooserListener(this);
        super.registerListeners(com);
    }

    /** 
     * {@inheritDoc}
     */
    protected Object createComponentInstance(Object parent, DomNode element) {
        String id = element.getAttribute("id");
        FileDialog w = new FileDialog(id);
        return w;
    }

    public void chooseCancel(Widget w) {
        ((GwtClient) getClient()).submitRequest((FileChooser) w);
    }

    public void chooseOk(Widget w) {
        ((GwtClient) getClient()).submitRequest((FileChooser) w);
    }
}
