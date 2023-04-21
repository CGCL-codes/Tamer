package graphmatcher.matcher.komatcher;

import graphmatcher.graph.Edge;
import graphmatcher.graph.Graph;
import graphmatcher.graph.Vertex;
import graphmatcher.helper.AngleHelper;
import graphmatcher.helper.DistanceHelper;
import graphmatcher.matcher.MatchingOptions;
import java.awt.Point;
import java.awt.Rectangle;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class EdgeRater {

    private static Logger logger = Logger.getLogger(EdgeRater.class);

    static {
        logger.setLevel(Level.FATAL);
    }

    public static double computeInitialEdgeRating(Graph pattern, int patternEdge, Graph template, int templateEdge, boolean[][] match1on1Array, MatchingOptions matchingOptions) {
        boolean match1on1 = match1On1(pattern, template, patternEdge, templateEdge, matchingOptions.isNoRotation());
        match1on1Array[patternEdge][templateEdge] = match1on1;
        Edge pEdge = pattern.virtualEdges()[patternEdge];
        double pEdgeLength = DistanceHelper.getLength(pEdge, pattern);
        double pMaxEdgeLength = pattern.getLengthOfLongestEdge();
        double pMaxDistanceToCenter = pattern.getMaximumVertexDistanceToCenter();
        double pLengthBonus = getBonus(pEdgeLength, pMaxEdgeLength);
        Edge tEdge = template.virtualEdges()[templateEdge];
        double tEdgeLength = DistanceHelper.getLength(tEdge, template);
        double tMaxEdgeLength = template.getLengthOfLongestEdge();
        double tMaxDistanceToCenter = template.getMaximumVertexDistanceToCenter();
        double tLengthBonus = getBonus(tEdgeLength, tMaxEdgeLength);
        double lenghtBonus = Math.min(pLengthBonus, tLengthBonus);
        Vertex pVertex1 = pattern.vertices()[pEdge.vertex1];
        Vertex pVertex2 = pattern.vertices()[pEdge.vertex2];
        Vertex tVertex1 = template.vertices()[tEdge.vertex1];
        Vertex tVertex2 = template.vertices()[tEdge.vertex2];
        if (matchingOptions.isNoRotation()) {
            Point pCenter = pattern.getCenter();
            Point pHelp = new Point(pCenter.x + 10, pCenter.y);
            Point tCenter = template.getCenter();
            Point tHelp = new Point(tCenter.x + 10, tCenter.y);
            double anglePV1, anglePV2, angleTV1, angleTV2;
            if (match1on1) {
                anglePV1 = AngleHelper.getAngle(pCenter, pHelp, pVertex1.toPoint());
                anglePV2 = AngleHelper.getAngle(pCenter, pHelp, pVertex2.toPoint());
                angleTV1 = AngleHelper.getAngle(tCenter, tHelp, tVertex1.toPoint());
                angleTV2 = AngleHelper.getAngle(tCenter, tHelp, tVertex2.toPoint());
            } else {
                anglePV1 = AngleHelper.getAngle(pCenter, pHelp, pVertex1.toPoint());
                anglePV2 = AngleHelper.getAngle(pCenter, pHelp, pVertex2.toPoint());
                angleTV1 = AngleHelper.getAngle(tCenter, tHelp, tVertex2.toPoint());
                angleTV2 = AngleHelper.getAngle(tCenter, tHelp, tVertex1.toPoint());
            }
            double rating = rateAngle(anglePV1, angleTV1) + rateAngle(anglePV2, angleTV2);
            rating = rating / 2;
            if (rating < 0.75) {
                return 0;
            }
        }
        double pV1Dist = DistanceHelper.getDistance(pVertex1.toPoint(), pattern.getCenter());
        double pV2Dist = DistanceHelper.getDistance(pVertex2.toPoint(), pattern.getCenter());
        double tV1Dist = DistanceHelper.getDistance(tVertex1.toPoint(), template.getCenter());
        double tV2Dist = DistanceHelper.getDistance(tVertex2.toPoint(), template.getCenter());
        double bestDistToCenterRatio;
        if (match1on1) {
            bestDistToCenterRatio = (rate(pV1Dist, pMaxDistanceToCenter, tV1Dist, tMaxDistanceToCenter) + rate(pV2Dist, pMaxDistanceToCenter, tV2Dist, tMaxDistanceToCenter)) / 2;
        } else {
            bestDistToCenterRatio = (rate(pV1Dist, pMaxDistanceToCenter, tV2Dist, tMaxDistanceToCenter) + rate(pV2Dist, pMaxDistanceToCenter, tV1Dist, tMaxDistanceToCenter)) / 2;
        }
        double lengthRatio = rate(pEdgeLength, pMaxDistanceToCenter, tEdgeLength, tMaxDistanceToCenter);
        double pAngle, tAngle;
        if (match1on1) {
            pAngle = AngleHelper.getAngle(pVertex1.toPoint(), pattern.getCenter(), pVertex2.toPoint());
            tAngle = AngleHelper.getAngle(tVertex1.toPoint(), template.getCenter(), tVertex2.toPoint());
        } else {
            pAngle = AngleHelper.getAngle(pVertex1.toPoint(), pattern.getCenter(), pVertex2.toPoint());
            tAngle = AngleHelper.getAngle(tVertex2.toPoint(), template.getCenter(), tVertex1.toPoint());
        }
        double angleDiff = AngleHelper.getDiffAngle(pAngle, tAngle);
        double angleRatio = (180 - angleDiff) / 180;
        angleRatio += lenghtBonus + 0.05;
        if (angleRatio > 1) {
            angleRatio = 1;
        }
        double ratio = (bestDistToCenterRatio + lengthRatio + angleRatio) / 3;
        if (ratio > 1) {
            ratio = 1;
        }
        return ratio;
    }

    private static double getNormedDistance(Point p1, Point pCenter, double pNorm, Point t1, Point tCenter, double tNorm) {
        double px = (p1.x - pCenter.x) / pNorm;
        double py = (p1.y - pCenter.y) / pNorm;
        double tx = (t1.x - tCenter.x) / tNorm;
        double ty = (t1.y - tCenter.y) / tNorm;
        double x = px - tx;
        double y = py - ty;
        return Math.sqrt(x * x + y * y);
    }

    public static double rateMatchingPair(Graph pattern, Graph template, int patternEdge1, int templateEdge1, int patternEdge2, int templateEdge2, boolean[][] match1on1Array) {
        Edge pEdge1 = pattern.virtualEdges()[patternEdge1];
        Edge tEdge1 = template.virtualEdges()[templateEdge1];
        Edge pEdge2 = pattern.virtualEdges()[patternEdge2];
        Edge tEdge2 = template.virtualEdges()[templateEdge2];
        boolean match1On1_1 = match1on1Array[patternEdge1][templateEdge1];
        boolean match1On1_2 = match1on1Array[patternEdge2][templateEdge2];
        double ratio = rateMatchingPair2(pattern, template, pEdge1, tEdge1, match1On1_1, pEdge2, tEdge2, match1On1_2);
        return ratio;
    }

    public static boolean[][] getMatch1On1Array(Graph pattern, Graph template) {
        boolean[][] match1on1 = new boolean[pattern.virtualEdges().length][template.virtualEdges().length];
        for (int i = 0; i < pattern.virtualEdges().length; i++) {
            for (int j = 0; j < template.virtualEdges().length; j++) {
                match1on1[i][j] = match1On1(pattern, template, i, j, false);
            }
        }
        return match1on1;
    }

    private static boolean match1On1(Graph pattern, Graph template, int patternEdgeID, int templateEdgeID, boolean noRotation) {
        Edge patternEdge = pattern.virtualEdges()[patternEdgeID];
        Edge templateEdge = template.virtualEdges()[templateEdgeID];
        Point pV1 = pattern.vertices()[patternEdge.vertex1].toPoint();
        Point pV2 = pattern.vertices()[patternEdge.vertex2].toPoint();
        Point tV1 = template.vertices()[templateEdge.vertex1].toPoint();
        Point tV2 = template.vertices()[templateEdge.vertex2].toPoint();
        double pRelLength = DistanceHelper.getDistance(pV1, pV2) / pattern.getAverageDistanceToCenterInGraph();
        double tRelLength = DistanceHelper.getDistance(tV1, tV2) / template.getAverageDistanceToCenterInGraph();
        double pV1RelDist = DistanceHelper.getDistance(pV1, pattern.getCenter()) / pattern.getAverageDistanceToCenterInGraph();
        double pV2RelDist = DistanceHelper.getDistance(pV2, pattern.getCenter()) / pattern.getAverageDistanceToCenterInGraph();
        double tV1RelDist = DistanceHelper.getDistance(tV1, template.getCenter()) / template.getAverageDistanceToCenterInGraph();
        double tV2RelDist = DistanceHelper.getDistance(tV2, template.getCenter()) / template.getAverageDistanceToCenterInGraph();
        boolean pV1FarAway = pV1RelDist > pV2RelDist;
        double pDelta;
        if (pV1FarAway) {
            pDelta = pV1RelDist - pV2RelDist;
        } else {
            pDelta = pV2RelDist - pV1RelDist;
        }
        boolean tV1FarAway = tV1RelDist > tV2RelDist;
        double tDelta;
        if (tV1FarAway) {
            tDelta = tV1RelDist - tV2RelDist;
        } else {
            tDelta = tV2RelDist - tV1RelDist;
        }
        double pRatio = pDelta / pRelLength;
        double tRatio = tDelta / tRelLength;
        if (pRatio > 0.5 && tRatio > 0.5) {
            if (pV1FarAway == tV1FarAway) {
                return true;
            } else {
                return false;
            }
        }
        double pAngle = AngleHelper.getAngle(pattern.getCenter(), pV1, pV2);
        if (roughly180Degree(pAngle)) {
            if (noRotation) {
                Point pCenter = pattern.getCenter();
                Point pHelp = new Point(pCenter.x + 10, pCenter.y);
                Point tCenter = template.getCenter();
                Point tHelp = new Point(tCenter.x + 10, tCenter.y);
                double pv1Angle = AngleHelper.getAngle(pCenter, pHelp, pV1);
                double pv2Angle = AngleHelper.getAngle(pCenter, pHelp, pV2);
                double tv1Angle = AngleHelper.getAngle(tCenter, tHelp, tV1);
                double tv2Angle = AngleHelper.getAngle(tCenter, tHelp, tV2);
                double m1on1 = maxMinusMin(pv1Angle, tv1Angle) + maxMinusMin(pv2Angle, tv2Angle);
                double m1on2 = maxMinusMin(pv1Angle, tv2Angle) + maxMinusMin(pv2Angle, tv1Angle);
                if (m1on1 < m1on2) {
                    return true;
                } else {
                    return false;
                }
            }
            return pV1FarAway == tV1FarAway;
        }
        boolean pV1Left = pAngle > 180;
        double tAngle = AngleHelper.getAngle(template.getCenter(), tV1, tV2);
        if (roughly180Degree(tAngle)) {
            return pV1FarAway == tV1FarAway;
        }
        boolean tV1Left = tAngle > 180;
        boolean result = pV1Left == tV1Left;
        return result;
    }

    private static double maxMinusMin(double a, double b) {
        return Math.max(a, b) - Math.min(a, b);
    }

    private static double rateMatchingPair2(Graph pattern, Graph template, Edge pEdgeA, Edge tEdgeA, boolean match1To1A, Edge pEdgeB, Edge tEdgeB, boolean match1To1B) {
        double averageDistanceInPattern = pattern.getAverageDistanceToCenterInGraph();
        double averageDistanceInTemplate = template.getAverageDistanceToCenterInGraph();
        Vertex v1pa, v2pa, v1ta, v2ta;
        v1pa = pattern.vertices()[pEdgeA.vertex1];
        v2pa = pattern.vertices()[pEdgeA.vertex2];
        if (match1To1A) {
            v1ta = template.vertices()[tEdgeA.vertex1];
            v2ta = template.vertices()[tEdgeA.vertex2];
        } else {
            v1ta = template.vertices()[tEdgeA.vertex2];
            v2ta = template.vertices()[tEdgeA.vertex1];
        }
        Vertex v1pb, v2pb, v1tb, v2tb;
        v1pb = pattern.vertices()[pEdgeB.vertex1];
        v2pb = pattern.vertices()[pEdgeB.vertex2];
        if (match1To1B) {
            v1tb = template.vertices()[tEdgeB.vertex1];
            v2tb = template.vertices()[tEdgeB.vertex2];
        } else {
            v1tb = template.vertices()[tEdgeB.vertex2];
            v2tb = template.vertices()[tEdgeB.vertex1];
        }
        double dist_v1pa_v1pb = DistanceHelper.getDistance(v1pa, v1pb);
        double dist_v1ta_v1tb = DistanceHelper.getDistance(v1ta, v1tb);
        double dist_v1pa_v2pb = DistanceHelper.getDistance(v1pa, v2pb);
        double dist_v1ta_v2tb = DistanceHelper.getDistance(v1ta, v2tb);
        double dist_v2pa_v1pb = DistanceHelper.getDistance(v2pa, v1pb);
        double dist_v2ta_v1tb = DistanceHelper.getDistance(v2ta, v1tb);
        double dist_v2pa_v2pb = DistanceHelper.getDistance(v2pa, v2pb);
        double dist_v2ta_v2tb = DistanceHelper.getDistance(v2ta, v2tb);
        double distanceRatio = 0;
        double pNorm = pattern.getMaximumVertexDistanceToCenter();
        double tNorm = template.getMaximumVertexDistanceToCenter();
        double distanceRatio1 = rate(dist_v1pa_v1pb, pNorm, dist_v1ta_v1tb, tNorm);
        double distanceRatio2 = rate(dist_v1pa_v2pb, pNorm, dist_v1ta_v2tb, tNorm);
        double distanceRatio3 = rate(dist_v2pa_v1pb, pNorm, dist_v2ta_v1tb, tNorm);
        double distanceRatio4 = rate(dist_v2pa_v2pb, pNorm, dist_v2ta_v2tb, tNorm);
        distanceRatio = Math.min(Math.min(distanceRatio1, distanceRatio2), Math.min(distanceRatio3, distanceRatio4));
        Point patternCenter = pattern.getCenter();
        Point templateCenter = template.getCenter();
        double angle_v1pa_v1pb = AngleHelper.getAngle(patternCenter, v1pa.toPoint(), v1pb.toPoint());
        double angle_v1ta_v1tb = AngleHelper.getAngle(templateCenter, v1ta.toPoint(), v1tb.toPoint());
        double angleDiff1 = AngleHelper.getDiffAngle(angle_v1pa_v1pb, angle_v1ta_v1tb);
        double angle_v2pa_v2pb = AngleHelper.getAngle(patternCenter, v2pa.toPoint(), v2pb.toPoint());
        double angle_v2ta_v2tb = AngleHelper.getAngle(templateCenter, v2ta.toPoint(), v2tb.toPoint());
        double angleDiff2 = AngleHelper.getDiffAngle(angle_v2pa_v2pb, angle_v2ta_v2tb);
        double angleRatio = (360d - angleDiff1 - angleDiff2) / 360d;
        Point pCenter = pattern.getCenter();
        double v1paDistance = DistanceHelper.getDistance(pCenter, v1pa.toPoint()) / averageDistanceInPattern;
        double v2paDistance = DistanceHelper.getDistance(pCenter, v2pa.toPoint()) / averageDistanceInPattern;
        double v1pbDistance = DistanceHelper.getDistance(pCenter, v1pb.toPoint()) / averageDistanceInPattern;
        double v2pbDistance = DistanceHelper.getDistance(pCenter, v2pb.toPoint()) / averageDistanceInPattern;
        Point tCenter = template.getCenter();
        double v1taDistance = DistanceHelper.getDistance(tCenter, v1ta.toPoint()) / averageDistanceInTemplate;
        double v2taDistance = DistanceHelper.getDistance(tCenter, v2ta.toPoint()) / averageDistanceInTemplate;
        double v1tbDistance = DistanceHelper.getDistance(tCenter, v1tb.toPoint()) / averageDistanceInTemplate;
        double v2tbDistance = DistanceHelper.getDistance(tCenter, v2tb.toPoint()) / averageDistanceInTemplate;
        double distanceToCenterRatio = 0;
        distanceToCenterRatio += rate(v1paDistance, 1, v1taDistance, 1);
        distanceToCenterRatio += rate(v2paDistance, 1, v2taDistance, 1);
        distanceToCenterRatio += rate(v1pbDistance, 1, v1tbDistance, 1);
        distanceToCenterRatio += rate(v2pbDistance, 1, v2tbDistance, 1);
        distanceToCenterRatio /= 4;
        double ratio = Math.min(distanceRatio, distanceToCenterRatio);
        ratio = Math.min(ratio, angleRatio);
        return ratio;
    }

    private static double ratio(double a, double b) {
        if (a == 0 && b == 0) {
            return 1;
        }
        return Math.min(a, b) / Math.max(a, b);
    }

    @Deprecated
    private static double ratioCatchingSingleNull(double a, double b, double min) {
        if (a == 0 ^ b == 0) {
            if (a == 0) {
                return 1 - (b / min);
            } else {
                return 1 - (a / min);
            }
        }
        double result = ratio(a, b);
        double aBouns = getBonus(a, min);
        double bBonus = getBonus(b, min);
        result += Math.max(aBouns, bBonus);
        result = Math.min(result, 1);
        return result;
    }

    private static boolean roughly180Degree(double angle) {
        if (165 < angle && angle < 195) {
            return true;
        }
        return false;
    }

    public static boolean similarEdges(Graph graph, int edgeID1, int edgeID2, boolean v1Tov1) {
        Edge edge1 = graph.virtualEdges()[edgeID1];
        Edge edge2 = graph.virtualEdges()[edgeID2];
        Vertex vertex1_1 = graph.vertices()[edge1.vertex1];
        Vertex vertex1_2 = graph.vertices()[edge1.vertex2];
        Vertex vertex2_1 = graph.vertices()[edge2.vertex1];
        Vertex vertex2_2 = graph.vertices()[edge2.vertex2];
        Rectangle rectangle = graph.getRectangle();
        int tolerance = Math.max(rectangle.height, rectangle.width) / 15;
        if (v1Tov1) {
            if (DistanceHelper.getDistance(vertex1_1, vertex2_1) > tolerance) {
                return false;
            }
            if (DistanceHelper.getDistance(vertex1_2, vertex2_2) > tolerance) {
                return false;
            }
        } else {
            if (DistanceHelper.getDistance(vertex1_1, vertex2_2) > tolerance) {
                return false;
            }
            if (DistanceHelper.getDistance(vertex1_2, vertex2_1) > tolerance) {
                return false;
            }
        }
        return true;
    }

    private static double getBonus(double value, double maximum) {
        if (value > maximum) {
            return 0;
        }
        double ratio = value / maximum;
        double result = 1 - Math.pow(ratio, (double) 1 / 10);
        return result;
    }

    /**
	 * Bewertet die �hnlichkeit von a und b (beide normiert):
	 * 
	 * @return 1-(a_norm - b_norm)
	 */
    private static double rate(double a, double aNorm, double b, double bNorm) {
        double _a = a / aNorm;
        double _b = b / bNorm;
        double normedDiff = Math.max(_a, _b) - Math.min(_a, _b);
        double exp = 1;
        return 1 - Math.pow(normedDiff, exp);
    }

    private static double rateAngle(double angle1, double angle2) {
        double diff = Math.max(angle1, angle2) - Math.min(angle1, angle2);
        if (360 - diff < diff) {
            diff = 360 - diff;
        }
        return 1 - (diff / 180);
    }
}
