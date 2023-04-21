package chrriis.udoc.model;

import java.util.ArrayList;
import java.util.List;
import chrriis.udoc.model.processor.ClassProcessor;

public class ClassInfo {

    protected String className;

    protected ClassProcessor classProcessor;

    protected PackageInfo packageInfo;

    protected ClassInfo(String className, ClassProcessor classProcessor) {
        this.className = className;
        this.classProcessor = classProcessor;
        int index = className.lastIndexOf('.');
        if (index == -1) {
            packageInfo = new PackageInfo("", null);
        } else {
            packageInfo = new PackageInfo(className.substring(0, index), null);
        }
    }

    public String getClassName() {
        return className;
    }

    public PackageInfo getPackage() {
        return packageInfo;
    }

    public String getName() {
        return Util.unescapeClassName(className.substring(className.lastIndexOf('.') + 1));
    }

    protected String declaration;

    protected String[] genericDeclarations;

    protected void setDeclaration(String declaration, String[] genericDeclarations) {
        this.declaration = declaration;
        this.genericDeclarations = genericDeclarations;
    }

    public String getDeclaration() {
        return declaration;
    }

    public String[] getGenericDeclarations() {
        return genericDeclarations;
    }

    protected int modifiers;

    protected void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public int getModifiers() {
        return modifiers;
    }

    protected ClassInfo[] superTypes;

    protected void setSuperTypes(ClassInfo[] superTypes) {
        this.superTypes = superTypes;
    }

    public ClassInfo[] getSuperTypes() {
        if (superTypes == null) {
            return new ClassInfo[0];
        }
        return superTypes;
    }

    protected ClassInfo[] subTypes;

    protected void setSubTypes(ClassInfo[] subTypes) {
        this.subTypes = subTypes;
    }

    public ClassInfo[] getSubTypes() {
        if (subTypes == null) {
            return new ClassInfo[0];
        }
        return subTypes;
    }

    protected transient ClassInfo[] compositions;

    public ClassInfo[] getCompositions() {
        if (compositions != null) {
            return compositions;
        }
        List list = new ArrayList();
        ClassInfo[] classInfos = getClassInfos(getFields());
        for (int i = 0; i < classInfos.length; i++) {
            ClassInfo classInfo = classInfos[i];
            if (!classInfo.equals(this)) {
                list.add(classInfo);
            }
        }
        classInfos = getClassInfos(getEnums());
        for (int i = 0; i < classInfos.length; i++) {
            ClassInfo classInfo = classInfos[i];
            if (!classInfo.equals(this)) {
                list.add(classInfo);
            }
        }
        compositions = (ClassInfo[]) list.toArray(new ClassInfo[0]);
        return compositions;
    }

    protected transient ClassInfo[] associations;

    public ClassInfo[] getAssociations() {
        if (associations != null) {
            return associations;
        }
        List list = new ArrayList();
        MethodInfo[] methodInfos = getMethods();
        for (int i = 0; i < methodInfos.length; i++) {
            MethodInfo methodInfo = methodInfos[i];
            ClassInfo[] classInfos = getClassInfos(methodInfo.getParameters());
            for (int j = 0; j < classInfos.length; j++) {
                ClassInfo classInfo = classInfos[j];
                if (!classInfo.equals(this)) {
                    list.add(classInfo);
                }
            }
            classInfos = getClassInfos(methodInfo.getExceptionParameters());
            for (int j = 0; j < classInfos.length; j++) {
                ClassInfo classInfo = classInfos[j];
                if (!classInfo.equals(this)) {
                    list.add(classInfo);
                }
            }
            classInfos = getClassInfos(new FieldInfo[] { methodInfo.getReturnedParameter() });
            for (int j = 0; j < classInfos.length; j++) {
                ClassInfo classInfo = classInfos[j];
                if (!classInfo.equals(this)) {
                    list.add(classInfo);
                }
            }
            classInfos = methodInfo.getAnnotations();
            for (int j = 0; j < classInfos.length; j++) {
                ClassInfo classInfo = classInfos[j];
                if (!classInfo.equals(this)) {
                    list.add(classInfo);
                }
            }
        }
        methodInfos = getConstructors();
        for (int i = 0; i < methodInfos.length; i++) {
            MethodInfo methodInfo = methodInfos[i];
            ClassInfo[] classInfos = getClassInfos(methodInfo.getParameters());
            for (int j = 0; j < classInfos.length; j++) {
                ClassInfo classInfo = classInfos[j];
                if (!classInfo.equals(this)) {
                    list.add(classInfo);
                }
            }
            classInfos = getClassInfos(methodInfo.getExceptionParameters());
            for (int j = 0; j < classInfos.length; j++) {
                ClassInfo classInfo = classInfos[j];
                if (!classInfo.equals(this)) {
                    list.add(classInfo);
                }
            }
        }
        methodInfos = getAnnotationMembers();
        for (int i = 0; i < methodInfos.length; i++) {
            MethodInfo methodInfo = methodInfos[i];
            ClassInfo[] classInfos = getClassInfos(new FieldInfo[] { methodInfo.getReturnedParameter() });
            for (int j = 0; j < classInfos.length; j++) {
                ClassInfo classInfo = classInfos[j];
                if (!classInfo.equals(this)) {
                    list.add(classInfo);
                }
            }
        }
        associations = (ClassInfo[]) list.toArray(new ClassInfo[0]);
        return associations;
    }

