package org.jtools.siterenderer.layout;

public interface Layouted {

    String getStyle();

    String getStyleId();

    String getStyleClass();

    String toIdLiteral();

    String toClassLiteral();
}
