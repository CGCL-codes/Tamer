package tudolist.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import org.jgenesis.bean.BasicBean;

@Entity
@SequenceGenerator(name = "SEQUENCE", sequenceName = "tasks_id_seq")
public class Task extends BasicBean {

    private static final long serialVersionUID = -2806748742282159816L;

    static enum Priority {

        Hight, Normal, Low
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Task parent;

    private Priority priority = Priority.Normal;

    @Column(name = "creation_date")
    private Date creationDate;

    private String description;

    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private TaskList owner;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TaskList getOwner() {
        return owner;
    }

    public void setOwner(TaskList owner) {
        this.owner = owner;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date createdAt) {
        this.creationDate = createdAt;
    }
}
