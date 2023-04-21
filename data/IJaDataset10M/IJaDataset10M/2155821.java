package jk.spider.core.task.threading;

import jk.spider.core.task.WorkerTask;
import org.apache.log4j.Logger;

public class WorkerThread extends Thread {

    protected static final Logger log = Logger.getLogger(WorkerThread.class);

    public static final int WORKERTHREAD_IDLE = 0;

    public static final int WORKERTHREAD_BLOCKED = 1;

    public static final int WORKERHTREAD_BUSY = 2;

    protected int state;

    protected boolean assigned;

    protected boolean running;

    protected WorkerThreadPool stp;

    protected WorkerTask task;

    public WorkerThread(WorkerThreadPool stp, String name, int i) {
        super(stp, name + " " + i);
        this.stp = stp;
        running = false;
        assigned = false;
        state = WORKERTHREAD_IDLE;
    }

    /**
	 * �ж��Ƿ񻹿ɷ������������߳�
	 * @return
	 */
    public boolean isAvailable() {
        return (!assigned) && running;
    }

    public boolean isOccupied() {
        return assigned;
    }

    /**
	 * ����һ���µ����񣬲���֪����̲߳������κ��µ�����
	 * @param task
	 */
    public synchronized void assign(WorkerTask task) {
        if (!running) {
            throw new RuntimeException("THREAD NOT RUNNING, CANNOT ASSIGN TASK !!!");
        }
        if (assigned) {
            throw new RuntimeException("THREAD ALREADY ASSIGNED !!!");
        }
        this.task = task;
        assigned = true;
        notify();
    }

    public int getStates() {
        return state;
    }

    public synchronized void run() {
        running = true;
        log.info("Worker thread ( " + this.getName() + " ) born...");
        synchronized (stp) {
            stp.notify();
        }
        while (running) {
            if (assigned) {
                state = WORKERTHREAD_BLOCKED;
                task.prepare();
                state = WORKERHTREAD_BUSY;
                try {
                    task.execute();
                } catch (Exception e) {
                    log.fatal("PANIC! Task " + task + " threw an excpetion!", e);
                    task = null;
                }
                synchronized (stp) {
                    assigned = false;
                    task = null;
                    state = WORKERTHREAD_IDLE;
                    stp.notify();
                    this.notify();
                }
            }
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.info("Worker thread (" + this.getName() + ") dying");
    }

    /**
	 * �ر������߳�
	 */
    public synchronized void stopRunning() {
        if (!running) {
            throw new RuntimeException("THREAD NOT RUNNING - CANNOT STOP !");
        }
        if (assigned) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        running = false;
        notify();
    }
}
