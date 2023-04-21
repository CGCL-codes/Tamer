package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;

public class SaveBack extends ActivityType {

    public SaveBack(Activity act) {
        super(act);
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 5855273834096647642L;

    public String getOnClickFunction() {
        return "ev_action('" + act.getType() + "', '" + act.getId() + "')";
    }

    public String getDefaultClass() {
        return DOCUMENT_BUTTON_CLASS;
    }

    public String getButtonId() {
        return DOCUMENT_BUTTON_ID;
    }

    public String getAfterAction() {
        return BASE_ACTION;
    }

    public String getBackAction() {
        return DOCUMENT_NAMESPACE + "/content.jsp";
    }

    public String getBeforeAction() {
        return DOCUMENT_NAMESPACE + "/saveback.action";
    }

    public String getDefaultOnClass() {
        return DOCUMENT_BUTTON_ON_CLASS;
    }
}
