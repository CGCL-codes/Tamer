package com.googlecode.datawander.codegenerator;

import com.googlecode.datawander.i18n.I18n;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.String;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author Marcin Stachniuk
 */
public class SourceCodeGenerator2Relational {

    private static final Logger logger = Logger.getLogger(SourceCodeGenerator2Relational.class);

    /**
     * Header of source file
     */
    public static final String SOURCE_FILE_HEADER;

    /**
     * New line char. Use to generate new line in file.
     */
    public static final String NEW_LINE;

    /**
     * String with one ident in files with source code
     */
    private static final String INDENT = "    ";

    /**
     * String use to insert into querys before and after table and field names.
     */
    public static final String ESC_CHAR = "\\\"";

    static {
        NEW_LINE = System.getProperty("line.separator");
        SOURCE_FILE_HEADER = "/* This file was generated automatically by DataWander." + NEW_LINE + " * Author of DataWander: Marcin Stachniuk" + NEW_LINE + " * Project Page: http://code.google.com/p/datawander/" + NEW_LINE + " */" + NEW_LINE;
    }

    private RelationalDatabaseScriptGenerator relDBScriptGen;

    private Map<String, GeneratedDatabaseFields> generatetField;

    private Set<Class> loadedClassSet;

    private Map<String, GeneratedDatabaseFields> associativeTables;

    /**
     * Class names that are imported in generated source files
     */
    private String[] importClasses = new String[] { "com.db4o.ObjectSet", "com.db4o.ObjectContainer", "java.sql.SQLException", "java.sql.Connection", "java.sql.PreparedStatement" };

    public void generateSourceCode(RelationalDatabaseScriptGenerator relDBScriptGen, String pathName) throws CodeGenerationException {
        this.relDBScriptGen = relDBScriptGen;
        generatetField = relDBScriptGen.getGeneratetField();
        loadedClassSet = relDBScriptGen.getLoadedClassSet();
        associativeTables = relDBScriptGen.getAssociativeTables();
        File fileDir = new File(pathName);
        fileDir.mkdir();
        if (!fileDir.isDirectory()) {
            String message = I18n.getString("thispathisnotadirectiory");
            message = String.format(message, fileDir.getAbsolutePath());
            throw new CodeGenerationException(message);
        }
        cleanDirectory(fileDir.getAbsolutePath());
        for (Class cl : loadedClassSet) {
            generateSourceCode(cl, fileDir);
        }
        if (associativeTables != null) {
            for (Map.Entry<String, GeneratedDatabaseFields> entry : associativeTables.entrySet()) {
                generateSourceCodeForAssocClass(entry.getKey(), entry.getValue(), fileDir);
            }
        }
    }

    /**
     * Generate source code for Class cl to directory fileDir.
     * @param cl Class object, that will be source code generated.
     * @param fileDir Output directory.
     * @throws CodeGenerationException If it is problem with generate source
     * code.
     */
    private void generateSourceCode(Class cl, File fileDir) throws CodeGenerationException {
        logger.trace("generateSourceCode(" + cl.getName() + ", " + fileDir + ");");
        String className = cl.getName();
        String classDir = fileDir.getAbsolutePath();
        String classOutputDir = getClassOutputDirectory(className, classDir);
        generateSourceFile(cl, classOutputDir);
    }

    private void generateSourcePreparedStatementSaveCodeBlock(GeneratedDatabaseFields fields, PrintWriter out) {
        out.append(INDENT + INDENT + "PreparedStatement stmt = " + "conn.prepareStatement(sql);" + NEW_LINE);
        int argumentIndex = 1;
        for (ForeignKey fk : fields.getForeignKeys()) {
            out.append(INDENT + INDENT + "stmt.setLong(" + argumentIndex + ", " + fk.getName() + ");" + NEW_LINE);
            argumentIndex++;
        }
        if (fields.getRelationalColumns() != null && !fields.getRelationalColumns().isEmpty()) {
            if (fields.getRelationalColumns().size() == 2) {
                RelationalColumn relCol = fields.getRelationalColumns().get(0);
                RelationalColumn relColIndex = fields.getRelationalColumns().get(1);
                out.append(INDENT + INDENT + "if(" + relCol.getName() + " != null) {" + NEW_LINE);
                out.append(INDENT + INDENT + INDENT + "for (int i = 0; i < " + relCol.getName() + ".length; i++) {" + NEW_LINE);
                generateSourceJdbcSetPreparedStatementForRelationalColumn(relCol, argumentIndex, out);
                argumentIndex++;
                out.append(INDENT + INDENT + INDENT + relColIndex.getName() + " = i;" + NEW_LINE);
                generateSourceJdbcSetPreparedStatementForRelationalColumn(relColIndex, argumentIndex, out);
                out.append(INDENT + INDENT + INDENT + INDENT + "stmt.execute();" + NEW_LINE);
                out.append(INDENT + INDENT + INDENT + "}" + NEW_LINE);
                out.append(INDENT + INDENT + "}" + NEW_LINE);
            } else {
                logger.fatal("Shulud be 2 RelationalColumns: for array and index.");
            }
            out.append(INDENT + INDENT + "stmt.close();" + NEW_LINE);
        } else {
            out.append(INDENT + INDENT + "stmt.execute();" + NEW_LINE);
            out.append(INDENT + INDENT + "stmt.close();" + NEW_LINE);
        }
    }

