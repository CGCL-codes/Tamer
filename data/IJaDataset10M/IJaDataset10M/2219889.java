package dms.core.document.resourceauthority.logic;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import dms.core.document.folder.logic.Folder;
import dms.core.document.folder.logic.FolderModel;
import dms.core.document.logic.Document;
import dms.core.document.logic.DocumentModel;
import dms.core.document.workspace.logic.Workspace;
import dms.core.document.workspace.logic.WorkspaceModel;
import dms.core.logic.BizModel;
import dms.core.logic.BizModelBase;
import dms.core.user.authority.logic.Authority;
import dms.core.user.authority.logic.AuthorityModel;
import dms.core.user.group.logic.Group;
import dms.core.user.group.logic.GroupModel;
import dms.core.user.logic.User;
import dms.core.user.logic.UserModel;
import dms.core.user.role.logic.Role;
import dms.core.user.role.logic.RoleModel;

@Entity
@Table(name = "TBL_RESOURCEAUTHORITY")
public class ResourceAuthorityModel extends BizModelBase implements ResourceAuthority {

    @Id
    @GeneratedValue
    private Long id;

    private String authType;

    private String inherit;

    @ManyToOne(targetEntity = UserModel.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = GroupModel.class)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(targetEntity = AuthorityModel.class)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @ManyToOne(targetEntity = FolderModel.class)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne(targetEntity = WorkspaceModel.class)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne(targetEntity = DocumentModel.class)
    @JoinColumn(name = "document_id")
    private Document document;

    @ManyToOne(targetEntity = RoleModel.class)
    @JoinColumn(name = "role_id")
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getInherit() {
        return inherit;
    }

    public void setInherit(String inherit) {
        this.inherit = inherit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Map<String, String> getFieldNames() {
        Map<String, String> fieldNames = new TreeMap<String, String>();
        fieldNames.put(ResourceAuthority.FLD_ID, Long.class.getName());
        fieldNames.put(ResourceAuthority.FLD_AUTHORITY, Authority.class.getName());
        fieldNames.put(ResourceAuthority.FLD_AUTHTYPE, String.class.getName());
        fieldNames.put(ResourceAuthority.FLD_DOCUMENT, Document.class.getName());
        fieldNames.put(ResourceAuthority.FLD_FOLDER, Folder.class.getName());
        fieldNames.put(ResourceAuthority.FLD_GROUP, Group.class.getName());
        fieldNames.put(ResourceAuthority.FLD_INHERIT, String.class.getName());
        fieldNames.put(ResourceAuthority.FLD_USER, User.class.getName());
        fieldNames.put(ResourceAuthority.FLD_WORKSPACE, Workspace.class.getName());
        fieldNames.put(ResourceAuthority.FLD_ROLE, Role.class.getName());
        fieldNames.put(BizModel.FLD_STATUS, String.class.getName());
        fieldNames.put(BizModel.FLD_CRTBY, Long.class.getName());
        fieldNames.put(BizModel.FLD_CRTDATE, Date.class.getName());
        fieldNames.put(BizModel.FLD_UPDBY, Long.class.getName());
        fieldNames.put(BizModel.FLD_UPDDATE, Date.class.getName());
        return fieldNames;
    }
}
