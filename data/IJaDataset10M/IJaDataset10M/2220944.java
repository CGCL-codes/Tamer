package org.feeddreamwork.processors;

import java.util.regex.*;
import org.feeddreamwork.Utils;
import org.feeddreamwork.feed.Entry;

public class SetEntrydataProcessor extends FeedEntryProcessor {

    private String source, target, regex;

    public SetEntrydataProcessor(String source, String target, String regex) {
        this.source = source;
        this.target = target;
        this.regex = regex;
    }

    @Override
    protected boolean processEntry(FeedContext context, Entry entry) throws FeedProcessException {
        if (Utils.isNullOrEmpty(source)) return true;
        String content = entry.get(source);
        Matcher mat = Pattern.compile(regex).matcher(content);
        if (mat.find()) {
            String result = mat.group();
            entry.set(target, result);
        }
        return true;
    }
}
