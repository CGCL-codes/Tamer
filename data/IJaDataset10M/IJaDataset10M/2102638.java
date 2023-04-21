package net.jforum.entities;

import java.io.File;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: Attachment.java,v 1.5 2006/08/20 22:47:35 rafaelsteil Exp $
 */
public class Attachment {

    private int id;

    private int postId;

    private int privmsgsId;

    private int userId;

    private AttachmentInfo info;

    /**
	 * @return Returns the id.
	 */
    public int getId() {
        return this.id;
    }

    /**
	 * @param id The id to set.
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return Returns the postId.
	 */
    public int getPostId() {
        return this.postId;
    }

    /**
	 * @param postId The postId to set.
	 */
    public void setPostId(int postId) {
        this.postId = postId;
    }

    /**
	 * @return Returns the privmsgsId.
	 */
    public int getPrivmsgsId() {
        return this.privmsgsId;
    }

    /**
	 * @param privmsgsId The privmsgsId to set.
	 */
    public void setPrivmsgsId(int privmsgsId) {
        this.privmsgsId = privmsgsId;
    }

    /**
	 * @return Returns the userId.
	 */
    public int getUserId() {
        return this.userId;
    }

    /**
	 * @param userId The userId to set.
	 */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
	 * @return Returns the info.
	 */
    public AttachmentInfo getInfo() {
        return this.info;
    }

    /**
	 * @param info The info to set.
	 */
    public void setInfo(AttachmentInfo info) {
        this.info = info;
    }

    public boolean hasThumb() {
        return SystemGlobals.getBoolValue(ConfigKeys.ATTACHMENTS_IMAGES_CREATE_THUMB) && new File(SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_STORE_DIR) + '/' + this.info.getPhysicalFilename() + "_thumb").exists();
    }

    public String thumbPath() {
        return SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_UPLOAD_DIR) + '/' + this.info.getPhysicalFilename() + "_thumb";
    }
}
