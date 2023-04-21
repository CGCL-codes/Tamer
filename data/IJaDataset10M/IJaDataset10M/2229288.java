package org.formaria.editor.netbeans.visualizer;

import org.formaria.editor.visualizer.VisualiserDebuggerModel;
import org.formaria.aria.data.DataModel;
import org.formaria.aria.helper.NumberFormatter;
import org.netbeans.api.debugger.jpda.JPDADebugger;
import org.netbeans.api.debugger.jpda.Variable;
import org.netbeans.api.debugger.jpda.ObjectVariable;

/**
 * A wrapper for the debugger node.
 *
 * <p> Copyright (c) Formaria Ltd., 2001-2006, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.9 $</p>
 */
public class NbDebuggerModel extends VisualiserDebuggerModel {

    private JPDADebugger debugger;

    private ObjectVariable modelNode;

    private static Variable[] zeroArgsArray = new Variable[0];

    private static Variable[] oneArgsArray = new Variable[1];

    private static Variable[] twoArgsArray = new Variable[2];

    private static Variable[] threeArgsArray = new Variable[3];

    /** Creates a new instance of JPDAModelNode */
    public NbDebuggerModel(JPDADebugger jpdaDebugger, ObjectVariable objVar) {
        debugger = jpdaDebugger;
        modelNode = objVar;
    }

