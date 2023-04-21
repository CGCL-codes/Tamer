package vademecum.math.distance;

public class MaximumDistance implements IDistance {

    public double getDistance(Double[] vector1, Double[] vector2) {
        double maxDist = 0;
        if (vector1.length == vector2.length) for (int i = 0; i < vector1.length; i++) maxDist = Math.max(maxDist, Math.abs(vector1[i] - vector2[i]));
        return maxDist;
    }

    public String toString() {
        return "Maximum";
    }
}
