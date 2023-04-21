package pt.ips.estsetubal.mig.academicCloud.shared.dto.view.academicOffice;

import java.io.Serializable;
import java.util.ArrayList;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.param.CountryDTO;

/**
 * This class is the DTO that stores the information required to initialize the
 * <code>ManualRegistrationView</code>.
 * 
 * @see pt.ips.estsetubal.mig.academicCloud.client.module.academicOffice.view.ManualRegistrationView
 * @author António Casqueiro
 */
public class ManualRegistrationViewInitDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Student information.
	 */
    private StudentInfoDTO studentInfo;

    private ArrayList<CountryDTO> countries;

    public ManualRegistrationViewInitDataDTO() {
        super();
    }

    public ArrayList<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<CountryDTO> countries) {
        this.countries = countries;
    }

    public void setStudentInfo(StudentInfoDTO studentInfo) {
        this.studentInfo = studentInfo;
    }

    public StudentInfoDTO getStudentInfo() {
        return studentInfo;
    }
}
