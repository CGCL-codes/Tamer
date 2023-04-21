package org.omg.tacsit.worldwind.common.layers;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.UserFacingIcon;
import gov.nasa.worldwind.render.WWIcon;
import java.awt.Color;
import java.awt.Point;

/**
 * A layer that displays positioned items as an icon.
 * @author Matthew Child
 * @param <P> The type of positioned item
 * @param <SEL_TYPE> The type of selection
 */
public class ItemIconLayer<P extends Positioned, SEL_TYPE> extends AbstractItemLayer<P, SEL_TYPE, WWIcon> {

    private SelectableIconLayer<SEL_TYPE> iconLayer;

    private ImagePack<P> imagePack;

    /**
   * Creates a new instance.
   */
    public ItemIconLayer() {
        this.imagePack = new KeyedImagePack();
        this.iconLayer = createIconLayer();
        this.iconLayer.setSelectionModel(new IconSelectionHelper());
    }

    private SelectableIconLayer createIconLayer() {
        SelectableIconLayer layer = new SelectableIconLayer();
        layer.setRegionCulling(false);
        return layer;
    }

    /**
   * Sets the selection color for a particular selection type.
   * @param selectionType The selection type to change the color of.
   * @param selectionColor The color to use for the selection type.
   */
    public void setSelectionColor(SEL_TYPE selectionType, Color selectionColor) {
        iconLayer.setSelectionColor(selectionType, selectionColor);
    }

    /**
   * Gets the selection color for a particular selection type.
   * @param selectionType The selection type to get the color of.
   * @return The color that's used for that selection type.
   */
    public Color getSelectionColor(SEL_TYPE selectionType) {
        return iconLayer.getSelectionColor(selectionType);
    }

    /**
   * Gets the image pack used to get images to display for items in the layer.
   * @return The image pack that picks images for items.
   */
    public ImagePack<P> getImagePack() {
        return imagePack;
    }

    /**
   * Sets the image pack used to get images to display for item in the layer.
   * @param imagePack The image pack that picks images for items. 
   */
    public void setImagePack(ImagePack<P> imagePack) {
        this.imagePack = imagePack;
        updateAllRepresentations();
    }

    @Override
    protected WWIcon newRepresentation(P item) {
        Object imageSource = getImageSource(item);
        Position p = Position.ZERO;
        if (item != null) {
            p = item.getPosition();
        }
        UserFacingIcon representation = new UserFacingIcon(imageSource, p);
        return representation;
    }

    @Override
    protected void addToLayer(WWIcon representation) {
        this.iconLayer.addIcon(representation);
    }

    @Override
    protected void removeFromLayer(WWIcon representation) {
        iconLayer.removeIcon(representation);
    }

    @Override
    protected void updateDisplayAttributes(P item, WWIcon representation) {
        Position itemPosition = item.getPosition();
        representation.setPosition(itemPosition);
        Object imageSource = getImageSource(item);
        representation.setImageSource(imageSource);
    }

    @Override
    protected void clearRepresentationsFromLayer() {
        iconLayer.removeAllIcons();
    }

    private Object getImageSource(P item) {
        Object imageSource = null;
        if (this.imagePack != null) {
            imageSource = this.imagePack.getImageSource(item);
        }
        return imageSource;
    }

    @Override
    public void pick(DrawContext dc, Point point) {
        iconLayer.pick(dc, point);
    }

    @Override
    public void preRender(DrawContext dc) {
        iconLayer.preRender(dc);
    }

    /**
   * Performs the rendering of the icons.
   * @param dc The draw context to render the icons on.
   */
    @Override
    protected void doRender(DrawContext dc) {
        iconLayer.render(dc);
    }

    private class IconSelectionHelper implements IconSelectionModel {

        public Object getSelectionType(WWIcon icon) {
            P item = getItem(icon);
            Object selectionType = ItemIconLayer.this.getDisplaySelectionType(item);
            return selectionType;
        }
    }
}
