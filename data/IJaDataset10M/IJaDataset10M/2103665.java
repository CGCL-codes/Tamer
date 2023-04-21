package org.az.tb.services.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(BeanNamesConst.PDF_SERVICE)
public interface PdfService extends RemoteService {

    public abstract Boolean isCertificateEligible(long examResultsId);
}
