package es.eucm.eadventure.engine.core.control.functionaldata;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.CustomAction;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.InfluenceArea;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.resources.Asset;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.gui.GUI;
import es.eucm.eadventure.engine.multimedia.MultimediaManager;
import es.eucm.eadventure.engine.resourcehandler.ResourceHandler;

/**
 * An atrezzo object in the game
 */
public class FunctionalAtrezzo extends FunctionalElement {

    /**
     * Resources being used in the atrezzo item
     */
    protected Resources resources;

    /**
     * Image of the atrezo item, to display in the scene
     */
    protected Image image;

    private Image oldImage = null;

    private int x1, y1, x2, y2;

    private int width, height;

    private float oldScale = -1;

    private Image oldOriginalImage = null;

    /**
     * Atrezzo item containing the data
     */
    protected Atrezzo atrezzo;

    private ElementReference reference;

    public FunctionalAtrezzo(Atrezzo atrezzo, ElementReference reference) {
        this(atrezzo, reference.getX(), reference.getY());
        this.reference = reference;
        this.layer = reference.getLayer();
        this.scale = reference.getScale();
    }

    /**
     * Creates a new FunctionalItem
     * 
     * @param atrezzo
     *            the atrezzo's data
     * @param x
     *            the atrezzo's horizontal position
     * @param y
     *            the atrezzo's vertical position
     */
    public FunctionalAtrezzo(Atrezzo atrezzo, int x, int y) {
        super(x, y);
        this.atrezzo = atrezzo;
        this.scale = 1;
        Image tempimage = null;
        resources = createResourcesBlock();
        MultimediaManager multimediaManager = MultimediaManager.getInstance();
        if (resources.existAsset(Item.RESOURCE_TYPE_IMAGE)) {
            tempimage = multimediaManager.loadImageFromZip(resources.getAssetPath(Item.RESOURCE_TYPE_IMAGE), MultimediaManager.IMAGE_SCENE);
            removeTransparentParts(tempimage);
        }
    }

    private void removeTransparentParts(Image tempimage) {
        x1 = tempimage.getWidth(null);
        y1 = tempimage.getHeight(null);
        x2 = 0;
        y2 = 0;
        width = x1;
        height = y1;
        for (int i = 0; i < tempimage.getWidth(null); i++) {
            boolean x_clear = true;
            for (int j = 0; j < tempimage.getHeight(null); j++) {
                boolean y_clear = true;
                BufferedImage bufferedImage = (BufferedImage) tempimage;
                int alpha = bufferedImage.getRGB(i, j) >>> 24;
                if (alpha > 0) {
                    if (x_clear) x1 = Math.min(x1, i);
                    if (y_clear) y1 = Math.min(y1, j);
                    x_clear = false;
                    y_clear = false;
                    x2 = Math.max(x2, i);
                    y2 = Math.max(y2, j);
                }
            }
        }
        image = GUI.getInstance().getGraphicsConfiguration().createCompatibleImage(x2 - x1, y2 - y1, Transparency.TRANSLUCENT);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.drawImage(tempimage, 0, 0, x2 - x1, y2 - y1, x1, y1, x2, y2, null);
        g.dispose();
    }

    /**
     * Updates the resources of the icon (if the current resources and the new
     * one are different)
     */
    public void updateResources() {
        Resources newResources = createResourcesBlock();
        if (resources != newResources) {
            resources = newResources;
            MultimediaManager multimediaManager = MultimediaManager.getInstance();
            Image tempimage = null;
            if (resources.existAsset(Item.RESOURCE_TYPE_IMAGE)) {
                tempimage = multimediaManager.loadImageFromZip(resources.getAssetPath(Item.RESOURCE_TYPE_IMAGE), MultimediaManager.IMAGE_SCENE);
                removeTransparentParts(tempimage);
            }
        }
    }

    /**
     * Returns this atrezzo's data
     * 
     * @return this atrezzo's data
     */
    public Atrezzo getAtrezzo() {
        return atrezzo;
    }

