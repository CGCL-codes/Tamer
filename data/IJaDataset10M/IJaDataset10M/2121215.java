package utoopia.content.html;

/**
 * Representa una cláusula META de un documento html
 * @author Jose
 *
 */
public class Meta extends Element {

    private String httpEquiv;

    private String content;

    /**
	 * Constructor
	 * @param httpEquiv Valor del atributo "httpequiv"
	 * @param content Valor del atributo "content"
	 */
    public Meta(String httpEquiv, String content) {
        super();
        this.httpEquiv = httpEquiv;
        this.content = content;
    }

    @Override
    protected String getHTMLContent() {
        return "";
    }

    @Override
    protected void setAttributes() {
        addAttribute("http-equiv", httpEquiv);
        addAttribute("content", content);
    }

    @Override
    protected String setTagName() {
        return "meta";
    }
}
