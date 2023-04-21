package org.aigebi.rbac.action;

import java.util.HashMap;
import java.util.List;
import org.aigebi.rbac.bean.RoleBean;
import org.aigebi.rbac.bean.SeniorityBean;
import org.aigebi.search.DefaultSearchGuide;
import org.aigebi.search.ItemListSearchVisitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Ligong Xu
 * @version $Id: SenioritySearch.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class SenioritySearch extends BaseSeniorityAction {

    private static Log log = LogFactory.getLog(SenioritySearch.class);

    private DefaultSearchGuide senioritySearchGuide;

    private List seniorities;

    public SenioritySearch() {
    }

    /** for search, return dao object directly */
    public String search() throws Exception {
        try {
            List seniorities = searchSeniority(getSeniorityBean());
            saveSearch(getSeniorityBean(), seniorities);
            if (seniorities == null || seniorities.isEmpty()) {
                String[] args = { getSeniorityBean().getName() };
                addSessionMessage(getText("seniority.search.nomatch", args));
            }
            return SUCCESS;
        } catch (Throwable t) {
            log.error("error search seniority", t);
            addActionError(getText("seniority.search.error"));
            return INPUT;
        }
    }

    private List searchSeniority(SeniorityBean seniorityBean) throws Exception {
        ItemListSearchVisitor dynQuery = new ItemListSearchVisitor();
        getSenioritySearchGuide().setSearchInput(seniorityBean);
        getSenioritySearchGuide().accept(dynQuery);
        return getSeniorityManager().findSeniorities(dynQuery);
    }

    public String showAll() throws Exception {
        try {
            List seniorities = listAllSeniorities();
            saveSearch(null, seniorities);
            return SUCCESS;
        } catch (Throwable t) {
            log.error(t);
            addActionError(getText("seniority.search.error"));
            return INPUT;
        }
    }

    private List listAllSeniorities() throws Exception {
        return getSeniorityManager().allSeniorities();
    }

    /**full list or search of seniority, various ajax input*/
    public String list() throws Exception {
        String seniorityname = getAjaxRequestSeniorityName();
        if (seniorityname == null || seniorityname.trim().length() == 0) {
            setSeniorities(listAllSeniorities());
        } else {
            getSeniorityBean().setName(seniorityname);
            setSeniorities(searchSeniority(getSeniorityBean()));
        }
        return "list";
    }

    private void saveSearch(SeniorityBean seniorityBean, List result) {
        if (seniorityBean == null) {
            getSessionProfile().saveSenioritySearch(null, result);
        } else {
            HashMap<String, String> search = new HashMap<String, String>();
            search.put("name", seniorityBean.getName());
            getSessionProfile().saveSenioritySearch(search, result);
        }
    }

    private String getAjaxRequestSeniorityName() {
        return getServletRequest().getParameter("roleBean.seniorityName");
    }

    public DefaultSearchGuide getSenioritySearchGuide() {
        return senioritySearchGuide;
    }

    public void setSenioritySearchGuide(DefaultSearchGuide pSenioritySearchGuide) {
        senioritySearchGuide = pSenioritySearchGuide;
    }

    public List getSeniorities() {
        return seniorities;
    }

    public void setSeniorities(List pSeniorities) {
        seniorities = pSeniorities;
    }
}
