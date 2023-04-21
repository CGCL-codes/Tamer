package org.configora.propertyType;

import org.configora.codeGeneration.JavaGenerator;
import org.configora.TypedProperty;

/**
 * @auther: sandeep dixit.<a href="mailto:dixitsandeep@gmail.com">dixitsandeep@gmail.com</a>
 * @Date: 04-Jul-2009
 * @Time: 20:40:56
 */
public class IntegerType extends AbstractPropertyType {

    private String typeToString = "Integer";

    public String getTypeShortName() {
        return typeToString;
    }

    public String createAccessorJavaMethod(TypedProperty property) {
        StringBuffer propertyMethodBuffer = new StringBuffer();
        propertyMethodBuffer.append(JavaGenerator.lineSeperator).append(" public  ").append(typeToString).append(" ").append(property.getName()).append("(){").append(JavaGenerator.lineSeperator).append(" \t\t").append("String value = " + JavaGenerator.propertiesAccessorMethod).append("(\"").append(property.getConfiguredString()).append("\");").append(JavaGenerator.lineSeperator).append("return Integer.parseInt(value);").append(JavaGenerator.lineSeperator).append(" }");
        return propertyMethodBuffer.toString();
    }

    public String toString() {
        return typeToString;
    }
}
