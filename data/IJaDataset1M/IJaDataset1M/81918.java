package ch.headshot.photomap.client;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import ch.headshot.photomap.client.gpx.WayPoint;
import com.google.gwt.maps.client.overlay.EncodedPolyline;

public class PolylineEncoder {

    private int numLevels = 18;

    private int zoomFactor = 2;

    private double verySmall = 0.00001;

    private boolean forceEndpoints = true;

    private final double[] zoomLevelBreaks;

    private HashMap<String, Double> bounds;

    public PolylineEncoder(int numLevels, int zoomFactor, double verySmall, boolean forceEndpoints) {
        this.numLevels = numLevels;
        this.zoomFactor = zoomFactor;
        this.verySmall = verySmall;
        this.forceEndpoints = forceEndpoints;
        this.zoomLevelBreaks = new double[numLevels];
        for (int i = 0; i < numLevels; i++) {
            this.zoomLevelBreaks[i] = verySmall * Math.pow(this.zoomFactor, numLevels - i - 1);
        }
    }

    public PolylineEncoder() {
        this.zoomLevelBreaks = new double[numLevels];
        for (int i = 0; i < numLevels; i++) {
            this.zoomLevelBreaks[i] = verySmall * Math.pow(this.zoomFactor, numLevels - i - 1);
        }
    }

    /**
	 * Douglas-Peucker algorithm, adapted for encoding
	 * 
	 * @return HashMap [EncodedPoints;EncodedLevels]
	 * 
	 */
    public HashMap<String, String> dpEncode(List<WayPoint> track) {
        int i, maxLoc = 0;
        Stack<int[]> stack = new Stack<int[]>();
        double[] dists = new double[track.size()];
        double maxDist, absMaxDist = 0.0, temp = 0.0;
        int[] current;
        String encodedPoints, encodedLevels;
        if (track.size() > 2) {
            int[] stackVal = new int[] { 0, (track.size() - 1) };
            stack.push(stackVal);
            while (stack.size() > 0) {
                current = stack.pop();
                maxDist = 0;
                for (i = current[0] + 1; i < current[1]; i++) {
                    temp = this.distance(track.get(i), track.get(current[0]), track.get(current[1]));
                    if (temp > maxDist) {
                        maxDist = temp;
                        maxLoc = i;
                        if (maxDist > absMaxDist) {
                            absMaxDist = maxDist;
                        }
                    }
                }
                if (maxDist > this.verySmall) {
                    dists[maxLoc] = maxDist;
                    int[] stackValCurMax = { current[0], maxLoc };
                    stack.push(stackValCurMax);
                    int[] stackValMaxCur = { maxLoc, current[1] };
                    stack.push(stackValMaxCur);
                }
            }
        }
        encodedPoints = createEncodings(track, dists);
        encodedPoints = replace(encodedPoints, "\\", "\\\\");
        encodedLevels = encodeLevels(track, dists, absMaxDist);
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("encodedPoints", encodedPoints);
        hm.put("encodedLevels", encodedLevels);
        return hm;
    }

    public EncodedPolyline dpEncodeToPolyLine(List<WayPoint> track) {
        HashMap<String, String> dpEncode = dpEncode(track);
        return EncodedPolyline.newInstance(dpEncode.get("encodedPoints"), zoomFactor, dpEncode.get("encodedLevels"), numLevels);
    }

    public String replace(String s, String one, String another) {
        if (s.equals("")) return "";
        String res = "";
        int i = s.indexOf(one, 0);
        int lastpos = 0;
        while (i != -1) {
            res += s.substring(lastpos, i) + another;
            lastpos = i + one.length();
            i = s.indexOf(one, lastpos);
        }
        res += s.substring(lastpos);
        return res;
    }

