package it.pronetics.madstore.crawler.downloader.impl;

import it.pronetics.madstore.crawler.downloader.Downloader;
import it.pronetics.madstore.crawler.model.Link;
import it.pronetics.madstore.crawler.model.Page;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link it.pronetics.madstore.crawler.downloader.Downloader} implementation.
 *
 * @author Salvatore Incandela
 * @author Sergio Bossa
 */
public class DownloaderImpl implements Downloader {

    private static final Logger LOG = LoggerFactory.getLogger(DownloaderImpl.class);

    public Page download(Link link) {
        String url = link.getLink();
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        try {
            LOG.info("Downloading page from: {}", url);
            int status = client.executeMethod(method);
            if (status == HttpStatus.SC_OK) {
                String data = method.getResponseBodyAsString();
                return new Page(link, data);
            } else {
                return new Page(link);
            }
        } catch (Exception e) {
            LOG.warn("Download failed: {}", url);
            LOG.warn(e.getMessage());
            LOG.debug(e.getMessage(), e);
            return new Page(link);
        } finally {
            method.releaseConnection();
        }
    }
}
