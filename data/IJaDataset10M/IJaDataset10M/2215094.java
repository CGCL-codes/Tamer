package net.jolm.codegen.parser;

import java.util.Set;
import net.jolm.codegen.model.Attribute;
import net.jolm.codegen.model.ObjectClass;
import net.jolm.codegen.model.SchemaBinding;
import net.jolm.maven.mojo.Logger;

/**
 * Parses the LDAP Schema in DXC format. 
 * 
 * @author Chunyun Zhao
 */
public class DxcSchemaParser extends AbstractSchemaParser {

    private enum State {

        READY, ATTRIBUTE, OBJECT_CLASS, OBJECT_CLASS_REQUIRED_FIELDS, OBJECT_CLASS_OPTIONAL_FIELDS, BINDING
    }

    private State currentState;

    private Object currentObject;

    @Override
    protected void initState() {
        currentState = State.READY;
        currentObject = null;
    }

    @Override
    protected void parseLine(String trimmedLine) {
        if (isCommentOrBlankLine(trimmedLine)) {
            return;
        }
        if (isObjectEndLine(trimmedLine)) {
            currentState = State.READY;
            currentObject = null;
            return;
        }
        switch(currentState) {
            case READY:
                parseLineInReadyState(trimmedLine);
                break;
            case ATTRIBUTE:
                parseLineInAttributeState(trimmedLine);
                break;
            case OBJECT_CLASS:
                parseLineInObjectClassState(trimmedLine);
                break;
            case OBJECT_CLASS_REQUIRED_FIELDS:
                parseLineInObjectClassRequiredFieldsState(trimmedLine);
                break;
            case OBJECT_CLASS_OPTIONAL_FIELDS:
                parseLineInObjectClassOptionalFieldsState(trimmedLine);
                break;
            case BINDING:
                parseLineInBindingState(trimmedLine);
                break;
            default:
                break;
        }
    }

    private void parseLineInReadyState(String trimmedLine) {
        if (trimmedLine.startsWith("schema set attribute")) {
            currentState = State.ATTRIBUTE;
            currentObject = new Attribute();
        } else if (trimmedLine.startsWith("schema set object-class")) {
            currentState = State.OBJECT_CLASS;
            currentObject = new ObjectClass();
            schema.getObjectClasses().add((ObjectClass) currentObject);
        } else if (trimmedLine.startsWith("schema set name-binding")) {
            currentObject = new SchemaBinding();
            currentState = State.BINDING;
        } else if (trimmedLine.startsWith("schema set oid-prefix")) {
        } else {
            reportNonSupportedLine(State.READY, trimmedLine);
        }
    }

    private void parseLineInAttributeState(String trimmedLine) {
        Attribute attribute = (Attribute) currentObject;
        if (trimmedLine.startsWith("name")) {
            attribute.setName(getAttributeValueDelimitedByEqualSign(trimmedLine));
            schema.getAttributes().put(attribute.getName().toLowerCase(), attribute);
        } else if (trimmedLine.startsWith("ldap-names")) {
            attribute.setLdapNames(getAttributeValueDelimitedByEqualSign(trimmedLine));
        } else if (trimmedLine.startsWith("syntax")) {
            attribute.setSyntax(getAttributeValueDelimitedByEqualSign(trimmedLine));
        } else if (trimmedLine.equals("single-valued")) {
            attribute.setMultiValues(false);
        } else {
            reportNonSupportedLine(State.ATTRIBUTE, trimmedLine);
        }
    }

    private void parseLineInObjectClassState(String trimmedLine) {
        ObjectClass objectClass = (ObjectClass) currentObject;
        if (trimmedLine.startsWith("name")) {
            objectClass.setName(getAttributeValueDelimitedByEqualSign(trimmedLine));
        } else if (trimmedLine.startsWith("ldap-names")) {
            objectClass.setLdapNames(getAttributeValueDelimitedByEqualSign(trimmedLine));
        } else if (trimmedLine.startsWith("subclass-of")) {
            objectClass.setSubclassOf(getAttributeValueDelimitedBySpace(trimmedLine));
        } else if (trimmedLine.startsWith("kind")) {
            objectClass.setKind(getAttributeValueDelimitedByEqualSign(trimmedLine));
        } else if (trimmedLine.equals("must-contain")) {
            currentState = State.OBJECT_CLASS_REQUIRED_FIELDS;
        } else if (trimmedLine.equals("may-contain")) {
            currentState = State.OBJECT_CLASS_OPTIONAL_FIELDS;
        } else {
            reportNonSupportedLine(State.OBJECT_CLASS, trimmedLine);
        }
    }

