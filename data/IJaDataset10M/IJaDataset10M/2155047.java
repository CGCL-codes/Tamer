package net.sf.gwtruts;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import java.io.PrintWriter;

/**
 *This class is internally used by GWT for late binding of Controllers and Views
 * @author Reza
 */
public class FactoryGenerator extends Generator {

    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
        logger.log(TreeLogger.INFO, "Generating source for " + typeName, null);
        TypeOracle typeOracle = context.getTypeOracle();
        JClassType originalType = typeOracle.findType(typeName);
        if (originalType == null) {
            logger.log(TreeLogger.ERROR, "Unable to find metadata for type '" + typeName + "'", null);
            throw new UnableToCompleteException();
        }
        logger.log(TreeLogger.INFO, "Generating source for " + originalType.getQualifiedSourceName(), null);
        String generatedClassQualifiedName;
        try {
            generatedClassQualifiedName = createClass(logger, context, typeName);
        } catch (NotFoundException ex) {
            throw new UnableToCompleteException();
        }
        if (generatedClassQualifiedName == null) {
            throw new UnableToCompleteException();
        }
        return generatedClassQualifiedName;
    }

    private String createClass(TreeLogger logger, GeneratorContext context, String typeName) throws NotFoundException {
        TypeOracle typeOracle = context.getTypeOracle();
        JClassType originalType = typeOracle.getType(typeName);
        String packageName = originalType.getPackage().getName();
        String originalClassName = originalType.getSimpleSourceName();
        String generatedClassName = originalClassName + "Gen";
        SourceWriter sourceWriter = getSourceWriter(logger, context, originalType, packageName, generatedClassName);
        writeClass(logger, originalType, typeOracle, sourceWriter);
        return originalType.getParameterizedQualifiedSourceName() + "Gen";
    }

    private SourceWriter getSourceWriter(TreeLogger logger, GeneratorContext context, JClassType originalType, String packageName, String generatedClassName) {
        ClassSourceFileComposerFactory classFactory = new ClassSourceFileComposerFactory(packageName, generatedClassName);
        classFactory.addImplementedInterface(originalType.getName());
        PrintWriter printWriter = context.tryCreate(logger, packageName, generatedClassName);
        if (printWriter == null) {
            return null;
        }
        SourceWriter sourceWriter = classFactory.createSourceWriter(context, printWriter);
        return sourceWriter;
    }

    private void writeClass(TreeLogger logger, JClassType originalType, TypeOracle typeOracle, SourceWriter sourceWriter) throws NotFoundException {
        String packageName = originalType.getPackage().getName();
        JClassType reflectableType = typeOracle.getType(packageName + ".Instantiable");
        if (sourceWriter != null) {
            sourceWriter.indent();
            String s = "public " + reflectableType.getQualifiedSourceName() + " newInstance(String className) {";
            sourceWriter.println(s);
            logger.log(TreeLogger.INFO, s);
            JClassType[] types = typeOracle.getTypes();
            int count = 0;
            for (int i = 0; i < types.length; i++) {
                if (types[i].isInterface() == null && types[i].isAssignableTo(reflectableType)) {
                    if (count == 0) {
                        s = "   if(\"" + types[i].getQualifiedSourceName() + "\".equals(className)) {" + " return new " + types[i].getQualifiedSourceName() + "();" + "}";
                        sourceWriter.println(s);
                        logger.log(TreeLogger.INFO, s);
                    } else {
                        s = "   else if(\"" + types[i].getQualifiedSourceName() + "\".equals(className)) {" + " return new " + types[i].getQualifiedSourceName() + "();" + "}";
                        sourceWriter.println(s);
                        logger.log(TreeLogger.INFO, s);
                    }
                    count++;
                }
            }
            sourceWriter.println("return null;");
            logger.log(TreeLogger.INFO, "return null;}");
            sourceWriter.println("}");
            logger.log(TreeLogger.INFO, "Done Generating source for " + originalType.getName(), null);
            sourceWriter.commit(logger);
        }
    }
}
