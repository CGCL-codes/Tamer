package games.midhedava.client.entity;

import games.midhedava.client.midhedava;
import games.midhedava.client.GameScreen;
import games.midhedava.client.sprite.AnimatedSprite;
import games.midhedava.client.sprite.Sprite;
import games.midhedava.client.sprite.SpriteStore;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * The 2D view of an entity.
 */
public abstract class Entity2DView implements EntityView, EntityChangeListener {

    /**
	 * The entity this view is for
	 */
    protected Entity entity;

    /**
	 * The entity image (or current one at least).
	 */
    private Sprite sprite;

    /**
	 * The entity drawing composite.
	 */
    protected Composite entityComposite;

    /**
	 * Whether this view is contained.
	 */
    protected boolean contained;

    /**
	 * Model values affecting animation.
	 */
    protected boolean animatedChanged;

    /**
	 * Model values affecting visual representation changed.
	 */
    protected boolean representationChanged;

    /**
	 * The visibility value changed.
	 */
    protected boolean visibilityChanged;

    /**
	 * Create a 2D view of an entity.
	 *
	 * @param	entity		The entity to render.
	 */
    public Entity2DView(final Entity entity) {
        this.entity = entity;
        entityComposite = AlphaComposite.SrcOver;
        contained = false;
        animatedChanged = false;
        visibilityChanged = true;
        representationChanged = false;
        entity.addChangeListener(this);
    }

    /**
	 * Rebuild the representation using the base entity.
	 */
    protected void buildRepresentation() {
        setSprite(SpriteStore.get().getSprite(translate(entity.getType())));
    }

    /**
	 * Draw the entity.
	 *
	 * @param	screen		The screen to drawn on.
	 */
    public void draw(final GameScreen screen) {
        update();
        Rectangle r = screen.convertWorldToScreen(getDrawnArea());
        if (screen.isInScreen(r)) {
            draw(screen, screen.expose(), r.x, r.y, r.width, r.height);
        }
    }

    /**
	 * Draw the entity.
	 *
	 * @param	screen		The screen to drawn on.
	 * @param	g2d		The graphics context.
	 * @param	x		The drawn X coordinate.
	 * @param	y		The drawn Y coordinate.
	 * @param	width		The drawn entity width.
	 * @param	height		The drawn entity height.
	 */
    protected void draw(final GameScreen screen, Graphics2D g2d, int x, int y, int width, int height) {
        Composite oldComposite;
        oldComposite = g2d.getComposite();
        g2d.setComposite(entityComposite);
        drawEntity(screen, g2d, x, y, width, height);
        g2d.setComposite(oldComposite);
        if (midhedava.SHOW_COLLISION_DETECTION) {
            g2d.setColor(Color.blue);
            g2d.drawRect(x, y, width, height);
            g2d.setColor(Color.green);
            g2d.draw(screen.convertWorldToScreen(entity.getArea()));
        }
    }

    /**
	 * Draw the base entity part.
	 *
	 * @param	screen		The screen to drawn on.
	 * @param	g2d		The graphics context.
	 * @param	x		The drawn X coordinate.
	 * @param	y		The drawn Y coordinate.
	 * @param	width		The drawn entity width.
	 * @param	height		The drawn entity height.
	 */
    protected void drawEntity(final GameScreen screen, Graphics2D g2d, int x, int y, int width, int height) {
        getSprite().draw(g2d, x, y);
    }

    /**
	 * Draw the top layer parts of an entity. This will be on down after
	 * all other game layers are rendered.
	 *
	 * @param	screen		The screen to drawn on.
	 */
    public void drawTop(final GameScreen screen) {
        Rectangle r = screen.convertWorldToScreen(getDrawnArea());
        if (screen.isInScreen(r)) {
            drawTop(screen, screen.expose(), r.x, r.y, r.width, r.height);
        }
    }

    /**
	 * Draw the entity.
	 *
	 * @param	screen		The screen to drawn on.
	 * @param	g2d		The graphics context.
	 * @param	x		The drawn X coordinate.
	 * @param	y		The drawn Y coordinate.
	 * @param	width		The drawn entity width.
	 * @param	height		The drawn entity height.
	 */
    protected void drawTop(final GameScreen screen, Graphics2D g2d, int x, int y, int width, int height) {
    }

    /**
	 * Get the class resource sub-path. The is the base sprite image name,
	 * relative to <code>translate()</code>.
	 *
	 * @return	The resource path.
	 */
    protected String getClassResourcePath() {
        String rpath = entity.getEntityClass();
        if (rpath != null) {
            String subclass = entity.getEntitySubClass();
            if (subclass != null) {
                rpath += "/" + subclass;
            }
        }
        return rpath;
    }

