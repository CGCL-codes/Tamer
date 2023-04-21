package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * @author Erik Wagner
 * 
 */
public class Cannon extends Shooter {

    private static final long serialVersionUID = -1165999636075257562L;

    private Vector startPosition;

    private final Vector shotDirection;

    /**
	 * @param world
	 * @param shape
	 * @param shotInterval
	 * @param activated
	 */
    public Cannon(World world, Vector position, boolean activated, Vector shotDirection) {
        super(world, ShapeFactory.createRectangle(position, 50, 50), Parameter.OBJECTS_CANNON_SHOTINTERVAL, activated);
        this.shotDirection = shotDirection.getDirection();
        this.startPosition = this.getPosition().add(new Vector(Math.abs(12.5f) * Math.signum(this.shotDirection.x), Math.abs(25.0f) * Math.signum(this.shotDirection.y)));
    }

    public Cannon(World world, Vector position, String arguments) {
        this(world, position, Boolean.parseBoolean(arguments.split(",")[0]), Vector.parseVector(arguments.split(",")[1]));
    }

    public void draw(Graphics g) {
        if (this.shotDirection.x >= 0) {
            GraphicUtils.drawImage(g, this.getShape(), ResourceManager.getInstance().getImage("object-pictures/cannon.png").getFlippedCopy(true, false));
        } else {
            GraphicUtils.drawImage(g, this.getShape(), ResourceManager.getInstance().getImage("object-pictures/cannon.png"));
        }
    }

    @Override
    protected void shot() {
        this.getWorld().add(new Cannonball(this.getWorld(), this.startPosition, this.shotDirection));
    }
}
