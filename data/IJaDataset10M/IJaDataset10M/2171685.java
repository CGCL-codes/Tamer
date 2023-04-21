package com.genia.toolbox.web.portlet.bean.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.genia.toolbox.basics.exception.BundledException;
import com.genia.toolbox.web.portlet.bean.Portlet;
import com.genia.toolbox.web.portlet.visitor.PortletVisitor;

/**
 * the implementation of a portlet containing sub portlets.
 */
public class ContainerPortlet extends AbstractPortlet {

    /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * @see java.lang.Object#equals(java.lang.Object)
   */
    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (obj instanceof ContainerPortlet) {
            final ContainerPortlet oPortlet = (ContainerPortlet) obj;
            if (!ObjectUtils.nullSafeEquals(getViewName(), oPortlet.getViewName())) {
                return false;
            }
            return ObjectUtils.nullSafeEquals(getSubPortlets(), oPortlet.getSubPortlets());
        }
        return false;
    }

    /**
   * the name of the spring view to use to display this container.
   */
    private String viewName;

    /**
   * the list of <code>Portlet</code> contained in this portlet.
   */
    private List<Portlet> subPortlets = new ArrayList<Portlet>();

    /**
   * The visit method to use with a <code>PortletVisitor</code>.
   * 
   * @param visitor
   *          the <code>PortletVisitor</code>
   * @throws BundledException
   *           when an error occurred.
   * @see com.genia.toolbox.web.portlet.bean.Portlet#visit(com.genia.toolbox.web.portlet.visitor.PortletVisitor)
   */
    public void visit(final PortletVisitor visitor) throws BundledException {
        visitor.visitContainerPortlet(this);
    }

    /**
   * getter for the viewName property.
   * 
   * @return the viewName
   */
    public String getViewName() {
        return viewName;
    }

    /**
   * setter for the viewName property.
   * 
   * @param viewName
   *          the viewName to set
   */
    public void setViewName(final String viewName) {
        this.viewName = StringUtils.trimWhitespace(viewName);
    }

    /**
   * getter for the subPortlets property.
   * 
   * @return the subPortlets
   */
    public List<Portlet> getSubPortlets() {
        return subPortlets;
    }

    /**
   * setter for the subPortlets property.
   * 
   * @param subPortlets
   *          the subPortlets to set
   */
    public void setSubPortlets(final List<Portlet> subPortlets) {
        this.subPortlets = subPortlets;
    }

    /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * @see java.lang.Object#hashCode()
   */
    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(new Object[] { getAttributes(), getDescriptionName(), getIdentifier(), getSubPortlets(), getViewName() });
    }
}
