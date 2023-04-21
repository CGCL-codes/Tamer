package com.cromoteca.meshcms.client.ui.filemanager;

import com.cromoteca.meshcms.client.toolbox.Path;
import java.util.ArrayList;
import java.util.List;

public class FileClipboard {

    private List<Path> contents;

    private Path dirPath;

    private boolean cut;

    public FileClipboard() {
        contents = new ArrayList<Path>();
    }

    public void addPath(Path path) {
        if (!contents.contains(path)) {
            contents.add(path);
        }
    }

    /**
	 * @return the dirPath
	 */
    public Path getDirPath() {
        return dirPath;
    }

    /**
	 * @param dirPath the dirPath to set
	 */
    public void setDirPath(Path dirPath) {
        this.dirPath = dirPath;
    }

    /**
	 * @return the filePaths
	 */
    public List<Path> getContents() {
        return contents;
    }

    /**
	 * @param contents the contents to set
	 */
    public void setContents(List<Path> contents) {
        this.contents = contents;
    }

    /**
	 * @return the cut
	 */
    public boolean isCut() {
        return cut;
    }

    /**
	 * @param cut the cut to set
	 */
    public void setCut(boolean cut) {
        this.cut = cut;
    }

    public void clear() {
        contents.clear();
    }
}
