package org.illico.web.common;

import org.illico.common.display.AbstractDisplayer;
import org.illico.common.text.TextUtils;

public class Displayer extends AbstractDisplayer {

    public void doDisplay(Object obj) {
        TextUtils.append(Context.getResponseWriter(), obj);
    }
}
