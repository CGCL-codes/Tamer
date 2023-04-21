package jreceiver.client.common;

import jreceiver.common.JRecException;
import jreceiver.common.rec.security.User;
import jreceiver.common.rpc.RpcException;
import jreceiver.common.rpc.RpcFactory;
import jreceiver.common.rpc.Scanner;

/**
 * A bean to provide dereferenced and localized scanner information
 * for the JReceiver file scanner.
 * <p>
 * Designed for use by JSP/Struts but can be used elsewhere.
 * <p>
 * Typically used with application scope, as there's an
 * ongoing accumulation of status information from scanner.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:08 $
 */
public class ScannerStatusBean extends BaseSessionBean {

    /**
    * return the state of the scanner   TODO: I18N
    */
    public synchronized String getState() throws RpcException {
        return (scan_rpc != null ? scan_rpc.getState() : "unknown");
    }

    /**
    * return the next scheduled run of the scanner
    */
    public synchronized long getSchedule() throws RpcException {
        return (scan_rpc != null ? scan_rpc.getSchedule().getTime() : 0L);
    }

    /**
     * initialize with user credentials
     */
    protected synchronized void onReset(User user) throws JRecException {
        if (user != null) scan_rpc = RpcFactory.newScanner(user); else scan_rpc = null;
    }

    private Scanner scan_rpc;
}
