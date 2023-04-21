package org.jcvi.glk.ctm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.jcvi.glk.EUID;
import org.jcvi.glk.Extent;
import org.jcvi.glk.GLKUtil;

/**
 * <code>Reference</code> links the CTM to the GLK.
 * {@link Reference}s refer usually refer to GLK {@link Extent}s.
 *
 *
 * @author jsitz
 * @author dkatzel
 */
@Entity
@Table(name = "ctm_reference")
public class Reference {

    private static DateComparator<ReferenceHistory> HISTORY_SORTER = new DateComparator<ReferenceHistory>(false);

    /**
     * the CTM reference id.
     */
    private int id;

    /**
     * The reference type, as of 2008
     * this is always "Extent".
     */
    private String type;

    /**
     * The value referred to be the reference;
     * as of 2008 this is always an Extent EUID
     * displayed as a String.
     */
    private String value;

    /**
     * The current status of the Reference.
     */
    private ReferenceStatus status;

    /**
     * The change history of this reference.
     */
    private Set<ReferenceHistory> history;

    /**
     * The ReferenceAttributes for this Reference.
     */
    private Map<ReferenceAttributeType, ReferenceAttribute> attributes;

    /**
     * The EUID of the referred to Extent.
     */
    private EUID extentId;

    /**
     * List of Tasks
     */
    private List<Task> tasks;

    /**
     * Creates a new <code>Reference</code>.
     *
     */
    public Reference() {
        super();
        history = new HashSet<ReferenceHistory>();
        attributes = new HashMap<ReferenceAttributeType, ReferenceAttribute>();
        tasks = new ArrayList<Task>();
    }

    /**
     *
     * Creates a new <code>Reference</code>.
     *
     * @param status the current status of the Reference.
     * @param euid the euid referred to by this Reference.
     */
    public Reference(ReferenceStatus status, long euid) {
        this(status, new EUID(euid));
    }

    /**
     *
     * Creates a new <code>Reference</code>.
     *
     * @param status the current status of the Reference.
     * @param euid the {@link EUID} refered to by this Reference.
     */
    public Reference(ReferenceStatus status, EUID euid) {
        this();
        setStatus(status);
        setExtentId(euid);
    }

    /**
     *
     * Creates a new <code>Reference</code>.
     *
     * @param id the id of this Reference.
     * @param status the current status of the Reference.
     * @param euid the euid referred to by this Reference.
     */
    public Reference(int id, ReferenceStatus status, long euid) {
        this(id, status, new EUID(euid));
    }

    /**
     *
     * Creates a new <code>Reference</code>.
     *
     * @param id the id of this Reference.
     * @param status the current status of the Reference.
     * @param euid the {@link EUID} referred to by this Reference.
     */
    public Reference(int id, ReferenceStatus status, EUID euid) {
        this(id, status, euid, new HashSet<ReferenceHistory>());
    }

    /**
     *
     * Creates a new <code>Reference</code>.
     *
     * @param id the id of this Reference.
     * @param status the current status of the Reference.
     * @param euid the {@link EUID} referred to by this Reference.
     * @param history The change history of this Reference.
     */
    public Reference(int id, ReferenceStatus status, EUID euid, Set<ReferenceHistory> history) {
        this(id, status, euid, history, new HashMap<ReferenceAttributeType, ReferenceAttribute>());
    }

    /**
     *
     * Creates a new <code>Reference</code>.
     *
     * @param id the id of this Reference.
     * @param status the current status of the Reference.
     * @param euid the {@link EUID} referred to by this Reference.
     * @param history The change history of this Reference.
     * @param attributes the ReferenceAttributes for this Reference.
     */
    public Reference(int id, ReferenceStatus status, EUID euid, Set<ReferenceHistory> history, Map<ReferenceAttributeType, ReferenceAttribute> attributes) {
        this();
        setId(id);
        setStatus(status);
        setExtentId(euid);
        setHistory(history);
        setAttributes(attributes);
    }

