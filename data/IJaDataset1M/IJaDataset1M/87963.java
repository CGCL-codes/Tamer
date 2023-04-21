package com.googlecode.jumpnevolve.game.physic;

import java.util.ArrayList;
import org.newdawn.slick.Input;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.Vector;

public abstract class PhysicalObject extends AbstractObject {

    private static ArrayList<PhysicalObject> objects = new ArrayList<PhysicalObject>();

    public PhysicalObject(World world, NextShape shape, float mass) {
        super(world, shape, mass);
        objects.add(this);
    }

    public PhysicalObject(World world, NextShape shape, float mass, Vector velocity) {
        super(world, shape, mass, velocity);
        objects.add(this);
    }

    public PhysicalObject(World world, NextShape shape) {
        super(world, shape);
        objects.add(this);
    }

    public static ArrayList<PhysicalObject> getPhysicalObjects() {
        return objects;
    }
}
