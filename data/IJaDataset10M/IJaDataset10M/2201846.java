package org.zkoss.zktest.bind.basic;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

@ComponentAnnotation("value:@ZKBIND(ACCESS=both,SAVE_EVENT=onEdited)")
public class Composite1 extends Div implements IdSpace {

    private static final long serialVersionUID = 1L;

    @Wire
    Textbox tb;

    @Wire
    Label lb;

    public Composite1() {
        Executions.createComponents("/bind/basic/composite1.zul", this, null);
        Selectors.wireComponents(this, this, false);
        Selectors.wireEventListeners(this, this);
    }

    public String getValue() {
        return tb.getValue();
    }

    public void setValue(String value) {
        tb.setValue(value);
        lb.setValue(value);
    }

    @Listen("onDoubleClick=#lb")
    public void doEditing() {
        tb.setVisible(true);
        lb.setVisible(false);
        tb.focus();
    }

    @Listen("onBlur=#tb")
    public void doEdited() {
        lb.setValue(tb.getValue());
        tb.setVisible(false);
        lb.setVisible(true);
        Events.postEvent("onEdited", this, null);
    }
}
