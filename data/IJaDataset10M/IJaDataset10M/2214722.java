package model;

import java.awt.Color;

public class Transform {

    public float vFaktor;

    public float widthFaktor;

    public float phi;

    public boolean luecke;

    public float phiFaktor;

    public Color circleColor;

    public void reset() {
        vFaktor = 1;
        widthFaktor = 1;
        phi = 0;
        luecke = false;
        phiFaktor = 1;
        circleColor = Color.YELLOW;
    }
}
