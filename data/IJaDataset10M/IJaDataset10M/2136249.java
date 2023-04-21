package xdoclet.modules.apache.struts;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import xjavadoc.XClass;
import xjavadoc.XMethod;
import xjavadoc.XTag;
import xdoclet.DocletSupport;
import xdoclet.XDocletException;

/**
 * Struts dynamic form validator tag handler.
 *
 * @author               Nick Heudecker (nick at systemmobile dot com)
 * @created              September 27, 2004
 * @xdoclet.taghandler   namespace="DFValidator"
 * @version              $Revision: 1.2 $
 */
public class StrutsDynaFormValidatorTagsHandler extends StrutsValidatorTagsHandler {

    /**
     * Iterates over all POJOs with dynaform tags and evaluates the body of the tag for each class.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void forAllForms(String template, Properties attributes) throws XDocletException {
        Collection classes = getAllClasses();
        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass currentClass = (XClass) i.next();
            setCurrentClass(currentClass);
            if (DocletSupport.isDocletGenerated(getCurrentClass()) || (getCurrentClass().isAbstract())) {
                continue;
            }
            if (currentClass.getDoc().hasTag("struts.dynaform")) {
                XTag tag = currentClass.getDoc().getTag("struts.dynaform");
                String validateAttr = tag.getAttributeValue("validate");
                if ((validateAttr != null) && (validateAttr.equals("true"))) {
                    generate(template);
                }
            }
        }
    }

    /**
     * Gets the "name" attribute for the &lt;form&gt; element in the xml descriptor.
     *
     * @param attributes         The content tag attributes.
     * @return                   form name
     * @throws XDocletException  if anything goes awry.
     * @doc.tag                  type="content"
     */
    public String formName(Properties attributes) throws XDocletException {
        return getTagValue(FOR_CLASS, "struts.dynaform", "name", null, null, false, true);
    }

    /**
     * Iterates the body for each field of the current form requiring validation.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException
     * @doc.tag                  type="block"
     */
    public void forAllFields(String template, Properties attributes) throws XDocletException {
        XClass clazz = getCurrentClass();
        Map setters = getFields(clazz);
        for (Iterator iterator = setters.keySet().iterator(); iterator.hasNext(); ) {
            curFieldName = (String) iterator.next();
            XMethod field = (XMethod) setters.get(curFieldName);
            if (field.getDoc().hasTag("struts.validator")) {
                setCurrentMethod(field);
                loadFieldArguments();
                generate(template);
            }
        }
    }
}
