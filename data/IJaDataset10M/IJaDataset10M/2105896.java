package tdProbleme;

/**
 *
 * @author boomar
 */
public class Line {

    private String[] lineWords;

    public Line() {
    }

    public Line(String[] lineWords) {
        this.lineWords = lineWords;
    }

    public String[] getLineWords() {
        return lineWords;
    }

    public void setLineWords(String[] lineWords) {
        this.lineWords = lineWords;
    }

    @Override
    public String toString() {
        return "Line{" + "lineWords=" + lineWords + '}';
    }
}
