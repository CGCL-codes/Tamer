package com.netx.generics.io;

import com.netx.generics.basic.Checker;

public class SearchOptions {

    public static enum ORDER {

        NAME_ASCENDING, NAME_DESCENDING, SIZE_ASCENDING, SIZE_DESCENDING, NO_ORDER
    }

    final FilenameFilter filter;

    final boolean showHidden;

    final ORDER order;

    public SearchOptions(FilenameFilter filter, boolean showHidden, ORDER order) {
        this.filter = filter;
        this.showHidden = showHidden;
        this.order = order;
    }

    public SearchOptions(FilenameFilter filter, boolean showHidden) {
        this(filter, showHidden, ORDER.NO_ORDER);
    }

    public SearchOptions(FilenameFilter filter) {
        this(filter, true);
        Checker.checkNull(filter, "filter");
    }

    public SearchOptions(String regexp, boolean showHidden, ORDER order) {
        this((FilenameFilter) (regexp == null ? null : new RegexpFilenameFilter(regexp)), showHidden, order);
    }

    public SearchOptions(String regexp, boolean showHidden) {
        this(regexp, showHidden, ORDER.NO_ORDER);
    }

    public SearchOptions(String regexp) {
        this(regexp, true, ORDER.NO_ORDER);
        Checker.checkNull(regexp, "regexp");
    }
}
