package com.jettmarks.bkthn.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.jettmarks.bkthn.domain.Commuter;
import com.jettmarks.bkthn.domain.MonthYear;
import com.jettmarks.bkthn.domain.Pledge;

public class GetCommuterForm extends org.apache.struts.action.ActionForm {

    /** *  */
    private static final long serialVersionUID = -278568624552405389L;

    private String commuterName = "";

    private Commuter commuter = null;

    private Pledge currentPledge = null;

    public GetCommuterForm() {
    }

    @Override
    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
        this.commuterName = "";
    }

    @Override
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        ActionErrors actionErrors = new ActionErrors();
        return actionErrors;
    }

    /**
   * @return the commuterName
   */
    public String getCommuterName() {
        return commuterName;
    }

    /**
   * @param commuterName the commuterName to set
   */
    public void setCommuterName(String commuterName) {
        this.commuterName = commuterName;
    }

    /**
   * @return the commuter
   */
    public Commuter getCommuter() {
        return commuter;
    }

    /**
   * @param commuter the commuter to set
   */
    public void setCommuter(Commuter commuter) {
        this.commuter = commuter;
    }

    /**
   * @return the currentPledge
   */
    public Pledge getCurrentPledge() {
        MonthYear currentMonthYear = (new MonthYear().getCurrentMonthYear());
        for (Pledge pledge : commuter.getPledges()) {
            if (pledge.getMonthYear().getMonthYear().equals(currentMonthYear.getMonthYear())) {
                currentPledge = pledge;
            }
        }
        return currentPledge;
    }
}
