package org.bibop.xml.xforge.components.mappers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.lang.*;
import java.math.*;
import org.bibop.xml.xforge.XForgeException;
import org.bibop.xml.xforge.XForgeContext;
import org.bibop.xml.xforge.VariablesBroker;
import org.bibop.xml.xforge.components.AbstractXForgeComponentMapper;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A generic mapper that maps via reflection your object to an xforge component
 */
public class XForgeReflectionMapper extends AbstractXForgeComponentMapper implements Configurable {

    protected Object mappedObject = null;

    protected Map methodstag = new HashMap();

    /**
     * Configure the object
     * @param conf
     * @exception ConfigurationException
     */
    public void configure(Configuration conf) throws ConfigurationException {
        if (conf != null) {
            Configuration[] tagsmap = conf.getChildren("mapping");
            int it = 0;
            try {
                while ((tagsmap != null) && (it < tagsmap.length)) {
                    Configuration tagconf = tagsmap[it];
                    String methodName = tagconf.getChild("from").getValue();
                    String tagName = tagconf.getChild("to").getValue();
                    if ((methodName == null) || (methodName.length() == 0)) {
                        throw new ConfigurationException("FATAL: Method name hasn't been specified !!!");
                    }
                    if ((tagName == null) || (tagName.length() == 0)) {
                        throw new ConfigurationException("FATAL: Tag name for method " + methodName + " hasn't been specified !!!");
                    }
                    this.methodstag.put(methodName, tagName);
                    it++;
                }
            } catch (Exception tagex) {
                throw new ConfigurationException("FATAL: An exception has occured while configuring Reflection Mapper.\n" + "Exception occured: " + tagex.getMessage());
            }
        }
    }

    /**
     * sets the class that is mapped
     * @param className
     * @exception XForgeException
     */
    public void setMappedComponentClass(String className) throws XForgeException {
        try {
            this.mappedObject = Class.forName(className).newInstance();
        } catch (Exception ex) {
            throw new XForgeException("An error ha occured while instantiating mapped object: " + className + " Error occured: " + ex.getMessage());
        }
    }

    /**
     * the method called by the mapped component
     * @exception XForgeException
     */
    public void run() throws XForgeException {
        super.run();
        if (this.mappedObject == null) {
            throw new XForgeException("FATAL: Mapped Object cannot be null !!!");
        }
        Method[] methods = this.mappedObject.getClass().getMethods();
        Iterator methodnames = this.parameters.keySet().iterator();
        while (methodnames.hasNext()) {
            String methodName = (String) methodnames.next();
            LinkedList argumentsList = this.parseArguments((String) this.parameters.get(methodName));
            Method thisMethod = this.getMethodByName(methods, methodName, argumentsList);
            Object methodResult = this.invokeMethod(thisMethod, argumentsList);
            Class returnType = thisMethod.getReturnType();
            if (!returnType.isAssignableFrom(Void.TYPE)) {
                this.objectToSax(methodName, methodResult);
            }
        }
    }

    /**
     * utility to print a method result
     * @param methodName
     * @param methodResult
     * @exception XForgeException
     */
    protected void objectToSax(String methodName, Object methodResult) throws XForgeException {
        String result = "";
        if (methodResult != null) {
            result = methodResult.toString();
        } else {
            result = "null";
        }
        try {
            if (this.methodstag.containsKey(methodName)) {
                methodName = (String) this.methodstag.get(methodName);
            }
            this.contentHandler.startElement("", "", methodName, new AttributesImpl());
            this.contentHandler.characters(result.toCharArray(), 0, result.length());
            this.contentHandler.endElement("", "", methodName);
        } catch (SAXException saxex) {
            throw new XForgeException(saxex.getMessage());
        }
    }

    /**
     * utility to parse the method arguments
     * @param toparse
     * @return arguments parsed
     * @exception XForgeException
     */
    protected LinkedList parseArguments(String toparse) throws XForgeException {
        LinkedList params = new LinkedList();
        if ((toparse == null) || (toparse.length() == 0)) {
            return params;
        }
        try {
            toparse = toparse.substring(toparse.indexOf("(") + 1, toparse.indexOf(")"));
            StringTokenizer tokens = new StringTokenizer(toparse, ",");
            while (tokens.hasMoreElements()) {
                String current = tokens.nextToken();
                params.add(current);
            }
        } catch (Exception formatex) {
            throw new XForgeException("FATAL: Illegal parameters format.");
        }
        return params;
    }

