package com.google.code.hibernate.rest.method;

import static com.google.code.hibernate.rest.internal.InternalPreconditions.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.code.hibernate.rest.EntityManager;
import com.google.code.hibernate.rest.internal.InternalStreams;
import com.google.code.hibernate.rest.representation.Representor;

/**
 * 
 * @author wangzijian
 * 
 */
public class PutMethodProcessor extends AbstractMethodProcessor {

    public PutMethodProcessor(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected void doProcess(URIMatchMode matchMode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        checkState(matchMode == URIMatchMode.Entity, "Illegal match mode " + matchMode);
        Representor representor = findRepresentor(request);
        String representation = InternalStreams.read(request.getInputStream());
        if (logger.isInfoEnabled()) {
            logger.info("Request -> " + representation);
        }
        String uri = request.getPathInfo();
        Map<String, String> variables = matchMode.template().match(uri);
        String entityName = variables.get("entityName");
        String id = variables.get("id");
        Object entity = representor.fromRepresentation(representation);
        entityManager.setId(entity, id);
        entityManager.update(entityName, entity);
        response.setStatus(HttpURLConnection.HTTP_NO_CONTENT);
    }
}