    /**
     * Calls the <code>getToStringValue()</code> method on the encapsulated object
     * and returns its value. This method is used to determine
     * class which instance is wrapped by <code>JPDAModelNode</code>
     * object.
     * @return String containing name of the wrapped class.
     */
    public String getNodeString() {
        String val = null;
        try {
            val = modelNode.getToStringValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public void setObjectVariable(DataModel node) {
        try {
            modelNode = (ObjectVariable) debugger.evaluate("\"" + node + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Reset the whole model, giving a new root node and a new hierarchy
   * 'deprecated since 1.0.3
   * @return The base model
   */
    public DataModel reset() {
        throw new UnsupportedOperationException();
    }

    public void setTagName(String name) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + name + "\"");
            modelNode.invokeMethod("setTagName", "(Ljava/lang/String;)V", oneArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Gets the model element tag name, e.g. 'Component' from the XML fragment
   * <Component ....
   * @return the model name
   */
    public String getTagName() {
        try {
            Variable var2 = modelNode.invokeMethod("getTagName", "()Ljava/lang/String;", zeroArgsArray);
            if (var2 != null) return stripQuotes(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Determine if there is no id set for this model in the datasource
   * @return true if there was no name for the element in the DataSource, an
   * example of this is the annonymouse nodes used to represent the record of
   * a table e.g. <tr><td>...</td><td>...</td></tr>
   */
    public boolean hasAutoId() {
        try {
            Variable var = modelNode.invokeMethod("hasAutoId", "()Z;", zeroArgsArray);
            return "true".equals(var.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
   * Gets the value of the ID attribute
   * @return the ID attribute
   */
    public String getId() {
        try {
            Variable var = modelNode.invokeMethod("getId", "()Ljava/lang/String;", zeroArgsArray);
            return stripQuotes(var.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Sets the ID attribute
   * @param newId the new name
   */
    public void setId(String newId) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + newId + "\"");
            Variable var = modelNode.invokeMethod("setId", "(Ljava/lang/String;)V", oneArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Get the value of the element located at the path in the element parameter
   * If the attribName parameter is not null we get the value of the
   * attributeValues
   * @param element The path to the DataModel we require
   * @return The value of the DataModel or the attribute
   */
    public Object get(String element) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + element + "\"");
            Variable var2 = modelNode.invokeMethod("get", "(Ljava/lang/String;)Ljava/lang/Object;", oneArgsArray);
            return new NbDebuggerModel(debugger, (ObjectVariable) var2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Set the named attribute value of this DataModel node. If the attribName is
   * null then this node's value is updated.
   * @param elementName The path to the DataModel in the format 'base/foo
   * @param newObject The new value of the DataModel
   */
    public void set(String elementName, Object newObject) {
        try {
            twoArgsArray[0] = debugger.evaluate("\"" + elementName + "\"");
            twoArgsArray[1] = debugger.evaluate("\"" + newObject.toString() + "\"");
            modelNode.invokeMethod("set", "(Ljava/lang/String;Ljava/lang/Object;)V", twoArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Returns the index of the attribiteNames array whose value is the same
   * as the attribName
   * @param attribName The name of the attribute we are trying to locate
   * @return The index of the attributeNames array containg the name
   */
    public int getAttribute(String attribName) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + attribName + "\"");
            Variable var = modelNode.invokeMethod("getAttribute", "(Ljava/lang/String;)I", oneArgsArray);
            return Integer.parseInt(stripQuotes(var.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
   * Sets the attribute value
   * @param i The index of the attributeValues array whose value we want
   * @param value the value object
   */
    public void setAttribValue(int i, Object value) {
        try {
            twoArgsArray[0] = debugger.evaluate(String.valueOf(i));
            twoArgsArray[1] = debugger.evaluate("\"" + value.toString() + "\"");
            modelNode.invokeMethod("setAttribValue", "(ILjava/lang/Object;)V", twoArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Sets the attribute name and value
   * @param i The index of the attributeValues array whose value we want
   * @param attribName the name of the attribute
   * @param value the value object
   */
    public void setAttribValue(int i, String attribName, Object value) {
        try {
            threeArgsArray[0] = debugger.evaluate(String.valueOf(i));
            threeArgsArray[1] = debugger.evaluate("\"" + attribName + "\"");
            threeArgsArray[2] = debugger.evaluate("\"" + value + "\"");
            modelNode.invokeMethod("setAttribValue", "(ILjava/lang/String;Ljava/lang/Object;)V", threeArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Get the DataModel at element i
   * @param i The index of the values array
   * @return The DataModel at location i
   */
    public DataModel get(int i) {
        try {
            oneArgsArray[0] = debugger.evaluate("" + i);
            Variable var2 = modelNode.invokeMethod("get", "(I)Lorg/formaria/aria/data/DataModel;", oneArgsArray);
            return new NbDebuggerModel(debugger, (ObjectVariable) var2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * retrieve the parent DataModel of this DataModel instance
   * @return The parent DataModel to which this instance belongs
   */
    public DataModel getParent() {
        try {
            Variable var2 = modelNode.invokeMethod("getParent", "()Lorg/formaria/aria/data/DataModel;", zeroArgsArray);
            return new NbDebuggerModel(debugger, (ObjectVariable) var2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * gets the value attribute
   * @return the value of the model
   */
    public Object get() {
        try {
            Variable var2 = modelNode.invokeMethod("get", "()Ljava/lang/Object;", zeroArgsArray);
            if (var2.getType().equals("java.lang.String")) return stripQuotes(var2.getValue()); else return new NbDebuggerModel(debugger, (ObjectVariable) var2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Sets the model value
   * @param s the new value
   */
    public void set(Object s) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + s.toString() + "\"");
            modelNode.invokeMethod("set", "(Ljava/lang/Object;)V", oneArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Used for elements which need a name assigned temporarily because one doesn't
   * exist in the DataSource.
   * @param b true if there was no name in the DataSource
   */
    public void hasAutoId(boolean b) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + b + "\"");
            modelNode.invokeMethod("hasAutoId", "(Z)V", oneArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Retrieve the name of the attribute at the specified index
   * @param i The index of the attributeNames array whose value we want
   * @return The string value of the attributeNames array at position i
   */
    public String getAttribName(int i) {
        try {
            oneArgsArray[0] = debugger.evaluate("" + i);
            Variable var2 = modelNode.invokeMethod("getAttribName", "(I)Ljava/lang/String;", oneArgsArray);
            if (var2 != null) return stripQuotes(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Retrieve the value of the attribute at the specified index, convert it to a
   * String and return it.
   * @param i The index of the attributeValues array whose value we want
   * @return The string value of the attributeValues array at position i
   */
    public String getAttribValueAsString(int i) {
        try {
            oneArgsArray[0] = debugger.evaluate("" + i);
            if (modelNode.getType().equals("java.lang.String")) {
                String attribValue = modelNode.getValue();
                return stripQuotes(attribValue);
            }
            Variable var2 = modelNode.invokeMethod("getAttribValueAsString", "(I)Ljava/lang/String;", oneArgsArray);
            if (var2 != null) {
                String attribValue = var2.getValue();
                return stripQuotes(attribValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Retrieve the value of the attribute at the specified index and return it.
   * @param i The index of the attributeValues array whose value we want
   * @return The string value of the attributeValues array at position i
   */
    public Object getAttribValue(int i) {
        try {
            oneArgsArray[0] = debugger.evaluate("" + i);
            Variable var2 = modelNode.invokeMethod("getAttribValue", "(I)Ljava/lang/Object;", oneArgsArray);
            return new NbDebuggerModel(debugger, (ObjectVariable) var2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Retrieve the value of the attribute at the specified index, convert it to a
   * double and return it.
   * @param i The index of the attributeValues array whose value we want
   * @return The double value of the attributeValues array at position i
   * @deprecated use getAttribValueAsDouble( i, decimalSeparator, groupingSeparator ) 
   * instead, if the locale is different from the locale used to write the values 
   * to the model, then the parsed value may be incorrect.
   */
    public double getAttribValueAsDouble(int i) {
        try {
            oneArgsArray[0] = debugger.evaluate("" + i);
            Variable var2 = modelNode.invokeMethod("getAttribValueAsDouble", "(I)D", oneArgsArray);
            return Double.parseDouble(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
   * Convert the attribute at the specified index to a double and return it
   * @param i The index of the attributeValues array whose value we want
   * @param decimalSeparator the decimal separator
   * @param groupingSeparator the grouping (thousands) separator
   * @return The double value of the attributeValues array at position i
   */
    public double getAttribValueAsDouble(int i, char decimalSeparator, char groupingSeparator) {
        try {
            oneArgsArray[0] = debugger.evaluate("" + i);
            Variable var2 = modelNode.invokeMethod("getAttribValueAsDouble", "(I)D", oneArgsArray);
            return NumberFormatter.parseDouble(var2.getValue(), decimalSeparator, groupingSeparator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
   * Retrieve the value of the attribute at the specified index, convert it to an int
   * and return it.
   * @param i The index of the attributeValues array whose value we want
   * @return The int value of the attributeValues array at position i
   */
    public int getAttribValueAsInt(int i) {
        try {
            oneArgsArray[0] = debugger.evaluate("" + i);
            Variable var2 = modelNode.invokeMethod("getAttribValueAsInt", "(I)I", oneArgsArray);
            return Integer.parseInt(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
   * Retrieve the named child, convert it's value to a double and return it.
   * @param elementName The name of the child whose value is required
   * @return the value as a double
   */
    public double getValueAsDouble(String elementName) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + elementName + "\"");
            Variable var2 = modelNode.invokeMethod("getValueAsDouble", "(Ljava/lang/String;)D", oneArgsArray);
            return Double.parseDouble(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
   * Retrieve the named child, convert it's value to an int and return it.
   * @param elementName The name of the child to be retrieved
   * @return the value as an int
   */
    public int getValueAsInt(String elementName) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + elementName + "\"");
            Variable var2 = modelNode.invokeMethod("getValueAsInt", "(Ljava/lang/String;)I", oneArgsArray);
            return Integer.parseInt(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
   * Retrieve the named child, convert it's value to a String and return it.
   * @param elementName The name of the child whose value is required
   * @return the value as a string
   */
    public String getValueAsString(String elementName) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + elementName + "\"");
            Variable var2 = modelNode.invokeMethod("getValueAsString", "(Ljava/lang/String;)Ljava/lang/String;", oneArgsArray);
            return stripQuotes(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Get a hash code for the node.
   * @return the node's hash code
   */
    public int hashCode() {
        try {
            Variable var2 = modelNode.invokeMethod("hashCode", "()I", zeroArgsArray);
            return Integer.parseInt(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
   * Gets the number of immediate children of this node
   * @retrun the number of child nodes
   */
    public int getNumChildren() {
        try {
            Variable var2 = modelNode.invokeMethod("getNumChildren", "()I", zeroArgsArray);
            if ((var2 != null) && (var2.getValue() != null)) return Integer.parseInt(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
   * Gets the number of attributes of this node
   * @return the number of attributes
   */
    public int getNumAttributes() {
        try {
            Variable var2 = modelNode.invokeMethod("getNumAttributes", "()I", zeroArgsArray);
            if ((var2 != null) && (var2.getValue() != null)) return Integer.parseInt(var2.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
   * Set the number of children of this node
   * @param num the new number of children
   */
    public void setNumChildren(int num) {
        try {
            oneArgsArray[0] = debugger.evaluate(String.valueOf(num));
            modelNode.invokeMethod("setNumChildren", "(I)V", oneArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Append a node
   * @param childNode the child node
   */
    public void append(DataModel childNode) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + childNode + "\"");
            modelNode.invokeMethod("append", "(Lorg/formaria/aria/data/DataModel;)V", oneArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Append a new node with the specified name. This method does not replace any
   * existing nodes.
   * @param elementName The immediate path to the DataModel required
   * @return The value of the DataModel or the attribute
   */
    public Object append(String elementName) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + elementName + "\"");
            Variable var = modelNode.invokeMethod("append", "(Ljava/lang/String;)Ljava/lang/Object;", oneArgsArray);
            return new NbDebuggerModel(debugger, (ObjectVariable) var);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Remove a child
   * @param name the ID or name of the node
   */
    public void removeChild(String name) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + name + "\"");
            modelNode.invokeMethod("removeChild", "(Ljava/lang/String;)V", oneArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Remove a child node
   * @param child the child to be removed
   */
    public void removeChild(DataModel childNode) {
        try {
            oneArgsArray[0] = debugger.evaluate("\"" + childNode + "\"");
            modelNode.invokeMethod("remove", "(Lorg/formaria/aria/data/DataModel;)V", oneArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Insert a node at a specified index in the list of children
   * @param newNode the new model node
   * @param idx the index at which to insert
   */
    public void insertChildAt(DataModel newNode, int idx) {
        try {
            twoArgsArray[0] = debugger.evaluate("\"" + newNode + "\"");
            twoArgsArray[1] = debugger.evaluate(String.valueOf(idx));
            modelNode.invokeMethod("insertChildAt", "(Lorg/formaria/aria/data/DataModel;I)V", twoArgsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String stripQuotes(String txt) {
        int idx = (txt.charAt(0) == '"' ? 1 : 0);
        return txt.substring(idx, txt.length() - idx);
    }
}
