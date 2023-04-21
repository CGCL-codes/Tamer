package pt.ips.estsetubal.mig.academicCloud.shared.dto.view.candidate;

import java.io.Serializable;
import java.util.ArrayList;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.DegreeDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.param.CountryDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.user.UserDTO;

/**
 * This class is the DTO that stores the information required to initialize the
 * <code>OnlineRegistrationView</code>.
 * 
 * @see pt.ips.estsetubal.mig.academicCloud.client.module.candidate.view.OnlineRegistrationView
 * @author António Casqueiro
 */
public class OnlineRegistrationViewInitDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Degree.
	 */
    private DegreeDTO degree;

    /**
	 * User.
	 */
    private UserDTO user;

    /**
	 * Countries.
	 */
    private ArrayList<CountryDTO> countries;

    public OnlineRegistrationViewInitDataDTO() {
        super();
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public DegreeDTO getDegree() {
        return degree;
    }

    public void setDegree(DegreeDTO degree) {
        this.degree = degree;
    }

    public ArrayList<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<CountryDTO> countries) {
        this.countries = countries;
    }
}
