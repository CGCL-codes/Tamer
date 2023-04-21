package org.feeddreamwork.processors;

import java.io.*;
import org.feeddreamwork.*;
import org.feeddreamwork.feed.*;
import org.feeddreamwork.fetcher.*;

public class MergeFeedProcessor implements FeedProcessor {

    private String url;

    public MergeFeedProcessor(String url) {
        Utils.throwIfNullOrEmpty(url);
        this.url = url;
    }

    @Override
    public void process(FeedContext context) throws FeedProcessException {
        Utils.throwIfNull(context);
        Utils.throwIfNull(context.getFeed());
        try {
            HttpFetcher fetcher = new HttpFetcher(url);
            Feed result = FeedParsers.parse(fetcher.getContent(), url);
            for (Entry entry : result.getEntries()) context.getFeed().getEntries().add(entry.clone());
        } catch (IOException e) {
            throw new FeedProcessException();
        } catch (FeedParsingException e) {
            throw new FeedProcessException();
        }
    }
}
