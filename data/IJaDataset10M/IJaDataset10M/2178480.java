package org.wikiup.servlet.imp.iterator;

import javax.servlet.http.HttpServletRequest;
import org.wikiup.core.imp.iterator.IterableIterator;

public class ServletRequestNameIterator extends IterableIterator<String> {

    public ServletRequestNameIterator(HttpServletRequest request) {
        super(request.getParameterMap().keySet());
    }
}
