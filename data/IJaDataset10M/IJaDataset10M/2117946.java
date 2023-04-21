package com.centraview.contacts.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.centraview.common.CVUtility;
import com.centraview.common.Globals;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.customfield.CustomField;
import com.centraview.settings.Settings;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public final class ViewEntityHandler extends Action {

    private static Logger logger = Logger.getLogger(ViewEntityHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        String[] rowId = null;
        String viewEntityId = "";
        HttpSession session = request.getSession();
        UserObject user = (UserObject) session.getAttribute("userobject");
        int individualId = user.getIndividualID();
        EntityVO entityVO = new EntityVO();
        if (request.getParameterValues("rowId") != null) {
            String[] selectedRowId = request.getParameterValues("rowId");
            StringTokenizer parseSelectedId = new StringTokenizer(selectedRowId[0], ",");
            int countOfRowId = parseSelectedId.countTokens();
            rowId = new String[countOfRowId];
            int i = 0;
            while (parseSelectedId.hasMoreTokens()) {
                rowId[i] = parseSelectedId.nextToken().toString();
                i++;
            }
            if (rowId.length > 0) {
                viewEntityId = rowId[0];
            }
        } else {
            viewEntityId = new String(request.getParameter("entityLinkId"));
        }
        ContactFacade contactFacade = null;
        try {
            contactFacade = (ContactFacade) CVUtility.setupEJB("ContactFacade", "com.centraview.contact.contactfacade.ContactFacadeHome", dataSource);
        } catch (Exception e) {
            logger.error("[execute] Exception thrown.", e);
            throw new ServletException(e);
        }
        entityVO = contactFacade.getEntity(Integer.parseInt(viewEntityId));
        DynaActionForm entityForm = (DynaActionForm) form;
        entityVO.populateFormBean(entityForm);
        CustomField customField = null;
        try {
            customField = (CustomField) CVUtility.setupEJB("CustomField", "com.centraview.customfield.CustomFieldHome", dataSource);
        } catch (Exception e) {
            logger.error("[execute] Exception thrown.", e);
            throw new ServletException(e);
        }
        TreeMap customFieldMap = customField.getCustomFieldData("Entity", entityVO.getContactID());
        Collection customFieldValues = customFieldMap.values();
        int arraySize = customFieldValues.size() > 4 ? 4 : customFieldValues.size();
        CustomFieldVO[] fieldArray = new CustomFieldVO[arraySize];
        Iterator i = customFieldValues.iterator();
        int count = 0;
        while (i.hasNext() && count < 4) {
            fieldArray[count++] = (CustomFieldVO) i.next();
        }
        entityForm.set("customFields", fieldArray);
        request.setAttribute("recordType", "Entity");
        request.setAttribute("recordName", entityVO.getName());
        request.setAttribute("recordId", viewEntityId);
        request.setAttribute("dynamicTitle", entityVO.getName());
        request.setAttribute("parentId", new Integer(entityVO.getList()));
        request.setAttribute("parentName", "");
        request.setAttribute("marketingList", new Integer(entityVO.getList()));
        if (request.getParameter("copyTo") != null) {
            return mapping.findForward(".view.contact.copyto");
        }
        entityForm.set("mocTypeList", Globals.MOC_TYPE);
        int rpp = 15;
        String currentParam = request.getParameter("current");
        int current = 1;
        try {
            current = Integer.valueOf(currentParam).intValue();
        } catch (Exception e) {
        }
        String selectedIds = request.getParameter("rowId");
        ValueListParameters listParameters = new ValueListParameters(ValueListConstants.ENTITY_LIST_TYPE, rpp, current);
        String filter = "SELECT entity.entityId FROM entity WHERE entityId IN (" + selectedIds + ")";
        listParameters.setFilter(filter);
        ArrayList columns = new ArrayList();
        FieldDescriptor nameField = (FieldDescriptor) ValueListConstants.entityViewMap.get("Name");
        listParameters.setSortColumn(nameField.getQueryIndex());
        listParameters.setSortDirection("ASC");
        columns.add(nameField);
        listParameters.setColumns(columns);
        ValueList valueList = null;
        try {
            valueList = (ValueList) CVUtility.setupEJB("ValueList", "com.centraview.valuelist.ValueListHome", dataSource);
        } catch (Exception e) {
            logger.error("[execute] Exception thrown.", e);
            throw new ServletException(e);
        }
        ValueListVO listObject = valueList.getValueList(individualId, listParameters);
        int total = listObject.getParameters().getTotalRecords();
        long totalPages = (long) Math.ceil((double) total / rpp);
        int previous = current - 1 > 0 ? current - 1 : 1;
        int next = current + 1 < total ? current + 1 : total;
        request.setAttribute("total", new Long(totalPages));
        request.setAttribute("current", new Integer(current));
        request.setAttribute("previous", new Integer(previous));
        request.setAttribute("next", new Integer(next));
        session.setAttribute("selectedContacts", listObject);
        session.setAttribute("selectedIds", selectedIds);
        if (request.getParameter("closeWindow") != null) {
            request.setAttribute("closeWindow", request.getParameter("closeWindow"));
        }
        return mapping.findForward(".view.contact.entitydetails");
    }
}
