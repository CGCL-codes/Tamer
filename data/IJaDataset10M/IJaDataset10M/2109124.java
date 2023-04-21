package com.gface.custom;

import org.eclipse.swt.widgets.Composite;

public class GList extends Composite {

    public GList(Composite parent, int style) {
        super(parent, checkStyle(style));
    }

    private static int checkStyle(int style) {
        return 0;
    }
}
