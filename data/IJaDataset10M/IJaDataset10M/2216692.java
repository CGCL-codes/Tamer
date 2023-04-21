package edu.ucla.cs.typecast.rmi;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 * @date Sep 19, 2007
 */
class ReturnSet implements Future {

    private static Timer timer = new Timer(true);

    private Long corID;

    private TypeCastClient tclient;

    private Vector resultSet = new Vector();

    private FutureListener listener;

    private int freezThreshold = -1;

    protected void finalize() {
        freeze();
    }

    public ReturnSet(TypeCastClient client, Long corID) {
        this.tclient = client;
        this.corID = corID;
    }

    public synchronized void freeze() {
        if (tclient != null) {
            tclient.removeFuture(this);
            tclient = null;
        }
        if (listener != null) {
            listener.freezed(this);
        }
    }

    public void freeze(long timeout, int count) {
        this.freezThreshold = count;
        if (timeout > 0) {
            TimerTask task = new TimerTask() {

                public void run() {
                    freeze();
                }
            };
            timer.schedule(task, timeout);
        }
    }

    public Object get(int index) {
        return resultSet.get(index);
    }

    public int getCounts() {
        return resultSet.size();
    }

    public synchronized void add(Object value) {
        resultSet.add(value);
        if (listener != null) {
            listener.newValueArrived(this, value);
        }
    }

    public Long getCorrelationID() {
        return this.corID;
    }

    public synchronized void addFutureListener(FutureListener listener) {
        this.listener = listener;
        if (resultSet.size() > 0) {
            for (Object entry : resultSet) {
                listener.newValueArrived(this, entry);
            }
        }
    }
}
