package de.cinek.rssview;

/**
 * @author saintedlama
 */
public class TransformerFactory {

    private static HtmlTransformer instance;

    /**
	 * Creates a HtmlTransformer depending on article. Currently only 
	 * instances of DefaultHtmlTransformer are returned.
	 * @param article
	 * @return HtmlTransformer
	 */
    public static HtmlTransformer createHtmlTransformer(Article article) {
        if (instance == null) {
            instance = new DefaultHtmlTransformer();
        }
        return instance;
    }
}
