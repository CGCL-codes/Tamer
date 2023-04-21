package org.feeddreamwork.processors;

import java.util.*;
import org.feeddreamwork.*;
import org.feeddreamwork.feed.*;

public class GetLatestProcessor implements FeedProcessor {

    private int count;

    public GetLatestProcessor(int count) {
        Utils.throwIfSmallerThan(count, 1);
        this.count = count;
    }

    @Override
    public void process(FeedContext context) {
        Utils.throwIfNull(context);
        Utils.throwIfNull(context.getFeed());
        List<Entry> entries = context.getFeed().getEntries();
        for (int i = entries.size() - 1; i >= count; i--) entries.remove(i);
    }
}
