package com.javaxyq.tools;

import java.io.IOException;
import java.io.InputStream;

/**
 * ��������wdf�ļ��ڵ��ļ�������
 * 
 * @author ����ΰ
 * @history 2008-6-11 ����ΰ �½�
 */
public class WdfFileNode implements FileObject {

    /**  */
    private static final long serialVersionUID = -307042623360452427L;

    /** �ļ�ID */
    private long id;

    /** �ļ���С */
    private long size;

    /** �ļ����ƫ�Ƶ�ַ */
    private int offset;

    /** �ļ�ʣ��ռ� */
    private int space;

    private String path;

    /** ����(ע��) */
    private String description;

    /** ���ڵ�����(WdfFile) */
    private WdfFile fileSystem;

    private FileObject parent;

    private String name;

    public void setParent(FileObject parent) {
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        if (this.description == null) {
            this.description = String.valueOf(id);
        }
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public String getName() {
        if (name != null) {
            return name;
        } else if (description != null) {
            return description;
        } else {
            return Long.toHexString(id);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return "[id=" + Long.toHexString(id) + ", description=" + description + ", path=" + path + "]";
    }

    public byte[] getData() throws IOException {
        return fileSystem.getNodeData(id);
    }

    public InputStream getDataStream() throws IOException {
        return fileSystem.getNodeAsStream(id);
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        if (description != null) {
            return description;
        } else {
            return Long.toHexString(id);
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return FileUtil.getContentType(this);
    }

    public boolean isDirectory() {
        return false;
    }

    public boolean isFile() {
        return true;
    }

    @Override
    public FileObject[] listFiles(String filter) {
        return null;
    }

    public FileObject[] listFiles() {
        return null;
    }

    public int compareTo(FileObject o) {
        if (this.isDirectory() && !o.isDirectory()) {
            return 1;
        } else if (!this.isDirectory() && o.isDirectory()) {
            return -1;
        }
        int len1 = this.path.length();
        int len2 = o.getPath().length();
        if (len1 != len2) {
            return len1 - len2;
        }
        return this.path.compareTo(o.getPath());
    }

    public WdfFile getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(WdfFile fileSystem) {
        this.fileSystem = fileSystem;
    }

    public FileObject getParent() {
        return parent;
    }
}
