package edu.pw.treegrid.client.report.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import edu.pw.treegrid.client.report.events.FetchReportHierarchiesCallback;
import edu.pw.treegrid.shared.Configuration;
import edu.pw.treegrid.shared.ReportColumnDescription;

public class ReportHierarchiesDataSource extends RestDataSource {

    private String reportId;

    private Map<String, ReportColumnDescriptionClient> columnByName = new HashMap<String, ReportColumnDescriptionClient>();

    public ReportHierarchiesDataSource(String reportId, List<ReportColumnDescriptionClient> columns) {
        this.reportId = reportId;
        for (ReportColumnDescriptionClient rc : columns) {
            this.columnByName.put(rc.getName(), rc);
        }
        setID("reportHierarchies-" + reportId);
        DataSourceTextField name = new DataSourceTextField(ReportColumnDescription.NAME);
        name.setPrimaryKey(true);
        DataSourceTextField column = new DataSourceTextField(ReportColumnDescription.COLUMN);
        setFields(name, column);
        setFetchDataURL(Configuration.PREFIX + Configuration.HIERARCHIES_URL);
    }

    @Override
    protected Object transformRequest(DSRequest dsRequest) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Configuration.REPORT_ID_PARAM, reportId);
        dsRequest.setParams(params);
        return super.transformRequest(dsRequest);
    }

    public void fetchReportHierarchies(final FetchReportHierarchiesCallback callback) {
        this.fetchData(null, new DSCallback() {

            public void execute(DSResponse response, Object rawData, DSRequest request) {
                Map<String, ReportHierarchy> hierarchies = new TreeMap<String, ReportHierarchy>();
                Record[] data = response.getData();
                for (int i = 0; i < data.length; ++i) {
                    String hierarchyName = data[i].getAttribute(ReportColumnDescription.NAME);
                    String columnName = data[i].getAttribute(ReportColumnDescription.COLUMN);
                    if (!hierarchies.containsKey(hierarchyName)) {
                        ReportHierarchy h = new ReportHierarchy();
                        h.setName(hierarchyName);
                        hierarchies.put(hierarchyName, h);
                    }
                    ReportColumnDescriptionClient column = ReportHierarchiesDataSource.this.columnByName.get(columnName);
                    if (column != null) {
                        hierarchies.get(hierarchyName).addColumn(column);
                    }
                }
                ReportHierarchy h = new ReportHierarchy();
                h.setName("BRAK");
                hierarchies.put(h.getName(), h);
                callback.reportHierarchies(hierarchies.values());
            }
        });
    }
}
