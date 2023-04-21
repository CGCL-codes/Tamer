package com.agimatec.annotations.jam;

import com.agimatec.annotations.DTOAttribute;
import com.agimatec.annotations.DTOAttributes;
import com.sun.javadoc.Type;
import com.sun.tools.javadoc.MethodDocImpl;
import com.sun.tools.javadoc.ParameterizedTypeImpl;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jam.JAnnotatedElement;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JField;
import org.codehaus.jam.JMethod;
import java.util.StringTokenizer;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 13.06.2007 <br/>
 * Time: 14:55:23 <br/>
 * Copyright: Agimatec GmbH
 */
public class JAMDtoMethod extends JAMDtoAnnotatedElement {

    private final JMethod jmethod;

    private final JAMDtoClass dtoClass;

    public JAMDtoMethod(JMethod jmethod, JAMDtoClass dtoClass) {
        this.jmethod = jmethod;
        this.dtoClass = dtoClass;
    }

    public JAMDtoClass getDtoClass() {
        return dtoClass;
    }

    public JAnnotatedElement element() {
        return jmethod;
    }

    protected String singleAnnotation() {
        return DTOAttribute.class.getName();
    }

    protected String multiAnnotation() {
        return DTOAttributes.class.getName();
    }

    public String getName() {
        int offset = 3;
        if (getType().equals("boolean") && jmethod.getSimpleName().startsWith("is")) {
            offset = 2;
        }
        return StringUtils.uncapitalize(jmethod.getSimpleName().substring(offset));
    }

    public String getType() {
        return jmethod.getReturnType().getQualifiedName();
    }

    public JClass getTypeJClass() {
        return jmethod.getReturnType();
    }

    public JField getTypeField(String path) {
        if (path == null || path.length() == 0) return null;
        StringTokenizer tokens = new StringTokenizer(path, ".");
        JClass current = jmethod.getReturnType();
        JField field = null;
        while (tokens.hasMoreTokens() && current != null) {
            String each = tokens.nextToken();
            field = findField(current, each);
            current = (field == null) ? null : field.getType();
        }
        return field;
    }

    @Override
    public String getGenericParameter() {
        return getGenericParameter(jmethod);
    }

    public static String getGenericParameter(JMethod jmethod) {
        Type type = ((MethodDocImpl) jmethod.getArtifact()).returnType();
        Type genericType = null;
        if (type instanceof ParameterizedTypeImpl) {
            Type[] args = ((ParameterizedTypeImpl) type).typeArguments();
            if (args != null && args.length == 1) {
                genericType = args[0];
            }
        }
        return genericType == null ? null : genericType.toString();
    }

    public boolean isEnumType() {
        return jmethod.getReturnType().isEnumType();
    }
}
