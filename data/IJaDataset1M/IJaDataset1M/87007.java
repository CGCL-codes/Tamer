package ru.ksu.niimm.cll.mocassin.frontend.dashboard.client;

import ru.ksu.niimm.cll.mocassin.frontend.common.client.PagingLoadConfig;
import ru.ksu.niimm.cll.mocassin.frontend.common.client.PagingLoadInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ArxivServiceAsync {

    void loadArticles(PagingLoadConfig pagingLoadConfig, AsyncCallback<PagingLoadInfo<ArxivArticleMetadata>> callback);
}