    /**
	 * distance(p0, p1, p2) computes the distance between the point p0 and the
	 * segment [p1,p2]. This could probably be replaced with something that is a
	 * bit more numerically stable.
	 * 
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
    public double distance(WayPoint p0, WayPoint p1, WayPoint p2) {
        double u, out = 0.0;
        if (p1.getLatitude() == p2.getLatitude() && p1.getLongitude() == p2.getLongitude()) {
            out = Math.sqrt(Math.pow(p2.getLatitude() - p0.getLatitude(), 2) + Math.pow(p2.getLongitude() - p0.getLongitude(), 2));
        } else {
            u = ((p0.getLatitude() - p1.getLatitude()) * (p2.getLatitude() - p1.getLatitude()) + (p0.getLongitude() - p1.getLongitude()) * (p2.getLongitude() - p1.getLongitude())) / (Math.pow(p2.getLatitude() - p1.getLatitude(), 2) + Math.pow(p2.getLongitude() - p1.getLongitude(), 2));
            if (u <= 0) {
                out = Math.sqrt(Math.pow(p0.getLatitude() - p1.getLatitude(), 2) + Math.pow(p0.getLongitude() - p1.getLongitude(), 2));
            }
            if (u >= 1) {
                out = Math.sqrt(Math.pow(p0.getLatitude() - p2.getLatitude(), 2) + Math.pow(p0.getLongitude() - p2.getLongitude(), 2));
            }
            if (0 < u && u < 1) {
                out = Math.sqrt(Math.pow(p0.getLatitude() - p1.getLatitude() - u * (p2.getLatitude() - p1.getLatitude()), 2) + Math.pow(p0.getLongitude() - p1.getLongitude() - u * (p2.getLongitude() - p1.getLongitude()), 2));
            }
        }
        return out;
    }

    private static int floor1e5(double coordinate) {
        return (int) Math.floor(coordinate * 1e5);
    }

    private static String encodeSignedNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        return (encodeNumber(sgn_num));
    }

    private static String encodeNumber(int num) {
        StringBuffer encodeString = new StringBuffer();
        while (num >= 0x20) {
            int nextValue = (0x20 | (num & 0x1f)) + 63;
            encodeString.append((char) (nextValue));
            num >>= 5;
        }
        num += 63;
        encodeString.append((char) (num));
        return encodeString.toString();
    }

    /**
	 * Now we can use the previous function to march down the list of points and
	 * encode the levels. Like createEncodings, we ignore points whose distance
	 * (in dists) is undefined.
	 */
    private String encodeLevels(List<WayPoint> points, double[] dists, double absMaxDist) {
        int i;
        StringBuffer encoded_levels = new StringBuffer();
        if (this.forceEndpoints) {
            encoded_levels.append(encodeNumber(this.numLevels - 1));
        } else {
            encoded_levels.append(encodeNumber(this.numLevels - computeLevel(absMaxDist) - 1));
        }
        for (i = 1; i < points.size() - 1; i++) {
            if (dists[i] != 0) {
                encoded_levels.append(encodeNumber(this.numLevels - computeLevel(dists[i]) - 1));
            }
        }
        if (this.forceEndpoints) {
            encoded_levels.append(encodeNumber(this.numLevels - 1));
        } else {
            encoded_levels.append(encodeNumber(this.numLevels - computeLevel(absMaxDist) - 1));
        }
        return encoded_levels.toString();
    }

    /**
	 * This computes the appropriate zoom level of a point in terms of it's
	 * distance from the relevant segment in the DP algorithm. Could be done in
	 * terms of a logarithm, but this approach makes it a bit easier to ensure
	 * that the level is not too large.
	 */
    private int computeLevel(double absMaxDist) {
        int lev = 0;
        if (absMaxDist > this.verySmall) {
            lev = 0;
            while (absMaxDist < this.zoomLevelBreaks[lev]) {
                lev++;
            }
            return lev;
        }
        return lev;
    }

    private String createEncodings(List<WayPoint> points, double[] dists) {
        StringBuffer encodedPoints = new StringBuffer();
        double maxlat = 0, minlat = 0, maxlon = 0, minlon = 0;
        int plat = 0;
        int plng = 0;
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                maxlat = minlat = points.get(i).getLatitude();
                maxlon = minlon = points.get(i).getLongitude();
            } else {
                if (points.get(i).getLatitude() > maxlat) {
                    maxlat = points.get(i).getLatitude();
                } else if (points.get(i).getLatitude() < minlat) {
                    minlat = points.get(i).getLatitude();
                } else if (points.get(i).getLongitude() > maxlon) {
                    maxlon = points.get(i).getLongitude();
                } else if (points.get(i).getLongitude() < minlon) {
                    minlon = points.get(i).getLongitude();
                }
            }
            if (dists[i] != 0 || i == 0 || i == points.size() - 1) {
                WayPoint point = points.get(i);
                int late5 = floor1e5(point.getLatitude());
                int lnge5 = floor1e5(point.getLongitude());
                int dlat = late5 - plat;
                int dlng = lnge5 - plng;
                plat = late5;
                plng = lnge5;
                encodedPoints.append(encodeSignedNumber(dlat));
                encodedPoints.append(encodeSignedNumber(dlng));
            }
        }
        HashMap<String, Double> bounds = new HashMap<String, Double>();
        bounds.put("maxlat", new Double(maxlat));
        bounds.put("minlat", new Double(minlat));
        bounds.put("maxlon", new Double(maxlon));
        bounds.put("minlon", new Double(minlon));
        this.setBounds(bounds);
        return encodedPoints.toString();
    }

    private void setBounds(HashMap<String, Double> bounds) {
        this.bounds = bounds;
    }

    public static HashMap createEncodings(List<WayPoint> track, int level, int step) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        StringBuffer encodedPoints = new StringBuffer();
        StringBuffer encodedLevels = new StringBuffer();
        List WayPointList = track;
        int plat = 0;
        int plng = 0;
        int counter = 0;
        int listSize = WayPointList.size();
        WayPoint WayPoint;
        for (int i = 0; i < listSize; i += step) {
            counter++;
            WayPoint = (WayPoint) WayPointList.get(i);
            int late5 = floor1e5(WayPoint.getLatitude());
            int lnge5 = floor1e5(WayPoint.getLongitude());
            int dlat = late5 - plat;
            int dlng = lnge5 - plng;
            plat = late5;
            plng = lnge5;
            encodedPoints.append(encodeSignedNumber(dlat)).append(encodeSignedNumber(dlng));
            encodedLevels.append(encodeNumber(level));
        }
        System.out.println("listSize: " + listSize + " step: " + step + " counter: " + counter);
        resultMap.put("encodedPoints", encodedPoints.toString());
        resultMap.put("encodedLevels", encodedLevels.toString());
        return resultMap;
    }

    public HashMap<String, Double> getBounds() {
        return bounds;
    }
}
