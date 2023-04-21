package ounl.otec.mace.contextserver.content;

import java.util.Vector;

public class ContentHandlerFactory {

    private Vector<IContentHandler> m_contentHandlers = new Vector<IContentHandler>();

    /**
	 * 
	 *
	 */
    public ContentHandlerFactory() {
        try {
            m_contentHandlers.add(new WordpressContentHandler());
            m_contentHandlers.add(new FlickrContentHandler());
            m_contentHandlers.add(new UriContentHandler());
            m_contentHandlers.add(new LanguageContentHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * @return
	 */
    public Vector<IContentHandler> getContentHandlers() {
        return m_contentHandlers;
    }
}
