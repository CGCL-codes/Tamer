package com.centraview.administration.globalreplace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import com.centraview.advancedsearch.AdvancedSearch;
import com.centraview.advancedsearch.AdvancedSearchHome;
import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

/**
 * @author Naresh Patel <npatel@centraview.com>
 */
public class GlobalReplaceSearchHandler extends Action {

    private static Logger logger = Logger.getLogger(GlobalReplaceSearchHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        ActionErrors allErrors = new ActionErrors();
        HttpSession session = request.getSession(true);
        String returnStatus = ".view.administration.global_replace.search";
        UserObject userObject = (UserObject) session.getAttribute("userobject");
        int individualId = userObject.getIndividualID();
        DynaActionForm globalReplaceForm = (DynaActionForm) form;
        String searchIdParameter = (String) request.getAttribute("searchId");
        String selectedSearchId = searchIdParameter != null ? searchIdParameter : "0";
        try {
            String replaceTableIDString = (String) request.getAttribute("replaceTableID");
            int replaceTableID = 0;
            int moduleId = 14;
            if (replaceTableIDString != null) {
                StringTokenizer tableInfo = new StringTokenizer(replaceTableIDString, "#");
                String tableIDString = null;
                String primaryTableName = null;
                if (tableInfo != null) {
                    while (tableInfo.hasMoreTokens()) {
                        tableIDString = (String) tableInfo.nextToken();
                        primaryTableName = (String) tableInfo.nextToken();
                    }
                    if (tableIDString != null && primaryTableName != null && !tableIDString.equals("") && !primaryTableName.equals("")) {
                        int tableID = Integer.parseInt(tableIDString);
                        String moduleIdString = AdvancedSearchUtil.getModuleId(primaryTableName, dataSource);
                        if (moduleIdString != null) {
                            moduleId = Integer.parseInt(moduleIdString);
                        }
                    }
                }
            }
            globalReplaceForm.set("moduleId", new Integer(moduleId));
            ContactFacadeHome cfh = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
            try {
                ContactFacade remote = (ContactFacade) cfh.create();
                remote.setDataSource(dataSource);
                Vector listVec = remote.getDBList(individualId);
                globalReplaceForm.set("listVec", listVec);
            } catch (Exception e) {
                logger.error("[Exception] GlobalReplaceSearchHandler.Execute Handler ", e);
            }
            GlobalReplace globalReplace = null;
            GlobalReplaceHome globalReplaceHome = (GlobalReplaceHome) CVUtility.getHomeObject("com.centraview.administration.globalreplace.GlobalReplaceHome", "GlobalReplace");
            try {
                globalReplace = globalReplaceHome.create();
                globalReplace.setDataSource(dataSource);
            } catch (Exception e) {
                logger.error("[Exception] GlobalReplaceSearchHandler.Execute Handler ", e);
            }
            Vector primaryReplaceTableList = globalReplace.getPrimaryReplaceTables();
            globalReplaceForm.set("replaceTableVec", primaryReplaceTableList);
            AdvancedSearch remoteAdvancedSearch = null;
            AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome) CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
            try {
                remoteAdvancedSearch = advancedSearchHome.create();
                remoteAdvancedSearch.setDataSource(dataSource);
            } catch (Exception e) {
                logger.error("[Exception] GlobalReplaceSearchHandler.Execute Handler ", e);
            }
            HashMap savedSearchMap = remoteAdvancedSearch.getSavedSearchList(individualId, moduleId);
            Vector savedSearchVec = new Vector();
            if (savedSearchMap != null) {
                Set savedSearchIds = savedSearchMap.keySet();
                Iterator idIterator = savedSearchIds.iterator();
                while (idIterator.hasNext()) {
                    Number searchId = (Number) idIterator.next();
                    String searchName = (String) savedSearchMap.get(searchId);
                    savedSearchVec.add(new DDNameValue(searchId.toString(), searchName));
                }
            }
            globalReplaceForm.set("savedSearchVec", savedSearchVec);
            HashMap allFields = (HashMap) globalReplaceForm.get("allFields");
            if (allFields == null || allFields.isEmpty()) {
                HashMap tableMap = remoteAdvancedSearch.getSearchTablesForModule(individualId, moduleId);
                HashMap conditionMap = SearchVO.getConditionOptions();
                ArrayList tableList = AdvancedSearchUtil.buildSelectOptionList(tableMap);
                ArrayList conditionList = AdvancedSearchUtil.buildSelectOptionList(conditionMap);
                globalReplaceForm.set("tableList", tableList);
                globalReplaceForm.set("conditionList", conditionList);
                TreeSet keySet = new TreeSet(tableMap.keySet());
                Iterator keyIterator = keySet.iterator();
                allFields = new HashMap();
                while (keyIterator.hasNext()) {
                    Number key = (Number) keyIterator.next();
                    HashMap tableFields = remoteAdvancedSearch.getSearchFieldsForTable(individualId, key.intValue(), moduleId);
                    ArrayList tableFieldList = AdvancedSearchUtil.buildSelectOptionList(tableFields);
                    allFields.put(key, tableFieldList);
                }
                globalReplaceForm.set("allFields", allFields);
            }
            SearchCriteriaVO[] searchCriteria = (SearchCriteriaVO[]) globalReplaceForm.get("searchCriteria");
            String addRow = (String) globalReplaceForm.get("addRow");
            if (addRow.equals("true")) {
                searchCriteria = AdvancedSearchUtil.addRow(searchCriteria);
                globalReplaceForm.set("addRow", "false");
            }
            String removeRow = (String) globalReplaceForm.get("removeRow");
            if (!removeRow.equals("false")) {
                searchCriteria = AdvancedSearchUtil.removeRow(searchCriteria, removeRow);
                globalReplaceForm.set("removeRow", "false");
            }
            globalReplaceForm.set("searchCriteria", searchCriteria);
            String actionType = (String) globalReplaceForm.get("actionType");
            if (actionType != null && actionType.equals("Fields")) {
                returnStatus = ".forward.administration.global_replace.fields";
                String searchType = (String) globalReplaceForm.get("searchType");
                if (searchType != null && searchType.equals("New Search")) {
                    if (searchCriteria.length == 1) {
                        SearchCriteriaVO searchCriteriaVO = searchCriteria[0];
                        String tableID = searchCriteriaVO.getTableID();
                        String fieldID = searchCriteriaVO.getFieldID();
                        String conditionID = searchCriteriaVO.getConditionID();
                        if (tableID != null && tableID.equals("0")) {
                            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Record Type"));
                        }
                        if (fieldID != null && fieldID.equals("0")) {
                            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Field"));
                        }
                        if (conditionID != null && conditionID.equals("0")) {
                            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Condition"));
                        }
                        if (!allErrors.isEmpty()) {
                            saveErrors(request, allErrors);
                            returnStatus = ".view.administration.global_replace.search";
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[Exception] GlobalReplaceSearchHandler.Execute Handler ", e);
        }
        return (mapping.findForward(returnStatus));
    }
}
