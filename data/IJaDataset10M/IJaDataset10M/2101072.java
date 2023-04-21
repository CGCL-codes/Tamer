package com.sun.xacml;

import com.sun.xacml.attr.AttributeFactory;
import com.sun.xacml.attr.AttributeFactoryProxy;
import com.sun.xacml.combine.CombiningAlgFactory;
import com.sun.xacml.combine.CombiningAlgFactoryProxy;
import com.sun.xacml.cond.FunctionFactory;
import com.sun.xacml.cond.FunctionFactoryProxy;

/**
 * This is used to share polcy meta-data throughout the policy tree. Examples
 * of common meta-data include the version of XACML or XPath being used in
 * a policy.
 *
 * @since 2.0
 * @author Seth Proctor
 */
public class PolicyMetaData {

    /**
     * XACML 1.0 identifier
     */
    public static final String XACML_1_0_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:policy";

    /**
     * XACML 2.0 identifier
     */
    public static final String XACML_2_0_IDENTIFIER = "urn:oasis:names:tc:xacml:2.0:policy:schema:os";

    /**
     * Version identifier for XACML 1.0
     */
    public static final int XACML_VERSION_1_0 = 0;

    /**
     * Version identifier for XACML 1.1 (which isn't a formal release
     * so has no namespace string, but still exists as a separate
     * specification)
     */
    public static final int XACML_VERSION_1_1 = 1;

    /**
     * Version identifier for XACML 1.2
     */
    public static final int XACML_VERSION_2_0 = 2;

    /**
     * The default version of XACML, 1.0, used if no namespace string
     * is specified
     */
    public static final int XACML_DEFAULT_VERSION = XACML_VERSION_1_0;

    private static String[] xacmlIdentifiers = { XACML_1_0_IDENTIFIER, XACML_1_0_IDENTIFIER, XACML_2_0_IDENTIFIER };

    /**
     * XPath 1.0 identifier
     */
    public static final String XPATH_1_0_IDENTIFIER = "http://www.w3.org/TR/1999/Rec-xpath-19991116";

    /**
     * Version identifier for an unspecified version of XPath
     */
    public static final int XPATH_VERSION_UNSPECIFIED = 0;

    /**
     * Version identifier for XPath 1.0
     */
    public static final int XPATH_VERSION_1_0 = 1;

    private static String[] xpathIdentifiers = { null, XPATH_1_0_IDENTIFIER };

    private int xacmlVersion;

    private int xpathVersion;

    private AttributeFactoryProxy afProxy;

    private CombiningAlgFactoryProxy cafProxy;

    private FunctionFactoryProxy ffProxy;

    /**
     * Creates a <code>PolicyMetaData</code> instance with all the parameters
     * set to their default values.
     */
    public PolicyMetaData() {
        this(XACML_DEFAULT_VERSION, XPATH_VERSION_UNSPECIFIED);
    }

    /**
     * Creates a <code>PolicyMetaData</code> instance with the given
     * parameters. The default factories are assumed.
     *
     * @param xacmlVersion the version of XACML used in a policy
     * @param xpathVersion the XPath version to use in any selectors
     */
    public PolicyMetaData(int xacmlVersion, int xpathVersion) {
        this(xacmlVersion, xpathVersion, null, null, null);
    }

    /**
     * Creates a <code>PolicyMetaData</code> instance with the given
     * parameters. The default factories are assumed.
     *
     * @param xacmlVersion the version of XACML used in a policy
     * @param xpathVersion the XPath version to use in any selectors, or
     *                     null if this is unspecified (ie, not supplied in
     *                     the defaults section of the policy)
     *
     * @throws IllegalArgumentException if the identifier strings are unknown
     */
    public PolicyMetaData(String xacmlVersion, String xpathVersion) {
        this(xacmlVersion, xpathVersion, null, null, null);
    }

    /**
     * Creates a <code>PolicyMetaData</code> instance with the given
     * parameters. A proxy value of null implies the default factory.
     *
     * @param xacmlVersion the version of XACML used in a policy
     * @param xpathVersion the XPath version to use in any selectors
     * @param attributeFactoryProxy
     * @param combiningAlgFactoryProxy
     * @param functionFactoryProxy
     */
    public PolicyMetaData(int xacmlVersion, int xpathVersion, AttributeFactoryProxy attributeFactoryProxy, CombiningAlgFactoryProxy combiningAlgFactoryProxy, FunctionFactoryProxy functionFactoryProxy) {
        this.xacmlVersion = xacmlVersion;
        this.xpathVersion = xpathVersion;
        proxySetup(attributeFactoryProxy, combiningAlgFactoryProxy, functionFactoryProxy);
    }