    /**
     * Retrieves the id.
     *
     * @return An int
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ctm_reference_id")
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     * @param id An int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the GLK type that this reference refers to.
     * While this field is required for
     * the ctm, it is unnecessary because
     * the value is currently always <code>Extent</code>
     * so this field is not public.
     * @return A {@link String}
     */
    @Column(name = "type", nullable = false, updatable = false)
    protected String getType() {
        return type;
    }

    /**
     * Sets the GLK type that this reference refers to.
     * @param type A String
     */
    protected void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the id value of the GLK object that this reference refers to.
     * This field is also required in the ctm but is a varchar instead
     * of a number.  Please use {@link #getExtentId()} to get the {@link EUID}.
     * @return A {@link String}
     */
    @Column(name = "value", nullable = false, unique = true, insertable = false, updatable = false)
    protected String getValue() {
        return value;
    }

    /**
     * Sets the id value of the GLK object that this reference refers to.
     * @param value A String
     */
    protected void setValue(String value) {
        this.value = value;
    }

    /**
     * Retrieves the EUID that this Reference refers to.
     * @return an {@link EUID}.
     */
    @Type(type = "org.jcvi.glk.hibernate.EUIDStringUserType")
    @Column(name = "value", nullable = false, unique = true, insertable = true, updatable = false)
    public EUID getExtentId() {
        return extentId;
    }

    /**
     * Sets the EUID that this Reference refers to.
     * @param extentId
     */
    public void setExtentId(EUID extentId) {
        this.extentId = extentId;
        setValue(extentId.getValue().toPlainString());
        setType("Extent");
    }

    /**
     * Retrieves the current {@link ReferenceStatus}.
     *
     * @return A {@link ReferenceStatus}
     */
    @ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @JoinColumn(name = "ctm_reference_status_id", unique = false, nullable = false)
    @Cascade({ org.hibernate.annotations.CascadeType.PERSIST })
    public ReferenceStatus getStatus() {
        return status;
    }

    /**
     * Sets the current {@link ReferenceStatus}.
     * @param status A ReferenceStatus
     */
    public void setStatus(ReferenceStatus status) {
        this.status = status;
    }

    /**
     * Hashcode is based on the value returned by {@link #getId()}.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId();
        return result;
    }

    /**
     * Compares two {@link Reference}s for equality.
     * The result is <code>true</code> if and only
     * if the argument is not <code>null</code>
     * and is an {@link Reference} object that
     * has the same id as this object.
     * @param obj the {@link Object} to compare with.
     * @return <code>true</code> if the objects
     * are the same; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Reference)) {
            return false;
        }
        final Reference other = (Reference) obj;
        return getId() == other.getId();
    }

    /**
     * Retrieves the {@link ReferenceHistory} objects
     * that chart the progress of this {@link Reference}.
     *
     * @return A {@link Set}
     */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "reference")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    protected Set<ReferenceHistory> getHistory() {
        return history;
    }

    @Transient
    public List<ReferenceHistory> getAllHistory() {
        List<ReferenceHistory> list = new ArrayList<ReferenceHistory>(getHistory());
        Collections.sort(list, HISTORY_SORTER);
        return list;
    }

    /**
     * Sets the {@link ReferenceHistory} objects
     * that chart the progress of this {@link Reference}.
     * @param history A Set of {@link ReferenceHistory} objects.
     */
    public void setHistory(Set<ReferenceHistory> history) {
        this.history = history;
    }

    /**
     * Adds a new {@link ReferenceHistory} entry to this Reference's history.
     *
     * @param historyEntry The entry to add.
     */
    private void addHistory(ReferenceHistory historyEntry) {
        this.history.add(historyEntry);
    }

    /**
     * Updates the Reference's status, adding a history entry with the supplied
     * user comment.
     *
     * @param status The new {@link ReferenceStatus} for this Reference.
     * @param editedBy The {@link User} reporting the status change.
     * @param comment A description of the cause or reason for the update.
     * @throws IllegalArgumentException if status equals old status.
     */
    public void updateStatus(ReferenceStatus status, User editedBy, String comment) {
        updateStatus(status, editedBy, comment, null);
    }

