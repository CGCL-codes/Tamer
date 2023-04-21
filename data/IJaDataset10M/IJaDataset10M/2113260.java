package org.rakiura.micro;

import org.rakiura.util.LinkedList;

/**
 * Represents a goal queue. Goal queue delegates the goal
 * processing to the Goal Processor.
 * 
 *<br><br>
 * GoalQueue.java<br>
 * Created: Mon Apr  2 12:57:51 2001<br>
 *
 * @author Mariusz Nowostawski   (mariusz@rakiura.org)
 * @version $Revision: 1.1 $ $Date: 2006/09/20 09:34:10 $
 */
class GoalQueue implements Runnable {

    LinkedList list = new LinkedList();

    boolean active = false;

    transient Thread thread;

    Agent agent;

    public GoalQueue(Agent agent) {
        this.active = true;
        this.agent = agent;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (active) {
            if (list.size() > 0) {
                process((Item) list.removeFirst());
                continue;
            }
            synchronized (list) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    if (!active) return;
                }
            }
        }
    }

    public void suspend() {
        active = false;
        thread.interrupt();
    }

    public void resume() {
        active = true;
        thread = new Thread(this);
        thread.start();
    }

    public void die() {
        active = false;
        thread.interrupt();
    }

    public void add(Item item) {
        synchronized (list) {
            list.addLast(item);
            list.notify();
        }
    }

    private void process(GoalProcessor.Item item) {
        item.setQueueSize(list.size());
        agent.getGoalProcessor().process(item);
    }

    static class Item implements GoalProcessor.Item {

        Customer sender;

        Goal goal;

        int size;

        public Item(Customer sender, Goal goal) {
            this.sender = sender;
            this.goal = goal;
        }

        public Customer getSender() {
            return this.sender;
        }

        public Goal getGoal() {
            return this.goal;
        }

        public int getQueueSize() {
            return this.size;
        }

        public void setQueueSize(int size) {
            this.size = size;
        }
    }
}
