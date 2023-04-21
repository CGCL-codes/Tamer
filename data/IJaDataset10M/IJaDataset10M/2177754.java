package de.objectcode.time4u.server.ejb.entities;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import de.objectcode.time4u.server.api.Day;

@Entity
@Table(name = "WORKITEMS")
public class WorkItem {

    private long m_id;

    private Calendar m_begin;

    private Calendar m_end;

    private String m_comment;

    private Project m_project;

    private Task m_task;

    private Person m_person;

    private Todo m_todo;

    @Id
    @GeneratedValue
    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    @Column(name = "wBegin", nullable = false)
    public Calendar getBegin() {
        return m_begin;
    }

    public void setBegin(Calendar begin) {
        m_begin = begin;
    }

    @Column(name = "wEnd", nullable = false)
    public Calendar getEnd() {
        return m_end;
    }

    public void setEnd(Calendar end) {
        m_end = end;
    }

    @Column(name = "wComment", length = 1000, nullable = false)
    public String getComment() {
        return m_comment;
    }

    public void setComment(String comment) {
        m_comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    public Project getProject() {
        return m_project;
    }

    public void setProject(Project project) {
        m_project = project;
    }

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    public Task getTask() {
        return m_task;
    }

    public void setTask(Task task) {
        m_task = task;
    }

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    public Person getPerson() {
        return m_person;
    }

    public void setPerson(Person person) {
        m_person = person;
    }

    @ManyToOne
    @JoinColumn(name = "todo_id", nullable = true)
    public Todo getTodo() {
        return m_todo;
    }

    public void setTodo(Todo todo) {
        m_todo = todo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof WorkItem)) return false;
        WorkItem castObj = (WorkItem) obj;
        return m_id == castObj.m_id;
    }

    public void toAPI(de.objectcode.time4u.server.api.WorkItem workItem, Day day) {
        workItem.setId(m_id);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(m_begin.get(Calendar.YEAR), m_begin.get(Calendar.MONTH), m_begin.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long dayBegin = calendar.getTimeInMillis();
        workItem.setBegin((int) ((m_begin.getTimeInMillis() - dayBegin) / 60L / 1000L));
        workItem.setEnd((int) ((m_end.getTimeInMillis() - dayBegin) / 60L / 1000L));
        workItem.setComment(m_comment);
        workItem.setProjectId(m_project.getId());
        workItem.setTaskId(m_task.getId());
        workItem.setPersonId(m_person.getId());
        day.setDay(m_begin.get(Calendar.DAY_OF_MONTH));
        day.setMonth(m_begin.get(Calendar.MONTH) + 1);
        day.setYear(m_begin.get(Calendar.YEAR));
        if (m_todo != null) workItem.setTodoId(m_todo.getId()); else workItem.setTodoId(null);
    }

    public void fromAPI(Context context, de.objectcode.time4u.server.api.WorkItem workItem, Day day) {
        m_begin = Calendar.getInstance();
        m_end = Calendar.getInstance();
        m_begin.clear();
        m_begin.set(day.getYear(), day.getMonth() - 1, day.getDay(), 0, 0, 0);
        m_begin.add(Calendar.MINUTE, workItem.getBegin());
        m_end.clear();
        m_end.set(day.getYear(), day.getMonth() - 1, day.getDay(), 0, 0);
        m_end.add(Calendar.MINUTE, workItem.getEnd());
        m_comment = workItem.getComment();
        if (m_comment == null) m_comment = "";
        if (context != null) {
            m_project = context.getManager().find(Project.class, workItem.getProjectId());
            m_task = context.getManager().find(Task.class, workItem.getTaskId());
            m_person = context.getAuthPerson();
            if (workItem.getTodoId() != null) m_todo = context.getManager().find(Todo.class, workItem.getTodoId()); else m_todo = null;
        }
    }
}
