package org.apache.myfaces.taglib.core;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentELTag;
import org.apache.myfaces.application.jsp.ServletViewResponseWrapper;

/**
 * @author Thomas Spiegl (latest modification by $Author: sobryan $)
 * @version $Revision: 896630 $ $Date: 2010-01-06 14:26:29 -0500 (Wed, 06 Jan 2010) $
 */
public class SubviewTag extends UIComponentELTag {

    public SubviewTag() {
        super();
    }

    @Override
    public String getComponentType() {
        return UINamingContainer.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    /**
     * Creates a UIComponent from the BodyContent If a Subview is included via the <jsp:include> tag the corresponding
     * jsp is rendered with getServletContext().getRequestDispatcher("includedSite").include(request,response) and it is
     * possible that something was written to the Response direct. So is is necessary that the content of the wrapped
     * response is added to the componenttree.
     * 
     * @return UIComponent or null
     */
    @Override
    protected UIComponent createVerbatimComponentFromBodyContent() {
        UIOutput component = (UIOutput) super.createVerbatimComponentFromBodyContent();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object response = facesContext.getExternalContext().getResponse();
        String wrappedOutput;
        if (response instanceof ServletViewResponseWrapper) {
            ServletViewResponseWrapper wrappedResponse = (ServletViewResponseWrapper) response;
            wrappedOutput = wrappedResponse.toString();
            if (wrappedOutput != null && wrappedOutput.length() > 0) {
                String componentvalue = null;
                if (component != null) {
                    componentvalue = (String) component.getValue();
                }
                component = super.createVerbatimComponent();
                if (componentvalue != null) {
                    component.setValue(wrappedOutput + componentvalue);
                } else {
                    component.setValue(wrappedOutput);
                }
                wrappedResponse.reset();
            }
        }
        return component;
    }
}
