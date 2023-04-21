package colors;

public class ColorGameBean {

    private String background = "yellow";

    private String foreground = "red";

    private String color1 = foreground;

    private String color2 = background;

    private String hint = "no";

    private int attempts = 0;

    private int intval = 0;

    private boolean tookHints = false;

    public void processRequest() {
        if (!color1.equals(foreground)) {
            if (color1.equalsIgnoreCase("black") || color1.equalsIgnoreCase("cyan")) {
                background = color1;
            }
        }
        if (!color2.equals(background)) {
            if (color2.equalsIgnoreCase("black") || color2.equalsIgnoreCase("cyan")) {
                foreground = color2;
            }
        }
        attempts++;
    }

    public void setColor2(String x) {
        color2 = x;
    }

    public void setColor1(String x) {
        color1 = x;
    }

    public void setAction(String x) {
        if (!tookHints) tookHints = x.equalsIgnoreCase("Hint");
        hint = x;
    }

    public String getColor2() {
        return background;
    }

    public String getColor1() {
        return foreground;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean getHint() {
        return hint.equalsIgnoreCase("Hint");
    }

    public boolean getSuccess() {
        if (background.equalsIgnoreCase("black") || background.equalsIgnoreCase("cyan")) {
            if (foreground.equalsIgnoreCase("black") || foreground.equalsIgnoreCase("cyan")) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean getHintTaken() {
        return tookHints;
    }

    public void reset() {
        foreground = "red";
        background = "yellow";
    }

    public void setIntval(int value) {
        intval = value;
    }

    public int getIntval() {
        return intval;
    }
}
