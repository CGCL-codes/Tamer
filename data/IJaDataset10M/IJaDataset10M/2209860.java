package fr.inria.zvtm.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import fr.inria.zvtm.common.compositor.MetisseWindow;
import fr.inria.zvtm.common.gui.menu.PopMenu;
import fr.inria.zvtm.engine.Camera;
import fr.inria.zvtm.engine.PPicker;
import fr.inria.zvtm.engine.Picker;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.event.ViewListener;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.glyphs.SICircle;
import fr.inria.zvtm.glyphs.SIRectangle;

/**
 * A zvtm cursor, piloted by a {@link CursorHandler} object.
 * This cursor can quit the screen by the top, if it is from a client. In that case, a phantom cursor with inverted y-axis appears (to facilitate the main cursor retrieve process).
 * @author Julien Altieri
 */
public class PCursor {

    /**
	 * The current bounds of the server's visible region.
	 */
    public static double[] wallBounds = { -512, 300, 512, -300 };

    protected double[] bounds;

    private SIRectangle cursorX;

    private SIRectangle cursorY;

    private SIRectangle phcursorX;

    private SIRectangle phcursorY;

    private SICircle circle;

    private double vx;

    private double vy;

    private PPicker picker;

    private PPicker picker2;

    private Camera mCamera;

    private Camera mCamera2;

    private ViewListener viewListener;

    private VirtualSpace ownerSpace;

    private VirtualSpace pickingSpace;

    private boolean phantomMode = false;

    private double wallX = 0;

    private double wallY = 0;

    private boolean mirrorMode = true;

    private CursorHandler parent;

    /**
	 * 
	 * @param cursorSpace The {@link VirtualSpace} in which the cursor will be
	 * @param pickingSpace The {@link VirtualSpace} in which the cursor will pick
	 * @param menuSpace The {@link VirtualSpace} containing the {@link PopMenu}
	 * @param locCam The camera observing pickingSpace
	 * @param menuCam The camera observing the menuSpace
	 * @param eh The {@link ViewListener} for handle events
	 * @param thickness Thickness of the lines composing the cross (shape of the cursor)
	 * @param size Length of the lines composing the cross (shape of the cursor)
	 */
    public PCursor(VirtualSpace cursorSpace, VirtualSpace pickingSpace, VirtualSpace menuSpace, Camera locCam, Camera menuCam, ViewListener eh, double thickness, double size) {
        Color c = getColor();
        cursorX = new SIRectangle(0, 0, 2, size, thickness, c);
        cursorY = new SIRectangle(0, 0, 2, thickness, size, c);
        cursorX.setBorderColor(c);
        cursorX.setVisible(true);
        this.ownerSpace = cursorSpace;
        this.pickingSpace = pickingSpace;
        ownerSpace.addGlyph(cursorX);
        cursorX.setSensitivity(false);
        cursorY.setBorderColor(c);
        cursorY.setVisible(true);
        ownerSpace.addGlyph(cursorY);
        cursorY.setSensitivity(false);
        c = getPhantomColor();
        phcursorX = new SIRectangle(0, 0, 2, size, thickness, c);
        phcursorY = new SIRectangle(0, 0, 2, thickness, size, c);
        circle = new SICircle(0, 0, 2, size, c);
        ownerSpace.addGlyph(circle);
        ownerSpace.addGlyph(phcursorX);
        ownerSpace.addGlyph(phcursorY);
        circle.setSensitivity(false);
        phcursorX.setSensitivity(false);
        phcursorY.setSensitivity(false);
        circle.setBorderColor(c);
        phcursorX.setBorderColor(Color.WHITE);
        phcursorY.setBorderColor(Color.WHITE);
        phcursorX.setColor(Color.WHITE);
        phcursorY.setColor(Color.WHITE);
        circle.setFilled(true);
        phcursorX.setFilled(true);
        phcursorY.setFilled(true);
        phcursorX.setVisible(false);
        phcursorY.setVisible(false);
        circle.setVisible(false);
        picker = new PPicker();
        picker2 = new PPicker();
        pickingSpace.registerPicker(picker);
        menuSpace.registerPicker(picker2);
        this.mCamera = locCam;
        this.mCamera2 = menuCam;
        this.viewListener = eh;
    }

    public PCursor() {
    }

    /**
	 * Moves the cursor to the specified position.
	 * @param x The x virtual coordinate of the {@link PCursor}
	 * @param y The y virtual coordinate of the {@link PCursor}
	 * @param jpx The x JPanel coordinate of the {@link PCursor}, used by the {@link Picker} when picking a {@link Glyph} in projected coordinates (not the case for {@link MetisseWindow})
	 * @param jpy The y JPanel coordinate of the {@link PCursor}, used by the {@link Picker} when picking a {@link Glyph} in projected coordinates (not the case for {@link MetisseWindow})
	 */
    public void moveCursorTo(double x, double y, int jpx, int jpy) {
        this.vx = x;
        this.vy = y;
        cursorX.moveTo(x, y);
        cursorY.moveTo(x, y);
        movePhantom(x, y);
        picker.setVSCoordinates(x, y);
        picker.setJPanelCoordinates(jpx, jpy);
        Camera c = ownerSpace.getCamera(0);
        double a = (c.focal + c.altitude) / c.focal;
        picker2.setVSCoordinates((x - c.vx) / a, (y - c.vy) / a);
        picker2.setJPanelCoordinates(jpx, jpy);
        refreshPicker();
    }

