package com.ajah.jsvg.interfaces.common;

/**
 * This interface contains the methods required to support properties in the
 * "%xlinkRefAttrs" of the SVG 1.0 DTD From the W3C Recomemendation: "A URI
 * reference is specified within an href attribute in the XLink [XLINK]
 * namespace. If the default prefix of 'xlink:' is used for attributes in the
 * XLink namespace, then the attribute will be specified as xlink:href. The
 * value of this attribute is a URI reference for the desired resource (or
 * resource fragment)."
 * 
 * @author Eric Savage <a href="mailto:esavage@ajah.com">esavage@ajah.com </a>
 * @version 1.0
 * @since JSVG 1.0
 */
public interface XlinkRefAttrs {

    /**
     * From the W3C Recommendation: "Indicates that the application should
     * traverse to the ending resource immediately on loading the starting
     * resource."
     * 
     * @return the xlink:actuate attribute
     */
    public String getXlinkActuate();

    /**
     * From the W3C Recommendation: "A URI reference that identifies some
     * resource that describes the intended property. The value must be a URI
     * reference as defined in [RFC2396], except that if the URI scheme used is
     * allowed to have absolute and relative forms, the URI portion must be
     * absolute. When no value is supplied, no particular role value is to be
     * inferred. Disallowed URI reference characters in these attribute values
     * must be specially encoded as described earlier in this section. The
     * arcrole attribute corresponds to the [RDF] notion of a property, where
     * the role can be interpreted as stating that "starting-resource HAS
     * arc-role ending-resource." This contextual role can differ from the
     * meaning of an ending resource when taken outside the context of this
     * particular arc. For example, a resource might generically represent a
     * "person," but in the context of a particular arc it might have the role
     * of "mother" and in the context of a different arc it might have the role
     * of "daughter.""
     * 
     * @return the xlink:arcrole attribute
     */
    public String getXlinkArcrole();

    /**
     * From the W3C Recommendation: "A URI reference that identifies some
     * resource that describes the intended property. The value must be a URI
     * reference as defined in [RFC2396], except that if the URI scheme used is
     * allowed to have absolute and relative forms, the URI portion must be
     * absolute. When no value is supplied, no particular role value is to be
     * inferred. Disallowed URI reference characters in these attribute values
     * must be specially encoded as described earlier in this section"
     * 
     * @return the xlink:role attribute
     */
    public String getXlinkRole();

    /**
     * From the W3C Recommendation: "An application traversing to the ending
     * resource should load its presentation in place of the presentation of the
     * starting resource."
     * 
     * @return the xlink:show attribute
     */
    public String getXlinkShow();

    /**
     * From the W3C Recommendation: "The title attribute is used to describe the
     * meaning of a link or resource in a human-readable fashion, along the same
     * lines as the role or arcrole attribute. A value is optional; if a value
     * is supplied, it should contain a string that describes the resource. The
     * use of this information is highly dependent on the type of processing
     * being done. It may be used, for example, to make titles available to
     * applications used by visually impaired users, or to create a table of
     * links, or to present help text that appears when a user lets a mouse
     * pointer hover over a starting resource."
     * 
     * @return the xlink:title attribute
     */
    public String getXlinkTitle();

    /**
     * From the W3C Recommendation: "Identifies the type of XLink being used. In
     * SVG, only simple links are available."
     * 
     * @return the xlink:type attribute
     */
    public String getXlinkType();

    /**
     * From the W3C Recommendation: "Standard XML attribute for identifying an
     * XML namespace. This attribute makes the XLink [XLink] namespace available
     * to the current element."
     * 
     * @return the xmlns:xlink attribute
     */
    public String getXmlnsXlink();

    /**
     * From the W3C Recommendation: "Indicates that the application should
     * traverse to the ending resource immediately on loading the starting
     * resource."
     * 
     * @param xlinkActuate
     *            the xlink:actuate attribute
     */
    public void setXlinkActuate(String xlinkActuate);

    /**
     * From the W3C Recommendation: "A URI reference that identifies some
     * resource that describes the intended property. The value must be a URI
     * reference as defined in [RFC2396], except that if the URI scheme used is
     * allowed to have absolute and relative forms, the URI portion must be
     * absolute. When no value is supplied, no particular role value is to be
     * inferred. Disallowed URI reference characters in these attribute values
     * must be specially encoded as described earlier in this section. The
     * arcrole attribute corresponds to the [RDF] notion of a property, where
     * the role can be interpreted as stating that "starting-resource HAS
     * arc-role ending-resource." This contextual role can differ from the
     * meaning of an ending resource when taken outside the context of this
     * particular arc. For example, a resource might generically represent a
     * "person," but in the context of a particular arc it might have the role
     * of "mother" and in the context of a different arc it might have the role
     * of "daughter.""
     * 
     * @param xlinkArcrole
     *            the xlink:arcrole attribute
     */
    public void setXlinkArcrole(String xlinkArcrole);

    /**
     * From the W3C Recommendation: "A URI reference that identifies some
     * resource that describes the intended property. The value must be a URI
     * reference as defined in [RFC2396], except that if the URI scheme used is
     * allowed to have absolute and relative forms, the URI portion must be
     * absolute. When no value is supplied, no particular role value is to be
     * inferred. Disallowed URI reference characters in these attribute values
     * must be specially encoded as described earlier in this section"
     * 
     * @param xlinkRole
     *            the xlink:role attribute
     */
    public void setXlinkRole(String xlinkRole);

    /**
     * From the W3C Recommendation: "An application traversing to the ending
     * resource should load its presentation in place of the presentation of the
     * starting resource."
     * 
     * @param xlinkShow
     *            the xlink:show attribute
     */
    public void setXlinkShow(String xlinkShow);

    /**
     * From the W3C Recommendation: "The title attribute is used to describe the
     * meaning of a link or resource in a human-readable fashion, along the same
     * lines as the role or arcrole attribute. A value is optional; if a value
     * is supplied, it should contain a string that describes the resource. The
     * use of this information is highly dependent on the type of processing
     * being done. It may be used, for example, to make titles available to
     * applications used by visually impaired users, or to create a table of
     * links, or to present help text that appears when a user lets a mouse
     * pointer hover over a starting resource."
     * 
     * @param xlinkTitle
     *            the xlink:title attribute
     */
    public void setXlinkTitle(String xlinkTitle);

    /**
     * From the W3C Recommendation: "Identifies the type of XLink being used. In
     * SVG, only simple links are available."
     * 
     * @param xlinkType
     *            the xlink:type attribute
     */
    public void setXlinkType(String xlinkType);

    /**
     * From the W3C Recommendation: "Standard XML attribute for identifying an
     * XML namespace. This attribute makes the XLink [XLink] namespace available
     * to the current element."
     * 
     * @param xmlnsXlink
     *            the xmlns:xlink attribute
     */
    public void setXmlnsXlink(String xmlnsXlink);
}
