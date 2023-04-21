package purej.web.tag.ajax;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * Tag handler for the callout AJAX tag.
 * 
 * @author Darren Spurgeon
 * @version $Revision: 1.13 $ $Date: 2006/08/23 01:21:41 $
 */
public class AjaxCalloutTag extends TagSupport {

    /**
     * <code>serialVersionUID</code>�� ����
     */
    private static final long serialVersionUID = -5833030775680862926L;

    private String var;

    private String attachTo;

    private String baseUrl;

    private String source;

    private String sourceClass;

    private String parameters;

    private String title;

    private String overlib;

    private String postFunction;

    private String preFunction;

    private String emptyFunction;

    private String errorFunction;

    private String openEventType;

    private String closeEventType;

    public String getCloseEventType() {
        return closeEventType;
    }

    public void setCloseEventType(String closeEvent) {
        this.closeEventType = closeEvent;
    }

    public String getOpenEventType() {
        return openEventType;
    }

    public void setOpenEventType(String openEvent) {
        this.openEventType = openEvent;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getEmptyFunction() {
        return emptyFunction;
    }

    public void setEmptyFunction(String emptyFunction) {
        this.emptyFunction = emptyFunction;
    }

    public String getErrorFunction() {
        return errorFunction;
    }

    public void setErrorFunction(String errorFunction) {
        this.errorFunction = errorFunction;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getAttachTo() {
        return attachTo;
    }

    public void setAttachTo(String attachTo) {
        this.attachTo = attachTo;
    }

    public String getOverlib() {
        return overlib;
    }

    public void setOverlib(String overlib) {
        this.overlib = overlib;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getPreFunction() {
        return preFunction;
    }

    public void setPreFunction(String preFunction) {
        this.preFunction = preFunction;
    }

    public String getPostFunction() {
        return postFunction;
    }

    public void setPostFunction(String postFunction) {
        this.postFunction = postFunction;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int doStartTag() throws JspException {
        this.baseUrl = (String) ExpressionEvaluatorManager.evaluate("baseUrl", this.baseUrl, String.class, this, super.pageContext);
        this.parameters = (String) ExpressionEvaluatorManager.evaluate("parameters", this.parameters, String.class, this, super.pageContext);
        if (this.title != null) {
            this.title = (String) ExpressionEvaluatorManager.evaluate("title", this.title, String.class, this, super.pageContext);
        }
        if (this.var != null) {
            this.var = (String) ExpressionEvaluatorManager.evaluate("var", this.var, String.class, this, super.pageContext);
        }
        if (this.attachTo != null) {
            this.attachTo = (String) ExpressionEvaluatorManager.evaluate("attachTo", this.attachTo, String.class, this, super.pageContext);
        }
        if (this.source != null) {
            this.source = (String) ExpressionEvaluatorManager.evaluate("source", this.source, String.class, this, super.pageContext);
        }
        if (this.sourceClass != null) {
            this.sourceClass = (String) ExpressionEvaluatorManager.evaluate("sourceClass", this.sourceClass, String.class, this, super.pageContext);
        }
        if (this.title != null) {
            this.title = (String) ExpressionEvaluatorManager.evaluate("title", this.title, String.class, this, super.pageContext);
        }
        if (this.overlib != null) {
            this.overlib = (String) ExpressionEvaluatorManager.evaluate("overlib", this.overlib, String.class, this, super.pageContext);
        }
        if (this.postFunction != null) {
            this.postFunction = (String) ExpressionEvaluatorManager.evaluate("postFunction", this.postFunction, String.class, this, super.pageContext);
        }
        if (this.errorFunction != null) {
            this.errorFunction = (String) ExpressionEvaluatorManager.evaluate("errorFunction", this.errorFunction, String.class, this, super.pageContext);
        }
        if (this.emptyFunction != null) {
            this.emptyFunction = (String) ExpressionEvaluatorManager.evaluate("emptyFunction", this.emptyFunction, String.class, this, super.pageContext);
        }
        if (this.openEventType != null) {
            this.openEventType = (String) ExpressionEvaluatorManager.evaluate("openEvent", this.openEventType, String.class, this, super.pageContext);
        }
        if (this.closeEventType != null) {
            this.closeEventType = (String) ExpressionEvaluatorManager.evaluate("closeEvent", this.closeEventType, String.class, this, super.pageContext);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        OptionsBuilder options = new OptionsBuilder();
        if (this.source != null) {
            options.add("source", this.source, true);
        } else {
            options.add("sourceClass", this.sourceClass, true);
        }
        options.add("parameters", this.parameters, true);
        if (this.title != null) options.add("title", this.title, true);
        if (this.overlib != null) options.add("overlib", this.overlib, true);
        if (this.postFunction != null) options.add("postFunction", this.postFunction, false);
        if (this.emptyFunction != null) options.add("emptyFunction", this.emptyFunction, false);
        if (this.errorFunction != null) options.add("errorFunction", this.errorFunction, false);
        if (this.openEventType != null) options.add("openEvent", this.openEventType, true);
        if (this.closeEventType != null) options.add("closeEvent", this.closeEventType, true);
        StringBuffer script = new StringBuffer();
        script.append("<script type=\"text/javascript\">\n");
        if (StringUtils.isNotEmpty(this.var)) {
            if (StringUtils.isNotEmpty(this.attachTo)) {
                script.append(this.attachTo).append(".").append(this.var);
            } else {
                script.append("var ").append(this.var);
            }
            script.append(" = ");
        }
        script.append("new AjaxJspTag.Callout(\n").append("\"").append(this.baseUrl).append("\", {\n").append(options.toString()).append("});\n").append("</script>\n\n");
        JspWriter writer = pageContext.getOut();
        try {
            writer.println(script);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_PAGE;
    }

    @Override
    public void release() {
        this.var = null;
        this.attachTo = null;
        this.baseUrl = null;
        this.source = null;
        this.sourceClass = null;
        this.parameters = null;
        this.title = null;
        this.overlib = null;
        this.postFunction = null;
        this.emptyFunction = null;
        this.errorFunction = null;
        this.openEventType = null;
        this.closeEventType = null;
        super.release();
    }
}