    private void parseLineInObjectClassRequiredFieldsState(String trimmedLine) {
        ObjectClass objectClass = (ObjectClass) currentObject;
        if (trimmedLine.equals("may-contain")) {
            currentState = State.OBJECT_CLASS_OPTIONAL_FIELDS;
        } else {
            String attributeName = trimComma(trimmedLine);
            objectClass.addRequiredAttributeName(attributeName);
        }
    }

    private void parseLineInObjectClassOptionalFieldsState(String trimmedLine) {
        ObjectClass objectClass = (ObjectClass) currentObject;
        if (isDescriptionLine(trimmedLine)) {
        } else {
            String attributeName = trimComma(trimmedLine);
            if (!containsIgnoreCase(objectClass.getRequiredAttributeNames(), attributeName)) {
                objectClass.addOptionalAttributeName(attributeName);
            }
        }
    }

    private boolean containsIgnoreCase(Set<String> set, String value) {
        for (String item : set) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private void parseLineInBindingState(String trimmedLine) {
        SchemaBinding schemaBinding = (SchemaBinding) currentObject;
        if (trimmedLine.startsWith("named-by")) {
            schemaBinding.setNamedBy(getAttributeValueDelimitedBySpace(trimmedLine));
        } else if (trimmedLine.startsWith("name")) {
            schemaBinding.setBindingName(getAttributeValueDelimitedByEqualSign(trimmedLine));
        } else if (trimmedLine.contains("allowable-parent")) {
            String[] tokens = trimmedLine.split(" ");
            if (tokens.length != 3) {
                reportNonSupportedLine(State.BINDING, trimmedLine);
            } else {
                schemaBinding.setLdapName(tokens[0]);
                schemaBinding.setAllowableParent(tokens[2]);
                schema.getSchemaBindings().put(schemaBinding.getLdapName(), schemaBinding);
            }
        } else {
            reportNonSupportedLine(State.BINDING, trimmedLine);
        }
    }

    private boolean isObjectEndLine(String trimmedLine) {
        return trimmedLine.endsWith("};");
    }

    private boolean isCommentOrBlankLine(String trimmedLine) {
        return trimmedLine.length() == 0 || trimmedLine.startsWith("#");
    }

    /**
	 * Reports when a line is not supported by the parser for a given state.
	 */
    private void reportNonSupportedLine(State state, String trimmedLine) {
        Logger.getInstance().debug("[" + state.toString() + "] Ignored non-supported line: " + trimmedLine);
    }

    private String getAttributeValueDelimitedByEqualSign(String line) {
        return getAttributeValue("=", line);
    }

    private String getAttributeValueDelimitedBySpace(String line) {
        return getAttributeValue(" ", line);
    }

    /**
	 * Returns the value of the object attribute. 
	 * @param delimiter Could be '=' or ' '
	 * @param line
	 * @return The value of the attribute (the second token).
	 */
    private String getAttributeValue(String delimiter, String line) {
        String[] tokens = line.split(delimiter);
        if (tokens.length != 2) {
            Logger.getInstance().debug("Ignored the line that doesn't represent a name-value pair attribute: " + line + " delimited by _" + delimiter + "_");
            return null;
        }
        return tokens[1].trim();
    }

    private String trimComma(String line) {
        return line.replaceAll(",", "").trim();
    }

    /**
	 * Returns true if it is the description for object class in the form of:
	 * 
	 * 		description = "blah blah..."
	 * 
	 * @param trimmedLine
	 * @return
	 */
    private boolean isDescriptionLine(String trimmedLine) {
        return trimmedLine.matches("description\\s*=\\s*\\\".*\\\"");
    }
}
