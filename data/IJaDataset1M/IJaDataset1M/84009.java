package org.openuss.braincontest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.openuss.documents.FileInfo;
import org.openuss.foundation.DomainObject;

/**
 * @author Ingo Dueppe
 */
public class BrainContestInfo implements Serializable, DomainObject {

    private static final long serialVersionUID = -6411381828985242813L;

    public BrainContestInfo() {
        this.id = null;
        this.domainIdentifier = null;
        this.releaseDate = null;
        this.description = null;
        this.title = null;
        this.released = false;
        this.answers = null;
        this.tries = null;
        this.solution = null;
    }

    public BrainContestInfo(Long id, Long domainIdentifier, Date releaseDate, String description, String title, boolean released, Integer answers, Integer tries, String solution) {
        this.id = id;
        this.domainIdentifier = domainIdentifier;
        this.releaseDate = releaseDate;
        this.description = description;
        this.title = title;
        this.released = released;
        this.answers = answers;
        this.tries = tries;
        this.solution = solution;
    }

    public BrainContestInfo(Long id, Long domainIdentifier, Date releaseDate, String description, String title, boolean released, Integer answers, Integer tries, String solution, List<FileInfo> attachments) {
        this.id = id;
        this.domainIdentifier = domainIdentifier;
        this.releaseDate = releaseDate;
        this.description = description;
        this.title = title;
        this.released = released;
        this.answers = answers;
        this.tries = tries;
        this.solution = solution;
        this.attachments = attachments;
    }

    /**
	 * Copies constructor from other BrainContestInfo
	 * 
	 * @param otherBean
	 *            , cannot be <code>null</code>
	 * @throws NullPointerException
	 *             if the argument is <code>null</code>
	 */
    public BrainContestInfo(BrainContestInfo otherBean) {
        this(otherBean.getId(), otherBean.getDomainIdentifier(), otherBean.getReleaseDate(), otherBean.getDescription(), otherBean.getTitle(), otherBean.isReleased(), otherBean.getAnswers(), otherBean.getTries(), otherBean.getSolution(), otherBean.getAttachments());
    }

    /**
	 * Copies all properties from the argument value object into this value
	 * object.
	 */
    public void copy(BrainContestInfo otherBean) {
        this.setId(otherBean.getId());
        this.setDomainIdentifier(otherBean.getDomainIdentifier());
        this.setReleaseDate(otherBean.getReleaseDate());
        this.setDescription(otherBean.getDescription());
        this.setTitle(otherBean.getTitle());
        this.setReleased(otherBean.isReleased());
        this.setAnswers(otherBean.getAnswers());
        this.setTries(otherBean.getTries());
        this.setSolution(otherBean.getSolution());
        this.setAttachments(otherBean.getAttachments());
    }

    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long domainIdentifier;

    public Long getDomainIdentifier() {
        return this.domainIdentifier;
    }

    public void setDomainIdentifier(Long domainIdentifier) {
        this.domainIdentifier = domainIdentifier;
    }

    private Date releaseDate;

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private boolean released;

    public boolean isReleased() {
        return this.released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    private Integer answers;

    public Integer getAnswers() {
        return this.answers;
    }

    public void setAnswers(Integer answers) {
        this.answers = answers;
    }

    private Integer tries;

    public Integer getTries() {
        return this.tries;
    }

    public void setTries(Integer tries) {
        this.tries = tries;
    }

    private String solution;

    public String getSolution() {
        return this.solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    private List<FileInfo> attachments;

    /**
	 * Get the attachments
	 */
    public List<FileInfo> getAttachments() {
        return this.attachments;
    }

    /**
	 * Sets the attachments
	 */
    public void setAttachments(List<FileInfo> attachments) {
        this.attachments = attachments;
    }

    /**
	 * Returns <code>true</code> if the argument is an BrainContestInfo instance
	 * and all identifiers for this object equal the identifiers of the argument
	 * object. Returns <code>false</code> otherwise.
	 */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BrainContestInfo)) {
            return false;
        }
        final BrainContestInfo that = (BrainContestInfo) object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId())) {
            return false;
        }
        return true;
    }

    /**
	 * Returns a hash code based on this entity's identifiers.
	 */
    public int hashCode() {
        int hashCode = 0;
        hashCode = 29 * hashCode + (id == null ? 0 : id.hashCode());
        return hashCode;
    }
}
