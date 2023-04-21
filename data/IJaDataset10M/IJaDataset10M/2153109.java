package poa.game.npc;

import java.io.File;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;
import poa.game.Camera;
import poa.game.conversation.Conversation;
import poa.game.conversation.Talkable;
import poa.game.entity.Entity;
import poa.game.npc.ai.AI;
import poa.util.Location;
import poa.util.Map;
import poa.util.ObjectFromString;
import poa.util.Image;
import poa.util.io.PoAParser;
import de.lessvoid.nifty.render.NiftyRenderEngine;

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
public class NPC extends Entity implements Talkable {

    /**
	 * Temporary walk images.
	 */
    private Image[] walkImages = new Image[4];

    /**
	 * Temporary stand image.
	 */
    private Image standImage = null;

    /**
	 * The NiftyRenderEngine used to create NiftyImages.
	 */
    private NiftyRenderEngine renderEngine = null;

    /**
	 * The name of the npc
	 */
    private String name;

    /**
	 * The ai of the NPC
	 */
    private AI ai;

    /**
	 * The walk animations of the npc.
	 * Every heading has it's own animation.
	 */
    private Animation[] walk;

    /**
	 * The SpriteSheet used when the npc isn't walking.
	 */
    private SpriteSheet stand;

    /**
	 * ArrayList containing all conversationBlocks of the npc.
	 * Every conversation in any state of the npc is made of these conversationBlocks.
	 */
    private Conversation conversation = null;

    /**
	 * Class constructor.
	 * 
	 * @param name The name of the npc also used for the location of the files to load.
	 * @param renderEngine The renderEngine used to generate a <b>NiftyImage</b>.
	 */
    public NPC(String name, NiftyRenderEngine renderEngine) {
        this.name = name;
        this.renderEngine = renderEngine;
    }

    @Override
    public void update(int delta) {
        ai.think(delta);
        if (moving) {
            walk[heading].update(delta);
            if (walk[heading].isStopped()) {
                moving = false;
            }
        }
        super.update(delta);
    }

    @Override
    public void drawEntity(Graphics g, Camera camera) {
        if (camera.isVisible(location.getX(), location.getY(), width, height)) {
            Location screenLocation = camera.toScreenLocation(location);
            if (moving) {
                g.drawImage(walk[heading].getCurrentFrame(), screenLocation.getX() - slideX * 32, screenLocation.getY() - slideY * 32);
            } else {
                g.drawImage(stand.getSprite(heading, 0), screenLocation.getX(), screenLocation.getY());
            }
        }
    }

    @Override
    public void move(int heading) {
        if (!moving) {
            this.heading = heading;
            switch(heading) {
                case UP:
                    location.setY(location.getY() - 1);
                    slideY -= 1;
                    break;
                case RIGHT:
                    location.setX(location.getX() + 1);
                    slideX += 1;
                    break;
                case DOWN:
                    location.setY(location.getY() + 1);
                    slideY += 1;
                    break;
                case LEFT:
                    location.setX(location.getX() - 1);
                    slideX -= 1;
                    break;
            }
            moving = true;
            walk[heading].restart();
        }
    }

    @Override
    public void collide() {
        if (moving) {
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
        moving = false;
        slideX = 0;
        slideY = 0;
        ai.rethink();
    }

    @Override
    public Conversation getConversation() {
        return conversation;
    }

    /**
	 * @return Returns the name of the npc.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return Returns the AI of the npc.
	 */
    public AI getAI() {
        return ai;
    }

    @Override
    public void prepareConversation(int heading) {
        ai.prepareConversation(heading);
    }

    @Override
    public void endConversation() {
        ai.endConversation();
    }

    @Override
    public void load() {
        PoAParser parser = new PoAParser("Data" + File.separator + "NPC" + File.separator + name + File.separator + name + ".npc");
        while (!parser.next()) {
            if (parser.pair()) {
                if (parser.getKey().startsWith("size")) {
                    String[] values = parser.getTrimmedValue().split(",");
                    widthUnit = Integer.valueOf(values[0]);
                    heightUnit = Integer.valueOf(values[1]);
                    width = widthUnit * 32;
                    height = heightUnit * 32;
                    continue;
                }
                if (parser.getKey().startsWith("ai")) {
                    String[] parameters = parser.getTrimmedValue().split(",");
                    String aiClass = parameters[0];
                    ai = (AI) ObjectFromString.getObject("poa.game.npc.ai." + aiClass);
                    ai.setNPC(this, parameters);
                    continue;
                }
                if (parser.getKey().startsWith("walkspeed")) {
                    moveSpeed = Float.valueOf(parser.getTrimmedValue());
                    continue;
                }
                if (parser.getKey().startsWith("location")) {
                    location = new Location(parser.getTrimmedValue());
                    location.setScene(null);
                    continue;
                }
            }
            if (parser.block()) {
                if (parser.getBlock().startsWith("collision")) {
                    collision = new Map(width, height);
                    int height = 0;
                    while (!parser.nextBlock()) {
                        String[] values = parser.getTrimmedLine().split(",");
                        for (int width = 0; width < values.length; width++) {
                            switch(Integer.valueOf(values[width])) {
                                case 1:
                                    collision.set(width, height, 1);
                                    break;
                            }
                        }
                        height++;
                    }
                    continue;
                }
                if (parser.getBlock().startsWith("conversation")) {
                    conversation = new Conversation();
                    conversation.load(parser, renderEngine);
                    continue;
                }
            }
        }
        walk = new Animation[4];
        for (int i = 0; i < 4; i++) {
            walkImages[i] = new Image("Data" + File.separator + "NPC" + File.separator + name + File.separator + "npc" + i + ".png");
        }
        standImage = new Image("Data" + File.separator + "NPC" + File.separator + name + File.separator + "stand.png");
        loaded = true;
    }

    @Override
    public void prepare() {
        for (int i = 0; i < 4; i++) {
            SpriteSheet sheet = new SpriteSheet(walkImages[i].getInternalImage(), width, height);
            walk[i] = new Animation(sheet, (int) (1 / (moveSpeed * 4)));
            walk[i].setLooping(false);
        }
        stand = new SpriteSheet(standImage.getInternalImage(), width, height);
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
