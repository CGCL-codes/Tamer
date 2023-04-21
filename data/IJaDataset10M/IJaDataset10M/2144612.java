package xdoclet.modules.ejb.dd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import org.apache.commons.logging.Log;
import xjavadoc.XClass;
import xjavadoc.XTag;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.modules.ejb.home.HomeTagsHandler;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian
 * @created              Oct 16, 2001
 * @xdoclet.taghandler   namespace="EjbRef"
 * @version              $Revision 1.1 $
 * @todo                 refactor ejbRefId properly to account for ejb:bean - it may not be needed anymore.
 * @todo                 refactor storeReferringClassId properly to take ejb:bean into account - may not be needed
 *      anymore.
 * @deprecated           please use {@link xdoclet.modules.ejb.env.EnvEjbRefTagsHandler}
 */
public class EjbRefTagsHandler extends EjbTagsHandler {

    /**
     * The id of the EJB referencing another EJB, used for setting up a correct unique id for the ejb-ref.
     *
     * @see   #ejbRefId()
     * @see   #forAllEjbRefs(java.lang.String,java.util.Properties)
     * @see   #storeReferringClassId()
     */
    protected transient String referringClassId;

    /**
     * Returns unique id for the specified ejb-ref. It prefixes it with the referring class's id, then a _ and the id of
     * the ejb object.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @todo                        refactor this properly to account for ejb:bean - it may not be needed anymore.
     * @doc.tag                     type="content"
     */
    public String ejbRefId() throws XDocletException {
        return referringClassId + '_' + EjbTagsHandler.getEjbIdFor(getCurrentClass());
    }

