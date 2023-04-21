package fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp;

public class Ocl4tstTokenStyle implements fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstTokenStyle {

    private int[] color;

    private int[] backgroundColor;

    private boolean bold;

    private boolean italic;

    private boolean strikethrough;

    private boolean underline;

    public Ocl4tstTokenStyle(int[] color, int[] backgroundColor, boolean bold, boolean italic, boolean striketrough, boolean underline) {
        super();
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.bold = bold;
        this.italic = italic;
        this.strikethrough = striketrough;
        this.underline = underline;
    }

    public int[] getColorAsRGB() {
        return color;
    }

    public int[] getBackgroundColorAsRGB() {
        return backgroundColor;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public boolean isUnderline() {
        return underline;
    }
}
