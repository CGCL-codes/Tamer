package com.sgw.runner;

/**
 *
 * @author paine
 */
public class JoggingLog {

    public static void main(String[] args) {
        System.out.println("Simple driver for jogging log...");
        float miles = 3.58f;
        int hours = 0;
        int minutes = 45;
        int seconds = 0;
        RunEntry entry1 = new RunEntry(RunType.EASY, miles, hours, minutes, seconds);
        int paceTime = entry1.getPace();
        System.out.println("Ran " + miles + " miles @ " + entry1.hoursFromTotal(paceTime) + ":" + entry1.minutesFromTotal(paceTime) + ":" + entry1.secondsFromTotal(paceTime));
        RunEntry entry2 = new RunEntry(RunType.HILL, miles, 0, 36, 15);
        paceTime = entry2.getPace();
        System.out.println("Ran " + miles + " miles @ " + entry1.hoursFromTotal(paceTime) + ":" + entry1.minutesFromTotal(paceTime) + ":" + entry1.secondsFromTotal(paceTime));
        float totalMiles = (3.58f * 2);
        int averagePaceTime = (entry1.getPace() + entry2.getPace()) / 2;
        RunEntry entry3 = new RunEntry();
        System.out.println(" Weekly totals: ");
        System.out.println("Ran " + totalMiles + " miles @ " + entry3.hoursFromTotal(averagePaceTime) + ":" + entry1.minutesFromTotal(averagePaceTime) + ":" + entry1.secondsFromTotal(averagePaceTime));
    }
}
