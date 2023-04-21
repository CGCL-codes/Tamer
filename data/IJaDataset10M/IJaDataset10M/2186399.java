package org.nakedobjects.nof.reflect.java.reflect;

import java.lang.reflect.Method;
import java.util.Enumeration;
import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedCollection;
import org.nakedobjects.noa.adapter.NakedReference;
import org.nakedobjects.noa.annotations.When;
import org.nakedobjects.noa.exceptions.DisabledDeclarativelyException;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.spec.Features;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.reflect.Veto;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nof.reflect.peer.FieldPeer;
import org.nakedobjects.nof.reflect.peer.MemberIdentifier;

public abstract class JavaField extends JavaMember implements FieldPeer {

    protected final Method defaultMethod;

    protected final Method getMethod;

    private final boolean isPersisted;

    private final boolean isOptional;

    protected final Method optionsMethod;

    protected final Method setMethod;

    protected final Class type;

    private final String businessKeyName;

    public JavaField(final MemberIdentifier identifier, final Class type, final DescriptiveMethods descriptiveMethods, final FieldMethods fieldMethods, final GeneralControlMethods controlMethods, final FieldFlags fieldFlags, final MemberSessionMethods sessionMethods) {
        super(identifier, descriptiveMethods, controlMethods, fieldFlags.isProtected(), fieldFlags.isHidden(), sessionMethods);
        this.type = type;
        this.isPersisted = fieldFlags.isPersisted();
        this.isOptional = fieldFlags.isOptional();
        this.getMethod = remember(fieldMethods.getGetMethod());
        this.setMethod = remember(fieldMethods.getSetMethod());
        this.defaultMethod = remember(fieldMethods.getDefaultMethod());
        this.optionsMethod = remember(fieldMethods.getOptionsMethod());
        this.businessKeyName = fieldFlags.getBusinessKeyName();
    }

    public void debugData(DebugString debug) {
        super.debugData(debug);
        debug.appendln("Get", getMethod);
        if (setMethod != null) {
            debug.appendln("Set", setMethod);
        }
        if (isPersisted()) {
            debug.appendln("Persisted");
        }
        if (isOptional) {
            debug.appendln("Optional");
        }
        if (hiddenMethod != null) {
            debug.appendln("Hidden", hiddenMethod);
        }
        if (defaultMethod != null) {
            debug.appendln("Default", defaultMethod);
        }
        if (optionsMethod != null) {
            debug.appendln("Options", optionsMethod);
        }
    }

    public String getBusinessKeyName() {
        return businessKeyName;
    }

    public Object[] getOptions(NakedReference target) {
        Object options = execute(optionsMethod, target.getObject(), new Object[0]);
        if (options == null) {
            return null;
        } else {
            Object[] optionArray;
            NakedCollection adapter = NakedObjectsContext.getObjectLoader().createAdapterForCollection(options, getType());
            Enumeration e = adapter.elements();
            optionArray = new Object[adapter.size()];
            int i = 0;
            while (e.hasMoreElements()) {
                optionArray[i++] = ((Naked) e.nextElement()).getObject();
            }
            return optionArray;
        }
    }

    /**
     * return the object type, as a Class object, that the method returns.
     */
    public NakedObjectSpecification getType() {
        return type == null ? null : NakedObjectsContext.getReflector().loadSpecification(type);
    }

    public Consent isUsable(final NakedReference target) {
        boolean isPersistent = target.getResolveState().isPersistent();
        if (isProtected == When.UNTIL_PERSISTED && !isPersistent) {
            return new Veto("Field disabled until object persisted");
        }
        if (isProtected == When.ONCE_PERSISTED && isPersistent) {
            return new Veto("Field disabled once object persisted");
        }
        NakedObjectSpecification specification = target.getSpecification();
        if (Features.isAlwaysImmutable(specification) || (Features.isImmutableOncePersisted(specification) && isPersistent)) {
            return new Veto("Field disabled as object cannot be changed");
        }
        return executeConsent(usableMethod, target, false);
    }

    /**
     * Returns true if this attribute is persisted, as opposed to being calculated from other data in the
     * object, or only used transiently.
     */
    public boolean isPersisted() {
        return isPersisted;
    }

    public boolean isMandatory() {
        return !isOptional;
    }

    @Override
    public Consent isUsableDeclaratively() {
        if (isProtected == When.ALWAYS) {
            return new Veto(new DisabledDeclarativelyException(getName(), "Field not editable"));
        }
        return super.isUsableDeclaratively();
    }

    public boolean isOptionEnabled() {
        return optionsMethod != null;
    }
}
