package com.em.validation.rebind.metadata;

public class ConstraintPropertyMetadata {

    private String returnType = null;

    private String importType = null;

    private String returnValue = "null";

    private String methodName = "";

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String fullReturnType) {
        this.importType = fullReturnType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConstraintPropertyMetadata other = (ConstraintPropertyMetadata) obj;
        if (methodName == null) {
            if (other.methodName != null) return false;
        } else if (!methodName.equals(other.methodName)) return false;
        return true;
    }
}
