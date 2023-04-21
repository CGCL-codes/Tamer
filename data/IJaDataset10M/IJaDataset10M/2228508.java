package org.wijiscommons.ssaf.drop_off.restServiceImpl;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.w3c.dom.Element;
import org.wijiscommons.ssaf.ServiceResponseInfo;
import org.wijiscommons.ssaf.drop_off.FatalFault;
import org.wijiscommons.ssaf.drop_off.util.Drop_OffUtil;
import org.wijiscommons.ssaf.drop_off.wrapper.FaultDescription;
import org.wijiscommons.ssaf.util.dom.DomUtils;
import org.wijiscommons.ssaf.util.dom.SSAFErrorTypes;

@Provider
public class CustomRestException implements ExceptionMapper<TestException> {

    public Response toResponse(TestException ex) {
        ServiceResponseInfo sb = new ServiceResponseInfo();
        sb.noteMessage("mailbox_nominal_uri attribute in incoming request " + "document is not specified properly");
        Element errorDocumentElement = Drop_OffUtil.createDropOffErrorMessage("TestRecordURI", null, null, null, SSAFErrorTypes.REQUEST_INVALID, "mailBoxName", null);
        FaultDescription faultDescription = new FaultDescription();
        faultDescription.setAny(errorDocumentElement);
        FatalFault fault = new FatalFault(sb.getMergedMessages(), faultDescription);
        return Response.status(404).entity(DomUtils.getStringFromDocument(errorDocumentElement.getOwnerDocument())).type("text/xml").build();
    }
}
