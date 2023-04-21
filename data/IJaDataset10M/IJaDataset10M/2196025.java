package rql4j.domain;

public class SubFolder {

    private Folder.Catalog catalog;

    private String description;

    private FolderType folderType;

    private String guid;

    private String name;

    private String path;

    private Folder.SaveType saveType;

    private Boolean shared;

    public Folder.Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Folder.Catalog catalog) {
        this.catalog = catalog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FolderType getFolderType() {
        return folderType;
    }

    public void setFolderType(FolderType folderType) {
        this.folderType = folderType;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Folder.SaveType getSaveType() {
        return saveType;
    }

    public void setSaveType(Folder.SaveType saveType) {
        this.saveType = saveType;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    @Override
    public String toString() {
        return "SubFolder{" + "catalog=" + catalog + ", description='" + description + '\'' + ", folderType=" + folderType + ", guid='" + guid + '\'' + ", name='" + name + '\'' + ", path='" + path + '\'' + ", saveType=" + saveType + ", shared=" + shared + '}';
    }
}