    /**
	 * Get the drawing composite.
	 *
	 * @return	The drawing composite.
	 */
    protected AlphaComposite getComposite() {
        int visibility = getVisibility();
        if (visibility == 100) {
            return AlphaComposite.SrcOver;
        } else if (visibility == 0) {
            return AlphaComposite.Dst;
        } else {
            return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, visibility / 100.0f);
        }
    }

    /**
	 * Get the 2D area that is drawn in.
	 *
	 * @return	The 2D area this draws in.
	 */
    public abstract Rectangle2D getDrawnArea();

    /**
	 * Get the sprite image for this entity.
	 *
	 * @return	The image representation.
	 */
    public Sprite getSprite() {
        return sprite;
    }

    /**
	 * Get the entity's visibility.
	 *
	 * @return	The visibility value (0-100).
	 */
    protected int getVisibility() {
        return entity.getVisibility();
    }

    /**
	 * Get the entity's X coordinate.
	 *
	 * @return	The X coordinate.
	 */
    protected double getX() {
        return entity.getX();
    }

    /**
	 * Get the entity's Y coordinate.
	 *
	 * @return	The Y coordinate.
	 */
    protected double getY() {
        return entity.getY();
    }

    /**
	 * Determines on top of which other entities this entity should be
	 * drawn. Entities with a high Z index will be drawn on top of ones
	 * with a lower Z index.
	 * 
	 * Also, players can only interact with the topmost entity.
	 * 
	 * @return	The drawing index.
	 */
    public int getZIndex() {
        return 10000;
    }

    /**
	 * Determine if this view is currently animatable.
	 *
	 * @return	<code>true</code> if animating enabled.
	 */
    protected boolean isAnimating() {
        return true;
    }

    /**
	 * Determine if this view is contained, and should render in a
	 * compressed (it's defined) area without clipping anything important.
	 *
	 * @return	<code>true</code> if contained.
	 */
    public boolean isContained() {
        return contained;
    }

    /**
	 * Set whether this view is contained, and should render in a
	 * compressed (it's defined) area without clipping anything important.
	 *
	 * @param	contained	<code>true</code> if contained.
	 */
    public void setContained(boolean contained) {
        this.contained = contained;
    }

    /**
	 * Set the sprite's animation state (if applicable).
	 *
	 * @param	sprite		The sprite.
	 */
    protected void setAnimation(final Sprite sprite) {
        if (sprite instanceof AnimatedSprite) {
            AnimatedSprite asprite = (AnimatedSprite) sprite;
            if (isAnimating()) {
                asprite.start();
            } else {
                asprite.stop();
                asprite.reset();
            }
        }
    }

    /**
	 * Set the sprite.
	 *
	 * @param	sprite		The sprite.
	 */
    protected void setSprite(final Sprite sprite) {
        setAnimation(sprite);
        animatedChanged = false;
        this.sprite = sprite;
    }

    /**
	 * Translate a resource name into it's sprite image path.
	 *
	 * @param	name		The resource name.
	 *
	 * @return	The full resource name.
	 */
    protected String translate(final String name) {
        return "data/sprites/" + name + ".png";
    }

    /**
	 * Handle updates.
	 */
    protected void update() {
        if (representationChanged) {
            buildRepresentation();
            representationChanged = false;
        }
        if (visibilityChanged) {
            entityComposite = getComposite();
            visibilityChanged = false;
        }
        if (animatedChanged) {
            setAnimation(getSprite());
            animatedChanged = false;
        }
    }

    /**
	 * An entity was changed.
	 *
	 * @param	entity		The entity that was changed.
	 * @param	property	The property identifier.
	 */
    public void entityChanged(final Entity entity, final Object property) {
        if (property == Entity.PROP_ANIMATED) {
            animatedChanged = true;
        } else if (property == Entity.PROP_TYPE) {
            representationChanged = true;
        } else if (property == Entity.PROP_VISIBILITY) {
            visibilityChanged = true;
        }
    }

    /**
	 * Get the view's entity.
	 *
	 * @return	The view's entity.
	 */
    public Entity getEntity() {
        return entity;
    }

    /**
	 * Determine if this entity can be moved (e.g. via dragging).
	 *
	 * @return	<code>true</code> if the entity is movable.
	 */
    public boolean isMovable() {
        return false;
    }

    /**
	 * Release any view resources. This view should not be used after
	 * this is called.
	 */
    public void release() {
        entity.removeChangeListener(this);
        entity = null;
    }
}
