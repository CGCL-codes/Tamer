package server;

import java.util.*;

public class Algorithm {

    public Algorithm() {
    }

    public synchronized long getMaxTime(List<Customer> waitingList, long averageWaitingTime, int numberOfScales, int constant) {
        long time = (((waitingList.size() - (numberOfScales - 1)) * averageWaitingTime) + constant);
        if (time < 0) {
            time = averageWaitingTime;
        }
        return time;
    }

    public synchronized void updatePeople(List<Customer> waitingList, long averageWaitingTime, int numberOfScales, int constant) {
        for (int i = 0; i < waitingList.size(); i++) {
            if (i < numberOfScales) {
                waitingList.get(i).setWaitingTime(averageWaitingTime + constant);
            } else {
                waitingList.get(i).setWaitingTime(((i - numberOfScales + 2) * averageWaitingTime) + constant);
            }
        }
    }

    public synchronized long updateAverage(long newServeTime, long oldServeTime, long average, long constant, int k) {
        System.out.println("endfinish: " + "newServeTime: " + newServeTime + " oldServeTime: " + oldServeTime + " average: " + average + " constant: " + constant + " k: " + k);
        long toAddTime = (oldServeTime - newServeTime) / k;
        long newAverage = average - toAddTime;
        System.out.println("toAddTime: " + toAddTime + " average: " + average);
        if (newAverage <= 0) {
            return constant;
        } else {
            return newAverage;
        }
    }

    public synchronized long makeAverage(long sum, long constant, int amount) {
        long newAverage = sum / amount;
        return newAverage;
    }
}
