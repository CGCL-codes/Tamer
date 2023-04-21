package bettria.streetRouting.game;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import bettria.streetrouting.producers.WeltGenerator;
import bettria.streetrouting.producers.WeltManager;
import jgame.*;
import jgame.platform.*;
import static bettria.streetRouting.game.Auto.Direction.*;

/**
 * 
 * @author B. Bettz&uuml;che
 * @author S. Zagaria
 *
 */
public class StreetRouting extends JGEngine {

    private static final long serialVersionUID = -2420333417188519400L;

    /**Gr&ouml;&szlig;e eines Tiles in Pixel.*/
    private static final JGPoint TILESIZE = new JGPoint(90, 90);

    /**Anzahl sichtbarer Tiles (Kacheln).*/
    private static final JGPoint CANV_TILES = new JGPoint(8, 8);

    private static final String SGN_CROSSING = "#";

    private static final String SGN_STREET_HOR = "*";

    private static final String SGN_STREET_VER = "$";

    /**Gibt Infos zur Welt (im Space)*/
    private WeltManager welt;

    public static void main(String[] args) {
        new StreetRouting(new JGPoint(800, 800));
    }

    public StreetRouting() {
        super.initEngineApplet();
    }

    public StreetRouting(JGPoint size) {
        startSpace();
        super.initEngine(size.x, size.y);
    }

    /**
	 * Sichtbare Fl&auml;che (Canvas) initialisieren.
	 * <p>{@inheritDoc}</p>
	 */
    public void initCanvas() {
        setCanvasSettings(CANV_TILES.x, CANV_TILES.y, TILESIZE.x, TILESIZE.y, JGColor.black, JGColor.white, null);
    }

    /**
	 * 
	 * <p>{@inheritDoc}</p>
	 */
    @SuppressWarnings(value = "unused")
    public void initGame() {
        defineMedia("mygame.tbl");
        setFrameRate(45, 1);
        generateTiles();
        Point size = welt.getWorldSize();
        Auto a1 = new Auto(1, 4, OST, size.x, size.y);
        Auto a4 = new Auto(0, 4, OST, size.x, size.y);
        Auto a2 = new Auto(2, 6, NORD, size.x, size.y);
        Auto a3 = new Auto(2, 7, NORD, size.x, size.y);
        Auto a8 = new Auto(2, 8, NORD, size.x, size.y);
        Auto a5 = new Auto(6, 2, NORD, size.x, size.y);
        Auto a6 = new Auto(6, 4, NORD, size.x, size.y);
    }

    /**
	 * <p>
	 * Beachte, dass die gegebenen Koordinaten bei (1,1) anfangen!
	 * </p>
	 */
    private void generateTiles() {
        Point size = welt.getWorldSize();
        List<Point> xKoords = welt.getKreuzungKoords();
        super.setPFSize(size.x, size.y);
        {
            Random r = new Random();
            String[] houses = { "h1", "h2", "h3" };
            for (int row = 0; row < size.y; row++) {
                for (int col = 0; col < size.x; col++) setTile(row, col, houses[r.nextInt(3)]);
            }
        }
        for (Point crossing : xKoords) {
            int cx = crossing.x - 1;
            int cy = crossing.y - 1;
            if (super.getTileStr(0, cy).startsWith("h")) {
                for (int x = 0; x < size.x; x++) super.setTile(x, cy, SGN_STREET_HOR);
            }
            if (super.getTileStr(cx, 0).startsWith("h")) {
                for (int y = 0; y < size.y; y++) setTile(cx, y, SGN_STREET_VER);
            }
            setTile(cx, cy, SGN_CROSSING);
        }
    }

    /**
	 * Startet den GSpace mittels <tt>producers.Weltgenerator</tt>.
	 */
    private void startSpace() {
        WeltGenerator wg = WeltGenerator.getInstance();
        wg.initWorld();
        welt = wg.getWeltManager();
    }

    /**
	 * 
	 * Spiellogik ausf&uuml;hren.<p>
	 * Hier kann kein 'painting' gemacht werden. &Uuml;berschreibe
	 * daf&uuml;r <tt>paintFrame</tt>.</p>
	 * <p>{@inheritDoc}</p>
	 */
    @Override
    public void doFrame() {
        super.moveObjects();
    }
}
