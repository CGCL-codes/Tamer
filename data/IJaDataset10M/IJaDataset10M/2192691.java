package ces.platform.system.ui.resource.form;

import org.apache.struts.action.ActionForm;

public class ObjTypeForm extends ActionForm {

    private String strObjName;

    private String strObjId;

    public String getTextObjId() {
        return strObjId;
    }

    public String getTextObjName() {
        return strObjName;
    }

    public void setTextObjId(String strObjId) {
        this.strObjId = strObjId;
    }

    public void setTextObjName(String strObjName) {
        this.strObjName = strObjName;
    }
}
