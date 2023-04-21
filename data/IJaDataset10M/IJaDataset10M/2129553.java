package ca.ucalgary.cpsc.ebe.fitClipse.render.html;

public class HtmlPageFactory {

    public HtmlPage newPage() {
        return new HtmlPage();
    }

    public String toString() {
        return getClass().getName();
    }
}
