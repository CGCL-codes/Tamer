package org.jaxson.tag.json;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import flexjson.JSON;

/**
 * Creates a javascript object representing a java object. Note that it doesn't
 * do deep reflection, only creates variable for the properties on the 1st level
 * (no nested properties).
 * 
 * @author Joe Maisel
 */
public class JsonConversionTag extends TagSupport {

    private static final long serialVersionUID = 10980970098709806L;

    private String className;

    private String alias;

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
    @Override
    @SuppressWarnings("unchecked")
    public int doEndTag() throws JspException {
        try {
            Class c = Class.forName(className);
            Method[] methods = c.getMethods();
            List<String> properties = new ArrayList<String>();
            for (Method m : methods) {
                JSON ann = m.getAnnotation(JSON.class);
                if (ann != null) {
                    properties.add(methodToProperty(m.getName()));
                }
            }
            pageContext.getOut().println(generateFunction(properties));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private String generateFunction(List<String> properties) {
        StringBuffer body = new StringBuffer();
        body.append("		\"class\":\"" + className + "\",\n\n");
        StringBuffer params = new StringBuffer();
        Iterator<String> i = properties.iterator();
        while (i.hasNext()) {
            String p = i.next();
            body.append("		\"" + p + "\": p_" + p + " === undefined?null:p_" + p);
            params.append("p_" + p);
            if (i.hasNext()) {
                body.append(",\n");
                params.append(", ");
            }
        }
        StringBuffer result = new StringBuffer();
        result.append(alias + " = function(" + params + ")\n");
        result.append("{\n");
        result.append("	return{\n");
        result.append(body + "\n");
        result.append("	};\n");
        result.append("}\n\n");
        return "\n" + result.toString();
    }

    private String methodToProperty(String methodName) {
        methodName = methodName.substring(3, methodName.length());
        methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1, methodName.length());
        return methodName;
    }
}
