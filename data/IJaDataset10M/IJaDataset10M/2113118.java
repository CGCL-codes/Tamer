package com.mvu.banana.domain.server;

import com.mvu.banana.domain.stub.Credential;
import com.mvu.banana.internal.server.BuilderBase;
import com.mvu.banana.tools.CodeGenerator;
import com.mvu.banana.tools.JavaClass;
import com.mvu.banana.tools.JavaMethod;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import static com.mvu.banana.tools.GeneratorUtil.getEntityName;
import static com.mvu.banana.tools.GeneratorUtil.getFieldName;

/**
 */
public class BuilderGenerator extends CodeGenerator {

    private static final String stubPackage = "com.mvu.banana.internal.builder.";

    private static final String builderPackage = "com.mvu.banana.internal.server.";

    public static void main(String[] args) {
        genBuilder(Credential.class);
    }

    public static void genBuilder(Class<?> defClass) {
        String simpleName = getEntityName(defClass.getSimpleName());
        String stubName = stubPackage + simpleName + "BuilderStub";
        String builderName = builderPackage + simpleName + "Builder";
        JavaClass stub = new JavaClass().withName(stubName).withSuperClass(BuilderBase.class, simpleName);
        stub.importClass(builderName);
        stub.importClass(getEntityName(defClass.getName()));
        JavaClass builder = new JavaClass().withName(builderName).withSuperClass(simpleName + "BuilderStub");
        JavaMethod constructor = stub.addConstructor();
        constructor.addLine("super(new ", simpleName, "())");
        constructor.addLine("init()");
        builder.addMethod(false, "void", "init", new String[0]);
        stub.addMethod(false, "void", "init", new String[0]);
        for (Field field : defClass.getDeclaredFields()) {
            String fieldName = getFieldName(field.getName());
            Class<?> fieldType = field.getType();
            if (Collection.class.isAssignableFrom(fieldType)) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class<?> typeArg = (Class<?>) genericType.getActualTypeArguments()[0];
                String typeName = typeArg.getSimpleName();
                stub.importClass(typeArg);
                JavaMethod withMethod = stub.addMethod(false, simpleName + "Builder", "with" + fieldName, typeName + "...");
                withMethod.addLine("for(", typeName, " item : p0){\n      product.addTo", fieldName, "(item)");
                withMethod.addLine("}");
                withMethod.addLine("return (", simpleName, "Builder) this");
            } else {
                JavaMethod withMethod = stub.addMethod(false, simpleName + "Builder", "with" + fieldName, fieldType);
                withMethod.addLine("product.set", fieldName, "(p0)");
                withMethod.addLine("return (", simpleName, "Builder) this");
            }
        }
        logger.info(stub.print());
        stub.writeToMain(true);
        logger.info(builder.print());
        builder.writeToMain(false);
    }
}
