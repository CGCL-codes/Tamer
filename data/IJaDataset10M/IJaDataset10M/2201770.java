package aga.jitracker.client.services;

import java.util.ArrayList;
import aga.jitracker.shared.domain.Issue;
import aga.jitracker.shared.domain.IssueInfo;
import aga.jitracker.shared.domain.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MainServiceAsync {

    void getCurrentUser(AsyncCallback<User> callback);

    void execQuery(long id, int start, int length, AsyncCallback<ArrayList<Issue>> callback);

    void getIssue(long id, AsyncCallback<IssueInfo> callback);

    void putIssue(Issue issue, String action, String comment, AsyncCallback<Long> callback);

    void createIssue(String type, AsyncCallback<IssueInfo> callback);
}
