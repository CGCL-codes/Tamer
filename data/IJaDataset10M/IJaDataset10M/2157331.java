package org.jaitools.tilecache;

/**
 * A visitor to collect information about tiles in a {@linkplain DiskMemTileCache}.
 * This can be used to examine cache performance in more detail than with
 * the cache's diagnostic methods and observer events.
 *
 * @see DiskMemTileCache
 * @author Michael Bedward
 * @since 1.0
 * @version $Id: DiskMemTileCacheVisitor.java 1711 2011-06-16 07:41:42Z michael.bedward $
 */
public interface DiskMemTileCacheVisitor {

    /**
     * Called by the cache once for each tile 
     * 
     * @param tile the tile being visited
     * @param isResident set by the cache to indicate whether the tile is
     * currently resident in memory
     */
    public void visit(DiskCachedTile tile, boolean isResident);
}
