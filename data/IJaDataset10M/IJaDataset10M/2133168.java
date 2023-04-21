package net.ar.guia.own.interfaces;

public interface ProgressBar extends VisualComponent {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    public int getMaximum();

    public int getOrientation();

    public String getText();

    public int getValue();

    public void setMaximum(int maximum);

    public void setOrientation(int orientation);

    public void setText(String text);

    public void setValue(int value);
}
