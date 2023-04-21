package cx.ath.contribs.internal.xerces.impl.xs.models;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import cx.ath.contribs.internal.xerces.impl.Constants;
import cx.ath.contribs.internal.xerces.impl.XMLErrorReporter;
import cx.ath.contribs.internal.xerces.impl.dtd.models.CMNode;
import cx.ath.contribs.internal.xerces.impl.xs.XSMessageFormatter;
import cx.ath.contribs.internal.xerces.util.SecurityManager;
import cx.ath.contribs.internal.xerces.xni.parser.XMLComponentManager;
import cx.ath.contribs.internal.xerces.xni.parser.XMLConfigurationException;

/**
 *
 * @xerces.internal 
 *
 * @author  Neeraj Bajaj
 * 
 * @version $Id: CMNodeFactory.java,v 1.2 2007/07/13 07:23:28 paul Exp $
 */
public class CMNodeFactory {

    /** Property identifier: error reporter. */
    private static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;

    /** property identifier: security manager. */
    private static final String SECURITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.SECURITY_MANAGER_PROPERTY;

    private static final boolean DEBUG = false;

    private static final int MULTIPLICITY = 1;

    private int nodeCount = 0;

    private int maxNodeLimit;

    /**
     * Error reporter. This property identifier is:
     * http://apache.org/xml/properties/internal/error-reporter
     */
    private XMLErrorReporter fErrorReporter;

    private SecurityManager fSecurityManager = null;

    /** default constructor */
    public CMNodeFactory() {
    }

    public void reset(XMLComponentManager componentManager) {
        fErrorReporter = (XMLErrorReporter) componentManager.getProperty(ERROR_REPORTER);
        try {
            fSecurityManager = (SecurityManager) componentManager.getProperty(SECURITY_MANAGER);
            if (fSecurityManager != null) {
                maxNodeLimit = fSecurityManager.getMaxOccurNodeLimit() * MULTIPLICITY;
            }
        } catch (XMLConfigurationException e) {
            fSecurityManager = null;
        }
    }

    public CMNode getCMLeafNode(int type, Object leaf, int id, int position) {
        nodeCountCheck();
        return new XSCMLeaf(type, leaf, id, position);
    }

    public CMNode getCMUniOpNode(int type, CMNode childNode) {
        nodeCountCheck();
        return new XSCMUniOp(type, childNode);
    }

    public CMNode getCMBinOpNode(int type, CMNode leftNode, CMNode rightNode) {
        nodeCountCheck();
        return new XSCMBinOp(type, leftNode, rightNode);
    }

    public void nodeCountCheck() {
        if (fSecurityManager != null && nodeCount++ > maxNodeLimit) {
            if (DEBUG) {
                System.out.println("nodeCount = " + nodeCount);
                System.out.println("nodeLimit = " + maxNodeLimit);
            }
            fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "maxOccurLimit", new Object[] { new Integer(maxNodeLimit) }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
            nodeCount = 0;
        }
    }

    public void resetNodeCount() {
        nodeCount = 0;
    }

    /**
     * Sets the value of a property. This method is called by the component
     * manager any time after reset when a property changes value.
     * <p>
     * <strong>Note:</strong> Components should silently ignore properties
     * that do not affect the operation of the component.
     *
     * @param propertyId The property identifier.
     * @param value      The value of the property.
     *
     * @throws SAXNotRecognizedException The component should not throw
     *                                   this exception.
     * @throws SAXNotSupportedException The component should not throw
     *                                  this exception.
     */
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            final int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.SECURITY_MANAGER_PROPERTY.length() && propertyId.endsWith(Constants.SECURITY_MANAGER_PROPERTY)) {
                fSecurityManager = (SecurityManager) value;
                maxNodeLimit = (fSecurityManager != null) ? fSecurityManager.getMaxOccurNodeLimit() * MULTIPLICITY : 0;
                return;
            }
            if (suffixLength == Constants.ERROR_REPORTER_PROPERTY.length() && propertyId.endsWith(Constants.ERROR_REPORTER_PROPERTY)) {
                fErrorReporter = (XMLErrorReporter) value;
                return;
            }
        }
    }
}
