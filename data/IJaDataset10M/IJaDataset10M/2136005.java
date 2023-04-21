package org.wikiup.core.bootstrap.imp;

import java.util.Iterator;
import org.wikiup.Wikiup;
import org.wikiup.core.imp.context.MapContext;
import org.wikiup.core.inf.Document;
import org.wikiup.core.inf.DocumentInitializationAware;

public class GlobalWikiupNamingDirectory extends MapContext<Object> implements DocumentInitializationAware, Iterable<String> {

    public void initialize(Document desc) {
        Wikiup.getInstance().loadBeans(Object.class, this, desc);
    }

    public Iterator<String> iterator() {
        return getMap().keySet().iterator();
    }
}
