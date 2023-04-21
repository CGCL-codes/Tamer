package org.jabusuite.webclient.address.employee.jobapplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jabusuite.address.employee.jobassignment.JaEducation;
import org.jabusuite.address.employee.jobassignment.JobAssignment;
import org.jabusuite.core.utils.JbsObject;

/**
 *
 * @author hilwers
 */
public class ColEducationEntries extends ColVitaEntries {

    private static final long serialVersionUID = 1L;

    @Override
    protected JbsObject createDataObject() {
        return new JaEducation();
    }

    @Override
    protected VitaRow createVitaRow(JbsObject data) {
        return new EducationRow(this, data);
    }

    public void setEducation(List<JaEducation> education) {
        if (education != null) {
            Iterator<JaEducation> it = education.iterator();
            this.getJaEntries().clear();
            while (it.hasNext()) {
                this.getJaEntries().add(it.next());
            }
        } else this.getJaEntries().clear();
        this.setControlData();
    }

    public List<JaEducation> getEducation(JobAssignment jobAssignment) {
        List<JaEducation> education = new ArrayList<JaEducation>();
        if (this.getJaEntries() != null) {
            Iterator<JbsObject> it = this.getJaEntries(true).iterator();
            while (it.hasNext()) {
                JaEducation experience = (JaEducation) it.next();
                experience.setJobAssignment(jobAssignment);
                education.add(experience);
            }
        }
        return education;
    }
}
