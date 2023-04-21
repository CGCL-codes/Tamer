package com.reverttoconsole.profile.controller;

import com.reverttoconsole.profile.model.Interests;
import com.reverttoconsole.profile.model.Profile;

/**
 * Action Controller for Viewing / Updating Profiles Interests.
 * Based on DispatchAction
 * @author Priyatam Mudivarti
 *
 */
public class ProfileInterestsAction extends BaseDispatchAction {

    @Override
    public Object getComponent(Profile profile) {
        return profile.getInterests();
    }

    @Override
    public void setComponent(Profile profile) {
        profile.setInterests(new Interests());
    }
}
