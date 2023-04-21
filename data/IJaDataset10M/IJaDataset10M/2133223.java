package de.miethxml.hawron.net;

import java.io.File;
import de.miethxml.hawron.project.PublishDestination;
import de.miethxml.hawron.project.Task;
import de.miethxml.hawron.security.PasswordManager;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 *
 *
 *
 *
 *
 */
public final class PublishTarget {

    protected String password;

    protected String username;

    protected String uri;

    protected String protocol;

    protected String id;

    protected String title;

    protected Task task;

    private PasswordManager pmanager;

    /**
     *
     *
     *
     */
    public PublishTarget() {
        super();
        id = "";
        password = "";
        username = "";
        protocol = "";
        uri = "";
        title = "";
    }

    /**
     * @return Returns the id.
     *
     */
    public String getID() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     *
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * @return Returns the encrypted password.
     *
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            The password to set.
     *
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String password) {
        PasswordManager.setPasswordManager(this);
        this.password = pmanager.encrypt(password);
        password = null;
    }

    /**
     * @return Returns the protocol.
     *
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     *            The protocol to set.
     *
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return Returns the uRI.
     *
     */
    public String getURI() {
        return uri;
    }

    /**
     * @param uri
     *            The uRI to set.
     *
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * @return Returns the username.
     *
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            The username to set.
     *
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return Returns the title.
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     *
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public Object clone() {
        PublishTarget clone = new PublishTarget();
        clone.setID(getID());
        clone.setPassword(getPassword());
        clone.setProtocol(getProtocol());
        clone.setTitle(getTitle());
        clone.setURI(getURI());
        clone.setUsername(getUsername());
        return clone;
    }

    public void publish(Publisher p, PublishDestination dest) {
        PasswordManager.setPasswordManager(this);
        p.setPassword(pmanager.decrypt(this.password));
        p.setUsername(this.username);
        p.setURI(this.uri);
        p.setProtocol(this.protocol);
        if (task != null) {
            File f = new File(dest.getSource());
            if (!f.isAbsolute()) {
                File builddir = new File(task.getBuildDir());
                p.rsync(builddir.getAbsolutePath() + File.separator + dest.getSource(), dest.getDestination());
            } else {
                p.rsync(dest.getSource(), dest.getDestination());
            }
        } else {
            p.rsync(dest.getSource(), dest.getDestination());
        }
    }

    public String toString() {
        return getTitle();
    }

    /**
     * @return Returns the task.
     */
    public Task getTask() {
        return task;
    }

    /**
     * @param task
     *            The task to set.
     */
    public void setTask(Task task) {
        this.task = task;
    }

    public void setPasswordManager(PasswordManager manager) {
        this.pmanager = manager;
    }
}
