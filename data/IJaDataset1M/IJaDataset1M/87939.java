package playground.wrashid.jdeqsim.parallel;

import java.util.LinkedList;
import java.util.PriorityQueue;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.mobsim.jdeqsim.DeadlockPreventionMessage;
import org.matsim.core.mobsim.jdeqsim.EndLegMessage;
import org.matsim.core.mobsim.jdeqsim.EndRoadMessage;
import org.matsim.core.mobsim.jdeqsim.EnterRoadMessage;
import org.matsim.core.mobsim.jdeqsim.EventMessage;
import org.matsim.core.mobsim.jdeqsim.LeaveRoadMessage;
import org.matsim.core.mobsim.jdeqsim.Message;
import org.matsim.core.mobsim.jdeqsim.MessageQueue;
import org.matsim.core.mobsim.jdeqsim.Road;
import org.matsim.core.mobsim.jdeqsim.StartingLegMessage;

public class PMessageQueue extends MessageQueue {

    private PriorityQueue<Message> queueThread1 = new PriorityQueue<Message>();

    private PriorityQueue<Message> queueThread2 = new PriorityQueue<Message>();

    public long idOfLowerThread = 0;

    public long idOfMainThread = 0;

    private LinkedList<Message> bufferThread1 = new LinkedList<Message>();

    private LinkedList<Message> bufferThread2 = new LinkedList<Message>();

    private double maxTimeDelta = 10;

    private boolean secondDayStarted = false;

    public boolean lowerThreadWitnessedEmptyQueue = false;

    public boolean higherThreadWitnessedEmptyQueue = false;

    private static final int SECONDS_IN_DAY = 86400;

    private int thread1_TimesCantProgressBecauseOfMaxDelta = 0;

    /**
	 *
	 * Putting a message into the queue
	 *
	 * @param m
	 */
    @Override
    public void putMessage(Message m) {
        long idOfCurrentThread = Thread.currentThread().getId();
        boolean inLowerThreadCurrently = idOfCurrentThread == idOfLowerThread ? true : false;
        ExtendedRoad messageTargetRoad = null;
        PVehicle vehicle = (PVehicle) ((EventMessage) m).vehicle;
        ExtendedRoad receivingRoad = (ExtendedRoad) m.getReceivingUnit();
        ExtendedRoad currentRoad = receivingRoad;
        ExtendedRoad nextRoad = receivingRoad;
        Id tempLinkId = null;
        if (m instanceof EnterRoadMessage) {
            tempLinkId = vehicle.getCurrentLinkId();
            if (tempLinkId != null) {
                currentRoad = (ExtendedRoad) Road.getRoad(tempLinkId);
            }
            messageTargetRoad = currentRoad;
        } else if (m instanceof LeaveRoadMessage) {
            messageTargetRoad = receivingRoad;
        } else if (m instanceof EndRoadMessage) {
            tempLinkId = vehicle.getNextLinkInLeg();
            if (tempLinkId != null) {
                nextRoad = (ExtendedRoad) Road.getRoad(tempLinkId);
            }
            messageTargetRoad = nextRoad;
        } else if (m instanceof DeadlockPreventionMessage) {
            messageTargetRoad = receivingRoad;
        } else if (m instanceof StartingLegMessage) {
            if (vehicle.getCurrentLeg().getMode().equals(TransportMode.car)) {
                tempLinkId = vehicle.getCurrentLinkId();
                if (tempLinkId != null) {
                    currentRoad = (ExtendedRoad) Road.getRoad(tempLinkId);
                }
                messageTargetRoad = currentRoad;
            } else {
                tempLinkId = vehicle.getNextLinkInLeg();
                if (tempLinkId != null) {
                    nextRoad = (ExtendedRoad) Road.getRoad(tempLinkId);
                }
                messageTargetRoad = nextRoad;
            }
        } else if (m instanceof EndLegMessage) {
            messageTargetRoad = currentRoad;
        } else {
            assert (false);
        }
        boolean roadBelongsToLowerThreadZone = messageTargetRoad.getThreadZoneId() == 0 ? true : false;
        if (roadBelongsToLowerThreadZone) {
            if (inLowerThreadCurrently) {
                queueThread1.add(m);
            } else {
                synchronized (bufferThread1) {
                    bufferThread1.add(m);
                }
            }
        } else {
            if (!inLowerThreadCurrently) {
                queueThread2.add(m);
            } else {
                synchronized (bufferThread2) {
                    bufferThread2.add(m);
                }
            }
        }
    }

