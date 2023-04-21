package org.kablink.teaming.module.rss;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.Entry;

public interface RssModule {

    public void deleteRssFeed(Binder binder);

    public void deleteRssFeed(Binder binder, Collection<Entry> entries);

    public void updateRssFeed(Entry entry);

    public String filterRss(HttpServletRequest request, HttpServletResponse response, Binder binder);

    public String filterAtom(HttpServletRequest request, HttpServletResponse response, Binder binder);

    public String AuthError(HttpServletRequest request, HttpServletResponse response);

    public String BinderExistenceError(HttpServletRequest request, HttpServletResponse response);
}
