package org.dcm4che.server;

import java.util.Iterator;
import org.dcm4che.net.AcceptorPolicy;
import org.dcm4che.net.AssociationListener;
import org.dcm4che.net.DcmServiceRegistry;

/**
 * @author     <a href="mailto:gunter@tiani.com">Gunter Zeilinger</a>
 * @since      June, 2002
 * @version    $Revision: 7898 $ $Date: 2008-11-03 06:15:52 -0500 (Mon, 03 Nov 2008) $
 */
public interface DcmHandler extends Server.Handler {

    /**
     *  Adds a feature to the AssociationListener attribute of the DcmHandler
     *  object
     *
     * @param  l  The feature to be added to the AssociationListener attribute
     */
    void addAssociationListener(AssociationListener l);

    /**
     *  Description of the Method
     *
     * @param  l  Description of the Parameter
     */
    void removeAssociationListener(AssociationListener l);

    Iterator associationListenerIterator();

    /**
     *  Sets the acceptorPolicy attribute of the DcmHandler object
     *
     * @param  policy  The new acceptorPolicy value
     */
    void setAcceptorPolicy(AcceptorPolicy policy);

    /**
     *  Gets the acceptorPolicy attribute of the DcmHandler object
     *
     * @return    The acceptorPolicy value
     */
    AcceptorPolicy getAcceptorPolicy();

    /**
     *  Sets the dcmServiceRegistry attribute of the DcmHandler object
     *
     * @param  services  The new dcmServiceRegistry value
     */
    void setDcmServiceRegistry(DcmServiceRegistry services);

    /**
     *  Gets the dcmServiceRegistry attribute of the DcmHandler object
     *
     * @return    The dcmServiceRegistry value
     */
    DcmServiceRegistry getDcmServiceRegistry();

    /**
     *  Getter for property rqTimeout.
     *
     * @return    Value of property rqTimeout.
     */
    int getRqTimeout();

    /**
     *  Setter for property rqTimeout.
     *
     * @param  timeout  The new rqTimeout value
     */
    void setRqTimeout(int timeout);

    /**
     *  Getter for property dimseTimeout.
     *
     * @return    Value of property dimseTimeout.
     */
    int getDimseTimeout();

    /**
     *  Setter for property dimseTimeout.
     *
     * @param  dimseTimeout  New value of property dimseTimeout.
     */
    void setDimseTimeout(int dimseTimeout);

    /**
     *  Getter for property soCloseDelay.
     *
     * @return    Value of property soCloseDelay.
     */
    int getSoCloseDelay();

    /**
     *  Setter for property soCloseDelay.
     *
     * @param  soCloseDelay  New value of property soCloseDelay.
     */
    void setSoCloseDelay(int soCloseDelay);

    /**
     *  Getter for property packPDVs.
     *
     * @return    Value of property packPDVs.
     */
    boolean isPackPDVs();

    /**
     *  Setter for property packPDVs.
     *
     * @param  packPDVs  New value of property packPDVs.
     */
    void setPackPDVs(boolean packPDVs);
}
