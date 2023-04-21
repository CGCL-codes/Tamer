package org.flexharmony.harmonizer;

import static org.flexharmony.harmonizer.utils.CommonMethods.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.flexharmony.ASAcces;
import org.flexharmony.ASFieldType;
import org.flexharmony.ASVisiblity;
import org.flexharmony.harmonizer.config.HarmonyConfiguration;
import org.flexharmony.harmonizer.javadef.ASFieldImpl;
import org.flexharmony.harmonizer.javadef.JavaClassDefinition;
import org.flexharmony.harmonizer.javadef.JavaClassVisitor;
import org.flexharmony.harmonizer.javadef.JavaFieldDefinition;
import org.flexharmony.harmonizer.javadef.JavaType;
import org.flexharmony.harmonizer.javadef.JavaVisibility;
import org.flexharmony.harmonizer.utils.Pair;
import org.objectweb.asm.ClassReader;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASMetaTag;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.dom.ASMethod.AccessorRole;

public class Harmonizer {

    private HarmonyConfiguration configuration;

    private ActionScriptFactory asFactory;

    private ActionScriptProject asProject;

    private boolean updatesMadeSinceLastWrite;

    public Harmonizer(HarmonyConfiguration configuration) {
        this.configuration = configuration;
        asFactory = new ActionScriptFactory();
        asProject = asFactory.newEmptyASProject(configuration.getFlexSrcDir().getAbsolutePath());
    }

    public boolean updatesMadeSinceLastWrite() {
        return updatesMadeSinceLastWrite;
    }

    public boolean harmonize(File classFile) throws IOException {
        FileInputStream fis = new FileInputStream(classFile);
        ClassReader cr = new ClassReader(fis);
        JavaClassVisitor classVisitor = new JavaClassVisitor();
        cr.accept(classVisitor, 0);
        fis.close();
        JavaClassDefinition javaClassDef = classVisitor.getClassDef();
        String packageDirPath = (javaClassDef.type.qName.packge == null) ? "" : (File.separator + javaClassDef.type.qName.packge.replace('.', File.separatorChar));
        File asDir = new File(configuration.getFlexSrcDir().getAbsolutePath() + packageDirPath);
        if (!asDir.exists()) {
            asDir.mkdirs();
        }
        File asFile = new File(asDir.getAbsolutePath() + File.separator + javaClassDef.type.qName.name + ".as");
        boolean updatesMade;
        if (!asFile.exists() || configuration.isCleanUpdates()) {
            ASCompilationUnit unit = asProject.newClass(javaClassDef.type.qName.toString());
            ASClassType classType = (ASClassType) unit.getType();
            ASMetaTag metaTag = classType.newMetaTag("RemoteClass");
            updateSuperType(unit, javaClassDef);
            metaTag.addParam("alias", javaClassDef.type.qName.toString());
            updateFields(unit, javaClassDef);
            updatesMade = true;
        } else {
            FileInputStream in = new FileInputStream(asFile);
            InputStreamReader reader = new InputStreamReader(in);
            ActionScriptParser parser = asFactory.newParser();
            ASCompilationUnit unit = parser.parse(reader);
            updatesMade = updateSuperType(unit, javaClassDef);
            updatesMade = updatesMade || updateFields(unit, javaClassDef);
            if (updatesMade) {
                asProject.addCompilationUnit(unit);
            }
            reader.close();
        }
        updatesMadeSinceLastWrite = updatesMadeSinceLastWrite || updatesMade;
        return updatesMade;
    }

    public boolean writeUpdates() throws IOException {
        if (updatesMadeSinceLastWrite) {
            asProject.performAutoImport();
            asProject.writeAll();
            updatesMadeSinceLastWrite = false;
            return true;
        } else {
            return false;
        }
    }

    boolean updateSuperType(ASCompilationUnit asUnit, JavaClassDefinition javaClassDef) {
        ASClassType asClass = ((ASClassType) asUnit.getType());
        if (javaClassDef.type.superQName != null) {
            String javaSuperClass = javaClassDef.type.superQName.name;
            String asSuperClass = asClass.getSuperclass();
            if (!javaSuperClass.equals(asSuperClass)) {
                if (!javaClassDef.type.isSuperClassInSamePackage()) {
                    asUnit.getPackage().addImport(javaClassDef.type.superQName.toString());
                }
                asClass.setSuperclass(javaSuperClass);
            }
            return true;
        } else if (asClass.getSuperclass() != null) {
            asClass.setSuperclass(null);
            return true;
        } else {
            return false;
        }
    }

