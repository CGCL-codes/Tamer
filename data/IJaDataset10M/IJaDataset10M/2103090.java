package forms;

import org.apache.struts.action.ActionForm;

/**
 * Form used to turn on and off antispam service. 
 * @author marcin
 *
 */
public class UseSpamChangeForm extends ActionForm {

    public boolean useSpam;

    /**
     * @return Returns the useSpam.
     */
    public boolean getUseSpam() {
        return useSpam;
    }

    /**
     * @param useSpam The useSpam to set.
     */
    public void setUseSpam(boolean useSpam) {
        this.useSpam = useSpam;
    }

    public void reset() {
        this.useSpam = false;
    }
}
