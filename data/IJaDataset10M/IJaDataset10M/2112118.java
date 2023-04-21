package cn.vlabs.clb.api.rest;

import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.UnImplemented;
import cn.vlabs.clb.api.folder.FolderInfo;
import cn.vlabs.clb.api.folder.FolderService;

public class FolderServiceImpl implements FolderService {

    private CLBConnection conn;

    public FolderServiceImpl(CLBConnection conn) {
        this.conn = conn;
    }

    public void createFile(int docid, String path, String filename) {
        CreateFileArg ca = new CreateFileArg();
        ca.docid = docid;
        ca.path = path;
        ca.filename = filename;
        conn.sendService(CREATE_FILE, ca);
    }

    public void createFile(int docid, String path) {
        createFile(docid, path, "");
    }

    public void delete(String path, boolean recurisive) {
        UnlinkMessage um = new UnlinkMessage();
        um.path = path;
        um.recursive = recurisive;
        conn.sendService(UNLINK, um);
    }

    public void link(String from, String to) {
        throw new UnImplemented("Link������δʵ��");
    }

    @SuppressWarnings("unchecked")
    public FolderInfo[] list(String path) {
        return (FolderInfo[]) conn.sendService(LIST, path);
    }

    public void mkdir(String path) {
        conn.sendService(MKDIR, path);
    }

    public void rmdir(String path) {
        UnlinkMessage um = new UnlinkMessage();
        um.path = path;
        um.recursive = false;
        conn.sendService(UNLINK, um);
    }

    public void unlink(String path) {
        UnlinkMessage um = new UnlinkMessage();
        um.path = path;
        um.recursive = false;
        conn.sendService(UNLINK, um);
    }

    public boolean exist(String path) {
        return (Boolean) conn.sendService(EXISTS, path);
    }

    private static final String CREATE_FILE = "folder.file.create";

    private static final String UNLINK = "folder.unlink";

    private static final String LIST = "folder.list";

    private static final String MKDIR = "folder.mkdir";

    private static final String EXISTS = "folder.exist";
}
