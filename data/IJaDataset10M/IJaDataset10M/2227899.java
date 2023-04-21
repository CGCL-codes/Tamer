package rtjdds.util.concurrent.lf;

import java.util.Stack;
import javax.realtime.RealtimeThread;
import rtjdds.util.GlobalProperties;
import rtjdds.util.Logger;
import rtjdds.util.collections.RTLinkedList;
import rtjdds.util.collections.RTList;

public class LIFOLeaderFollowerTP extends LeaderFollowerTP {

    private Stack _followerStack = new Stack();

    public LIFOLeaderFollowerTP(int numThreads, int defaultPriority, boolean noHeap, EventHandler[] handler) {
        super(numThreads, defaultPriority, noHeap, handler);
    }

    protected void promoteLeader() {
        if (!_followerStack.isEmpty()) {
            GlobalProperties.logger.log(Logger.SEVERE, getClass(), "promoteLeader()", "Going to pop()");
            RealtimeThread leader = (RealtimeThread) _followerStack.pop();
            GlobalProperties.logger.log(Logger.SEVERE, getClass(), "promoteLeader()", "popped");
            synchronized (leader) {
                leader.notify();
            }
        } else {
            GlobalProperties.logger.log(Logger.SEVERE, getClass(), "promoteLeader()", "No followers available");
        }
    }

    /**
	 * In this implementation each follower waits on its own (private) semaphore.
	 * The last returned thread is the first that will be woken up.
	 */
    protected void returnToPool() {
        RealtimeThread caller = null;
        caller = RealtimeThread.currentRealtimeThread();
        _followerStack.push(caller);
        GlobalProperties.logger.log(Logger.SEVERE, getClass(), "returnToPool()", "Pushed, current stack size = " + _followerStack.size());
        synchronized (caller) {
            try {
                GlobalProperties.logger.log(Logger.SEVERE, getClass(), "returnToPool()", "Waiting");
                caller.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
