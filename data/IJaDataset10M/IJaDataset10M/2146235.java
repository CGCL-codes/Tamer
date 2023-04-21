package ipmss.entity.planning;

import ipmss.entity.authorities.User;
import ipmss.planning.TaskChangeAction;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class TaskChangeHistory.
 *
 * @author Michał Czarnik
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class TaskChangeHistory {

    /** The id. */
    @Id
    @GeneratedValue
    private int id;

    /** The user. */
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    /** The task. */
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    /** The date. */
    private java.util.Calendar date;

    /** akcja wykonana na tasku (zmiana zaleznosci, dodanie komentrza, pliku itd). */
    private TaskChangeAction action;

    /** The descritpion. */
    private String descritpion;

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the task.
     *
     * @return the task
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the task.
     *
     * @param task the new task
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Gets the action.
     *
     * @return the action
     */
    public TaskChangeAction getAction() {
        return action;
    }

    /**
     * Sets the action.
     *
     * @param action the new action
     */
    public void setAction(TaskChangeAction action) {
        this.action = action;
    }

    /**
     * Gets the descritpion.
     *
     * @return the descritpion
     */
    public String getDescritpion() {
        return descritpion;
    }

    /**
     * Sets the descritpion.
     *
     * @param descritpion the new descritpion
     */
    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public java.util.Calendar getDate() {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }
}
