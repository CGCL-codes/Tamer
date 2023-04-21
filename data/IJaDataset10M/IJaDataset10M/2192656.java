package cn.vlabs.clb.domain.folder.exception;

import cn.vlabs.clb.domain.folder.File;

/**
 * ·���Ѵ���
 * @author л����(xiejj@cnic.cn)
 * @created Sep 10, 2008
 */
public class PathExist extends FolderError {

    public PathExist(String path) {
        super("·���Ѵ���" + path);
        this.path = path;
    }

    public PathExist(String path, String entryName) {
        super("·���Ѵ���" + path + File.seperator + entryName);
        this.path = path;
        this.entryName = entryName;
    }

    public String getPath() {
        return path;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEntryName() {
        return entryName;
    }

    private String entryName;

    private String path;

    private static final long serialVersionUID = 1L;
}
