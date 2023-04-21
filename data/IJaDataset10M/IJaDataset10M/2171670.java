package com.once.servicescout.parser.query.impl;

import com.once.servicescout.parser.query.SingleQoSQuery;

/**
 *
 * @author cwchen
 * @Created 2008-1-18
 * @Contact comain@gmail.com
 */
public class SingleQoSQueryImpl implements SingleQoSQuery {

    String attrName;

    String lowerValue;

    String upperValue;

    boolean lowerInclusion;

    boolean upperInclusion;

    public SingleQoSQueryImpl(String attr, String lower, String upper, boolean lowerI, boolean upperI) {
        this.attrName = attr;
        this.lowerValue = lower;
        this.upperValue = upper;
        this.lowerInclusion = lowerI;
        this.upperInclusion = upperI;
    }

    public String getAttrName() {
        return attrName;
    }

    public String getLowerValue() {
        return lowerValue;
    }

    public boolean getLowerInclusion() {
        return lowerInclusion;
    }

    public String getUpperValue() {
        return upperValue;
    }

    public boolean getUpperInclusion() {
        return upperInclusion;
    }
}
