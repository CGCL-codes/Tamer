package org.jcrpg.world.ai.flora.middle.deciduous;

import org.jcrpg.space.Cube;
import org.jcrpg.space.Side;
import org.jcrpg.space.sidetype.SideSubType;
import org.jcrpg.world.ai.flora.FloraDescription;
import org.jcrpg.world.ai.flora.middle.MiddlePlant;

public class GreenBush extends MiddlePlant {

    public static final String TYPE_BUSH = "GREEN";

    public static final SideSubType SUBTYPE_BUSH = new SideSubType(TYPE_BUSH + "_BUSH");

    static Side[][] BUSH = new Side[][] { null, null, null, null, null, { new Side(TYPE_BUSH, SUBTYPE_BUSH) } };

    public GreenBush() {
        super();
        defaultDescription = new FloraDescription(new Cube(null, BUSH, 0, 0, 0), 0, false, false);
    }
}
