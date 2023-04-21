package name.gauravmadan.mdg.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import name.gauravmadan.mdg.pim.Attribute;
import name.gauravmadan.mdg.pim.Class;
import name.gauravmadan.mdg.service.ModelDataService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class AttributeListController extends MultiActionController {

    private ModelDataService service;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = null;
        String parentClassId = request.getParameter("parentClassId");
        if (parentClassId != null && !"".equals(parentClassId)) {
            List<Attribute> attributeList = service.getAttributeListByClassId(new Integer(parentClassId));
            mav = new ModelAndView("attribute-list");
            mav.addObject("attributeList", attributeList);
            Class clazz = service.getClassById(new Integer(parentClassId));
            mav.addObject("parentComponentId", clazz.getParentComponentId());
        } else {
        }
        return mav;
    }

    public ModelAndView showFormNew(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = null;
        Attribute attribute = new Attribute();
        String parentClassId = request.getParameter("parentClassId");
        if (parentClassId != null && !"".equals(parentClassId)) {
            attribute.setParentClassId(new Integer(parentClassId));
            mav = new ModelAndView("attribute-form");
            mav.addObject("attribute", attribute);
        } else {
        }
        return mav;
    }

    public ModelAndView showFormEdit(HttpServletRequest request, HttpServletResponse response) {
        String selectedAttributeId = request.getParameter("selectedAttributeId");
        ModelAndView mav = new ModelAndView("attribute-form");
        Attribute attribute = null;
        if (selectedAttributeId != null && !"".equals(selectedAttributeId)) {
            attribute = service.getAttributeById(new Integer(selectedAttributeId));
        } else {
        }
        mav.addObject("attribute", attribute);
        return mav;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        String selectedAttributeId = request.getParameter("selectedAttributeId");
        if (selectedAttributeId != null && !"".equals(selectedAttributeId)) {
            service.deleteAttributeById(new Integer(selectedAttributeId));
        }
        ModelAndView mav = new ModelAndView("redirect:/attribute-list.html?method=list");
        mav.addObject("parentClassId", request.getParameter("parentClassId"));
        return mav;
    }

    public ModelDataService getService() {
        return service;
    }

    public void setService(ModelDataService service) {
        this.service = service;
    }
}
