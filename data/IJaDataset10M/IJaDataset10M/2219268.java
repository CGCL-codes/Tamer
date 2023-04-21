package com.infineon.dns.action;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.infineon.dns.form.AttributeForm;
import com.infineon.dns.model.Attribute;
import com.infineon.dns.service.AttributeService;
import com.infineon.dns.util.Locator;
import com.infineon.dns.util.PagedListAndTotalCount;

public class AttributeAction extends BaseAction {

    public ActionForward listAttributes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            AttributeService attributeService = Locator.lookupService(AttributeService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PagedListAndTotalCount<Attribute> map = attributeService.getAttributes(request.getParameter("sort"), request.getParameter("dir"), request.getParameter("start"), request.getParameter("limit"));
            StringBuffer json = new StringBuffer("{totalCount:" + map.getTotalCount() + ",attributes:[");
            for (Attribute attribute : map.getPagedList()) {
                json.append("{'attributeId':'" + attribute.getAttributeId() + "','attributeName':'" + StringEscapeUtils.escapeHtml(attribute.getAttributeName()).replace("\\", "\\\\").replace("'", "\\'").replace("/", "\\/") + "','attributeNameText':'" + StringEscapeUtils.escapeJavaScript(attribute.getAttributeName()) + "','attributeCode':'" + attribute.getAttributeCode() + "','attributeRemark':'" + StringEscapeUtils.escapeHtml(attribute.getAttributeRemark()).replace("\r\n", "<br>").replace("\\", "\\\\").replace("'", "\\'").replace("/", "\\/") + "','attributeRemarkText':'" + StringEscapeUtils.escapeJavaScript(attribute.getAttributeRemark()) + "'},");
            }
            if (map.getTotalCount() != 0) {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("]}");
            response.getWriter().write(json.toString());
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.findForward("");
        }
    }

    public ActionForward createAttribute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            AttributeForm attributeForm = (AttributeForm) form;
            Attribute attribute = new Attribute();
            AttributeService attributeService = Locator.lookupService(AttributeService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            if (attributeService.getAttributeByAttributeName(attributeForm.getAttributeName()).size() > 0) {
                response.getWriter().write("{success:false,message:'Attribute name: " + attributeForm.getAttributeName() + " already existed'}");
                return mapping.findForward("");
            }
            if (attributeService.getAttributeByAttributeCode(attributeForm.getAttributeCode()).size() > 0) {
                response.getWriter().write("{success:false,message:'Attribute code: " + attributeForm.getAttributeCode() + " already existed'}");
                return mapping.findForward("");
            }
            attribute.setAttributeName(attributeForm.getAttributeName());
            attribute.setAttributeCode(attributeForm.getAttributeCode());
            attribute.setAttributeRemark(attributeForm.getAttributeRemark());
            attributeService.insertAttribute(attribute);
            response.getWriter().write("{success:true,message:'New attribute successfully added'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }

    public ActionForward updateAttribute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            AttributeForm attributeForm = (AttributeForm) form;
            AttributeService attributeService = Locator.lookupService(AttributeService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            Attribute attribute = attributeService.getAttributeByAttributeId(attributeForm.getAttributeId(), true);
            if (attribute == null) {
                response.getWriter().write("{success:true,message:'This attribute information has already been deleted'}");
                return mapping.findForward("");
            }
            List<Attribute> attributeList = attributeService.getAttributeByAttributeName(attributeForm.getAttributeName());
            if (attributeList.size() > 0 && attributeList.get(0).getAttributeId() != attributeForm.getAttributeId()) {
                response.getWriter().write("{success:false,message:'Attribute name: " + attributeForm.getAttributeName() + " already existed'}");
                return mapping.findForward("");
            }
            attributeList = attributeService.getAttributeByAttributeCode(attributeForm.getAttributeCode());
            if (attributeList.size() > 0 && attributeList.get(0).getAttributeId() != attributeForm.getAttributeId()) {
                response.getWriter().write("{success:false,message:'Attribute code: " + attributeForm.getAttributeCode() + " already existed'}");
                return mapping.findForward("");
            }
            attribute.setAttributeName(attributeForm.getAttributeName());
            attribute.setAttributeCode(attributeForm.getAttributeCode());
            attribute.setAttributeRemark(attributeForm.getAttributeRemark());
            attributeService.updateAttribute(attribute);
            response.getWriter().write("{success:true,message:'Modify attribute information successfully'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }

    public ActionForward deleteAttribute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            AttributeForm attributeForm = (AttributeForm) form;
            AttributeService attributeService = Locator.lookupService(AttributeService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            Attribute attribute = attributeService.getAttributeByAttributeId(attributeForm.getAttributeId(), true);
            if (attribute == null) {
                response.getWriter().write("{success:true,message:'This attribute information has already been deleted'}");
                return mapping.findForward("");
            }
            if (attribute.getDocuments().size() != 0) {
                response.getWriter().write("{success:true,message:'This attribute information has been attached to some document numbers, it can not be deleted'}");
                return mapping.findForward("");
            }
            attributeService.deleteAttribute(attributeForm.getAttributeId());
            response.getWriter().write("{success:true,message:'Successfully delete attribute information'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
}
