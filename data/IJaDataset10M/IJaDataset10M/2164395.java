package org.zkoss.zktest.test;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 * Test code for GenericForwardComposer.
 * Enter "xxx" in textbox, show "You entered: xxx" in label.
 * @author henrichen
 *
 */
public class MyForwardComposer extends GenericForwardComposer {

    private Textbox mytextbox;

    private Label mylabel;

    public void setMylabel(Label lb) {
        mylabel = lb;
    }

    public void onChange$mytextbox() {
        mylabel.setValue("You entered: " + mytextbox.getValue());
        mytextbox.focus();
    }
}
