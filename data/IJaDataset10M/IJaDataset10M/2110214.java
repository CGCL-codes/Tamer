package org.icehockeymanager.ihm.game.match.simpleagentengine;

/**
 * The Class MatchMessage.
 */
public class MatchMessage implements Comparable<MatchMessage> {

    /** The Constant RECEIVE_BALL. */
    public static final int RECEIVE_BALL = 0;

    /** The Constant PASS_TO_ME. */
    public static final int PASS_TO_ME = 1;

    /** The Constant SUPPORT_ATTACKER. */
    public static final int SUPPORT_ATTACKER = 2;

    /** The Constant GO_HOME. */
    public static final int GO_HOME = 3;

    /** The Constant WAIT. */
    public static final int WAIT = 4;

    /** The sender. */
    public int sender;

    /** The receiver. */
    public int receiver;

    /** The message. */
    public int message;

    /** The dispatch time. */
    public double dispatchTime;

    /** The extra info. */
    public Object extraInfo;

    /**
   * Instantiates a new match message.
   * 
   * @param t the t
   * @param s the s
   * @param r the r
   * @param msg the msg
   * @param i the i
   */
    public MatchMessage(double t, int s, int r, int msg, Object i) {
        sender = s;
        receiver = r;
        message = msg;
        dispatchTime = t;
        extraInfo = i;
    }

    public int compareTo(MatchMessage m) {
        if (dispatchTime < m.dispatchTime) return -1;
        if (dispatchTime > m.dispatchTime) return 1;
        return 0;
    }
}
