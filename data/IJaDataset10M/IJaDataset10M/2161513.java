package org.mitre.rt.client.ui.recommendations;

import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.mitre.rt.client.core.MetaManager;
import org.mitre.rt.client.ui.tables.*;
import org.mitre.rt.client.ui.application.listview.RecommendationShowPanel;
import org.mitre.rt.client.xml.RecommendationHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.ApplicationType.Recommendations;
import org.mitre.rt.rtclient.RecommendationType;
import org.mitre.rt.rtclient.UserType;

/**
 *
 * @author BWORRELL
 */
public class RecommendationsTableModel extends AbsTableItemModel<RecommendationTableItem> {

    private static final Logger logger = Logger.getLogger(RecommendationsTableModel.class.getPackage().getName());

    public static final int ID = 0, TITLE = 1, GROUP = 2, ASSIGNED_TO = 3, CREATED_BY = 4, LAST_MODIFIED = 5, CATEGORY = 6, STATUS = 7;

    private int recommendationFilter = -1;

    private final RecommendationHelper recommendationHelper = new RecommendationHelper();

    public RecommendationsTableModel() {
        super();
        this.createHeaders();
        this.processData();
    }

    public RecommendationsTableModel(ApplicationType app) {
        super(app);
        this.createHeaders();
        this.processData();
    }

    public RecommendationsTableModel(ApplicationType parent, List<RecommendationTableItem> data) {
        super(parent, data);
        this.createHeaders();
        this.processData();
    }

    public int getViewFilter() {
        return this.recommendationFilter;
    }

    @Override
    protected final void createHeaders() {
        String[] headers = new String[] { "ID", "Title", "Group", "Assigned To", "Created By", "Last Modified", "Category", "Status" };
        super.setColumnHeaders(headers);
    }

    public void setData(ApplicationType app) {
        if (this.recommendationFilter == -1) this.recommendationFilter = RecommendationShowPanel.ACTIVE;
        super.setParent(app);
        this.processData();
    }

    public void setData(ApplicationType app, int filter) {
        this.recommendationFilter = filter;
        super.setParent(app);
        this.processData();
    }

    private List<RecommendationType> getRecommendationList() {
        List<RecommendationType> recommendations = null;
        ApplicationType application = (ApplicationType) super.getParent();
        UserType me = MetaManager.getAuthenticatedUser();
        switch(this.recommendationFilter) {
            case RecommendationShowPanel.ALL:
                recommendations = recommendationHelper.getAllRecommendations(application);
                break;
            case RecommendationShowPanel.ACTIVE:
                recommendations = recommendationHelper.getActiveRecommendations(application);
                break;
            case RecommendationShowPanel.ASSIGNED_TO_ME:
                recommendations = recommendationHelper.getRecommendationsAssignedToUser(application, me);
                break;
            case RecommendationShowPanel.NOTEWORTHY:
                recommendations = recommendationHelper.getNoteworthyRecommendations(application);
                break;
            case RecommendationShowPanel.WARNING:
                recommendations = recommendationHelper.getWarningRecommendations(application);
                break;
            case RecommendationShowPanel.DELETED:
                recommendations = recommendationHelper.getDeletedRecommendations(application);
                break;
            case RecommendationShowPanel.TOUCHED:
                recommendations = recommendationHelper.getTouchedRecommendations(application);
                break;
            default:
                recommendations = Collections.emptyList();
                break;
        }
        return recommendations;
    }

    private void processData() {
        AbsTableItemManager manager = new AbsTableItemManager();
        ApplicationType app = (ApplicationType) super.getParent();
        List<RecommendationType> tableRecs = this.getRecommendationList();
        List<RecommendationTableItem> tableItems = manager.getRecommendationTableItems(app, tableRecs);
        this.setData(tableItems);
    }
}
