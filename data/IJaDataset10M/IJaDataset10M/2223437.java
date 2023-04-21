package com.ibatis.sqlmap.implgen;

import com.ibatis.sqlmap.implgen.annotations.CacheModel;
import com.ibatis.sqlmap.implgen.annotations.CacheModels;
import com.ibatis.sqlmap.implgen.annotations.Delete;
import com.ibatis.sqlmap.implgen.annotations.HasSql;
import com.ibatis.sqlmap.implgen.annotations.Insert;
import com.ibatis.sqlmap.implgen.annotations.Parameter;
import com.ibatis.sqlmap.implgen.annotations.ParameterMap;
import com.ibatis.sqlmap.implgen.annotations.ParameterMaps;
import com.ibatis.sqlmap.implgen.annotations.Procedure;
import com.ibatis.sqlmap.implgen.annotations.Result;
import com.ibatis.sqlmap.implgen.annotations.ResultMap;
import com.ibatis.sqlmap.implgen.annotations.ResultMaps;
import com.ibatis.sqlmap.implgen.annotations.Select;
import com.ibatis.sqlmap.implgen.annotations.Statement;
import com.ibatis.sqlmap.implgen.annotations.Update;
import com.ibatis.sqlmap.implgen.bean.ParsedCacheModel;
import com.ibatis.sqlmap.implgen.bean.ParsedClass;
import com.ibatis.sqlmap.implgen.bean.ParsedMethod;
import com.ibatis.sqlmap.implgen.bean.ParsedParam;
import com.ibatis.sqlmap.implgen.bean.ParsedParameter;
import com.ibatis.sqlmap.implgen.bean.ParsedParameterMap;
import com.ibatis.sqlmap.implgen.bean.ParsedResult;
import com.ibatis.sqlmap.implgen.bean.ParsedResultMap;
import com.ibatis.sqlmap.implgen.template.generated.GeneratedSqlMapImplementationTemplate;
import com.ibatis.sqlmap.implgen.template.generated.GeneratedSqlMapXmlTemplate;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.MirroredTypeException;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Annotation processor that implements annotated Interface's with SQL in comments as an iBATIS SQLMap Class and XML
 * file.
 * <p/>
 * todo: method overloading support in interfaces that we generate from
 *
 * @see com.ibatis.sqlmap.implgen.example.ExampleDaoOne
 */
public class IbatisImplGenAnnotationProcessor implements AnnotationProcessor, AnnotationProcessorFactory {

    Util.Log log = Util.getLog();

    private AnnotationProcessorEnvironment env;

    private Messager messager;

    private static final String DEBUG_OPTION = "-Adebug";

    private static final String DAO_SNIPPET_OPTION = "-AoutputDaoSnippet";

    private static final String SQL_MAP_SNIPPET_OPTION = "-AoutputSqlMapSnippet";

    private static final String SQL_MAP_FORCE_EXTEND_OPTION = "-AforceSqlMapImplsToExtend";

    private static final String START_SQL_INDICATOR = "/*sql{";

    private boolean outputDaoSnippet = true;

    private boolean outputSqlMapSnippet = true;

    private String forceExtendClass = null;

    public IbatisImplGenAnnotationProcessor() {
    }

    public IbatisImplGenAnnotationProcessor(AnnotationProcessorEnvironment env) {
        this.env = env;
        messager = env.getMessager();
    }

