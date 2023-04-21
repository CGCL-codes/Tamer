package com.cube42.hworld.world;

import javax.vecmath.Point3d;
import com.cube42.echoverse.model.WorldSeed;
import com.cube42.util.math.Orientation;
import com.cube42.util.math.Position;

/**
 * Represends the class required to construct HelloWorld
 *
 * @author  Matt Paulin
 * @version $Id: HelloWorld.java,v 1.2 2003/03/12 01:48:32 zer0wing Exp $
 */
public class HelloWorld extends WorldSeed {

    /**
     * Tells the world seed to intialize the world.
     */
    public void initWorld() {
        this.createEnaction("com.cube42.hworld.world.Avitar", Position.ORGIN);
        Position pos = new Position(new Point3d(100, 100, 0), new Orientation());
        this.createEnaction("com.cube42.hworld.world.Avitar", pos);
        Position pos2 = new Position(new Point3d(-200, 200, 0), new Orientation());
        this.createEnaction("com.cube42.hworld.world.SpudVent", pos2);
        Position pos3 = new Position(new Point3d(-200, 350, 0), new Orientation());
        this.createEnaction("com.cube42.hworld.world.FryDaddy", pos3);
        Position pos4 = new Position(new Point3d(-200, 50, 0), new Orientation());
        this.createEnaction("com.cube42.hworld.world.FryDaddy", pos4);
        Position pos5 = new Position(new Point3d(-400, -200, 0), new Orientation());
        this.createEnaction("com.cube42.hworld.world.Fob", pos5);
    }
}