    /**
	 *
	 * Remove the message from the queue and discard it. - queue1.remove(m) does
	 * not function, because it discards all message with the same priority as m
	 * from the queue. - This java api bug is reported at:
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6207984 =>
	 * queue1.removeAll(Collections.singletonList(m)); can be used, but it has
	 * been removed because of just putting a flag to kill a message is more
	 * efficient.
	 *
	 * @param m
	 */
    @Override
    public void removeMessage(Message m) {
        boolean inLowerThreadCurrently = Thread.currentThread().getId() == idOfLowerThread ? true : false;
        synchronized (m) {
            m.killMessage();
        }
    }

    /**
	 *
	 * get the first message in the queue (with least time stamp)
	 *
	 * @return
	 */
    @Override
    public Message getNextMessage() {
        boolean inLowerThreadCurrently = Thread.currentThread().getId() == idOfLowerThread ? true : false;
        Message m = null;
        return m;
    }

    /**
	 * Especially if the network is almost empty, the simulation might slow down
	 * because of small max time delta. TODO: make this better.
	 */
    public LinkedList<Message> getNextMessages(LinkedList<Message> list) {
        boolean inLowerThreadCurrently = Thread.currentThread().getId() == idOfLowerThread ? true : false;
        Message m = null;
        if (inLowerThreadCurrently) {
            synchronized (bufferThread1) {
                queueThread1.addAll(bufferThread1);
                bufferThread1.clear();
            }
        } else {
            synchronized (bufferThread2) {
                queueThread2.addAll(bufferThread2);
                bufferThread2.clear();
            }
        }
        double maxTimeStampAllowed = -1;
        double myMinTimeStamp = -1;
        double otherThreadMinTimeStamp = -1;
        if (!secondDayStarted) {
            try {
                if (inLowerThreadCurrently) {
                    if (queueThread1.peek() != null) {
                        myMinTimeStamp = queueThread1.peek().getMessageArrivalTime();
                    }
                    if (queueThread2.peek() != null) {
                        otherThreadMinTimeStamp = queueThread2.peek().getMessageArrivalTime();
                    }
                } else {
                    if (queueThread2.peek() != null) {
                        myMinTimeStamp = queueThread2.peek().getMessageArrivalTime();
                    }
                    if (queueThread1.peek() != null) {
                        otherThreadMinTimeStamp = queueThread1.peek().getMessageArrivalTime();
                    }
                }
            } catch (Exception e) {
            }
            if (otherThreadMinTimeStamp == -1) {
                maxTimeStampAllowed = myMinTimeStamp + maxTimeDelta;
            } else {
                maxTimeStampAllowed = otherThreadMinTimeStamp + maxTimeDelta;
            }
            if (maxTimeStampAllowed > SECONDS_IN_DAY) {
                secondDayStarted = true;
            }
        } else {
            maxTimeStampAllowed = Integer.MAX_VALUE;
        }
        if (inLowerThreadCurrently) {
            while (queueThread1.peek() != null && queueThread1.peek().getMessageArrivalTime() <= maxTimeStampAllowed) {
                list.add(queueThread1.poll());
            }
            if (list.size() == 0 && queueThread1.size() != 0) {
                thread1_TimesCantProgressBecauseOfMaxDelta++;
            }
        } else {
            while (queueThread2.peek() != null && queueThread2.peek().getMessageArrivalTime() <= maxTimeStampAllowed) {
                list.add(queueThread2.poll());
            }
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return queueThread1.size() + queueThread2.size() == 0;
    }

    public boolean isListEmptyWitnessedByAll() {
        boolean inLowerThreadCurrently = Thread.currentThread().getId() == idOfLowerThread ? true : false;
        synchronized (queueThread1) {
            synchronized (queueThread2) {
                if (isEmpty()) {
                    if (inLowerThreadCurrently) {
                        lowerThreadWitnessedEmptyQueue = true;
                    } else {
                        higherThreadWitnessedEmptyQueue = true;
                    }
                } else {
                    if (inLowerThreadCurrently) {
                        lowerThreadWitnessedEmptyQueue = false;
                    } else {
                        higherThreadWitnessedEmptyQueue = false;
                    }
                }
                return lowerThreadWitnessedEmptyQueue && higherThreadWitnessedEmptyQueue;
            }
        }
    }

    public double getMaxTimeDelta() {
        return maxTimeDelta;
    }

    public void setMaxTimeDelta(double maxTimeDelta) {
        this.maxTimeDelta = maxTimeDelta;
    }
}
