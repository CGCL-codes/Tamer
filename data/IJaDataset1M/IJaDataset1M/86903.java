package gov.ca.modeling.timeseries.map.server.data.persistence;

import gov.ca.modeling.server.utils.GenericDAO;
import gov.ca.modeling.timeseries.map.shared.data.UserProfileData;
import java.util.List;

public interface UserProfileDataDAO extends GenericDAO<UserProfileData> {

    /**
	 * Returns the user's profile for given email
	 * 
	 * @param email
	 * @return
	 */
    public UserProfileData getUserForEmail(String email);

    /**
	 * Returns the list of all user profiles
	 * 
	 * @return
	 */
    public List<UserProfileData> findAll();
}