    @Override
    public Element getElement() {
        return atrezzo;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void update(long elapsedTime) {
    }

    public void draw() {
        int x_image = Math.round((x + x1 * scale) - (getWidth() * scale / 2)) - Game.getInstance().getFunctionalScene().getOffsetX();
        int y_image = Math.round((y + y1 * scale) - getHeight() * scale);
        if (scale != 1) {
            Image temp;
            if (image == oldOriginalImage && scale == oldScale) {
                temp = oldImage;
            } else {
                temp = GUI.getInstance().getGraphicsConfiguration().createCompatibleImage(Math.round(image.getWidth(null) * scale), Math.round(image.getHeight(null) * scale), Transparency.TRANSLUCENT);
                ((Graphics2D) temp.getGraphics()).drawImage(image, AffineTransform.getScaleInstance(scale, scale), null);
                oldImage = temp;
                oldOriginalImage = image;
                oldScale = scale;
            }
            if (layer == -1) GUI.getInstance().addElementToDraw(temp, x_image, y_image, Math.round(y), Math.round(y), highlight, this); else GUI.getInstance().addElementToDraw(temp, x_image, y_image, layer, Math.round(y), highlight, this);
        } else if (layer == -1) GUI.getInstance().addElementToDraw(image, x_image, y_image, Math.round(y), Math.round(y), highlight, this); else GUI.getInstance().addElementToDraw(image, x_image, y_image, layer, Math.round(y), highlight, this);
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return false;
    }

    @Override
    public boolean isInInventory() {
        return false;
    }

    @Override
    public boolean examine() {
        return false;
    }

    @Override
    public boolean canBeUsedAlone() {
        return false;
    }

    /**
     * Creates the current resource block to be used
     */
    public Resources createResourcesBlock() {
        Resources newResources = null;
        for (int i = 0; i < atrezzo.getResources().size() && newResources == null; i++) if (new FunctionalConditions(atrezzo.getResources().get(i).getConditions()).allConditionsOk()) newResources = atrezzo.getResources().get(i);
        if (newResources == null) {
            newResources = new Resources();
            newResources.addAsset(new Asset(Item.RESOURCE_TYPE_IMAGE, ResourceHandler.DEFAULT_IMAGE));
        }
        return newResources;
    }

    @Override
    public boolean canPerform(int action) {
        return false;
    }

    @Override
    public Action getFirstValidAction(int actionType) {
        return null;
    }

    @Override
    public CustomAction getFirstValidCustomAction(String actionName) {
        return null;
    }

    @Override
    public CustomAction getFirstValidCustomInteraction(String actionName) {
        return null;
    }

    @Override
    public InfluenceArea getInfluenceArea() {
        return null;
    }

    public ElementReference getReference() {
        return reference;
    }

    /**
     * Returns the X coordinate of the left-top vertex of this item.
     * It takes into account scene offset, transparent parts, and scale.  
     * @return The Current Absolute X position of the let-top vertex of the item 
     */
    @Override
    public int getXImage() {
        return Math.round((x + x1 * scale) - (getWidth() * scale / 2));
    }

    /**
     * Returns the Y coordinate of the left-top vertex of this item.
     * It takes into account transparent parts, and scale.  
     * @return The Current Absolute Y position of the let-top vertex of the item 
     */
    @Override
    public int getYImage() {
        return Math.round((y + y1 * scale) - getHeight() * scale);
    }

    /**
     * Returns the current height of this item.
     * It takes into account transparent parts, and scale.  
     * @return The Current height of the item 
     */
    @Override
    public int getHImage() {
        return Math.round((y2 - y1) * scale);
    }

    /**
     * Returns the current width of this item.
     * It takes into account scene offset, transparent parts, and scale.  
     * @return The Current width of the item 
     */
    @Override
    public int getWImage() {
        return Math.round((x2 - x1) * scale);
    }
}
