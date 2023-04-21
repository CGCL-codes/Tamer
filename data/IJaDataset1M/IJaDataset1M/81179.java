package org.tigr.microarray.mev.script.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import org.apache.xerces.parsers.DOMParser;
import org.tigr.microarray.mev.TMEV;
import org.tigr.microarray.mev.cluster.algorithm.AlgorithmData;
import org.tigr.microarray.mev.script.ScriptManager;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ParameterValidator extends DefaultHandler {

    /** Root element for validation XML.
     */
    Element validationRoot;

    /** True if have the val. root.
     */
    boolean haveValidationRoot;

    /** Creates a new instance of ParameterValidator */
    public ParameterValidator() {
    }

    /** Load the XML ParameterConstraints.
     */
    public boolean loadParameterConstraints() {
        try {
            File paramFile = TMEV.getConfigurationFile("ParameterConstraints.xml");
            DOMParser parser = new DOMParser();
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setErrorHandler(this);
            parser.parse(paramFile.toURL().toString());
            validationRoot = parser.getDocument().getDocumentElement();
            haveValidationRoot = true;
        } catch (NullPointerException e) {
            haveValidationRoot = false;
            JOptionPane.showMessageDialog(new JFrame(), "The parameter validation feature in support of scripting could not be initialized.\n" + "The constraint file \"ParameterConstraints.xml\" could not be located.\nScript capabilities will operate without full parameter validation", "Parameter Validation Initialization Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            haveValidationRoot = false;
            JOptionPane.showMessageDialog(new JFrame(), "The parameter validation feature in support of scripting could not be initialized properly.\n" + "\"ParameterConstraints.xml\" contained errors reported in the console window.\nScript capabilities will operate without full parameter validation", "Parameter Validation Initialization Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /** True if validator is enabled.
     */
    public boolean isEnabled() {
        return haveValidationRoot;
    }

    /**  Returns true if script parameters match the requirements
     *  defined in ScriptValidation.xml.  Algorithm parameter keys
     *  must match valid keys, value type, and value range as defined.
     * @param manager script manager
     * @param tree Script tree to validate.
     * @param log error log
     * @return
     */
    public boolean validate(ScriptManager manager, ScriptTree tree, ErrorLog log) {
        AlgorithmSet[] sets = tree.getAlgorithmSets();
        AlgorithmNode node;
        boolean isValid = true;
        log.reset();
        NodeList list = validationRoot.getElementsByTagName("script_algorithm");
        for (int setIndex = 0; setIndex < sets.length; setIndex++) {
            for (int algIndex = 0; algIndex < sets[setIndex].getAlgorithmCount(); algIndex++) {
                node = sets[setIndex].getAlgorithmNodeAt(algIndex);
                if (!validateAlgorithm(node, list, log)) isValid = false;
            }
        }
        return isValid;
    }

    /** Returns true if the algorithm parameters are valid.
     * @param algorithmNode algorithm name to validate
     * @param algList algorithm list
     * @param log
     * @return
     */
    private boolean validateAlgorithm(AlgorithmNode algorithmNode, NodeList algList, ErrorLog log) {
        AlgorithmData data = algorithmNode.getAlgorithmData();
        Map map = data.getParams().getMap();
        Iterator iter = map.keySet().iterator();
        String key;
        String value;
        String algName = algorithmNode.getAlgorithmName();
        boolean isValid = true;
        if (algName == null) {
            return false;
        }
        if (algorithmNode.getAlgorithmType().equals(ScriptConstants.ALGORITHM_TYPE_ADJUSTMENT)) return true;
        Element algElement = getAlgorithmElement(algName, algList);
        if (algElement == null) {
            JOptionPane.showMessageDialog(new JFrame(), "The " + algName + " algorithm does not have information to support parameter validation." + "\nPlease be aware that script loading will proceed without validation of this algorithm.", "Unsupported Parameter Validation", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        if (algElement == null) {
            ScriptParameterException spe = createScriptParameterException(algorithmNode, "Parameter constraint information is not available for this algorithm. " + "The algorithm parameters can not be validated.");
            log.recordParameterError(spe);
            return false;
        }
        Vector missingParameterVector = validateRequiredParameters(map.keySet(), algElement);
        if (missingParameterVector.size() > 0) {
            for (int i = 0; i < missingParameterVector.size(); i++) {
                ScriptParameterException spe = new ScriptParameterException(algorithmNode.getAlgorithmName(), algorithmNode.getID(), algorithmNode.getDataNodeRef(), (String) (missingParameterVector.elementAt(i)), "N/A", "Missing required parameter.");
                log.recordParameterError(spe);
            }
            isValid = false;
        }
        while (iter.hasNext()) {
            key = (String) (iter.next());
            value = (String) (map.get(key));
            if (!validateParameter(key, value, algElement, algorithmNode, log)) {
                isValid = false;
            }
        }
        return isValid;
    }

    /** returns the algorithm element given a list and algorithm name
     * @param algName algorithm name
     * @param algList node list to search
     * @return
     */
    private Element getAlgorithmElement(String algName, NodeList algList) {
        Element elem = null;
        Element resultElement = null;
        String name;
        for (int i = 0; i < algList.getLength(); i++) {
            elem = (Element) (algList.item(i));
            if (elem.getAttribute("name").equals(algName)) {
                resultElement = elem;
                break;
            }
        }
        return resultElement;
    }

    /**
     *  Returns true if required parameters are present.
     */
    private Vector validateRequiredParameters(Set keys, Element algElement) {
        Vector requiredKeys = new Vector();
        NodeList params = algElement.getElementsByTagName("param");
        for (int i = 0; i < params.getLength(); i++) {
            if (((Element) (params.item(i))).getAttribute("val_level").equalsIgnoreCase("REQUIRED")) {
                requiredKeys.add(((Element) (params.item(i))).getAttribute("key"));
            }
        }
        Vector keyVector = new Vector(keys);
        Vector missingKeys = new Vector();
        for (int i = 0; i < requiredKeys.size(); i++) {
            if (!keyVector.contains((String) (requiredKeys.elementAt(i)))) missingKeys.add(requiredKeys.elementAt(i));
        }
        return missingKeys;
    }

    /**
     * Returns true if key, value type, and value range are valid.
     */
    private boolean validateParameter(String key, String value, Element algElement, AlgorithmNode algNode, ErrorLog log) {
        NodeList paramList = algElement.getElementsByTagName("param");
        Element paramElement = getParameterElement(paramList, key);
        boolean result = true;
        if (paramElement == null) {
            ScriptParameterException spe = createScriptParameterException(algNode, paramElement, value, "Invalid key. Key name not recognized.");
            log.recordParameterError(spe);
            return false;
        }
        if (!validateValueType(value, paramElement, algElement, log)) {
            ScriptParameterException spe = createScriptParameterException(algNode, paramElement, value, "Incorrect value type.");
            log.recordParameterError(spe);
            result = false;
        }
        if (!validateValueConstraints(algNode.getAlgorithmName(), key, value)) {
        }
        return result;
    }

    /** Creates a script parameter exception
     * @return
     * @param algNode algorithm node
     * @param paramElement parameter element
     * @param currValue value
     * @param message exception message
     */
    private ScriptParameterException createScriptParameterException(AlgorithmNode algNode, Element paramElement, String currValue, String message) {
        ScriptParameterException spe = new ScriptParameterException(algNode.getAlgorithmName(), algNode.getID(), algNode.getDataNodeRef(), paramElement.getAttribute("key"), currValue, message);
        return spe;
    }

    /** Create exception
     * @param algNode algorithm node
     * @param message exception message
     * @return
     */
    private ScriptParameterException createScriptParameterException(AlgorithmNode algNode, String message) {
        ScriptParameterException spe = new ScriptParameterException(algNode.getAlgorithmName(), algNode.getID(), algNode.getDataNodeRef(), "N/A", "N/A", message);
        return spe;
    }

    /** Returns the parameter element given a node list
     * and the parameter key.
     * @param list list
     * @param key key to search on.
     * @return
     */
    private Element getParameterElement(NodeList list, String key) {
        Element elem = null;
        for (int i = 0; i < list.getLength(); i++) {
            elem = (Element) (list.item(i));
            if (elem.getAttribute("key").equals(key)) {
                break;
            }
        }
        return elem;
    }

    /** Validate type
     * @param value value
     * @param paramElement parameter element
     * @param algElement algorithm element
     * @param log error log
     * @return
     */
    private boolean validateValueType(String value, Element paramElement, Element algElement, ErrorLog log) {
        String type = paramElement.getAttribute("value_type");
        boolean isValid = true;
        try {
            if (type.equals("boolean")) {
                if (!value.equals("true") && !value.equals("false")) isValid = true;
            } else if (type.equals("int")) {
                Integer.parseInt(value);
            } else if (type.equals("long")) {
                Long.parseLong(value);
            } else if (type.equals("float")) {
                Float.parseFloat(value);
            } else if (type.equals("double")) {
                Double.parseDouble(value);
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return isValid;
    }

    /** Validate value's constraints
     * @param algName Algorithm name
     * @param key key for parameter to validate
     * @param value value to check
     * @return
     */
    private boolean validateValueConstraints(String algName, String key, String value) {
        ParameterAttributes atts = this.getParameterAttributes(algName, key);
        if (atts == null || !atts.hasConstraints()) return true;
        return checkConstraints(value, atts.getValueType(), atts.getMin(), atts.getMax());
    }

    /** Check the value against constraints. Returns true if
     * passes.
     * @param val Value
     * @param type Type attribute
     * @param min min
     * @param max max
     * @return
     */
    private boolean checkConstraints(String val, String type, String min, String max) {
        if (type.equals("int")) {
            int value = Integer.parseInt(val);
            if (!min.equals("") && !max.equals("")) return (value >= Integer.parseInt(min) && value <= Integer.parseInt(max)); else if (!max.equals("")) return (value <= Integer.parseInt(max)); else if (!min.equals("")) return (value >= Integer.parseInt(min));
            return true;
        } else if (type.equals("float")) {
            float value = Float.parseFloat(val);
            if (!min.equals("") && !max.equals("")) return (value >= Float.parseFloat(min) && value <= Float.parseFloat(max)); else if (!max.equals("")) return (value <= Float.parseFloat(max)); else if (!min.equals("")) return (value >= Float.parseFloat(min));
            return true;
        } else if (type.equals("long")) {
            long value = Long.parseLong(val);
            if (!min.equals("") && !max.equals("")) return (value >= Long.parseLong(min) && value <= Long.parseLong(max)); else if (!max.equals("")) return (value <= Long.parseLong(max)); else if (!min.equals("")) return (value >= Long.parseLong(min));
            return true;
        } else if (type.equals("double")) {
            double value = Double.parseDouble(val);
            if (!min.equals("") && !max.equals("")) return (value >= Double.parseDouble(min) && value <= Double.parseDouble(max)); else if (!max.equals("")) return (value <= Double.parseDouble(max)); else if (!min.equals("")) return (value >= Double.parseDouble(min));
            return true;
        }
        return true;
    }

    /** Returns an html string table of valid parameters
     * for the algorithm named.
     * @return
     * @param algName algorithm name */
    public String getValidParameterTable(String algName) {
        if (!isEnabled()) return null;
        String table = null;
        String key;
        String valueType;
        String min;
        String max;
        String valStatus;
        Element algElement = findAlgorithmElement(algName);
        Element paramElement;
        Element constraint;
        if (algElement != null) {
            Element paramList = (Element) (algElement.getElementsByTagName("param_list").item(0));
            if (paramList == null) return null;
            NodeList params = paramList.getElementsByTagName("param");
            table = "";
            table = "<h2>Valid Script Parameters for " + algName + "</h2>";
            table += "<p>Note: Parameters that are not listed as \"Always\" required usually depend on the value of" + " other entered parameters to determine if they are required.</p>";
            table += "<table border=3><th>Key</th><th>Value Type</th><th>Min</th><th>Max</th><th>Required</th>";
            for (int i = 0; i < params.getLength(); i++) {
                paramElement = (Element) (params.item(i));
                NodeList constraintList = paramElement.getElementsByTagName("constraint");
                key = paramElement.getAttribute("key");
                valueType = paramElement.getAttribute("value_type");
                valStatus = paramElement.getAttribute("val_level");
                min = " ";
                max = " ";
                if (constraintList != null && constraintList.getLength() > 0) {
                    constraint = (Element) (constraintList.item(0));
                    min = constraint.getAttribute("min");
                    max = constraint.getAttribute("max");
                }
                table += "<tr><td>" + key + "</td><td>" + valueType + "</td><td>" + min + "</td><td>" + max + "</td><td>" + (valStatus.equalsIgnoreCase("REQUIRED") ? "Always" : "Dependent") + "</td></tr>";
            }
            table += "</table>";
        }
        return table;
    }

    /** returns a hash table for specified algorithm.
     * @param algName algorithm name
     * @return
     */
    public Hashtable getParameterHash(String algName) {
        Element algElement = findAlgorithmElement(algName);
        if (algElement == null) {
            return null;
        }
        Element paramList = (Element) (algElement.getElementsByTagName("param_list").item(0));
        Hashtable paramHash = new Hashtable();
        NodeList list = paramList.getElementsByTagName("param");
        String key;
        String valueType;
        String valueRequirements;
        boolean hasConstraints;
        String min;
        String max;
        Element paramElement;
        NodeList constrList;
        ParameterAttributes attr;
        Element constrElement;
        for (int i = 0; i < list.getLength(); i++) {
            paramElement = (Element) (list.item(i));
            key = paramElement.getAttribute("key");
            valueType = paramElement.getAttribute("value_type");
            valueRequirements = paramElement.getAttribute("val_level");
            constrList = null;
            constrList = paramElement.getElementsByTagName("constraint");
            if (constrList == null || constrList.getLength() == 0) {
                attr = new ParameterAttributes(key, valueType, valueRequirements);
            } else {
                constrElement = (Element) (constrList.item(0));
                min = constrElement.getAttribute("min");
                max = constrElement.getAttribute("max");
                attr = new ParameterAttributes(key, valueType, valueRequirements, min, max);
            }
            paramHash.put(key, attr);
        }
        return paramHash;
    }

    /** Returns a <CODE>ParameterAttributes</CODE> object for algorithm and
     * specified key.
     * @param algName
     * @param key
     * @return
     */
    public ParameterAttributes getParameterAttributes(String algName, String key) {
        Hashtable hash = getParameterHash(algName);
        if (hash == null) return null;
        return (ParameterAttributes) (hash.get(key));
    }

    /** Displays a dialog warning that loaded algorithms are dependent on the specific data
     * set.  Parameters such as grouping experiments is an example
     * @param tree the script tree to evaluate
     * @param manager the script manager     
     */
    public void checkAlgorithmsForDataDependance(ScriptTree tree, ScriptManager manager) {
        AlgorithmSet[] algSet = tree.getAlgorithmSets();
        Element algElement;
        String algName;
        Vector algNames = new Vector();
        for (int i = 0; i < algSet.length; i++) {
            for (int j = 0; j < algSet[i].getAlgorithmCount(); j++) {
                algName = algSet[i].getAlgorithmNodeAt(j).getAlgorithmName();
                algElement = findAlgorithmElement(algName);
                if (algElement != null) {
                    if (algElement.getAttribute("input_data_dep").equals("true")) {
                        if (!algNames.contains(algName)) algNames.addElement(algName);
                    }
                }
            }
        }
        if (algNames.size() > 0) {
            JTextPane pane = new JTextPane();
            pane.setContentType("text/html");
            pane.setEditable(false);
            pane.setMargin(new Insets(10, 10, 10, 5));
            String text = "<html><center><h2>Data Dependent Script Algorithms</h2></center>";
            text += "<hr size=3>The processing of the following algorithm";
            if (algNames.size() == 1) text += " is "; else text += "s are ";
            text += "dependent on the number and order of the loaded experiments.<br>";
            text += "<center><b>";
            for (int i = 0; i < algNames.size(); i++) {
                text += ((String) (algNames.elementAt(i))) + "<br>";
            }
            text += "</b><br></center>";
            text += "Please verify that the data set (number of loaded experiments and their order) is appropriate for the listed algorithms.</html>";
            pane.setText(text);
            JScrollPane scroll = new JScrollPane(pane);
            scroll.setViewportBorder(BorderFactory.createLineBorder(Color.black));
            scroll.getViewport().setViewSize(new Dimension(450, 275));
            scroll.setPreferredSize(new Dimension(450, 275));
            JOptionPane.showMessageDialog(manager.getFrame(), scroll, "Data Set Dependent Algorithms", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /** Returns the algorithm element given an algorithm name
     * @param algName
     * @return
     */
    private Element findAlgorithmElement(String algName) {
        NodeList list = validationRoot.getElementsByTagName("script_algorithm");
        Element algElement = null;
        String algElementName;
        for (int i = 0; i < list.getLength(); i++) {
            algElementName = null;
            algElement = (Element) (list.item(i));
            algElementName = algElement.getAttribute("name");
            if (algElementName != null && algElementName.equals(algName)) return algElement;
        }
        return null;
    }

    /** records a warning
     * @param e
     * @throws SAXException
     */
    public void warning(SAXParseException e) throws SAXException {
    }

    /** records a warning.
     */
    public void error(SAXParseException e) throws SAXException {
    }

    /** records a fatal error
     */
    public void fatalError(SAXParseException e) throws SAXException {
    }
}
