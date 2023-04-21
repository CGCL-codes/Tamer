package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;

public class ExportToExcel extends ActivityType {

    public ExportToExcel(Activity act) {
        super(act);
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 6577567526874137106L;

    public String getOnClickFunction() {
        return "ev_submit('" + act.getId() + "')";
    }

    public String getDefaultClass() {
        return VIEW_BUTTON_CLASS;
    }

    public String getButtonId() {
        return VIEW_BUTTON_ID;
    }

    public String getAfterAction() {
        return VIEW_NAMESPACE + "/displayView.action";
    }

    public String getBackAction() {
        return VIEW_NAMESPACE + "/displayView.action";
    }

    public String getBeforeAction() {
        return act.getExcelName() != null ? VIEW_NAMESPACE + "/expDocToExcel.action?filename=" + act.getExcelName() : VIEW_NAMESPACE + "/expDocToExcel.action?filename=" + act.getId();
    }

    public String getDefaultOnClass() {
        return DOCUMENT_BUTTON_ON_CLASS;
    }
}
