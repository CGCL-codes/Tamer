package iwork.icrafter.util;

import java.io.*;
import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import iwork.state.*;
import iwork.icrafter.system.IllegalSDLException;

/**
 *
 * <b> This class is intended to be used only by templates </b> Helper
 * class to the template processor that does SDL parsing. This parser
 * maintains state, in other words, it is assumed that the document
 * will be parsed in a certain sequence (as shown below) -
 * 
 * numValidMethods = getNumMethods
 * for each method i 1..numValidMethods :
 *   get details about method i
 *   numParams = getNumParams
 *   for each parameter for method i
 *       get details about the parameter
 *   get return type
 *
 * If methods are called out of sync with the valid sequence,
 * an exception is thrown. 
 * 
 * Maintaining state here makes the template scripts that parse SDL
 * easier to write. To see how this class may be used, refer the
 * Generic template "Generic.py".
 *
 * Efficiency has been compromised here for ease of implementation esp
 * since this code is likely to change!
 *
 **/
public class StatefulSDLParser {

    public final boolean DEBUG = true;

    Document doc = null;

    /** The following variables maintain state regarding where in the
        parsing we are */
    NodeList methodList = null;

    NodeList paramList = null;

    int curMethodNum = -1;

    Element curMethod = null;

    int curParamNum = -1;

    Element curParam = null;

    int numValidMethods = 0;

    int numAllMethods = 0;

    int numParams = 0;

    /** Type of current parameter */
    String type = null;

    /** Type element for current parameter */
    Element typeElem = null;

    /** If the current parameter is an enumeration, this var keeps
	track of number of values seen so far */
    int enumIndex;