    /**
	 * Moves the phantom cursor to the specified position.
	 * @param x The x virtual coordinate of the phantom cursor
	 * @param y The y virtual coordinate of the phantom cursor
	 */
    private void movePhantom(double x, double y) {
        if (!phantomMode) return;
        bounds = pickingSpace.getCamera(0).getOwningView().getVisibleRegion(pickingSpace.getCamera(0));
        double w = (bounds[2] - bounds[0]);
        double h = (bounds[1] - bounds[3]);
        double W = (wallBounds[2] - wallBounds[0]);
        double H = (wallBounds[1] - wallBounds[3]);
        double xx = (x - wallBounds[0]) / W * w + bounds[0];
        double yy = (y - bounds[1]) / H * h + bounds[3];
        wallX = x;
        wallY = y - bounds[1] + wallBounds[3];
        if (mirrorMode) {
            yy = bounds[1] + bounds[3] - yy;
        }
        phcursorX.moveTo(xx, yy);
        phcursorY.moveTo(xx, yy);
        circle.moveTo(xx, yy);
    }

    /**
	 * @return The {@link PPicker} of the {@link VirtualSpace} containing the Metisse windows.
	 */
    public PPicker getPicker() {
        return picker;
    }

    /**
	 * @return The {@link PPicker} of the {@link VirtualSpace} containing the {@link PopMenu}.
	 */
    public PPicker getPicker2() {
        return picker2;
    }

    /**
	 * Sets the color of the cross representing the cursor.
	 * @param color a {@link Color} object.
	 */
    public void setColor(Color color) {
        cursorX.setBorderColor(color);
        cursorX.setColor(color);
        cursorY.setBorderColor(color);
        cursorY.setColor(color);
    }

    /**
	 * @return The x virtual coordinate
	 */
    public double getVSXCoordinate() {
        return vx;
    }

    /**
	 * @return The y virtual coordinate
	 */
    public double getVSYCoordinate() {
        return vy;
    }

    /**
	 * Refreshes pickers' pick list.
	 */
    public void refreshPicker() {
        picker.computePickedGlyphList(viewListener, mCamera);
        picker2.computePickedGlyphList(viewListener, mCamera2);
    }

    private static Color getColor() {
        return new Color(200, 0, 0);
    }

    private static Color getPhantomColor() {
        return new Color(000, 150, 150);
    }

    /**
	 * Makes the main cross invisible and the phantom cross visible. It also handles border mapping.
	 */
    public void enablePhantomMode() {
        if (phantomMode) return;
        phantomMode = true;
        bounds = pickingSpace.getCamera(0).getOwningView().getVisibleRegion(pickingSpace.getCamera(0));
        double w = (bounds[2] - bounds[0]);
        double W = (wallBounds[2] - wallBounds[0]);
        Camera c = ownerSpace.getCamera(0);
        Dimension d = parent.viewer.getView().getPanelSize();
        double coef = c.focal / (c.focal + c.altitude);
        double vxx = (vx - bounds[0]) * W / w + wallBounds[0];
        int cx = (int) Math.round((d.width / 2) + (vxx - c.vx) * coef);
        int cy = (int) Math.round((d.height / 2) - (vy - c.vy) * coef);
        parent.jumpTo(vxx, vy, cx, cy);
        phcursorX.setVisible(true);
        phcursorY.setVisible(true);
        circle.setVisible(true);
    }

    /**
	 * Makes the main cross visible and the phantom cross invisible. It also handles border mapping.
	 */
    public void disablePhantomMode() {
        if (!phantomMode) return;
        phantomMode = false;
        bounds = pickingSpace.getCamera(0).getOwningView().getVisibleRegion(pickingSpace.getCamera(0));
        double w = (bounds[2] - bounds[0]);
        double W = (wallBounds[2] - wallBounds[0]);
        Camera c = ownerSpace.getCamera(0);
        Dimension d = parent.viewer.getView().getPanelSize();
        double coef = c.focal / (c.focal + c.altitude);
        double vxx = (vx - wallBounds[0]) * w / W + bounds[0];
        int cx = (int) Math.round((d.width / 2) + (vxx - c.vx) * coef);
        int cy = (int) Math.round((d.height / 2) - (vy - c.vy) * coef);
        parent.jumpTo(vxx, vy, cx, cy);
        phcursorX.setVisible(false);
        phcursorY.setVisible(false);
        circle.setVisible(false);
    }

    /**
	 * Enables phantom mode if disabled, disables it otherwise.
	 */
    public void togglePhantomMode() {
        if (phantomMode) disablePhantomMode(); else enablePhantomMode();
    }

    /**
	 * @see PCursor#getWallY()
	 * @return the x-coordinate in the wall
	 */
    public double getWallX() {
        return wallX;
    }

    /**
	 * @see PCursor#getWallX()
	 * @return the y-coordinate in the wall (it differs from {@link PCursor#getVSYCoordinate()} since there is an offset of the height of the laptop's screen)
	 */
    public double getWallY() {
        return wallY;
    }

    /**
	 * Sets the owner of this {@link PCursor}
	 * @param cursorHandler a {@link CursorHandler} object
	 */
    public void setParent(CursorHandler cursorHandler) {
        parent = cursorHandler;
    }

    /**
	 * Makes the current cursor (main of phantom) visible or not
	 * @param b
	 */
    public void setVisible(boolean b) {
        cursorX.setVisible(b);
        cursorY.setVisible(b);
        if (!phantomMode) return;
        phcursorX.setVisible(b);
        phcursorY.setVisible(b);
        circle.setVisible(b);
    }

    /**
	 * Removes all glyphs composing this {@link PCursor} from the {@link VirtualSpace}.
	 */
    public void end() {
        ownerSpace.removeGlyph(circle);
        ownerSpace.removeGlyph(cursorX);
        ownerSpace.removeGlyph(cursorY);
        ownerSpace.removeGlyph(phcursorX);
        ownerSpace.removeGlyph(phcursorY);
    }
}
