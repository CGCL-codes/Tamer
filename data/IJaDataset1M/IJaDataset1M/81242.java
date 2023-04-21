package org.openprojectservices.opsadmin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.openprojectservices.opsadmin.util.PersonComparator;

/**
 * An OPS project.
 * @author flavia
 *
 * Bear in mind that it is compulsory for projects to have a UID,
 * but this constraint does not come into action before the
 * project hits the DAO. In fact only the DAO can provide a
 * project with a UID.
 * For this reason new projects should be created without an id.
 */
public class Project implements Serializable {

    /** Default cereal */
    private static final long serialVersionUID = 1L;

    private String name;

    private String mailFolder;

    private String uid;

    private ProjectStatus status;

    private Person manager;

    private List<Person> members = new ArrayList<Person>();

    private Date startDate;

    private Date endDate;

    private String opsDocumentPath;

    private Collection<QuoteTask> quoteTasks;

    /** 0-arg constructor. The uid will be added by the DAO when the new Project is persisted. */
    public Project() {
        super();
    }

    public Project(final String uid, final String name) {
        super();
        this.setUid(uid);
        this.setName(name);
    }

    public Project(final String uid, final Date startDate) {
        this(uid, startDate, null);
    }

    public Project(final String uid, final Date startDate, final Date endDate) {
        super();
        this.setUid(uid);
        this.setName(name);
        this.setEndDate(endDate);
    }

    /** clone a project */
    public Project(final Project original) {
        this();
        if (original == null) {
            throw new IllegalArgumentException("Cannot clone a null project.");
        }
        this.setEndDate(original.getEndDate());
        this.setMailFolder(original.getMailFolder());
        this.setManager(original.getManager());
        this.setMembers(original.getMembers());
        this.setName(original.getName());
        this.setOpsDocumentPath(original.getOpsDocumentPath());
        this.setQuoteTasks(original.getQuoteTasks());
        this.setStartDate(original.getStartDate());
        this.setStatus(original.getStatus());
        this.setUid(original.getUid());
    }

    /**
	 * Close the project.
	 * Changes the status and sets the end date.
	 */
    public void close() {
        setEndDate(new Date());
        setStatus(ProjectStatus.closed);
    }

    public void addMember(final Person person) {
        getMembers().add(person);
    }

    @Override
    public String toString() {
        return "\nname: " + name + "\nuid: " + uid + "\nmail: " + mailFolder + "\ndocpath: " + opsDocumentPath + "\nStartdate: " + startDate + "\nEndDate: " + endDate + "\nManager: " + manager + "\nStatus: " + status + "\nMembers: " + members;
    }

    public Date getEndDate() {
        return endDate;
    }

    /**
	 * The end date can't be changed arbitrarily, it depends on the
	 * date the project is finished.
	 * Edit: the UID has to be settable, so ldap data can be put in a Project object
	 * @param endDate
	 */
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public String getUid() {
        return uid;
    }

    /**
	 * The uid of a project can't be changed, it uniquely defines it.
	 * Edit: the UID has to be settable, so ldap data can be put in a Project object
	 * @param uid
	 */
    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getMailFolder() {
        return mailFolder;
    }

    public void setMailFolder(final String mailFolder) {
        this.mailFolder = mailFolder;
    }

    public Person getManager() {
        return manager;
    }

    public void setManager(final Person manager) {
        this.manager = manager;
    }

    public List<Person> getMembers() {
        Collections.sort(members, new PersonComparator());
        return members;
    }

    public void setMembers(final List<Person> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(final ProjectStatus status) {
        this.status = status;
    }

    public Collection<QuoteTask> getQuoteTasks() {
        return quoteTasks;
    }

    public void setQuoteTasks(final Collection<QuoteTask> quoteTasks) {
        this.quoteTasks = quoteTasks;
    }

    public void addQuoteTask(final QuoteTask quoteTask) {
        if (this.getQuoteTasks() == null) {
            this.setQuoteTasks(new ArrayList<QuoteTask>());
        }
        this.getQuoteTasks().add(quoteTask);
    }

    public String getOpsDocumentPath() {
        return opsDocumentPath;
    }

    public void setOpsDocumentPath(final String inputPath) {
        opsDocumentPath = inputPath;
    }

    /**
	 * convenience method
	 * @return a set of all ImplementationTasks for this project, never null
	 */
    public Collection<ImplementationTask> getAllImplementationTasks() {
        final Collection<ImplementationTask> allImplementationtasks = new ArrayList<ImplementationTask>();
        if (this.getQuoteTasks() != null) {
            for (final QuoteTask qtask : this.getQuoteTasks()) {
                if (qtask.getImplementationTasks() != null) {
                    allImplementationtasks.addAll(qtask.getImplementationTasks());
                }
            }
        }
        return allImplementationtasks;
    }

    @Override
    public boolean equals(final Object o) {
        if ((o instanceof Project) && (((Project) o).getUid().equals(this.uid))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return uid.hashCode() * 17;
    }
}
