package com.google.gwt.inject.client.method;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public interface Ellipse {

    @Inject
    void setColor(@Named("color") String color);

    String getColor();
}
