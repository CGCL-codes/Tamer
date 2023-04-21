package org.jwatter.toolkit.generate.code;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jwatter.util.StringUtil;

public class ClassDefinition implements Formattable {

    protected int modifiers;

    protected String className;

    protected String baseClassName;

    protected List<FieldDeclaration> fieldDeclarations;

    protected Set<String> fieldNames;

    protected List<MethodDefinition> methodDefinitions;

    protected Set<String> methodSignatures;

    ClassDefinition(int modifiers, String classname) {
        this(modifiers, classname, (String) null);
    }

    ClassDefinition(int modifiers, String classname, String baseclassname) {
        this.modifiers = modifiers;
        this.className = classname;
        this.baseClassName = baseclassname;
        fieldDeclarations = new ArrayList<FieldDeclaration>();
        fieldNames = new HashSet<String>();
        methodDefinitions = new ArrayList<MethodDefinition>();
        methodSignatures = new HashSet<String>();
    }

    ClassDefinition(int modifiers, String classname, Class<?> baseclass) {
        this(modifiers, classname, baseclass.getSimpleName());
    }

    public void addFieldDeclaration(FieldDeclaration field) throws MalformedCodeError {
        String fieldName = field.getName();
        if (!fieldNames.add(fieldName)) {
            throw new MalformedCodeError("duplicate field: " + fieldName);
        }
        fieldDeclarations.add(field);
    }

    public boolean hasFieldDeclaration(String fieldname) {
        return fieldNames.contains(fieldname);
    }

    public MethodBuilder addConstructor(int modifiers, Class<?>[] parametertypes, String[] parameternames, Class<?>[] exceptiontypes) throws MalformedCodeError {
        MethodBuilder constructorBuilder = CodeFactory.createNewConstructorDefinition(modifiers, className, parametertypes, parameternames, exceptiontypes);
        addMethodDefinition(constructorBuilder.getMethodDefinition());
        return constructorBuilder;
    }

    public void addMethodDefinition(MethodDefinition method) throws MalformedCodeError {
        String methodSignature = method.getSignature(true);
        if (!methodSignatures.add(methodSignature)) {
            throw new MalformedCodeError("duplicate method: " + methodSignature);
        }
        methodDefinitions.add(method);
    }

    public boolean containsMethod(MethodDefinition method) {
        return methodSignatures.contains(method.getSignature(true));
    }

    public String format() {
        StringBuilder formatted = new StringBuilder();
        if (modifiers != 0) {
            formatted.append(Modifier.toString(modifiers)).append(" ");
        }
        formatted.append("class ").append(className);
        if (baseClassName != null) {
            formatted.append(" extends ").append(baseClassName);
        }
        formatted.append("\n{\n");
        for (Formattable code : fieldDeclarations) {
            formatted.append("\t").append(code.format()).append("\n");
        }
        formatted.append("\n");
        for (Formattable code : methodDefinitions) {
            formatted.append(code.format());
            formatted.append("\n");
        }
        formatted.append("}\n");
        return formatted.toString();
    }

    public String getName() {
        return className;
    }

    public String getEncoding() {
        String enc = "UTF-8";
        if (StringUtil.containsNonLatin1Characters(className)) return enc;
        if (baseClassName != null && StringUtil.containsNonLatin1Characters(baseClassName)) return enc;
        for (Formattable code : fieldDeclarations) {
            enc = code.getEncoding();
            if (enc.equals("UTF-8")) return enc;
        }
        for (Formattable code : methodDefinitions) {
            enc = code.getEncoding();
            if (enc.equals("UTF-8")) return enc;
        }
        return "ISO-8859-1";
    }
}
