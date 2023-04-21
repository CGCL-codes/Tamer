package net.sf.sasl.language.placeholder.syntax.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.sasl.language.placeholder.syntax.ASTNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * Defines a string node that can store a string.
 * 
 * @author Philipp Förmer
 * @since 0.0.1 (sasl-common-aspect-library)
 */
public class StringNode extends TypeValueNode<String> {

    /**
	 * Transforms the over given string value into a normalized string value
	 * where the open and close tag of the string is removed and all escapes of
	 * the open/close tag are replace with the open/close tag.
	 * 
	 * @param value
	 *            non null value.
	 * @param ocString
	 *            non null open/close tag of the string.
	 * @param parentNode
	 *            null or non null parent node
	 * @throws IllegalArgumentException
	 *             if value is null or not a well quoted string via ocString.
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public StringNode(String value, String ocString, ASTNode parentNode) throws IllegalArgumentException {
        Assert.notNull(value, "Parameter value must be non null!");
        String fixedString = value;
        if (ocString != null) {
            boolean raiseError = true;
            if (value.length() >= 2 * ocString.length() && value.startsWith(ocString) && value.endsWith(ocString)) {
                fixedString = value.substring(ocString.length(), value.length() - ocString.length());
                String quotedPattern = Pattern.quote(ocString);
                Pattern pattern = Pattern.compile("[^\\\\]" + quotedPattern);
                Matcher matcher = pattern.matcher(fixedString);
                if (!matcher.find()) {
                    fixedString = fixedString.replace("\\" + ocString, ocString);
                    raiseError = false;
                }
            }
            if (raiseError) {
                throw new IllegalArgumentException("Parameter value is not a well quoted string with: " + ocString);
            }
        }
        setParentNode(parentNode);
        setTypeValue(fixedString);
    }

    /**
	 * Creates a string node that has the given string value and given parent
	 * node.
	 * 
	 * @param value
	 *            non null value.
	 * @param parentNode
	 *            null or non null parent node
	 * @throws IllegalArgumentException
	 *             if value is null.
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public StringNode(String value, ASTNode parentNode) throws IllegalArgumentException {
        this(value, null, parentNode);
    }

    /**
	 * Creates a string node that has the given string value and no parent node.
	 * 
	 * @param value
	 *            non null value.
	 * @throws IllegalArgumentException
	 *             if value is null.
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public StringNode(String value) throws IllegalArgumentException {
        this(value, null);
    }

    @Override
    public String prettyPrint(int indent) throws IllegalArgumentException {
        Assert.isTrue(indent >= 0, "Parameter indent must be greater or equal to 0!");
        StringBuilder strBuilder = new StringBuilder(StringUtils.repeat(INDENT_STRING, indent));
        strBuilder.append(getClass().getSimpleName());
        strBuilder.append(":\n");
        String typeValueIndentString = StringUtils.repeat(INDENT_STRING, indent + 2);
        strBuilder.append(typeValueIndentString);
        String typeValue = getTypeValue();
        int len = typeValue.length();
        for (int i = 0; i < len; i++) {
            strBuilder.append(typeValue.charAt(i));
            if (typeValue.charAt(i) == '\n') {
                strBuilder.append(typeValueIndentString);
            }
        }
        return strBuilder.toString();
    }
}
