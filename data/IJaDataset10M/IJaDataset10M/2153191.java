package net.jforum.entities;

/**
 * @author Rafael Steil
 * @version $Id: Bookmark.java,v 1.4 2006/08/23 02:13:46 rafaelsteil Exp $
 */
public class Bookmark {

    private int id;

    private int userId;

    private int relationId;

    private int relationType;

    private boolean publicVisible;

    private String title;

    private String description;

    public Bookmark() {
    }

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
	 * @return Returns the publicVisible.
	 */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
	 * @param publicVisible The publicVisible to set.
	 */
    public void setPublicVisible(boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    /**
	 * @return Returns the relationId.
	 */
    public int getRelationId() {
        return this.relationId;
    }

    /**
	 * @param relationId The relationId to set.
	 */
    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    /**
	 * @return Returns the relationType.
	 */
    public int getRelationType() {
        return this.relationType;
    }

    /**
	 * @param relationType The relationType to set.
	 */
    public void setRelationType(int relationType) {
        this.relationType = relationType;
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
	 * @return Returns the description.
	 */
    public String getDescription() {
        return this.description;
    }

    /**
	 * @param description The description to set.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return Returns the title.
	 */
    public String getTitle() {
        return this.title;
    }

    /**
	 * @param title The title to set.
	 */
    public void setTitle(String title) {
        this.title = title;
    }
}
