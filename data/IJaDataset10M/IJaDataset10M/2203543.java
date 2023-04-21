package org.lindenb.scifoaf;

/**
 * @author lindenb
 *
 * <code>Task</code>
 */
public class Task {

    SciFOAF sciFOAF;

    String tasktype;

    Integer pmid;

    Author author;

    Laboratory laboratory;

    public Task(SciFOAF sciFOAF, String tasktype) {
        this.sciFOAF = sciFOAF;
        this.tasktype = tasktype;
        this.pmid = null;
        this.author = null;
        this.laboratory = null;
    }

    public String toString() {
        return getType().toString();
    }

    public Object getType() {
        return this.tasktype;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Task)) return false;
        return (o == this);
    }
}
