package org.netbeans.cubeon.tasks.spi.task;

import org.netbeans.cubeon.tasks.spi.repository.TaskRepository;

/**
 *
 * @author Anuradha G
 */
public final class TaskPriority {

    private final String id;

    private final String name;

    private final TaskRepository repository;

    public TaskPriority(TaskRepository repository, String id, String name) {
        this.repository = repository;
        this.id = id;
        this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    public String getText() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    public TaskRepository getRepository() {
        return repository;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskPriority other = (TaskPriority) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.repository != other.repository && (this.repository == null || !this.repository.equals(other.repository))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 83 * hash + (this.repository != null ? this.repository.hashCode() : 0);
        return hash;
    }
}
