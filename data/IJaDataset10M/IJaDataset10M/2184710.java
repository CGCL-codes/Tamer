package org.njo.webapp.root.action.dts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.njo.webapp.root.action.GenericAction;
import org.njo.webapp.root.model.activity.DTSActivity;
import org.njo.webapp.root.utility.ObjectExistsException;

/**
 * TODO:comment
 *
 * @author yu.peng
 * @version 0.01
 */
public class SaveFieldSetAction extends GenericAction {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws Exception if the application business logic throws
     *                   an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DTSActivity dtsactivity = new DTSActivity();
        FieldSetForm fieldSetForm = (FieldSetForm) form;
        try {
            Map yamlObject = new HashMap();
            yamlObject.put("field_set_physical_name", fieldSetForm.getField_set_physical_name());
            yamlObject.put("field_set_logic_name", fieldSetForm.getField_set_logic_name());
            yamlObject.put("field_set_description", fieldSetForm.getField_set_description());
            yamlObject.put("fields", fieldSetForm.getFields());
            dtsactivity.saveFieldSet(fieldSetForm.getField_set_physical_name(), yamlObject, fieldSetForm.getField_set_description());
        } catch (ObjectExistsException e) {
            addMessage(request, new ActionMessage("dts.field.new.error.alreadyexist"));
            return (new ActionForward("/processor.prenewfieldset.tiles", false));
        }
        return (new ActionForward("/processor.listfieldset.tiles", false));
    }
}
