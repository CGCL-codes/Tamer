package net.sf.eclipse.portlet.core.internal.model.descriptor.v1;

import java.util.List;
import java.util.Locale;
import java.util.Vector;
import net.sf.eclipse.portlet.core.Constants;
import net.sf.eclipse.portlet.core.internal.model.common.LocaleFactory;
import net.sf.eclipse.portlet.core.internal.model.common.dom.AbstractDomModelElement;
import net.sf.eclipse.portlet.core.internal.model.common.dom.DomLocalizedString;
import net.sf.eclipse.portlet.core.internal.model.common.dom.DomModelElementFactory;
import net.sf.eclipse.portlet.core.internal.model.common.dom.DomModelElementMap;
import net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint;
import net.sf.eclipse.portlet.core.model.descriptor.IUserDataConstraint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Implementation of {@link ISecurityConstraint} based upon version 1.0 of the specification
 * 
 * @author fwjwiegerinck
 * @since 0.2
 */
public class SecurityConstraint10 extends AbstractDomModelElement implements ISecurityConstraint {

    /**
	 * Inner class to manipulate collection of portlet names
	 */
    private class PortletCollection extends AbstractDomModelElement {

        /**
		 * Initialize portlet collection based upon existing DOM element
		 * 
		 * @param domDocument
		 *            DOM Document for the element
		 * @param domElement
		 *            DOM Element to wrap
		 */
        public PortletCollection(Document domDocument, Element domElement) {
            super(domDocument, domElement);
        }

        /**
		 * Create new element
		 * 
		 * @param domDocument
		 *            DOM Document for the element
		 * @param parentElement
		 *            Parent element for the new portlet collection
		 * @param elementName
		 *            Name of the element to create
		 */
        public PortletCollection(Document domDocument, Element parentElement, String elementName) {
            super(domDocument, parentElement, elementName);
        }

        /**
		 * Add portlet name to the collection
		 * 
		 * @param portletName
		 *            Portlet name to add. Value cannot be NULL
		 */
        public void addPortletName(String portletName) {
            if (portletName == null) throw new IllegalArgumentException("Parameter \"portletName\" cannot be NULL");
            List<String> values = this.getElementValues(Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_PORTLETS_NAME);
            values.add(portletName);
            this.setElementValues(Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_PORTLETS_NAME, values);
        }

        /**
		 * Get list of all portlet names available
		 */
        public List<String> getPortletNames() {
            return this.getElementValues(Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_PORTLETS_NAME);
        }

        /**
		 * Remove portlet name from the collection
		 * 
		 * @param portletName
		 *            Portlet name to remove. Value cannot be NULL
		 */
        public void removePortletName(String portletName) {
            if (portletName == null) throw new IllegalArgumentException("Parameter \"portletName\" cannot be NULL");
            List<String> values = this.getElementValues(Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_PORTLETS_NAME);
            values.remove(portletName);
            this.setElementValues(Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_PORTLETS_NAME, values);
        }
    }

    /**
	 * Define map to retrieve all display names
	 */
    private DomModelElementMap<Locale, DomLocalizedString> displayNames;

    /**
	 * Initialize security constraint based upon existing DOM element
	 * 
	 * @param domDocument
	 *            DOM Document for the element
	 * @param domElement
	 *            DOM Element to wrap
	 */
    public SecurityConstraint10(Document domDocument, Element domElement) {
        super(domDocument, domElement);
        this.displayNames = new DomModelElementMap<Locale, DomLocalizedString>(domElement, Constants.XML_DESCRIPTOR_ATTR_LANGUAGE, Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_DISPLAYNAME, new LocaleFactory(), new DomModelElementFactory<DomLocalizedString>(domDocument, DomLocalizedString.class));
    }

