package de.uhilger.netzpult.client;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import de.uhilger.netzpult.shared.Document;
import de.uhilger.netzpult.shared.Folder;

/**
 * 
 * @author Copyright (c) Ulrich Hilger, http://uhilger.de
 * @author Published under the terms and conditions of
 * the <a href="http://www.gnu.org/licenses/" target="_blank">GNU General Public License</a>
 */
@RemoteServiceRelativePath("public")
public interface PublicService extends RemoteService, BoundTreeService {

    String getDefaultDocument() throws Exception;

    String getDocumentOwner(int docId) throws Exception;

    Document getDocument(String owner, String id) throws Exception;

    String getParameter(String parameterName) throws Exception;

    String getDefaultOwner() throws Exception;

    List<Folder> getFoldersContaining(String owner, int documentId) throws Exception;

    List<Folder> getPathToFolder(Folder folder) throws Exception;

    Document getDocumentForPath(String owner, String path) throws Exception;

    List<Document> findDocuments(String owner, String searchItem) throws Exception;
}
