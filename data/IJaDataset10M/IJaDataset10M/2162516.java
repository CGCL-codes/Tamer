package jamsa.rcp.downloader.actions;

import jamsa.rcp.downloader.models.Task;
import jamsa.rcp.downloader.models.TaskThreadManager;
import java.util.Iterator;
import java.util.Observable;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * 停止任务动作
 * 
 * @author 朱杰
 * 
 */
public class StopTaskAction extends BaseTaskAction {

    public static final String ID = StopTaskAction.class.getName();

    public StopTaskAction(IWorkbenchWindow window, String label) {
        super(window, label);
        setId(ID);
        setText(label);
    }

    public void run() {
        TaskThreadManager.getInstance().stop(tasks);
    }

    public void update(Observable o, Object arg) {
        boolean enable = false;
        if (tasks != null && !tasks.isEmpty()) {
            for (Iterator it = tasks.iterator(); it.hasNext(); ) {
                Task task = (Task) it.next();
                if (TaskThreadManager.getInstance().isAllowStop(task)) {
                    enable = true;
                    break;
                }
            }
        }
        setEnabled(enable);
    }
}
