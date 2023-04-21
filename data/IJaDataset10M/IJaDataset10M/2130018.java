package vars.knowledgebase;

/**
 * <h2><u>Description</u></h2> <p><!--Insert summary here--></p> <h2><u>UML</u></h2> <pre> </pre> <h2><u>License</u></h2> <p><font size="-1" color="#336699"><a href="http://www.mbari.org"> The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this documentation and code &quot;as is&quot;, with no warranty, express or implied, of its quality or consistency. It is provided without support and without obligation on the part of MBARI to assist in its use, correction, modification, or enhancement. This information should not be published or distributed to third parties without specific written permission from MBARI.</font></p> <p><font size="-1" color="#336699">Copyright 2003 MBARI. MBARI Proprietary Information. All rights reserved.</font></p>
 * @author  <a href="http://www.mbari.org">MBARI</a>
 * @version  $Id: IConceptName.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public interface IConceptName {

    /**
     *  Used when the author of a conceptname is unknown
     */
    static final String AUTHOR_UNKNOWN = "unknown";

    /**
     * TODO brian: how is this different than synonym?
     */
    static final String NAMETYPE_ALTERNATE = "Alternate";

    /**
     *  Common name
     */
    static final String NAMETYPE_COMMON = "Common";

    /**
     * Indicates
     */
    static final String NAMETYPE_FORMER = "Former";

    /**
     *  The primary name of a concept. For organisms this is generally a
     * genus-species composite
     */
    static final String NAMETYPE_PRIMARY = "Primary";

    /**
     *  Synonym for the concept
     */
    static final String NAMETYPE_SYNONYM = "Synonym";

    /** <!-- Field description --> */
    static final String NAME_DEFAULT = "object";

    /**
     *  Description of the Field
     */
    static final String NAME_UNKNOWN = "unknown";

    /**
     * <p><!-- Method description --></p>
     * @return
     * @uml.property  name="author"
     */
    String getAuthor();

    /**
         * <p><!-- Method description --></p>
         * @return
         * @uml.property  name="concept"
         * @uml.associationEnd
         */
    IConcept getConcept();

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    Long getId();

    /**
     * <p><!-- Method description --></p>
     * @return
     * @uml.property  name="name"
     */
    String getName();

    /**
     * <p><!-- Method description --></p>
     * @return
     * @uml.property  name="nameType"
     */
    String getNameType();

    /**
     * <p><!-- Method description --></p>
     * @param  author
     * @uml.property  name="author"
     */
    void setAuthor(String author);

    void setConcept(IConcept concept);

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param id
     */
    void setId(Long id);

    /**
     * <p><!-- Method description --></p>
     * @param  name
     * @uml.property  name="name"
     */
    void setName(String name);

    /**
     * <p><!-- Method description --></p>
     * @param  nameType
     * @uml.property  name="nameType"
     */
    void setNameType(String nameType);

    String stringValue();
}
