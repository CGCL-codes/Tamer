package com.xith3d.utility.properties;

import org.apache.log4j.Category;

public interface PropertyInterface {

    public static final Category LOG = Category.getInstance(PropertyInterface.class.getName());

    String convertToString();

    void convertFromString(String text);

    String getName();

    String getComment();
}
