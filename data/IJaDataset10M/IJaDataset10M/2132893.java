package net.jwde;

import java.net.URL;
import net.jwde.extractor.ExtractorManager;
import org.jdom.Document;

public interface InfoExtractor {

    public Document extract(URL inputURL, URL transformURL, URL outputURL);

    public Document extract(URL configURL);

    public ExtractorManager getExtractorManager();
}
