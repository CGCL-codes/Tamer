package info.reflectionsofmind.connexion.tilelist;

public final class TileSourceUtil {

    private TileSourceUtil() {
        throw new UnsupportedOperationException();
    }

    public static TileData getTileDataByCode(final ITileSource tileSource, final String code) {
        for (final TileData tileData : tileSource.getTiles()) {
            if (code.equals(tileData.getCode())) return tileData;
        }
        return null;
    }
}
