package com.hitao.codegen.configs.common;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_GENERATE_SET_AND_GET_COMMENTS;
import static com.hitao.codegen.constent.StringConstants.LINE_BREAK;
import static com.hitao.codegen.constent.StringConstants.SEMICOLON;
import static com.hitao.codegen.util.StringUtils.EMPTY;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.configs.DaoServiceConfigurationManager;
import com.hitao.codegen.configs.IClassCodeGenConfig;
import com.hitao.codegen.configs.IClassElementCodeGenConfig;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.ICodePieceInfo;
import com.hitao.codegen.configs.append.AppendCodesInfo;
import com.hitao.codegen.configs.append.IAppendCodesInfo;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.util.StringUtils;

/***
 * all classes configuration.
 * 
 * @author zhangjun.ht
 * @created 2010-11-28
 * @version $Id: ClassConfig.java 61 2011-05-27 02:08:23Z guest $
 */
public class ClassConfig extends ClassElementConfig implements IClassCodeGenConfig {

    private static final long serialVersionUID = 1463502222275614979L;

    private static final String EXTENDS_CLASS = "extends";

    private static final String IMPLEMENTS_CLASS = "implements";

    private String extendsClass_ = null;

    private String implementsClasses_ = null;

    private List<FieldConfig> fieldList_ = new ArrayList<FieldConfig>();

    private List<MethodConfig> methodList_ = new ArrayList<MethodConfig>();

    @Override
    public void setConfigObject(String argKey, IConfigObject argValue) {
        if (EXTENDS_CLASS.equalsIgnoreCase(argKey)) {
            setExtendsClass(argValue.toString());
        } else if (FieldConfig.MAIN_TAG.equalsIgnoreCase(argKey) || argValue instanceof FieldConfig) {
            addField((FieldConfig) argValue);
        } else if (MethodConfig.MAIN_TAG.equalsIgnoreCase(argKey) || argValue instanceof MethodConfig) {
            addMethod((MethodConfig) argValue);
        } else if (IMPLEMENTS_CLASS.equalsIgnoreCase(argKey)) {
            setImplementsClasses(argValue.toString());
        } else {
            super.setConfigObject(argKey, argValue);
        }
    }

    protected String getStatement() {
        StringBuffer statement = new StringBuffer();
        for (FieldConfig field : getFieldList()) {
            if (field.isGenGetMethod()) {
                addMethod(getMethedByField((FieldConfig) field, MethodType.GET_METHOD));
            }
            if (field.isGenSetMethod()) {
                addMethod(getMethedByField((FieldConfig) field, MethodType.SET_METHOD));
            }
        }
        if (!shouldBeAppend()) {
            if (!StringUtils.isEmpty(getPackagePath())) {
                statement.append(getPackageStatement() + LINE_BREAK);
            }
            statement.append(LINE_BREAK);
            getImportInfo(statement);
            statement.append(LINE_BREAK);
            getClassCommentsInfo(statement);
            getAnnotations(statement);
            String modify = getModify();
            if (!StringUtils.isEmpty(modify)) {
                statement.append(modify + " ");
            }
            statement.append(getClassType() + " ");
            getClassName(statement);
            statement.append(" ");
            getExtendsInfo(statement);
            getImplementsInfo(statement);
            statement.append("{" + LINE_BREAK + LINE_BREAK);
        }
        getFieldInfo(statement);
        getMethodInfo(statement);
        if (!shouldBeAppend()) {
            statement.append("}");
        }
        return statement.toString();
    }

    @Override
    public List<IAppendCodesInfo> getAppendCodesInfo(List<IAppendCodesInfo> argList) {
        for (String importClass : getAllImportClass()) {
            AppendCodesInfo info = new AppendCodesInfo();
            info.setCheckConditionCodes(importClass);
            info.addFindAppendIndexStrategy("import");
            info.addFindAppendIndexStrategyByStartsWith("\r\n");
            info.setWhiteSpace(EMPTY);
            info.setBreakLine(LINE_BREAK);
            addAppendCodeInfo(argList, info);
        }
        for (ICodeGenConfig field : getFieldList()) {
            field.getAppendCodesInfo(argList);
        }
        for (ICodeGenConfig method : getMethodList()) {
            method.getAppendCodesInfo(argList);
        }
        return argList;
    }

