package forms;

import org.apache.struts.action.ActionForm;

/** 
 * Form used to turn on and off auto-replier feature.
 * @author marcin
 *
 */
public class UseReplierChangeForm extends ActionForm {

    public boolean useReplier;

    /**
     * @return Returns the useRules.
     */
    public boolean getUseReplier() {
        return useReplier;
    }

    /**
     * @param useRules The useRules to set.
     */
    public void setUseReplier(boolean useRules) {
        this.useReplier = useRules;
    }

    public void reset() {
        this.useReplier = false;
    }
}
