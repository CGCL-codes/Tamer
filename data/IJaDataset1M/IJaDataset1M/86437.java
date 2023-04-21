package com.solium.annotationprocessor.gwtrpc;

import com.solium.annotation.gwtrpc.GwtRpcServlet;
import com.solium.annotationprocessor.AnnotatedMethodInfo;
import com.solium.annotationprocessor.AnnotationProcessorUtil;
import com.solium.annotationprocessor.gwtrpc.config.GwtRpcAnnotationConfig;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class GwtRpcMethodAnnotationHandler {

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected final ProcessingEnvironment _env;

    protected final GwtRpcAnnotationConfig _config;

    protected final Messager _messager;

    public GwtRpcMethodAnnotationHandler(GwtRpcAnnotationConfig config, ProcessingEnvironment env) {
        _config = config;
        _env = env;
        _messager = env.getMessager();
    }

    public void process(Set<? extends Element> annotatedMethods) {
        log.fine("process: entry");
        AnnotatedMethodInfo annotatedMethodInfo = new AnnotatedMethodInfo(annotatedMethods, Collections.<Class<? extends Annotation>>singleton(GwtRpcServlet.class));
        for (AnnotatedMethodInfo.ClassInfo classInfo : annotatedMethodInfo.getNonAbstractClasses()) {
            createInterface(classInfo, false);
            createInterface(classInfo, true);
        }
        log.fine("process: exit");
    }

    protected String stripImplSuffix(String classSimpleName) {
        String stripped;
        if (classSimpleName.endsWith("Impl")) {
            stripped = classSimpleName.substring(0, classSimpleName.lastIndexOf("Impl"));
        } else {
            throw new RuntimeException("Unexpected name: " + classSimpleName + " should end with ServiceImpl");
        }
        return stripped;
    }

    protected String getPackageNameOfGenClass(TypeElement classElement, boolean isAsync) {
        String packageName;
        try {
            String implPackageName = ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();
            packageName = _config.getGeneratedClassPackage(implPackageName);
        } catch (ClassCastException e) {
            throw new RuntimeException("Not a package:" + classElement.getEnclosingElement());
        }
        return packageName;
    }

    protected String getSimpleNameOfGenClass(TypeElement classElement, boolean isAsync) {
        String origClassSimpleName = classElement.getSimpleName().toString();
        String origClassNameBase = stripImplSuffix(origClassSimpleName);
        String newClassSimpleName = origClassNameBase;
        if (isAsync) {
            newClassSimpleName += "Async";
        }
        return newClassSimpleName;
    }

    protected String getQualifiedNameOfGenClass(TypeElement classElement, boolean isAsync) {
        String packageName = getPackageNameOfGenClass(classElement, isAsync);
        String simpleNameOfNewClass = getSimpleNameOfGenClass(classElement, isAsync);
        return packageName + "." + simpleNameOfNewClass;
    }

    protected void createInterface(AnnotatedMethodInfo.ClassInfo classInfo, boolean isAsync) {
        TypeElement classElement = classInfo.getClassElement();
        List<ExecutableElement> methodElementList = classInfo.getAnnotatedMethods(true);
        if (methodElementList.isEmpty()) {
            return;
        }
        GwtRpcServlet gwtRpcServlet = classElement.getAnnotation(GwtRpcServlet.class);
        String packageNameOfGenClass = getPackageNameOfGenClass(classElement, isAsync);
        String simpleNameOfGenClass = getSimpleNameOfGenClass(classElement, isAsync);
        String qualifiedNameOfGenClass = getQualifiedNameOfGenClass(classElement, isAsync);
        log.fine("Processing: " + simpleNameOfGenClass);
        List<String> interfaceQualifiedNames = new ArrayList<String>();
        for (TypeMirror currInterface : classElement.getInterfaces()) {
            DeclaredType currInterfaceDeclaredType = (DeclaredType) currInterface;
            TypeElement currInterfaceTypeElement = (TypeElement) currInterfaceDeclaredType.asElement();
            if (currInterfaceTypeElement.getSimpleName().toString().equals(getSimpleNameOfGenClass(classElement, false)) || currInterfaceTypeElement.getSimpleName().toString().equals(getSimpleNameOfGenClass(classElement, true))) {
                continue;
            }
            String interfaceQualifiedName = getInterfaceQualifiedName(currInterfaceDeclaredType);
            if (interfaceQualifiedName.endsWith("Service")) {
                interfaceQualifiedNames.add(interfaceQualifiedName + (isAsync ? "Async" : ""));
            }
        }
        log.fine("Attempting to create:" + qualifiedNameOfGenClass);
        PrintWriter srcOut = null;
        try {
            srcOut = new PrintWriter(new BufferedWriter(_env.getFiler().createSourceFile(qualifiedNameOfGenClass, classElement).openWriter()));
            srcOut.println("package " + packageNameOfGenClass + ";");
            srcOut.println("");
            for (String additionalImportedClass : _config.getAdditionalImportedClasses(isAsync)) {
                srcOut.println("import " + additionalImportedClass + ";");
            }
            if (!isAsync) {
                srcOut.println("import com.google.gwt.user.client.rpc.RemoteService;");
                srcOut.println("import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;");
                srcOut.println("");
                srcOut.println("//autogenerated");
                if ((gwtRpcServlet.remoteServiceRelativePath() != null) && (!"".equals(gwtRpcServlet.remoteServiceRelativePath()))) {
                    srcOut.println("@RemoteServiceRelativePath(\"" + gwtRpcServlet.remoteServiceRelativePath() + "\")");
                }
                srcOut.println("public interface " + simpleNameOfGenClass);
                interfaceQualifiedNames.add("RemoteService");
            } else {
                srcOut.println("import com.google.gwt.user.client.rpc.AsyncCallback;");
                srcOut.println("");
                srcOut.println("//autogenerated");
                srcOut.println("public interface " + simpleNameOfGenClass);
            }
            if (!interfaceQualifiedNames.isEmpty()) {
                srcOut.println(" extends ");
                srcOut.println(AnnotationProcessorUtil.createCommaDelimitedList(interfaceQualifiedNames));
            }
            srcOut.println(" {");
            srcOut.append(_config.getAdditionalGeneratedCode(classElement.getSimpleName().toString(), simpleNameOfGenClass, isAsync));
            Collections.sort(methodElementList, new Comparator<ExecutableElement>() {

                public int compare(ExecutableElement o1, ExecutableElement o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
            for (ExecutableElement currMethod : methodElementList) {
                if (!isAsync) {
                    srcOut.println(getMethodSignature(currMethod));
                } else {
                    srcOut.println(getMethodSignatureAsync(currMethod));
                }
                srcOut.println("");
            }
            srcOut.println("}");
        } catch (java.io.IOException e) {
            throw new RuntimeException("Factory already exists or cannot be created");
        } finally {
            if (srcOut != null) {
                srcOut.close();
            }
        }
    }

    protected String getInterfaceQualifiedName(DeclaredType currInterfaceDeclaredType) {
        TypeElement currInterfaceTypeElement = (TypeElement) currInterfaceDeclaredType.asElement();
        List<String> genericTypes = new ArrayList<String>();
        for (TypeMirror currTypeMirror : currInterfaceDeclaredType.getTypeArguments()) {
            DeclaredType currDeclaredType = (DeclaredType) currTypeMirror;
            genericTypes.add(((TypeElement) currDeclaredType.asElement()).getQualifiedName().toString());
        }
        StringBuilder interfaceQualifiedName = new StringBuilder();
        interfaceQualifiedName.append(currInterfaceTypeElement.getQualifiedName().toString());
        if (!genericTypes.isEmpty()) {
            interfaceQualifiedName.append('<');
            interfaceQualifiedName.append(AnnotationProcessorUtil.createCommaDelimitedList(genericTypes));
            interfaceQualifiedName.append('>');
        }
        return interfaceQualifiedName.toString();
    }

    protected String getMethodSignature(ExecutableElement currMethod) {
        StringBuilder ret = new StringBuilder(100);
        ret.append("    " + currMethod.getReturnType().toString() + " " + currMethod.getSimpleName());
        ret.append("(");
        List<String> methodParameterClassesAndNames = getMethodParameterClassesAndNames(currMethod);
        ret.append(AnnotationProcessorUtil.createCommaDelimitedList(methodParameterClassesAndNames));
        ret.append(") ");
        List<String> exceptionNames = new ArrayList<String>();
        for (TypeMirror currException : currMethod.getThrownTypes()) {
            exceptionNames.add(currException.toString());
        }
        exceptionNames.addAll(_config.getAdditionalExceptionsForAllMethods());
        if (!exceptionNames.isEmpty()) {
            ret.append(" throws ");
            ret.append(AnnotationProcessorUtil.createCommaDelimitedList(exceptionNames));
        }
        ret.append(";");
        return ret.toString();
    }

    protected String getMethodSignatureAsync(ExecutableElement currMethod) {
        StringBuilder ret = new StringBuilder(100);
        ret.append("    void " + currMethod.getSimpleName());
        ret.append("(");
        List<String> methodParameterClassesAndNames = getMethodParameterClassesAndNames(currMethod);
        methodParameterClassesAndNames.add(getAsyncClassName(currMethod) + " async");
        ret.append(AnnotationProcessorUtil.createCommaDelimitedList(methodParameterClassesAndNames));
        ret.append(") ");
        ret.append(";");
        return ret.toString();
    }

    protected List<String> getMethodParameterClassesAndNames(ExecutableElement currMethod) {
        List<String> methodParameterNames = new ArrayList<String>();
        for (VariableElement currParameter : currMethod.getParameters()) {
            TypeMirror paramType = currParameter.asType();
            methodParameterNames.add(paramType.toString() + " " + currParameter.getSimpleName());
        }
        return methodParameterNames;
    }

    protected String getAsyncClassName(ExecutableElement currMethod) {
        TypeMirror returnType = currMethod.getReturnType();
        String asyncClass;
        switch(returnType.getKind()) {
            case BOOLEAN:
                asyncClass = "AsyncCallback<Boolean>";
                break;
            case BYTE:
                asyncClass = "AsyncCallback<Byte>";
                break;
            case SHORT:
                asyncClass = "AsyncCallback<Short>";
                break;
            case INT:
                asyncClass = "AsyncCallback<Integer>";
                break;
            case LONG:
                asyncClass = "AsyncCallback<Long>";
                break;
            case CHAR:
                asyncClass = "AsyncCallback<Char>";
                break;
            case FLOAT:
                asyncClass = "AsyncCallback<Float>";
                break;
            case DOUBLE:
                asyncClass = "AsyncCallback<Double>";
                break;
            case VOID:
                asyncClass = "AsyncCallback";
                break;
            default:
                asyncClass = "AsyncCallback<" + currMethod.getReturnType().toString() + ">";
                break;
        }
        return asyncClass;
    }
}
