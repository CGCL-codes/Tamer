package org.granite.generator.ant;

public class PackageTranslator {

    private String java = null;

    private String as3 = null;

    public String getJava() {
        return java;
    }

    public void setJava(String java) {
        this.java = java;
    }

    public String getAs3() {
        return as3;
    }

    public void setAs3(String as3) {
        this.as3 = as3;
    }

    public boolean isValid() {
        return java != null && java.length() > 0 && as3 != null && as3.length() > 0;
    }

    @Override
    public String toString() {
        return "<translator java=\"" + java + "\" as=\"" + as3 + "\" />";
    }
}
