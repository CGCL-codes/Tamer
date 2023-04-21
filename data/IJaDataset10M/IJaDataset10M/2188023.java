package com.redhat.gs.mrlogistics.seam.report_views;

import java.util.*;
import java.util.List;
import javax.ejb.Stateless;
import org.hibernate.Session;
import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.datamodel.*;
import org.jboss.seam.log.*;
import com.redhat.gs.mrlogistics.data.Person;
import com.redhat.gs.mrlogistics.seam.ReportView;
import com.redhat.gs.mrlogistics.reporting.reports.UserSchedule;
import com.redhat.gs.mrlogistics.reporting.formatters.*;
import com.redhat.gs.mrlogistics.util.HibernateUtil;

/**
 * Display view to the UserSchedule report
 *
 * @author mmorsi
 */
@Name("UserScheduleView")
public class UserScheduleView extends ReportView {

    @DataModel
    private List<Person> users;

    @In
    private Session session;

    @Logger
    private Log log;

    private int userId;

    private Date startDate;

    private Date endDate;

    public UserScheduleView() {
        name = "User Schedule";
        setViewTemplate("ViewUserSchedule.xhtml");
        setReportTemplate("UserSchedule");
    }

    public boolean hasAccess() {
        return true;
    }

    @Factory("users")
    public void GenerateView() {
        users = session.createQuery("select p from Person p").list();
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Generating User Schedule View" + users.size());
    }

    public String Generate() {
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Generating User Schedule " + userId);
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Generating User Schedule " + startDate.toString());
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Generating User Schedule " + endDate.toString());
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Generating User Schedule " + getReportTemplate());
        UserSchedule schedule = new UserSchedule(userId);
        schedule.Format(new SeamMultiWeekFormatter(startDate, endDate));
        return getReportTemplate();
    }

    public List<Person> getUsers() {
        return users;
    }

    public void setUsers(List<Person> users) {
        this.users = users;
    }

    /**
	 * user_id setter / getter
	 */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int uid) {
        userId = uid;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