    /**
     * Evaluates the body block for each ejb:ejb-ref defined for the EJB. One of the useful things is does is to lookup
     * the EJB using the ejb-name parameter of ejb:ejb-ref and fill in other required info.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void forAllEjbRefs(String template, Properties attributes) throws XDocletException {
        Log log = LogUtil.getLog(EjbRefTagsHandler.class, "forAllEjbRefs");
        boolean superclasses = TypeConversionUtil.stringToBoolean(attributes.getProperty("superclasses"), true);
        XClass oldCurClass = getCurrentClass();
        XClass currentClass = getCurrentClass();
        HashMap already = new HashMap();
        do {
            Collection tags = currentClass.getDoc().getTags("ejb.ejb-ref");
            for (Iterator i = tags.iterator(); i.hasNext(); ) {
                XTag tag = (XTag) i.next();
                setCurrentClassTag(tag);
                storeReferringClassId();
                String ejbNameAttribute = tag.getAttributeValue("ejb-name");
                if (ejbNameAttribute == null || ejbNameAttribute.length() < 1) {
                    mandatoryParamNotFound(tag.getDoc(), "ejb-name", "ejb.ejb-ref");
                }
                XClass refedEJBClass = findEjb(ejbNameAttribute);
                setCurrentClass(refedEJBClass);
                String refName = EjbTagsHandler.ejbRefName();
                if (!already.containsKey(refName)) {
                    already.put(refName, tag);
                    pushCurrentClass(refedEJBClass);
                    generate(template);
                    popCurrentClass();
                } else {
                    XTag previousTag = (XTag) already.get(refName);
                    if (!previousTag.getAttributeValue("ejb-name").equals(tag.getAttributeValue("ejb-name")) || !previousTag.getAttributeValue("jndi-name").equals(tag.getAttributeValue("jndi-name"))) {
                        log.error("Duplicate @ejb.ejb-ref found with different parameters!");
                        log.error("Previous tag: @ejb.ejb-ref ref-name=\"" + previousTag.getAttributeValue("ref-name") + "\" ejb-name=\"" + previousTag.getAttributeValue("ejb-name") + "\" view-type=\"" + previousTag.getAttributeValue("view-type") + "\"");
                        log.error("Current tag: @ejb.ejb-ref ref-name=\"" + tag.getAttributeValue("ref-name") + "\" ejb-name=\"" + tag.getAttributeValue("ejb-name") + "\" view-type=\"" + tag.getAttributeValue("view-type") + "\"");
                        throw new XDocletException("Duplicate @ejb.ejb-ref with different parameters");
                    } else {
                        log.warn("Duplicated @ejb.ejb-ref found, ref-name=\"" + refName + "\"");
                    }
                }
                setCurrentClassTag(null);
                referringClassId = null;
            }
            if (superclasses == true) {
                currentClass = currentClass.getSuperclass();
            } else {
                break;
            }
        } while (currentClass != null);
        setCurrentClass(oldCurClass);
    }

    /**
     * Returns the global JNDI name for the current EJB ref.
     *
     * @return                      The JNDI name of current EJB ref.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String ejbRefJndiName() throws XDocletException {
        String ejbRefJndiName = null;
        String jndiNameParameter = getCurrentClassTag().getAttributeValue("jndi-name");
        if (jndiNameParameter != null) {
            ejbRefJndiName = jndiNameParameter;
        } else {
            String refed_ejb_name = getCurrentClassTag().getAttributeValue("ejb-name");
            if (refed_ejb_name == null) {
                throw new XDocletException("No ejb-name attribute found in ejb-ref specified in bean " + getCurrentClass());
            }
            XClass refed_clazz = findEjb(refed_ejb_name);
            String ejb_type = isLocalEjb(refed_clazz) ? "local" : "remote";
            ejbRefJndiName = HomeTagsHandler.getJndiNameOfTypeFor(ejb_type, refed_clazz);
        }
        return ejbRefJndiName;
    }

    /**
     * Generates code if the ejb-ref is local
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifLocalEjbRef(String template) throws XDocletException {
        if (isLocalEjbRef(getCurrentClassTag())) {
            generate(template);
        }
    }

    /**
     * Generates code if the ejb-ref is local
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifRemoteEjbRef(String template) throws XDocletException {
        if (isRemoteEjbRef(getCurrentClassTag())) {
            generate(template);
        }
    }

    /**
     * Return true if the ejb-ref is local
     *
     * @param ejbRefTag
     * @return                      true if the ejb-ref is local otherwise false
     * @exception XDocletException
     */
    protected boolean isLocalEjbRef(XTag ejbRefTag) throws XDocletException {
        String viewTypeParameter = ejbRefTag.getAttributeValue("view-type");
        if (viewTypeParameter == null) {
            return isLocalEjb(getCurrentClass()) && !isRemoteEjb(getCurrentClass());
        } else {
            return "local".equals(viewTypeParameter);
        }
    }

    /**
     * Return true if the ejb-ref is remote
     *
     * @param ejbRefTag
     * @return                      true if the ejb-ref is remote otherwise false
     * @exception XDocletException
     */
    protected boolean isRemoteEjbRef(XTag ejbRefTag) throws XDocletException {
        return !isLocalEjbRef(ejbRefTag);
    }

    /**
     * Stores the id of current EJB for further use by other tags in referringClassId attribute.
     *
     * @exception XDocletException
     * @todo                        refactor this properly to take ejb:bean into account - may not be needed anymore.
     */
    protected void storeReferringClassId() throws XDocletException {
        referringClassId = EjbTagsHandler.getEjbIdFor(getCurrentClass());
    }

    /**
     * Finds and returns the class with the specified ejbName. An XDocletException is thrown if not found.
     *
     * @param ejbName               Description of Parameter
     * @return                      Description of the Returned Value
     * @exception XDocletException
     */
    protected XClass findEjb(String ejbName) throws XDocletException {
        Collection classes = getXJavaDoc().getSourceClasses();
        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();
            if (isEjb(clazz) && ejbName.equals(getEjbNameFor(clazz))) {
                return clazz;
            }
        }
        throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.NOT_DEFINED, new String[] { ejbName }));
    }
}
