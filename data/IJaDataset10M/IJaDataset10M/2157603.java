package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;

/** special entity for laying out a path for patroling guards.*/
public class GuardFlag extends Entity {

    public GuardFlag(Vector3f pos, float fD, float bsr) {
        super(pos, fD, bsr);
    }
}
