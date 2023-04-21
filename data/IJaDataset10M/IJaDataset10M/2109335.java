package com.riseOfPeople.physics.particleEngine;

import java.io.Serializable;

/**
 * All basic needs of a particle
 * @author Erwin
 */
public class Particle implements Serializable {

    /**
	 * Declared variables
	 */
    private static final long serialVersionUID = 1L;

    public float life;

    public float fade;

    public float colorRed;

    public float colorGreen;

    public float colorBlue;

    public float posX;

    public float posY;

    public float posZ;

    public float dirX;

    public float dirY;

    public float dirZ;

    public float gravX;

    public float gravY;

    public float gravZ;
}
