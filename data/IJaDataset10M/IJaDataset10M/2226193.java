package com.emeraldjb.generator.javatypes;

/**
 * <p>
 * @author: J.Gibbons
 * Created: 26-Nov-2003
 * </p><p>
 * Copyright (c) 2003, 2004 by Emeraldjb LLC<br>
 * All Rights Reserved.
 * </p>
 */
public class JTypeDouble implements JTypeBase {

    public String lookupLangType() {
        return "double";
    }

    public boolean isPrimitive() {
        return true;
    }

    /**
   * Gets the code block for XML rendering of the type - coupled to the
   * XmlStreamGenerator
   * @param line_pad spaces to be prefixed before any code on the line
   * so indentation looks good.
   * @param element_name The XML element name
   * @param var_name the name of the varibale to be output to binary
   */
    public String getToXmlCode(String line_pad, String element_name, String var_name) {
        return "\n" + line_pad + "xos.opElement(" + element_name + ",\"\"+" + var_name + ");";
    }

    /**
   * Presume the stream  is a datainputstream
   * @param line_pad spaces to be prefixed before any code on the line
   * so indentation looks good.
   * @param element_name The XML element name
   * @param setter_func the name of the function to call with the value.
   * @return
   */
    public String getFromXmlCode(String line_pad, String element_name, String setter_func) {
        return "\n" + line_pad + "if (local_name.equals(" + element_name + ")) {" + "\n" + line_pad + "  " + setter_func + "(Double.parseDouble(cdata_.toString()));" + "\n" + line_pad + "}";
    }

    /**
   * Presume the stream  is a dataoutputstream
   * @param line_pad spaces to be prefixed before any code on the line
   * so indentation looks good.
   * @param stream_name the name of the variable holding the stream
   * @param var_name the name of the varibale to be output to binary
   * @return
   */
    public String getToBinaryCode(String line_pad, String stream_name, String var_name) {
        return "\n" + line_pad + stream_name + ".writeDouble(" + var_name + ");";
    }

    /**
   * Presume the stream  is a datainputstream
   * @param line_pad spaces to be prefixed before any code on the line
   * so indentation looks good.
   * @param stream_name the name of the variable holding the stream
   * @param setter_func the name of the function to call with the value.
   * @return
   */
    public String getFromBinaryCode(String line_pad, String stream_name, String setter_func) {
        return "\n" + line_pad + setter_func + "(" + stream_name + ".readDouble());";
    }

    /**
   * This returns a string rendering of the value.
   * @param line_pad spaces to be prefixed before any code on the line
   * so indentation looks good.
   * @param getter_func The getter function including ()
   * @param col_len The column length if appropriate for this member,
   * can be 0 or -1 if not appropriate
   * @return The full code snippet
   */
    public String getToString(String line_pad, String getter_func, int col_len) {
        return "Double.toString(" + getter_func + ")";
    }
}
