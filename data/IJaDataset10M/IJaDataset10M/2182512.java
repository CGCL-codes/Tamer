package com.hszt.solver;

import com.hszt.structure.*;
import com.hszt.util.RunConfiguration;
import com.hszt.util.log.LogFactory;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Creates a test labyrinth and its solution to check if the program runs correct.
 *
 * @author danielroth
 * @author adrianchristen
 * @author matthiasschmid
 */
public class TestPathBuilding {

    /**
	 * Sets up the run configuration and generates a log entry.
	 * 
	 * @see PointFactory#clear()
	 * @see PathFactory#clear()
	 * @throws Exception
	 */
    private void setUp() throws Exception {
        RunConfiguration.getInstance(new String[] {});
        LogFactory.getPersistentLog(RunConfiguration.getInstance().getDisplayLogLevel(), RunConfiguration.getInstance().getLogLevel(), RunConfiguration.getInstance().getLogPath());
        PointFactory.clear();
        PathFactory.clear();
    }

    /**
     * Creates an arrayList with four simple paths and a solutions matrix with solution vektors in a vektor.
     * 
     * @throws Exception print out stack trace
     */
    @Test
    public void simplePath() throws Exception {
        setUp();
        try {
            Point start = PointFactory.createStartPoint(1);
            PointFactory.createFinishPoint(10);
            ArrayList<Path> pathList = new ArrayList<Path>();
            pathList.add(PathFactory.createPath(1, 2, 10));
            pathList.add(PathFactory.createPath(1, 3, 4));
            pathList.add(PathFactory.createPath(2, 10, 40));
            pathList.add(PathFactory.createPath(3, 10, 100));
            Vector<Vector<Point>> solutions = new Vector<Vector<Point>>();
            Vector<Point> solution = new Vector<Point>();
            solution.add(PointFactory.createPoint(1));
            solution.add(PointFactory.createPoint(2));
            solution.add(PointFactory.createPoint(10));
            solutions.add(solution);
            solution = new Vector<Point>();
            solution.add(PointFactory.createPoint(1));
            solution.add(PointFactory.createPoint(3));
            solution.add(PointFactory.createPoint(10));
            solutions.add(solution);
            assertTrue("FAILED to compare", compareSolution(buildPath(start, pathList), solutions));
            System.out.println("Test successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Comparing size of vektors and point by point of solutions1 and solutions2.
     * 
     * @param solutions matrix 1 and 2
     * @return true or false 
     */
    private boolean compareSolution(Vector<Vector<Point>> solutions1, Vector<Vector<Point>> solutions2) {
        Vector<Vector<Point>> copy1 = (Vector<Vector<Point>>) solutions1.clone();
        for (Iterator<Vector<Point>> copy1Iterator = copy1.iterator(); copy1Iterator.hasNext(); ) {
            Vector<Point> pointList1 = copy1Iterator.next();
            for (Vector<Point> pointList2 : solutions2) {
                if (pointList2.size() == pointList1.size()) {
                    boolean actualListEquals = true;
                    for (int j = 0; j < pointList1.size(); j++) {
                        if (pointList1.get(j) != pointList2.get(j)) {
                            actualListEquals = false;
                            break;
                        }
                    }
                    if (actualListEquals) copy1Iterator.remove();
                }
            }
        }
        Vector<Vector<Point>> copy2 = (Vector<Vector<Point>>) solutions2.clone();
        for (Iterator<Vector<Point>> copy2Iterator = copy2.iterator(); copy2Iterator.hasNext(); ) {
            Vector<Point> pointList2 = copy2Iterator.next();
            for (Vector<Point> pointList1 : solutions1) {
                if (pointList2.size() == pointList1.size()) {
                    boolean actualListEquals = true;
                    for (int j = 0; j < pointList2.size(); j++) {
                        if (pointList1.get(j) != pointList2.get(j)) {
                            actualListEquals = false;
                            break;
                        }
                    }
                    if (actualListEquals) copy2Iterator.remove();
                }
            }
        }
        return copy1.size() == 0 && copy2.size() == 0;
    }

    /**
     * Calculates all possible chains of a given list of paths beginning at a given point. 
     * 
     * @param start    starting point
     * @param pathlist list of paths
     * @return point list for each solution
     */
    private Vector<Vector<Point>> buildPath(Point start, ArrayList<Path> pathlist) {
        LabStarBuilder starBuilder = new LabStarBuilder();
        starBuilder.reset(pathlist);
        TreeBuilder builder = new TreeBuilder();
        builder.reset(starBuilder.buildStars());
        builder.buildTree(start);
        return builder.getPaths();
    }
}