    protected ClassInfo[] getClassInfos(FieldInfo[] fieldInfos) {
        List list = new ArrayList();
        for (int j = 0; j < fieldInfos.length; j++) {
            FieldInfo fieldInfo = fieldInfos[j];
            ClassInfo[] classInfos = fieldInfo.getClassInfos();
            for (int k = 0; k < classInfos.length; k++) {
                ClassInfo classInfo = classInfos[k];
                if (!classInfo.equals(this)) {
                    list.add(classInfo);
                }
            }
        }
        return (ClassInfo[]) list.toArray(new ClassInfo[0]);
    }

    protected FieldInfo[] enums;

    protected void setEnums(FieldInfo[] enums) {
        this.enums = enums;
    }

    public FieldInfo[] getEnums() {
        if (enums == null) {
            return new FieldInfo[0];
        }
        return enums;
    }

    protected MethodInfo[] annotationMembers;

    protected void setAnnotationMembers(MethodInfo[] annotationMembers) {
        this.annotationMembers = annotationMembers;
    }

    public MethodInfo[] getAnnotationMembers() {
        if (annotationMembers == null) {
            return new MethodInfo[0];
        }
        return annotationMembers;
    }

    protected FieldInfo[] fields;

    protected void setFields(FieldInfo[] fields) {
        this.fields = fields;
    }

    public FieldInfo[] getFields() {
        if (fields == null) {
            return new FieldInfo[0];
        }
        return fields;
    }

    protected MethodInfo[] methods;

    protected void setMethods(MethodInfo[] methods) {
        this.methods = methods;
    }

    public MethodInfo[] getMethods() {
        if (methods == null) {
            return new MethodInfo[0];
        }
        return methods;
    }

    protected MethodInfo[] constructors;

    protected void setConstructors(MethodInfo[] constructors) {
        this.constructors = constructors;
    }

    public MethodInfo[] getConstructors() {
        if (constructors == null) {
            return new MethodInfo[0];
        }
        return constructors;
    }

    public static final int NOT_LOADED_STATE = 0;

    public static final int LOADING_STATE = 1;

    public static final int LOADED_STATE = 2;

    public static final int LOADING_FAILED_STATE = 3;

    protected final Object LOADING_LOCK = new Object();

    protected int loadingState = NOT_LOADED_STATE;

    public int getLoadingState() {
        return loadingState;
    }

    public boolean isLoaded() {
        synchronized (LOADING_LOCK) {
            return loadingState == LOADED_STATE || loadingState == LOADING_FAILED_STATE;
        }
    }

    protected void setLoadingState(int loadingState) {
        this.loadingState = loadingState;
    }

