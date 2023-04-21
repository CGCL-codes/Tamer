package salto.fwk.mvc.taglib.security;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import salto.fwk.exception.TechniqueException;
import salto.fwk.security.HabilitationManager;
import salto.fwk.security.HabilitationManagerFactory;

public class SecurityTag extends BodyTagSupport {

    private String mode;

    private String task;

    private static Pattern pattern = Pattern.compile("(?i)<\\s*(input|select|a|textarea|img)[^>]*>");

    public int doStartTag() throws JspException {
        if ("none".equalsIgnoreCase(mode)) return SKIP_BODY;
        return super.doStartTag();
    }

    public int doAfterBody() throws JspException {
        BodyContent body = getBodyContent();
        if (body == null) return super.doAfterBody();
        JspWriter writer = body.getEnclosingWriter();
        String bodyString = body.getString();
        if (bodyString != null) {
            try {
                if (task != null) {
                    try {
                        HabilitationManager hm = HabilitationManagerFactory.getHabilitationManager();
                        char tmp = hm.getAccessRigths((HttpServletRequest) pageContext.getRequest(), task);
                        switch(tmp) {
                            case HabilitationManager.READ_ONLY:
                                writer.print(setReadOnly(bodyString));
                                break;
                            case HabilitationManager.ALL:
                                writer.print(bodyString);
                                return SKIP_BODY;
                            case HabilitationManager.NONE:
                                return SKIP_BODY;
                        }
                    } catch (TechniqueException e) {
                        throw new JspException(e);
                    }
                } else {
                    String mode = this.mode;
                    if ("read-only".equalsIgnoreCase(mode)) writer.print(setReadOnly(bodyString));
                    if ("read-write".equalsIgnoreCase(mode)) {
                        writer.print(bodyString);
                        return SKIP_BODY;
                    }
                    if ("none".equalsIgnoreCase(mode)) return SKIP_BODY;
                }
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
        return super.doAfterBody();
    }

    private String setReadOnly(String bodyString) {
        Matcher m = pattern.matcher(bodyString);
        StringBuffer sb = new StringBuffer(bodyString);
        int pad = 0;
        String toAdd = " disabled=\"disabled\"";
        String toAdd2 = "<a>";
        int l = toAdd.length();
        while (m.find()) {
            if ("a".equalsIgnoreCase(m.group(1))) {
                sb.replace(m.start(0) + pad, m.end(0) + pad, toAdd2);
                pad += (toAdd2.length() - m.group(0).length());
            } else {
                sb.insert(m.start(1) + m.group(1).length() + pad, toAdd);
                pad += l;
            }
        }
        return sb.toString();
    }

    public void cleanup() {
        this.mode = null;
        this.task = null;
    }

    public void release() {
        cleanup();
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
