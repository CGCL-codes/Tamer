package org.nodevision.portal.struts.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nodevision.portal.om.searchconfig.Search;
import org.nodevision.portal.om.searchconfig.Searches;
import org.nodevision.portal.repositories.RepositorySearch;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

public class SearchDisplay extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SchedulerFactory schedFactory = new org.quartz.impl.StdSchedulerFactory();
        Scheduler scheduler = schedFactory.getScheduler();
        Searches searches = RepositorySearch.getInstance(getServlet().getServletContext()).getSearches();
        ArrayList list = new ArrayList();
        ArrayList searchesArray = searches.getSearches();
        ArrayList jobNames = new ArrayList(Arrays.asList(scheduler.getJobNames("searchGroup")));
        if ("delete".equalsIgnoreCase(request.getParameter("action"))) {
            String delSearch = request.getParameter("search");
            ActionErrors errors = new ActionErrors();
            for (int i = 0; i < searchesArray.size(); i++) {
                Search search = (Search) searchesArray.get(i);
                String name = search.getName();
                if (name.equalsIgnoreCase(delSearch)) {
                    try {
                        searchesArray.remove(i);
                        searches.setSearches(searchesArray);
                        RepositorySearch.getInstance(getServlet().getServletContext()).saveSearches(searches);
                        break;
                    } catch (Exception e) {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("search.error", e.toString()));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    }
                }
            }
        }
        for (int i = 0; i < searchesArray.size(); i++) {
            Search search = (Search) searchesArray.get(i);
            String name = search.getName();
            String path = search.getPath();
            String start = search.getStart();
            String type = search.getType();
            String cron = search.getCron();
            HashMap temp = new HashMap();
            temp.put("name", name);
            temp.put("path", path);
            temp.put("start", start);
            temp.put("type", type);
            temp.put("cron", cron);
            if (jobNames.contains(search.getName())) {
                temp.put("running", "true");
            } else {
                temp.put("running", "false");
            }
            list.add(temp);
        }
        request.setAttribute("searches", list);
        return mapping.getInputForward();
    }
}
