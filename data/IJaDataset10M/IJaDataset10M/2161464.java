package edu.mit.wi.omnigene.omnitide.request.das;

import java.rmi.*;
import java.util.*;
import java.net.*;
import edu.mit.wi.omnigene.omnibus.*;
import edu.mit.wi.omnigene.util.*;
import edu.mit.wi.omnigene.omnidas.*;
import edu.mit.wi.omnigene.omnitide.response.das.*;
import edu.mit.wi.omnigene.omnitide.request.*;
import edu.mit.wi.omnigene.omnitide.response.*;

/**
 * Class to handle client's
 * <CODE>DASEntryPointRequest</CODE>
 *
 *
 * @author Rajesh Kuttan
 */
public class GetEntryPointRequest extends OmnigeneRequest {

    private DASEntryPointRequest entryPointRequest;

    /**
     * Empty Constructor
     */
    public GetEntryPointRequest() {
        super();
    }

    /**
     * Create object and pass client side request
     * @param entryPointRequest Client <CODE>DASEntryPointRequest</CODE>
     */
    public GetEntryPointRequest(DASRequest entryPointRequest) {
        this.entryPointRequest = (DASEntryPointRequest) entryPointRequest;
    }

    /**
     * set <CODE>DASEntryPointRequest</CODE>
     * @param entryPointRequest <CODE>DASEntryPointRequest</CODE>
     */
    public void setClientRequest(DASRequest entryPointRequest) {
        this.entryPointRequest = (DASEntryPointRequest) entryPointRequest;
    }

    /**
     * Do Process to get response object
     * @throws OmnigeneException OmnigeneException
     * @return OmnigeneResponse
     */
    public OmnigeneResponse execute() throws OmnigeneException {
        OmnigeneResponse entryPointResponse = null;
        try {
            if (entryPointRequest == null) throw new OmnigeneException("DASEntryPointRequest not set!");
            edu.mit.wi.omnigene.omnibus.DASQuery ds = BeanReference.getDASEJB();
            entryPointResponse = ds.getEntryPoints(entryPointRequest);
        } catch (RemoteException omniEx) {
            System.out.println("GetEntryPointRequest(execute): Error " + omniEx.getMessage());
            throw new OmnigeneException(omniEx.getMessage());
        } catch (Exception ex) {
            System.out.println("GetEntryPointRequest(execute): Error " + ex.getMessage());
            throw new OmnigeneException(ex.getMessage());
        }
        return entryPointResponse;
    }

    public static void main(String args[]) {
        try {
            DSN dsn = new DSNImpl();
            dsn.setID("HG5");
            dsn.setSourceName("MITOG");
            dsn.setVersion(2);
            DASEntryPointRequestImpl requestImpl = new DASEntryPointRequestImpl();
            requestImpl.setDSN(dsn);
            requestImpl.setDASSource(new URL("http://www.omnigene.com"));
            requestImpl.setDASVersion(2.0f);
            GetEntryPointRequest getEntryPointRequest = new GetEntryPointRequest();
            getEntryPointRequest.setClientRequest(requestImpl);
            OmnigeneResponse omnigeneResponse = getEntryPointRequest.execute();
            System.out.println("EntryPointResponse " + omnigeneResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
