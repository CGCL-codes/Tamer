package org.jadapter.examples.wiki;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pjesi
 * Date: Apr 23, 2009
 * Time: 2:23:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateHandler implements Handler {

    private Date internal;

    static boolean handled;

    public DateHandler(Date internal) {
        this.internal = internal;
        handled = false;
    }

    public void handle() {
        handled = true;
    }
}
