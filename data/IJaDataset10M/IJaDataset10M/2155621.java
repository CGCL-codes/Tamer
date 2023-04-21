package com.ark.fix.model;

public interface Tag {

    public String getTag();

    public int getTagDataType();

    public String[] getTagValueEnum();

    public String getTagValue();

    public void setTagValue(String s) throws ModelException;
}
