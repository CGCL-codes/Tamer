package games.midhedava.client.gui.wt;

import games.midhedava.client.GameScreen;
import games.midhedava.client.MidhedavaClient;
import games.midhedava.client.entity.Entity;
import games.midhedava.client.entity.EntityFactory;
import games.midhedava.client.entity.Player;
import games.midhedava.client.entity.User;
import games.midhedava.client.gui.wt.core.WtDraggable;
import games.midhedava.client.gui.wt.core.WtDropTarget;
import games.midhedava.client.gui.wt.core.WtPanel;
import games.midhedava.client.sprite.Sprite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPObject;

/**
 * This is a container which contains exactly one Entity. The name do not have
 * to be unique. Items can be dropped to this container when it is empty. Note
 * that the onDrop() method simply informs the server that the item was dropped.
 * Whatever the server decides will be the next content of this EntitySlot
 * 
 * @author mtotz
 */
public class EntitySlot extends WtPanel implements WtDropTarget {

    /** the (background) sprite for this slot */
    private Sprite graphic;

    /** the content of the slot */
    private RPObject content;

    /** the parent of the slot */
    private Entity parent;

    /** need this to find the sprite for each RPObject */
    private MidhedavaClient client;

    /** sprite for showing the quartity */
    private Sprite quantityImage;

    /** cached old quantity */
    private int oldQuantity;

    /** cached sprite for the entity */
    private Sprite sprite;

    /** background sprite */
    private Sprite background;

    /** Creates a new instance of RPObjectSlot */
    public EntitySlot(MidhedavaClient client, String name, Sprite graphic, Sprite background, int x, int y) {
        super(name, x, y, graphic.getWidth(), graphic.getHeight());
        this.graphic = graphic;
        this.client = client;
        this.background = background;
        this.sprite = background;
    }

    /** */
    public void setParent(Entity parent) {
        this.parent = parent;
    }

    /** called when an object is dropped. */
    public boolean onDrop(WtDraggable droppedObject) {
        if ((droppedObject instanceof MoveableEntityContainer) && (parent != null)) {
            MoveableEntityContainer container = (MoveableEntityContainer) droppedObject;
            if (container != null && content != null) if (container.getContent() == content.getID().getObjectID()) return false;
            RPAction action = new RPAction();
            action.put("type", "equip");
            container.fillRPAction(action);
            action.put("targetobject", parent.getID().getObjectID());
            action.put("targetslot", getName());
            client.send(action);
        }
        return false;
    }

    /** clears the content of this slot */
    public void clear() {
        content = null;
        sprite = background;
    }

    /** adds an object to this slot, this replaces any previous content */
    public void add(RPObject object) {
        content = object;
        Entity entity = EntityFactory.createEntity(object);
        if (entity != null) {
            sprite = entity.getView().getSprite();
            entity.release();
        } else {
            sprite = null;
        }
    }

    /**
	 * ensures that the quantity image is set
	 */
    private void checkQuantityImage(int quantity) {
        if ((quantityImage == null) || (quantity != oldQuantity)) {
            oldQuantity = quantity;
            String quantityString;
            if (quantity > 99999) {
                quantityString = (quantity / 1000) + "K";
            } else {
                quantityString = Integer.toString(quantity);
            }
            quantityImage = GameScreen.get().createString(quantityString, Color.white);
        }
    }

    /**
	 * draws the panel into the graphics object
	 * 
	 * @param g
	 *            graphics where to render to
	 * @return a graphics object for deriving classes to use. It is already
	 *         clipped to the correct client region
	 */
    @Override
    public Graphics draw(Graphics g) {
        if (isClosed()) {
            return g;
        }
        Graphics childArea = super.draw(g);
        graphic.draw(childArea, 0, 0);
        if (sprite != null) {
            int x = (getWidth() - sprite.getWidth()) / 2;
            int y = (getHeight() - sprite.getHeight()) / 2;
            sprite.draw(childArea, x, y);
            if (content != null) {
                if (content.has("quantity")) {
                    int quantity = content.getInt("quantity");
                    checkQuantityImage(quantity);
                    quantityImage.draw(childArea, 0, 0);
                }
            }
        }
        return childArea;
    }

    /**
	 * returns a draggable object
	 */
    @Override
    protected WtDraggable getDragged(int x, int y) {
        if (content != null) {
            return new MoveableEntityContainer(content, parent, getName(), sprite);
        }
        return null;
    }

    /** right mouse button was clicked */
    @Override
    public synchronized boolean onMouseRightClick(Point p) {
        if (content != null) {
            Entity entity = EntityFactory.createEntity(content);
            CommandList list = new CommandList(getName(), entity.offeredActions(), entity);
            list.setContext(parent.getID().getObjectID(), getName());
            setContextMenu(list);
        }
        return true;
    }

    /** doubleclick moves this item to the players inventory */
    @Override
    public synchronized boolean onMouseDoubleClick(Point p) {
        if (super.onMouseDoubleClick(p)) {
            return true;
        }
        if (content == null) {
            return (false);
        }
        if (parent instanceof Player) {
            Entity entity = EntityFactory.createEntity(content);
            if (entity != null) {
                entity.onAction(entity.defaultAction(), Integer.toString(parent.getID().getObjectID()), getName());
                return (true);
            }
            return (false);
        }
        RPAction action = new RPAction();
        action.put("type", "equip");
        action.put("baseobject", parent.getID().getObjectID());
        action.put("baseslot", getName());
        action.put("baseitem", content.getID().getObjectID());
        action.put("targetobject", User.get().getID().getObjectID());
        action.put("targetslot", "bag");
        client.send(action);
        return true;
    }
}
