package com.patientis.client.security.prefs;

import static com.patientis.model.common.ModelReference.*;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DefaultBaseModel;
import com.patientis.model.security.ApplicationViewModel;

/**
 * One line class description
 *
 * 
 *   
 */
public class ControlPrefModel extends DefaultBaseModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int saveDefaultInd = 0;

    /**
	 * Left control list
	 */
    private ApplicationViewModel leftModel = new ApplicationViewModel();

    /**
	 * Right control list
	 */
    private ApplicationViewModel rightModel = new ApplicationViewModel();

    /**
	 * @return the leftModel
	 */
    public ApplicationViewModel getLeftModel() {
        return leftModel;
    }

    /**
	 * @param leftModel the leftModel to set
	 */
    public void setLeftModel(ApplicationViewModel leftModel) {
        this.leftModel = leftModel;
    }

    /**
	 * @return the rightModel
	 */
    public ApplicationViewModel getRightModel() {
        return rightModel;
    }

    /**
	 * @param rightModel the rightModel to set
	 */
    public void setRightModel(ApplicationViewModel rightModel) {
        this.rightModel = rightModel;
    }

    @Override
    public Object getValue(int modelRefId) {
        if (modelRefId == APPLICATIONCONTROLS_ACTIVEIND) {
            return getSaveDefaultInd();
        } else {
            return super.getValue(modelRefId);
        }
    }

    @Override
    public void setValue(int modelRefId, Object o) {
        if (modelRefId == APPLICATIONCONTROLS_ACTIVEIND) {
            setSaveDefaultInd(Converter.convertInteger(o));
        } else {
            super.setValue(modelRefId, o);
        }
    }

    /**
     * Active indicator 1=Active 0=Inactive
     */
    public void setSaveDefaultInd(int activeInd) {
        if (!(this.saveDefaultInd == activeInd)) {
            int oldactiveInd = 1;
            oldactiveInd = this.saveDefaultInd;
            this.saveDefaultInd = activeInd;
            setModified("activeInd");
            firePropertyChange(String.valueOf(APPLICATIONCONTROLS_ACTIVEIND), oldactiveInd, activeInd);
        }
    }

    /**
     * Active indicator 1=Active 0=Inactive
     */
    public int getSaveDefaultInd() {
        return this.saveDefaultInd;
    }
}