    /**
     * Updates the Reference's status, adding a history entry with the supplied
     * user comment.
     *
     * @param status The new {@link ReferenceStatus} for this Reference.
     * @param editedBy The {@link User} reporting the status change.
     * @param comment A description of the cause or reason for the update.
     * @param date the edit date to set for this status
     * @throws IllegalArgumentException if status equals old status.
     */
    public void updateStatus(ReferenceStatus status, User editedBy, String comment, Date date) {
        if (status == null) {
            throw new IllegalArgumentException("new status can not be null");
        }
        if (editedBy == null) {
            throw new IllegalArgumentException("user can not be null");
        }
        if (comment == null) {
            throw new IllegalArgumentException("comment can not be null");
        }
        if (comment.length() == 0) {
            throw new IllegalArgumentException("comment can not be empty");
        }
        final ReferenceHistory newHistoryEntry = new ReferenceHistory(this, status, editedBy, comment);
        if (date != null) {
            newHistoryEntry.setEditedDate(date);
        }
        this.addHistory(newHistoryEntry);
        this.setStatus(status);
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "id.reference")
    @MapKey(name = "id.type")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    protected Map<ReferenceAttributeType, ReferenceAttribute> getAttributes() {
        return attributes;
    }

    protected void setAttributes(Map<ReferenceAttributeType, ReferenceAttribute> attributes) {
        this.attributes = attributes;
    }

    @Transient
    public Set<ReferenceAttribute> getReferenceAttributes() {
        return GLKUtil.getValues(getAttributes(), null);
    }

    @Transient
    public ReferenceAttribute getReferenceAttributeByType(ReferenceAttributeType type) {
        return getAttributes().get(type);
    }

    public boolean hasReferenceAttribute(ReferenceAttributeType type) {
        return getAttributes().containsKey(type);
    }

    public void setReferenceAttribute(ReferenceAttributeType type, String value) {
        if (hasReferenceAttribute(type)) {
            getAttributes().get(type).setValue(value);
        } else {
            ReferenceAttribute attr = new ReferenceAttribute(this, type, value);
            getAttributes().put(type, attr);
        }
    }

    public void removeReferenceAttribute(ReferenceAttributeType type) {
        getAttributes().remove(type);
    }

    public void clearAllReferenceAttributes() {
        getAttributes().clear();
    }

    @Override
    public String toString() {
        return "reference id " + getId();
    }

    /**
     * Retrieves the
     * NEEDSDOC: Update this method
     *
     * @return A {@link Set}
     */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "reference")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    protected List<Task> getTasks() {
        return tasks;
    }

    @Transient
    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(getTasks());
    }

    /**
     * NEEDSDOC: Update this method.
     * @param tasks A Set<Task>
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Add the given task to this reference.
     * @param task
     * @throws CTMConstraintException if this task
     * is similar to a task that is already is associated
     * with this reference.
     */
    public void addTask(Task task) {
        if (foundSimilarTask(task)) {
            throw new CTMConstraintException("Task for " + task + " already exists!");
        }
        getTasks().add(task);
    }

    /**
     * CTM Business logic: a reference can't have
     * 2 similar tasks.
     * @param task the new task to be added
     * @return <code>true</code> if there is already
     * a similar task; <code>false</code> otherwise.
     */
    private boolean foundSimilarTask(Task task) {
        boolean foundSimilarTask = false;
        for (Task preExistingTask : getTasks()) {
            foundSimilarTask |= similarTask(task, preExistingTask);
        }
        return foundSimilarTask;
    }

    /**
     * Following CTM.pm business logic - make sure
     * that no 2 tasks
     * have the same
     * status,type,description,and reference
     * @param newTask
     * @param preExistingTask
     * @return <code>true</code> if the 2 given Tasks meet those
     * requirements; <code>false</code> otherwise.
     */
    private boolean similarTask(Task newTask, Task preExistingTask) {
        return newTask.getDescription().equals(preExistingTask.getDescription()) && newTask.getType().equals(preExistingTask.getType()) && newTask.getStatus().equals(preExistingTask.getStatus());
    }
}
