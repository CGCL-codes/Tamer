package zuilib.components;

import processing.core.PConstants;

public class RectCheckbox extends checkbox {

    public RectCheckbox(String sname, float fx, float fy, String stext, float fsize) {
        super(sname, fx, fy, stext, fsize);
    }

    public void draw() {
        Color.doBackground();
        graphic.rectMode(PConstants.CORNER);
        graphic.rect(0, 3, dimension.height, dimension.height);
        Color.doForeground();
        if (checked) {
            graphic.rect(2, 5, dimension.height - 4, dimension.height - 4);
        }
        text.draw();
    }
}
