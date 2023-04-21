package sceneMatch.nearest_neighbor_matching;

import sceneInfo.GoalInfo;
import sceneInfo.ObjectInfo;
import sceneInfo.Scene;
import sceneMatch.penalties.Penalty;

/**
 * @author Kevin Lam
 * Heavily Revised by: Christopher Kafka
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NearestNeighborCellBallGoalDistance extends NearestNeighborParent {

    int kValue = 1;

    char friendlyside = 'l';

    char enemyside = 'r';

    /**
	 * 
	 */
    public NearestNeighborCellBallGoalDistance(int k, char side, Penalty p) {
        super(k, side, p);
        if (side == 'r') {
            friendlyside = 'r';
            enemyside = 'l';
        }
    }

    public float distanceBetween(Scene one, Scene two) {
        int high = 999;
        int x1 = high, y1 = high, x2 = high, y2 = high;
        int gx1 = high, gy1 = high, gx2 = high, gy2 = high;
        float b1angle = 180, b2angle = 180, g1angle = 180, g2angle = 180;
        if (one.getBallObjects().size() > 0) {
            ObjectInfo ballOne = (ObjectInfo) one.getBallObjects().get(0);
            x1 = ballOne.getTableColumn();
            y1 = ballOne.getTableRow() * 800;
            b1angle = ballOne.getDirection();
        }
        if (two.getBallObjects().size() > 0) {
            ObjectInfo ballTwo = (ObjectInfo) two.getBallObjects().get(0);
            x2 = ballTwo.getTableColumn();
            y2 = ballTwo.getTableRow() * 800;
            b2angle = ballTwo.getDirection();
        }
        if (one.getGoalObjects().size() > 0) {
            GoalInfo goalOne = (GoalInfo) one.getGoalObjects().get(0);
            if (one.getGoalObjects().size() > 1) {
                if (((GoalInfo) goalOne).getSide() == friendlyside) {
                    goalOne = (GoalInfo) one.getGoalObjects().get(1);
                }
            }
            if (goalOne.getSide() == enemyside) {
                gx1 = goalOne.getTableColumn();
                gy1 = goalOne.getTableRow();
            }
            g1angle = goalOne.getDirection();
        }
        if (two.getGoalObjects().size() > 0) {
            GoalInfo goalTwo = (GoalInfo) two.getGoalObjects().get(0);
            if (two.getGoalObjects().size() > 1) {
                if (((GoalInfo) goalTwo).getSide() == friendlyside) {
                    goalTwo = (GoalInfo) one.getGoalObjects().get(1);
                }
            }
            if (goalTwo.getSide() == enemyside) {
                gx2 = goalTwo.getTableColumn();
                gy2 = goalTwo.getTableColumn();
            }
            g2angle = goalTwo.getDirection();
        }
        float distance = (float) m_penalty.calculatePenalty();
        if (x1 != high && y1 != high && x1 != high && y2 != high && gx1 != high && gy1 != high && gx1 != high && gy2 != high) {
            distance = cartesianDistance(x1, y1, x2, y2);
            distance = distance + cartesianDistance(gx1, gy1, gx2, gy2) / (float) 2.0;
            float balldiffangle = Math.abs(b1angle - b2angle) / (float) 10;
            float goaldiffangle = Math.abs(g1angle - g2angle) / (float) 10;
            distance = distance + balldiffangle + goaldiffangle;
        }
        return distance;
    }
}
