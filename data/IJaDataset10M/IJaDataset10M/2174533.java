package org.jabusuite.address.employee.jobassignment;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.core.utils.JbsObject;

/**
 * Stores a language for a job-application
 * @author hilwers
 */
@Entity
public class JaLanguage extends JbsObject implements Serializable {

    private static final long serialVersionUID = -331733527582557098L;

    private String name;

    private int knowledgeLevel;

    private JobAssignment jobAssignment;

    @Override
    protected void setStandardValues() {
        super.setStandardValues();
        this.setName("");
        this.setKnowledgeLevel(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(int knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    @ManyToOne
    public JobAssignment getJobAssignment() {
        return jobAssignment;
    }

    public void setJobAssignment(JobAssignment jobAssignment) {
        if ((jobAssignment != null) && (jobAssignment != this.getJobAssignment())) this.copyPermissionsFrom(jobAssignment);
        this.jobAssignment = jobAssignment;
    }

    @Override
    public void checkData(boolean doAdditionalChecks) throws EJbsObject {
        super.checkData(doAdditionalChecks);
        if ((this.getName() == null) || (this.getName().trim().equals(""))) {
            throw new EJobAssignment(EJobAssignment.ET_JALANGUAGENAME);
        }
    }
}
