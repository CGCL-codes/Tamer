package pt.ips.estsetubal.mig.academicCloud.shared.dto.view.administrator;

import java.io.Serializable;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.SchoolDTO;

/**
 * This class is the DTO that stores the information required to initialize the
 * <code>CreateSchoolView</code>.
 * 
 * @see pt.ips.estsetubal.mig.academicCloud.client.module.administrator.view.CreateSchoolView
 * @author António Casqueiro
 */
public class CreateSchoolViewInitDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * School.
	 */
    private SchoolDTO school;

    public CreateSchoolViewInitDataDTO() {
        super();
    }

    public SchoolDTO getSchool() {
        return school;
    }

    public void setSchool(SchoolDTO school) {
        this.school = school;
    }
}
