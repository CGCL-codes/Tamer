package de.fuh.xpairtise.plugin.ui.trims.role;

import de.fuh.xpairtise.common.SessionRole;

/**
 * An interface for the MVC model of the role display/change trim. 
 */
public interface IRoleTrimModel {

    /**
   * Returns the current session role of the local user. The possible
   * session roles are: driver, navigator, spectator or none, if the user
   * is not in a session.
   * 
   * @return the current session role.
   */
    public SessionRole getSessionRole();

    /**
   * Sets the current session role of the local user. 
   * 
   * @param sessionRole the current session role.
   */
    public void setSessionRole(SessionRole sessionRole);

    /**
   * Returns true if the local user has requested a role change.
   * 
   * @return local request state.
   */
    public boolean getRequestStateSelf();

    /**
   * Returns true if the other main participant of a session has requested 
   * a role change. A user is a main particpant, if his current session role
   * is either driver or navigator.
   * 
   * @return remote request state.
   */
    public boolean getRequestStateOther();

    /**
   * Sets the state of a role change request of the other main participant 
   * of a session. 
   * 
   * @param requestStateOther state of the role change request.
   */
    public void setRequestStateOther(boolean requestStateOther);

    /**
   * Sets the state of a role change request of the local user. Only 
   * if the local user is a main participant of a session a role change 
   * request is possible.
   *  
   * @param requestStateSelf state of the role change request.
   */
    public void setRequestStateSelf(boolean requestStateSelf);

    /**
   * Returns if the local control is enabled or not. An enabled local control 
   * means, that the local user is a main participant of a session. 
   * 
   * @return state of the local control.
   */
    public boolean isLocalControlEnabled();

    /**
   * Sets the state of the local control. The local control is the button to 
   * request a role change.
   * 
   * @param isMainParticipant state of the local control
   */
    public void setLocalControlEnabled(boolean isMainParticipant);
}