    /**
	 * Create a new security constraint including DOM element
	 * 
	 * @param domDocument
	 *            DOM Document for the element
	 * @param parentElement
	 *            Parent element for the new security constraint
	 * @param elementName
	 *            Name of the element to create
	 */
    public SecurityConstraint10(Document domDocument, Element parentElement, String elementName) {
        super(domDocument, parentElement, elementName);
        this.displayNames = new DomModelElementMap<Locale, DomLocalizedString>(this.getDOMElement(), Constants.XML_DESCRIPTOR_ATTR_LANGUAGE, Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_DISPLAYNAME, new LocaleFactory(), new DomModelElementFactory<DomLocalizedString>(domDocument, DomLocalizedString.class));
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#addDisplayName(java.util.Locale,
	 *      java.lang.String)
	 */
    public void addDisplayName(Locale locale, String displayName) {
        if (displayName == null) throw new IllegalArgumentException("Parameter \"displayName\" cannot be NULL");
        Element newElement = this.getDocument().createElement(Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_DISPLAYNAME);
        DomLocalizedString dls = new DomLocalizedString(this.getDocument(), newElement);
        dls.setLocale(locale);
        dls.setText(displayName);
        this.displayNames.put(locale, dls);
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#addPortlet(java.lang.String)
	 */
    public void addPortlet(String portletName) {
        if (portletName == null) throw new IllegalArgumentException("Parameter \"portletName\" cannot be NULL");
        PortletCollection pc = this.getPortletCollection(true);
        pc.addPortletName(portletName);
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#getDisplayName(java.util.Locale)
	 */
    public String getDisplayName(Locale locale) {
        String returnValue = null;
        DomLocalizedString dls = this.displayNames.get(locale);
        if (dls != null) {
            returnValue = dls.getText();
        }
        return returnValue;
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#getId()
	 */
    public String getId() {
        return this.getAttributeValue(Constants.XML_DESCRIPTOR_ATTR_ID);
    }

    /**
	 * Get instance of portlet collection. If not available and create = <code>true</code>, then an attempt will be made load
	 * an existing or create a new collection
	 * 
	 * @param create
	 *            TRUE if a collection should created if not available, FALSE otherwise
	 * @return Instance of {@link PortletCollection} or NULL if not initialized before and create = <code>false</code>
	 */
    private PortletCollection getPortletCollection(boolean create) {
        PortletCollection returnValue = null;
        NodeList portletCollectionNodeList = this.getDOMElement().getElementsByTagName(Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_PORTLETS);
        if (portletCollectionNodeList.getLength() > 0) {
            Node portletCollectionNode = portletCollectionNodeList.item(0);
            if (portletCollectionNode instanceof Element) {
                returnValue = new PortletCollection(this.getDocument(), (Element) portletCollectionNode);
            }
        }
        if ((returnValue == null) && (create)) {
            returnValue = new PortletCollection(this.getDocument(), this.getDOMElement(), Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_PORTLETS);
        }
        return returnValue;
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#getPortlets()
	 */
    public List<String> getPortlets() {
        List<String> returnValue;
        PortletCollection pc = this.getPortletCollection(false);
        if (pc != null) {
            returnValue = pc.getPortletNames();
        } else {
            returnValue = new Vector<String>(0);
        }
        return returnValue;
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#getUserDataConstraint()
	 */
    public IUserDataConstraint getUserDataConstraint() {
        IUserDataConstraint returnValue = null;
        NodeList userDataConstraintNodeList = this.getDOMElement().getElementsByTagName(Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_USERDATACONSTRAINT);
        if (userDataConstraintNodeList.getLength() > 0) {
            Node userDataConstraintNode = userDataConstraintNodeList.item(0);
            if (userDataConstraintNode instanceof Element) {
                returnValue = new UserDataConstraint10(this.getDocument(), (Element) userDataConstraintNode);
            }
        }
        if (returnValue == null) {
            returnValue = new UserDataConstraint10(this.getDocument(), this.getDOMElement(), Constants.XML_DESCRIPTOR_ELEMENT_SECURITYCONSTRAINT_USERDATACONSTRAINT);
        }
        return returnValue;
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#removeDisplayName(java.util.Locale)
	 */
    public void removeDisplayName(Locale locale) {
        this.displayNames.remove(locale);
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#removePorlet(java.lang.String)
	 */
    public void removePorlet(String portletName) {
        if (portletName == null) throw new IllegalArgumentException("Parameter \"portletName\" cannot be NULL");
        PortletCollection pc = this.getPortletCollection(false);
        if (pc != null) {
            pc.removePortletName(portletName);
        }
    }

    /**
	 * @see net.sf.eclipse.portlet.core.model.descriptor.ISecurityConstraint#setId(java.lang.String)
	 */
    public void setId(String id) {
        this.setAttributeValue(Constants.XML_DESCRIPTOR_ATTR_ID, id);
    }
}
