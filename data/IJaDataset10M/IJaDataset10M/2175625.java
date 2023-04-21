package com.shine.sourceflow.web.show;

import java.util.List;
import com.shine.DBUtil.model.DBModel;
import com.shine.sourceflow.model.show.IPTrafficDto;
import com.shine.sourceflow.service.show.IPTrafficService;

/**
 * IP流量
 */
public class IPTrafficAction extends ShowGenericAction {

    private static final long serialVersionUID = 1730268592434335250L;

    public static final String IP_SRC = "ipSrc";

    public static final String IP_DST = "ipDst";

    public IPTrafficAction() {
        this.dto = new IPTrafficDto();
        this.service = new IPTrafficService();
    }

    @Override
    protected void generateCharts() {
        StringBuffer cvsStrSrc = new StringBuffer();
        DBModel dbModelSrc = dbModels.get(IP_SRC);
        for (int i = 0; i < dbModelSrc.size(); i++) {
            cvsStrSrc.append(dbModelSrc.get(i).getString("src_ip"));
            cvsStrSrc.append(";");
            cvsStrSrc.append(dbModelSrc.get(i).getString("format_bytes_total"));
            cvsStrSrc.append("\\n");
        }
        this.charts.put(IP_SRC, cvsStrSrc.toString());
        StringBuffer cvsStrDst = new StringBuffer();
        DBModel dbModelDst = dbModels.get(IP_DST);
        for (int i = 0; i < dbModelDst.size(); i++) {
            cvsStrDst.append(dbModelDst.get(i).getString("dst_ip"));
            cvsStrDst.append(";");
            cvsStrDst.append(dbModelDst.get(i).getString("format_bytes_total"));
            cvsStrDst.append("\\n");
        }
        this.charts.put(IP_DST, cvsStrDst.toString());
    }

    @Override
    protected List<String> getTableDatas() {
        return null;
    }

    @Override
    protected String getTableTitles() {
        return null;
    }

    @Override
    protected String getPDFTitle() {
        return null;
    }
}
