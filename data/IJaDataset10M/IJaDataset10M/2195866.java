package org.osmdroid.tileprovider.modules;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.CantContinueException;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import android.graphics.drawable.Drawable;

/**
 * An abstract base class for modular tile providers
 *
 * @author Marc Kurtz
 * @author Neil Boyd
 */
public abstract class MapTileModuleProviderBase implements OpenStreetMapTileProviderConstants {

    /**
	 * Gets the human-friendly name assigned to this tile provider.
	 *
	 * @return the thread name
	 */
    protected abstract String getName();

    /**
	 * Gets the name assigned to the thread for this provider.
	 *
	 * @return the thread name
	 */
    protected abstract String getThreadGroupName();

    /**
	 * It is expected that the implementation will construct an internal member which internally
	 * implements a {@link TileLoader}. This method is expected to return a that internal member to
	 * methods of the parent methods.
	 *
	 * @return the internal member of this tile provider.
	 */
    protected abstract Runnable getTileLoader();

    /**
	 * Returns true if implementation uses a data connection, false otherwise. This value is used to
	 * determine if this provider should be skipped if there is no data connection.
	 *
	 * @return true if implementation uses a data connection, false otherwise
	 */
    public abstract boolean getUsesDataConnection();

    /**
	 * Gets the minimum zoom level this tile provider can provide
	 *
	 * @return the minimum zoom level
	 */
    public abstract int getMinimumZoomLevel();

    /**
	 * Gets the maximum zoom level this tile provider can provide
	 *
	 * @return the maximum zoom level
	 */
    public abstract int getMaximumZoomLevel();

    /**
	 * Sets the tile source for this tile provider.
	 *
	 * @param tileSource
	 *            the tile source
	 */
    public abstract void setTileSource(ITileSource tileSource);

    private static final Logger logger = LoggerFactory.getLogger(MapTileModuleProviderBase.class);

    private final int mThreadPoolSize;

    private final ThreadGroup mThreadPool = new ThreadGroup(getThreadGroupName());

    private final ConcurrentHashMap<MapTile, MapTileRequestState> mWorking;

    final LinkedHashMap<MapTile, MapTileRequestState> mPending;

    public MapTileModuleProviderBase(final int pThreadPoolSize, final int pPendingQueueSize) {
        mThreadPoolSize = pThreadPoolSize;
        mWorking = new ConcurrentHashMap<MapTile, MapTileRequestState>();
        mPending = new LinkedHashMap<MapTile, MapTileRequestState>(pPendingQueueSize + 2, 0.1f, true) {

            private static final long serialVersionUID = 6455337315681858866L;

            @Override
            protected boolean removeEldestEntry(final Entry<MapTile, MapTileRequestState> pEldest) {
                return size() > pPendingQueueSize;
            }
        };
    }

    public void loadMapTileAsync(final MapTileRequestState pState) {
        final int activeCount = mThreadPool.activeCount();
        synchronized (mPending) {
            mPending.put(pState.getMapTile(), pState);
        }
        if (DEBUGMODE) {
            logger.debug(activeCount + " active threads");
        }
        if (activeCount < mThreadPoolSize) {
            final Thread t = new Thread(mThreadPool, getTileLoader());
            t.start();
        }
    }

    private void clearQueue() {
        synchronized (mPending) {
            mPending.clear();
        }
        mWorking.clear();
    }

    /**
	 * Detach, we're shutting down - Stops all workers.
	 */
    public void detach() {
        this.clearQueue();
        this.mThreadPool.interrupt();
    }

    private void removeTileFromQueues(final MapTile mapTile) {
        synchronized (mPending) {
            mPending.remove(mapTile);
        }
        mWorking.remove(mapTile);
    }

    /**
	 * Load the requested tile. An abstract internal class whose objects are used by worker threads
	 * to acquire tiles from servers. It processes tiles from the 'pending' set to the 'working' set
	 * as they become available. The key unimplemented method is 'loadTile'.
	 *
	 * @param aTile
	 *            the tile to load
	 * @throws CantContinueException
	 *             if it is not possible to continue with processing the queue
	 */
    protected abstract class TileLoader implements Runnable {

        /**
		 * The key unimplemented method.
		 *
		 * @return true if the tile was loaded successfully and other tile providers need not be
		 *         called, false otherwise
		 * @param pState
		 * @throws {@link CantContinueException}
		 */
        protected abstract Drawable loadTile(MapTileRequestState pState) throws CantContinueException;

        private MapTileRequestState nextTile() {
            synchronized (mPending) {
                MapTile result = null;
                Iterator<MapTile> iterator = mPending.keySet().iterator();
                while (iterator.hasNext()) {
                    try {
                        final MapTile tile = iterator.next();
                        if (!mWorking.containsKey(tile)) {
                            result = tile;
                        }
                    } catch (final ConcurrentModificationException e) {
                        if (DEBUGMODE) {
                            logger.warn("ConcurrentModificationException break: " + (result != null));
                        }
                        if (result != null) {
                            break;
                        } else {
                            iterator = mPending.keySet().iterator();
                        }
                    }
                }
                if (result != null) {
                    mWorking.put(result, mPending.get(result));
                }
                return (result != null ? mPending.get(result) : null);
            }
        }

        /**
		 * A tile has loaded.
		 */
        private void tileLoaded(final MapTileRequestState pState, final Drawable pDrawable) {
            removeTileFromQueues(pState.getMapTile());
            pState.getCallback().mapTileRequestCompleted(pState, pDrawable);
        }

        protected void tileCandidateLoaded(final MapTileRequestState pState, final Drawable pDrawable) {
            pState.getCallback().mapTileRequestCandidate(pState, pDrawable);
        }

        private void tileLoadedFailed(final MapTileRequestState pState) {
            removeTileFromQueues(pState.getMapTile());
            pState.getCallback().mapTileRequestFailed(pState);
        }

        /**
		 * This is a functor class of type Runnable. The run method is the encapsulated function.
		 */
        @Override
        public final void run() {
            MapTileRequestState state;
            Drawable result = null;
            while ((state = nextTile()) != null) {
                if (DEBUGMODE) {
                    logger.debug("Next tile: " + state);
                }
                try {
                    result = null;
                    result = loadTile(state);
                } catch (final CantContinueException e) {
                    logger.info("Tile loader can't continue", e);
                    clearQueue();
                } catch (final Throwable e) {
                    logger.error("Error downloading tile: " + state, e);
                }
                if (result != null) {
                    tileLoaded(state, result);
                } else {
                    tileLoadedFailed(state);
                }
                if (DEBUGMODE) {
                    logger.debug("No more tiles");
                }
            }
        }
    }

    class CantContinueException extends Exception {

        private static final long serialVersionUID = 146526524087765133L;

        public CantContinueException(final String pDetailMessage) {
            super(pDetailMessage);
        }

        public CantContinueException(final Throwable pThrowable) {
            super(pThrowable);
        }
    }
}
