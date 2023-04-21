package project.cn.dataType;

import java.util.Calendar;
import java.util.Date;

public class DPlan extends Data {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8786223673847416496L;

    private String subject;

    private Date dateStart;

    private Date dateEnd;

    private String place;

    private int state;

    private int feature1;

    private int feature2;

    private int week;

    private int planid;

    private String context;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
        Calendar ca = Calendar.getInstance();
        ca.setTime(dateStart);
        this.week = ca.get(Calendar.WEEK_OF_MONTH);
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFeature1() {
        return feature1;
    }

    public void setFeature1(int feature1) {
        this.feature1 = feature1;
    }

    public int getFeature2() {
        return feature2;
    }

    public void setFeature2(int feature2) {
        this.feature2 = feature2;
    }

    public int getWeek() {
        return week;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setPlanid(int planid) {
        this.planid = planid;
    }

    public int getPlanid() {
        return planid;
    }
}
