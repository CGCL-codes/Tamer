package com.redtwitch.craps.agents;

import com.redtwitch.craps.view.*;
import java.util.*;

/**
 * A simple Agent that will simply play the pass line, and back
 * up the passline bet with maximum odds.
 *
 * @author  <a href="mailto:glenn@redtwitch.com">Glenn Wilson</a>
 * $Id: ConservativeAgent.java,v 1.2 2005/06/29 04:44:48 wilsong123 Exp $
 */
public class ConservativeAgent extends Agent {

    public ConservativeAgent() {
        super();
    }

    public ConservativeAgent(int init, int units) {
        super(init, units);
    }

    protected boolean placeBets(Table table, boolean comeOut, int point) {
        if (comeOut) {
            if (!makePassLineBet(table, point)) return false;
        } else {
            if (!backUpPassLineBet(table, point)) return false;
        }
        return true;
    }
}
