package ru.newton.pokertrainer.web.gwt.pokertrainermodule.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.newton.pokertrainer.web.gwt.pokertrainermodule.client.holders.Profile;
import ru.newton.pokertrainer.web.gwt.pokertrainermodule.shared.exceptions.NoPermissionException;

/**
 * @author echo
 */
public interface ProfileServiceAsync {

    void getProfile(String login, AsyncCallback<Profile> async);

    void updateProfile(Profile profile, AsyncCallback<Void> async);

    void resetPassword(String login, String oldPassword, String newPassword, final AsyncCallback<Void> async);

    void logout(final AsyncCallback<Void> async);

    void removeProfile(String login, final AsyncCallback<Void> async);

    void getProfiles(final AsyncCallback<Profile[]> async);
}
