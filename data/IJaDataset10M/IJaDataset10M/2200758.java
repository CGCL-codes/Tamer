package uk.ac.ebi.rhea.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import uk.ac.ebi.rhea.ws.core.exception.RheaWsException;

/**
 *
 *
 * @author <a href="mailto:hongcao@ebi.ac.uk">Hong Cao</a>
 * @since 19-10-2010
 */
public class RheaWsExceptionResponder implements ExceptionMapper<RheaWsException> {

    public Response toResponse(RheaWsException rheaWsException) {
        Response exceptionResponse = Response.status(rheaWsException.getHtmlErrorCode()).entity(rheaWsException.getMessage()).type(rheaWsException.getMediaType()).build();
        return exceptionResponse;
    }
}
