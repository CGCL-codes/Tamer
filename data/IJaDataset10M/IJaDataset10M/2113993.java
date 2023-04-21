package com.centraview.administration.merge;

import java.io.IOException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
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
import org.apache.struts.validator.DynaValidatorForm;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

/**
 * The merge-search form will forward here when the form is finally submitted.
 * This class will Squeeze the form-bean into the SearchCriteriaVO and submit it
 * to the EJB.  The EJB in turn will hand us the results.  Which we can display the first
 * set, and a summary of the other sets.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 * 
 */
public class MergeSearchResults extends Action {

    private static Logger logger = Logger.getLogger(MergeSearchResults.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        DynaValidatorForm mergeSearchForm = (DynaValidatorForm) form;
        SearchCriteriaVO searchCriteria = this.populateSearchCriteria(mergeSearchForm);
        MergeSearchResultVO searchResult = null;
        MergeHome mergeHome = (MergeHome) CVUtility.getHomeObject("com.centraview.administration.merge.MergeHome", "Merge");
        try {
            Merge remoteMerge = mergeHome.create();
            remoteMerge.setDataSource(dataSource);
            searchResult = remoteMerge.performSearch(searchCriteria);
        } catch (Exception e) {
            logger.error("[Exception] MergeSearchResults.Execute Handler ", e);
            throw new ServletException(e);
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("mergeSearchResult", searchResult);
        return mapping.findForward(".view.administration.merge_search_results");
    }

    private SearchCriteriaVO populateSearchCriteria(DynaActionForm mergeSearchForm) {
        SearchCriteriaVO searchCriteria = new SearchCriteriaVO();
        int mergeType = Integer.parseInt((String) mergeSearchForm.get("mergeType"));
        searchCriteria.setType(mergeType);
        int searchDomain = Integer.parseInt((String) mergeSearchForm.get("searchDomain"));
        searchCriteria.setSearchDomain(searchDomain);
        int threshhold = Integer.parseInt((String) mergeSearchForm.get("threshhold"));
        searchCriteria.setThreshhold(threshhold);
        SearchCriteriaLine[] lines = (SearchCriteriaLine[]) mergeSearchForm.get("criteriaLine");
        for (int i = 0; i < lines.length; i++) {
            SearchCriteriaLine criterion = lines[i];
            int fieldIndex = Integer.parseInt(criterion.getFieldIndex());
            int searchType = Integer.parseInt(criterion.getSearchTypeIndex());
            int score = Integer.parseInt(criterion.getMatchValue());
            searchCriteria.addFieldCriterion(fieldIndex, searchType, score);
        }
        return searchCriteria;
    }
}