    /**
     * get a method by the name and arguments
     * @param methods
     * @param methodName
     * @param arguments
     * @return the method
     * @exception XForgeIllegalMethodException
     */
    protected Method getMethodByName(Method[] methods, String methodName, LinkedList arguments) throws XForgeIllegalMethodException {
        int i = 0;
        boolean found = false;
        while ((i < methods.length) && (!found)) {
            if (methods[i].getName().equals(methodName)) {
                Class[] argsTypes = methods[i].getParameterTypes();
                if (argsTypes.length == arguments.size()) {
                    found = true;
                } else i++;
            } else i++;
        }
        if (!found) {
            throw new XForgeIllegalMethodException("FATAL: Method " + methodName + " not found !!!");
        }
        return methods[i];
    }

    /**
     * invoke a method
     * @param toInvoke
     * @param arguments
     * @return
     * @exception XForgeIllegalMethodException
     */
    protected Object invokeMethod(Method toInvoke, LinkedList arguments) throws XForgeIllegalMethodException {
        Class[] argsTypes = toInvoke.getParameterTypes();
        Object[] castedArguments = this.convertArguments(arguments, argsTypes);
        try {
            return toInvoke.invoke(this.mappedObject, castedArguments);
        } catch (Exception invokeex) {
            throw new XForgeIllegalMethodException("An error has occured while invoking method: " + toInvoke.getName() + "\nException occured: " + invokeex.getMessage());
        }
    }

    /**
     * put your documentation comment here
     * @param arguments
     * @param argTypes
     * @return
     * @exception XForgeIllegalArgumentsException
     */
    protected Object[] convertArguments(LinkedList arguments, Class[] argTypes) throws XForgeIllegalArgumentsException {
        Object[] castedArguments = new Object[arguments.size()];
        for (int i = 0; i < castedArguments.length; i++) {
            castedArguments[i] = this.stringToObject((String) arguments.get(i), argTypes[i]);
        }
        return castedArguments;
    }

    /**
     * put your documentation comment here
     * @param value
     * @param finalType
     * @return
     * @exception XForgeIllegalArgumentCastException
     */
    protected Object stringToObject(String value, Class finalType) throws XForgeIllegalArgumentCastException {
        if (value == null) {
            return null;
        }
        try {
            if (finalType.isAssignableFrom(Integer.TYPE)) {
                return new Integer(value);
            } else if (finalType.isAssignableFrom(Long.TYPE)) {
                return new Long(value);
            } else if (finalType.isAssignableFrom(Short.TYPE)) {
                return new Short(value);
            } else if (finalType.isAssignableFrom(Float.TYPE)) {
                return new Float(value);
            } else if (finalType.isAssignableFrom(Double.TYPE)) {
                return new Double(value);
            } else if (finalType.isAssignableFrom(Byte.TYPE)) {
                return new Byte(value);
            } else if (finalType.isAssignableFrom(Class.forName("java.math.BigInteger"))) {
                return new BigInteger(value);
            } else if (finalType.isAssignableFrom(Class.forName("java.math.BigDecimal"))) {
                return new BigDecimal(value);
            } else if (finalType.isAssignableFrom(Boolean.TYPE)) {
                return new Boolean(value);
            } else if (finalType.isAssignableFrom(Character.TYPE)) {
                return new Character(value.charAt(0));
            } else if (finalType.isAssignableFrom(Class.forName("java.lang.String"))) {
                return value;
            } else if (finalType.isAssignableFrom(Class.forName("java.lang.StringBuffer"))) {
                return new StringBuffer(value);
            } else {
                throw new XForgeException(finalType.getName() + " not supported.");
            }
        } catch (Exception ex) {
            throw new XForgeIllegalArgumentCastException("FATAL: Cannot convert parameter value (" + value + ") " + "to Class: " + finalType.getName() + "\n" + "Exception occured: " + ex.getMessage());
        }
    }

    class XForgeIllegalMethodException extends XForgeException {

        /**
         * put your documentation comment here
         * @param         String msg
         */
        public XForgeIllegalMethodException(String msg) {
            super(msg);
        }
    }

    class XForgeIllegalArgumentsException extends XForgeIllegalMethodException {

        /**
         * put your documentation comment here
         * @param         String msg
         */
        public XForgeIllegalArgumentsException(String msg) {
            super(msg);
        }
    }

    class XForgeIllegalArgumentCastException extends XForgeIllegalArgumentsException {

        /**
         * put your documentation comment here
         * @param         String msg
         */
        public XForgeIllegalArgumentCastException(String msg) {
            super(msg);
        }
    }
}
