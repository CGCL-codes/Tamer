package au.com.gworks.jump.app.wiki.server.mockimpl;

import java.security.Principal;
import org.javaongems.server.GemServe;
import au.com.gworks.jump.app.wiki.client.service.WikiRpc;

public class WikiRpcImpl implements WikiRpc {

    public MyspacesInfo openMyspaces() throws SystemFailureException {
        Principal principal = GemServe.getThreadLocalPrincipal();
        MyspacesSvrController ctlr = new MyspacesSvrController(principal);
        return ctlr.fetchAvailableSpaces();
    }

    public ExplorerInfo exploreFolder(String namespace, Integer revCtx, String folderPath) throws UnauthorisedAccessException, ResourceNotFoundException, SystemFailureException {
        Principal principal = GemServe.getThreadLocalPrincipal();
        ExplorerSvrController ctrlr = new ExplorerSvrController(namespace, principal);
        return ctrlr.fetchFolderDetails(namespace, revCtx, folderPath);
    }

    public SearchInfo searchResources(String lookIn, String criteria, long date1, long date, boolean inclSubFolders, boolean inclAllMatchingRevs) throws UnauthorisedAccessException, ResourceNotFoundException, SystemFailureException {
        return null;
    }

    public LogHistoryInfo logHistory(String namespace, String resource, Integer fromRev, Integer toRev, boolean inclHierarchy) throws UnauthorisedAccessException, ResourceNotFoundException, SystemFailureException {
        return null;
    }

    public RevisionChangesInfo reportRevisionChanges(String namespace, Integer revision) throws UnauthorisedAccessException, SystemFailureException {
        return null;
    }

    public ResourceDiffInfo reportRevisionDifferences(String namespace, String resource, Integer asOfRev, Integer revA, Integer revB) throws UnauthorisedAccessException, ResourceNotFoundException, SystemFailureException {
        return null;
    }
}
