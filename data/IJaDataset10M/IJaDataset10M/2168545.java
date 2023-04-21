package org.agile.dfs.core.action;

/**
 * Action example: action:file.exists;arg:/home/fileName
 * 
 * @author kevin
 * 
 */
public class FileExist extends DfsBaseAction {

    public static final String ACTION_NAME = "file.exist";

    public FileExist() {
        this.setName(ACTION_NAME);
    }
}