    public StatefulSDLParser(String SDL) throws IllegalSDLException {
        try {
            DOMParser dp = new DOMParser();
            dp.parse(new InputSource(new StringReader(SDL)));
            doc = dp.getDocument();
            Element opnsElem = XMLHelper.GetChildElement(doc.getDocumentElement(), "operations");
            methodList = XMLHelper.GetChildrenByTagName(opnsElem, "operation");
            numAllMethods = methodList.getLength();
            for (int i = 0; i < methodList.getLength(); i++) {
                Element methodElem = (Element) methodList.item(i);
                if (checkMethodSDL(methodElem)) numValidMethods++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    /**
     * Checks if the SDL for this method element is properly formatted
     * this also involves checking that each parameter element is well
     * formatted checks only if what is required is present
     **/
    public static boolean checkMethodSDL(Element methodElem) {
        try {
            String name;
            String description;
            name = XMLHelper.GetChildText(methodElem, "name");
            description = XMLHelper.GetChildText(methodElem, "description");
            Element paramsElem = XMLHelper.GetChildElement(methodElem, "parameters");
            NodeList params = XMLHelper.GetChildrenByTagName(methodElem, "parameter");
            for (int i = 0; i < params.getLength(); i++) {
                Element paramElem = (Element) params.item(i);
                name = XMLHelper.GetChildText(paramElem, "name");
                description = XMLHelper.GetChildText(paramElem, "description");
                String type = XMLHelper.GetChildText(paramElem, "type");
                Element typeElem = XMLHelper.GetChildElement(paramElem, "type");
                if (type.equals(StateConstants.INT) || type.equals(StateConstants.FLOAT) || type.equals(StateConstants.STRING)) {
                } else if (type.equals(ICrafterConstants.INTENUM)) {
                    NodeList values = XMLHelper.GetChildrenByTagName(typeElem, "value");
                    for (int j = 0; j < values.getLength(); j++) {
                        Element value = (Element) values.item(j);
                        String sval = XMLHelper.GetText(value);
                        Integer val = new Integer(sval);
                    }
                } else if (type.equals(ICrafterConstants.FLOATENUM)) {
                    NodeList values = XMLHelper.GetChildrenByTagName(typeElem, "value");
                    for (int j = 0; j < values.getLength(); j++) {
                        Element value = (Element) values.item(j);
                        String sval = XMLHelper.GetText(value);
                        Float val = new Float(sval);
                    }
                } else if (type.equals(ICrafterConstants.STRINGENUM)) {
                    NodeList values = XMLHelper.GetChildrenByTagName(typeElem, "value");
                    for (int j = 0; j < values.getLength(); j++) {
                        Element value = (Element) values.item(j);
                        String sval = XMLHelper.GetText(value);
                    }
                } else if (type.equals(ICrafterConstants.INTRANGE)) {
                    Integer low = new Integer(XMLHelper.GetChildText(typeElem, "low"));
                    Integer high = new Integer(XMLHelper.GetChildText(typeElem, "high"));
                } else if (type.equals(ICrafterConstants.FLOATRANGE)) {
                    Float low = new Float(XMLHelper.GetChildText(typeElem, "low"));
                    Float high = new Float(XMLHelper.GetChildText(typeElem, "high"));
                } else {
                    Utils.debug("StatefulSDLParser", "Unknown type in checkMethodSDL " + type + " for parameter " + i + " for method " + name);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getNumValidMethods() {
        return numValidMethods;
    }

    public String getMethod() throws IllegalSDLException {
        try {
            while (true) {
                curParamNum = -1;
                curMethodNum++;
                if (curMethodNum >= numAllMethods) {
                    return null;
                }
                curMethod = (Element) methodList.item(curMethodNum);
                if (!checkMethodSDL(curMethod)) continue;
                Element paramsElem = XMLHelper.GetChildElement(curMethod, "parameters");
                if (paramsElem == null) {
                    paramList = null;
                } else {
                    paramList = XMLHelper.GetChildrenByTagName(paramsElem, "parameter");
                }
                return XMLHelper.GetChildText(curMethod, "name");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    public String getMethodDescription() throws IllegalSDLException {
        try {
            return XMLHelper.GetChildText(curMethod, "description");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    public String getMethodReturnType() throws IllegalSDLException {
        try {
            return XMLHelper.GetChildText(curMethod, "return");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    public int getNumParams() throws IllegalSDLException {
        try {
            if (paramList == null) return 0;
            numParams = paramList.getLength();
            return numParams;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    public String getParam() throws IllegalSDLException {
        try {
            curParamNum++;
            if (curParamNum >= numParams) {
                return null;
            }
            curParam = (Element) paramList.item(curParamNum);
            type = XMLHelper.GetChildText(curParam, "type");
            typeElem = XMLHelper.GetChildElement(curParam, "type");
            enumIndex = 0;
            return XMLHelper.GetChildText(curParam, "name");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    public String getParamDescription() throws IllegalSDLException {
        try {
            return XMLHelper.GetChildText(curParam, "description");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    public String getParamType() {
        return type;
    }

    /**
     * Get the min end in the float range 
     */
    public float getFRMin() throws IllegalSDLException {
        try {
            if (!type.equals(ICrafterConstants.FLOATRANGE)) {
                throw new RuntimeException("Illegal method call on StatefulSDLParser");
            }
            return (new Float(XMLHelper.GetChildText(typeElem, "low"))).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    /**
     * Get the max end in the float range 
     */
    public float getFRMax() throws IllegalSDLException {
        try {
            if (!type.equals(ICrafterConstants.FLOATRANGE)) {
                throw new RuntimeException("Illegal method call on StatefulSDLParser");
            }
            return (new Float(XMLHelper.GetChildText(typeElem, "high"))).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    /**
     * Get the min end in the int range 
     */
    public int getIRMin() throws IllegalSDLException {
        try {
            if (!type.equals(ICrafterConstants.INTRANGE)) {
                throw new RuntimeException("Illegal method call on StatefulSDLParser");
            }
            return (new Integer(XMLHelper.GetChildText(typeElem, "low"))).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    /**
     * Get the max end in the int range 
     */
    public int getIRMax() throws IllegalSDLException {
        try {
            if (!type.equals(ICrafterConstants.INTRANGE)) {
                throw new RuntimeException("Illegal method call on StatefulSDLParser");
            }
            return (new Integer(XMLHelper.GetChildText(typeElem, "high"))).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    /**
     * number of values in the enumeration 
     */
    public int getNumVals() throws IllegalSDLException {
        try {
            if (!type.equals(ICrafterConstants.INTENUM) && !type.equals(ICrafterConstants.FLOATENUM) && !type.equals(ICrafterConstants.STRINGENUM)) {
                throw new RuntimeException("Illegal method call on StatefulSDLParser");
            }
            NodeList values = XMLHelper.GetChildrenByTagName(typeElem, "value");
            return values.getLength();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    /**
     * Get the next int in the enumeration                    
     */
    public int getNextInt() throws IllegalSDLException {
        try {
            if (!type.equals(ICrafterConstants.INTENUM)) {
                throw new RuntimeException("Illegal method call on StatefulSDLParser");
            }
            NodeList values = XMLHelper.GetChildrenByTagName(typeElem, "value");
            String sval = XMLHelper.GetText((Element) values.item(enumIndex));
            enumIndex++;
            return (new Integer(sval)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    /**
     * Get the next float in the enumeration
     */
    public float getNextFloat() throws IllegalSDLException {
        try {
            if (!type.equals(ICrafterConstants.FLOATENUM)) {
                throw new RuntimeException("Illegal method call on StatefulSDLParser");
            }
            NodeList values = XMLHelper.GetChildrenByTagName(typeElem, "value");
            String sval = XMLHelper.GetText((Element) values.item(enumIndex));
            enumIndex++;
            return (new Float(sval)).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }

    public String getNextString() throws IllegalSDLException {
        try {
            if (!type.equals(ICrafterConstants.STRINGENUM)) {
                throw new RuntimeException("Illegal method call on StatefulSDLParser");
            }
            NodeList values = XMLHelper.GetChildrenByTagName(typeElem, "value");
            return XMLHelper.GetText((Element) values.item(enumIndex++));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalSDLException(e);
        }
    }
}
