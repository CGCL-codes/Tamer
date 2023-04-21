package com.fourspaces.scratch.taglib;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fourspaces.scratch.mapping.ControllerMappingPath;
import com.fourspaces.scratch.mapping.MappingRegistry;
import com.fourspaces.scratch.validation.ValidationHandler;
import com.fourspaces.scratch.validation.ValidatorFactory;
import com.fourspaces.scratch.validation.annotation.Validate;

public class FormTag extends BodyTagSupport {

    protected Log log = LogFactory.getLog(getClass());

    private static final long serialVersionUID = 6697970491664649784L;

    protected String path = null;

    protected String href = null;

    protected String enctype = null;

    protected String onsubmit = null;

    protected String novalidation = null;

    protected String id = null;

    protected String name = null;

    protected String method = "GET";

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setAction(String href) {
        this.href = href;
    }

    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    public void setOnsubmit(String onsubmit) {
        this.onsubmit = onsubmit;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().write("</form>");
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException {
        StringWriter w = new StringWriter();
        boolean validation = false;
        MappingRegistry registry = (MappingRegistry) pageContext.getServletContext().getAttribute(MappingRegistry.CONTEXT_KEY);
        ControllerMappingPath cmp = null;
        if (path != null && (novalidation == null || novalidation.toLowerCase().equals("false"))) {
            cmp = registry.findMapping(path, (HttpServletRequest) pageContext.getRequest());
        } else if (href != null && (novalidation == null || novalidation.toLowerCase().equals("false"))) {
            cmp = registry.findMapping(href, (HttpServletRequest) pageContext.getRequest());
        }
        if (cmp != null) {
            if (cmp.getControllerMapping().getValidations() != null) {
                validation = true;
                w.write("<script>");
                w.write("function __scratch_validation(form) {\n");
                w.write("\tvar valid=true;\n");
                w.write("\tvar msg=\"\";\n");
                w.write("\tvar focusEl=\"\";\n");
                Map<String, String> jsFunctions = new HashMap<String, String>();
                for (Validate val : cmp.getControllerMapping().getValidations()) {
                    ValidationHandler validationHandler = ValidatorFactory.getValidator(val.handler());
                    if (validationHandler.getJavascriptFunctionName() != null) {
                        w.write("\n\tif (!" + validationHandler.getJavascriptFunctionName() + "(form,");
                        if (val.field() != null) {
                            for (int i = 0; i < val.field().length; i++) {
                                if (i > 0) {
                                    w.write(",");
                                }
                                w.write("'" + val.field()[i] + "'");
                            }
                        }
                        w.write(")) { \n\t\tvalid=false; \n");
                        w.write("\t\tif (msg) { msg+=\"\\n\"; }\n");
                        w.write("\t\tmsg+=\"");
                        w.write(validationHandler.errorMessage(val.field(), val.errorMessage()));
                        w.write("\"\n");
                        if (val.field() != null) {
                            w.write("\t\t if (!focusEl) { focusEl=form.elements['" + val.field()[0] + "']; }");
                        }
                        w.write(" \n\t} \n");
                        if (!jsFunctions.containsKey(validationHandler.getJavascriptFunctionName())) {
                            jsFunctions.put(validationHandler.getJavascriptFunctionName(), validationHandler.getJavascriptFunction());
                        }
                    }
                }
                w.write("\t if (!valid) { " + "\n\t\t" + "if (!msg) { " + "msg=\"There are errors with this form!\";" + "\n\t\t} " + "\n\t\talert(msg); ");
                w.write("\n\t\t if (focusEl) { focusEl.focus(); }");
                w.write("\n\t\treturn false; ");
                w.write("\n\t}\n");
                w.write("\treturn true;");
                w.write("\n}\n");
                for (String key : jsFunctions.keySet()) {
                    w.write(jsFunctions.get(key));
                    w.write("\n");
                }
                w.write("</script>\n");
            }
        }
        w.write("<form action=\"");
        if (href != null) {
            w.write(href);
        } else if (path != null) {
            w.write(((HttpServletRequest) pageContext.getRequest()).getContextPath());
            w.write(path);
        }
        w.write("\" method=\"" + method + "\"");
        if (enctype != null) {
            w.write(" enctype=\"" + enctype + "\"");
        }
        if (name != null) {
            w.write(" name=\"" + name + "\"");
        }
        if (id != null) {
            w.write(" id=\"" + id + "\"");
        }
        if (onsubmit != null) {
            if (validation) {
                w.write(" onsubmit=\"if (!__scratch_validation(this)) { return false; } else { " + onsubmit + " }\"");
            } else {
                w.write(" onsubmit=\"" + onsubmit + "\"");
            }
        } else if (validation) {
            w.write(" onsubmit=\"return __scratch_validation(this);\"");
        }
        w.write(">");
        try {
            pageContext.getOut().write(w.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    public void setNovalidation(String noValidation) {
        this.novalidation = noValidation;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
