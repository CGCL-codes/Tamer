package mobac.program.atlascreators.tileprovider;

import java.awt.image.BufferedImage;
import java.io.IOException;
import mobac.program.interfaces.MapSource;

public interface TileProvider {

    public byte[] getTileData(int x, int y) throws IOException;

    public BufferedImage getTileImage(int x, int y) throws IOException;

    public MapSource getMapSource();

    /**
	 * Indicates if subsequent filter in the filter-chain should prefer the {@link #getTileImage(int, int)} or
	 * {@link #getTileData(int, int)} method.
	 * 
	 * @return
	 */
    public boolean preferTileImageUsage();
}
