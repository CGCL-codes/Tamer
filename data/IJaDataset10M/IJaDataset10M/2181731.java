package org.itsnat.impl.core.response.html;

import org.itsnat.impl.core.clientdoc.ClientAJAXDocumentImpl;
import org.itsnat.impl.core.request.RequestNormalLoadDocImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseNormalLoadHTMLDocOperaMobile8Impl extends ResponseNormalLoadHTMLDocImpl {

    public ResponseNormalLoadHTMLDocOperaMobile8Impl(RequestNormalLoadDocImpl request) {
        super(request);
    }

    public void processAJAXDocumentLoad() {
        super.processAJAXDocumentLoad();
        StringBuffer code = new StringBuffer();
        code.append("var listener = function () ");
        code.append("{");
        code.append("  document.body.onclick=function () { window.location.reload(true); }; ");
        code.append("};");
        code.append("window.addEventListener('unload',listener,false);");
        ClientAJAXDocumentImpl clientDoc = getClientAJAXDocument();
        clientDoc.addCodeToSend(code);
    }
}
