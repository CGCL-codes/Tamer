package jmud.engine.core;

import jmud.engine.stats.StatMap;

/**
 * <code>Targetable</code> is the abstract definition of something that can be
 * attacked. This has to be an interface so that Mobs can implement other things
 * as well, like Attacker (haven't made an Attacker interface yet, but probably
 * will, ... maybe) ... Thing is, if I have a separate attack command for Mobs
 * then I don't need an attacker interface.
 * @author David Loman
 */
public interface Targetable {

    /**
    * Get the name of the target.
    * @return the name of the target
    */
    String getName();

    /**
    * Get the statMap of the target.
    * @return the statMap of the target
    */
    StatMap getStatMap();
}
