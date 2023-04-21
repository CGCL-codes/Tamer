package edu.dimacs.mms.boxer;

import java.io.*;
import java.util.*;
import java.text.*;
import org.w3c.dom.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.apache.xerces.dom.DocumentImpl;

/** A FeatureDictionary provides a two-way mapping between feature
    labels (strings found in input and output files) and internal
    feature IDs. There is normally only one feature dictionary in a
    running application; it contains all features seen in the training
    and test documents we have seen so far.

    The FeatureDictionary is also used to ensure that all feature
    names have legal format (contain no whitespace, prohibited
    characters, etc)

    <p> Most API users would not need to ever use a FeatureDictionary
    object directly, as there is one encapsulated inside the {@link
    edu.dimacs.mms.boxer.Suite Suite} object your application would use, and BOXER
    would be using it everywhere the Suite is used.

    <p>However, one may need to access a FeatureDictionary (typically,
    the FeatureDictionary of a suite you've created) when
    e.g. creating {@link edu.dimacs.mms.boxer.DataPoint DataPoint} objects directly
    (rather than reading them from an XML dataset definition).

*/
public class FeatureDictionary {

    /** If this flag is true, we add a dummy component x_0=1 to each vector
	when we read it in, and strip it on the output. */
    static final boolean ADD_DUMMY_COMPONENT = true;

    /** The value (@dummy) used for the intercept feature */
    public static final String DUMMY_LABEL = "@dummy";

    /** Maps an integer 0-based feature id to feature label. All
     * values stored here are supposed to be syntactically legal
     * feature labels; thus, validation is done before every add call. */
    private Vector<String> id2label = new Vector<String>();

    /** Reverse: feature label to feature id */
    private HashMap<String, Integer> label2id = new HashMap<String, Integer>();

    /** Creates an empty (or near-empty) dictionary. The only value
	that may be put in by this constructor is  DUMMY_LABEL, and only 
	if the ADD_DUMMY_COMPONENT flag is on.
     */
    public FeatureDictionary() {
        if (ADD_DUMMY_COMPONENT) {
            id2label.add(DUMMY_LABEL);
            label2id.put(DUMMY_LABEL, new Integer(0));
        }
    }

    /** Returns true if the dictionary is either completely empty, or only 
	contains the DUMMY_LABEL
     */
    public boolean isPracticallyEmpty() {
        return getDimension() == 0 || getDimension() == 1 && id2label.elementAt(0).equals(DUMMY_LABEL);
    }

    boolean hasDummyIfRequired() {
        return !ADD_DUMMY_COMPONENT || getDimension() > 0 && id2label.elementAt(0).equals(DUMMY_LABEL);
    }

    /** Retrieves the (0-based integer) ID for the given feature label
      - provided it's in the dictionary already */
    public synchronized int getId(String label) {
        return label2id.get(label).intValue();
    }

    public String getLabel(int id) {
        return id2label.elementAt(id);
    }

    /** Is this the dummy component? (We just test by position, which
     * is no. 0 for the dummy. A more reliable way would be to compare
     * the label to {@link #DUMMY_LABEL} */
    public boolean isDummy(int id) {
        return ADD_DUMMY_COMPONENT && id == 0;
    }

    /** Always returns a valid ID for the given feature label: an
     already recorded one, if there is one, or a new one,
     otherwise. In the latter case, the feature label is validated
     before being inserted into the dictionary.

     This method must be synchronized to prevent double insertion

     @param label The feature label (name). Should be non-null
    */
    public synchronized int getIdAlways(String label) throws BoxerXMLException {
        if (label == null) throw new IllegalArgumentException("label=null");
        Integer x = label2id.get(label);
        if (x != null) return x.intValue();
        if (!IDValidation.validateFeatureName(label)) {
            throw new BoxerXMLException("Can't add feature with the name '" + label + "' to the feature dictionary, because this is not a legal name");
        }
        id2label.add(label);
        int z = id2label.size() - 1;
        label2id.put(label, new Integer(z));
        return z;
    }

