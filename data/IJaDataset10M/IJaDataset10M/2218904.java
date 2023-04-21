package org.buginese.client;

import java.util.List;
import org.buginese.shared.BugAnalyzedWithoutRelations;
import org.buginese.shared.BugsRelation;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>BugineseService</code>.
 */
public interface BugineseServiceAsync {

    void getProjects(AsyncCallback<List<String>> callback);

    void analyze(String projectName, AsyncCallback<List<BugAnalyzedWithoutRelations>> callback);

    void getRelated(int bugId, AsyncCallback<List<BugsRelation>> callback);
}
