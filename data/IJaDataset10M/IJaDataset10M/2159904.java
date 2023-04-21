package edu.mit.wi.omnigene.omnitide.request.service.snp;

import edu.mit.wi.omnigene.omnitide.request.*;
import edu.mit.wi.omnigene.service.snp.ejb.*;
import edu.mit.wi.omnigene.bio.*;
import edu.mit.wi.omnigene.omnidas.*;
import edu.mit.wi.omnigene.util.*;
import edu.mit.wi.omnigene.webservice.*;
import edu.mit.wi.omnigene.webservice.request.*;

/**
 *
 * @author  rajesh kuttan
 * @version 
 */
public class GetSequenceRequest extends OmnigeneRequest {

    edu.mit.wi.omnigene.webservice.request.GetSnpSequenceRequest getSequenceRequest = null;

    /** Creates new GetSequenceRequest */
    public GetSequenceRequest() {
    }

    public GetSequenceRequest(edu.mit.wi.omnigene.webservice.request.GetSnpSequenceRequest getSequenceRequest) {
        this.getSequenceRequest = getSequenceRequest;
    }

    public void setClientRequest(edu.mit.wi.omnigene.webservice.request.GetSnpSequenceRequest getSequenceRequest) {
        this.getSequenceRequest = getSequenceRequest;
    }

    public SnpSequenceHash executeRequest() throws OmnigeneException {
        SnpSequenceHash snpSequenceHash = null;
        try {
            edu.mit.wi.omnigene.service.snp.ejb.SnpQuery snpQuery = BeanReference.getSnpQueryEJB();
            snpSequenceHash = snpQuery.getSequence(getSequenceRequest);
            if (snpSequenceHash == null) throw new OmnigeneException("GetSequenceRequest:executeRequest Operation failed, null value returned for SNPs");
        } catch (Exception ex) {
            System.out.println("GetSequenceRequest:executeRequest : Error " + ex.getMessage());
            ex.printStackTrace();
            throw new OmnigeneException(ex.getMessage());
        }
        return snpSequenceHash;
    }
}
