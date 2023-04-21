package net.sourceforge.spacebutcher2.game.dataman;

import net.sourceforge.spacebutcher2.game.main.ImagesPool;
import com.golden.gamedev.object.GameFont;
import com.golden.gamedev.object.GameFontManager;

/**
 * Class representing attributes of <code>GameFont</code> bitmap: splitted into many rows and columns 
 * of bitmaps containg different characters.
 * <p><code>GameFont</code> specified by all parameters can be obtained by 
 * call to {@link #getGameFont(GameFontManager)} method.
 *
 * @author M.Olszewski
 */
public class GameFontBitmapFileAttribute {

    /** Number of all possible attributes passed to 
   * {@link GameFontBitmapFileAttribute} constructor. 
   */
    public static final int MAX_GAME_FONT_ATTRIBUTES_COUNT = 3;

    /** Number of almost all attributes (without rows number) passed to 
   * {@link GameFontBitmapFileAttribute} constructor.
   */
    public static final int AVERAGE_GAME_FONT_ATTRIBUTES_COUNT = 2;

    /** 
   * Minimum number of  attributes (without columns and rows numbers) passed to 
   * {@link GameFontBitmapFileAttribute} constructor. 
   */
    public static final int MIN_GAME_FONT_ATTRIBUTES_COUNT = 1;

    /** Invalid value of columns or rows variables. */
    private static final int INVALID_COL_ROW_VALUE = -1;

    /** Path to a file with font bitmap. */
    private String path = null;

    /** Number of columns. */
    private int columns;

    /** Number of rows. */
    private int rows;

    /**
   * Creates instance of {@link GameFontBitmapFileAttribute} class describing
   * font with specified path to its bitmap, number of columns and rows.
   * 
   * @param fontPath - path to file with font's bitmap.
   * @param fontColumns - number of font's bitmap columns.
   * @param fontRows - number of font's bitmap rows.
   */
    public GameFontBitmapFileAttribute(String fontPath, int fontColumns, int fontRows) {
        if (fontPath == null) {
            throw new NullPointerException("GameFontBitmapFileAttribute(): fontPath is null!");
        }
        path = fontPath;
        columns = fontColumns;
        rows = fontRows;
    }

    /**
   * Creates instance of {@link GameFontBitmapFileAttribute} class describing
   * font with specified path to its bitmap and number of columns.
   * 
   * @param fontPath - path to file with font's bitmap.
   * @param fontColumns - number of font's bitmap columns.
   */
    public GameFontBitmapFileAttribute(String fontPath, int fontColumns) {
        this(fontPath, fontColumns, INVALID_COL_ROW_VALUE);
    }

    /**
   * Creates instance of {@link GameFontBitmapFileAttribute} class describing
   * font only with specified path to its bitmap. 
   * <p><b>NOTE:<br>
   * This constructor should be used only with default fonts.</b>
   * 
   * @param fontPath - path to file with font's bitmap.
   */
    public GameFontBitmapFileAttribute(String fontPath) {
        this(fontPath, INVALID_COL_ROW_VALUE, INVALID_COL_ROW_VALUE);
    }

    /**
   * Gets instance of <code>GameFont</code> class based upon font bitmap file
   * attributes (number of columns, rows and path to a file).
   * 
   * @param fontManager - reference to instance of <code>GameFontManager</code> class.
   * 
   * @return Returns instance of <code>GameFont</code> class based upon 
   *         font bitmap file attributes.  
   */
    public GameFont getGameFont(GameFontManager fontManager) {
        if (fontManager == null) {
            throw new NullPointerException("GameFontBitmapFileAttribute.getGameFont(): fontManager is null.");
        }
        ImagesPool imgPool = ImagesPool.getInstance();
        GameFont font = null;
        if (columns > 0) {
            if (rows > 0) {
                font = fontManager.getFont(imgPool.loadImages(path, columns, rows));
            } else {
                font = fontManager.getFont(imgPool.loadImages(path, columns));
            }
        } else {
            font = fontManager.getFont(imgPool.loadImage(path));
        }
        return font;
    }
}
