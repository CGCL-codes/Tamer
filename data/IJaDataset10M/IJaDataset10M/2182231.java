package org.onemind.swingweb.client.gwt.ui;

import org.onemind.swingweb.client.core.AbstractClient;
import org.onemind.swingweb.client.dom.DomNode;
import com.google.gwt.user.client.ui.*;

public class CheckboxUIHandler extends ComponentUIHandler implements ClickListener {

    public void onClick(Widget sender) {
        CheckBox chkbox = (CheckBox) sender;
        handleEvent(sender, String.valueOf(chkbox.isChecked()));
    }

    public CheckboxUIHandler(AbstractClient client) {
        super(client);
    }

    /** 
     * {@inheritDoc}
     */
    protected Object createComponentInstance(Object parent, DomNode element) {
        CheckBox chkbox = new CheckBox();
        return chkbox;
    }

    /** 
     * {@inheritDoc}
     */
    protected void postEvent(Object sender, Object data) {
        System.out.println("post event");
        postEvent(sender, "-value", data, false);
        postEvent(sender, "true", true);
    }

    /** 
     * {@inheritDoc}
     */
    protected Object updateUI(Object com, DomNode element) {
        CheckBox chkbox = (CheckBox) com;
        String label = (String) element.getAttribute("label");
        chkbox.setText(label);
        String state = element.getAttribute("state");
        if (state == null) {
            state = element.getAttribute("selected");
        }
        if (state.equalsIgnoreCase("true")) {
            chkbox.setChecked(true);
        } else {
            chkbox.setChecked(false);
        }
        return super.updateUI(com, element);
    }

    /** 
     * {@inheritDoc}
     */
    protected void registerListeners(Object com) {
        ((CheckBox) com).addClickListener(this);
        super.registerListeners(com);
    }
}
