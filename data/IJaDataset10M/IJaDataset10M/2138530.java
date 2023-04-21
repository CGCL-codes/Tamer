package co.edu.unal.ungrid.util;

import co.edu.unal.ungrid.core.AbstractDimension;
import co.edu.unal.ungrid.core.Dimension3D;
import co.edu.unal.ungrid.core.Position3D;
import co.edu.unal.ungrid.image.util.TileHelper;

public class DoublePrecisionTileHelper extends TileHelper<Double> {

    public DoublePrecisionTileHelper(final AbstractDimension<Double> domnSize, final AbstractDimension<Double> tileSize) {
        super(domnSize, tileSize);
    }

    public DoublePrecisionTileHelper(final AbstractDimension<Double> domnSize, final AbstractDimension<Double> tileSize, final AbstractDimension<Double> brdrSize) {
        super(domnSize, tileSize, brdrSize);
    }

    public DoublePrecisionTileHelper(final AbstractDimension<Double> domnSize, final AbstractDimension<Double> tileSize, final AbstractDimension<Double> brdrSize, final AbstractDimension<Double> elemSize) {
        super(domnSize, tileSize, brdrSize, elemSize);
    }

    @Override
    public Position3D<Double> getTilePosBorder(int x, int y, int z) {
        return new Position3D<Double>(x * (m_tileSize.getWidth().doubleValue() - 2 * m_brdrSize.getWidth().doubleValue()), y * (m_tileSize.getHeight().doubleValue() - 2 * m_brdrSize.getHeight().doubleValue()), z * (m_tileSize.getDepth().doubleValue() - 2 * m_brdrSize.getDepth().doubleValue()));
    }

    public Position3D<Double> getTilePosNoBorder(int x, int y, int z) {
        Position3D<Double> pos = new Position3D<Double>(x * m_tileSize.getWidth().doubleValue(), y * m_tileSize.getHeight().doubleValue(), z * m_tileSize.getDepth().doubleValue());
        return pos;
    }

    public static void testTilingNoBorder(String[] args) {
        System.out.println("RealTileHelper.testTilingNoBorder()");
        AbstractDimension<Double> domnSize = new Dimension3D<Double>(0.00020, 0.00020, 0.00002);
        AbstractDimension<Double> tileSize = new Dimension3D<Double>(0.00010, 0.00010, 0.00002);
        System.out.println("domnSize=" + domnSize);
        System.out.println("tileSize=" + tileSize);
        DoublePrecisionTileHelper helper = new DoublePrecisionTileHelper(domnSize, tileSize);
        System.out.println("xt=" + helper.numTilesX());
        System.out.println("yt=" + helper.numTilesY());
        System.out.println("zt=" + helper.numTilesZ());
        for (int z = 0; z < helper.numTilesZ(); z++) {
            for (int y = 0; y < helper.numTilesY(); y++) {
                for (int x = 0; x < helper.numTilesX(); x++) {
                    System.out.println("[" + x + "," + y + "," + z + "] " + helper.getTilePosNoBorder(x, y, z));
                }
            }
        }
    }

    public static void testTilingBorder(String[] args) {
        System.out.println("RealTileHelper.testTilingBorder()");
        AbstractDimension<Double> domnSize = new Dimension3D<Double>(20.0, 20.0, 2.0);
        AbstractDimension<Double> tileSize = new Dimension3D<Double>(10.0, 10.0, 2.0);
        AbstractDimension<Double> brdrSize = new Dimension3D<Double>(2.0, 2.0, 2.0);
        System.out.println("domnSize=" + domnSize);
        System.out.println("tileSize=" + tileSize);
        System.out.println("brdrSize=" + brdrSize);
        DoublePrecisionTileHelper helper = new DoublePrecisionTileHelper(domnSize, tileSize, brdrSize);
        System.out.println("xt=" + helper.numTilesX());
        System.out.println("yt=" + helper.numTilesY());
        System.out.println("zt=" + helper.numTilesZ());
        for (int z = 0; z < helper.numTilesZ(); z++) {
            for (int y = 0; y < helper.numTilesY(); y++) {
                for (int x = 0; x < helper.numTilesX(); x++) {
                    System.out.println("[" + x + "," + y + "," + z + "] " + helper.getTilePosBorder(x, y, z));
                }
            }
        }
    }

    public static void main(String[] args) {
        testTilingNoBorder(args);
        testTilingBorder(args);
    }
}