    /** How many features are there so far? */
    public int getDimension() {
        return id2label.size();
    }

    public String describe() {
        NumberFormat fmt = new DecimalFormat("###");
        StringBuffer b = new StringBuffer("--- FeatureDictionary ---\n");
        for (int i = 0; i < id2label.size(); i++) {
            b.append(fmt.format(i) + " " + getLabel(i) + "\n");
        }
        b.append("------------------------");
        return b.toString();
    }

    /** FIXME: is there a better place to set the dummy coeff? */
    void addDummyCompoIfRequired(Vector<DataPoint.FVPair> fv) throws BoxerXMLException {
        if (ADD_DUMMY_COMPONENT) {
            fv.add(new DataPoint.FVPair(getIdAlways(DUMMY_LABEL), 1));
        }
    }

    /**  Element names for XML serializing and deserializing  */
    class XML {

        static final String FEATURES = "features";
    }

    /** Describe the list of features as an element of an XML document */
    public org.w3c.dom.Element createFeaturesElement(Document xmldoc) {
        Element e = xmldoc.createElement(XML.FEATURES);
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < id2label.size(); i++) {
            if (b.length() > 0) b.append(" ");
            b.append(getLabel(i));
        }
        Text textNode = xmldoc.createTextNode(b.toString());
        e.appendChild(textNode);
        return e;
    }

    /** Saves the dictionary as an XML document
     */
    public Document saveAsXML() {
        Document xmldoc = new DocumentImpl();
        Element root = createFeaturesElement(xmldoc);
        xmldoc.appendChild(root);
        return xmldoc;
    }

    /** Saves the dictionary as an XML document and writes it to a file
     */
    public void saveAsXML(String fname) {
        XMLUtil.writeXML(saveAsXML(), fname);
    }

    /** Reads in a   FeatureDictionary from an XML file.
     */
    public FeatureDictionary(File f) throws IOException, SAXException, BoxerXMLException {
        this(ParseXML.readFileToElement(f));
    }

    /** Creates a new Feature Dictionary and loads it with the feature
	list from an XML element that may have been produced by
	createFeaturesElement().   

	@param e A <tt>features</tt> XML element

	@throws BoxerXMLException When the content of the XML element
	is inappropriate, e.g. when it contains unexpected child
	elements or an illegal feature label
    */
    public FeatureDictionary(Element e) throws BoxerXMLException {
        if (!e.getTagName().equals(XML.FEATURES)) {
            throw new BoxerXMLException("FeatureDictionary can only be deserialized from an XML element named `" + XML.FEATURES + "'");
        }
        for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling()) {
            int type = n.getNodeType();
            boolean found = false;
            if (type == Node.COMMENT_NODE) {
            } else if (type == Node.TEXT_NODE) {
                String val = n.getNodeValue().trim();
                if (val.length() == 0) {
                } else {
                    if (found) throw new BoxerXMLException("FeatureDictionary deserializer expects only one non-empty TEXT element in the XML element being parsed");
                    found = true;
                    String[] tokens = val.split("\\s+");
                    if (ADD_DUMMY_COMPONENT && (tokens.length == 0 || !tokens[0].equals(DUMMY_LABEL))) {
                        throw new BoxerXMLException("FeatureDictionary deserializer:  ADD_DUMMY_COMPONENT flag is on, but the feature list in the XML element does not start with " + DUMMY_LABEL);
                    }
                    id2label.setSize(tokens.length);
                    for (int i = 0; i < tokens.length; i++) {
                        if (!IDValidation.validateFeatureName(tokens[i])) {
                            throw new BoxerXMLException("Can't add feature with the name '" + tokens[i] + "' to the feature dictionary, because this is not a legal name");
                        }
                        id2label.set(i, tokens[i]);
                        label2id.put(tokens[i], i);
                    }
                }
            } else {
                throw new IllegalArgumentException("FeatureDictionary deserializer: encountered unexpected node type " + type);
            }
        }
    }
}
