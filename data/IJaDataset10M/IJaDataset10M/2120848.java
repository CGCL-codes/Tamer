package net.sf.webwarp.reports.ui;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import net.sf.webwarp.reports.RenderedReport;
import net.sf.webwarp.reports.RenderedReportsDAO;
import net.sf.webwarp.reports.ReportManager;
import org.apache.shale.tiger.managed.Bean;
import org.apache.shale.tiger.managed.Property;
import org.apache.shale.tiger.managed.Scope;
import org.springframework.context.MessageSource;
import net.sf.webwarp.modules.user.User;
import net.sf.webwarp.modules.user.UserDAO;
import net.sf.webwarp.modules.user.UserGroup;
import net.sf.webwarp.modules.user.ui.trinidad.UserUtils;

@Bean(name = "reports$renderedReportList", scope = Scope.REQUEST)
public final class RenderedReportListController {

    @Property("#{RenderedReportsDAO}")
    private RenderedReportsDAO renderedReportsProvider;

    @Property("#{ReportManager}")
    private ReportManager reportManager;

    @Property("#{UserDAO}")
    private UserDAO<User, UserGroup> userProvider;

    private DataModel renderedReports = null;

    public DataModel getRenderedReports() {
        MessageSource messageSource = reportManager.getMessageSource();
        if (renderedReports == null) {
            User currentUser = UserUtils.getCurrentUser();
            List<RenderedReport> beans = renderedReportsProvider.getRenderedReports(currentUser.getId(), true);
            List<RenderedReportDisplayBean> displayBeans = new ArrayList<RenderedReportDisplayBean>(beans.size());
            for (RenderedReport bean : beans) {
                displayBeans.add(new RenderedReportDisplayBean(bean, messageSource, currentUser.getLocale(), (User) userProvider.getUser(bean.getUserID())));
            }
            renderedReports = new ListDataModel(displayBeans);
        }
        return renderedReports;
    }

    public void setRenderedReportsProvider(RenderedReportsDAO renderedReportsProvider) {
        this.renderedReportsProvider = renderedReportsProvider;
    }

    public void setReportManager(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    public void setUserProvider(UserDAO<User, UserGroup> userProvider) {
        this.userProvider = userProvider;
    }
}
