package onepoint.project.modules.custom_attribute;

import onepoint.persistence.OpBroker;
import onepoint.persistence.OpSiteObject;
import onepoint.project.OpProjectSession;

/**
 * @author dfreis
 *
 */
public class OpCustomTextValue extends OpSiteObject implements OpCustomValue {

    private String memo;

    /**
    * 
    */
    public OpCustomTextValue() {
    }

    /**
    * @param memo2
    */
    public OpCustomTextValue(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Object clone(OpProjectSession session, OpBroker broker) {
        return new OpCustomTextValue(getMemo());
    }
}
