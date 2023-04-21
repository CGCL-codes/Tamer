package uk.org.ogsadai.database.xmldb.simple;

/**
 * Constants and utilities about a simple set of XMLDB collections,
 * sub-collections and resources.
 * <p>
 * Each document has form:
 * <pre>
 * &lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;
 * &lt;ogsadaiDocument id="NNNNN"&gt;
 *     &lt;child1&gt;child1:COLLECTION:I&lt;/child1&gt;
 *     &lt;child2&gt;child1:COLLECTION:I&lt;/child2&gt;
 *     &lt;child3&gt;child3:COLLECTION:I&lt;/child3&gt;
 * &lt;/ogsadaiDocument&gt;
 * </pre>
 * where:
 * <ul>
 * <li>
 * <code>I</code> is an integer index.
 * </li>
 * <li>
 * <code>NNNNN</code> is an integer formed from <code>I</code> with up
 * to five preceding <code>0</code>s depending on the value of
 * <code>I</code>. 
 * </li>
 * <li>
 * <code>COLLECTION</code> is the name of the collection in which
 * the document exists.
 * </li>
 * </ul>
 * <p>
 * Documents with value <code>I</code> greater than 5 have a
 * <code>ogsadaiDocument</code> element with namespace
 * <code>http://namespace1</code> and a <code>child3</code> element
 * with namespace <code>http://namespace2</code>.
 * <p>
 * An example document is:
& <pre>
 * &lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;
 * &lt;ogsadaiDocument id="00005" xmlns="http://namespace1"&gt;
 *     &lt;child1&gt;child1:subCollection1:5&lt;/child1&gt;
 *     &lt;child2&gt;child1:subCollection1:5&lt;/child2&gt;
 *     &lt;child3 xmlns="namespace2"&gt;child3:subCollection1:5&lt;/child3&gt;
 * &lt;/ogsadaiDocument&gt;
 * </pre>
 *
 * @author The OGSA-DAI Project Team.
 */
public class SimpleData {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2002-2009.";

    /** XMLDB sub-collection name. */
    public static final String SUB_COLLECTION_1 = "subCollection1";

    /** XMLDB sub-collection name. */
    public static final String SUB_COLLECTION_2 = "subCollection2";

    /** XMLDB sub-collection name. */
    public static final String SUB_COLLECTION_3 = "subCollection3";

    /** XMLDB sub-collection name. */
    public static final String SUB_COLLECTION_4 = "subCollection4";

    /** Namespace of <code>document</code> element of resources 5000-5099. */
    public static final String NAMESPACE_1 = "http://namespace1";

    /** Namespace of <code>child3</code> element of resources 5000-5099. */
    public static final String NAMESPACE_2 = "http://namespace2";

    /** XML declaration used for generated XML strings. */
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    /** Number of documents per collection with name spaces. */
    public static final int NUMBER_OF_DOCUMENTS_WITH_NAMESPACES = 5;

    /** Number of documents per collection without name spaces. */
    public static final int NUMBER_OF_DOCUMENTS_WITHOUT_NAMESPACES = 5;

    /** Number of documents per collection without name spaces. */
    public static final int NUMBER_OF_DOCUMENTS = NUMBER_OF_DOCUMENTS_WITH_NAMESPACES + NUMBER_OF_DOCUMENTS_WITHOUT_NAMESPACES;

    /**
     * Gets the unique XML document content corresponding to the
     * specified test document index.
     * 
     * @param collection
     *     Name of the collection containing the document.
     * @param i
     *     Index of the test document.
     * @return A <code>String</code> containing the document XML.
     */
    public static String getDocumentContent(final String collection, final int i) {
        final StringBuffer sb = new StringBuffer();
        final StringBuffer ns1 = new StringBuffer();
        final StringBuffer ns2 = new StringBuffer();
        if (i >= NUMBER_OF_DOCUMENTS_WITHOUT_NAMESPACES) {
            ns1.append(" xmlns=\"").append(NAMESPACE_1).append("\"");
            ns2.append(" xmlns=\"").append(NAMESPACE_2).append("\"");
        }
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<ogsadaiDocument id=\"").append(getDocumentId(i)).append("\"");
        sb.append(ns1).append(">\n");
        sb.append("    <child1>");
        sb.append("child1:").append(collection).append(":").append(i);
        sb.append("</child1>\n");
        sb.append("    <child2>");
        sb.append("child2:").append(collection).append(":").append(i);
        sb.append("</child2>\n");
        sb.append("    <child3").append(ns2).append(">");
        sb.append("child3:").append(collection).append(":").append(i);
        sb.append("</child3>\n");
        sb.append("</ogsadaiDocument>\n");
        return sb.toString();
    }

    /**
     * Gets the unique ID corresponding to the specified test document
     * index. 
     * 
     * @param index
     *     Index of the test document.
     * @return A <code>String</code> containing the document ID.
     */
    public static String getDocumentId(int index) {
        String id = String.valueOf(index);
        while (id.length() < 5) id = "0" + id;
        return id;
    }

    /**
     * Gets the unique <code>child3</code> element XML corresponding
     * to the specified test document index.
     * 
     * @param collection
     *     The name of the collection containing the document.
     * @param index
     *     The index of the test document.
     * @return A <code>String</code> containing the XML.
     */
    public static String getDocumentContentChild3(final String collection, final int index) {
        final StringBuffer sb = new StringBuffer();
        final StringBuffer ns2 = new StringBuffer();
        if (index >= NUMBER_OF_DOCUMENTS_WITHOUT_NAMESPACES) {
            ns2.append(" xmlns=\"").append(NAMESPACE_2).append("\"");
        }
        sb.append(XML_DECLARATION);
        sb.append("<child3").append(ns2).append(">");
        sb.append("child3:").append(collection).append(":").append(index);
        sb.append("</child3>\n");
        return sb.toString();
    }
}
