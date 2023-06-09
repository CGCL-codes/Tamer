package au.edu.qut.yawl.schema;

import au.edu.qut.yawl.elements.data.YVariable;
import au.edu.qut.yawl.exceptions.YDataValidationException;
import au.edu.qut.yawl.util.DOMUtil;
import au.edu.qut.yawl.util.JDOMConversionTools;
import org.jdom.Element;
import javax.xml.XMLConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

/** * This class serves as a validation mechanism for the specification specific * schema and the instance data from either the net or a task. This is performed * by taking the data available at the various validation points and converting * it into convential XML which is then validated using a SchemaHandler. * * @author Mike Fowler *         Date: 05-Jul-2006 */
public class YDataValidator {

    /**     * Object that performs the real validation on XML documents     */
    private SchemaHandler handler;

    /**     * Constructs a new validator and handler. The     * handler is not ready for use until validateSchema     * has been called.     *     * @param schema W3C XML Schema     */
    public YDataValidator(String schema) {
        this.handler = new SchemaHandler(schema);
    }

    /**     * Compiles and determines the validity of the current schema     * @return true if the schema compiled without error.     */
    public boolean validateSchema() {
        return handler.compileSchema();
    }

    /**     * Validates a single data variable     *     * @param variable to be validated     * @param data XML representation of variable to be validated     * @param source     * @throws YDataValidationException if the data is not valid     */
    public void validate(YVariable variable, Element data, String source) throws YDataValidationException {
        Vector<YVariable> vars = new Vector<YVariable>(1);
        vars.add(variable);
        validate(vars, data, source);
    }

    /**     * Validates a collection of variables against the schema. This is achieved by temporarily adding     * a schema element declarion for the data. This avoids attempting to create a new schema containing     * only the relevant data types.     *     * @param vars variables to be validated     * @param data XML representation fo the variables to be validated     * @param source     * @throws YDataValidationException if the data is not valid     */
    public void validate(Collection vars, Element data, String source) throws YDataValidationException {
        try {
            org.w3c.dom.Document xsd = DOMUtil.getDocumentFromString(handler.getSchema());
            String prefix = ensureValidPrefix(xsd.lookupPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI));
            xsd.setPrefix(prefix);
            org.w3c.dom.Element element = xsd.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, prefix + "element");
            element.setAttribute("name", data.getName());
            org.w3c.dom.Element complex = xsd.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, prefix + "complexType");
            org.w3c.dom.Element sequence = xsd.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, prefix + "sequence");
            Vector varVect = new Vector(vars.size());
            for (Iterator iter = vars.iterator(); iter.hasNext(); varVect.add(iter.next())) ;
            Collections.sort(varVect);
            for (Object obj : varVect) {
                YVariable var = (YVariable) obj;
                org.w3c.dom.Element child = xsd.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, prefix + "element");
                child.setAttribute("name", var.getName());
                String type = var.getDataTypeName();
                if (type.equals("date") || type.equals("string") || type.equals("double") || type.equals("long") || type.equals("boolean") || type.equals("time") || type.equals("duration")) {
                    type = prefix + type;
                }
                child.setAttribute("type", type);
                child.setAttribute("minOccurs", "0");
                sequence.appendChild(child);
            }
            complex.appendChild(sequence);
            element.appendChild(complex);
            xsd.getDocumentElement().appendChild(element);
            String schema = DOMUtil.getXMLStringFragmentFromNode(xsd);
            SchemaHandler handler = new SchemaHandler(schema);
            if (!handler.compileSchema()) {
                throw new YDataValidationException(handler.getSchema(), data, handler.getConcatenatedMessage(), source, "Problem with process model.  Failed to compile schema");
            }
            if (!handler.validate(JDOMConversionTools.elementToString(data))) {
                throw new YDataValidationException(handler.getSchema(), data, handler.getConcatenatedMessage(), source, "Problem with process model.  Schema validation failed");
            }
        } catch (Exception e) {
            if (e instanceof YDataValidationException) throw (YDataValidationException) e;
        }
    }

    /**     * @return String representation of the schema     */
    public String getSchema() {
        return handler.getSchema();
    }

    /**     * @return All error/warning messages relating to the last validation/compilation     */
    public Vector<String> getMessages() {
        return handler.getMessages();
    }

    /**     * Utility method to ensure the prefix is valid (enforces : and     * defaults to xs:)     *     * @param prefix to validate     * @return validated prefix     */
    private String ensureValidPrefix(String prefix) {
        if (prefix == null || prefix.trim().length() == 0) {
            return "xs:";
        } else if (!prefix.endsWith(":")) {
            return prefix + ":";
        }
        return prefix;
    }
}
