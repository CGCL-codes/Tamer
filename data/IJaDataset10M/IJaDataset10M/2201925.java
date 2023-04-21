package com.once.servicescout.parser.query.impl;

import com.once.servicescout.parser.query.InputQuery;

/**
 *
 * @author cwchen
 * @Created 2008-1-18
 * @Contact comain@gmail.com
 */
public class InputQueryImpl implements InputQuery {

    String func;

    String qos;

    float[] pref;

    int resultNum;

    public InputQueryImpl(String func, String qos, float[] pref, int resultNum) {
        this.func = func;
        this.qos = qos;
        this.pref = pref;
        this.resultNum = resultNum;
    }

    public String getFuncPart() {
        return func;
    }

    public String getQoSPart() {
        return qos;
    }

    public float[] getQoSPref() {
        return pref;
    }

    public int getResultNum() {
        return resultNum;
    }
}
