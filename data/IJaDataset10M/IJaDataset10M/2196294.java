package rpg.editor.core;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import rpg.editor.model.Tile;
import rpg.editor.model.TileSet;

/**
 * Functional TileSelection stub so that we can run the TilePicker and MapEditor
 * components in isolation. 
 * @author seldred
 */
public class TileSelectionStub implements TileSelection, TileConversion {

    private Tile tile;

    private static TileSelectionStub instance = new TileSelectionStub();

    private TileSelectionStub() {
        TileSet tileSet = TileSet.loadTileSet("grass");
        tile = convertTile(tileSet.getTile("tr2"));
    }

    public static TileSelectionStub getInstance() {
        return instance;
    }

    public void tileSelected(Tile tile) {
        System.out.println("tile selected: " + tile.getName());
    }

    public Tile convertTile(Tile tile) {
        ImageData imageData = tile.getImage().getImageData();
        PaletteData paletteData = imageData.palette;
        imageData.transparentPixel = paletteData.getPixel(ImageHelper.TRANSPARENT_COLOUR);
        Image transparentImage = new Image(DisplayHelper.getDisplay(), imageData);
        return new Tile(tile.getName(), transparentImage);
    }

    public Tile getSelectedTile() {
        return tile;
    }

    public boolean isTileSelected() {
        return true;
    }
}
