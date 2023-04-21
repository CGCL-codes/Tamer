package org.micthemodel.helpers;

import org.micthemodel.factory.Parameters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class stores the change in the free surface of a grain with
 * the change of radius.
 *
 * @author bishnoi
 */
public class SurfaceHistory {

    private ArrayList<HistoryPoint> history;

    /** Creates a new instance of SurfaceHistory */
    public SurfaceHistory() {
        this.history = new ArrayList<HistoryPoint>(0);
    }

    public void add(float area, double radius) {
        HistoryPoint maxFront = this.maxFront();
        if (radius < maxFront.radius) {
            return;
        }
        if (radius == maxFront.radius) {
            maxFront.area = area;
            return;
        }
        if (radius == maxFront.radius) {
            maxFront.area = area;
            return;
        }
        if (area == this.maxFront().area) {
            return;
        }
        HistoryPoint historyPoint = new HistoryPoint(area, radius);
        this.history.add(historyPoint);
    }

    public void removeAbove(double radius) {
        int location = this.findRadius(radius);
        if (location < 0) {
            location = -location - 1;
        }
        this.history = new ArrayList<HistoryPoint>(this.history.subList(0, location - 1));
    }

    public void removeBelow(double radius) {
        int location = this.findRadius(radius);
        if (location < 0) {
            location = -location - 2;
        }
        if (location <= 0) {
            return;
        }
        this.history = new ArrayList<HistoryPoint>(this.history.subList(location, this.history.size() - 1));
    }

    /**
     * This method returns the free area at a particular radius.
     * The free area returned is the same as that for the immediate
     * lower radius.
     */
    public float getArea(double radius) {
        if (this.history.isEmpty() || radius < this.minRad()) {
            return 1.0f;
        }
        int location = this.findRadius(radius);
        if (location >= 0) {
            return this.history.get(location).area;
        }
        if (location == -1) {
            return 1.0f;
        }
        if (this.history.size() <= -location) {
            return this.maxFront().area;
        }
        return this.history.get(-location - 1).area;
    }

    /**
     * This method returns the free area at a particular location.
     * If the parameter is negative, 1.0 is returned
     */
    public float getArea(int location) {
        if (this.history.isEmpty() || location < 0) {
            return 1.0f;
        }
        if (location > this.history.size() - 1) {
            location = this.history.size() - 1;
        }
        return this.history.get(location).area;
    }

    /**
     * This method returns the volume at a particular history.
     * If the parameter is negative, 0 is returned
     */
    private double getVolume(int location) {
        if (this.history.isEmpty() || location < 0) {
            return 0.0;
        }
        if (location > this.history.size() - 1) {
            location = this.history.size() - 1;
        }
        double total = 0.0;
        double lastRadius = 0.0;
        for (int i = 0; i <= location; i++) {
            HistoryPoint historyPoint = this.history.get(i);
            total += (Parameters.cube(historyPoint.radius) - Parameters.cube(lastRadius)) * historyPoint.area;
            lastRadius = historyPoint.radius;
        }
        return total;
    }

    /**
     * This method returns the radius at a particular history.
     * If the parameter is negative, 0 is returned
     */
    private double getRadius(int location) {
        if (this.history.isEmpty() || location < 0) {
            return 0.0;
        }
        if (location > this.history.size() - 1) {
            location = this.history.size() - 1;
        }
        return this.history.get(location).radius;
    }

    /**
     * This method returns the history at a particular history.
     * If the parameter is negative, a new History is returned
     */
    private HistoryPoint getHistory(int location) {
        if (this.history.isEmpty() || location < 0) {
            return new HistoryPoint();
        }
        if (location > this.history.size() - 1) {
            location = this.history.size() - 1;
        }
        return this.history.get(location);
    }

    public double volume(double radius) {
        double total = 0.0;
        double lastRadius = 0.0;
        double lastArea = 1.0;
        for (HistoryPoint historyPoint : this.history) {
            if (historyPoint.radius <= radius) {
                total += (Parameters.cube(historyPoint.radius) - Parameters.cube(lastRadius)) * lastArea;
                lastRadius = historyPoint.radius;
                lastArea = historyPoint.area;
                continue;
            } else {
                if (lastRadius <= radius) {
                    total += (Parameters.cube(radius) - Parameters.cube(lastRadius)) * lastArea;
                    lastRadius = historyPoint.radius;
                    lastArea = historyPoint.area;
                    break;
                }
            }
            total += (Parameters.cube(historyPoint.radius) - Parameters.cube(lastRadius)) * lastArea;
            lastRadius = historyPoint.radius;
            lastArea = historyPoint.area;
        }
        if (radius > lastRadius) {
            total += (Parameters.cube(radius) - Parameters.cube(lastRadius)) * lastArea;
        }
        return total;
    }

    /**
     * This method returns the volume of the grain between two radii.
     * The volume is progressively calculated between the given radii.
     * The returned value is in r-cube.
     */
    public double volume(double innerRad, double outerRad) {
        return this.volume(outerRad) - this.volume(innerRad);
    }

