package xdoclet.ant;

import java.util.Hashtable;
import java.util.Properties;
import xdoclet.XDocletException;
import xdoclet.template.TemplateTagHandler;

/**
 * Extracts properties from Ant.
 *
 * @author               <a href="mailto:aslak.nospam@users.sf.net">Aslak Helles�y</a>
 * @created              5. januar 2002
 * @xdoclet.taghandler   namespace="Ant"
 * @version              $Revision: 1.8 $
 */
public class AntPropertyTagsHandler extends TemplateTagHandler {

    private Hashtable antProperties;

    /**
     * Describe what the AntPropertyTagsHandler constructor does
     *
     * @param antProperties  Describe what the parameter does
     */
    public AntPropertyTagsHandler(Hashtable antProperties) {
        this.antProperties = antProperties;
    }

    /**
     * Returns the values of a configuration parameter with the name paramName.
     *
     * @param attributes            The attributes of the template tag
     * @return                      The value of the ant property
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="name" optional="false" description="The ant property name, without enclosing
     *      ${}"
     */
    public String property(Properties attributes) throws XDocletException {
        String propertyName = attributes.getProperty("name");
        String propertyValue = (String) antProperties.get(propertyName);
        if (propertyValue == null) {
            throw new XDocletException("The referenced property " + propertyName + " is not defined.");
        }
        return propertyValue;
    }
}
