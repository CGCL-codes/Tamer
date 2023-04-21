package pub.tairtags;

import pub.utils.StringUtils;
import java.io.IOException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class ReplaceTag extends BodyTagSupport {

    private String from;

    private String to;

    public int doAfterBody() throws JspException {
        BodyContent body_content = getBodyContent();
        String body_text = body_content.getString();
        String replaced_body = replaceBody(body_text);
        try {
            body_content.clearBody();
            getPreviousOut().print(replaced_body);
        } catch (IOException e) {
            ;
        }
        return EVAL_BODY_INCLUDE;
    }

    private String replaceBody(String body_text) {
        return StringUtils.replace(body_text, this.from, this.to);
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
