package it.unimib.disco.itis.polimar.pcm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class Nfp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String operator;

    private Set<String> parametersSet = new HashSet<String>();

    private String superClass;

    private String unit;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setParameter(String p) {
        parametersSet.add(p);
    }

    public Set<String> getParametersSet() {
        return parametersSet;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public String toString() {
        return name;
    }

    public void setParameters(Set<String> parameters) {
        parametersSet = parameters;
    }
}
