package listo.client.taskfilters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import listo.client.ContextManager;
import listo.client.model.Folder;
import listo.client.model.ObjectId;
import listo.client.model.Task;
import listo.client.model.Tasks;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class FoldersFilter extends BaseFilter implements TaskFilter.Listener {

    private final ContextManager contextManager;

    @Inject
    public FoldersFilter(TaskListFilter taskListFilter, ContextManager contextManager) {
        this.contextManager = contextManager;
        taskListFilter.addFilterListener(this);
    }

    public void tasksChanged(Object updateObject) {
        Tasks tasks = (Tasks) updateObject;
        List<Folder> folders = contextManager.getCurrentContext().getAllFolders();
        Map<ObjectId, Tasks> map = new HashMap<ObjectId, Tasks>(folders.size() + 2);
        for (Task task : tasks) {
            if (task.hasFolders()) {
                for (ObjectId folderId : task.getFolders()) {
                    Tasks folderTasks = map.get(folderId);
                    if (folderTasks == null) {
                        folderTasks = new Tasks();
                        map.put(folderId, folderTasks);
                    }
                    folderTasks.add(task);
                }
            }
        }
        notifyListeners(map);
    }
}
