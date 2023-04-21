package org.systemsbiology.apps.gui.client.rpc;

import org.systemsbiology.apps.gui.client.controller.request.FileBrowseRequest;
import org.systemsbiology.apps.gui.client.exception.AuthenticationException;
import org.systemsbiology.apps.gui.client.exception.SRMException;
import org.systemsbiology.apps.gui.client.widget.fileselector.SerializableTreeNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for file browser service 
 * 
 * @author Mark Christiansen
 *
 */
@RemoteServiceRelativePath("FileBrowser")
public interface FileBrowserService extends RemoteService {

    public SerializableTreeNode getDirectoryTreeAtNode(FileBrowseRequest request) throws SRMException, AuthenticationException;

    public SerializableTreeNode[] getRootDirectoryTree(FileBrowseRequest request) throws SRMException, AuthenticationException;

    public static class Util {

        public static FileBrowserServiceAsync getInstance() {
            FileBrowserServiceAsync instance = (FileBrowserServiceAsync) GWT.create(FileBrowserService.class);
            return instance;
        }
    }
}
