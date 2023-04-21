package org.zkforge.zkzultest;

/**
 * @author Dennis.Chen
 *
 */
public class RunResult {

    boolean identical = false;

    String sample1;

    String sample2;

    String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSample1() {
        return sample1;
    }

    public void setSample1(String sample1) {
        this.sample1 = sample1;
    }

    public String getSample2() {
        return sample2;
    }

    public void setSample2(String sample2) {
        this.sample2 = sample2;
    }

    public boolean isIdentical() {
        return identical;
    }

    public void setIdentical(boolean identical) {
        this.identical = identical;
    }
}
