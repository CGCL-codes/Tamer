package cs.pancava.caltha.worlds;

import cs.pancava.caltha.Editor;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;

/**
 * <p><b>Tato trida definuje vlastnosti mistnosti ve vytvorenem svete.</b></p>
 *
 * @author Milan Vaclavik<br />
 * @version $Revision: 22 $<br />
 * $LastChangedBy: mihlon $<br />
 */
public class Room extends GameEntity {

    /**
     * Graficka reprezentace mistnosti v editoru.
     */
    private Ellipse2D graphicsObject;

    /**
     * Prumer mistnosti (kruznice).
     */
    private int diameter;

    /**
     * Barva mistnosti.
     */
    private final Color ROOM_COLOR = Color.BLUE;

    /**
     * Barva vybrane mistnosti.
     */
    private final Color ROOM_SELECTED_COLOR = Color.CYAN;

    /**
     * Barva popisku - textu.
     */
    private final Color TEXT_COLOR = Color.WHITE;

    /**
     * Konstruktor - nastavuje souradnice umisteni dane mistnosti v editoru.
     * @param x int : X-ova souradnice mistnosti.
     * @param y int : Y-ova souradnice mistnosti.
     * @param r int : Polomer kruznice - urcuje velikost mistnosti.
     */
    public Room(final int x, final int y, final int r) {
        super(x, y, World.WORLD_ROOM, Editor.getEditorDesktop().getWorld().getWorldRoomsAL().size());
        this.diameter = r;
        this.graphicsObject = new Ellipse2D.Double(x, y, this.diameter, this.diameter);
    }

    /**
     * Vraci stred objektu v ose X.
     * @return int : Stred objektu v ose X.
     */
    @Override
    public final int getCenterX() {
        return this.getLocationX() + this.diameter / 2;
    }

    /**
     * Vraci stred objektu v ose Y.
     * @return int : Stred objektu v ose Y.
     */
    @Override
    public final int getCenterY() {
        return this.getLocationY() + this.diameter / 2;
    }

    /**
     * Vraci grafickou reprezentaci mistnosti v editoru.
     * @return RectangularShape : Graficka reprezentace mistnosti v editoru.
     */
    @Override
    public final Object getGraphicsObject() {
        return this.graphicsObject;
    }

    /**
     * Zobrazi danou mistnost v editoru.
     * @param g2 Graphics2D : Graficky kontext, do ktereho se bude kreslit.
     */
    @Override
    public final void showGraphicsObject(final Graphics2D g2) {
        final TextLayout textID = new TextLayout(String.valueOf(this.getId()), new Font("Helvetica", 0, this.diameter / 2), g2.getFontRenderContext());
        this.graphicsObject.setFrame(this.getLocationX(), this.getLocationY(), this.diameter, this.diameter);
        if (this.isSeleted()) {
            g2.setColor(this.ROOM_SELECTED_COLOR);
        } else {
            g2.setColor(this.ROOM_COLOR);
        }
        g2.fill(this.graphicsObject);
        g2.setColor(this.TEXT_COLOR);
        textID.draw(g2, this.getLocationX() + (int) ((this.diameter - textID.getBounds().getWidth()) / 2), this.getLocationY() + (int) (this.diameter - textID.getBounds().getHeight()));
    }

    /**
     * V teto tride nepotrebuji tuto vlastnost, proto vratime null.
     * @return gameEntity : Vratime null, protoze je tato vlastnost v tomto kontextu k nicemu.
     */
    @Override
    public final GameEntity getFirstRoom() {
        return null;
    }

    /**
     * V teto tride nepotrebuji tuto vlastnost, proto vratime null.
     * @return gameEntity : Vratime null, protoze je tato vlastnost v tomto kontextu k nicemu.
     */
    @Override
    public final GameEntity getSecondRoom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
