package com.volantis.mcs.build.javadoc;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.volantis.mcs.build.javadoc.ClassInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains information about a method in an object which is used
 * for generating code.
 */
public class MethodInfo {

    /**
   * The class to which this object belongs.
   */
    private ClassInfo classInfo;

    /**
   * The name of the method this object represents.
   */
    private String name;

    /**
   * The return type of the method this object represents.
   */
    private TypeInfo returnType;

    /**
   * The comment associated with the method this object represents.
   */
    private String comment;

    /**
   * The underlying JavaDoc object.
   */
    private MethodDoc methodDoc;

    /**
   * The list of the parameters (represented by ParameterInfo objects) in
   * this method.
   */
    private List parameters;

    /**
   * A map from parameter name to ParameterInfo.
   */
    private Map parameterMap;

    /**
   * Create a new <code>MethodInfo</code>.
   */
    private MethodInfo() {
        parameterMap = new HashMap();
        parameters = new ArrayList();
    }

    /**
   * Create a new <code>MethodInfo</code>.
   * @param classInfo The class information for the class which the method
   * which this object represents belongs.
   * @param methodDoc The underlying JavaDoc object representing a class.
   */
    public MethodInfo(ClassInfo classInfo, MethodDoc methodDoc) {
        this();
        this.classInfo = classInfo;
        this.methodDoc = methodDoc;
        this.name = methodDoc.name();
        this.returnType = new TypeInfo(methodDoc.returnType());
        this.comment = methodDoc.getRawCommentText();
        Parameter[] parametersDoc = methodDoc.parameters();
        for (int i = 0; i < parametersDoc.length; i += 1) {
            addParameter(new ParameterInfo(classInfo, parametersDoc[i]));
        }
    }

    /**
   * Create a new <code>MethodInfo</code>.
   * @param name The name of the method which this object represents.
   * @param returnType The return type of the method which this object
   * represents.
   * @param comment The comment associated with the method which this object
   * represents.
   */
    public MethodInfo(String name, TypeInfo returnType, String comment) {
        this();
        this.name = name;
        this.returnType = returnType;
        this.comment = comment;
    }

    /**
   * Set the class information of the class which the constructor
   * which this object represents belongs.
   * @param classInfo The class information for the class which the constructor
   * which this object represents belongs.
   */
    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    /**
   * Add the specified parameter to the list of parameters.
   * @param parameter The parameter to add.
   */
    public void addParameter(ParameterInfo parameter) {
        parameters.add(parameter);
        parameterMap.put(parameter.getName(), parameter);
    }

    /**
   * Get the list of parameters.
   * @return The list of parameters.
   */
    public List getParameters() {
        return parameters;
    }

    /**
   * Get the parameter with the specified name.
   * @param parameterName The name of the parameter.
   * @return The specified parameter, or null if it could not be found.
   */
    public ParameterInfo getParameter(String parameterName) {
        return (ParameterInfo) parameterMap.get(parameterName);
    }

    /**
   * Get the name of the method this object represents.
   * @return The name.
   */
    public String getName() {
        return name;
    }

    /**
   * Get the return type of the method this object represents.
   * @return The return type.
   */
    public TypeInfo getReturnType() {
        return returnType;
    }

    /**
   * Get the name of the return type (including dimension) of the method
   * this object represents.
   * @return The name of the return type.
   */
    public String getReturnTypeName() {
        return returnType.getName();
    }

    /**
   * Get the comment associated wth the method this object represents.
   * @return The comment.
   */
    public String getComment() {
        return comment;
    }
}
