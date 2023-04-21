package org.eesgmbh.gimv.client.event;

/**
 * Indicates that image position and dimensions should be changed
 * by some offset.
 *
 * @author Christian Seewald - EES GmbH - c.seewald@ees-gmbh.de
 */
public class ChangeImagePixelBoundsEvent extends FilteredDispatchGwtEvent<ChangeImagePixelBoundsEventHandler> {

    public static Type<ChangeImagePixelBoundsEventHandler> TYPE = new Type<ChangeImagePixelBoundsEventHandler>();

    private final double offsetX, offsetY;

    private final double offsetWidth;

    private final double offsetHeight;

    public ChangeImagePixelBoundsEvent(double offsetX, double offsetY, ChangeImagePixelBoundsEventHandler... blockedHandlers) {
        this(offsetX, offsetY, 0, 0, blockedHandlers);
    }

    public ChangeImagePixelBoundsEvent(double offsetX, double offsetY, double offsetWidth, double offsetHeight, ChangeImagePixelBoundsEventHandler... blockedHandlers) {
        super(blockedHandlers);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetWidth = offsetWidth;
        this.offsetHeight = offsetHeight;
    }

    public double getOffsetX() {
        return this.offsetX;
    }

    public double getOffsetY() {
        return this.offsetY;
    }

    public double getOffsetWidth() {
        return this.offsetWidth;
    }

    public double getOffsetHeight() {
        return this.offsetHeight;
    }

    @Override
    public Type<ChangeImagePixelBoundsEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void onDispatch(ChangeImagePixelBoundsEventHandler handler) {
        handler.onSetImageBounds(this);
    }
}