    protected void addField(FieldConfig argProperyConfig) {
        if (!fieldList_.contains(argProperyConfig)) {
            fieldList_.add(argProperyConfig);
            setParentForConfigObject(argProperyConfig);
        }
    }

    protected void addMethod(MethodConfig argMethodConfig) {
        if (!methodList_.contains(argMethodConfig)) {
            methodList_.add(argMethodConfig);
            setParentForConfigObject(argMethodConfig);
        }
    }

    /***
	 * Get package statement;
	 * 
	 * @return
	 */
    public String getPackageStatement() {
        String packagePath = getPackagePath();
        if (!StringUtils.isEmpty(packagePath)) {
            if (packagePath.indexOf("package ") < 0) {
                packagePath = "package " + packagePath;
            }
            if (!packagePath.endsWith(SEMICOLON)) {
                packagePath += SEMICOLON;
            }
            return packagePath;
        } else {
            return EMPTY;
        }
    }

    protected int methodNeedOverride(MethodConfig argMethodConfig) {
        int result = -1;
        MethodConfig method = null;
        for (int i = 0; i < methodList_.size(); i++) {
            method = methodList_.get(i);
            if (method.isOverride() && method.equals(argMethodConfig)) {
                result = i;
                break;
            }
        }
        return result;
    }

    /***
	 * get import message from <import name="" /> element and whole class path
	 * of the field.
	 * 
	 * @param argStringBuffer
	 */
    protected void getImportInfo(StringBuffer argStringBuffer) {
        Iterator<String> imp = getAllImportClass().iterator();
        ImportConfig importConfig;
        while (imp.hasNext()) {
            importConfig = new ImportConfig();
            importConfig.setValue(imp.next());
            importConfig.getCodePieceInfo(argStringBuffer);
            argStringBuffer.append(LINE_BREAK);
        }
    }

    /***
	 * get all class needed imported.
	 */
    public synchronized Set<String> getAllImportClass() {
        Set<String> set = new TreeSet<String>(getImportClassComparator());
        Set<ImportConfig> implictSet = this.getImportList();
        for (Iterator<ImportConfig> it = implictSet.iterator(); it.hasNext(); ) {
            set.add(it.next().getStatement());
        }
        return set;
    }

    @Override
    public Set<ImportConfig> getImportList() {
        Set<ImportConfig> set = super.getImportList();
        set.addAll(getFieldImportList());
        set.addAll(getMethodImportList());
        return set;
    }

    @Override
    public void clearImportClasses() {
        super.clearImportClasses();
        for (IClassElementCodeGenConfig field : getFieldList()) {
            field.clearImportClasses();
        }
        for (IClassElementCodeGenConfig method : getMethodList()) {
            method.clearImportClasses();
        }
    }

    /***
	 * Get imported classes from field configuration object.
	 * 
	 * @return
	 */
    protected Set<ImportConfig> getFieldImportList() {
        Set<ImportConfig> set = new HashSet<ImportConfig>();
        for (IClassElementCodeGenConfig field : getFieldList()) {
            set.addAll(field.getImportList());
        }
        return set;
    }

    /***
	 * Get imported classes from method configuration object.
	 * 
	 * @return
	 */
    protected Set<ImportConfig> getMethodImportList() {
        Set<ImportConfig> set = new HashSet<ImportConfig>();
        for (IClassElementCodeGenConfig method : getMethodList()) {
            set.addAll(method.getImportList());
        }
        return set;
    }

