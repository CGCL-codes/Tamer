package org.blueoxygen.papaje.jobs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.SessionCredentialsAware;
import org.blueoxygen.papaje.entity.Country;
import org.blueoxygen.papaje.entity.Employee;
import org.blueoxygen.papaje.entity.Employer;
import org.blueoxygen.papaje.entity.IndustryCategory;
import org.blueoxygen.papaje.entity.Jobs;

public class UpdateJobs extends JobsForm implements SessionCredentialsAware {

    private SessionCredentials sess;

    private String categoryId = "";

    private String employerId = "";

    private Employer employer = new Employer();

    private IndustryCategory category = new IndustryCategory();

    private List<Employer> ers = new ArrayList<Employer>();

    public String execute() {
        setCategories(getManager().getList("FROM " + IndustryCategory.class.getName() + "", null, null));
        if (getJob().getTitle() == null || "".equals(getJob().getTitle())) {
            addActionError("Jobs is Empty");
        }
        if ("".equals(getCategoryId())) {
            addActionError("Industry Category is Empty");
        }
        if (getJob().getDescription() == null || "".equals(getJob().getDescription())) {
            addActionError("Description is Empty");
        }
        if (hasErrors()) {
            return INPUT;
        }
        Jobs job;
        LogInformation logInfo;
        if ("".equals(getId())) {
            job = new Jobs();
            logInfo = new LogInformation();
            logInfo.setCreateBy(sess.getCurrentUser().getId());
            logInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
            setMsg("Add New");
        } else {
            job = (Jobs) getManager().getById(Jobs.class, getId());
            logInfo = job.getLogInformation();
            setMsg("Update");
        }
        logInfo.setActiveFlag(1);
        logInfo.setLastUpdateBy(sess.getCurrentUser().getId());
        logInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        category = (IndustryCategory) getManager().getById(IndustryCategory.class, getCategoryId());
        if ("".equals(getEmployerId())) {
            String query = "FROM " + Employer.class.getName() + " e WHERE e.user.id='" + sess.getCurrentUser().getId() + "'";
            ers = getManager().getList(query, null, null);
            if (!ers.isEmpty()) {
                setEmployer(ers.get(0));
            }
        } else {
            setEmployer((Employer) getManager().getById(Employer.class, getEmployerId()));
        }
        job.setTitle(getJob().getTitle());
        job.setCategory(category);
        job.setDescription(getJob().getDescription());
        job.setCompany(getEmployer().getCompany());
        job.setLogInformation(logInfo);
        getManager().save(job);
        return SUCCESS;
    }

    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sess = sessionCredentials;
    }

    public IndustryCategory getCategory() {
        return category;
    }

    public void setCategory(IndustryCategory category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }
}
