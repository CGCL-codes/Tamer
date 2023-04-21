package org.developersblog.architecture.analyzer.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rafsob
 */
public class ClassDescription {

    private Map<String, String> variableClassReferences = new Hashtable<String, String>();

    public Map<String, String> getVariableClassReferences() {
        return variableClassReferences;
    }

    public void setVariableClassReferences(Map<String, String> variableClassReferences) {
        this.variableClassReferences = variableClassReferences;
    }

    private String packageName;

    private String className;

    private Map<String, String> importsMap = new Hashtable<String, String>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getImportsMap() {
        return importsMap;
    }

    public void setImportsMap(Map<String, String> importsMap) {
        this.importsMap = importsMap;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private List<MethodInformation> methodInformations = new ArrayList<MethodInformation>();

    public List<MethodInformation> getMethodInformations() {
        return methodInformations;
    }

    public void setMethodInformations(List<MethodInformation> methodInformations) {
        this.methodInformations = methodInformations;
    }

    public String getPackageForMethodTargetCallInformation(MethodCallInformation methodTargetCallInformation) {
        String clazzName = getClassForMethodTargetCallInformation(methodTargetCallInformation);
        if (this.importsMap.containsKey(clazzName)) return this.importsMap.get(clazzName).replaceAll("." + clazzName, ""); else return "null";
    }

    public String getClassForMethodTargetCallInformation(MethodCallInformation methodTargetCallInformation) {
        if (this.variableClassReferences.containsKey(methodTargetCallInformation.getVariable())) return this.variableClassReferences.get(methodTargetCallInformation.getVariable()); else return methodTargetCallInformation.getVariable();
    }
}
