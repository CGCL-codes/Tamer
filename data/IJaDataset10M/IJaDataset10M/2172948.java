package net.redlightning.dht.kad.utils;

import java.io.PrintStream;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.*;
import net.redlightning.dht.kad.Key;
import net.redlightning.dht.kad.Prefix;
import android.util.Log;

/**
 * @author The_8472, Damokles
 */
public class PopulationEstimator {

    static final int KEYSPACE_BITS = Key.KEY_BITS;

    static final double KEYSPACE_SIZE = Math.pow(2, KEYSPACE_BITS);

    static final double DISTANCE_INITIAL_WEIGHT = 0.1;

    static final int INITIAL_WEIGHT_COUNT = 20;

    static final double DISTANCE_WEIGHT = 0.03;

    private double averageNodeDistanceExp2 = KEYSPACE_BITS;

    private int updateCount = 0;

    private List<PopulationListener> listeners = new ArrayList<PopulationListener>(1);

    private static final int MAX_RECENT_LOOKUP_CACHE_SIZE = 40;

    private static final String TAG = PopulationEstimator.class.getSimpleName();

    private LinkedList<Prefix> recentlySeenPrefixes = new LinkedList<Prefix>();

    public long getEstimate() {
        return (long) (Math.pow(2, KEYSPACE_BITS - averageNodeDistanceExp2 + 0.6180339));
    }

    public double getRawDistanceEstimate() {
        return averageNodeDistanceExp2;
    }

    public void setInitialRawDistanceEstimate(double initialValue) {
        averageNodeDistanceExp2 = initialValue;
        if (averageNodeDistanceExp2 > KEYSPACE_BITS) averageNodeDistanceExp2 = KEYSPACE_BITS;
    }

    public void update(SortedSet<Key> neighbors) {
        if (neighbors.size() < 4) return;
        double[] distances = new double[neighbors.size() - 1];
        Log.d(TAG, "Estimator: new node group of " + neighbors.size());
        Prefix prefix = Prefix.getCommonPrefix(neighbors);
        synchronized (recentlySeenPrefixes) {
            for (Prefix oldPrefix : recentlySeenPrefixes) {
                if (oldPrefix.isPrefixOf(prefix)) {
                    recentlySeenPrefixes.remove(oldPrefix);
                    recentlySeenPrefixes.addLast(prefix);
                    return;
                }
                if (prefix.isPrefixOf(oldPrefix)) return;
            }
            recentlySeenPrefixes.addLast(prefix);
            if (recentlySeenPrefixes.size() > MAX_RECENT_LOOKUP_CACHE_SIZE) recentlySeenPrefixes.removeFirst();
        }
        Key previous = null;
        int i = 0;
        for (Key entry : neighbors) {
            if (previous == null) {
                previous = entry;
                continue;
            }
            byte[] rawDistance = previous.distance(entry).getHash();
            double distance = 0;
            int nonZeroBytes = 0;
            for (int j = 0; j < Key.SHA1_HASH_LENGTH; j++) {
                if (rawDistance[j] == 0) {
                    continue;
                }
                if (nonZeroBytes == 8) {
                    break;
                }
                nonZeroBytes++;
                distance += (rawDistance[j] & 0xFF) * Math.pow(2, KEYSPACE_BITS - (j + 1) * 8);
            }
            distance = Math.log(distance) / Math.log(2);
            Log.d(TAG, "Estimator: distance value #" + updateCount + ": " + distance + " avg:" + averageNodeDistanceExp2);
            distances[i++] = distance;
            previous = entry;
        }
        double weight;
        Arrays.sort(distances);
        weight = updateCount < INITIAL_WEIGHT_COUNT ? DISTANCE_INITIAL_WEIGHT : DISTANCE_WEIGHT;
        updateCount++;
        double middle = (distances.length - 1.0) / 2.0;
        int idx1 = (int) Math.floor(middle);
        int idx2 = (int) Math.ceil(middle);
        double middleWeight = middle - idx1;
        double median = distances[idx1] * (1.0 - middleWeight) + distances[idx2] * middleWeight;
        synchronized (PopulationEstimator.class) {
            averageNodeDistanceExp2 = median * weight + averageNodeDistanceExp2 * (1. - weight);
        }
        Log.d(TAG, "Estimator: new estimate:" + getEstimate());
        fireUpdateEvent();
    }

    public void addListener(PopulationListener l) {
        listeners.add(l);
    }

    public void removeListener(PopulationListener l) {
        listeners.remove(l);
    }

    private void fireUpdateEvent() {
        long estimated = getEstimate();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).populationUpdated(estimated);
        }
    }

    public static void main(String[] args) throws Exception {
        PrintStream out = new PrintStream("dump.txt");
        Random rand = new Random();
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.GERMANY);
        formatter.setMaximumFractionDigits(30);
        List<Key> keyspace = new ArrayList<Key>(5000000);
        for (int i = 0; i < 5000; i++) keyspace.add(Key.createRandomKey());
        Collections.sort(keyspace);
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 3; j++) {
                Key target = Key.createRandomKey();
                TreeSet<Key> closestSet = new TreeSet<Key>(new Key.DistanceOrder(target));
                int sizeGoal = 5 + rand.nextInt(4);
                closestSet.addAll(keyspace);
                while (closestSet.size() > sizeGoal) closestSet.remove(closestSet.last());
                double[] distances = new double[closestSet.size() - 1];
                Key previous = null;
                int k = 0;
                for (Key entry : closestSet) {
                    if (previous == null) {
                        previous = entry;
                        continue;
                    }
                    byte[] rawDistance = previous.distance(entry).getHash();
                    double distance = 0;
                    distance = new BigInteger(rawDistance).doubleValue();
                    distance = Math.log(distance) / Math.log(2);
                    distances[k++] = distance;
                    previous = entry;
                }
                Arrays.sort(distances);
                double middle = (distances.length - 1.0) / 2.0;
                int idx1 = (int) Math.floor(middle);
                int idx2 = (int) Math.ceil(middle);
                double middleWeight = middle - idx1;
                double median = distances[idx1] * (1.0 - middleWeight) + distances[idx2] * middleWeight;
                out.println(distances.length + "\t" + keyspace.size() + "\t" + formatter.format(median));
            }
            int newGoal = (int) (keyspace.size() * 1.008);
            System.out.println(i + ": " + newGoal);
            while (keyspace.size() < newGoal) keyspace.add(Key.createRandomKey());
            Collections.sort(keyspace);
        }
        out.close();
    }
}
