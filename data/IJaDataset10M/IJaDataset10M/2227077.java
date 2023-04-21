package com.escape.synder.setters;

import java.util.ArrayList;
import java.util.List;
import com.escape.synder.ParseContext;
import com.sun.syndication.feed.synd.SyndCategory;

/**
 * Setter for type List<SyndContent>.
 * @author escape-llc
 *
 */
public class SyndCategoryListSetter extends ListSetter<SyndCategory> {

    public SyndCategoryListSetter(Class<?> target, String method) throws SecurityException, NoSuchMethodException {
        super(target, method);
    }

    @Override
    protected List<SyndCategory> create() {
        return new ArrayList<SyndCategory>();
    }

    @Override
    protected <T> void setList(List<SyndCategory> list, T value, ParseContext ctx) {
        list.add((SyndCategory) value);
    }
}
