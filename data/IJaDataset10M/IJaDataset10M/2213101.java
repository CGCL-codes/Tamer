package uk.gov.dti.og.fox.ex;

import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.ex.ExRoot;

public class ExGeneral extends ExRoot {

    static String TYPE = "General Error";

    public ExGeneral(String msg) {
        super(msg, TYPE, null, null);
    }

    public ExGeneral(String msg, DOM xml) {
        super(msg, TYPE, xml, null);
    }

    public ExGeneral(String msg, Throwable e) {
        super(msg, TYPE, null, e);
    }

    public ExGeneral(String msg, DOM xml, Throwable e) {
        super(msg, TYPE, xml, e);
    }

    public ExGeneral(String msg, String type, DOM xml, Throwable exception) {
        super(msg, type, xml, exception);
    }
}
