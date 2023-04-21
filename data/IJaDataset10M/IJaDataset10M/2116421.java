package com.sitescape.team.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import com.sun.org.apache.bcel.internal.generic.ISTORE;

/**
 * @hibernate.class table="SS_FolderEntries" dynamic-update="true" lazy="true"
 * @hibernate.mapping auto-import="false"
 * need auto-import = false so names don't collide with jbpm
 * <code>FolderEntry</code> represents a entry or a reply.
 *
 */
public class FolderEntry extends WorkflowControlledEntry implements WorkflowSupport, Reservable {

    protected HistoryStamp reservation;

    protected List replies;

    protected HKey docHKey;

    protected int replyCount = 0;

    protected int nextDescendant = 1;

    protected Date lastActivity;

    protected int totalReplyCount = 0;

    protected FolderEntry topEntry;

    protected FolderEntry parentEntry;

    protected String owningFolderSortKey;

    protected Integer lockedFileCount;

    protected String postedBy;

    public FolderEntry() {
        super();
    }

    public EntityIdentifier.EntityType getEntityType() {
        return EntityIdentifier.EntityType.folderEntry;
    }

    public Folder getParentFolder() {
        return (Folder) getParentBinder();
    }

    public void setParentFolder(Folder parentFolder) {
        setParentBinder(parentFolder);
    }

    public Folder getTopFolder() {
        Folder f = getParentFolder().getTopFolder();
        if (f != null) {
            return f;
        } else {
            return getParentFolder();
        }
    }

    /** 
     * @hibernate.property 
     * @return
     */
    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    /**
     * @hibernate.component class="com.sitescape.team.domain.HistoryStamp" prefix="reserved_"
     */
    public HistoryStamp getReservation() {
        return this.reservation;
    }

    public void setReservation(HistoryStamp reservation) {
        this.reservation = reservation;
    }

    public void setReservation(User owner) {
        setReservation(new HistoryStamp(owner));
    }

    public void clearReservation() {
        this.reservation = null;
    }

    /**
     * @hibernate.component
     */
    public HKey getHKey() {
        return docHKey;
    }

    public void setHKey(HKey docHKey) {
        this.docHKey = docHKey;
    }

    public int getDocLevel() {
        return getHKey().getLevel();
    }

    public String getDocNumber() {
        return getHKey().getEntryNumber();
    }

    /**
     * @hibernate.property length="512" 
     * @return
     */
    public String getOwningFolderSortKey() {
        return owningFolderSortKey;
    }

    public void setOwningFolderSortKey(String owningFolderSortKey) {
        this.owningFolderSortKey = owningFolderSortKey;
    }

    /**
     * @hibernate.property 
     * @return
     */
    public Date getLastActivity() {
        return this.lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    public void updateLastActivity(Date lastActivity) {
        if (lastActivity == null) return;
        if ((this.lastActivity == null) || (this.lastActivity.getTime() < lastActivity.getTime())) {
            this.lastActivity = lastActivity;
            if (topEntry != null) topEntry.updateLastActivity(lastActivity);
        }
    }

    public void setWorkflowChange(HistoryStamp workflowChange) {
        super.setWorkflowChange(workflowChange);
        if (workflowChange == null) return;
        updateLastActivity(workflowChange.getDate());
    }

    /**
     * @hibernate.property not-null="true"
     */
    public int getReplyCount() {
        return this.replyCount;
    }

    protected void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    /**
     * @hibernate.property not-null="true"
     */
    public int getNextDescendant() {
        return this.nextDescendant;
    }

    protected void setNextDescendant(int nextDescendant) {
        this.nextDescendant = nextDescendant;
    }

    /**
     * @hibernate.property not-null="true"
     * @return
     */
    public int getTotalReplyCount() {
        return this.totalReplyCount;
    }

    protected void setTotalReplyCount(int totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    /**
     * @hibernate.many-to-one class="com.sitescape.team.domain.FolderEntry"
     * @return
     */
    public FolderEntry getTopEntry() {
        return topEntry;
    }

    public void setTopEntry(FolderEntry topEntry) {
        this.topEntry = topEntry;
    }

    public boolean isTop() {
        return topEntry == null;
    }

    /**
     * @hibernate.many-to-one class="com.sitescape.team.domain.FolderEntry"
     * @return
     */
    public FolderEntry getParentEntry() {
        return parentEntry;
    }

    public void setParentEntry(FolderEntry parentEntry) {
        this.parentEntry = parentEntry;
    }

    public List getReplies() {
        if (replies == null) replies = new ArrayList();
        return replies;
    }

    public void addReply(FolderEntry child) {
        getReplies().add(child);
        child.setParentEntry(this);
        if (topEntry == null) child.setTopEntry(this); else child.setTopEntry(topEntry);
        child.setHKey(new HKey(docHKey, nextDescendant++));
        child.setParentFolder(getParentFolder());
        child.setOwningFolderSortKey(owningFolderSortKey);
        ++replyCount;
        addAncestor(child);
    }

    public void removeReply(FolderEntry child) {
        if (!child.getParentEntry().getId().equals(this.getId())) {
            throw new NoFolderEntryByTheIdException(child.getId(), "Entry is not a child");
        }
        child.setParentEntry(null);
        child.setTopEntry(null);
        child.setHKey(null);
        getReplies().remove(child);
        --replyCount;
        removeAncestor(child);
        getParentFolder().removeEntry(child);
    }

    protected void addAncestor(FolderEntry reply) {
        FolderEntry parent = getParentEntry();
        ++totalReplyCount;
        if (parent != null) {
            parent.addAncestor(reply);
        }
    }

    protected void removeAncestor(FolderEntry reply) {
        FolderEntry parent = getParentEntry();
        totalReplyCount = totalReplyCount - reply.getTotalReplyCount() - 1;
        if (parent != null) {
            parent.removeAncestor(reply);
        }
    }
}
