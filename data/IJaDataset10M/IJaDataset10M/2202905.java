package net.kano.joustsim.oscar.oscar.service.icbm.ft;

import net.kano.joscar.rv.RvSession;
import net.kano.joscar.rvcmd.RvConnectionInfo;

public interface RvSessionConnectionInfo {

    RvSession getRvSession();

    RvRequestMaker getRequestMaker();

    void setConnectionInfo(RvConnectionInfo connInfo);

    RvConnectionInfo getConnectionInfo();

    int getRequestIndex();

    /**
   * Returns the new request index
   */
    int increaseRequestIndex();

    void setRequestIndex(int requestIndex);

    void setInitiator(Initiator initiator);

    Initiator getInitiator();

    boolean buddyAccepted();

    void setBuddyAccepted(boolean accepted);
}
