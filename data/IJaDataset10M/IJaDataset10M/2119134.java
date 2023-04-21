package org.seamantics.session.api;

import org.seamantics.model.ContentFile;
import org.seamantics.model.ContentFolder;
import org.seamantics.model.ContentNode;

public abstract class AbstractFolderService<T extends ContentFolder> {

    public boolean isFolder(ContentNode node) {
        return node instanceof ContentFolder;
    }

    public boolean isFile(ContentNode node) {
        return node instanceof ContentFile;
    }

    public ContentFolder findContentFolderByPath(ContentFolder root, String path) {
        for (ContentFolder folder : root.getFolders()) {
            if (folder.getPath().equals(path)) {
                return folder;
            } else {
                ContentFolder contentFolder = findContentFolderByPath(folder, path);
                if (contentFolder != null) return contentFolder;
            }
        }
        return null;
    }
}
