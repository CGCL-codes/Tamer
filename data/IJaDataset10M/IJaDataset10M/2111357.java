package presentation.com.sampleprj.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import persistence.com.sampleprj.common.boundary.SampleBoundaryOptionObject;
import presentation.com.sampleprj.common.form.SimpleTagSampleForm;
import com.eis.ds.core.log.DSLogger;
import com.eis.ds.core.log.LoggerFactory;

/**
 * Description: This is a sample action to get or put data on simpletagsample.jsp.
 * 
 * @author zhong wen qing
 * @version 1.0
 */
public class SimpleTagSampleAction extends Action {

    private static DSLogger log = LoggerFactory.getInstance().getLogger(SimpleTagSampleAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        SimpleTagSampleForm sampleForm = (SimpleTagSampleForm) form;
        ActionMessages errors = new ActionMessages();
        ActionMessages messages = new ActionMessages();
        try {
            String action = sampleForm.getAction();
            String pageName = sampleForm.getPageName();
            log.debug("From action : " + action);
            log.debug("From page : " + pageName);
            if (action.equalsIgnoreCase("fromHome")) {
                req.setAttribute("countryList", getSampleCountryList());
                sampleForm.setSelectedCountry("2");
                MessageResources messageResources = getResources(req, "common");
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("common.message.help"));
                messages.add("message1", new ActionMessage("common.message.dynamic", "fillingString"));
                errors.add("error1", new ActionMessage("common.error.UnknownError"));
                errors.add("error2", new ActionMessage("common.error.dynamic", "fillingString"));
                saveMessages(req, messages);
                saveErrors(req, errors);
                return mapping.findForward("gotoSimpleTagSample");
            } else if (action.equalsIgnoreCase("getValue") && pageName.equalsIgnoreCase("simpletagsample")) {
                HashMap hm = new HashMap();
                hm.put("name", sampleForm.getName());
                hm.put("address", sampleForm.getAddress());
                hm.put("selectedCountry", sampleForm.getSelectedCountry());
                hm.put("selectedMultiCountry", sampleForm.getSelectedMultiCountry());
                hm.put("simpleCheckBox", sampleForm.getSimpleCheckBox());
                hm.put("simpleRadio", sampleForm.getSimpleRadio());
                req.setAttribute("countryList", getSampleCountryList());
                req.setAttribute("getValues", hm);
                return mapping.findForward("gotoSimpleTagSample");
            }
        } catch (Exception ex) {
            log.error(ex);
            ActionMessage error = new ActionMessage("common.error.UnknownError");
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(req, errors);
        }
        return mapping.findForward("table_sample");
    }

    /**
     * This method executes a dummy database operation
     * 
     * @return a list of boundary data object that represents the coutries
     */
    private List getSampleCountryList() {
        List nameList = new ArrayList();
        for (int i = 0; i < 6; i++) {
            SampleBoundaryOptionObject sboo = new SampleBoundaryOptionObject();
            sboo.setId(String.valueOf(i));
            sboo.setName("country" + i);
            nameList.add(sboo);
        }
        return nameList;
    }
}
