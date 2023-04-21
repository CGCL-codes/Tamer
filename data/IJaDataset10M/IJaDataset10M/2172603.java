package org.ochnygosch.jIMAP.base.command.search;

import java.util.Date;

public class SinceKey extends DateSearchKey {

    public SinceKey(Date arg) {
        super("SINCE", arg);
    }
}
