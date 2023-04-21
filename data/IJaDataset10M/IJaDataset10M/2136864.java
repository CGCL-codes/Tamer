package dms.core.document.logic;

import dms.core.document.doctype.logic.DocType;
import dms.core.document.folder.logic.Folder;
import dms.core.document.workspace.logic.Workspace;
import dms.core.logic.BizModel;
import dms.core.user.logic.User;

public interface Document extends BizModel {

    public static final String FLD_DOCTYPE = "docType";

    public static final String FLD_FOLDER = "folder";

    public static final String FLD_WORKSPACE = "workspace";

    public static final String FLD_OWNER = "owner";

    public static final String FLD_NAME = "name";

    public static final String FLD_LOCATION = "location";

    public static final String FLD_VERSION = "version";

    public static final String FLD_EXT = "ext";

    public static final String FLD_SIZE = "size";

    public static final String FLD_TITLE = "title";

    public static final String FLD_DESCR = "descr";

    public static final String FLD_ENCODING = "encoding";

    public static final String FLD_CONTENTTYPE = "contentType";

    public static final String FLD_DOCCODE = "docCode";

    public static final String FLD_CHECKOUT_STATUS = "checkOutStatus";

    public static final String FLD_CONTENT = "content";

    public static final String FLD_USERACCESS = "useraccess";

    public static final String FLD_GROUPACCESS = "groupaccess";

    public static final String STATUS_CHECKOUT = "CO";

    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract DocType getDocType();

    public abstract void setDocType(DocType docType);

    public abstract Folder getFolder();

    public abstract void setFolder(Folder folder);

    public abstract Workspace getWorkspace();

    public abstract void setWorkspace(Workspace workspace);

    public abstract User getOwner();

    public abstract void setOwner(User owner);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getLocation();

    public abstract void setLocation(String location);

    public abstract Double getVersion();

    public abstract void setVersion(Double version);

    public abstract String getExt();

    public abstract void setExt(String ext);

    public abstract Long getSize();

    public abstract void setSize(Long size);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract String getDescr();

    public abstract void setDescr(String descr);

    public abstract String getEncoding();

    public abstract void setEncoding(String encoding);

    public abstract String getContentType();

    public abstract void setContentType(String contentType);

    public abstract String getDocCode();

    public abstract void setDocCode(String docCode);

    public abstract String getCheckOutStatus();

    public abstract void setCheckOutStatus(String checkOutStatus);

    public abstract String getContent();

    public abstract void setContent(String content);

    public abstract String getUseraccess();

    public abstract void setUseraccess(String useraccess);

    public abstract String getGroupaccess();

    public abstract void setGroupaccess(String groupaccess);
}
