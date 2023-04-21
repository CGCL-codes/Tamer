package com.totsp.gwt.freezedry.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.rpc.SerializableTypeOracle;
import com.google.gwt.user.rebind.rpc.TypeSerializerCreator;
import com.totsp.gwt.freezedry.server.SimpleSerializer;
import java.io.PrintWriter;

/**
 *
 * @author rcooper
 */
public class FreezerGenerator extends Generator {

    /** Creates a new instance of FreezerGenerator */
    public FreezerGenerator() {
    }

    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
        String factoryClassName = typeName + "Factory";
        TypeOracle typeOracle = context.getTypeOracle();
        try {
            Object factory = null;
            try {
                factory = Class.forName(factoryClassName).newInstance();
            } catch (ClassNotFoundException nfe) {
                logger.log(TreeLogger.INFO, "The factory class: " + factoryClassName + " was not found on the classpath. Creating Dictionary implementation.", null);
            } catch (InstantiationException ine) {
                logger.log(TreeLogger.ERROR, "The factory class: " + factoryClassName + " could not be instantiated.", ine);
            } catch (IllegalAccessException ine) {
                logger.log(TreeLogger.ERROR, "The factory class: " + factoryClassName + " could not be instantiated.", ine);
            }
            Class iface = Class.forName(typeName);
            JClassType interfaceType = (JClassType) typeOracle.getType(typeName);
            String implementationPackage = interfaceType.getPackage().getName();
            String implementationName = interfaceType.getSimpleSourceName() + "_Impl";
            logger.log(logger.INFO, "Checking " + interfaceType, null);
            SerializableTypeOracleBuilder stob = new SerializableTypeOracleBuilder(logger, typeOracle);
            SerializableTypeOracle sto = stob.build(context.getPropertyOracle(), interfaceType);
            TypeSerializerCreator tsc = new TypeSerializerCreator(logger, sto, context, interfaceType);
            String serializerName = tsc.realize(logger);
            logger.log(TreeLogger.INFO, "Serializer: " + serializerName, null);
            PrintWriter printWriter = context.tryCreate(logger, implementationPackage, implementationName);
            if (printWriter == null) {
                return implementationPackage + "." + implementationName;
            }
            ClassSourceFileComposerFactory cfcf = new ClassSourceFileComposerFactory(implementationPackage, implementationName);
            cfcf.addImplementedInterface(typeName);
            SourceWriter writer = cfcf.createSourceWriter(context, printWriter);
            writer.println("private static final " + serializerName + " SERIALIZER = new " + serializerName + "();");
            if (factory == null) {
                writer.println("private static final com.google.gwt.i18n.client.Dictionary DICTIONARY = ");
                writer.println("    com.google.gwt.i18n.client.Dictionary.getDictionary(\"" + interfaceType.getQualifiedSourceName().replace(".", "_") + "\");");
            }
            JMethod[] methods = interfaceType.getMethods();
            for (int i = 0; i < methods.length; i++) {
                logger.log(TreeLogger.WARN, methods[i].getName(), null);
                if (methods[i].getParameters() != null && methods[i].getParameters().length > 0) {
                    logger.log(TreeLogger.ERROR, typeName + " " + methods[i].getName() + " is not a no-arguments method.", null);
                    throw new UnableToCompleteException();
                }
                String decl = methods[i].getReadableDeclaration(false, true, false, true, true);
                if (decl.indexOf("<") > -1) {
                    decl = decl.subSequence(0, decl.indexOf("<")) + decl.substring(decl.lastIndexOf(">") + 1, decl.length());
                }
                writer.println(decl + " " + "{");
                writer.indent();
                for (int j = 0; j < iface.getDeclaredMethods().length; j++) {
                    if (iface.getDeclaredMethods()[j].getName().equals(methods[i].getName())) {
                        if (factory == null) {
                            writer.println("String value = DICTIONARY.get(\"" + methods[i].getName() + "\");");
                        } else {
                            IsSerializable value = (IsSerializable) iface.getDeclaredMethods()[i].invoke(factory, new Object[0]);
                            writer.println("String value = \"" + escape(SimpleSerializer.serializeObject(value)) + "\";");
                        }
                        writer.println("com.google.gwt.core.client.GWT.log( \"Deserializing value: \"+ value, null ); ");
                        writer.println("com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader reader = new com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader(SERIALIZER); ");
                        writer.println("try{");
                        writer.indent();
                        writer.println("com.google.gwt.core.client.GWT.log( \"Value: \"+ value, null );");
                        writer.println("reader.prepareToRead( value );");
                        writer.println("return (" + methods[i].getReturnType().getQualifiedSourceName() + ") reader.readObject();");
                        writer.outdent();
                        writer.println("} catch( Exception e ){ ");
                        writer.indent();
                        writer.println("com.google.gwt.core.client.GWT.log( \"Exception: \"+ e.toString(), e );");
                        writer.println("throw new RuntimeException( e.toString() );");
                        writer.outdent();
                        writer.println("}");
                    }
                }
                writer.outdent();
                writer.println("}");
            }
            writer.outdent();
            writer.println("}");
            logger.log(TreeLogger.WARN, "Here!", null);
            context.commit(logger, printWriter);
            return implementationPackage + "." + implementationName;
        } catch (NotFoundException nfe) {
            logger.log(TreeLogger.ERROR, "Unable to get type info for " + typeName, nfe);
            throw new UnableToCompleteException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToCompleteException();
        }
    }
}
