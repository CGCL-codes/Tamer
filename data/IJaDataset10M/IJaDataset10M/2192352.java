package net.sourceforge.plantuml.skin;

public class SimpleContext2D implements Context2D {

    private final boolean isBackground;

    private final boolean withShadow;

    public SimpleContext2D(boolean isBackground) {
        this(isBackground, false);
    }

    public SimpleContext2D(boolean isBackground, boolean withShadow) {
        this.isBackground = isBackground;
        this.withShadow = withShadow;
    }

    public boolean isBackground() {
        return isBackground;
    }

    public boolean withShadow() {
        return withShadow;
    }
}
