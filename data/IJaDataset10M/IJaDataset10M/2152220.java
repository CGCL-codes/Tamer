package purej.web.servlet;

import javax.servlet.http.HttpServletResponse;

/**
 * ���� ���ؽ�Ʈ
 * 
 * @author leesangboo
 * 
 */
public class ResponseContext {

    private HttpServletResponse response;

    public ResponseContext(HttpServletResponse response, RequestContext requestContext) {
        this.response = response;
    }

    public ResponseContext(HttpServletResponse response) {
        setResponse(response);
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
