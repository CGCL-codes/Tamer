package com.servengine.filepublisher;

import com.servengine.tag.Tag;
import com.servengine.tag.TagManagerLocal;
import com.servengine.user.Role;
import com.servengine.user.UserManagerLocal;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@ConversationScoped
@RolesAllowed("admin")
public class FilePublisherAdminView extends FilePublisherView {

    private static Logger logger = Logger.getLogger(FilePublisherAdminView.class.getName());

    private static final long serialVersionUID = 1L;

    @EJB
    private transient FilePublisherManagerLocal manager;

    @EJB
    private transient UserManagerLocal userManager;

    @EJB
    private transient TagManagerLocal tagManager;

    @Inject
    private transient Conversation conversation;

    private FilePublisherCategory category;

    private FilePublisherFile file;

    private FilePublisherFileDataContent fileContent;

    private boolean guestUserCanDownloadFromCategory;

    private String newPermissionUserid;

    private Role newPermissionRole;

    private boolean newPermissionUploadRight = false, newPermissionDownloadRight = true;

    private String newFileTag;

    public String storeCategory() {
        if (newPermissionRole != null) category.getRolePermissions().add(new FilePublisherRoleCategoryPermission(newPermissionRole, category, false, false));
        manager.persist(category);
        manager.setDownloadableByGuest(category, getUserSession().getPortal().getGuestUser(), guestUserCanDownloadFromCategory);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(getText("dataSaved")));
        conversation.end();
        return "index";
    }

    public void createRoleCategoryPermission() {
        if (getNewPermissionRole() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getText("pleaseChooseARole"), null));
            return;
        }
        FilePublisherRoleCategoryPermission permission = new FilePublisherRoleCategoryPermission(getNewPermissionRole(), category, isNewPermissionDownloadRight(), isNewPermissionUploadRight());
        manager.persist(permission);
        category.getRolePermissions().add(permission);
        setNewPermissionRole(null);
    }

    public void removeRoleCategoryPermission(FilePublisherRoleCategoryPermission permission) {
        manager.removePermission(permission);
    }

    public void createUserCategoryPermission() {
        if (getNewPermissionUserid() == null || getNewPermissionUserid().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getText("pleaseEnterAUserid"), null));
            return;
        }
        FilePublisherUserCategoryPermission permission = new FilePublisherUserCategoryPermission(userManager.getUser(getUserSession().getPortalid(), getNewPermissionUserid()), category, isNewPermissionDownloadRight(), isNewPermissionUploadRight());
        manager.persist(permission);
        category.getUserPermissions().add(permission);
        setNewPermissionUserid(null);
    }

    public void removeUserCategoryPermission(FilePublisherUserCategoryPermission permission) {
        manager.removePermission(permission);
    }

    public void storeFile() {
        if (file.getDatePublished() == null) file.setDatePublished(new Date());
        file.setDateUpdated(new Date());
        try {
            if (fileContent != null) {
                if (file.getDataContent() != null) manager.removeFileContent(getUserSession().getUser(), file);
                manager.persist(fileContent);
                file.setDataContent(fileContent);
                fileContent = null;
            }
            manager.persist(file);
            if (file.getCategories().contains(category) && !category.getFiles().contains(file)) category.getFiles().add(file);
        } catch (Throwable e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        fileContent = new FilePublisherFileDataContent(event.getFile().getContentType(), event.getFile().getContents());
        if (file.getName() == null) file.setName(event.getFile().getFileName());
    }

    public void removeFileTag(Tag tag) {
        file.getTags().remove(tag);
    }

    public void addFileTag() {
        file.getTags().add(tagManager.getTag(newFileTag, true));
        newFileTag = null;
    }

    public void removeCategory(FilePublisherCategory categoryExpurganda) {
        manager.removeCategory(getUserSession().getUser(), categoryExpurganda.getId());
        conversation.end();
    }

    public void removeFile(FilePublisherFile file) {
        if (category.getFiles().contains(file)) category.getFiles().remove(file);
        manager.removeFile(getUserSession().getUser(), file);
    }

    public void setCategory(FilePublisherCategory categoryExpurganda) {
        if (conversation.isTransient()) conversation.begin();
        this.category = categoryExpurganda;
    }

    public FilePublisherCategory getCategory() {
        return category;
    }

    public String editCategory(FilePublisherCategory category) {
        if (conversation.isTransient()) conversation.begin();
        this.category = category;
        guestUserCanDownloadFromCategory = category.isUserCanDownload(getUserSession().getPortal().getGuestUser());
        return "category";
    }

    public void cancel() {
        if (!conversation.isTransient()) conversation.end();
    }

    public String createCategory() {
        return createCategory(null);
    }

    public String createCategory(FilePublisherCategory parentCategory) {
        if (conversation.isTransient()) conversation.begin();
        category = new FilePublisherCategory(getUserSession().getUser(), parentCategory, null);
        return "category";
    }

    public String createFile(FilePublisherCategory category) {
        file = new FilePublisherFile(getUserSession().getUser(), null);
        file.getCategories().add(category);
        return "file";
    }

    public void setCategoryOwnerUserid(String categoryOwnerUserid) {
        this.category.setOwner(userManager.getUser(getUserSession().getPortalid(), categoryOwnerUserid));
    }

    public String getCategoryOwnerUserid() {
        return category.getOwner().getUserid();
    }

    public List<FilePublisherCategory> getOtherCategories() {
        return category.getId() == null ? null : manager.getAllCategories(getUserSession().getPortal(), category.getId());
    }

    public void setFile(FilePublisherFile file) {
        this.file = file;
    }

    public FilePublisherFile getFile() {
        return file;
    }

    public void setNewPermissionUserid(String newPermissionUserid) {
        this.newPermissionUserid = newPermissionUserid;
    }

    public String getNewPermissionUserid() {
        return newPermissionUserid;
    }

    public void setNewPermissionRole(Role newPermissionRole) {
        this.newPermissionRole = newPermissionRole;
    }

    public Role getNewPermissionRole() {
        return newPermissionRole;
    }

    public void setNewPermissionUploadRight(boolean newPermissionUploadRight) {
        this.newPermissionUploadRight = newPermissionUploadRight;
    }

    public boolean isNewPermissionUploadRight() {
        return newPermissionUploadRight;
    }

    public void setNewPermissionDownloadRight(boolean newPermissionDownloadRight) {
        this.newPermissionDownloadRight = newPermissionDownloadRight;
    }

    public boolean isNewPermissionDownloadRight() {
        return newPermissionDownloadRight;
    }

    public void setGuestUserCanDownloadFromCategory(boolean guestUserCanDownloadFromCategory) {
        this.guestUserCanDownloadFromCategory = guestUserCanDownloadFromCategory;
    }

    public boolean isGuestUserCanDownloadFromCategory() {
        return guestUserCanDownloadFromCategory;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setNewFileTag(String newFileTag) {
        this.newFileTag = newFileTag;
    }

    public String getNewFileTag() {
        return newFileTag;
    }

    public FilePublisherFileDataContent getFileContent() {
        return fileContent;
    }

    public StreamedContent getStreamedContent() {
        FilePublisherFileDataContent content = file.getDataContent();
        ByteArrayInputStream stream = new ByteArrayInputStream(content.getData());
        return new DefaultStreamedContent(stream, content.getContentType(), file.getName());
    }
}
