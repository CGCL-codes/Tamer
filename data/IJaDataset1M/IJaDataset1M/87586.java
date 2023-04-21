package org.kablink.teaming.remoting.rest.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.kablink.teaming.rest.v1.model.ErrorInfo;
import org.kablink.util.api.ApiErrorCode;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

@Provider
public class HibernateOptimisticLockingFailureMapper implements ExceptionMapper<HibernateOptimisticLockingFailureException> {

    public Response toResponse(HibernateOptimisticLockingFailureException ex) {
        return Response.status(Response.Status.CONFLICT).entity(new ErrorInfo(ApiErrorCode.OPTIMISTIC_LOCKING_FAILURE.name(), ex.getMessage())).build();
    }
}
