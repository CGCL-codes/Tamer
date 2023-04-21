package org.matsim.socialnetworks.algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import org.matsim.interfaces.core.v01.Activity;
import org.matsim.interfaces.core.v01.Facility;
import org.matsim.interfaces.core.v01.Person;
import org.matsim.socialnetworks.mentalmap.TimeWindow;
import org.matsim.socialnetworks.socialnet.EgoNet;

public class CompareTimeWindows {

    private static final Logger log = Logger.getLogger(CompareTimeWindows.class);

    /**
	 * Calculates a set of statistics about the face to face interactions
	 * at a social event and maps them to the act in the plan
	 * 
	 * Thus you can use this structure to look up the act for an ego and get
	 * the number of other agents who were at the same place at that same time
	 * 
	 * This might be more appropriate somewhere else than in an EventHandler
	 * 
	 * @param plan
	 * @return
	 */
    public static LinkedHashMap<Activity, ArrayList<Double>> calculateTimeWindowEventActStats(LinkedHashMap<Facility, ArrayList<TimeWindow>> timeWindowMap) {
        LinkedHashMap<Activity, ArrayList<Double>> actStats = new LinkedHashMap<Activity, ArrayList<Double>>();
        int count = 0;
        Iterator<Facility> fit = timeWindowMap.keySet().iterator();
        while (fit.hasNext()) {
            double friend = 0.;
            double foe = 0.;
            double totalTimeWithFriends = 0;
            TimeWindow tw1 = null;
            TimeWindow tw2 = null;
            Person p1 = null;
            Person p2 = null;
            Facility myFacility = (Facility) fit.next();
            ArrayList<TimeWindow> visits = timeWindowMap.get(myFacility);
            if (!timeWindowMap.keySet().contains(myFacility)) {
                log.error(" activityMap does not contain myActivity");
            }
            if (!(visits.size() > 0)) {
                log.error(" number of visitors not >0");
            }
            for (int i = 0; i < visits.size(); i++) {
                tw1 = visits.get(i);
                p1 = visits.get(i).person;
                if (p1.getId().toString().equals("21923040")) {
                    log.info(p1.getId() + " " + tw1.act.getType() + " " + tw1.startTime + " " + tw1.endTime);
                }
                for (int j = i + 1; j < visits.size(); j++) {
                    p2 = visits.get(j).person;
                    tw2 = visits.get(j);
                    if (CompareTimeWindows.overlapTimePlaceType(tw1, tw2) && !p1.equals(p2)) {
                        EgoNet net = p1.getKnowledge().getEgoNet();
                        if (net.getAlters().contains(p2)) {
                            friend++;
                            totalTimeWithFriends += CompareTimeWindows.getTimeWindowOverlapDuration(tw1, tw2);
                        } else {
                            foe++;
                        }
                    }
                }
                if (actStats.isEmpty() || !(actStats.keySet().contains(tw1.act))) {
                    ArrayList<Double> stats = new ArrayList<Double>();
                    actStats.put(tw1.act, stats);
                }
                if (actStats.containsKey(tw1.act)) {
                    ArrayList<Double> stats = actStats.get(tw1.act);
                    if ((friend + foe) == 0) {
                        stats.add((double) 0);
                    } else {
                        stats.add(friend / (foe + .1 * (friend + foe)));
                    }
                    stats.add(friend);
                    stats.add(totalTimeWithFriends);
                    count++;
                }
            }
        }
        return actStats;
    }

    public static boolean overlapTimePlaceType(TimeWindow tw1, TimeWindow tw2) {
        if (tw1.act.getFacility().getActivityOption(tw2.act.getType()) == null) {
            System.out.println("It's act2 " + tw2.act.getType() + " " + tw1.act.getFacility().getId() + ": " + tw2.act.getType() + " " + tw2.act.getFacility().getId());
        }
        if (tw1.act.getFacility().getActivityOption(tw1.act.getType()) == null) {
            System.out.println("It's act1 " + tw1.act.getType() + " " + tw1.act.getFacility().getId() + ": " + tw2.act.getType() + " " + tw2.act.getFacility().getId());
        }
        Activity act1 = tw1.act;
        Activity act2 = tw2.act;
        boolean overlap = false;
        if (act2.getFacility().getActivityOption(act2.getType()).equals(act1.getFacility().getActivityOption(act1.getType()))) {
            if (act2.getEndTime() >= act1.getStartTime() && act2.getStartTime() <= act1.getEndTime()) {
                overlap = true;
            }
        }
        return overlap;
    }

    public static boolean overlapTimePlaceTypeFriend(TimeWindow tw1, TimeWindow tw2) {
        if (tw1.act.getFacility().getActivityOption(tw2.act.getType()) == null) {
            System.out.println("It's act2 " + tw2.act.getType() + " " + tw1.act.getFacility().getId() + ": " + tw2.act.getType() + " " + tw2.act.getFacility().getId());
        }
        if (tw1.act.getFacility().getActivityOption(tw1.act.getType()) == null) {
            System.out.println("It's act1 " + tw1.act.getType() + " " + tw1.act.getFacility().getId() + ": " + tw2.act.getType() + " " + tw2.act.getFacility().getId());
        }
        Activity act1 = tw1.act;
        Activity act2 = tw2.act;
        boolean overlap = false;
        if (act2.getFacility().getActivityOption(act2.getType()).equals(act1.getFacility().getActivityOption(act1.getType()))) {
            if (act2.getEndTime() >= act1.getStartTime() && act2.getStartTime() <= act1.getEndTime() && tw1.person.getKnowledge().getEgoNet().knows(tw2.person)) {
                overlap = true;
            }
        }
        return overlap;
    }

    public static double getTimeWindowOverlapDuration(TimeWindow tw1, TimeWindow tw2) {
        double duration = Math.min(tw1.endTime, tw2.endTime) - Math.max(tw1.startTime, tw2.startTime);
        if (duration < 0) duration = 0.;
        return duration;
    }
}
