package org.primordion.xholon.service.validation;

/**
 * A simple Xholon domain-class validator that only uses Java statements, with no XML.
 * It's not based on any other validation framework and has no external dependencies.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on November 5, 2009)
 */
public class SimpleXholonValidator extends AbstractXholonValidator {

    public String makeValidationContentsJava(String domainClassName) {
        StringBuffer sb = new StringBuffer().append("\n\tprotected java.util.List validate(org.primordion.xholon.base.IXholon recordArg)\n").append("\t{\n").append("\t\tjava.util.List validationMessages = new java.util.ArrayList();\n").append("\t\t").append(domainClassName).append(" record = (").append(domainClassName).append(")recordArg;\n\n");
        sb.append(getValidationDetails(getClassAttributes(domainClassName)));
        sb.append("\t\treturn validationMessages;\n").append("\t}\n");
        return sb.toString();
    }

    /**
	 * Get the validation details for the current domain class.
	 * @param attribute The attributes of the domain class.
	 * @return An XML String, ready to be inserted into the validations.xml file.
	 */
    protected String getValidationDetails(Object[][] attribute) {
        StringBuffer sb = new StringBuffer();
        for (int index = 0; index < attribute.length; index++) {
            if (!isADomainAttribute(attribute, index)) {
                continue;
            }
            String name = (String) attribute[index][0];
            Class clazz = (Class) attribute[index][2];
            if (clazz == boolean.class) {
            } else if (clazz == byte.class) {
                sb.append("\t\t// ").append(name).append("\n").append("\t\tif (\trecord.get").append(name).append("() < 1 ||\n").append("\t\t\t\trecord.get").append(name).append("() > 99)\n").append("\t\t\t{validationMessages.add(\"").append(name).append(" must be at least 1 and at most 99.\");}\n\n");
            } else if (clazz == double.class) {
                sb.append("\t\t// ").append(name).append("\n").append("\t\tif (\trecord.get").append(name).append("() < 1.0 ||\n").append("\t\t\t\trecord.get").append(name).append("() > 9999.0)\n").append("\t\t\t{validationMessages.add(\"").append(name).append(" must be at least 1 and at most 9999.\");}\n\n");
            } else if (clazz == float.class) {
                sb.append("\t\t// ").append(name).append("\n").append("\t\tif (\trecord.get").append(name).append("() < 1.0f ||\n").append("\t\t\t\trecord.get").append(name).append("() > 9999.0f)\n").append("\t\t\t{validationMessages.add(\"").append(name).append(" must be at least 1 and at most 9999.\");}\n\n");
            } else if (clazz == int.class) {
                sb.append("\t\t// ").append(name).append("\n").append("\t\tif (\trecord.get").append(name).append("() < 1 ||\n").append("\t\t\t\trecord.get").append(name).append("() > 9999)\n").append("\t\t\t{validationMessages.add(\"").append(name).append(" must be at least 1 and at most 9999.\");}\n\n");
            } else if (clazz == long.class) {
                sb.append("\t\t// ").append(name).append("\n").append("\t\tif (\trecord.get").append(name).append("() < 1 ||\n").append("\t\t\t\trecord.get").append(name).append("() > 999999)\n").append("\t\t\t{validationMessages.add(\"").append(name).append(" must be at least 1 and at most 999999.\");}\n\n");
            } else if (clazz == short.class) {
                sb.append("\t\t// ").append(name).append("\n").append("\t\tif (\trecord.get").append(name).append("() < 1 ||\n").append("\t\t\t\trecord.get").append(name).append("() > 999)\n").append("\t\t\t{validationMessages.add(\"").append(name).append(" must be at least 1 and at most 999.\");}\n\n");
            } else {
                sb.append("\t\t// ").append(name).append("\n").append("\t\tif (\trecord.get").append(name).append("() == null ||\n").append("\t\t\t\trecord.get").append(name).append("().length() < 1 ||\n").append("\t\t\t\trecord.get").append(name).append("().length() > 30)\n").append("\t\t\t{validationMessages.add(\"").append(name).append(" must be 1 to 30 characters long.\");}\n\n");
            }
        }
        return sb.toString();
    }
}
