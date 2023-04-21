package jreceiver.j2me.common.rpc;

/**
 * Basic queries to a remote server, or direct if server
 * is in same VM.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:07 $
 */
public interface RpcBase {

    public static final String DETECT = "detect";

    /**
     * returns true if server is detected and active
     *
     * @return
     */
    public boolean detect();
}
