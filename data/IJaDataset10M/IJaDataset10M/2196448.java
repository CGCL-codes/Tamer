package edu.mit.wi.omnigene.omnitide.request.das;

import java.rmi.*;
import java.util.*;
import java.text.*;
import edu.mit.wi.omnigene.omnibus.*;
import edu.mit.wi.omnigene.omnidas.*;
import edu.mit.wi.omnigene.util.*;
import edu.mit.wi.omnigene.omnitide.response.das.*;
import edu.mit.wi.omnigene.omnitide.request.*;
import edu.mit.wi.omnigene.omnitide.response.*;

/**
 * GetDSNRequest.java
 *
 *
 * @author  Rajesh Kuttan
 */
public class GetDSNRequest extends OmnigeneRequest {

    private DASDSNRequest clientDASDSNRequest = null;

    /**
     * Empty Constructor, use <CODE>setClientRequest</CODE>
     * function to pass client request
     */
    public GetDSNRequest() {
        super();
    }

    /**
     * Contructor to pass client <CODE>DSNRequest</CODE>
     * @param clientDASDSNRequest This carrys client request information
     */
    public GetDSNRequest(DASRequest clientDASDSNRequest) {
        this.clientDASDSNRequest = (DASDSNRequest) clientDASDSNRequest;
    }

    /**
     * Use this function, if empty constructor
     * @param clientDASDSNRequest Client passed request information,
     * through this object
     */
    public void setClientRequest(DASRequest clientDASDSNRequest) {
        this.clientDASDSNRequest = (DASDSNRequest) clientDASDSNRequest;
    }

    /**
     * Response String is wrapped in
     * <CODE>OmnigeneResponse</CODE> object
     *
     * @return OmnigeneResponse
     * @throws OmnigeneException OmnigeneException
     */
    public OmnigeneResponse execute() throws OmnigeneException {
        OmnigeneResponse dsnResponse = null;
        try {
            if (clientDASDSNRequest == null) throw new OmnigeneException("Client DASRequest not set!");
            edu.mit.wi.omnigene.omnibus.DASQuery ds = BeanReference.getDASEJB();
            dsnResponse = ds.getDSN(clientDASDSNRequest);
        } catch (RemoteException omniEx) {
            System.out.println("GetDSNRequest(execute): Error " + omniEx.getMessage());
            omniEx.printStackTrace();
            throw new OmnigeneException(omniEx.getMessage());
        } catch (Exception ex) {
            System.out.println("GetDSNRequest(execute): Error " + ex.getMessage());
            ex.printStackTrace();
            throw new OmnigeneException(ex.getMessage());
        }
        return dsnResponse;
    }
}
