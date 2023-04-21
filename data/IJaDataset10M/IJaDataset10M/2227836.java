package fate.gameUnit.components;

import fate.server.gameClock.*;

/** This is the parent class for each of the divisible parts of a ship
 * or other unit.
 */
public class Component {

    public GameTime created;

    public float damage;

    public float mass;

    public float powerUse;

    public float volume;

    public float radiantEnergy;

    public Armor shielding;
}
