package siouxsie.mvc;

import java.util.Map;
import siouxsie.mvc.impl.viewvalues.MapViewValues;

public class SubmitAction extends ActionController {

    @Override
    public ActionForward execute(ActionMapping mapping, Map parameters) throws Exception {
        parameters.put("name", parameters.get("name"));
        return mapping.findForward("success");
    }
}