    boolean updateFields(ASCompilationUnit compilationUnit, JavaClassDefinition javaClassDef) {
        boolean updatesMade = false;
        boolean syncAllFields = ((javaClassDef.asClass == null) || javaClassDef.asClass.syncAllFields());
        ASClassType asClass = (ASClassType) compilationUnit.getType();
        ASPackage asPackage = compilationUnit.getPackage();
        List<String> fieldNames = new ArrayList<String>();
        List<Pair<JavaFieldDefinition, ASFieldType>> propertyFields = new ArrayList<Pair<JavaFieldDefinition, ASFieldType>>();
        for (JavaFieldDefinition javaFieldDef : javaClassDef.fields) {
            if (javaFieldDef.asTransient || (!syncAllFields && (javaFieldDef.asField == null))) continue;
            if ((javaFieldDef.asField == null) && (javaFieldDef.visibility == JavaVisibility.PRIVATE) && !javaClassDef.hasPublicBeanAccessors(javaFieldDef)) {
                continue;
            }
            boolean hasAnnotation = (javaFieldDef.asField != null);
            org.flexharmony.ASField asFieldAnnot = hasAnnotation ? javaFieldDef.asField : new ASFieldImpl();
            boolean hasProperty;
            if (asFieldAnnot.access() == ASAcces.PROPERTY) {
                hasProperty = true;
            } else if (asFieldAnnot.access() == ASAcces.FIELD) {
                hasProperty = false;
            } else if (javaFieldDef.isStatic) {
                hasProperty = false;
            } else {
                hasProperty = (asFieldAnnot.visibility() == ASVisiblity.NULL) || (asFieldAnnot.visibility() == ASVisiblity.PRIVATE);
            }
            Visibility visibility;
            if (asFieldAnnot.visibility() != ASVisiblity.NULL) {
                visibility = getVisiblity(asFieldAnnot.visibility());
            } else {
                if (javaFieldDef.isStatic) {
                    visibility = getVisiblity(javaFieldDef.visibility);
                } else if ((asFieldAnnot.access() == ASAcces.NULL) || (asFieldAnnot.access() == ASAcces.PROPERTY)) {
                    visibility = Visibility.PRIVATE;
                } else {
                    visibility = Visibility.PUBLIC;
                }
            }
            String fieldName = hasProperty ? ("_" + javaFieldDef.name) : javaFieldDef.name;
            fieldNames.add(fieldName);
            if ((asClass.getField(javaFieldDef.name) != null) || (asClass.getField(fieldName) != null)) {
                continue;
            }
            ASFieldType asFieldType = (asFieldAnnot.type() == ASFieldType.NULL) ? getDefaultType(javaFieldDef.type) : asFieldAnnot.type();
            String asTypeName;
            if (asFieldType == ASFieldType.CUSTOM) {
                addImportIfNeeded(javaFieldDef.type.qName, asPackage);
                asTypeName = javaFieldDef.type.qName.name;
            } else {
                addImportIfNeeded(asFieldType.qName, asPackage);
                asTypeName = asFieldType.qName.name;
            }
            ASField asField = asClass.newField(fieldName, visibility, asTypeName);
            asField.setConst(javaFieldDef.isFinal);
            asField.setStatic(javaFieldDef.isStatic);
            if ((asFieldAnnot.initialization() != null) && (asFieldAnnot.initialization().trim().length() > 0)) {
                asField.setInitializer(asFieldAnnot.initialization().trim());
            } else if (javaFieldDef.initializationValue != null) {
                asField.setInitializer(getInitializationExpression(asFieldType, javaFieldDef.initializationValue));
            }
            if (hasProperty) propertyFields.add(new Pair<JavaFieldDefinition, ASFieldType>(javaFieldDef, asFieldType));
            updatesMade = true;
        }
        for (Pair<JavaFieldDefinition, ASFieldType> pair : propertyFields) {
            JavaFieldDefinition javaFieldDef = pair.getX();
            ASFieldType asFieldType = pair.getY();
            String propertyName = javaFieldDef.name;
            String fieldName = "_" + propertyName;
            String typeName = (asFieldType == ASFieldType.CUSTOM) ? javaFieldDef.type.qName.name : asFieldType.qName.name;
            ASMethod getter = asClass.newMethod(propertyName, Visibility.PUBLIC, typeName);
            getter.setAccessorRole(AccessorRole.GETTER);
            getter.addStmt("return " + fieldName + ";");
            ASMethod setter = asClass.newMethod(propertyName, Visibility.PUBLIC, "void");
            setter.setAccessorRole(AccessorRole.SETTER);
            setter.addParam("value", typeName);
            setter.addStmt(fieldName + " = value;");
        }
        if (!configuration.isAdditiveOnly()) {
            List<ASField> asFields = asClass.getFields();
            for (ASField asField : asFields) {
                String asFieldName = asField.getName();
                if (!fieldNames.contains(asFieldName)) {
                    asClass.removeField(asFieldName);
                    if (asFieldName.startsWith("_") && (asFieldName.length() > 1)) {
                        String propertyName = asFieldName.substring(1, asFieldName.length());
                        asClass.removeMethod(propertyName);
                        asClass.removeMethod(propertyName);
                    }
                    updatesMade = true;
                }
            }
        }
        return updatesMade;
    }