    /***
	 * get the comments of this class.
	 * 
	 * @param argStringBuffer
	 */
    protected void getClassCommentsInfo(StringBuffer argStringBuffer) {
        CommentConfig common = getComment();
        if (common.isOriginal()) {
            common.getCodePieceInfo(argStringBuffer);
        } else {
            argStringBuffer.append("/***" + LINE_BREAK);
            argStringBuffer.append(" *");
            common.getCodePieceInfo(argStringBuffer);
            argStringBuffer.append(LINE_BREAK);
            argStringBuffer.append(" */" + LINE_BREAK);
        }
    }

    /***
	 * get inherited class information.
	 * 
	 * @param argStringBuffer
	 */
    protected void getExtendsInfo(StringBuffer argStringBuffer) {
        if (!StringUtils.isEmpty(getExtendsClass())) {
            argStringBuffer.append("extends " + getExtendsClass() + " ");
        }
    }

    /***
	 * get implemented interfaces information of this class.
	 * 
	 * @param argStringBuffer
	 */
    protected void getImplementsInfo(StringBuffer argStringBuffer) {
        if (!StringUtils.isEmpty(getImplementsClasses())) {
            argStringBuffer.append("implements " + getImplementsClasses());
        }
    }

    /***
	 * get class type.
	 * 
	 * @return
	 */
    protected String getClassType() {
        return "class";
    }

    /***
	 * Get class name
	 * 
	 * @param argStringBuffer
	 */
    protected void getClassName(StringBuffer argStringBuffer) {
        argStringBuffer.append(getName() + getGenericType());
    }

    /***
	 * get all field information.
	 * 
	 * @param argStringBuffer
	 */
    protected void getFieldInfo(StringBuffer argStringBuffer) {
        for (ICodePieceInfo field : getFieldList()) {
            field.getCodePieceInfo(argStringBuffer);
            argStringBuffer.append(LINE_BREAK);
        }
    }

    /***
	 * get all method information.
	 * 
	 * @param argStringBuffer
	 */
    protected void getMethodInfo(StringBuffer argStringBuffer) {
        for (ICodePieceInfo method : getMethodList()) {
            method.getCodePieceInfo(argStringBuffer);
            argStringBuffer.append(LINE_BREAK);
        }
    }

    public String getExtendsClass() {
        return getShortClassName(extendsClass_);
    }

    public void setExtendsClass(String extendsClass) {
        this.extendsClass_ = extendsClass;
        addImport(extendsClass);
    }

    public String getImplementsClasses() {
        return getShortClassName(implementsClasses_);
    }

    public void setImplementsClasses(String argImplementsClasses) {
        this.implementsClasses_ = argImplementsClasses;
        addImport(argImplementsClasses);
    }

    @Override
    public void setName(String argName) {
        int index = argName.indexOf('<');
        if (index > -1) {
            setGenericType(argName.substring(index));
            argName = argName.substring(0, index);
        }
        super.setName(argName);
    }

    /***
	 * Check whether this class should override it's name.
	 * 
	 * @return
	 */
    protected boolean needExpandName() {
        return false;
    }

    public String getName() {
        String className = super.getName();
        if (!needExpandName()) {
            return className;
        }
        return CodeGenUtils.getClassName(className);
    }

    @Override
    public List<FieldConfig> getFieldList() {
        return fieldList_;
    }

    @Override
    public List<MethodConfig> getMethodList() {
        return methodList_;
    }

    @Override
    public void removeImportedClass(String argImport) {
        super.removeImportedClass(argImport);
        for (FieldConfig fieldConfig : getFieldList()) {
            fieldConfig.removeImportedClass(argImport);
        }
        for (MethodConfig methodConfig : getMethodList()) {
            methodConfig.removeImportedClass(argImport);
        }
    }

    private static final boolean genSetAndGetComment;

    static {
        genSetAndGetComment = Boolean.valueOf(DaoServiceConfigurationManager.getProperty(CODEGEN_GENERATE_SET_AND_GET_COMMENTS));
    }

