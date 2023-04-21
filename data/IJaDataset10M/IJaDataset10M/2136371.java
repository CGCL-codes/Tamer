package com.mockturtlesolutions.snifflib.graphics;

import java.awt.Color;
import java.awt.Font;

public interface SLAxesPreferences extends SLPreferences {

    public Color getDefaultAxesBackgroundColor(String repos);

    public Color getDefaultAxesTitleColor(String repos);

    public Color getDefaultAxesColor(String repos);

    public Color getDefaultAxesXLabelColor(String repos);

    public Color getDefaultAxesYLabelColor(String repos);

    public Color getDefaultAxesZLabelColor(String repos);

    public int getDefaultAxesLineThickness(String repos);

    public Font getDefaultAxesXLabelFont(String repos);

    public Font getDefaultAxesYLabelFont(String repos);

    public Font getDefaultAxesZLabelFont(String repos);

    public Font getDefaultAxesFont(String repos);

    public Font getDefaultAxesTitleFont(String repos);

    public boolean getDefaultAxesOn(String repos);

    public boolean getDefaultAxesGridOn(String repos);

    public boolean getDefaultAxesBoxOn(String repos);
}
