package org.nakedobjects.reflector.original.reflect;

import org.nakedobjects.applib.ValueParseException;
import org.nakedobjects.applib.control.AboutType;
import org.nakedobjects.applib.valueholder.BusinessValueHolder;
import org.nakedobjects.object.Formatter;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedReference;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.context.NakedObjectsContext;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.object.defaults.NullFormatter;
import org.nakedobjects.object.reflect.MemberIdentifier;
import org.nakedobjects.object.reflect.ReflectionException;
import org.nakedobjects.object.reflect.ValuePeer;
import org.nakedobjects.object.security.Session;
import org.nakedobjects.reflector.original.control.About;
import org.nakedobjects.reflector.original.control.FieldInfoAccessor;
import org.nakedobjects.reflector.original.control.SimpleFieldAbout;
import org.nakedobjects.reflector.original.control.SimpleFieldInfo;
import org.nakedobjects.utility.DebugString;
import org.nakedobjects.utility.NakedObjectRuntimeException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Category;

public class JavaValueAssociation extends JavaField implements ValuePeer {

    private static final Category LOG = Category.getInstance(JavaValueAssociation.class);

    protected Method setMethod;

    private final Formatter formatter;

    public JavaValueAssociation(final MemberIdentifier identifier, final Class type, final Method get, final Method set, final Method info, final Method about, final boolean isHidden, final boolean derived) {
        super(identifier, type, get, info, about, isHidden, derived);
        this.setMethod = set;
        formatter = new NullFormatter();
    }

    public void debugData(final DebugString debugString) {
        debugString.appendln("Get method", getMethod);
        if (setMethod != null) {
            debugString.appendln("Set method", setMethod);
        }
        super.debugData(debugString);
    }

    protected boolean doIsHidden() {
        return executeInfo(NakedObjectsContext.getSession()).isHidden();
    }

    private About executeAbout(final AboutType type, final NakedReference object, final Naked associate) {
        Method aboutMethod = getAboutMethod();
        Class parameter = getMethod.getReturnType();
        Object object2 = object.getObject();
        if (associate != null && associate.getObject() != null && !parameter.isAssignableFrom(associate.getObject().getClass())) {
            SimpleFieldAbout about = new SimpleFieldAbout(object2, type);
            about.unmodifiable("Invalid type: field must be set with a " + NakedObjectsContext.getSpecificationLoader().loadSpecification(parameter.getName()));
            return about;
        }
        if (aboutMethod == null) {
            DefaultHint hint = new DefaultHint();
            return hint;
        }
        try {
            SimpleFieldAbout hint = new SimpleFieldAbout(object2, type);
            Object[] parameters;
            if (aboutMethod.getParameterTypes().length == 2) {
                parameters = new Object[] { hint, associate == null ? null : associate.getObject() };
            } else {
                parameters = new Object[] { hint };
            }
            aboutMethod.invoke(object2, parameters);
            return hint;
        } catch (InvocationTargetException e) {
            invocationException("Exception executing " + aboutMethod, e);
        } catch (IllegalAccessException ignore) {
            LOG.error("illegal access of " + aboutMethod, ignore);
        }
        return new DefaultHint();
    }

    protected FieldInfoAccessor executeInfo(final Session session) {
        Method infoMethod = getInfoMethod();
        if (infoMethod == null) {
            return new DefaultFieldInfoAccessor();
        }
        try {
            SimpleFieldInfo hint = new SimpleFieldInfo(session);
            Object[] parameters;
            if (infoMethod.getParameterTypes().length == 2) {
                parameters = new Object[] { hint, null };
            } else {
                parameters = new Object[] { hint };
            }
            infoMethod.invoke(null, parameters);
            return hint;
        } catch (InvocationTargetException e) {
            invocationException("Exception executing " + infoMethod, e);
        } catch (IllegalAccessException ignore) {
            LOG.error("illegal access of " + infoMethod, ignore);
        }
        return new DefaultFieldInfoAccessor();
    }

    public int getMaximumLength() {
        return 0;
    }

    public int getMinimumLength() {
        return 0;
    }

    public NakedValue getValue(final NakedObject fromObject) {
        try {
            Object obj = getMethod.invoke(fromObject.getObject(), new Object[0]);
            if (obj == null) {
                return null;
            } else {
                NakedValue adapter = NakedObjectsContext.getObjectLoader().createAdapterForValue(obj);
                if (adapter == null) {
                    throw new NakedObjectRuntimeException("Value field did not have a value adapter created for it: " + getIdentifier() + "/" + obj);
                }
                return adapter;
            }
        } catch (InvocationTargetException e) {
            invocationException("Exception executing " + getMethod, e);
            return null;
        } catch (IllegalAccessException ignore) {
            LOG.error("illegal access of " + getMethod, ignore);
            throw new ReflectionException(ignore);
        }
    }

