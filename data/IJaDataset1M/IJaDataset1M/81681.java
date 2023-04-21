package org.netbeans.cubeon.tasks.core.api;

import org.netbeans.cubeon.tasks.spi.task.TaskElement;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepository;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Anuradha G
 */
public interface TaskNodeFactory {

    Node createTaskElementNode(TaskElement element, boolean withActions);

    Node createTaskElementNode(TaskElement element, Children children, boolean withActions);

    Node createTaskElementNode(TaskFolder folder, TaskElement element, boolean withActions);

    Node createTaskElementNode(TaskFolder folder, TaskElement element, Children children, boolean withActions);

    Node createTaskRepositoryNode(TaskRepository repository, boolean withChildern);
}
