package pam.core;

public class File implements IFile {

    private static final long serialVersionUID = -8888562939211163074L;

    private String id;

    private String title;

    private String extName;

    private String catalog;

    private long size;

    private String description;

    private byte[] content;

    public File() {
        super();
    }

    public File(String id, String title, String extName, String catalog, long size, String note, byte[] content) {
        super();
        this.id = id;
        this.title = title;
        this.extName = extName;
        this.catalog = catalog;
        this.size = size;
        this.description = note;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toXML() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<File>");
        stringBuffer.append("<ID>");
        stringBuffer.append("<![CDATA[");
        stringBuffer.append(getId() == null ? "" : getId());
        stringBuffer.append("]]>");
        stringBuffer.append("</ID>");
        stringBuffer.append("<Title>");
        stringBuffer.append("<![CDATA[");
        stringBuffer.append(getTitle() == null ? "" : getTitle());
        stringBuffer.append("]]>");
        stringBuffer.append("</Title>");
        stringBuffer.append("<Extension-Name>");
        stringBuffer.append("<![CDATA[");
        stringBuffer.append(getExtName() == null ? "" : getExtName());
        stringBuffer.append("]]>");
        stringBuffer.append("</Extension-Name>");
        stringBuffer.append("<Catalog>");
        stringBuffer.append("<![CDATA[");
        stringBuffer.append(getCatalog() == null ? "" : getCatalog());
        stringBuffer.append("]]>");
        stringBuffer.append("</Catalog>");
        stringBuffer.append("<Catalog>");
        stringBuffer.append("<![CDATA[");
        stringBuffer.append(getCatalog() == null ? "" : getCatalog());
        stringBuffer.append("]]>");
        stringBuffer.append("</Catalog>");
        stringBuffer.append("<Size>");
        stringBuffer.append(size < 1024 ? size + "Byte" : size < 1048576 ? size / 1024 + "KB" : size / 1048576 + "MB");
        stringBuffer.append("</Size>");
        stringBuffer.append("<Description>");
        stringBuffer.append("<![CDATA[");
        stringBuffer.append(getDescription() == null ? "" : getDescription());
        stringBuffer.append("]]>");
        stringBuffer.append("</Description>");
        stringBuffer.append("</File>");
        return stringBuffer.toString();
    }
}
