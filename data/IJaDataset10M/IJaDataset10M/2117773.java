package com.armatiek.infofuze.source.filesystem.webcrawl;

import java.io.Reader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import com.armatiek.infofuze.error.InfofuzeException;
import com.armatiek.infofuze.source.filesystem.HTTPBasedSource;
import com.armatiek.infofuze.stream.filesystem.FileIf;
import com.armatiek.infofuze.stream.filesystem.webcrawl.CrawlState;
import com.armatiek.infofuze.stream.filesystem.webcrawl.HTTPFile;
import com.armatiek.infofuze.stream.filesystem.webcrawl.WebCrawlReader;
import com.armatiek.infofuze.utils.CommonUtils;
import com.armatiek.infofuze.utils.XPathUtils;

/**
 * JAXP based Source class that can be used to stream an XML representation of
 * the result of a website spider/crawl.
 * 
 * @author Maarten Kroon
 */
public class WebCrawlSource extends HTTPBasedSource {

    protected boolean followImages;

    protected boolean followScripts;

    protected boolean followLinks;

    protected int maxDepth;

    protected int wait;

    public WebCrawlSource(Element configElem) {
        super(configElem);
        XPath xpath = XPathFactory.newInstance().newXPath();
        maxDepth = XPathUtils.getIntegerValue(xpath, "@maxDepth", configElem, 5);
        wait = XPathUtils.getIntegerValue(xpath, "@wait", configElem, 500);
        followImages = XPathUtils.getBooleanValue(xpath, "@followImages", configElem, false);
        followScripts = XPathUtils.getBooleanValue(xpath, "@followScripts", configElem, false);
        followLinks = XPathUtils.getBooleanValue(xpath, "@followLinks", configElem, false);
    }

    @Override
    public FileIf getFile(String location) {
        try {
            CrawlState crawlState = new CrawlState(location, maxDepth, wait, followImages, followScripts, followLinks);
            return new HTTPFile(httpClient, location, 0, crawlState);
        } catch (Exception e) {
            throw new InfofuzeException("Could not create HTTPFile for \"" + location + "\"");
        }
    }

    @Override
    public Reader getInternalReader() {
        if (!isOpen) {
            throw new InfofuzeException("Could not get WebCrawlReader; Source \"" + name + "\" is not open");
        }
        return new WebCrawlReader(getBaseFiles(), fileExtractors, transformMode, CommonUtils.getLastTransformed(transformId), docTypeSystemId, docTypePublicId);
    }
}