    public void process() {
        Map<String, String> options = env.getOptions();
        Set<String> keys = options.keySet();
        for (String key : keys) {
            String value = key.substring(key.indexOf("=") + 1);
            if (key.startsWith(DEBUG_OPTION + "=")) log.setDebug("true".equalsIgnoreCase(value));
            if (key.startsWith(DAO_SNIPPET_OPTION + "=")) outputDaoSnippet = "true".equalsIgnoreCase(value);
            if (key.startsWith(SQL_MAP_SNIPPET_OPTION + "=")) outputSqlMapSnippet = "true".equalsIgnoreCase(value);
            if (key.startsWith(SQL_MAP_FORCE_EXTEND_OPTION + "=")) forceExtendClass = value;
        }
        log.info("Starting " + IbatisImplGenAnnotationProcessor.class.getSimpleName());
        log.debug("outputDaoSnippet set to " + outputDaoSnippet);
        log.debug("outputSqlMapSnippet set to " + outputSqlMapSnippet);
        log.debug("forceExtendClass set to " + forceExtendClass);
        try {
            HashMap<String, List<ParsedClass>> packages = new HashMap<String, List<ParsedClass>>();
            for (TypeDeclaration typeDecl : env.getSpecifiedTypeDeclarations()) {
                log.debug("processing type " + typeDecl);
                ParsedClass parsedClass = new ParsedClass();
                parsedClass.setName(typeDecl.getSimpleName());
                parsedClass.setPackageStr(typeDecl.getPackage().getQualifiedName());
                parsedClass.setClassFile(typeDecl.getPosition().file());
                parsedClass.setClassAnInterface(typeDecl instanceof InterfaceDeclaration);
                parsedClass.setForceExtendClass(forceExtendClass);
                log.debug("an interface? " + parsedClass.isClassAnInterface());
                HasSql hasSql = typeDecl.getAnnotation(HasSql.class);
                if (hasSql != null) {
                    if (hasSql.xmlType() != null) {
                        parsedClass.setOverrideXmlType(ParsedMethod.findType(hasSql.xmlType()));
                        if (parsedClass.getOverrideXmlType() != null) {
                            log.debug("got overrideXmlType " + parsedClass.getOverrideXmlType());
                        }
                    }
                    try {
                        hasSql.extend();
                    } catch (MirroredTypeException e) {
                        TypeMirror typeMirror = e.getTypeMirror();
                        if (typeMirror != null) {
                            parsedClass.setForceExtendClass(typeMirror.toString());
                        }
                    }
                }
                List<ParsedMethod> rawMethods = new ArrayList<ParsedMethod>();
                for (MethodDeclaration methodDeclaration : typeDecl.getMethods()) {
                    log.debug("pre-processing method " + methodDeclaration);
                    ParsedMethod method = processRawMethod(methodDeclaration, parsedClass);
                    rawMethods.add(method);
                }
                Collections.sort(rawMethods);
                int posIdx = 0;
                Set<String> methodNames = new HashSet<String>();
                for (ParsedMethod method : rawMethods) {
                    log.debug("processing method " + method);
                    ParsedMethod nextMethod = posIdx + 1 >= rawMethods.size() ? null : rawMethods.get(posIdx + 1);
                    log.debug("next method " + nextMethod);
                    processPost(parsedClass, method, nextMethod);
                    if (method.isSqlMethod()) {
                        if (methodNames.contains(method.getName())) {
                            method.setUseIdInIbatisId(true);
                        } else {
                            methodNames.add(method.getName());
                        }
                        parsedClass.getMethods().add(method);
                    }
                    posIdx++;
                }
                if (parsedClass.isAnySQLMethods()) {
                    log.info("Parsed " + parsedClass.getMethods().size() + " SQL methods from " + parsedClass.getFullyQualifiedName());
                    processClass(parsedClass);
                }
                List<ParsedClass> classList = packages.get(typeDecl.getPackage().getQualifiedName());
                if (classList == null) {
                    classList = new ArrayList<ParsedClass>();
                    packages.put(typeDecl.getPackage().getQualifiedName(), classList);
                }
                if (parsedClass.isAnySQLMethods()) classList.add(parsedClass);
            }
            if (outputDaoSnippet) {
                for (List<ParsedClass> classes : packages.values()) {
                    if (classes.size() == 0) continue;
                    PrintWriter daoXmlFileWriter = env.getFiler().createTextFile(Filer.Location.SOURCE_TREE, classes.get(0).getPackageStr(), new File("GeneratedDaoSnippet.xml"), null);
                    daoXmlFileWriter.println("<!-- generated snippet intended for use in iBATIS DAO config -->");
                    for (ParsedClass parsedClass : classes) {
                        daoXmlFileWriter.println("<dao interface=\"" + parsedClass.getFullyQualifiedName() + "\" " + "implementation=\"" + parsedClass.getGeneratedJavaClassFullyQualifiedName() + "\"/>");
                    }
                    daoXmlFileWriter.close();
                    log.info("wrote GeneratedDaoSnippet.xml");
                }
            }
            if (outputSqlMapSnippet) {
                for (List<ParsedClass> classes : packages.values()) {
                    if (classes.size() == 0) continue;
                    PrintWriter sqlMapXmlFileWriter = env.getFiler().createTextFile(Filer.Location.SOURCE_TREE, classes.get(0).getPackageStr(), new File("GeneratedSqlMapSnippet.xml"), null);
                    sqlMapXmlFileWriter.println("<!-- generated snippet intended for use in iBATIS SqlMap config -->");
                    for (ParsedClass parsedClass : classes) {
                        sqlMapXmlFileWriter.println("<sqlMap resource=\"" + parsedClass.getGeneratedXmlFilePath() + "\"/>");
                    }
                    sqlMapXmlFileWriter.close();
                    log.info("wrote GeneratedSqlMapSnippet.xml");
                }
            }
            log.info("done");
        } catch (IOException e) {
            messager.printError(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private void processPost(ParsedClass parsedClass, ParsedMethod method, ParsedMethod nextMethod) throws IOException {
        int nextMethodStartLine = nextMethod != null ? nextMethod.getLine() : -1;
        int nextMethodStartCol = nextMethod != null ? nextMethod.getColumn() : -1;
        String sql = getSql(parsedClass.getClassFile(), method.getLine(), method.getColumn(), nextMethodStartLine, nextMethodStartCol).toString();
        method.setSql(sql);
        if (method.getType() == null && sql.length() > 0) {
            ParsedMethod.Type type = guessType(sql);
            if (type != null) {
                method.setType(type);
                log.debug("detected type " + type);
            }
        }
        if (method.isAnyResultMap()) {
            for (ParsedResultMap resultMap : parsedClass.getResultMaps()) {
                if (!method.getResultMap().equals(resultMap.getId())) continue;
                resultMap.setJavaClass(method.getReturnsType());
            }
        }
    }

    private ParsedMethod.Type guessType(String sql) {
        ParsedMethod.Type type = null;
        if (Util.sqlStartsWithKeyword("select", sql)) type = ParsedMethod.Type.SELECT;
        if (Util.sqlStartsWithKeyword("delete", sql)) type = ParsedMethod.Type.DELETE;
        if (Util.sqlStartsWithKeyword("update", sql)) type = ParsedMethod.Type.UPDATE;
        if (Util.sqlStartsWithKeyword("insert", sql)) type = ParsedMethod.Type.INSERT;
        if (Util.sqlStartsWithKeyword("call", sql)) type = ParsedMethod.Type.PROCEDURE;
        return type;
    }

    /**
     * Outputs the implemented class file and xml file.
     */
    private void processClass(ParsedClass parsedClass) throws IOException {
        PrintWriter xmlFileWriter = env.getFiler().createTextFile(Filer.Location.SOURCE_TREE, parsedClass.getPackageStr(), new File(parsedClass.getGeneratedXmlFileName()), null);
        GeneratedSqlMapXmlTemplate mapXmlTemplate = new GeneratedSqlMapXmlTemplate();
        mapXmlTemplate.parsedClass = parsedClass;
        mapXmlTemplate.write(xmlFileWriter);
        PrintWriter mapClassWriter = env.getFiler().createSourceFile(parsedClass.getGeneratedJavaClassFullyQualifiedName());
        GeneratedSqlMapImplementationTemplate mapClassTemplate = new GeneratedSqlMapImplementationTemplate();
        mapClassTemplate.parsedClass = parsedClass;
        mapClassTemplate.write(mapClassWriter);
        log.info("wrote " + parsedClass.getGeneratedXmlFileName() + " and " + parsedClass.getGeneratedJavaClassName() + ".java");
    }

    private ParsedMethod processRawMethod(MethodDeclaration declaration, ParsedClass parsedClass) {
        ParsedMethod method = new ParsedMethod();
        method.setBelongsToClass(parsedClass);
        method.setName(declaration.getSimpleName());
        method.setLine(declaration.getPosition().line());
        method.setColumn(declaration.getPosition().column());
        for (ReferenceType referenceType : declaration.getThrownTypes()) {
            log.debug("added alternative throw for " + referenceType.toString());
            method.setAlternativeThrowsClass(referenceType.toString());
        }
        Select selectAnnotation = declaration.getAnnotation(Select.class);
        if (selectAnnotation != null) {
            method.setType(ParsedMethod.Type.SELECT);
            method.setResultSetType(selectAnnotation.resultSetType());
            method.setCacheModel(selectAnnotation.cacheModel());
            method.setResultMap(selectAnnotation.resultMap());
            method.setParameterMap(selectAnnotation.parameterMap());
        }
        Procedure procAnnotation = declaration.getAnnotation(Procedure.class);
        if (procAnnotation != null) {
            method.setType(ParsedMethod.Type.PROCEDURE);
            method.setCacheModel(procAnnotation.cacheModel());
            method.setResultMap(procAnnotation.resultMap());
            method.setParameterMap(procAnnotation.parameterMap());
        }
        Statement statementAnnotation = declaration.getAnnotation(Statement.class);
        if (statementAnnotation != null) {
            method.setType(ParsedMethod.Type.STATEMENT);
            method.setCacheModel(statementAnnotation.cacheModel());
            method.setResultMap(statementAnnotation.resultMap());
            method.setParameterMap(statementAnnotation.parameterMap());
        }
        Delete deleteAnnotation = declaration.getAnnotation(Delete.class);
        if (deleteAnnotation != null) {
            method.setType(ParsedMethod.Type.DELETE);
            method.setParameterMap(deleteAnnotation.parameterMap());
        }
        Insert insertAnnotation = declaration.getAnnotation(Insert.class);
        if (insertAnnotation != null) {
            method.setType(ParsedMethod.Type.INSERT);
            method.setParameterMap(insertAnnotation.parameterMap());
        }
        Update updateAnnotation = declaration.getAnnotation(Update.class);
        if (updateAnnotation != null) {
            method.setType(ParsedMethod.Type.UPDATE);
            method.setParameterMap(updateAnnotation.parameterMap());
        }
        ResultMaps resultMapsAnnotation = declaration.getAnnotation(ResultMaps.class);
        if (resultMapsAnnotation != null) {
            ResultMap[] resultMaps = resultMapsAnnotation.value();
            for (ResultMap resultMap : resultMaps) {
                addResultMapAnnotation(parsedClass, resultMap);
            }
        }
        ResultMap resultMapAnnotation = declaration.getAnnotation(ResultMap.class);
        if (resultMapAnnotation != null) {
            addResultMapAnnotation(parsedClass, resultMapAnnotation);
        }
        ParameterMaps parameterMapsAnnotation = declaration.getAnnotation(ParameterMaps.class);
        if (parameterMapsAnnotation != null) {
            ParameterMap[] parameterMaps = parameterMapsAnnotation.value();
            for (ParameterMap parameterMap : parameterMaps) {
                addParameterMapAnnotation(parsedClass, parameterMap);
            }
        }
        ParameterMap parameterMapAnnotation = declaration.getAnnotation(ParameterMap.class);
        if (parameterMapAnnotation != null) {
            addParameterMapAnnotation(parsedClass, parameterMapAnnotation);
        }
        CacheModels cacheModelsAnnotation = declaration.getAnnotation(CacheModels.class);
        if (cacheModelsAnnotation != null) {
            CacheModel[] cacheModels = cacheModelsAnnotation.value();
            for (CacheModel cacheModel : cacheModels) {
                addCacheModelAnnoataion(parsedClass, cacheModel);
            }
        }
        CacheModel cacheModelAnnotation = declaration.getAnnotation(CacheModel.class);
        if (cacheModelAnnotation != null) {
            addCacheModelAnnoataion(parsedClass, cacheModelAnnotation);
        }
        Result resultAnnotation = declaration.getAnnotation(Result.class);
        if (resultAnnotation != null) {
            messager.printError(declaration.getPosition(), "incorrectly placed @Result");
        }
        String returnsType = declaration.getReturnType().toString();
        if (returnsType.contains("<") && returnsType.contains(">")) {
            returnsType = returnsType.substring(returnsType.indexOf("<") + 1, returnsType.indexOf(">"));
        }
        method.setReturns(declaration.getReturnType().toString());
        method.setReturnsType(returnsType);
        for (ParameterDeclaration typeParam : declaration.getParameters()) {
            ParsedParam param = new ParsedParam();
            param.setName(typeParam.getSimpleName());
            param.setJavaType(typeParam.getType().toString());
            method.getParams().add(param);
        }
        return method;
    }

    private void addResultMapAnnotation(ParsedClass parsedClass, ResultMap resultMapAnnotation) {
        ParsedResultMap resultMap = new ParsedResultMap();
        resultMap.setId(resultMapAnnotation.id());
        resultMap.setExtendsMap(resultMapAnnotation.extendsMap());
        Result[] results = resultMapAnnotation.results();
        for (Result result : results) {
            ParsedResult parsedResult = new ParsedResult();
            parsedResult.setProperty(result.property());
            parsedResult.setColumn(result.column());
            parsedResult.setJavaType(result.javaType());
            parsedResult.setJdbcType(result.jdbcType());
            parsedResult.setNullValue(result.nullValue());
            resultMap.getResults().add(parsedResult);
        }
        parsedClass.getResultMaps().add(resultMap);
    }

    private void addParameterMapAnnotation(ParsedClass parsedClass, ParameterMap parameterMapAnnotation) {
        ParsedParameterMap parameterMap = new ParsedParameterMap();
        parameterMap.setId(parameterMapAnnotation.id());
        parameterMap.setJavaClass(parameterMapAnnotation.classFor());
        Parameter[] parameters = parameterMapAnnotation.parameters();
        for (Parameter parameter : parameters) {
            ParsedParameter parsedParameter = new ParsedParameter();
            parsedParameter.setProperty(parameter.property());
            parsedParameter.setJavaType(parameter.javaType());
            parsedParameter.setJdbcType(parameter.jdbcType());
            parsedParameter.setNullValue(parameter.nullValue());
            parsedParameter.setMode(parameter.mode());
            parameterMap.getParameters().add(parsedParameter);
        }
        parsedClass.getParameterMaps().add(parameterMap);
    }

    private void addCacheModelAnnoataion(ParsedClass parsedClass, CacheModel cacheModelAnnotation) {
        ParsedCacheModel cacheModel = new ParsedCacheModel();
        cacheModel.setId(cacheModelAnnotation.id());
        cacheModel.setType(cacheModelAnnotation.type());
        cacheModel.setFlushIntervalHours(cacheModelAnnotation.flushIntervalHours());
        parsedClass.getCacheModels().add(cacheModel);
    }

    /**
     * Look for SQL in the file starting at line and col ending at stopAtLine and stopAtCol.
     */
    private StringBuffer getSql(File file, int line, int col, int stopAtLine, int stopAtCol) throws IOException {
        log.debug("looking for sql " + file.getName() + " line " + line + " col " + col + " stopAtLine " + stopAtLine + " stopAtCol " + stopAtCol);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int currentLine = 0;
        char lastChar = 0;
        StringBuffer sql = new StringBuffer();
        boolean appending = false;
        int startPosUpTo = -1;
        boolean breakOuter = false;
        while (br.ready()) {
            String lineStr = br.readLine();
            currentLine++;
            if (currentLine + 1 >= line) {
                int charPos = 0;
                for (char currChar : lineStr.toCharArray()) {
                    charPos++;
                    if (currentLine + 1 == line && charPos < col) continue;
                    if (currentLine + 1 >= stopAtLine && stopAtLine != -1 && charPos >= stopAtCol && stopAtCol != -1) {
                        breakOuter = true;
                        break;
                    }
                    if (appending) {
                        sql.append(currChar);
                    } else {
                        int startPos = START_SQL_INDICATOR.indexOf(currChar);
                        if (startPosUpTo + 1 == startPos) {
                            startPosUpTo++;
                            if (startPosUpTo == START_SQL_INDICATOR.length() - 1) appending = true;
                        } else {
                            startPosUpTo = -1;
                        }
                    }
                    if (sql.length() > 0 && '*' == lastChar && '/' == currChar) {
                        sql.delete(sql.length() - 2, sql.length());
                        breakOuter = true;
                        break;
                    }
                    lastChar = currChar;
                }
            }
            if (breakOuter) break;
        }
        br.close();
        if (sql.lastIndexOf("}") == sql.length() - 1 && sql.length() > 0) {
            sql.delete(sql.length() - 1, sql.length());
        }
        if (sql.length() > 0) {
            log.debug("SQL: " + sql);
        }
        return sql;
    }

    /**
     * Returns an annotation processor.
     *
     * @return An annotation processor for annotations if requested, otherwise, returns the NO_OP annotation processor.
     */
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> decls, AnnotationProcessorEnvironment env) {
        AnnotationProcessor result;
        if (decls.isEmpty()) {
            result = AnnotationProcessors.NO_OP;
        } else {
            result = new IbatisImplGenAnnotationProcessor(env);
        }
        return result;
    }

    /**
     * This factory only builds processors for all Ibatis Impl Gen annotations.
     *
     * @return a collection containing the annotation names.
     */
    public Collection<String> supportedAnnotationTypes() {
        return Arrays.asList("com.ibatis.sqlmap.implgen.*");
    }

    public Collection<String> supportedOptions() {
        Set<String> set = new HashSet<String>();
        set.add(DEBUG_OPTION);
        return set;
    }
}
