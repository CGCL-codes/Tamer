package net.sf.nebulacards.util.meta;

import net.sf.nebulacards.main.*;

/**
 * Tell the game to start immediately.  As with all meta operations, each 
 * game (ruleset) may respond differently to this operation.  The recommended
 * behaviour is to stop waiting for more players and begin the game.
 * @author James Ranson
 * @version 0.8
 */
public class MetaStart extends MetaOperation implements java.io.Serializable {

    public String toString() {
        return "Meta Operation: Start";
    }
}
