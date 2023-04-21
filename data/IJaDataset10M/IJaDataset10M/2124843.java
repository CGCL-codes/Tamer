package de.sehversuche.pse.access.pse5impl;

import de.sehversuche.pse.model.PseCollection;

public class Pse5Folder {

    private Long id;

    private String name;

    private Long firstChildFolderId;

    private Long nextFolderId;

    private Long folderAttributes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFirstChildFolderId() {
        return firstChildFolderId;
    }

    public void setFirstChildFolderId(Long firstChildFolderId) {
        this.firstChildFolderId = firstChildFolderId;
    }

    public Long getNextFolderId() {
        return nextFolderId;
    }

    public void setNextFolderId(Long nextFolderId) {
        this.nextFolderId = nextFolderId;
    }

    public PseCollection toPseCollection(PseCollection parent) {
        return new PseCollection(id, name, parent);
    }

    public boolean isPseCollection() {
        return folderAttributes == Pse5TableConstants.FOLDER_ATTRIBUTE_COLLECTION_GROUP || folderAttributes == Pse5TableConstants.FOLDER_ATTRIBUTE_COLLECTION;
    }

    public Long getFolderAttributes() {
        return folderAttributes;
    }

    public void setFolderAttributes(Long folderAttributes) {
        this.folderAttributes = folderAttributes;
    }
}
