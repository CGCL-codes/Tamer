package de.objectcode.time4u.server.ejb.entities;

import java.util.Calendar;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import de.objectcode.time4u.server.api.Day;
import de.objectcode.time4u.server.api.MetaProperty;

@Entity
@Table(name = "TODOS")
public class Todo {

    private long m_id;

    private Task m_task;

    private Person m_assignedToPerson;

    private Team m_assignedToTeam;

    private Person m_reporter;

    private int m_priority;

    private String m_header;

    private String m_description;

    private boolean m_completed;

    private Calendar m_createdOn;

    private Calendar m_completedAt;

    private Calendar m_deadline;

    private Map<String, TodoProperty> m_metaProperties;

    @Id
    @GeneratedValue
    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = true)
    public Person getAssignedToPerson() {
        return m_assignedToPerson;
    }

    public void setAssignedToPerson(Person assignedToPerson) {
        m_assignedToPerson = assignedToPerson;
    }

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    public Team getAssignedToTeam() {
        return m_assignedToTeam;
    }

    public void setAssignedToTeam(Team assignedToTeam) {
        m_assignedToTeam = assignedToTeam;
    }

    @Column(name = "completed", nullable = false)
    public boolean isCompleted() {
        return m_completed;
    }

    public void setCompleted(boolean completed) {
        m_completed = completed;
    }

    @Column(name = "completedat", nullable = true)
    public Calendar getCompletedAt() {
        return m_completedAt;
    }

    public void setCompletedAt(Calendar completedAt) {
        m_completedAt = completedAt;
    }

    @Column(name = "createdon", nullable = false)
    public Calendar getCreatedOn() {
        return m_createdOn;
    }

    public void setCreatedOn(Calendar createdOn) {
        m_createdOn = createdOn;
    }

    @Column(name = "deadline", nullable = true)
    public Calendar getDeadline() {
        return m_deadline;
    }

    public void setDeadline(Calendar deadline) {
        m_deadline = deadline;
    }

    @Column(name = "description", length = 1000, nullable = false)
    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    @Column(name = "header", length = 1000, nullable = false)
    public String getHeader() {
        return m_header;
    }

    public void setHeader(String header) {
        m_header = header;
    }

    @Column(name = "priority", nullable = false)
    public int getPriority() {
        return m_priority;
    }

    public void setPriority(int priority) {
        m_priority = priority;
    }

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = true)
    public Person getReporter() {
        return m_reporter;
    }

    public void setReporter(Person reporter) {
        m_reporter = reporter;
    }

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    public Task getTask() {
        return m_task;
    }

    public void setTask(Task task) {
        m_task = task;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "todo")
    @MapKey(name = "name")
    public Map<String, TodoProperty> getMetaProperties() {
        return m_metaProperties;
    }

    public void setMetaProperties(Map<String, TodoProperty> metaProperties) {
        m_metaProperties = metaProperties;
    }

    public void fromAPI(Context context, de.objectcode.time4u.server.api.Todo todo) {
        m_id = todo.getId();
        m_task = context.getManager().find(Task.class, todo.getTaskId());
        m_createdOn = Calendar.getInstance();
        m_createdOn.clear();
        m_createdOn.set(todo.getCreatedOn().getYear(), todo.getCreatedOn().getMonth() - 1, todo.getCreatedOn().getDay());
        if (todo.getReporterId() != null) {
            m_reporter = context.getManager().find(Person.class, todo.getReporterId());
        } else {
            m_reporter = null;
        }
        if (todo.getAssignedToPersonId() != null) {
            m_assignedToPerson = context.getManager().find(Person.class, todo.getAssignedToPersonId());
        } else {
            m_assignedToPerson = null;
        }
        if (todo.getAssignedToTeamId() != null) {
            m_assignedToTeam = context.getManager().find(Team.class, todo.getAssignedToTeamId());
        } else {
            m_assignedToTeam = null;
        }
        m_completed = todo.isCompleted();
        if (todo.getCompletedAt() != null) {
            m_completedAt = Calendar.getInstance();
            m_completedAt.clear();
            m_completedAt.set(todo.getCompletedAt().getYear(), todo.getCompletedAt().getMonth() - 1, todo.getCompletedAt().getDay());
        } else {
            m_completedAt = null;
        }
        m_header = todo.getHeader();
        m_description = todo.getDescription();
        if (todo.getDeadline() != null) {
            m_deadline = Calendar.getInstance();
            m_deadline.clear();
            m_deadline.set(todo.getDeadline().getYear(), todo.getDeadline().getMonth() - 1, todo.getDeadline().getDay());
        } else {
            m_deadline = null;
        }
        m_priority = todo.getPriority();
    }

    public void toAPI(de.objectcode.time4u.server.api.Todo todo) {
        todo.setId(m_id);
        todo.setTaskId(m_task.getId());
        todo.setCreatedOn(new Day(m_createdOn));
        if (m_reporter != null) todo.setReporterId(m_reporter.getId()); else todo.setReporterId(null);
        if (m_assignedToPerson != null) todo.setAssignedToPersonId(m_assignedToPerson.getId()); else todo.setAssignedToPersonId(null);
        if (m_assignedToTeam != null) todo.setAssignedToTeamId(m_assignedToTeam.getId()); else todo.setAssignedToTeamId(null);
        todo.setCompleted(m_completed);
        if (m_completedAt != null) todo.setCompletedAt(new Day(m_completedAt)); else todo.setCompletedAt(null);
        todo.setHeader(m_header);
        todo.setDescription(m_description);
        if (m_deadline != null) todo.setDeadline(new Day(m_deadline)); else todo.setDeadline(null);
        todo.setPriority(m_priority);
        if (m_metaProperties != null) {
            for (TodoProperty property : m_metaProperties.values()) {
                if (property.getBoolValue() != null) todo.addMetaProperties(new MetaProperty(property.getName(), property.getBoolValue())); else if (property.getStrValue() != null) todo.addMetaProperties(new MetaProperty(property.getName(), property.getStrValue())); else if (property.getDateValue() != null) todo.addMetaProperties(new MetaProperty(property.getName(), property.getDateValue())); else if (property.getIntValue() != null) todo.addMetaProperties(new MetaProperty(property.getName(), property.getIntValue()));
            }
        }
    }
}
