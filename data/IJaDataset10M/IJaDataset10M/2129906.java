package org.libreplan.ws.common.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.libreplan.business.common.exceptions.InstanceNotFoundException;
import org.libreplan.ws.common.api.InternalErrorDTO;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for <code>InstanceNotFoundException</code>.
 *
 * @author Fernando Bellas Permuy <fbellas@udc.es>
 */
@Provider
@Component("instanceNotFoundExceptionMapper")
public class InstanceNotFoundExceptionMapper implements ExceptionMapper<InstanceNotFoundException> {

    public Response toResponse(InstanceNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(new InternalErrorDTO(e.getMessage(), Util.getStackTrace(e))).type("application/xml").build();
    }
}
