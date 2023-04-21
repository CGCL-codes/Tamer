package eu.popeye.middleware.groupmanagement.membership;

import eu.popeye.networkabstraction.communication.basic.util.PopeyeView;

/**
 * This interface must be implemented for each class that registers a 
 * MembershipListener in the PullPushAdapter. Notification of a new view 
 * is provided through the newViewAccepted method
 * 
 * @author Marcel Arrufat Arias
 */
public interface PopeyeMembershipListener {

    public void newViewAccepted(PopeyeView view);
}
