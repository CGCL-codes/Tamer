package org.openliberty.arisid.policy;

import java.net.URI;
import javax.xml.namespace.QName;

/**
 * Indicates the contractual or legal context governing the sharing of identity
 * attributes.
 * 
 * <PRE>
 * &lt;element name=&quot;ContractOrLegalConstraint&quot;&gt;
 *     &lt;complexType&gt;
 *         &lt;attribute ref=&quot;pri:Issuer&quot;/&gt;
 *         &lt;attribute name=&quot;uri&quot; type=&quot;anyURI&quot; use=&quot;required&quot;/&gt;
 *     &lt;/complexType&gt;
 * &lt;/element&gt;
 * </PRE>
 * 
 * This specification defines a single standard URI for constraining contract or
 * legal context.
 * <DL>
 * <DT>urn:liberty:names:1.0:igf:pri:contract:context
 * <DD>Indicates that the contractual or legal context under which the data
 * value is sought SHOULD be determined from application context.
 * </DL>
 * There is an expectation is that communities will define additional URIs based
 * on rules for industry verticals and national jurisdictions.
 */
public interface IContractOrLegalConstraint extends IAssertion {

    public static final String NAMESPACE = IArisWSPolicy.AppIdPolicy_NS;

    public static final String ELEMENT = "ContractOrLegalConstraint";

    public static final QName qelement = new QName(NAMESPACE, ELEMENT);

    public static final String URI_CONTRACT_CONTEXT = "urn:liberty:names:1.0:igf:pri:contract:context";

    /**
	 * @return A URI representing the Contract or Legal context for exchange.
	 */
    public URI getContractUri();
}