    /**
     * This method returns the radius at which the given volume
     * is reached.
     */
    public double getRadius(double volume) {
        double total = 0.0;
        double lastRadius = 0.0;
        double lastArea = 1.0;
        for (HistoryPoint historyPoint : this.history) {
            double historyVolume = (Parameters.cube(historyPoint.radius) - Parameters.cube(lastRadius)) * lastArea;
            if (total + historyVolume > volume) {
                return Parameters.cbrt((volume - total) / lastArea + Parameters.cube(lastRadius));
            }
            total += historyVolume;
            if (total == volume) {
                return historyPoint.radius;
            }
            lastRadius = historyPoint.radius;
            lastArea = historyPoint.area;
        }
        return Parameters.cbrt((volume - total) / lastArea + Parameters.cube(lastRadius));
    }

    /**
     * This method returns the radius at which the given volume
     * is reached starting the given radius.
     */
    public double getRadius(double volume, double fromRadius) {
        double total = 0.0;
        double lastRadius = 0.0;
        double lastArea = 1.0;
        for (HistoryPoint historyPoint : this.history) {
            if (historyPoint.radius < fromRadius) {
                lastRadius = historyPoint.radius;
                lastArea = historyPoint.area;
                continue;
            }
            if (lastRadius < fromRadius) {
                total += (Parameters.cube(historyPoint.radius) - Parameters.cube(fromRadius)) * lastArea;
                if (total == volume) {
                    return historyPoint.radius;
                }
                if (total > volume) {
                    return Parameters.cbrt(volume / lastArea + Parameters.cube(fromRadius));
                }
                lastRadius = historyPoint.radius;
                lastArea = historyPoint.area;
                continue;
            }
            double historyVolume = (Parameters.cube(historyPoint.radius) - Parameters.cube(lastRadius)) * lastArea;
            if (total + historyVolume > volume) {
                return Parameters.cbrt((volume - total) / lastArea + Parameters.cube(lastRadius));
            }
            total += historyVolume;
            if (total == volume) {
                return historyPoint.radius;
            }
            lastRadius = historyPoint.radius;
            lastArea = historyPoint.area;
        }
        if (lastRadius < fromRadius) {
            lastRadius = fromRadius;
        }
        return Parameters.cbrt((volume - total) / lastArea + Parameters.cube(lastRadius));
    }

    /**
     * This method returns the index of the location in the list
     * where the given radius is present. If the radius is not present
     * in the -index -1 of the location where the radius will be inserted
     * is returned.
     */
    private int findRadius(double radius) {
        if (this.isEmpty()) {
            return -1;
        }
        int low = 0;
        int high = this.history.size() - 1;
        while (low <= high) {
            int mid = (low + high) >> 1;
            HistoryPoint midVal = this.history.get(mid);
            if (midVal.radius < radius) {
                low = mid + 1;
            } else if (midVal.radius > radius) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    /**
     * This method returns the index of the location in the list
     * where the given radius is present. If the radius is not present
     * in the -index -1 of the location where the radius will be inserted
     * is returned.
     */
    private int findVolume(double volume) {
        int location = -1;
        double total = 0.0;
        double lastRadius = 0.0;
        double lastArea = 0.0;
        for (HistoryPoint historyPoint : this.history) {
            location++;
            total += (Parameters.cube(historyPoint.radius) - Parameters.cube(lastRadius)) * lastArea;
            if (total == volume) {
                return location;
            }
            if (total > volume) {
                return -location;
            }
        }
        return location;
    }

    /**
     * This method reorders the history list in the order of increasing radii.
     */
    private void reorder() {
        Collections.sort(this.history, new HistoryComparator());
    }

    /**
     * Returns the smallest radius in the history list. This value is effectively
     * the point over which the area can have a value less than 1.
     */
    private double minRad() {
        return (this.history.isEmpty() ? 0.0 : this.history.get(0).radius);
    }

    /**
     * Returns the maximum radius in this history.
     */
    private double maxRad() {
        return (this.history.isEmpty() ? 0.0 : this.history.get(this.history.size() - 1).radius);
    }

    /**
     * Returns the innermost history.
     */
    private HistoryPoint minFront() {
        return (this.history.size() == 0 ? new HistoryPoint(1.0f, Double.POSITIVE_INFINITY) : this.history.get(0));
    }

    /**
     * Returns the outermost history.
     */
    private HistoryPoint maxFront() {
        return (this.isEmpty() ? new HistoryPoint() : this.history.get(this.history.size() - 1));
    }

    /**
     * Indicates if this is empty.
     */
    private boolean isEmpty() {
        return this.history.isEmpty();
    }

    /**
     * @return the history
     */
    public ArrayList<HistoryPoint> getHistory() {
        return history;
    }

    /**
     * This inner class is the wrapper for the area and radius information.
     */
    public class HistoryPoint {

        public float area;

        public double radius;

        public HistoryPoint() {
            this.area = 1.0f;
            this.radius = 0.0;
        }

        public HistoryPoint(float area, double radius) {
            this.area = area;
            this.radius = radius;
        }
    }

    /**
     * This comparator is to sort the historys in the order of increasing radii.
     */
    private static class HistoryComparator implements Comparator<HistoryPoint> {

        @Override
        public int compare(HistoryPoint history1, HistoryPoint history2) {
            return Double.compare(history1.radius, history2.radius);
        }
    }
}
