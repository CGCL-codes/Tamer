package xdoclet.tagshandler;

import java.util.Properties;
import xjavadoc.*;
import xdoclet.ConfigParamIntrospector;
import xdoclet.XDocletException;

/**
 * @author               <a href="mailto:aslak.nospam@users.sf.net">Aslak Helles�y</a>
 * @created              13. januar 2002
 * @xdoclet.taghandler   namespace="Field"
 * @version              $Revision: 1.16 $
 */
public class FieldTagsHandler extends AbstractProgramElementTagsHandler {

    /**
     * Gets the FieldTypeFor attribute of the FieldTagsHandler class
     *
     * @param field                 Describe what the parameter does
     * @return                      The FieldTypeFor value
     * @exception XDocletException  Describe the exception
     */
    public static String getFieldTypeFor(XField field) throws XDocletException {
        return field.getType().getQualifiedName() + field.getDimensionAsString();
    }

    /**
     * Returns the capitalized name of the current field.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String getCapitalizedFieldName() throws XDocletException {
        return ConfigParamIntrospector.capitalize(getCurrentField().getName());
    }

    /**
     * Iterates over all fields of current class and evaluates the body of the tag for each field.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="superclasses" optional="true" values="true,false" description="If true then
     *      traverse superclasses also, otherwise look up the tag in current concrete class only."
     * @doc.param                   name="sort" optional="true" values="true,false" description="If true then sort the
     *      fields list."
     */
    public void forAllFields(String template, Properties attributes) throws XDocletException {
        forAllMembers(template, attributes, FOR_FIELD);
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String modifiers() throws XDocletException {
        return modifiers(FOR_FIELD);
    }

    /**
     * Return standard javadoc of current field.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String firstSentenceDescriptionOfCurrentField() throws XDocletException {
        return firstSentenceDescriptionOfCurrentMember(getCurrentField());
    }

    /**
     * Returns the name of the current field.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String fieldName() throws XDocletException {
        return getCurrentField().getName();
    }

    /**
     * The comment for the current field.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         ClassTagsHandler#classComment(java.util.Properties)
     * @doc.tag                     type="content"
     * @doc.param                   name="no-comment-signs" optional="true" values="true,false" description="If true
     *      then don't decorate the comment with comment signs."
     * @doc.param                   name="indent" optional="true" description="Number of spaces to indent the comment.
     *      Default is 0."
     */
    public String fieldComment(Properties attributes) throws XDocletException {
        return memberComment(attributes, FOR_FIELD);
    }

    /**
     * Iterates over all tags of current field and evaluates the body of the tag for each field.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="superclasses" optional="true" values="true,false" description="If true then
     *      traverse superclasses also, otherwise look up the tag in current concrete class only. Default is true."
     */
    public void forAllFieldTags(String template, Properties attributes) throws XDocletException {
        forAllMemberTags(template, attributes, FOR_FIELD, XDocletTagshandlerMessages.ONLY_CALL_FIELD_NOT_NULL, new String[] { "forAllFieldTags" });
    }

    /**
     * Iterates over all tokens in current field tag with the name tagName and evaluates the body for every token.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="delimiter" description="delimiter for the StringTokenizer. consult javadoc for
     *      java.util.StringTokenizer default is ','"
     * @doc.param                   name="skip" description="how many tokens to skip on start"
     */
    public void forAllFieldTagTokens(String template, Properties attributes) throws XDocletException {
        forAllMemberTagTokens(template, attributes, FOR_FIELD);
    }

    /**
     * Iterates over all field tags with the specified tagName for the current field probably inside of a
     * forAllFieldTags body.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="values" description="The valid values for the parameter, comma separated. An
     *      error message is printed if the parameter value is not one of the values."
     * @doc.param                   name="default" description="The default value is returned if parameter not specified
     *      by user for the tag."
     */
    public String fieldTagValue(Properties attributes) throws XDocletException {
        attributes.setProperty("field", "true");
        return getExpandedDelimitedTagValue(attributes, FOR_FIELD);
    }

    /**
     * Returns the type of the current field.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String fieldType() throws XDocletException {
        return getFieldTypeFor(getCurrentField());
    }

    /**
     * Evaluates the body if current field has at least one tag with the specified name.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="superclasses" optional="true" values="true,false" description="If true then
     *      traverse superclasses also, otherwise look up the tag in current concrete class only. Default is true."
     * @doc.param                   name="error" description="Show this error message if no tag found."
     */
    public void ifHasFieldTag(String template, Properties attributes) throws XDocletException {
        if (hasTag(attributes, FOR_FIELD)) {
            generate(template);
        } else {
            String error = attributes.getProperty("error");
            if (error != null) {
                getEngine().print(error);
            }
        }
    }

    /**
     * Evaluates the body if current field doesnt have any tags with the specified name.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="error" description="Show this error message if no tag found."
     */
    public void ifDoesntHaveFieldTag(String template, Properties attributes) throws XDocletException {
        if (!hasTag(attributes, FOR_FIELD)) {
            generate(template);
        } else {
            String error = attributes.getProperty("error");
            if (error != null) {
                getEngine().print(error);
            }
        }
    }

    /**
     * Evaluates the body if value for the field tag equals the specified value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="value" optional="false" description="The desired value."
     */
    public void ifFieldTagValueEquals(String template, Properties attributes) throws XDocletException {
        if (isTagValueEqual(attributes, FOR_FIELD)) {
            generate(template);
        }
    }
}
