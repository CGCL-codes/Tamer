package org.netbeans.cubeon.jira.query;

import org.netbeans.cubeon.jira.repository.JiraTaskRepository;
import org.netbeans.cubeon.tasks.spi.Notifier;
import org.netbeans.cubeon.tasks.spi.query.TaskQuery;
import org.netbeans.cubeon.tasks.spi.query.TaskQueryEventAdapter;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepository;

/**
 *
 * @author Anuradha G
 */
public abstract class AbstractJiraQuery implements TaskQuery {

    private final JiraTaskRepository repository;

    private final String id;

    protected final QueryExtension extension;

    public enum Type {

        FILTER, UTIL
    }

    public AbstractJiraQuery(JiraTaskRepository repository, String id) {
        this.repository = repository;
        this.id = id;
        extension = new QueryExtension(this);
    }

    public String getId() {
        return id;
    }

    public TaskRepository getTaskRepository() {
        return repository;
    }

    public JiraTaskRepository getRepository() {
        return repository;
    }

    public Notifier<TaskQueryEventAdapter> getNotifier() {
        return extension;
    }

    public QueryExtension getJiraExtension() {
        return extension;
    }

    public abstract Type getType();
}
