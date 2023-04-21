package com.oranda.webofcontacts.struts2.actions;

import java.io.File;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import com.oranda.webofcontacts.struts2.domain.Comment;
import com.oranda.webofcontacts.struts2.domain.Contact;
import com.oranda.webofcontacts.struts2.domain.User;
import com.oranda.webofcontacts.struts2.interceptors.LoginInterceptor;
import com.oranda.webofcontacts.struts2.persistence.HibernateCommand;
import com.oranda.webofcontacts.struts2.persistence.HibernateTx;
import com.oranda.webofcontacts.struts2.service.ContactService;
import com.oranda.webofcontacts.struts2.service.ContactServiceImpl;
import com.oranda.webofcontacts.struts2.service.Service;

/**
 * ContactAction
 */
public class ContactAction extends HibernateCRUDAction implements Preparable {

    private static final Log LOG = LogFactory.getLog(ContactAction.class);

    private ContactService contactService = new ContactServiceImpl();

    private List<Contact> contacts;

    private Contact contact;

    private File photoFile;

    private String photoContentType;

    private String photoFilename;

    private String newComment;

    private String searchTerm;

    public Service getService() {
        return this.contactService;
    }

    public void prepare() throws Exception {
        if (this.contact != null && this.contact.getId() != -1) {
            LOG.trace("prepare() for contact id" + contact.getId());
            HibernateTx tx = new HibernateTx(getService());
            tx.executeTx(new HibernateCommand() {

                public String execute() {
                    contact = contactService.getContact(contact.getId()).copy();
                    return SUCCESS;
                }
            });
            LOG.trace("contact retrieved: " + getContact());
        }
    }

    public String save() throws Exception {
        LOG.debug("ContactAction.update(): contact is " + this.contact);
        LOG.trace("update contact new comment text is: " + getNewComment());
        User user = getCurrentUser();
        Comment comment = new Comment(this.contact, user, getNewComment());
        LOG.trace("update contact, comment is: " + comment);
        this.contact.addComment(comment);
        if (this.contact.getId() == -1) {
            return insert();
        } else {
            return update();
        }
    }

    public String doInsert() {
        LOG.debug("ContactAction.doInsert(): contact is " + this.contact);
        try {
            storePhoto();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            addActionError(e.getMessage());
            return INPUT;
        }
        this.contactService.createContact(this.contact);
        return SUCCESS;
    }

    private void storePhoto() throws Exception {
        LOG.trace("Filename: " + this.photoFilename);
        if ((this.photoFile != null) && (this.photoFilename != null) && (this.photoFilename.trim().length() > 0)) {
            try {
                String imageDir = "/resources/photos/";
                ServletContext context = ServletActionContext.getServletContext();
                String filesystemImageDir = context.getRealPath(imageDir);
                LOG.trace("Filesystem image dir is " + filesystemImageDir);
                new File(filesystemImageDir).mkdirs();
                String pathToSavePhotoAt = filesystemImageDir + "/" + this.photoFilename;
                LOG.trace("pathToSavePhotoAt " + pathToSavePhotoAt);
                File newPhotoFile = new File(pathToSavePhotoAt);
                FileUtils.copyFile(this.photoFile, newPhotoFile);
                contact.setPhotoUri(this.photoFilename);
                String msg = "Successfully saved file at " + newPhotoFile.getPath();
                LOG.debug(msg);
                addActionMessage(msg);
            } catch (Exception e) {
                throw new Exception("Could not upload photo " + this.photoFilename + " because of exception " + e.getMessage(), e);
            }
        }
    }

    public String doList() {
        this.contacts = this.contactService.getAllContacts();
        return SUCCESS;
    }

    public String doUpdate() {
        LOG.trace("num comments: " + this.contact.getComments().size());
        LOG.trace("comments: " + this.contact.getComments());
        this.contactService.updateContact(this.contact);
        return this.SUCCESS;
    }

    private User getCurrentUser() {
        User user = (User) ActionContext.getContext().getSession().get(LoginInterceptor.USER_HANDLE);
        return user;
    }

    public String doDelete() {
        this.contactService.deleteContact(this.contact.getId());
        return this.SUCCESS;
    }

    public String incrementPriority() throws Exception {
        LOG.trace("incrementPriority() start");
        HibernateTx tx = new HibernateTx(getService());
        tx.executeTx(new HibernateCommand() {

            public String execute() {
                return doIncrementPriority();
            }
        });
        return (String) tx.getResult();
    }

    public String doIncrementPriority() {
        LOG.trace("ContactAction.doIncrementPriority() " + this.contact.getId());
        this.contactService.incrementPriority(this.contact.getId());
        return SUCCESS;
    }

    public String decrementPriority() throws Exception {
        LOG.trace("decrementPriority() start");
        HibernateTx tx = new HibernateTx(getService());
        tx.executeTx(new HibernateCommand() {

            public String execute() {
                return doDecrementPriority();
            }
        });
        return (String) tx.getResult();
    }

    public String doDecrementPriority() {
        LOG.debug("ContactAction.doDecrementPriority() " + this.contact.getId());
        this.contactService.decrementPriority(this.contact.getId());
        return SUCCESS;
    }

    public String search() throws Exception {
        LOG.trace("search() start");
        HibernateTx tx = new HibernateTx(getService());
        tx.executeTx(new HibernateCommand() {

            public String execute() {
                return doSearch();
            }
        });
        return (String) tx.getResult();
    }

    public String doSearch() {
        LOG.trace("search(" + searchTerm + ")");
        this.contacts = this.contactService.searchContacts(searchTerm);
        return SUCCESS;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Contact> getContacts() {
        return this.contacts;
    }

    public void setPhoto(File photoFile) {
        this.photoFile = photoFile;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public void setPhotoFileName(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public String getNewComment() {
        return this.newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    public String getSearchTerm() {
        return this.searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
