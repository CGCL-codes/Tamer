package org.progeeks.util.xml;

import java.util.*;
import javax.swing.Action;
import org.xml.sax.*;
import org.progeeks.util.*;
import org.progeeks.util.log.*;

/**
 *  ObjectHandler implementation that can set values on Action
 *  objects.  This handler will only attempt to handle objects that
 *  implement the Action interface.
 *
 *  @version   $Revision: 1.5 $
 *  @author    Paul Speed
 */
public class ActionObjectHandler extends BeanObjectHandler {

    static Log log = Log.getLog(ActionObjectHandler.class);

    public ActionObjectHandler() {
    }

    /**
     *  Returns true if this handler applies to the specified
     *  object tag.
     */
    public boolean canHandle(String tag) {
        if (log.isDebugEnabled()) log.debug("canHandle(" + tag + ")");
        if ("null".equals(tag)) return (true);
        Class c = getTagClass(tag);
        if (c == null) return (false);
        return (Action.class.isAssignableFrom(c));
    }

    protected void setActionProperty(Action a, String field, Object value) {
        if ("name".equals(field)) field = Action.NAME; else if ("shortDescription".equals(field)) field = Action.SHORT_DESCRIPTION; else if ("longDescription".equals(field)) field = Action.LONG_DESCRIPTION; else if ("smallIcon".equals(field)) field = Action.SMALL_ICON; else if ("actionCommandKey".equals(field)) field = Action.ACTION_COMMAND_KEY; else if ("acceleratorKey".equals(field)) field = Action.ACCELERATOR_KEY; else if ("mnemonicKey".equals(field)) field = Action.MNEMONIC_KEY;
        a.putValue(field, value);
    }

    /**
     *  Sets a property on the specified object.
     */
    public void setProperty(Object obj, String field, Object value, ObjectXmlReader reader) {
        Inspector ins = new Inspector(obj);
        if (ins.hasMutator(field)) super.setProperty(obj, field, value, reader); else setActionProperty((Action) obj, field, value);
    }

    /**
     *  Sets a text-value property on the specified object.  The
     *  handler may do some internal conversions to translate into
     *  an appropriate type.
     */
    public void setTextProperty(Object obj, String field, String value, ObjectXmlReader reader) {
        Inspector ins = new Inspector(obj);
        if (ins.hasMutator(field)) {
            super.setTextProperty(obj, field, value, reader);
            return;
        }
        Object v = getConstant(value, obj.getClass());
        if (v == null) v = value;
        setActionProperty((Action) obj, field, v);
    }

    /**
     *  Returns the base type for the specified field.  This is used to determine
     *  whether or not to accumulate field values in a collection.
     */
    public Class getPropertyClass(Object obj, String field, ObjectXmlReader reader) {
        Inspector ins = new Inspector(obj);
        Class type = ins.getType(field);
        if (type != null) return (super.getPropertyClass(obj, field, reader));
        return (Object.class);
    }

    /**
     *  Returns the a collection for the specified field that can be used to
     *  accumulate multiple object values.  This is used when the XML field
     *  declaration specifies the progressive attribute.
     */
    public Collection getPropertyCollection(Object obj, String field, ObjectXmlReader reader) {
        Inspector ins = new Inspector(obj);
        if (ins.getType(field) != null) return (super.getPropertyCollection(obj, field, reader));
        Action a = (Action) obj;
        Collection c = (Collection) a.getValue(field);
        if (c == null) {
            c = new ArrayList();
            setActionProperty((Action) obj, field, c);
        }
        return (c);
    }

    /**
     *  Creates and returns an object based on the specified parameters.
     */
    public Object createObject(String tag, Attributes atts, ObjectXmlReader reader) {
        if ("null".equals(tag)) return (null);
        Class type = getTagClass(tag);
        log.debug("createObject() type:" + type);
        String ctorVal = atts.getValue("_ctor");
        if (ctorVal != null || Inspector.hasConstructor(type)) return (super.createObject(tag, atts, reader));
        if (!Inspector.hasConstructor(type, new Class[] { String.class })) throw new RuntimeException("Cannot instantiate object of type:" + tag + "  No valid constructor.");
        String name = atts.getValue("name");
        if (name == null) {
            throw new RuntimeException("Cannot instantiate object of type:" + tag + "  No valid constructor and no name attribute specified.");
        }
        Object bean = Inspector.newInstance(type, new Object[] { name });
        setObjectProperties(bean, atts, reader);
        return (bean);
    }
}