    private void generateSourceSaveObjectsAfterPreparedStatement(Class cl, PrintWriter out) {
        Map<String, List<ClassToSaveAfter>> objectsToAfterSave = relDBScriptGen.getObjectsToAfterSave();
        if (objectsToAfterSave != null) {
            Set<String> keySet = objectsToAfterSave.keySet();
            for (String s : keySet) {
                logger.trace("key Set: " + s);
            }
            List<ClassToSaveAfter> objectToSave = objectsToAfterSave.get(cl.getName());
            if (objectToSave != null) {
                int classCounter = 1;
                for (ClassToSaveAfter objToSave : objectToSave) {
                    logger.trace("I foud fo save after: " + objToSave.getToSaveClassName());
                    out.append(INDENT + INDENT + objToSave.getToSaveClassName() + " obj" + classCounter + " = new " + objToSave.getToSaveClassName() + "();" + NEW_LINE);
                    out.append(INDENT + INDENT + "obj" + classCounter + ".set" + firstCharUp(objToSave.getForeginKeyFieldName()) + "(getIdFromDb4o());" + NEW_LINE);
                    out.append(INDENT + INDENT + "obj" + classCounter + ".set" + firstCharUp(objToSave.getSetMethodAtributeName()) + "(" + objToSave.getSetMethodAtributeName() + ");" + NEW_LINE);
                    out.append(INDENT + INDENT + "obj" + classCounter + ".saveRecord(conn);" + NEW_LINE + NEW_LINE);
                    classCounter++;
                }
            }
        } else {
            logger.trace("objectsToAfterSave null");
        }
    }

    private void generateSourceSetStatementForeginClass(ForeignKey fk, int numberOfMySet, int argumentCounter, PrintWriter out) {
        out.append(INDENT + INDENT + "com.db4o.ObjectSet<" + fk.getReferencedTableName() + "> mySet" + numberOfMySet + " = " + fk.getReferencedClass().getName() + "." + relDBScriptGen.limitNameForMethodToGetReferencedObjectsFromDb4o(fk) + "(this);" + NEW_LINE);
        out.append(INDENT + INDENT + "if(mySet" + numberOfMySet + ".size() != 0) {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "if(mySet" + numberOfMySet + ".size() == 1) {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + INDENT + fk.getReferencedTableName() + " temp = mySet" + numberOfMySet + ".get(0);" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + INDENT + "stmt.set" + firstCharUp(fk.getObjectType()) + "(" + argumentCounter + ", temp.getIdFromDb4o());" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "} else {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + INDENT + "System.out.println(\"Error - should found one reference object\");" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + INDENT + "} else {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "stmt.setNull(" + argumentCounter + ", java.sql.Types.NUMERIC);" + NEW_LINE);
        out.append(INDENT + INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    private void generateSourceSqlSaveQueyStringCodeBlock(String packageName, String className, GeneratedDatabaseFields fields, PrintWriter out) {
        out.append(INDENT + INDENT + "String sql = \"insert into \\\"" + (packageName != null ? packageName + "." : "") + className + "\\\" (\" +" + NEW_LINE);
        int i = 0;
        for (ForeignKey fk : fields.getForeignKeys()) {
            out.append(INDENT + INDENT + INDENT + INDENT + "\"\\\"" + fk.getName() + "\\\"");
            i++;
            if (i == fields.getForeignKeys().size()) {
                if (fields.getRelationalColumns() != null && !fields.getRelationalColumns().isEmpty()) {
                    out.append(", \" + " + NEW_LINE);
                } else {
                    out.append("\" + " + NEW_LINE);
                }
            } else {
                out.append(",\" + " + NEW_LINE);
            }
        }
        i = 0;
        for (RelationalColumn relCol : fields.getRelationalColumns()) {
            out.append(INDENT + INDENT + INDENT + INDENT + "\"\\\"" + relCol.getName() + "\\\"");
            i++;
            if (i == fields.getRelationalColumns().size()) {
                out.append("\" + " + NEW_LINE);
            } else {
                out.append(",\" + " + NEW_LINE);
            }
        }
        int numberOfQuestionSymbols = fields.getForeignKeys().size();
        if (fields.getRelationalColumns() != null && fields.getRelationalColumns().size() != 0) {
            numberOfQuestionSymbols = numberOfQuestionSymbols + fields.getRelationalColumns().size();
        }
        out.append(INDENT + INDENT + INDENT + INDENT + "\") values (");
        for (i = 0; i < numberOfQuestionSymbols; i++) {
            out.append("?");
            if (i != numberOfQuestionSymbols - 1) {
                out.append(", ");
            }
        }
        out.append(")\";" + NEW_LINE);
    }

    /**
     * Contact to outputDir a folders form package name from classname.
     * @param className Full name of class (with packages).
     * @param outputDir Absolute Path to place where source code sould be generated.
     * @return String that reprezents full path where source file sould be
     * created (with created folders contain packages folders).
     */
    private String getClassOutputDirectory(String className, String outputDir) {
        String classOutputDir = outputDir + (outputDir.endsWith(File.separator) ? "" : File.separator);
        if (className.contains(".")) {
            String[] parts = className.split("\\.");
            for (int i = 0; i < parts.length - 1; i++) {
                classOutputDir = classOutputDir + parts[i] + File.separator;
                File dir = new File(classOutputDir);
                if (!dir.exists()) {
                    dir.mkdir();
                }
            }
        }
        return classOutputDir;
    }

    /**
     * Generate one source file for cl class in classDir.
     * @param cl Class object, that will be source code generated.
     * @param classDir Directory where pile sould be created. directory MUST
     * contain package name of generated class.
     * @throws CodeGenerationException If it is problem with generate source
     * code.
     */
    private void generateSourceFile(Class cl, String classDir) throws CodeGenerationException {
        String classNameLastPart = cl.getName();
        String packageName = null;
        if (classNameLastPart.contains(".")) {
            packageName = classNameLastPart.substring(0, classNameLastPart.lastIndexOf("."));
            classNameLastPart = classNameLastPart.substring(classNameLastPart.lastIndexOf(".") + 1, classNameLastPart.length());
        }
        logger.debug("clasDir: " + classDir + " last part: " + classNameLastPart);
        try {
            PrintWriter out = new PrintWriter(new File(classDir + File.separator + classNameLastPart + ".java"));
            out.append(SOURCE_FILE_HEADER);
            if (packageName != null) {
                out.append("package " + packageName + ";" + NEW_LINE + NEW_LINE);
            }
            generateImports(out);
            out.append("public class " + classNameLastPart + " ");
            Class superClass = cl.getSuperclass();
            if (superClass != null && !superClass.getName().equals("java.lang.Object")) {
                out.append("extends " + superClass.getName());
            }
            out.append(" {" + NEW_LINE);
            generateFields(cl, out);
            generateLoadAllObjects(cl, out);
            generateSaveAllRecords(cl, out);
            generateSourceSaveRecord(cl, out);
            generateGettersAndSetters(cl, out);
            generateGetIdFromDb4o(out);
            generateMethodsToGetReferencedObjectsFromDb4o(cl, out);
            generateSaveAsociacionClassMethods(cl, out);
            out.append("}" + NEW_LINE);
            out.close();
        } catch (FileNotFoundException ex) {
            logger.error(ex, ex);
            throw new CodeGenerationException(I18n.getString("codegenerationexceptionproblem"));
        }
    }

    private void generateFields(Class cl, PrintWriter out) {
        for (Field field : cl.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod)) {
                String modif = Modifier.toString(mod);
                out.append(INDENT + modif + " " + getRealTypeFromField(field) + " " + field.getName() + ";" + NEW_LINE);
            }
        }
        out.append(NEW_LINE + NEW_LINE);
    }

    private String getRealTypeFromField(Field field) {
        String genTypeName = null;
        String realType = field.getType().getName();
        try {
            genTypeName = relDBScriptGen.getGenericTypeFromType(field);
            realType = realType + "<" + genTypeName + ">";
        } catch (StringIndexOutOfBoundsException e) {
        }
        if (field.getType().isArray()) {
            String arrayType = relDBScriptGen.getArrayTypeFromField(field);
            realType = arrayType + " []";
        }
        return realType;
    }

    /**
     * Delete contents of directory.
     * @param directory Directory to clean
     */
    private void cleanDirectory(String directory) {
        logger.trace("cleanDirectory(" + directory + ")");
        String cleanMessage = I18n.getString("cleandirectory");
        cleanMessage = String.format(cleanMessage, directory);
        logger.info(cleanMessage);
        deleteDir(new File(directory));
    }

    /**
     * Delete cascade directory. Method delete non-empty directory's
     * @param dir
     */
    private void deleteDir(File dir) {
        logger.trace("deleteDir(" + dir.getAbsolutePath() + ")");
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                deleteDir(f);
                f.delete();
            } else {
                f.delete();
            }
        }
    }

    private void generateGettersAndSetters(Class cl, PrintWriter out) {
        for (Field field : cl.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                generateGettersAndSetters(field, out);
            }
        }
    }

