package net.sf.dhwt.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang.StringUtils;
import net.sf.dhwt.*;
import net.sf.dhwt.exception.ExceptionConstants;
import net.sf.dhwt.manager.*;

/**
 *  This tag is used to paint the CSS and JS file of a dhwt component.
 *
 * @author     Huijing Sheng
 * @version    2.0 2003-11-29
 *
 *   @jsp.tag
 *       name="env"
 *       body-content="empty"
 */
public class EnvTag extends TagSupport implements ExceptionConstants {

    public static final String CSS_TYPE = "css";

    public static final String JS_TYPE = "js";

    private String type;

    private String name;

    /**
     *  processing of the start tag
     *
     * @return
     * @exception  JspException
     */
    public int doStartTag() throws JspException {
        Component component = null;
        Container container = null;
        try {
            component = (Component) ComponentManager.getManager().get(name);
            container = (Container) ContainerManager.getManager().get(name);
        } catch (Exception e) {
        }
        if (component == null && container == null) {
            throw new JspException(COMPONENT_NOT_EXIST);
        }
        try {
            if (component != null) {
                paintEnv(component);
            }
            if (container != null) {
                paintEnv(container);
            }
        } catch (IOException ioe) {
            throw new JspException(PAINT_ERROR);
        }
        return SKIP_BODY;
    }

    /**
     *  Sets the name attribute of the EnvTag object
     *
     * @jsp.attribute
     *   required="true"
     *   rtexprvalue="true"
     */
    public void setName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(NAME_CAN_NOT_BE_NULL);
        }
        this.name = name;
    }

    /**
     *  Sets the type attribute of the EnvTag object
     *
     * @jsp.attribute
     *   required="false"
     *   rtexprvalue="true"
     */
    public void setType(String type) {
        if (StringUtils.isEmpty(type) || !(StringUtils.equals(type, CSS_TYPE) || StringUtils.equals(type, JS_TYPE))) {
            throw new IllegalArgumentException();
        }
        this.type = type;
    }

    private void paintEnv(Component component) throws IOException {
        JspWriter out = pageContext.getOut();
        if (StringUtils.equals(type, CSS_TYPE)) {
            out.print("<link href=\"" + component.getCssFile() + "\" rel=\"stylesheet\" type=\"text/css\">");
        } else {
            out.print("<script language=\"JavaScript\" src=\"" + component.getJsFile() + "\"></script>");
        }
    }

    /**  release attributes  */
    public void release() {
        name = null;
        type = null;
    }
}
