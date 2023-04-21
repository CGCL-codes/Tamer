package com.google.gwt.i18n.rebind;

import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JEnumConstant;
import com.google.gwt.core.ext.typeinfo.JEnumType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.i18n.rebind.AbstractResource.ResourceEntry;
import com.google.gwt.i18n.rebind.AbstractResource.ResourceList;
import com.google.gwt.i18n.server.AbstractMessage;
import com.google.gwt.i18n.server.AbstractParameter;
import com.google.gwt.i18n.server.MessageInterface;
import com.google.gwt.i18n.server.MessageTranslation;
import com.google.gwt.i18n.server.Parameter;
import com.google.gwt.i18n.server.Type;
import com.google.gwt.i18n.shared.GwtLocale;
import com.google.gwt.i18n.shared.GwtLocaleFactory;
import com.google.gwt.safehtml.shared.SafeHtml;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the {@link com.google.gwt.i18n.server.Message} API on
 * top of type oracle.
 */
public class TypeOracleMessage extends AbstractMessage {

    /**
   * An implementation of {@link Parameter} on top of type oracle.
   */
    public class TypeOracleParameter extends AbstractParameter {

        private final JParameter param;

        public TypeOracleParameter(int index, JParameter param) {
            super(getLocaleFactory(), index, mapJTypeToType(param.getType()));
            this.param = param;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annotClass) {
            return param.getAnnotation(annotClass);
        }

        @Override
        public String getName() {
            return param.getName();
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotClass) {
            return param.isAnnotationPresent(annotClass);
        }
    }

    /**
   * A single translated message obtained from an {@link AbstractResource}.
   */
    private class ResourceMessageTranslation implements MessageTranslation {

        private final ResourceEntry resourceEntry;

        private final GwtLocale matchedLocale;

        public ResourceMessageTranslation(ResourceEntry resourceEntry, GwtLocale matchedLocale) {
            this.resourceEntry = resourceEntry;
            this.matchedLocale = matchedLocale;
        }

        public Iterable<AlternateFormMapping> getAllMessageForms() {
            List<AlternateFormMapping> mapping = new ArrayList<AlternateFormMapping>();
            if (!isStringMap()) {
                mapping.add(new AlternateFormMapping(defaultForms(), getDefaultMessage()));
            }
            int numSelectors = getSelectorParameterIndices().length;
            for (String joinedForms : resourceEntry.getForms()) {
                addMapping(mapping, numSelectors, joinedForms, resourceEntry.getForm(joinedForms));
            }
            Collections.sort(mapping);
            return mapping;
        }

        public String getDefaultMessage() {
            return resourceEntry.getForm(null);
        }

        public GwtLocale getMatchedLocale() {
            return matchedLocale;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("ResourceMT: ").append(resourceEntry);
            return buf.toString();
        }
    }

    private final JMethod method;

    private final TypeOracle oracle;

    private List<Parameter> parameters;

    private final ResourceList resources;

    public TypeOracleMessage(TypeOracle oracle, GwtLocaleFactory localeFactory, MessageInterface msgIntf, JMethod method, ResourceList resources) {
        super(localeFactory, msgIntf);
        this.method = method;
        this.oracle = oracle;
        this.resources = resources;
        init();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotClass) {
        A annot = method.getAnnotation(annotClass);
        if (annot != null) {
            return annot;
        }
        return method.getEnclosingType().findAnnotationInTypeHierarchy(annotClass);
    }

    @Override
    public String getMethodName() {
        return method.getName();
    }

    @Override
    public List<Parameter> getParameters() {
        if (parameters == null) {
            ensureParameters();
        }
        return parameters;
    }

    @Override
    public Type getReturnType() {
        JType returnType = method.getReturnType();
        return mapJTypeToType(returnType);
    }

    @Override
    public MessageTranslation getTranslation(GwtLocale locale) {
        ResourceEntry entry = null;
        if (resources != null) {
            entry = resources.getEntry(getKey());
        }
        if (entry != null) {
            return new ResourceMessageTranslation(entry, resources.findLeastDerivedLocale(null, locale));
        }
        return this;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotClass) {
        return method.isAnnotationPresent(annotClass);
    }

    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    protected Type mapJTypeToType(JType type) {
        JPrimitiveType primType = type.isPrimitive();
        if (primType != null) {
            if (primType == JPrimitiveType.BOOLEAN) {
                return Type.BOOLEAN;
            }
            if (primType == JPrimitiveType.BYTE) {
                return Type.BYTE;
            }
            if (primType == JPrimitiveType.CHAR) {
                return Type.CHAR;
            }
            if (primType == JPrimitiveType.DOUBLE) {
                return Type.DOUBLE;
            }
            if (primType == JPrimitiveType.FLOAT) {
                return Type.FLOAT;
            }
            if (primType == JPrimitiveType.INT) {
                return Type.INT;
            }
            if (primType == JPrimitiveType.LONG) {
                return Type.LONG;
            }
            if (primType == JPrimitiveType.SHORT) {
                return Type.SHORT;
            }
            throw new RuntimeException("Unexpected primitive type " + primType);
        }
        JArrayType arrayType = type.isArray();
        if (arrayType != null) {
            JType componentType = arrayType.getComponentType();
            return new Type.ArrayType(componentType.getQualifiedSourceName() + "[]", mapJTypeToType(componentType));
        }
        JEnumType enumType = type.isEnum();
        String qualSourceName = type.getQualifiedSourceName();
        if (enumType != null) {
            JEnumConstant[] enumConstants = enumType.getEnumConstants();
            int n = enumConstants.length;
            String[] names = new String[n];
            for (int i = 0; i < n; ++i) {
                names[i] = enumConstants[i].getName();
            }
            return new Type.EnumType(qualSourceName, names);
        }
        String stringQualifiedName = String.class.getCanonicalName();
        if (stringQualifiedName.equals(qualSourceName)) {
            return Type.STRING;
        }
        if (SafeHtml.class.getCanonicalName().equals(qualSourceName)) {
            return Type.SAFEHTML;
        }
        JClassType date = oracle.findType(Date.class.getCanonicalName());
        JClassType classType = type.isClassOrInterface();
        if (date != null && classType != null && date.isAssignableFrom(classType)) {
            return Type.DATE;
        }
        String listQualifiedName = List.class.getCanonicalName();
        JClassType list = oracle.findType(listQualifiedName);
        JParameterizedType parameterizedType = type.isParameterized();
        if (list != null && classType != null && list.isAssignableFrom(classType)) {
            if (parameterizedType == null) {
                return new Type.ListType(listQualifiedName + "<Object>", Type.OBJECT);
            }
            JType componentType = parameterizedType.getTypeArgs()[0];
            return new Type.ListType(listQualifiedName + "<" + componentType.getQualifiedSourceName() + ">", mapJTypeToType(componentType));
        }
        String mapQualifiedName = Map.class.getCanonicalName();
        JClassType map = oracle.findType(mapQualifiedName);
        if (map != null && classType != null && map.isAssignableFrom(classType) && parameterizedType != null) {
            JClassType[] typeArgs = parameterizedType.getTypeArgs();
            if (typeArgs.length == 2 && stringQualifiedName.equals(typeArgs[0].getQualifiedSourceName()) && stringQualifiedName.equals(typeArgs[1].getQualifiedSourceName())) {
                return Type.STRING_MAP;
            }
        }
        return new Type(qualSourceName);
    }

    private void ensureParameters() {
        parameters = new ArrayList<Parameter>();
        int i = 0;
        for (JParameter parameter : method.getParameters()) {
            parameters.add(new TypeOracleParameter(i++, parameter));
        }
    }
}
