package ch.epfl.lsr.adhoc.simulator.mobility;

import java.io.Serializable;

/** Represents an segment of the node path.
 *  
 * @version $Revision: 1.5 $ $Date: 2004/06/05 17:14:16 $
 * @author Author: Boris Danev and Aurelien Frossard
 */
class Segment implements Serializable {

    public static final String codeRevision = "$Revision: 1.5 $ $Date: 2004/06/05 17:14:16 $ Author: Boris Danev and Aurelien Frossard";

    /** start point of the segment, x and y coordinate */
    private double[] m_startPoint;

    /** speed on that segment, x and y coordinate */
    private double[] m_speed;

    /** simulation time when the node is at the beginning of the segment */
    private double m_startTime;

    /** simulation time when the node is at the end of the segment */
    private double m_endTime;

    /**
     * @param p_timeAtStartPoint : simulation time when the node is at the beginning of the segment
     * @param p_timeAtEndPoint : simulation time when the node is at the end of the segment
     * @param p_startPoint_X : start point of the segment, x coordinate
     * @param p_startPoint_Y : start point of the segment, y coordinate
     * @param p_speed_X : speed on that segment, x coordinate
     * @param p_speed_Y : speed on that segment, y coordinate
     */
    Segment(double p_startTime, double p_endTime, double p_startPoint_X, double p_startPoint_Y, double p_speed_X, double p_speed_Y) {
        m_startPoint = new double[2];
        m_speed = new double[2];
        m_startPoint[0] = p_startPoint_X;
        m_startPoint[1] = p_startPoint_Y;
        m_speed[0] = p_speed_X;
        m_speed[1] = p_speed_Y;
        m_startTime = p_startTime;
        m_endTime = p_endTime;
    }

    /** true if this segment can tell the position of the node at p_simulationTime */
    boolean isCorrectSegment(double p_simulationTime) {
        return m_startTime <= p_simulationTime && p_simulationTime <= m_endTime;
    }

    /** The equation used is
    	* <p>
    * newX = startPoint_X + speed_X*(simulationTime - timeAtStartPoint) */
    double[] getPosition(double p_simulationTime) {
        double time = p_simulationTime - m_startTime;
        return new double[] { m_startPoint[0] + m_speed[0] * time, m_startPoint[1] + m_speed[1] * time };
    }

    /** Time when destination is reached */
    double endTime() {
        return m_endTime;
    }

    /** Returns the point reached at endTime */
    double[] endPoint() {
        return getPosition(m_endTime);
    }

    /** the length of the speed vector */
    private double speedValue() {
        return Math.sqrt(m_speed[0] * m_speed[0] + m_speed[1] * m_speed[1]);
    }

    public String toString() {
        double[] endPoint = endPoint();
        return TAG_STARTTIME + m_startTime + TAG_ENDTIME + m_endTime + TAG_STARTPOINT + "(" + m_startPoint[0] + "  " + m_startPoint[1] + ")" + TAG_ENDPOINT + "(" + endPoint[0] + "  " + endPoint[1] + ")" + TAG_SPEEDVECTOR + "(" + m_speed[0] + "  " + m_speed[1] + ")" + TAG_SPEEDVALUE + speedValue();
    }

    private static final String TAG_STARTTIME = "startTime: ";

    private static final String TAG_ENDTIME = "\t endTime: ";

    private static final String TAG_STARTPOINT = "\t startPoint: ";

    private static final String TAG_ENDPOINT = "\t endPoint: ";

    private static final String TAG_SPEEDVECTOR = "\t speedVector: ";

    private static final String TAG_SPEEDVALUE = "\t speedValue: ";
}
