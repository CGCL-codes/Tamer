package com.objectcode.time4u.server.ejb.entities;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TODOS_HISTORY")
public class TodoHistoryEntity {

    private long m_id;

    private TodoEntity m_todo;

    private PersonEntity m_performedBy;

    private Date m_performedAt;

    private TaskEntity m_task;

    private PersonEntity m_assignedToPerson;

    private TeamEntity m_assignedToTeam;

    private PersonEntity m_reporter;

    private int m_priority;

    private String m_header;

    private String m_description;

    private boolean m_completed;

    private Calendar m_createdOn;

    private Calendar m_completedAt;

    private Calendar m_deadline;

    public TodoHistoryEntity() {
    }

    public TodoHistoryEntity(TodoEntity todo, PersonEntity performedBy) {
        m_todo = todo;
        m_performedBy = performedBy;
        m_performedAt = new Date();
        m_task = todo.getTask();
        m_assignedToPerson = todo.getAssignedToPerson();
        m_assignedToTeam = todo.getAssignedToTeam();
        m_reporter = todo.getReporter();
        m_priority = todo.getPriority();
        m_header = todo.getHeader();
        m_description = todo.getDescription();
        m_completed = todo.isCompleted();
        m_createdOn = todo.getCreatedOn();
        m_completedAt = todo.getCompletedAt();
        m_deadline = todo.getDeadline();
    }

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
    public PersonEntity getAssignedToPerson() {
        return m_assignedToPerson;
    }

    public void setAssignedToPerson(PersonEntity assignedToPerson) {
        m_assignedToPerson = assignedToPerson;
    }

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    public TeamEntity getAssignedToTeam() {
        return m_assignedToTeam;
    }

    public void setAssignedToTeam(TeamEntity assignedToTeam) {
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
    public PersonEntity getReporter() {
        return m_reporter;
    }

    public void setReporter(PersonEntity reporter) {
        m_reporter = reporter;
    }

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    public TaskEntity getTask() {
        return m_task;
    }

    public void setTask(TaskEntity task) {
        m_task = task;
    }

    @JoinColumn(name = "todo_id", nullable = false)
    @ManyToOne
    public TodoEntity getTodo() {
        return m_todo;
    }

    public void setTodo(TodoEntity todo) {
        m_todo = todo;
    }

    @JoinColumn(name = "performedBy_id", nullable = false)
    @ManyToOne
    public PersonEntity getPerformedBy() {
        return m_performedBy;
    }

    public void setPerformedBy(PersonEntity performedBy) {
        this.m_performedBy = performedBy;
    }

    @Column(nullable = false)
    public Date getPerformedAt() {
        return m_performedAt;
    }

    public void setPerformedAt(Date performedAt) {
        m_performedAt = performedAt;
    }
}
