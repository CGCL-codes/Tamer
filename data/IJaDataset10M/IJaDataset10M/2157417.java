package org.shiftone.cdep.model;

import org.shiftone.cdep.util.ClassFilter;
import org.shiftone.cdep.util.NullIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

public class ClassInfo {

    private String importLocation;

    private String className;

    private String superClassName;

    private boolean interfaceIn;

    private boolean innerIn;

    private int classId = 0;

    private PackageInfo packageInfo;

    private HashSet implementsList;

    private HashSet callsList;

    private ArrayList methods;

    public ClassInfo(String importLocation, String className, String superClassName, boolean interfaceIn) {
        this.importLocation = importLocation;
        this.className = className;
        if (ClassFilter.acceptType(superClassName)) this.superClassName = superClassName;
        this.interfaceIn = interfaceIn;
        this.innerIn = false;
    }

    public String getImportLocation() {
        return importLocation;
    }

    public String getClassName() {
        return className;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public boolean isInterface() {
        return interfaceIn;
    }

    public void setInner(boolean innerIn) {
        this.innerIn = innerIn;
    }

    public boolean isInner() {
        return innerIn;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public void addImplementsClass(String className) {
        if (implementsList == null) implementsList = new HashSet();
        implementsList.add(className);
    }

    public Iterator getImplementsClasses() {
        return (implementsList == null) ? NullIterator.getNullIterator() : implementsList.iterator();
    }

    public boolean hasImplementsClasses() {
        return (implementsList != null) && (implementsList.size() > 0);
    }

    public void addClassCall(String className) {
        if (!ClassFilter.acceptType(className)) return;
        if (callsList == null) callsList = new HashSet();
        callsList.add(className);
    }

    public Iterator getClassCalls() {
        return (callsList == null) ? NullIterator.getNullIterator() : callsList.iterator();
    }

    public boolean hasClassCalls() {
        return (callsList != null) && (callsList.size() > 0);
    }

    public void addMethod(MethodInfo methodDep) {
        if (methods == null) methods = new ArrayList();
        methods.add(methodDep);
    }

    public Iterator getMethods() {
        return (methods == null) ? NullIterator.getNullIterator() : methods.iterator();
    }

    public boolean hasMethods() {
        return (methods != null) && (methods.size() > 0);
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
