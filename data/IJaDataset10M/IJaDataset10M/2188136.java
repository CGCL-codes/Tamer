package ru.ksu.niimm.cll.mocassin.frontend.viewer.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("GWT.rpc")
public interface ViewerService extends RemoteService {

    ArticleInfo load(String uri);

    Graph retrieveGraph(String uri);
}