    /**
     * Creates a <code>PolicyMetaData</code> instance with the given
     * parameters.
     *
     * @param xacmlVersion the version of XACML used in a policy
     * @param xpathVersion the XPath version to use in any selectors, or
     *                     null if this is unspecified (ie, not supplied in
     *                     the defaults section of the policy)
     * @param 
     *
     * @throws IllegalArgumentException if the identifier strings are unknown
     */
    public PolicyMetaData(String xacmlVersion, String xpathVersion, AttributeFactoryProxy attributeFactoryProxy, CombiningAlgFactoryProxy combiningAlgFactoryProxy, FunctionFactoryProxy functionFactoryProxy) {
        if (xacmlVersion == null) this.xacmlVersion = XACML_DEFAULT_VERSION; else if (xacmlVersion.equals(XACML_1_0_IDENTIFIER)) this.xacmlVersion = XACML_VERSION_1_0; else if (xacmlVersion.equals(XACML_2_0_IDENTIFIER)) this.xacmlVersion = XACML_VERSION_2_0; else throw new IllegalArgumentException("Unknown XACML version " + "string: " + xacmlVersion);
        if (xpathVersion != null) {
            if (!xpathVersion.equals(XPATH_1_0_IDENTIFIER)) throw new IllegalArgumentException("Unsupported XPath " + " version: " + xpathVersion);
            this.xpathVersion = XPATH_VERSION_1_0;
        } else {
            this.xpathVersion = XPATH_VERSION_UNSPECIFIED;
        }
        proxySetup(attributeFactoryProxy, combiningAlgFactoryProxy, functionFactoryProxy);
    }

    /**
     *
     */
    private void proxySetup(AttributeFactoryProxy attributeFactoryProxy, CombiningAlgFactoryProxy combiningAlgFactoryProxy, FunctionFactoryProxy functionFactoryProxy) {
        if (attributeFactoryProxy == null) this.afProxy = new AttributeFactoryProxy() {

            public AttributeFactory getFactory() {
                return AttributeFactory.getInstance();
            }
        }; else this.afProxy = attributeFactoryProxy;
        if (combiningAlgFactoryProxy == null) this.cafProxy = new CombiningAlgFactoryProxy() {

            public CombiningAlgFactory getFactory() {
                return CombiningAlgFactory.getInstance();
            }
        }; else this.cafProxy = combiningAlgFactoryProxy;
        if (functionFactoryProxy == null) this.ffProxy = FunctionFactory.getInstance(); else this.ffProxy = functionFactoryProxy;
    }

    /**
     * Returns which version of XACML is specified in this meta-data.
     *
     * @return the XACML version
     */
    public int getXACMLVersion() {
        return xacmlVersion;
    }

    /**
     * Returns the identifier string for the specified version of XACML.
     *
     * @return the identifier string
     */
    public String getXACMLIdentifier() {
        return xacmlIdentifiers[xacmlVersion];
    }

    /**
     * Returns which version of XPath is specified in this meta-data.
     *
     * @return the XPath version or null
     */
    public int getXPathVersion() {
        return xpathVersion;
    }

    /**
     * Returns the identifier string for the specified version of XPath, or
     * null if no version is specified.
     *
     * @return the identifier string or null
     */
    public String getXPathIdentifier() {
        return xpathIdentifiers[xpathVersion];
    }

    /**
     * Returns the <code>AttributeFactory</code> used by the associated
     * policy.
     *
     * @return a <code>AttributeFactory</code>
     */
    public AttributeFactory getAttributeFactory() {
        return afProxy.getFactory();
    }

    /**
     * Returns the <code>CombiningAlgFactory</code> used by the associated
     * policy.
     *
     * @return a <code>CombiningAlgFactory</code>
     */
    public CombiningAlgFactory getCombiningAlgFactory() {
        return cafProxy.getFactory();
    }

    /**
     * Returns the Target <code>FunctionFactory</code> used by the associated
     * policy.
     *
     * @return a <code>FunctionFactory</code>
     */
    public FunctionFactory getTargetFunctionFactory() {
        return ffProxy.getTargetFactory();
    }

    /**
     * Returns the Condition <code>FunctionFactory</code> used by the
     * associated policy.
     *
     * @return a <code>FunctionFactory</code>
     */
    public FunctionFactory getConditionFunctionFactory() {
        return ffProxy.getConditionFactory();
    }

    /**
     * Returns the General <code>FunctionFactory</code> used by the associated
     * policy.
     *
     * @return a <code>FunctionFactory</code>
     */
    public FunctionFactory getGeneralFunctionFactory() {
        return ffProxy.getGeneralFactory();
    }
}
