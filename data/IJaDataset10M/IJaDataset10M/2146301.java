package com.jujunie.project1901;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import com.jujunie.service.log.Log;
import com.jujunie.service.web.DisplayException;
import com.jujunie.service.web.WriterDisplayHandlerXML;

public abstract class DIList<E extends SessionItem> extends DIBase {

    /** logger */
    private static final Log LOG = Log.getInstance(DIList.class);

    protected abstract SessionList<E> getSessionList();

    protected abstract WriterDisplayHandlerXML.Writer setInView(List<E> list, Set<E> selection, int totalSize);

    @Override
    protected void init() throws DisplayException {
        LOG.enter("init");
        LOG.debug("Retreiving the saved list in session");
        SessionList<E> list = this.getSessionList();
        LOG.debug("Sorting and filtering");
        String submittedSort = super.getRequest().getParameter("sort");
        LOG.debug("Requested Sort", submittedSort);
        boolean submittedAsc = "ASC".equals(super.getRequest().getParameter("dir"));
        list.sort(submittedSort, submittedAsc);
        int start = Integer.parseInt(super.getRequest().getParameter("start"));
        int limit = Integer.parseInt(super.getRequest().getParameter("limit"));
        list.processSelection(getSelections(super.getRequest()));
        if ("true".equals(super.getRequest().getParameter("invert"))) {
            LOG.debug("Invert selection requested");
            list.invertSelection();
        }
        List<E> toDisplay = list.getList().subList(start, Math.min(start + limit, list.getList().size()));
        list.setSublistDisplayed(toDisplay);
        LOG.debug("Creating view object for list ouput");
        WriterDisplayHandlerXML.Writer res = this.setInView(toDisplay, list.getSelection(), list.getList().size());
        LOG.debug("Pushing the view object as WRITER for the WriterDisplayHandler");
        super.getRequest().setAttribute(WriterDisplayHandlerXML.WRITER_KEY, res);
        LOG.exit("init");
    }

    static int[] getSelections(HttpServletRequest req) {
        try {
            String param = req.getParameter("selections");
            if (StringUtils.isNotBlank(param)) {
                JSONArray jsonSelection = new JSONArray(param);
                int[] selections = new int[jsonSelection.length()];
                for (int i = 0; i < jsonSelection.length(); i++) {
                    selections[i] = jsonSelection.getInt(i);
                }
                return selections;
            } else {
                return new int[0];
            }
        } catch (JSONException e) {
            LOG.error("Error parsing selections JSON array", e);
            return new int[0];
        }
    }
}
