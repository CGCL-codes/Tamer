package com.google.code.hibernate.rest.method;

import static com.google.code.hibernate.rest.internal.InternalPreconditions.checkState;
import java.net.HttpURLConnection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.code.hibernate.rest.EntityManager;

/**
 * 
 * @author wangzijian
 * 
 */
public class DeleteMethodProcessor extends AbstractMethodProcessor {

    public DeleteMethodProcessor(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected void doProcess(URIMatchMode matchMode, HttpServletRequest request, HttpServletResponse response) {
        checkState(matchMode == URIMatchMode.Entity, "Illegal match mode " + matchMode);
        String uri = request.getPathInfo();
        Map<String, String> variables = matchMode.template().match(uri);
        String entityName = variables.get("entityName");
        String id = variables.get("id");
        entityManager.delete(entityName, id);
        response.setStatus(HttpURLConnection.HTTP_NO_CONTENT);
    }
}