    Expression getInitializationExpression(ASFieldType fieldType, Object initialValue) {
        switch(fieldType) {
            case STRING:
                return asFactory.newStringLiteral(initialValue.toString());
            case NUMBER:
            case INT:
                return asFactory.newIntegerLiteral(Integer.parseInt(initialValue.toString()));
            case BOOLEAN:
                return asFactory.newBooleanLiteral(Boolean.parseBoolean(initialValue.toString()));
            default:
                return null;
        }
    }

    ASFieldType getDefaultType(JavaType javaType) {
        String qName = javaType.qName.toString();
        if (javaType.isArray) {
            if (qName.equals("B") || qName.equals("java.lang.Byte")) {
                return ASFieldType.BYTE_ARRAY;
            } else if (qName.equals("C") || qName.equals("java.lang.Character")) {
                return ASFieldType.STRING;
            } else {
                return ASFieldType.ARRAY;
            }
        } else {
            if (qName.equals("java.lang.String") || qName.equals("java.lang.Character") || qName.equals("C") || qName.equals("java.math.BigInteger") || qName.equals("java.math.BigDecimal")) {
                return ASFieldType.STRING;
            } else if (qName.equals("Z") || qName.equals("java.lang.Boolean")) {
                return ASFieldType.BOOLEAN;
            } else if (qName.equals("I") || qName.equals("java.lang.Integer") || qName.equals("B") || qName.equals("java.lang.Byte") || qName.equals("S") || qName.equals("java.lang.Short") || qName.equals("J") || qName.equals("java.lang.Long") || qName.equals("F") || qName.equals("java.lang.Float") || qName.equals("D") || qName.equals("java.lang.Double")) {
                return ASFieldType.NUMBER;
            } else if (qName.equals("java.util.GregorianCalendar") || qName.equals("java.util.Date")) {
                return ASFieldType.DATE;
            } else if (qName.equals("java.util.List") || qName.equals("java.util.Collection") || qName.equals("java.util.Stack") || qName.equals("java.util.PriorityQueue") || qName.equals("java.util.HashSet") || qName.equals("java.util.EnumSet") || qName.equals("java.util.TreeSet") || qName.equals("java.util.LinkedBlockingQueue") || qName.equals("java.util.LinkedHashSet") || qName.equals("java.util.CopyOnWriteArraySet") || qName.equals("java.util.ConcurrentLinkedQueue") || qName.equals("java.util.PriorityBlockingQueue") || qName.equals("java.util.SynchronousQueue") || qName.equals("java.util.LinkedList") || qName.equals("java.util.ArrayCollection") || qName.equals("java.util.Vector")) {
                return ASFieldType.ARRAY_COLLECTION;
            } else if (qName.equals("java.util.Map") || qName.equals("java.util.Hashtable") || qName.equals("java.util.Dictionary") || qName.equals("java.util.HashMap") || qName.equals("java.util.Properties") || qName.equals("java.util.Attributes") || qName.equals("java.util.IdentityHashMap") || qName.equals("java.util.LinkedHashMap") || qName.equals("java.util.TreeMap") || qName.equals("java.util.WeakHashMap") || qName.equals("java.util.EnumMap") || qName.equals("java.util.ConcurrentHashMap") || qName.equals("java.util.ConcurrentSkipListMap") || qName.equals("java.lang.Object")) {
                return ASFieldType.OBJECT;
            } else if (qName.equals("org.w3c.dom.Document")) {
                return ASFieldType.XML;
            } else if (qName.equals("java.io.Externalizable")) {
                return ASFieldType.IEXTERNALIZABLE;
            } else if (qName.contains("$")) {
                return ASFieldType.STRING;
            } else {
                return ASFieldType.CUSTOM;
            }
        }
    }

    Visibility getVisiblity(JavaVisibility visibility) {
        switch(visibility) {
            case PACKAGE:
                return Visibility.DEFAULT;
            case PRIVATE:
                return Visibility.PRIVATE;
            case PROTECTED:
                return Visibility.PROTECTED;
            case PUBLIC:
            default:
                return Visibility.PUBLIC;
        }
    }

    Visibility getVisiblity(ASVisiblity visibility) {
        switch(visibility) {
            case DEFAULT:
                return Visibility.DEFAULT;
            case PRIVATE:
                return Visibility.PRIVATE;
            case PROTECTED:
                return Visibility.PROTECTED;
            case PUBLIC:
            default:
                return Visibility.PUBLIC;
        }
    }

    void addImportIfNeeded(QualifiedTypeName qtn, ASPackage asPackage) {
        if ((qtn.packge == null) || (nullEquals(qtn.packge, asPackage.getName()))) return;
        List<String> imports = asPackage.findImports();
        String qualifiedName = qtn.toString();
        for (String imp : imports) {
            if (qualifiedName.equals(imp)) {
                return;
            }
        }
        asPackage.addImport(qualifiedName);
    }
}
