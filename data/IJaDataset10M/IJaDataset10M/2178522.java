package org.jfree.report.event;

import java.util.EventObject;

/**
 * Creation-Date: 15.11.2006, 21:02:25
 *
 * @author Thomas Morgner
 */
public class ReportProgressEvent extends EventObject {

    public static final int COMPUTING_LAYOUT = 0;

    public static final int PRECOMPUTING_VALUES = 1;

    public static final int PAGINATING = 2;

    public static final int GENERATING_CONTENT = 3;

    private int activity;

    private int row;

    private int page;

    public ReportProgressEvent(final Object source, final int activity, final int row, final int page) {
        super(source);
        this.page = page;
        this.activity = activity;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getActivity() {
        return activity;
    }

    public int getPage() {
        return page;
    }
}
