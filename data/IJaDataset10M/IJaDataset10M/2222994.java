package net.ontopia.topicmaps.nav2.taglibs.logic;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import net.ontopia.utils.ontojsp.*;
import net.ontopia.topicmaps.nav2.utils.NavigatorUtils;
import net.ontopia.topicmaps.nav2.core.*;
import net.ontopia.topicmaps.nav2.impl.basic.Function;
import net.ontopia.topicmaps.nav2.utils.FrameworkUtils;
import net.ontopia.utils.ontojsp.TaglibTagFactory;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: Logic Tag for loading and registering an external function.
 *
 * @see net.ontopia.topicmaps.nav2.taglibs.logic.CallTag
 */
public class ExternalFunctionTag extends TagSupport {

    private static Logger log = LoggerFactory.getLogger(ExternalFunctionTag.class.getName());

    private String functionName;

    private String functionFQCN;

    /**
   * Process the start tag for this instance.
   */
    public int doStartTag() throws JspException {
        ContextTag contextTag = FrameworkUtils.getContextTag(pageContext);
        FunctionIF function = contextTag.getFunction(functionName);
        if (function == null) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                function = (FunctionIF) Class.forName(functionFQCN, true, classLoader).newInstance();
                contextTag.registerFunction(functionName, function);
                if (log.isInfoEnabled()) log.info("registered external function: " + function.toString());
            } catch (InstantiationException e) {
                String msg = "ExternalFunctionTag: could not instantiate external" + " function " + functionFQCN + " with name '" + functionName + "'. Does it have a default constructor?";
                throw new NavigatorRuntimeException(msg, e);
            } catch (ClassNotFoundException e) {
                String msg = "ExternalFunctionTag function " + functionFQCN + " with name '" + functionName + "': " + e.getMessage() + " not found.";
                throw new NavigatorRuntimeException(msg, e);
            } catch (IllegalAccessException e) {
                String msg = "ExternalFunctionTag function " + functionFQCN + " with name '" + functionName + "': " + e.getMessage() + " can't be accessed.";
                throw new NavigatorRuntimeException(msg, e);
            }
        }
        if (function == null) throw new NavigatorRuntimeException("ExternalFunctionTag: function with name '" + functionName + "' not found.");
        return SKIP_BODY;
    }

    /**
   * reset the state of the Tag.
   */
    public void release() {
    }

    /**
   * INTERNAL: sets name of this function.
   */
    public void setName(String functionName) {
        this.functionName = functionName;
    }

    /**
   * INTERNAL: sets name of the class that implements the function and
   * should be called.
   */
    public void setFqcn(String fqcn) {
        this.functionFQCN = fqcn;
    }
}