    public void load(boolean isForced) {
        if (isLoaded() && !isForced) {
            return;
        }
        synchronized (LOADING_LOCK) {
            if (loadingState == NOT_LOADED_STATE || loadingState == LOADING_FAILED_STATE) {
                loadingState = LOADING_STATE;
            } else {
                while (loadingState == LOADING_STATE) {
                    try {
                        LOADING_LOCK.wait();
                    } catch (InterruptedException e) {
                    }
                }
                return;
            }
        }
        int index = className.lastIndexOf('.');
        if (index == -1) {
            index = className.length();
        }
        ClassInfoLoader.loadClassInfo(this, classProcessor);
        compositions = null;
        associations = null;
        synchronized (LOADING_LOCK) {
            loadingState = (getModifiers() & Modifiers.LOADING_FAILED) != 0 ? LOADING_FAILED_STATE : LOADED_STATE;
            LOADING_LOCK.notifyAll();
        }
    }

    protected int[] getClassNameIndices(String classDeclaration) {
        return getClassNameIndices(classDeclaration, getGenericDeclarations());
    }

    protected static int[] getClassNameIndices(String classDeclaration, String[] genericDeclarations) {
        int index = classDeclaration.indexOf('<');
        List indexList = new ArrayList();
        if (index >= 0) {
            int count = 1;
            int start = index + 1;
            for (int i = start; count > 0 && i < classDeclaration.length(); i++) {
                int[] indices = null;
                switch(classDeclaration.charAt(i)) {
                    case '<':
                        count++;
                        break;
                    case '>':
                        count--;
                        if (count == 0) {
                            indices = getClassNameIndices(classDeclaration.substring(start, i), genericDeclarations);
                            for (int j = 0; j < indices.length; j++) {
                                indices[j] += start;
                            }
                        }
                        break;
                    case '&':
                        if (count == 1) {
                            indices = getClassNameIndices(classDeclaration.substring(start, i - 1), genericDeclarations);
                            for (int j = 0; j < indices.length; j++) {
                                indices[j] += start;
                            }
                            i++;
                            start = i + 1;
                        }
                        break;
                    case ',':
                        if (count == 1) {
                            indices = getClassNameIndices(classDeclaration.substring(start, i), genericDeclarations);
                            for (int j = 0; j < indices.length; j++) {
                                indices[j] += start;
                            }
                            start = i + 1;
                        }
                        break;
                }
                if (indices != null) {
                    for (int j = 0; j < indices.length; j++) {
                        indexList.add(new Integer(indices[j]));
                    }
                    indices = null;
                }
            }
            classDeclaration = classDeclaration.substring(0, index);
        } else {
            index = classDeclaration.indexOf('[');
            if (index >= 0) {
                classDeclaration = classDeclaration.substring(0, index);
            }
        }
        int offset = classDeclaration.lastIndexOf(" ") + 1;
        classDeclaration = classDeclaration.substring(offset);
        if (classDeclaration.endsWith("...")) {
            classDeclaration = classDeclaration.substring(0, classDeclaration.length() - "...".length());
        }
        if ("void".equals(classDeclaration) || "?".equals(classDeclaration) || "boolean".equals(classDeclaration) || "byte".equals(classDeclaration) || "char".equals(classDeclaration) || "short".equals(classDeclaration) || "int".equals(classDeclaration) || "long".equals(classDeclaration) || "float".equals(classDeclaration) || "double".equals(classDeclaration)) {
        } else {
            boolean isGeneric = false;
            for (int i = 0; i < genericDeclarations.length; i++) {
                if (classDeclaration.equals(genericDeclarations[i])) {
                    isGeneric = true;
                    break;
                }
            }
            if (!isGeneric) {
                indexList.add(0, new Integer(offset));
                indexList.add(1, new Integer(offset + classDeclaration.length()));
            }
        }
        int[] indices = new int[indexList.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = ((Integer) indexList.get(i)).intValue();
        }
        return indices;
    }

    public ClassProcessor getClassProcessor() {
        return classProcessor;
    }

    protected String prototype;

    protected void setPrototype(String prototype) {
        this.prototype = prototype;
    }

    public String getPrototype() {
        return prototype;
    }
}
