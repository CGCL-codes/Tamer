package xdoclet.modules.spring;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.collections.SequencedHashMap;
import xjavadoc.XClass;
import xjavadoc.XMethod;
import xjavadoc.XParameter;
import xjavadoc.XTag;
import xdoclet.DocletSupport;
import xdoclet.XDocletException;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;
import xdoclet.util.Translator;

/**
 * Spring Validator tag handler tags
 *
 * @author               Matt Raible (matt@raibledesigns.com)
 * @created              April 26, 2004
 * @xdoclet.taghandler   namespace="CommonsValidator"
 * @version              $Revision: 1.5 $
 */
public class SpringValidatorTagsHandler extends AbstractProgramElementTagsHandler {

    protected static final List supportedTypes = new ArrayList();

    protected String curFieldName;

    protected String currentArgKey;

    protected Map args;

    static {
        supportedTypes.add("java.lang.String");
        supportedTypes.add("java.lang.Integer");
        supportedTypes.add("int");
        supportedTypes.add("java.lang.Float");
        supportedTypes.add("float");
        supportedTypes.add("java.lang.Long");
        supportedTypes.add("long");
        supportedTypes.add("java.lang.Double");
        supportedTypes.add("double");
        supportedTypes.add("java.lang.Boolean");
        supportedTypes.add("boolean");
        supportedTypes.add("java.util.Date");
        supportedTypes.add("byte");
        supportedTypes.add("byte[]");
    }

    /**
     * Iterates over all POJOs and evaluates the body of the tag for each class.
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
            generate(template);
        }
    }

    /**
     * Gets the "name" attribute for the &lt;form&gt; element in the xml descriptor. This should be the "path" form
     * attribute if this is a ValidatorActionForm or the "name" attribute otherwise.
     *
     * @param attributes         The content tag attributes.
     * @return                   form name
     * @throws XDocletException  if anything goes awry.
     * @doc.tag                  type="content"
     */
    public String formName(Properties attributes) throws XDocletException {
        return Introspector.decapitalize(getCurrentClass().getTransformedName());
    }

