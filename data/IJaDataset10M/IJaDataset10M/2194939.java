package org.nakedobjects.reflector.original.reflect;

import org.nakedobjects.applib.control.FieldAbout;
import org.nakedobjects.applib.control.FieldInfo;
import org.nakedobjects.object.reflect.FieldPeer;
import org.nakedobjects.object.reflect.MemberIdentifier;
import org.nakedobjects.object.reflect.defaults.MemberIdentifierImpl;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.log4j.Logger;

public abstract class AbstractOneToManyStrategy implements OneToManyStrategy {

    private static final Logger LOG = Logger.getLogger(AbstractOneToManyStrategy.class);

    public void introspectAssociations(final JavaIntrospector introspector, final Vector associations) {
        Vector v = introspector.findPrefixedMethods(JavaIntrospector.OBJECT, JavaIntrospector.GET_PREFIX, getReturnType(), 0);
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            Method getMethod = (Method) e.nextElement();
            LOG.debug("  identified 1-many association method " + getMethod);
            String name = JavaIntrospector.javaBaseName(getMethod.getName());
            Method infoMethod = introspector.findMethod(JavaIntrospector.CLASS, JavaIntrospector.INFO_PREFIX + name, null, new Class[] { FieldInfo.class });
            if (infoMethod == null) {
                infoMethod = introspector.getDefaultInfoFieldMethod();
            }
            Method aboutMethod = introspector.findMethod(JavaIntrospector.OBJECT, JavaIntrospector.ABOUT_PREFIX + name, null, new Class[] { FieldAbout.class, null, boolean.class });
            Class aboutType = (aboutMethod == null) ? null : aboutMethod.getParameterTypes()[1];
            if (aboutMethod == null) {
                aboutMethod = introspector.getDefaultAboutFieldMethod();
            }
            Method addMethod = introspector.findMethod(JavaIntrospector.OBJECT, "addTo" + name, void.class, null);
            if (addMethod == null) {
                addMethod = introspector.findMethod(JavaIntrospector.OBJECT, "add" + name, void.class, null);
            }
            if (addMethod == null) {
                addMethod = introspector.findMethod(JavaIntrospector.OBJECT, "associate" + name, void.class, null);
            }
            Method removeMethod = introspector.findMethod(JavaIntrospector.OBJECT, "removeFrom" + name, void.class, null);
            if (removeMethod == null) {
                removeMethod = introspector.findMethod(JavaIntrospector.OBJECT, "remove" + name, void.class, null);
            }
            if (removeMethod == null) {
                removeMethod = introspector.findMethod(JavaIntrospector.OBJECT, "dissociate" + name, void.class, null);
            }
            Class removeType = (removeMethod == null) ? null : removeMethod.getParameterTypes()[0];
            Class addType = (addMethod == null) ? null : addMethod.getParameterTypes()[0];
            Class elementType = getElementType(getMethod, aboutType, removeType, addType);
            if (elementType == null) {
                LOG.warn("cannot determine a type for the collection " + name + "; not added as a field");
                elementType = Object.class;
            }
            if (((aboutType != null) && (aboutType != elementType)) || ((addType != null) && (addType != elementType)) || ((removeType != null) && (removeType != elementType))) {
                LOG.error("The add/remove/associate/dissociate/about methods in " + introspector.className() + " must " + "all deal with same type of object.  There are at least two different types");
            }
            boolean isHidden = false;
            if (name.startsWith(JavaIntrospector.HIDDEN_PREFIX)) {
                isHidden = true;
                name = name.substring(JavaIntrospector.HIDDEN_PREFIX.length());
            }
            boolean isDerived = addMethod == null && removeMethod == null;
            MemberIdentifier identifier = new MemberIdentifierImpl(introspector.className(), name);
            FieldPeer peer = createPeer(getMethod, infoMethod, aboutMethod, addMethod, removeMethod, elementType, isHidden, isDerived, identifier);
            associations.addElement(peer);
        }
    }

    protected Class getElementType(Method getMethod, Class aboutType, Class removeType, Class addType) {
        Class elementType = (aboutType == null) ? null : aboutType;
        elementType = (addType == null) ? elementType : addType;
        elementType = (removeType == null) ? elementType : removeType;
        return elementType;
    }

    protected FieldPeer createPeer(Method getMethod, Method infoMethod, Method aboutMethod, Method addMethod, Method removeMethod, Class elementType, boolean isHidden, boolean isDerived, MemberIdentifier identifier) {
        return new JavaOneToManyAssociation(identifier, elementType, getMethod, addMethod, removeMethod, infoMethod, aboutMethod, isHidden, isDerived);
    }
}
