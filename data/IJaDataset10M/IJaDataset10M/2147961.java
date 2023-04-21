package com.javector.adaptive.util.dto;

import javax.xml.namespace.QName;
import com.javector.adaptive.framework.interfaces.Mapping;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import java.util.List;
import java.util.Map;
import java.util.Collection;

public class SOAJOperationDTO {

    public enum JavaMethodTypeEnum {

        POJO, EJB21, EJB30, UNKNOWN
    }

    private String targetServiceClassName;

    private String targetServiceMethodName;

    private List<XMLMapping> paramMapping;

    private List<String> paramClasses;

    private String portName;

    private String endpoint;

    private String serviceName;

    private String operationName;

    private String targetNameSpace;

    private JavaMethodTypeEnum methodType;

    private String classpathReference;

    private String jndiName;

    private String homeInterface;

    private Boolean local;

    private List<String> inputParamTypes;

    private QName returnTypeName;

    private String returnJavaClass;

    private QName returnType;

    public QName getReturnTypeName() {
        return returnTypeName;
    }

    public void setReturnTypeName(QName returnTypeName) {
        this.returnTypeName = returnTypeName;
    }

    public List<String> getInputParamTypes() {
        return inputParamTypes;
    }

    public void setInputParamTypes(List<String> inputParamTypes) {
        this.inputParamTypes = inputParamTypes;
    }

    public String getTargetNameSpace() {
        return targetNameSpace;
    }

    public void setTargetNameSpace(String targetNameSpace) {
        this.targetNameSpace = targetNameSpace;
    }

    public String getReturnJavaClass() {
        return returnJavaClass;
    }

    public void setReturnJavaClass(String returnJavaClass) {
        this.returnJavaClass = returnJavaClass;
    }

    public QName getReturnType() {
        return returnType;
    }

    public void setReturnType(QName returnType) {
        this.returnType = returnType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getTargetServiceClassName() {
        return targetServiceClassName;
    }

    public String getTargetServiceMethodName() {
        return targetServiceMethodName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public void setTargetServiceClassName(String targetServiceClassName) {
        this.targetServiceClassName = targetServiceClassName;
    }

    public void setTargetServiceMethodName(String targetServiceMethodName) {
        this.targetServiceMethodName = targetServiceMethodName;
    }

    public List<XMLMapping> getParamMapping() {
        return paramMapping;
    }

    public void setParamMapping(List<XMLMapping> paramMapping) {
        this.paramMapping = paramMapping;
    }

    public JavaMethodTypeEnum getMethodType() {
        return methodType;
    }

    public void setMethodType(JavaMethodTypeEnum methodType) {
        this.methodType = methodType;
    }

    public String getClasspathReference() {
        return classpathReference;
    }

    public void setClasspathReference(String classpathReference) {
        this.classpathReference = classpathReference;
    }

    public String getHomeInterface() {
        return homeInterface;
    }

    public void setHomeInterface(String homeInterface) {
        this.homeInterface = homeInterface;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public List<String> getParamClasses() {
        return paramClasses;
    }

    public void setParamClasses(List<String> paramNames) {
        this.paramClasses = paramNames;
    }
}
