package org.itsnat.impl.core.response.svg;

import org.itsnat.impl.core.response.*;
import org.itsnat.impl.core.clientdoc.ClientAJAXDocumentOwnerImpl;
import org.itsnat.impl.core.request.RequestNormalLoadDocImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseNormalLoadSVGDocImpl extends ResponseNormalLoadAJAXDocImpl {

    /**
     * Creates a new instance of ResponseNormalLoadSVGDocImpl
     */
    public ResponseNormalLoadSVGDocImpl(RequestNormalLoadDocImpl request) {
        super(request);
    }

    public ResponseLoadAJAXDocDelegateImpl createReponseDelegate() {
        return new ResponseLoadSVGDocDelegateImpl(this);
    }

    public String rewriteClientHTMLForms(ClientAJAXDocumentOwnerImpl clientDoc) {
        return "";
    }
}
