package rm.m7print.client.me.ctrls;

import rm.gwt.appspace.client.ctrl.AbstractLabelCtrl;
import rm.m7print.client.admin.ctrls.Person;

public class HomePhoneCtrl extends AbstractLabelCtrl<Person> {

    @Override
    protected void doSetParamValue(Person t) {
        display.setText(t.phHome);
    }

    @Override
    protected String getParamText() {
        return null;
    }

    public String getCaption() {
        return "Home";
    }

    public void getParamValue(Person t) {
    }
}
