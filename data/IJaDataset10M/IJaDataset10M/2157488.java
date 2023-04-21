package com.liferay.portlet.google.action;

import com.google.soap.search.GoogleSearch;
import com.google.soap.search.GoogleSearchFault;
import com.google.soap.search.GoogleSearchResult;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.google.util.GoogleUtil;
import com.liferay.util.GetterUtil;
import com.liferay.util.ParamUtil;
import com.liferay.util.StringPool;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="SearchAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class SearchAction extends PortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        try {
            PortletPreferences prefs = req.getPreferences();
            String args = ParamUtil.getString(req, "args");
            int start = (ParamUtil.get(req, "start", 1) - 1) * 10;
            GoogleSearch googleSearch = GoogleUtil.getGoogleSearch();
            googleSearch.setQueryString(args);
            googleSearch.setStartResult(start);
            googleSearch.setSafeSearch(GetterUtil.getBoolean(prefs.getValue("safe-search", StringPool.BLANK)));
            GoogleSearchResult searchResuklt = googleSearch.doSearch();
            req.setAttribute(WebKeys.GOOGLE_SEARCH_RESULT, searchResuklt);
            return mapping.findForward("portlet.google.search");
        } catch (GoogleSearchFault gse) {
            GoogleUtil.useNewKey();
            return mapping.findForward("portlet.google.error");
        }
    }
}
