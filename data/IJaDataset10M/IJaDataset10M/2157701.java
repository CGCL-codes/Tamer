package org.paquitosoft.namtia.vo;

import java.util.Calendar;
import javax.swing.text.Utilities;
import org.paquitosoft.namtia.common.Constants;
import org.paquitosoft.namtia.common.NamtiaUtilities;

/**
 *
 * @author telemaco
 */
public class GenericVO {

    private String identifier;

    private String state;

    /** Creates a new instance of GenericVO */
    public GenericVO() {
        this.identifier = new NamtiaUtilities().createIdentifier();
        this.state = Constants.NEW;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean equals(Object obj) {
        if (obj instanceof GenericVO && ((GenericVO) obj).getIdentifier().equals(this.getIdentifier())) {
            return true;
        }
        return false;
    }

    /**
     *  This method is used to change object's state into Constants.MODIFIED
     */
    public void update() {
        this.setState(Constants.MODIFIED);
    }

    /**
     *  This method is used to change object's state into Constants.DELETED
     */
    public void delete() {
        this.setState(Constants.DELETED);
    }
}
