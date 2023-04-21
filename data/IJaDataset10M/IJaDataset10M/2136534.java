package org.in4ama.editor.googledocs.project;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import org.in4ama.datasourcemanager.googledocs.utils.DocumentListException;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.FolderEntry;
import com.google.gdata.util.ServiceException;

public class Folder {

    Folder parent;

    String folderName;

    String id;

    Vector children;

    public Folder(String folderId, Folder parent, String name) {
        this(folderId, parent, name, false);
    }

    public Folder(String folderId, Folder parentFolder, String name, boolean create) {
        id = folderId;
        folderName = name;
        children = new Vector();
        if (parent != null) {
            parent.addChild(this);
        }
        parent = parentFolder;
        if (create) {
            createFolder();
        }
    }

    public String getId() {
        return id;
    }

    public void addChild(int index, Folder child) {
        children.add(index, child);
    }

    public void addChild(Folder child) {
        children.add(child);
    }

    public Folder getChild(int index) {
        return (Folder) children.get(index);
    }

    private URL getFolderURL() {
        URL destFolderUrl = null;
        try {
            if (parent == null) {
                destFolderUrl = GoogleSession.buildUrl("docs.google.com", GoogleSession.URL_DEFAULT + GoogleSession.URL_DOCLIST_FEED, null);
            } else {
                destFolderUrl = new URL("https://docs.google.com/feeds/default/private/full/" + getId() + "/contents");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destFolderUrl;
    }

    public void createFolder() {
        DocumentListEntry newEntry = new FolderEntry();
        newEntry.setTitle(new PlainTextConstruct(folderName));
        try {
            URL destFolderUrl = null;
            if (parent == null) {
                destFolderUrl = GoogleSession.buildUrl("docs.google.com", GoogleSession.URL_DEFAULT + GoogleSession.URL_DOCLIST_FEED, null);
            } else {
                destFolderUrl = new URL("https://docs.google.com/feeds/default/private/full/" + parent.getId() + "/contents");
            }
            FolderEntry googleFolder = (FolderEntry) GoogleSession.getInstance().getSession().insert(destFolderUrl, newEntry);
            id = GoogleSession.getId(googleFolder.getId());
            System.out.println("new folder id + " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadDocument(String filePath, String title) {
        try {
            File file = new File(filePath);
            String mimeType = DocumentListEntry.MediaType.fromFileName(file.getName()).getMimeType();
            uploadDocument(filePath, title, mimeType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadDocument(String filePath, String title, String mimeType) {
        DocumentList documentList = GoogleSession.getInstance().getDocumentList();
        try {
            File file = new File(filePath);
            DocumentEntry newDocument = new DocumentEntry();
            newDocument.setFile(file, mimeType);
            newDocument.setTitle(new PlainTextConstruct(title));
            URL destFolderUrl = getFolderURL();
            GoogleSession.getInstance().getSession().insert(destFolderUrl, newDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
