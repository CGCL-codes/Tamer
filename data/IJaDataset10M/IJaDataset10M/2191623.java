package nl.huub.van.amelsvoort.server;

import nl.huub.van.amelsvoort.spel.Koppeling;

public class areanode_t {

    int axis;

    float dist;

    areanode_t children[] = new areanode_t[2];

    Koppeling trigger_edicts = new Koppeling(this);

    Koppeling solid_edicts = new Koppeling(this);
}
