package com.shine.sourceflow.web.show;

import java.util.List;
import com.shine.DBUtil.model.DBModel;
import com.shine.sourceflow.model.show.SessionTrafficDto;
import com.shine.sourceflow.service.show.SessionTrafficService;

public class SessionTrafficAction extends ShowGenericAction {

    private static final long serialVersionUID = -7257832792447006718L;

    public SessionTrafficAction() {
        this.service = new SessionTrafficService();
        this.dto = new SessionTrafficDto();
    }

    @Override
    protected void generateCharts() {
        StringBuffer cvsStr = new StringBuffer();
        DBModel dbModel = dbModels.get(DATA_DEFAULT);
        for (int i = 0; i < dbModel.size(); i++) {
            cvsStr.append(dbModel.get(i).getString("session_alias"));
            cvsStr.append(";");
            cvsStr.append(dbModel.get(i).getString("src_session_total"));
            cvsStr.append(";");
            cvsStr.append(dbModel.get(i).getString("dst_session_total"));
            cvsStr.append("\\n");
        }
        this.charts.put(DATA_DEFAULT, cvsStr.toString());
    }

    @Override
    protected String getPDFTitle() {
        return null;
    }

    @Override
    protected List<String> getTableDatas() {
        return null;
    }

    @Override
    protected String getTableTitles() {
        return null;
    }
}
