package securus.services;

/**
 * @author e.dorofeev,a.malyarenko
 */
public class FileItem {

    public static class ShareItem extends FileItem {
    }

    ;

    private String file;

    private boolean isFolder;

    private boolean isArchive = false;

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    public FileItem() {
    }

    public FileItem(String file, boolean isFolder) {
        this.file = file;
        this.isFolder = isFolder;
    }

    public FileItem(String file, boolean isFolder, boolean isArchive) {
        this(file, isFolder);
        this.isArchive = isArchive;
    }

    public String getFile() {
        return file;
    }

    public boolean isFolder() {
        return isFolder;
    }
}