    /***
	 * Create the set and get methods.
	 * 
	 * @param argField
	 * @param argMethodType
	 * @return MethodConfig
	 */
    protected MethodConfig getMethedByField(FieldConfig argField, MethodType argMethodType) {
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setGenComment(genSetAndGetComment);
        methodConfig.setModify("public");
        String titleCaseName = StringUtils.capitalize(argField.getName());
        StringBuffer methodName = new StringBuffer();
        methodName.append(argMethodType.getName()).append(titleCaseName);
        methodConfig.setName(methodName.toString());
        if (MethodType.SET_METHOD.equals(argMethodType)) {
            genSetMethod(methodConfig, argField);
        } else {
            genGetMethod(methodConfig, argField);
        }
        return methodConfig;
    }

    /***
	 * Get the set method information for field configuration.
	 * 
	 * @param methodConfig
	 * @param argField
	 */
    protected void genSetMethod(MethodConfig methodConfig, FieldConfig argField) {
        methodConfig.setReturnType("void");
        ParameterConfig parameter = new ParameterConfig();
        if (containsConcreteClass(argField)) {
            parameter.setConcreteClass(argField.getConcreteClass());
        }
        parameter.setClassName(argField.getClassName());
        String paramName = argField.getName();
        parameter.setName(paramName);
        methodConfig.addParameter(parameter);
        if (genSetAndGetComment) {
            CommentConfig methodComment = new CommentConfig();
            methodComment.setValue("Set " + argField.getName());
            methodConfig.setComment(methodComment);
            CommentConfig paramComment = new CommentConfig();
            paramComment.setValue(argField.getName());
            parameter.setComment(paramComment);
        }
        BodyConfig body = new BodyConfig();
        methodConfig.setMethodBody(body);
        body.setValue("this." + argField.getName() + " = " + parameter.getName() + SEMICOLON);
    }

    /***
	 * get get method information for field configuration.
	 * 
	 * @param methodConfig
	 * @param argField
	 */
    protected void genGetMethod(MethodConfig methodConfig, FieldConfig argField) {
        String returnType = argField.getClassName();
        if (containsConcreteClass(argField)) {
            returnType = returnType + getConreteClassStatement(argField);
        }
        methodConfig.setReturnType(returnType);
        if (genSetAndGetComment) {
            CommentConfig comment = new CommentConfig();
            comment.setValue("Get " + argField.getName());
            methodConfig.setComment(comment);
        }
        BodyConfig body = new BodyConfig();
        methodConfig.setMethodBody(body);
        body.setValue("return " + argField.getName() + SEMICOLON);
    }

    private enum MethodType {

        GET_METHOD("get"), SET_METHOD("set");

        private String name_;

        MethodType(String argName) {
            this.name_ = argName;
        }

        public String getName() {
            return name_;
        }
    }

    public Object clone() throws CloneNotSupportedException {
        ClassConfig config = (ClassConfig) super.clone();
        config.fieldList_ = new ArrayList<FieldConfig>();
        for (FieldConfig fieldConfig : config.getFieldList()) {
            config.setConfigObject(FieldConfig.MAIN_TAG, (FieldConfig) fieldConfig.clone());
        }
        config.methodList_ = new ArrayList<MethodConfig>();
        for (MethodConfig methodConfig : config.getMethodList()) {
            config.setConfigObject(MethodConfig.MAIN_TAG, (MethodConfig) methodConfig.clone());
        }
        return config;
    }

    protected Comparator<String> getImportClassComparator() {
        return importComparator;
    }

    protected static Comparator<String> importComparator = new ImportClassComparator();

    public static class ImportClassComparator implements Comparator<String>, Serializable {

        private static final long serialVersionUID = -1244394684393174605L;

        @Override
        public int compare(String o1, String o2) {
            if (StringUtils.isEmpty(o1) || StringUtils.isEmpty(o2)) {
                return -1;
            } else if (o1.equals(o2)) {
                return 0;
            }
            return o1.startsWith("import static") ? -1 : o1.compareTo(o2);
        }
    }
}
