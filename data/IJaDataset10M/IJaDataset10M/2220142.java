package com.mockturtlesolutions.snifflib.graphics;

import java.awt.Color;
import java.awt.Font;

public interface SLAnnotationPreferences extends SLPreferences {

    public Color getDefaultAnnotationColor(String repos);

    public Font getDefaultAnnotationFont(String repos);

    public String getDefaultAnnotationRenderer(String repos);
}
