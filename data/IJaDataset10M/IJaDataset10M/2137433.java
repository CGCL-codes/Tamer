package com.gwtent.gen.reflection;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.gwtent.client.CheckedExceptionWrapper;
import com.gwtent.client.reflection.Reflection;
import com.gwtent.client.reflection.impl.TypeOracleImpl;
import com.gwtent.gen.GenUtils;
import com.gwtent.gen.LogableSourceCreator;

public class SourceVisitor extends LogableSourceCreator {

    public SourceVisitor(TreeLogger logger, GeneratorContext context, String typeName) {
        super(logger, context, typeName);
    }

    @Override
    protected SourceWriter doGetSourceWriter(JClassType classType) {
        String packageName = classType.getPackage().getName();
        String simpleName = getSimpleUnitName(classType);
        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
        composer.setSuperclass(TypeOracleImpl.class.getCanonicalName());
        composer.addImport("com.gwtent.client.reflection.*");
        composer.addImport("java.util.*");
        composer.addImport(classType.getPackage().getName() + ".*");
        PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
        if (printWriter == null) {
            return null;
        } else {
            SourceWriter sw = composer.createSourceWriter(context, printWriter);
            return sw;
        }
    }

    @Override
    protected String getSUFFIX() {
        return GenUtils.getReflection_SUFFIX();
    }

    @Override
    protected void createSource(SourceWriter source, JClassType classType) {
        source.println("public " + getSimpleUnitName(classType) + "(){");
        source.indent();
        List<JClassType> types = allReflectionClasses();
        for (JClassType type : types) {
            ReflectionProxyGenerator gen = new ReflectionProxyGenerator();
            try {
                String classname = gen.generate(this.logger, context, type.getQualifiedSourceName());
                source.println("new " + classname + "();");
            } catch (UnableToCompleteException e) {
                throw new CheckedExceptionWrapper(e);
            }
        }
        source.outdent();
        source.println("}");
    }

    private List<JClassType> allReflectionClasses() {
        List<JClassType> types = new ArrayList<JClassType>();
        try {
            JClassType reflectionClass = typeOracle.getType(Reflection.class.getCanonicalName());
            for (JClassType classType : typeOracle.getTypes()) {
                if (classType.isAssignableTo(reflectionClass)) {
                    processRelationClasses(types, classType);
                    addClassIfNotExists(types, classType);
                }
            }
        } catch (Exception e) {
            this.logger.log(TreeLogger.Type.ERROR, e.getMessage(), e);
        }
        return types;
    }

    private void processRelationClasses(List<JClassType> types, JClassType classType) {
        if (classType.getSuperclass() != null) {
            processRelationClasses(types, classType.getSuperclass());
            addClassIfNotExists(types, classType.getSuperclass());
        }
        for (JClassType type : classType.getImplementedInterfaces()) {
            addClassIfNotExists(types, type);
        }
        for (JField field : classType.getFields()) {
            addClassIfNotExists(types, field.getType().isClassOrInterface());
        }
        for (JMethod method : classType.getMethods()) {
            if (method.getReturnType() != null) addClassIfNotExists(types, method.getReturnType().isClassOrInterface());
        }
    }

    private void addClassIfNotExists(List<JClassType> types, JClassType classType) {
        if ((classType != null) && (types.indexOf(classType) < 0)) {
            types.add(classType);
        }
    }
}
