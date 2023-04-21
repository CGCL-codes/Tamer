package org.netbeans.cubeon.context.spi;

import org.netbeans.cubeon.tasks.spi.task.TaskElement;
import org.openide.util.Lookup;

/**
 *
 * @author Anuradha G
 */
public interface TaskResouresProvider {

    Lookup getLookup();

    boolean isSupported(TaskElement taskElement);

    TaskResourceSet createResourceSet(TaskElement element);
}
