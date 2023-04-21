package org.archive.crawler.restws;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.archive.crawler.client.Crawler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class CrawlerResource extends BaseResource {

    public static final Status MALFORMED_ID = new Status(400, null, "Malformed crawler id", getApiUrl(CrawlerResource.class));

    public static final Status NOT_FOUND = new Status(404, null, "Unknown crawler", getApiUrl(CrawlerResource.class));

    private static final Pattern CRAWLER_ID_REGEX = Pattern.compile("([A-Za-z0-9.-]+):([0-9-]+).([0-9]+)");

    private String crawlerId;

    public CrawlerResource() {
    }

    public CrawlerResource(Context context, Request request, Response response) {
        super(context, request, response);
        this.crawlerId = (String) request.getAttributes().get("crawler");
    }

    @Override
    public Representation represent(Variant variant) throws ResourceException {
        return new StringRepresentation("hello from " + getCrawler());
    }

    public Crawler getCrawler() throws ResourceException {
        Matcher m = CRAWLER_ID_REGEX.matcher(crawlerId);
        if (!m.matches()) {
            throw new ResourceException(MALFORMED_ID);
        }
        String host = m.group(1);
        int port = Integer.valueOf(m.group(2));
        int instanceNo = Integer.valueOf(m.group(3));
        Crawler crawler = getCluster().getCrawler(host, port, instanceNo);
        if (crawler == null) throw new ResourceException(NOT_FOUND);
        return crawler;
    }

    @Override
    protected void setup() {
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
    }
}
