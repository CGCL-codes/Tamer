package jgcp.master.management;

import java.util.List;
import jgcp.common.Task;
import jgcp.common.TaskStatus;
import jgcp.master.network.WorkerList;
import jgcp.master.service.MasterService;

/**
 * 
 * @Date 31/05/2009
 * @author Jie Zhao (288654)
 * @version 1.0
 */
public class TaskDistributor extends Thread {

    private TaskQueue tq = TaskQueue.getInstance();

    private MasterService ms = new MasterService();

    private int index = -1;

    private Worker_Task wt = Worker_Task.getInstance();

    public TaskDistributor() {
        setDaemon(true);
    }

    public void run() {
        System.out.println("Task Distribution Thread started.");
        while (true) {
            try {
                Task t = tq.take();
                int nextWorker = nextWorker();
                if (nextWorker == -1) {
                    tq.put(t);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    continue;
                }
                t.setStatus(TaskStatus.SCHEDULED);
                wt.assign(nextWorker, t.getTaskid());
                ms.sendTask(nextWorker, t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * 
	 * @return
	 */
    private int nextWorker() {
        List<Integer> workers = WorkerList.getInstance().getWorkerList();
        int size = workers.size();
        if (size == 0) {
            index = -1;
            return -1;
        } else if (index < size) {
            index++;
        }
        if (index >= size) {
            index = 0;
        }
        return workers.get(index);
    }
}
