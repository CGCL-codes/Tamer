package jaguar;

/**
 * @author peter
 *
 */
public class JaguarLabel {

    private String name;

    private int line;

    private String text;

    public JaguarLabel(String name, String text) {
        this.name = name;
        this.text = text;
        try {
            line = Integer.parseInt(text, 10);
        } catch (NumberFormatException e) {
            line = 0;
        }
    }

    /**
	 * @return the line
	 */
    public int getLine() {
        return line;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param line the line to set
	 */
    public void setLine(int line) {
        this.line = line;
        this.text = String.valueOf(line);
    }

    public void setText(String text) {
        this.text = text;
        if (name != null) {
            try {
                line = Integer.parseInt(text, 10);
            } catch (NumberFormatException e) {
                line = 0;
            }
        }
    }

    /**
	 * @return the text
	 */
    public String getText() {
        return text;
    }

    public String toString() {
        return ("label " + name + " " + line + " " + text).trim();
    }
}
