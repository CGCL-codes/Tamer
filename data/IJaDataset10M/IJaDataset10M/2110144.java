package wsl.mdn.html;

import java.util.Vector;
import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.ecs.wml.*;
import wsl.fw.servlet.*;
import wsl.fw.util.Util;
import wsl.fw.resource.ResId;
import wsl.fw.wml.WEUtil;
import wsl.fw.datasource.*;
import wsl.mdn.dataview.*;
import wsl.mdn.guiconfig.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author
 * @version 1.0
 */
public class QueryResultDelegate extends MdnHtmlServletDelegate {

    /**
     * Default ctor
     */
    public QueryResultDelegate() {
    }

    /**
     * Called by servlet
     */
    public void run() throws ServletException, IOException {
        try {
            Util.argCheckNull(_request);
            Util.argCheckNull(_response);
            String strIndex = _request.getParameter(MdnHtmlServlet.PV_RECORD_INDEX);
            if (strIndex != null && strIndex.length() > 0) {
                int index = Integer.parseInt(strIndex);
                PagedSelectDelegate psd = (PagedSelectDelegate) getUserState().getCurrentPagedQuery();
                Record rec = psd.getRecord(index);
                if (rec != null) {
                    MenuAction ma = getUserState().getCurrentMenu();
                    if (ma != null) {
                        assert ma instanceof QueryRecords;
                        if (ma.getChildren() != null && ma.getChildren().size() > 0) {
                            QueryRecords subq = (QueryRecords) ma.getChildren().elementAt(0);
                            getUserState().setMenu(subq.getId());
                            delegate(new QueryRecordsDelegate(rec));
                        } else {
                            delegate(new ShowRecordDelegate(rec, strIndex));
                        }
                    }
                }
            }
        } catch (Exception e) {
            onError(e);
        }
    }
}