    private void generateGettersAndSetters(Field field, PrintWriter out) {
        String realType = getRealTypeFromField(field);
        out.append(INDENT + "public " + realType + " get" + firstCharUp(field.getName()) + "() {" + NEW_LINE);
        out.append(INDENT + INDENT + "return " + field.getName() + ";" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE);
        out.append(INDENT + "public void set" + firstCharUp(field.getName()) + "(" + realType + " " + field.getName() + ") {" + NEW_LINE);
        out.append(INDENT + INDENT + "this." + field.getName() + " = " + field.getName() + ";" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    private String firstCharUp(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void generateLoadAllObjects(Class cl, PrintWriter out) {
        out.append(INDENT + "public static ObjectSet " + "loadAllObjects() {" + NEW_LINE);
        out.append(INDENT + INDENT + "ObjectContainer db = Db4oUtil.getObjectContainer();" + NEW_LINE);
        out.append(INDENT + INDENT + "return db.query(" + cl.getName() + ".class);" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    private void generateImports(PrintWriter out) {
        for (String s : importClasses) {
            out.append("import " + s + ";" + NEW_LINE);
        }
        out.append(NEW_LINE);
    }

    /**
     * Method use to generate get id form db4o method. getIdFromDb4o() method is
     * used to get unique id from db4o and use this id as Primary Key in
     * relational database.
     * @param out Output stream.
     */
    private void generateGetIdFromDb4o(PrintWriter out) {
        out.append(INDENT + "public long getIdFromDb4o() {" + NEW_LINE);
        out.append(INDENT + INDENT + "ObjectContainer db = Db4oUtil.getObjectContainer();" + NEW_LINE);
        out.append(INDENT + INDENT + "long id = db.ext().getID(this);" + NEW_LINE);
        out.append(INDENT + INDENT + "return id;" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    private void generateSaveAllRecords(Class cl, PrintWriter out) {
        out.append(INDENT + "public static void saveAllRecords(java.util.List<" + cl.getName() + "> objects) throws Exception {" + NEW_LINE);
        out.append(INDENT + INDENT + "System.out.println(\"saveAllRecords " + cl.getName() + "\");" + NEW_LINE);
        out.append(INDENT + INDENT + "if(objects != null) {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "Connection conn = OracleConnector.getConnection();" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "for(" + cl.getName() + " o : " + "objects) {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + INDENT + "o.saveRecord(conn);" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    private void generateSourceSaveRecord(Class cl, PrintWriter out) {
        out.append(INDENT + "private void saveRecord(Connection conn) " + "throws SQLException {" + NEW_LINE);
        if (!relDBScriptGen.hasClassDeclaredFields(cl)) {
            out.append(INDENT + "// No save this object in database. " + "This class have no declared fields." + NEW_LINE);
            out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
            return;
        }
        int numberOfFields = generateSourceSQLCommandToSaveRecord(cl, out);
        int numberOfGeneratedInDBFields = relDBScriptGen.calculateNumberOfGeneratedFields(cl.getDeclaredFields());
        out.append(INDENT + INDENT + "PreparedStatement stmt = conn." + "prepareStatement(sql);" + NEW_LINE);
        out.append(INDENT + INDENT + "stmt.setLong(1, getIdFromDb4o());" + NEW_LINE);
        int argumentCounter = 2;
        GeneratedDatabaseFields genField = generatetField.get(cl.getName());
        if (genField != null) {
            int numberOfMySet = 1;
            for (ForeignKey fk : genField.getForeignKeys()) {
                if (fk.getName().startsWith(RelationalDatabaseScriptGenerator.INHERITANCE_KEY_PREFIX)) {
                    out.append(INDENT + INDENT + "stmt.set" + firstCharUp(fk.getObjectType()) + "(" + argumentCounter + ", " + "super.getIdFromDb4o());" + NEW_LINE);
                } else {
                    generateSourceSetStatementForeginClass(fk, numberOfMySet, argumentCounter, out);
                    numberOfMySet++;
                }
                argumentCounter++;
            }
        }
        if (numberOfGeneratedInDBFields != 0) {
            int i = 0;
            for (Field f : cl.getDeclaredFields()) {
                if (relDBScriptGen.isGeneratedColumnForField(f)) {
                    i++;
                    generateSourceJdbcSetPreparedStatement(f, argumentCounter, out);
                    argumentCounter++;
                }
            }
        }
        out.append(INDENT + INDENT + "stmt.execute();" + NEW_LINE);
        out.append(INDENT + INDENT + "stmt.close();" + NEW_LINE + NEW_LINE);
        generateSourceSaveObjectsAfterPreparedStatement(cl, out);
        generateSourceSaveAcociacivesClassesMethodInvokes(cl, out);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    private int generateSourceSQLCommandToSaveRecord(Class cl, PrintWriter out) {
        int numberOfFields = 0;
        out.append(INDENT + INDENT + "String sql = \"insert into \\\"" + relDBScriptGen.limitNameForMainClasses(cl.getName()) + "\\\" (\\\"" + RelationalDatabaseScriptGenerator.PRIMARY_KEY_NAME + "\\\", \" +" + NEW_LINE);
        numberOfFields++;
        int numberOfGeneratedInDBFields = relDBScriptGen.calculateNumberOfGeneratedFields(cl.getDeclaredFields());
        GeneratedDatabaseFields genField = generatetField.get(cl.getName());
        if (genField != null) {
            List<ForeignKey> foreignKeys = genField.getForeignKeys();
            Iterator<ForeignKey> iter = foreignKeys.iterator();
            for (int i = 0; i < foreignKeys.size(); i++) {
                ForeignKey fk = iter.next();
                numberOfFields++;
                out.append(INDENT + INDENT + INDENT + INDENT + "\"\\\"" + fk.getName() + "\\\"");
                if (numberOfGeneratedInDBFields == 0 && i == foreignKeys.size() - 1) {
                    out.append(")");
                } else {
                    out.append(", \" +" + NEW_LINE);
                }
            }
        }
        logger.trace("numberOfGeneratedInDBFields: " + numberOfGeneratedInDBFields + " in " + cl.getName());
        if (numberOfGeneratedInDBFields != 0) {
            int i = 0;
            for (Field f : cl.getDeclaredFields()) {
                if (relDBScriptGen.isGeneratedColumnForField(f)) {
                    i++;
                    numberOfFields++;
                    out.append(INDENT + INDENT + INDENT + INDENT + "\"\\\"" + f.getName() + "\\\"");
                    if (i != numberOfGeneratedInDBFields) {
                        out.append(", \" + " + NEW_LINE);
                    } else {
                        out.append(")");
                    }
                }
            }
        }
        out.append(" values (");
        for (int j = 0; j < numberOfFields; j++) {
            out.append("?");
            if (j != numberOfFields - 1) {
                out.append(", ");
            }
        }
        out.append(")\";" + NEW_LINE);
        return numberOfFields;
    }

    public void unloadOldClasses() {
        associativeTables = null;
        generatetField = null;
        loadedClassSet = null;
        relDBScriptGen = null;
    }

    /**
     * Return list of all generated classes names
     * @return
     */
    public Set<String> getAllToReloadClass() {
        Set<String> allToReloadClass = new HashSet<String>();
        for (Class cl : loadedClassSet) {
            allToReloadClass.add(cl.getName());
        }
        if (associativeTables != null) {
            for (String name : associativeTables.keySet()) {
                allToReloadClass.add(name);
            }
        }
        return allToReloadClass;
    }

    public void addImportClassName(String className) {
        if (className != null) {
            if (importClasses == null) {
                importClasses = new String[] { className };
            } else {
                importClasses = Arrays.copyOf(importClasses, importClasses.length + 1);
                importClasses[importClasses.length - 1] = className;
            }
        }
    }

    /**
     * Generate source code for associatives classes.
     * @param className String that reprezents generate class name.
     * @param fields Object of class GeneratedDatabaseFields that reprezents
     * fields existed in class.
     * @param fileDir Directory where source code sould be created.
     * @exception CodeGenerationException If it is a problem with geenrate source
     * code.
     */
    private void generateSourceCodeForAssocClass(String className, GeneratedDatabaseFields fields, File fileDir) throws CodeGenerationException {
        logger.trace("generateSourceCodeForAssocClass(" + className + ", fields, " + fileDir.getAbsolutePath());
        String classDir = fileDir.getAbsolutePath();
        String classOutputDir = getClassOutputDirectory(className, classDir);
        generateSourceFileForAssocClass(className, fields, classOutputDir);
    }

    private void generateSourceFileForAssocClass(String className, GeneratedDatabaseFields fields, String classOutputDir) throws CodeGenerationException {
        String classNameLastPart = className;
        String packageName = null;
        if (classNameLastPart.contains(".")) {
            packageName = classNameLastPart.substring(0, classNameLastPart.lastIndexOf("."));
            classNameLastPart = classNameLastPart.substring(classNameLastPart.lastIndexOf(".") + 1, classNameLastPart.length());
        }
        logger.debug("clasDir: " + classOutputDir + " last part: " + classNameLastPart);
        try {
            PrintWriter out = new PrintWriter(new File(classOutputDir + File.separator + classNameLastPart + ".java"));
            out.append(SOURCE_FILE_HEADER);
            if (packageName != null) {
                out.append("package " + packageName + ";" + NEW_LINE + NEW_LINE);
            }
            generateImports(out);
            out.append("public class " + classNameLastPart + " {" + NEW_LINE + NEW_LINE);
            generateFields(fields, out);
            generateGettersAndSettersForFields(fields, out);
            generateLoadAllObjectEmptyMethod(out);
            generateSaveAllRecords(classNameLastPart, fields, out);
            generateSourceSaveRecord(packageName, classNameLastPart, fields, out);
            out.append(INDENT + "//TODO: Dopisac generowanie kodu aaa" + NEW_LINE + NEW_LINE);
            out.append("}" + NEW_LINE);
            out.close();
        } catch (FileNotFoundException ex) {
            logger.error(ex, ex);
            throw new CodeGenerationException(I18n.getString("codegenerationexceptionproblem"));
        }
    }

    private void generateFields(GeneratedDatabaseFields fields, PrintWriter out) {
        List<ForeignKey> foreginKeys = fields.getForeignKeys();
        for (ForeignKey fk : foreginKeys) {
            out.append(INDENT + "private " + fk.getObjectType() + " " + fk.getName() + ";" + NEW_LINE);
        }
        out.append(NEW_LINE);
        List<RelationalColumn> relatinalColumns = fields.getRelationalColumns();
        if (relatinalColumns != null) {
            for (RelationalColumn relCol : relatinalColumns) {
                out.append(INDENT + "private " + relCol.getObjectType() + " " + relCol.getName() + ";" + NEW_LINE);
            }
        }
        out.append(NEW_LINE);
    }

    private void generateGettersAndSettersForFields(GeneratedDatabaseFields fields, PrintWriter out) {
        for (ForeignKey fk : fields.getForeignKeys()) {
            out.append(INDENT + "public " + fk.getObjectType() + " get" + firstCharUp(fk.getName()) + "() {" + NEW_LINE);
            out.append(INDENT + INDENT + "return " + fk.getName() + ";" + NEW_LINE);
            out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
            out.append(INDENT + "public void set" + firstCharUp(fk.getName()) + "(" + fk.getObjectType() + " " + fk.getName() + ") {" + NEW_LINE);
            out.append(INDENT + INDENT + "this." + fk.getName() + " = " + fk.getName() + ";" + NEW_LINE);
            out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
        }
        for (RelationalColumn relCol : fields.getRelationalColumns()) {
            out.append(INDENT + "public " + relCol.getObjectType() + " get" + firstCharUp(relCol.getName()) + "() {" + NEW_LINE);
            out.append(INDENT + INDENT + "return " + relCol.getName() + ";" + NEW_LINE);
            out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
            out.append(INDENT + "public void set" + firstCharUp(relCol.getName()) + "(" + relCol.getObjectType() + " " + relCol.getName() + ") {" + NEW_LINE);
            out.append(INDENT + INDENT + "this." + relCol.getName() + " = " + relCol.getName() + ";" + NEW_LINE);
            out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
        }
    }

    /**
     * Generate method loadAllObject. Generated method is empty (do nothing).
     * This metod is generated for asociaces class, becouse that class have not
     * object stored in db4o.
     * @param out Output Stream.
     */
    private void generateLoadAllObjectEmptyMethod(PrintWriter out) {
        out.append(INDENT + "public static ObjectSet loadAllObjects() {" + NEW_LINE);
        out.append(INDENT + INDENT + "return null;" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    /**
     * Generate static method to save all objects in relational database.
     * @param className Name of class where method is generated.
     * @param fields Fields needed to save object in database.
     * @param out Output Stream.
     */
    private void generateSaveAllRecords(String className, GeneratedDatabaseFields fields, PrintWriter out) {
        out.append(INDENT + "public static void saveAllRecords(java.util.List " + "objects) throws Exception {" + NEW_LINE);
        out.append(INDENT + INDENT + "System.out.println(\"saveAllRecords " + className + "\");" + NEW_LINE);
        out.append(INDENT + INDENT + "if(objects != null) {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "Connection conn = " + "OracleConnector.getConnection();" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "for(Object" + " o : objects) {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + INDENT + "((" + className + ")o).saveRecord(conn);" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    /**
     * Generate method to save one record in relational database.
     * @param packageName Name of package class where code is generated.
     * @param className Name of class where code is generated.
     * @param fields Fields needed to save object in database.
     * @param out Output Stream.
     */
    private void generateSourceSaveRecord(String packageName, String className, GeneratedDatabaseFields fields, PrintWriter out) {
        logger.trace("generateSourceSaveRecord(" + packageName + ", " + className + ", fields, out);");
        out.append(INDENT + "public void saveRecord(java.sql.Connection conn) " + "throws SQLException {" + NEW_LINE);
        generateSourceSqlSaveQueyStringCodeBlock(packageName, className, fields, out);
        generateSourcePreparedStatementSaveCodeBlock(fields, out);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    /**
     * Generate method that set argument of PreparedStatement object.
     * E.g. For field: String myString and argumentCounter = 1, to out will
     * append text: <br> stmt.setString(1, myString);
     * @param field Field object for that is genered invode method. Field have
     * to be a primitive type.
     * @param argumentCounter Number of argument in whole query.
     * @param out Output stream.
     */
    private void generateSourceJdbcSetPreparedStatement(Field field, int argumentCounter, PrintWriter out) {
        Class type = field.getType();
        String fieldTypeName = field.getType().getName();
        if (type.isPrimitive()) {
            if (type.getName().equals("char")) {
                out.append(INDENT + INDENT + "stmt.setString");
                out.append("(" + argumentCounter + ", \"\" + " + field.getName() + ");" + NEW_LINE);
            } else {
                out.append(INDENT + INDENT + "stmt.set" + firstCharUp(type.getName()));
                out.append("(" + argumentCounter + ", " + field.getName() + ");" + NEW_LINE);
            }
        } else if (relDBScriptGen.isFieldPrimitiveWrapper(field.getType().getName())) {
            out.append(INDENT + INDENT + "stmt.set");
            if (fieldTypeName.equals("java.lang.Character")) {
                out.append("String(" + argumentCounter + ", \"\" + " + field.getName() + ");" + NEW_LINE);
            } else if (fieldTypeName.equals("java.lang.Integer")) {
                out.append("Int(" + argumentCounter + ", " + field.getName() + ");" + NEW_LINE);
            } else {
                out.append(fieldTypeName.substring(fieldTypeName.lastIndexOf(".") + 1, fieldTypeName.length()) + "(" + argumentCounter + ", " + field.getName() + ");" + NEW_LINE);
            }
        } else if (field.getType().getName().equals("java.math.BigDecimal")) {
            out.append(INDENT + INDENT + "stmt.setBigDecimal(" + argumentCounter + ", " + field.getName() + ");" + NEW_LINE);
        } else if (field.getType().getName().equals("java.sql.Date")) {
            out.append(INDENT + INDENT + "stmt.setDate(" + argumentCounter + ", " + field.getName() + ");" + NEW_LINE);
        } else if (field.getType().getName().equals("java.util.Date")) {
            out.append(INDENT + INDENT + "stmt.setDate(" + argumentCounter + ", new java.sql.Date(" + field.getName() + ".getTime()));" + NEW_LINE);
        } else if (field.getType().getName().equals("java.lang.String")) {
            out.append(INDENT + INDENT + "stmt.setString(" + argumentCounter + ", " + field.getName() + ");" + NEW_LINE);
        } else if (field.getType().getName().equals("java.sql.Timestamp")) {
            out.append(INDENT + INDENT + "stmt.setTimestamp(" + argumentCounter + ", " + field.getName() + ");" + NEW_LINE);
        } else if (field.getType().getName().equals("oracle.sql.TIMESTAMP")) {
            out.append(INDENT + INDENT + "//TODO: Do something for: oracle.sql.TIMESTAMP" + NEW_LINE);
        } else {
            out.append(INDENT + INDENT + "//No suported type: " + field.getType().getName() + NEW_LINE);
        }
    }

    /**
     * Generate method that set argument of PreparedStatement object. This
     * method is for tables of primitive types.
     * E.g. For relColumn byte[] myByte and argumentCounter = 1, to out will
     * append text: <br> stmt.setByte(1, myByte[i]);
     * @param relColumn RelationalColumn object for that is generated method.
     * @param argumentCounter Number of argument in whole query.
     * @param out Output stream.
     */
    private void generateSourceJdbcSetPreparedStatementForRelationalColumn(RelationalColumn relColumn, int argumentCounter, PrintWriter out) {
        logger.trace("generateSourceJdbcSetPreparedStatementForTab(" + relColumn.getName() + " : " + relColumn.getObjectType() + ", " + argumentCounter + ", out);");
        String objectType = relColumn.getObjectType();
        boolean isArray = isObjectTypeNameArray(objectType);
        if (isArray) {
            objectType = objectType.substring(0, objectType.indexOf("[")).trim();
        }
        if (relDBScriptGen.isPrimitiveType(objectType)) {
            if (objectType.equals("char")) {
                out.append(INDENT + INDENT + INDENT + "stmt.setString");
                out.append("(" + argumentCounter + ", \"\" + " + relColumn.getName() + (isArray ? "[i]" : "") + ");" + NEW_LINE);
            } else {
                out.append(INDENT + INDENT + INDENT + "stmt.set" + firstCharUp(objectType));
                out.append("(" + argumentCounter + ", " + relColumn.getName() + (isArray ? "[i]" : "") + ");" + NEW_LINE);
            }
        } else if (relDBScriptGen.isFieldPrimitiveWrapper(objectType)) {
            if (objectType.equals("java.lang.Character")) {
                out.append(INDENT + INDENT + INDENT + INDENT + "stmt.setString(" + argumentCounter + ", \"\" + " + relColumn.getName() + (isArray ? "[i]" : "") + ");" + NEW_LINE);
            } else if (objectType.equals("java.lang.Integer")) {
                out.append(INDENT + INDENT + INDENT + INDENT + "stmt.setInt(" + argumentCounter + ", " + relColumn.getName() + (isArray ? "[i]" : "") + ");" + NEW_LINE);
            } else {
                out.append(INDENT + INDENT + INDENT + INDENT + "stmt.set" + objectType.substring(objectType.lastIndexOf(".") + 1, objectType.length()) + "(" + argumentCounter + ", " + relColumn.getName() + (isArray ? "[i]" : "") + ");" + NEW_LINE);
            }
        } else if (objectType.equals("java.math.BigDecimal")) {
            out.append(INDENT + INDENT + INDENT + INDENT + "stmt.setBigDecimal(" + argumentCounter + ", " + relColumn.getName() + (isArray ? "[i]" : "") + ");" + NEW_LINE);
        } else if (objectType.equals("java.sql.Date")) {
            out.append(INDENT + INDENT + INDENT + INDENT + "stmt.setDate(" + argumentCounter + ", " + relColumn.getName() + (isArray ? "[i]" : "") + ");" + NEW_LINE);
        } else if (objectType.equals("java.util.Date")) {
            out.append(INDENT + INDENT + INDENT + INDENT + "stmt.setDate(" + argumentCounter + ", new java.sql.Date(" + relColumn.getName() + (isArray ? "[i]" : "") + ".getTime()));" + NEW_LINE);
        } else if (objectType.equals("java.sql.Timestamp")) {
            out.append(INDENT + INDENT + "stmt.setTimestamp(" + argumentCounter + ", " + relColumn.getName() + (isArray ? "[i]" : "") + ");" + NEW_LINE);
        } else if (objectType.equals("oracle.sql.TIMESTAMP")) {
            out.append(INDENT + INDENT + "//TODO: Do something for: oracle.sql.TIMESTAMP" + NEW_LINE);
        } else {
            out.append(INDENT + INDENT + INDENT + INDENT + "//No suported tab type: " + objectType + NEW_LINE);
        }
    }

    /**
     * Method to generate methods to get objects referenced to class from db4o.
     * @param cl Class where code is generated.
     * @param out Output stream.
     */
    private void generateMethodsToGetReferencedObjectsFromDb4o(Class cl, PrintWriter out) throws CodeGenerationException {
        logger.trace("generateMethodsToGetReferencedObjectsFromDb4o(" + cl.getName() + ", out);");
        List<ForeignKey> referencedForeginKeys = findForeginFieldsThatReferencedToClass(cl);
        if (!referencedForeginKeys.isEmpty()) {
            for (ForeignKey fk : referencedForeginKeys) {
                if (!fk.getName().startsWith(RelationalDatabaseScriptGenerator.INHERITANCE_KEY_PREFIX)) {
                    logger.trace("found foreging keys: " + fk.getName() + " objType: " + fk.getObjectType() + " owner table: " + fk.getOwnerTable() + " refTabName: " + fk.getReferencedTableName() + " refTablPK: " + fk.getReferencedTablePrimaryKey() + " relType: " + fk.getRelationalType() + " referenced Class: " + fk.getReferencedClass().getName() + " referenced Object Field:" + fk.getReferencedObjectFieldName() + " unique: " + fk.isUnique());
                    generateMethodToGetObjectsFromDb4o(cl, fk, out);
                }
            }
        }
    }

    /**
     * Find ForeginKey's in generatetField Map where ReferencedTableName in
     * ForeginKey is set to cl.getName(). Method find ForeginKey's that are
     * referenced fo class cl.
     * @param cl Class object that we find ForeginKey's, that referenced to this
     * class.
     * @return List of ForeginKey's that ReferencedTableName is set ot cl object.
     */
    private List<ForeignKey> findForeginFieldsThatReferencedToClass(Class cl) {
        List<ForeignKey> result = new ArrayList<ForeignKey>();
        if (generatetField != null) {
            Collection<GeneratedDatabaseFields> coll = generatetField.values();
            if (coll != null) {
                for (GeneratedDatabaseFields fields : coll) {
                    for (ForeignKey fk : fields.getForeignKeys()) {
                        if (fk.getReferencedClass() != null && fk.getReferencedClass().equals(cl)) {
                            result.add(fk);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Generate method to get referenced object from db4o database. If class1 has
     * reference to class2 (relation 1:1 or 1:N) then in class1 is generated
     * method to get objects type class2, referenced to class1.
     * @param cl Class where is method generate.
     * @param fk ForeginKey from second class that have information about
     * referential integrity.
     * @param out Output stream.
     */
    private void generateMethodToGetObjectsFromDb4o(Class cl, ForeignKey fk, PrintWriter out) throws CodeGenerationException {
        logger.trace("generateMethodToGetObjectsFromDb4o(" + cl.getName() + ", " + fk.getName() + ", out);");
        out.append(INDENT + "public static ObjectSet<" + fk.getReferencedTableName() + "> " + relDBScriptGen.limitNameForMethodToGetReferencedObjectsFromDb4o(fk) + "(final " + fk.getOwnerTable() + " obj) {" + NEW_LINE);
        out.append(INDENT + INDENT + "ObjectContainer db = Db4oUtil.getObjectContainer();" + NEW_LINE);
        out.append(INDENT + INDENT + "ObjectSet<" + fk.getReferencedTableName() + "> result = db.query(new com.db4o.query.Predicate<" + fk.getReferencedTableName() + ">() {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "public boolean match(" + fk.getReferencedTableName() + " candidate) {" + NEW_LINE);
        if (fk.getOwnerTable().equals(fk.getReferencedClass().getName())) {
            out.append(INDENT + INDENT + INDENT + INDENT + "if(obj." + fk.getReferencedObjectFieldName() + " != null) {" + NEW_LINE);
        } else {
            out.append(INDENT + INDENT + INDENT + INDENT + "if(candidate." + fk.getReferencedObjectFieldName() + " != null) {" + NEW_LINE);
        }
        generateConditionReturnResultInMethodToGetObjectsFromDb4o(cl, fk, out);
        out.append(INDENT + INDENT + INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + INDENT + "return false;" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + INDENT + "});" + NEW_LINE);
        out.append(INDENT + INDENT + "return result;" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE + NEW_LINE);
    }

    /**
     * Method to generate return results condition in math method from Predicate
     * interface. This method is used to generate return value condition in
     * method generated by generateMethodToGetObjectsFromDb4o() method.
     * @param cl Class where is generated method.
     * @param fk ForeginKey from second class that have information about
     * referential integrity.
     * @param out Output stream.
     */
    private void generateConditionReturnResultInMethodToGetObjectsFromDb4o(Class cl, ForeignKey fk, PrintWriter out) throws CodeGenerationException {
        logger.trace("generateConditionReturnResultInMethodToGetObjectsFromDb4o(" + cl.getName() + ", " + fk.getName() + ", out);");
        Field refField = null;
        Class referencedClass = fk.getReferencedClass();
        try {
            refField = referencedClass.getDeclaredField(fk.getReferencedObjectFieldName());
        } catch (Exception ex) {
            logger.fatal("Problem with get field from class " + referencedClass.getName(), ex);
            out.append(INDENT + INDENT + INDENT + INDENT + INDENT + "// Problem with get field from class " + referencedClass.getName() + NEW_LINE);
            throw new CodeGenerationException("Problem with get field from class " + referencedClass.getName());
        }
        if (fk.getOwnerTable().equals(fk.getReferencedClass().getName())) {
            if (relDBScriptGen.isFieldCollection(refField)) {
                out.append(INDENT + INDENT + INDENT + INDENT + INDENT + "return obj." + fk.getReferencedObjectFieldName() + ".contains(candidate);" + NEW_LINE);
            } else if (refField.getType().isArray()) {
                out.append(INDENT + INDENT + INDENT + INDENT + INDENT + "return java.util.Arrays.asList(obj." + fk.getReferencedObjectFieldName() + ").contains(candidate);" + NEW_LINE);
            } else {
                out.append(INDENT + INDENT + INDENT + INDENT + INDENT + "return obj." + fk.getReferencedObjectFieldName() + ".equals(candidate);" + NEW_LINE);
            }
        } else {
            if (relDBScriptGen.isFieldCollection(refField)) {
                out.append(INDENT + INDENT + INDENT + INDENT + INDENT + "return candidate." + fk.getReferencedObjectFieldName() + ".contains(obj);" + NEW_LINE);
            } else if (refField.getType().isArray()) {
                out.append(INDENT + INDENT + INDENT + INDENT + INDENT + "return java.util.Arrays.asList(candidate." + fk.getReferencedObjectFieldName() + ").contains(obj);" + NEW_LINE);
            } else {
                out.append(INDENT + INDENT + INDENT + INDENT + INDENT + "return candidate." + fk.getReferencedObjectFieldName() + ".equals(obj);" + NEW_LINE);
            }
        }
    }

    /**
     * Method return asociacions save methods names that sould be invoked after
     * normal save in cl class.
     * @param cl Class object for that are generated names of methods.
     * @param out Output stream.
     * @return List of String - names of methods to invoke after normal save
     * object for cl class.
     */
    private List<String> getSaveAsociacionClassMethodsNames(Class cl) {
        logger.trace("getSaveAsociacionClassMethodsNames(" + cl.getName() + ", out);");
        List<String> result = new ArrayList<String>();
        Set<Entry<String, GeneratedDatabaseFields>> entrys = associativeTables.entrySet();
        for (Entry<String, GeneratedDatabaseFields> entry : entrys) {
            if (entry.getKey().startsWith(RelationalDatabaseScriptGenerator.ASOCIACION_TABLE_PREFIX)) {
                GeneratedDatabaseFields value = entry.getValue();
                if (value.getForeignKeys().get(0).getReferencedTableName().equals(cl.getName())) {
                    String str = limitNameForSaveAsociacionClassMethod(entry.getKey());
                    result.add(str);
                }
            }
        }
        return result;
    }

    /**
     * Method generate save asociacions class methods. 
     * @param cl Class object where is methods generated.
     * @param out Outpu Stream.
     */
    private void generateSaveAsociacionClassMethods(Class cl, PrintWriter out) {
        logger.trace("generateSaveAsociacionClassMethod(" + cl.getName() + ", out);");
        Set<Entry<String, GeneratedDatabaseFields>> entrys = associativeTables.entrySet();
        for (Entry<String, GeneratedDatabaseFields> entry : entrys) {
            if (entry.getKey().startsWith(RelationalDatabaseScriptGenerator.ASOCIACION_TABLE_PREFIX)) {
                GeneratedDatabaseFields value = entry.getValue();
                if (value.getForeignKeys().get(0).getReferencedTableName().equals(cl.getName())) {
                    generateSaveAsociacionClassMethod(entry.getKey(), value.getForeignKeys().get(0), value.getForeignKeys().get(1), out);
                }
            }
        }
    }

    /**
     * Generate one method to save asociacions class method. 
     * @param asocClassName Name of asociacion class.
     * @param firstForeginKey First ForeginKey in asocClassName. This ForeginKey
     * should be referenced to class where code is generated.
     * @param secondForeginKey Second ForeginKey in asocClassName.
     * @param out Output Stream.
     */
    private void generateSaveAsociacionClassMethod(String asocClassName, ForeignKey firstForeginKey, ForeignKey secondForeginKey, PrintWriter out) {
        out.append(INDENT + "private void " + limitNameForSaveAsociacionClassMethod(asocClassName) + "(Connection conn) throws SQLException {" + NEW_LINE);
        out.append(INDENT + INDENT + asocClassName + " obj = new " + asocClassName + "();" + NEW_LINE);
        out.append(INDENT + INDENT + "obj.set" + firstForeginKey.getName() + "(getIdFromDb4o());" + NEW_LINE);
        out.append(INDENT + INDENT + "if(" + firstForeginKey.getReferencedObjectFieldName() + " != null) {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "for(" + secondForeginKey.getReferencedTableName() + " o : " + firstForeginKey.getReferencedObjectFieldName() + ") {" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + INDENT + "obj.set" + secondForeginKey.getName() + "(o.getIdFromDb4o());" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + INDENT + "obj.saveRecord(conn);" + NEW_LINE);
        out.append(INDENT + INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + INDENT + "}" + NEW_LINE);
        out.append(INDENT + "}" + NEW_LINE);
    }

    /**
     * Generate name of method that save asocioans class.
     * @param associacionClassName Name of asocioacion class name.
     * @return Name of asocioacion save method.
     */
    private String limitNameForSaveAsociacionClassMethod(String associacionClassName) {
        return "save" + relDBScriptGen.lastPartOfClassName(associacionClassName);
    }

    /**
     * method generate invokes of methods to save asociacives objects.
     * @param cl
     * @param out
     */
    private void generateSourceSaveAcociacivesClassesMethodInvokes(Class cl, PrintWriter out) {
        List<String> methodNames = getSaveAsociacionClassMethodsNames(cl);
        if (!methodNames.isEmpty()) {
            for (String s : methodNames) {
                out.append(INDENT + INDENT + s + "(conn);" + NEW_LINE);
            }
        }
    }

    private boolean isObjectTypeNameArray(String objectTypeName) {
        return objectTypeName.contains("[");
    }
}