    /**
     * Iterates over all arguments for the current field.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException
     * @doc.tag                  type="block"
     */
    public void forAllFieldArgs(String template, Properties attributes) throws XDocletException {
        for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); ) {
            currentArgKey = (String) iterator.next();
            generate(template);
        }
    }

    /**
     * Current argument index number (0 to 3).
     *
     * @param props  The content tag attributes.
     * @return       current argument index
     * @doc.tag      type="content"
     */
    public String argIndex(Properties props) {
        return currentArgKey.charAt(3) + "";
    }

    /**
     * Current argument name - only valid if argument is for a specific validator type.
     *
     * @param props  The content tag attributes.
     * @return       current argument name
     * @doc.tag      type="content"
     */
    public String argName(Properties props) {
        String name = currentArgKey.substring(currentArgKey.indexOf('_') + 1);
        return name;
    }

    /**
     * Current argument value, which is either an inline value or resource key.
     *
     * @param props  The content tag attributes.
     * @return       current argument value
     * @doc.tag      type="content"
     */
    public String argValue(Properties props) {
        return (String) args.get(currentArgKey);
    }

    /**
     * Evaluates body if current argument is a resource key.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException
     * @doc.tag                  type="block"
     */
    public void ifArgIsResource(String template, Properties attributes) throws XDocletException {
        if (currentArgKey.indexOf("resource") > 0) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if the current argument is an inline value rather than a resource key.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException
     * @doc.tag                  type="block"
     */
    public void ifArgIsValue(String template, Properties attributes) throws XDocletException {
        if (currentArgKey.indexOf("value") > 0) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if the current argument is a validator-specific argument.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException
     * @doc.tag                  type="block"
     */
    public void ifArgIsForType(String template, Properties attributes) throws XDocletException {
        if (currentArgKey.indexOf('_') > 0) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if there is no arg0 specified.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException
     * @doc.tag                  type="block"
     */
    public void ifNoArg0(String template, Properties attributes) throws XDocletException {
        if (args.get("arg0resource") == null && args.get("arg0value") == null) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if form has fields requiring validation.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException
     * @doc.tag                  type="block"
     */
    public void ifFormHasFields(String template, Properties attributes) throws XDocletException {
        if (getFields(getCurrentClass()).size() > 0) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if the current field has an indexed component.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException
     * @doc.tag                  type="block"
     */
    public void ifFieldIsIndexed(String template, Properties attributes) throws XDocletException {
        if (curFieldName.indexOf("[]") >= 0) {
            generate(template);
        }
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
            setCurrentMethod(field);
            loadFieldArguments();
            generate(template);
        }
    }

    /**
     * Returns the current field's name, local to the indexed property if there is one.
     *
     * @param props  The content tag attributes.
     * @return       current field name
     * @doc.tag      type="content"
     */
    public String fieldName(Properties props) {
        int listIdx = curFieldName.indexOf("[]");
        if (listIdx == -1) return curFieldName;
        String result = curFieldName.substring(listIdx + 3);
        if (result.length() == 0) return curFieldName.substring(0, listIdx);
        return result;
    }

    /**
     * Returns the current field's indexedListProperty attribute, if any.
     *
     * @param props  The content tag attributes.
     * @return       current field's indexedListProperty
     * @doc.tag      type="content"
     */
    public String indexedListProperty(Properties props) {
        int listIdx = curFieldName.indexOf("[]");
        if (listIdx == -1) return "";
        return curFieldName.substring(0, listIdx);
    }

    /**
     * Returns a comma-separated list of the specified validator types.
     *
     * @param props  The content tag attributes.
     * @return       validator types list
     * @doc.tag      type="content"
     */
    public String validatorList(Properties props) {
        XMethod method = getCurrentMethod();
        Collection tags = method.getDoc().getTags("spring.validator");
        StringBuffer buffer = new StringBuffer();
        for (Iterator iterator = tags.iterator(); iterator.hasNext(); ) {
            XTag tag = (XTag) iterator.next();
            buffer.append(tag.getAttributeValue("type"));
            if (iterator.hasNext()) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    protected Map getFields(XClass clazz) throws XDocletException {
        return getFields(clazz, "");
    }

    protected Map getFields(XClass clazz, String prefix) throws XDocletException {
        Map fields = new SequencedHashMap();
        Collection curFields = clazz.getMethods(true);
        for (Iterator iterator = curFields.iterator(); iterator.hasNext(); ) {
            XMethod method = (XMethod) iterator.next();
            XTag tag = method.getDoc().getTag("spring.validator");
            String override = null;
            if (tag != null) {
                override = tag.getAttributeValue("override");
            }
            if (tag != null) {
                List params = method.getParameters();
                String name = method.getPropertyName();
                XParameter param = null;
                if (MethodTagsHandler.isSetterMethod(method)) {
                    param = (XParameter) params.get(0);
                } else if (params.size() == 2) {
                    if (!MethodTagsHandler.isSetter(method.getName())) continue;
                    Iterator paramIter = params.iterator();
                    if (!((XParameter) paramIter.next()).getType().isA("int")) continue;
                    if (name.indexOf("[]") >= 0) {
                        throw new XDocletException(Translator.getString(SpringValidatorMessages.class, SpringValidatorMessages.ONLY_ONE_LEVEL_LIST_PROPS, new String[] { clazz.getName() + '.' + name + "[]" }));
                    }
                    name = name + "[]";
                    param = (XParameter) paramIter.next();
                } else continue;
                String type = param.getType().getQualifiedName();
                if (supportedTypes.contains(type)) {
                    fields.put(prefix + name, method);
                } else if ((override != null) && (override.equals("true"))) {
                    fields.put(prefix + name, method);
                } else {
                    boolean preDot = (prefix.length() > 0 && prefix.charAt(prefix.length() - 1) != '.');
                    fields.putAll(getFields(param.getType(), prefix + (preDot ? "." : "") + name + "."));
                }
            }
        }
        return fields;
    }

    protected void loadFieldArguments() {
        args = new SequencedHashMap();
        XMethod method = getCurrentMethod();
        Collection argTags = method.getDoc().getTags("spring.validator-args");
        for (Iterator argsIterator = argTags.iterator(); argsIterator.hasNext(); ) {
            XTag tag = (XTag) argsIterator.next();
            Collection attributeNames = tag.getAttributeNames();
            for (Iterator attributesIterator = attributeNames.iterator(); attributesIterator.hasNext(); ) {
                String name = (String) attributesIterator.next();
                if (name.startsWith("arg")) {
                    args.put(name, tag.getAttributeValue(name));
                }
            }
        }
        Collection argTypeTags = method.getDoc().getTags("spring.validator");
        for (Iterator typeTagsIterator = argTypeTags.iterator(); typeTagsIterator.hasNext(); ) {
            XTag tag = (XTag) typeTagsIterator.next();
            Collection attributeNames = tag.getAttributeNames();
            String type = tag.getAttributeValue("type");
            for (Iterator attributesIterator = attributeNames.iterator(); attributesIterator.hasNext(); ) {
                String name = (String) attributesIterator.next();
                if (name.startsWith("arg")) {
                    args.put(name + "_" + type, tag.getAttributeValue(name));
                }
            }
        }
    }
}
