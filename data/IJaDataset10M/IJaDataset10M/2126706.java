package org.oddtake.client.options.forums.datahandlers;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.oddtake.client.options.forums.ForumList;
import org.oddtake.client.options.forums.datahandlers.interfaces.ForumDataHandler;
import org.oddtake.server.hibernate.ForumInfo;

public class AllForumsDataProvider extends AbstractForumDataProvider {

    ForumDataHandler dataHandler = null;

    public AllForumsDataProvider(ForumDataHandler in) {
        super();
        dataHandler = in;
    }

    public void callCurrentForumInfo(int startAtThisRow) {
        AsyncCallback callback = new AsyncCallback() {

            public void onSuccess(Object result) {
                dataHandler.handleCurrentForumInfo((ForumInfo) result);
            }

            public void onFailure(Throwable caught) {
            }
        };
        forumService.getForums(startAtThisRow, ForumList.VISIBLE_FORUM_COUNT, callback);
    }
}