    public String getDescription() {
        return executeInfo(NakedObjectsContext.getSession()).getDescription();
    }

    public Object getExtension(final Class cls) {
        return null;
    }

    public Class[] getExtensions() {
        return new Class[0];
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public String getHelp() {
        return executeInfo(NakedObjectsContext.getSession()).getHelp();
    }

    public String getName() {
        return executeInfo(NakedObjectsContext.getSession()).getName();
    }

    /**
     * Set the data in an NakedObject. Passes in an existing object to for the EO to reference.
     */
    public void initValue(final NakedObject inObject, final Object setValue) {
        LOG.debug("local initValue() " + getIdentifier() + " " + inObject.getOid() + "/" + setValue);
        try {
            if (setMethod == null) {
                BusinessValueHolder value = (BusinessValueHolder) getMethod.invoke(inObject.getObject(), new Object[] {});
                if (setValue instanceof String) {
                    value.parseUserEntry((String) setValue);
                } else if (setValue instanceof BusinessValueHolder) {
                    value.copyObject((BusinessValueHolder) setValue);
                }
            } else {
                setMethod.invoke(inObject.getObject(), new Object[] { setValue == null ? null : setValue });
            }
        } catch (InvocationTargetException e) {
            invocationException("Exception executing " + setMethod, e);
        } catch (IllegalAccessException ignore) {
            LOG.error("illegal access of " + setMethod, ignore);
        } catch (ValueParseException e) {
            LOG.error("parse error: " + setValue, e);
        }
    }

    protected Consent isAvailableForObject(final NakedReference target) {
        return executeAbout(AboutType.AVAILABLE, target, null).isAvailable();
    }

    public boolean isEmpty(final NakedObject fromObject) {
        try {
            Object obj = getMethod.invoke(fromObject.getObject(), new Object[0]);
            if (obj instanceof BusinessValueHolder) {
                BusinessValueHolder value = (BusinessValueHolder) obj;
                return value.isEmpty();
            } else {
                return obj == null;
            }
        } catch (InvocationTargetException e) {
            invocationException("Exception executing " + getMethod, e);
            throw new ReflectionException(e);
        } catch (IllegalAccessException ignore) {
            LOG.error("illegal access of " + getMethod, ignore);
            throw new ReflectionException(ignore);
        }
    }

    public Consent isValueValid(final NakedObject inObject, final NakedValue value) {
        return executeAbout(AboutType.VALID, inObject, value).isValid();
    }

    public boolean isVisible(final NakedReference target) {
        return executeAbout(AboutType.VISIBLE, target, null).isVisible().isAllowed();
    }

    public void parseTextEntry(final NakedObject inObject, final String text) {
        if (setMethod == null) {
            try {
                Object obj = getMethod.invoke(inObject.getObject(), new Object[0]);
                BusinessValueHolder value = (BusinessValueHolder) obj;
                value.parseUserEntry(text);
                NakedObjectsContext.getObjectPersistor().objectChanged(inObject);
            } catch (InvocationTargetException e) {
                invocationException("Exception executing " + getMethod, e);
            } catch (IllegalAccessException ignore) {
                LOG.error("illegal access of " + getMethod, ignore);
                throw new ReflectionException("Illegal access of " + getMethod, ignore);
            } catch (ValueParseException e) {
                throw new TextEntryParseException(e);
            }
        } else {
            try {
                setMethod.invoke(inObject.getObject(), new Object[] { text });
            } catch (InvocationTargetException e) {
                invocationException("Exception executing " + setMethod, e);
            } catch (IllegalAccessException ignore) {
                LOG.error("llegal access of " + setMethod, ignore);
                throw new ReflectionException("Illegal access of " + setMethod, ignore);
            }
        }
    }

    public void setValue(final NakedObject inObject, final Object setValue) {
        LOG.debug("local setValue() " + inObject.getOid() + "/" + getIdentifier() + "/" + setValue);
        try {
            if (setMethod == null) {
                NakedObjectsContext.getObjectPersistor().objectChanged(inObject);
            } else {
                setMethod.invoke(inObject.getObject(), new Object[] { setValue });
            }
        } catch (InvocationTargetException e) {
            invocationException("Exception executing " + setMethod, e);
        } catch (IllegalAccessException ignore) {
            LOG.error("illegal access of " + setMethod, ignore);
        } catch (ValueParseException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String toString() {
        String methods = (getMethod == null ? "" : "GET") + (setMethod == null ? "" : " SET");
        return "Association [name=\"" + getIdentifier() + "\", method=" + getMethod + ",about=" + getAboutMethod() + ", methods=" + methods + ", type=" + getType() + " ]";
    }
}
