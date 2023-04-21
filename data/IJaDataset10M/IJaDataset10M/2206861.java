package org.nms.spider.helpers.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nms.spider.beans.IElement;
import org.nms.spider.beans.impl.TypedElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Url processor using JSoup utilities.
 * @author daviz
 *
 */
public class JSoupUrlProcessorImpl extends UrlProcessorImpl {

    /**
	 * Logger.
	 */
    private static final Logger log = LoggerFactory.getLogger(JSoupUrlProcessorImpl.class);

    @Override
    public List<IElement> process(List<IElement> elements) {
        ArrayList<IElement> result = new ArrayList<IElement>();
        for (IElement el : elements) {
            String urlString = (String) el.getElement();
            log.info("Preprocessing url : " + urlString);
            TypedElement te = new TypedElement();
            te.setId(urlString);
            te.setType("htmlcontent");
            try {
                Document doc = Jsoup.connect(urlString).timeout(this.getConnectionTimeOut()).get();
                te.setElement(doc.toString());
                result.add(te);
            } catch (IOException ioe) {
                log.error("Error processing url {}", urlString);
                log.error("Exception : ", ioe);
            }
        }
        return result;
    }
}
