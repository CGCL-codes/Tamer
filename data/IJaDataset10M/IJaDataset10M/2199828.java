package pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice;

import java.io.Serializable;
import java.util.ArrayList;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.CompetenceCourseDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.DepartmentDTO;

/**
 * This class is the DTO that stores the information required to initialize the
 * <code>CreateCompetenceCourseView</code>.
 * 
 * @see pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.view.CreateCompetenceCourseView
 * @author António Casqueiro
 */
public class CreateCompetenceCourseViewInitDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Competence course.
	 */
    private CompetenceCourseDTO competenceCourse;

    /**
	 * Departments.
	 */
    private ArrayList<DepartmentDTO> departments;

    public CreateCompetenceCourseViewInitDataDTO() {
        super();
    }

    public void setCompetenceCourse(CompetenceCourseDTO competenceCourse) {
        this.competenceCourse = competenceCourse;
    }

    public CompetenceCourseDTO getCompetenceCourse() {
        return competenceCourse;
    }

    public void setDepartments(ArrayList<DepartmentDTO> departments) {
        this.departments = departments;
    }

    public ArrayList<DepartmentDTO> getDepartments() {
        return departments;
    }
}
