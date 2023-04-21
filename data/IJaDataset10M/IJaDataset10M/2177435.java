package navigators.smart.tom.util;

import navigators.smart.tom.core.messages.TOMMessage;

/**
 *
 * @author Joao Sousa
 */
public class DebugInfo {

    public final int eid;

    public final int round;

    public final int leader;

    public final TOMMessage msg;

    public DebugInfo(int eid, int round, int leader, TOMMessage msg) {
        this.eid = eid;
        this.round = round;
        this.leader = leader;
        this.msg = msg;
    }
}
