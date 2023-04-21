package purej.web.servlet;

import javax.servlet.http.HttpServletResponse;

/**
 * ���� ���ؽ�Ʈ ���丮
 * 
 * @author leesangboo
 * 
 */
public class ResponseContextFactory {

    static ResponseContextFactory factory = new ResponseContextFactory();

    public static ResponseContextFactory getInstance() {
        return factory;
    }

    public ResponseContext createResponseContext(HttpServletResponse response, RequestContext requestContext) {
        ResponseContext res = new ResponseContext(response, requestContext);
        return res;
    }

    public ResponseContext getResponseContext(String requestType, HttpServletResponse response) {
        return new ResponseContext(response);
    }

    public ResponseContext getResponseContext(ResponseContext response) {
        return response;
    }
}
