package com.acv.common.model;

import com.acv.dao.profiles.model.UserProfile;

/**
 * Inteface to define the response specific behavors.
 */
public interface BusResponseObject extends BusObject<BusResponseObject> {

    UserProfile getUserProfile();

    /** Mix two response into one response.
	 * @param busResponseObject A <code>BusResponseObject</code> onject to be mixed.
	 * @return A <code>BusResponseObject</code> object after mixing.
	 */
    BusResponseObject mix(BusResponseObject busResponseObject);
}
