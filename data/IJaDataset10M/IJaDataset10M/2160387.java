package net.sf.josceleton.playground.motion.app2.framework.view;

import net.sf.josceleton.core.impl.async.DefaultAsync;
import net.sf.josceleton.playground.motion.app2.framework.page.PageArgs;

public abstract class AbstractPageView extends DefaultAsync<PageViewListener> implements PageView {

    protected final void dispatch(final String pageId) {
        this.dispatch(pageId, null);
    }

    protected final void dispatch(final String pageId, PageArgs args) {
        for (PageViewListener listener : getListeners()) {
            listener.onNavigateTo(pageId, args);
        }
    }
}
