package com.joes.imageutils;

import com.joes.categorizer.CategorizeableString;

public class ImageDescription {

    public enum type {

        RGB, H
    }

    private type myType;

    private String[] descriptors;

    public ImageDescription() {
    }

    public type getMyType() {
        return myType;
    }

    public void setMyType(type myType) {
        this.myType = myType;
    }

    public String[] getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(String[] descriptors) {
        this.descriptors = descriptors;
    }

    public String toString() {
        StringBuffer myRet = new StringBuffer();
        for (int i = 0; i < descriptors.length; i++) {
            myRet.append(descriptors[i]);
            myRet.append("\n");
        }
        return myRet.toString();
    }

    public CategorizeableString getCategorizable() {
        CategorizeableString myCategorizable = new CategorizeableString();
        myCategorizable.setData(getDescriptors());
        return myCategorizable;
    }
}
