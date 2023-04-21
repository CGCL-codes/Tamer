package ca.ucalgary.cpsc.ebe.fitClipse.render.html;

public class RawHtml extends HtmlElement {

    private String html;

    public RawHtml(String html) {
        this.html = html;
    }

    public String html() {
        return html;
    }
}
