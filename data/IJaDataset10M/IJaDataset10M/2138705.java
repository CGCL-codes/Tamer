package pub.tairtags;

import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class UrlEncodeTag extends BodyTagSupport {

    public int doAfterBody() throws JspException {
        BodyContent body = getBodyContent();
        String baseURL = body.getString();
        body.clearBody();
        try {
            String encodedURL = java.net.URLEncoder.encode(baseURL, "utf-8");
            getPreviousOut().print(encodedURL);
        } catch (IOException e) {
            throw new JspTagException("I/O exception " + e.getMessage());
        }
        return SKIP_BODY;
    }
}
