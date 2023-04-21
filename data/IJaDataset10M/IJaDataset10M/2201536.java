package org.dozer.vo;

import org.dozer.vo.deep.SrcNestedDeepObj;

public class OneWayObject extends BaseTestObject {

    private String oneWayField;

    private SrcNestedDeepObj nested;

    private String stringToList;

    private String setOnly = "setOnly";

    public String getOneWayField() {
        return oneWayField;
    }

    public void setOneWayField(String oneWayField) {
        this.oneWayField = oneWayField;
    }

    public SrcNestedDeepObj getNested() {
        return nested;
    }

    public void setNested(SrcNestedDeepObj nested) {
        this.nested = nested;
    }

    public String getStringToList() {
        return stringToList;
    }

    public void setStringToList(String stringToList) {
        this.stringToList = stringToList;
    }

    public String getSetOnly() {
        return setOnly;
    }
}
