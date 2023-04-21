package com.janus.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import com.janus.models.PatternStop;
import com.janus.models.Time;
import com.janus.models.Trip;

public class DataWriter {

    public static boolean write(DataStore data, File stopTimes, File trips) {
        PrintWriter stopTimesOut, tripsOut;
        try {
            stopTimesOut = new PrintWriter(stopTimes);
        } catch (FileNotFoundException e) {
            System.out.println("FAILURE: Unable to output to the file \"" + stopTimes.getName() + "\".");
            return false;
        }
        try {
            tripsOut = new PrintWriter(trips);
        } catch (FileNotFoundException e) {
            System.out.println("FAILURE: Unable to output to the file \"" + trips.getName() + "\".");
            return false;
        }
        tripsOut.println("route_id,service_id,trip_id,trip_headsign," + "direction_id,block_id,shape_id");
        int counter = 1;
        for (Trip trip : data.getTrips()) {
            if (counter % 10 == 0) {
                tripsOut.flush();
            }
            tripsOut.print("\"" + trip.getPattern().getRoute() + "\",");
            tripsOut.print("\"" + trip.getCalendar() + "\",");
            tripsOut.print("\"" + trip.getId() + "\",");
            tripsOut.print("\"" + trip.getPattern().getHeadsign() + "\",");
            tripsOut.print("" + trip.getPattern().getDirection() + ",");
            tripsOut.print("\"" + trip.getBlock() + "\",");
            tripsOut.println("\"" + trip.getPattern().getShape() + "\"");
            counter++;
        }
        tripsOut.flush();
        tripsOut.close();
        stopTimesOut.println("trip_id,arrival_time,departure_time," + "stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type");
        counter = 1;
        for (Trip trip : data.getTrips()) {
            boolean writeStops = trip.getTimes().get(0) != null;
            boolean isGuaranteed = true;
            int timeUsed = 0;
            try {
                for (PatternStop stop : trip.getPattern().getStops()) {
                    if (writeStops) {
                        if (stop.isTimepoint()) {
                            Time time = trip.getTimes().get(timeUsed);
                            if (time == null) {
                                writeStops = false;
                            } else {
                                stopTimesOut.println(createStop(trip.getTimes().get(timeUsed), isGuaranteed, trip, stop));
                                isGuaranteed = time.isGuaranteed();
                                if (timeUsed + 1 < trip.getTimes().size()) {
                                    Time futureTime = trip.getTimes().get(timeUsed + 1);
                                    if (futureTime == null) {
                                        writeStops = false;
                                    } else if (!futureTime.isGuaranteed()) {
                                        isGuaranteed = false;
                                    }
                                }
                            }
                            timeUsed++;
                        } else {
                            stopTimesOut.println(createStop(null, isGuaranteed, trip, stop));
                        }
                    } else {
                        if (stop.isTimepoint()) {
                            Time time = trip.getTimes().get(timeUsed);
                            if (time == null) {
                            } else {
                                writeStops = true;
                                stopTimesOut.println(createStop(trip.getTimes().get(timeUsed), isGuaranteed, trip, stop));
                            }
                            timeUsed++;
                        }
                    }
                    if (counter % 10 == 0) {
                        stopTimesOut.flush();
                    }
                    counter++;
                }
            } catch (Exception e) {
                System.out.println("WARNING: Problems with trip " + trip.getId() + ", skipping.");
            }
        }
        stopTimesOut.flush();
        stopTimesOut.close();
        return true;
    }

    private static String createStop(Time time, boolean isGuaranteedState, Trip trip, PatternStop stop) {
        String answer = "\"" + trip.getId() + "\",";
        if (time == null) {
            answer += ",,";
        } else {
            answer += "" + time.toTime(stop.getArrivalOffset() * -1) + ",";
            answer += "" + time.toTime(0) + ",";
        }
        answer += "\"" + stop.getStop() + "\",";
        answer += "" + stop.getSequence() + ",";
        answer += "\"" + stop.getHeadsign() + "\",";
        if (!isGuaranteedState) {
            answer += "2,3";
        } else {
            answer += "" + stop.getPickup() + ",";
            answer += "" + stop.getDropoff() + "";
        }
        return answer;
    }
}
