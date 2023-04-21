package net.sourceforge.plantuml.ugraphic;

public interface UDriver<O> {

    public void draw(UShape shape, double x, double y, ColorMapper mapper, UParam param, O object);
}
