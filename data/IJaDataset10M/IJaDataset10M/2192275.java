package poa.game.world.objects;

import java.io.File;
import org.newdawn.slick.Graphics;
import poa.game.Camera;
import poa.game.entity.Entity;
import poa.util.Location;
import poa.util.ObjectFromString;
import poa.util.Image;

/**
 *  PagesOfAdventure: A top-down puzzle adventure.
 *  Copyright (C) 2010, 2011  PagesOfAdventure
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 * @author Edward_Lii
 */
public class Object extends Entity {

    /**
	 * The name of the object.
	 */
    private String name;

    /**
	 * The image of the object.
	 */
    private Image image;

    /**
	 * The behaviour of this object.
	 */
    private Behaviour behaviour;

    /**
	 * Class constructor.
	 * @param name The name of the object.
	 */
    public Object(String name) {
        this.name = name;
    }

    @Override
    public void drawEntity(Graphics g, Camera camera) {
        if (camera.isVisible(location.getX(), location.getY(), width, height)) {
            Location screenLocation = camera.toScreenLocation(location);
            g.drawImage(image.getInternalImage(), screenLocation.getX() - slideX * 32, screenLocation.getY() - slideY * 32);
        }
    }

    /**
	 * Sets the size of the object.
	 * @param width The new width of the object.
	 * @param height The new height of the object.
	 */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(int delta) {
        behaviour.update(delta);
        super.update(delta);
        if (slideX == 0 && slideY == 0) {
            moving = false;
        }
    }

    /**
	 * Stops the object after a collision.
	 */
    public void collide() {
        moving = false;
        slideX = 0;
        slideY = 0;
        switch(heading) {
            case UP:
                location.setY(location.getY() + 1);
                break;
            case RIGHT:
                location.setX(location.getX() - 1);
                break;
            case DOWN:
                location.setY(location.getY() - 1);
                break;
            case LEFT:
                location.setX(location.getX() + 1);
                break;
        }
    }

    /**
	 * Sets the behaviour of the object.
	 * @param behaviour The name of the behaviour.
	 */
    public void setBehaviour(String behaviour) {
        this.behaviour = (Behaviour) ObjectFromString.getObject("poa.game.world.objects." + behaviour, new Class[] { this.getClass() }, new Object[] { this });
    }

    /**
	 * @return Returns the behaviour of the object.
	 */
    public Behaviour getBehaviour() {
        return behaviour;
    }

    @Override
    public void load() {
        image = new Image("Data" + File.separator + "Objects" + File.separator + name + ".png");
        loaded = true;
    }

    @Override
    public void prepare() {
        image.prepare();
        prepared = true;
    }

    @Override
    public boolean loaded() {
        return loaded;
    }

    @Override
    public boolean prepared() {
        return prepared;
    }
}
