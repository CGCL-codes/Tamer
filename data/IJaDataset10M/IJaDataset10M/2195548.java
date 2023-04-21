package net.sourceforge.mededis.model;

import java.util.*;

/**
 * Container for a related set of code values.
 * The module refers to e.g people, central - and is used for
 * categorizing codesets by module
 */
public class CodeSet extends MDObject {

    private String module;

    private String name;

    private List codeValues;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized List getCodeValues() {
        return codeValues;
    }

    public void setCodeValues(List codeValues) {
        this.codeValues = codeValues;
    }

    public synchronized void add(CodeValue value) {
        if (codeValues == null) codeValues = new ArrayList();
        value.setCodeSet(this);
        codeValues.add(value);
    }

    public String toString() {
        String values = "";
        if (codeValues != null) {
            Iterator iter = codeValues.iterator();
            while (iter.hasNext()) {
                CodeValue code = (CodeValue) iter.next();
                values += code.toString();
            }
        }
        return "CodeSet with: Id: " + getId() + " Name:  " + name + " module: " + module + " codeValues " + values;
    }
}
