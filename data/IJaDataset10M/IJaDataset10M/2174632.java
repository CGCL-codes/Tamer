package pathfinder.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import utilities.MathUtil;
import world.modifier.Pathable;

/**
 * defines a grouping system that groups pathable objects
 * @author Secondary
 *
 */
public class PathableGrouper {

    /**
	 * groups pathable objects by proximity to one another
	 * @param pathables the pathable objects to be grouped
	 * @param minDist the minimum distance separating objects from at least one
	 * other object in a group
	 * @param maxGroupSize the maximum pathable objects allowed in a single group
	 */
    public static Group[] groupPathables(HashSet<Pathable> pathables, double minDist, int maxGroupSize) {
        ArrayList<HashSet<Pathable>> groups = new ArrayList<HashSet<Pathable>>();
        HashSet<Pathable> sorted = new HashSet<Pathable>();
        HashMap<Pathable, HashSet<Pathable>> groupMap = new HashMap<Pathable, HashSet<Pathable>>();
        HashMap<HashSet<Pathable>, double[]> locations = new HashMap<HashSet<Pathable>, double[]>();
        HashMap<HashSet<Pathable>, Double> radii = new HashMap<HashSet<Pathable>, Double>();
        for (Pathable p : pathables) {
            if (!sorted.contains(p)) {
                for (Pathable temp : pathables) {
                    if (temp != p && MathUtil.distance(p.getLocation()[0], p.getLocation()[1], temp.getLocation()[0], temp.getLocation()[1]) - p.getRadius() - temp.getRadius() <= minDist) {
                        if (groupMap.get(temp) != null) {
                            if (groupMap.get(temp).size() < maxGroupSize) {
                                if (groupMap.get(p) == null) {
                                    addToGroup(p, groupMap.get(temp), groupMap, sorted, locations, radii);
                                    break;
                                } else if (groupMap.get(p).size() + groupMap.get(temp).size() < maxGroupSize) {
                                    mergeGroups(groupMap.get(p), groupMap.get(temp), groupMap, groups, locations, radii);
                                }
                            }
                        } else {
                            if (!sorted.contains(p)) {
                                HashSet<Pathable> newGroup = new HashSet<Pathable>();
                                newGroup.add(p);
                                groupMap.put(p, newGroup);
                                groups.add(newGroup);
                                sorted.add(p);
                                locations.put(newGroup, p.getLocation());
                                radii.put(newGroup, p.getRadius());
                            }
                            if (groupMap.get(p).size() < maxGroupSize) {
                                addToGroup(temp, groupMap.get(p), groupMap, sorted, locations, radii);
                            }
                        }
                    }
                }
            }
        }
        Group[] g = new Group[groups.size()];
        int index = 0;
        for (HashSet<Pathable> group : groups) {
            g[index] = new Group(group, locations.get(group), radii.get(group));
            index++;
        }
        return g;
    }

    /**
	 * merges the two passed groups, the smaller group's elements are added to the larger group
	 * @param group1
	 * @param group2
	 * @param groupMap
	 * @param groups
	 * @param locations
	 */
    private static void mergeGroups(HashSet<Pathable> group1, HashSet<Pathable> group2, HashMap<Pathable, HashSet<Pathable>> groupMap, ArrayList<HashSet<Pathable>> groups, HashMap<HashSet<Pathable>, double[]> locations, HashMap<HashSet<Pathable>, Double> radii) {
        HashSet<Pathable> smallGroup = group1.size() < group2.size() ? group1 : group2;
        HashSet<Pathable> largeGroup = group1.size() < group2.size() ? group2 : group1;
        largeGroup.addAll(smallGroup);
        for (Pathable pathable : smallGroup) {
            groupMap.put(pathable, largeGroup);
        }
        groups.remove(smallGroup);
        double[] l = locations.get(smallGroup);
        locations.remove(smallGroup);
        l[0] = (l[0] + locations.get(largeGroup)[0]) / 2;
        l[1] = (l[1] + locations.get(largeGroup)[1]) / 2;
        locations.put(largeGroup, l);
        if (radii.get(smallGroup) > radii.get(largeGroup)) {
            radii.put(largeGroup, radii.get(smallGroup));
        }
        radii.remove(smallGroup);
    }

    /**
	 * properly adds the passes pathable object to the specified group
	 * @param p
	 * @param group
	 * @param groupMap
	 * @param sorted
	 * @param locations
	 */
    private static void addToGroup(Pathable p, HashSet<Pathable> group, HashMap<Pathable, HashSet<Pathable>> groupMap, HashSet<Pathable> sorted, HashMap<HashSet<Pathable>, double[]> locations, HashMap<HashSet<Pathable>, Double> radii) {
        group.add(p);
        groupMap.put(p, group);
        sorted.add(p);
        double[] l = locations.get(group);
        l[0] = (l[0] + p.getLocation()[0]) / 2;
        l[1] = (l[1] + p.getLocation()[1]) / 2;
        locations.put(group, l);
        if (radii.get(group) < p.getRadius()) {
            radii.put(group, p.getRadius());
        }
    }
}
