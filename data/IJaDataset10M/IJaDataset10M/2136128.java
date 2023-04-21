package de.byteholder.geoclipse.mapprovider;

import java.util.ArrayList;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import de.byteholder.geoclipse.logging.StatusUtil;
import de.byteholder.geoclipse.map.ParentImageStatus;
import de.byteholder.geoclipse.map.Tile;
import de.byteholder.geoclipse.map.TileFactory;
import de.byteholder.geoclipse.swt.UI;

/**
 * Wraps all map providers into a map profile, these map providers can be selected individually
 */
public class MPProfile extends MP {

    private static final String WMS_CUSTOM_TILE_PATH = "all-map-profile-wms";

    /**
	 * contains a wrapper for all none profile map providers
	 */
    private ArrayList<MapProviderWrapper> fMpWrappers;

    private TileFactoryProfile fTileFactory;

    /**
	 * background color for the profile image, this color is displayed in the transparent areas
	 */
    private int fBackgroundColor = 0xFFFFFF;

    private boolean fIsSaveImage = true;

    public MPProfile() {
    }

    public MPProfile(final ArrayList<MapProviderWrapper> mpWrappers) {
        fMpWrappers = mpWrappers;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final MPProfile clonedMpProfile = (MPProfile) super.clone();
        final ArrayList<MapProviderWrapper> clonedMpWrapperList = new ArrayList<MapProviderWrapper>();
        for (final MapProviderWrapper mpWrapper : fMpWrappers) {
            clonedMpWrapperList.add((MapProviderWrapper) mpWrapper.clone());
        }
        clonedMpProfile.fMpWrappers = clonedMpWrapperList;
        clonedMpProfile.fTileFactory = new TileFactoryProfile(clonedMpProfile);
        return clonedMpProfile;
    }

    /**
	 * Creates tile children for all mp wrapper which are displayed in one tile
	 * 
	 * @param parentTile
	 * @return
	 */
    public ArrayList<Tile> createTileChildren(final Tile parentTile) {
        final ArrayList<Tile> tileChildren = new ArrayList<Tile>();
        for (final MapProviderWrapper mpWrapper : fMpWrappers) {
            final int parentZoom = parentTile.getZoom();
            final MP mapProvider = mpWrapper.getMapProvider();
            if (parentZoom < mapProvider.getMinZoomLevel() || parentZoom > mapProvider.getMaxZoomLevel()) {
                continue;
            }
            if (mpWrapper.isDisplayedInMap() && mpWrapper.isEnabled()) {
                final MP childMP = mapProvider;
                final TileFactory childTileFactory = childMP.getTileFactory();
                final Tile childTile = new Tile(childTileFactory, parentTile.getX(), parentTile.getY(), parentZoom, childMP.getId());
                childTile.setBbox(childTileFactory.getInfo(), childTileFactory.getProjection());
                childTile.setParentTile(parentTile);
                childTile.setAlpha(mpWrapper.getAlpha());
                childTile.setIsTransparentPixel(mpWrapper.isTransparentPixel());
                childTile.setTransparentPixel(mpWrapper.getTransparentPixel());
                if (childTileFactory instanceof TileFactoryWms) {
                    childTile.setTileCustomPath(WMS_CUSTOM_TILE_PATH);
                }
                tileChildren.add(childTile);
            }
        }
        return tileChildren;
    }

    /**
	 * Create a wrapper from a map provider
	 * 
	 * @param mapProvider
	 * @return
	 */
    private MapProviderWrapper createWrapper(final MP mapProvider) {
        MapProviderWrapper mpWrapper = null;
        try {
            final MP clonedMapProvider = (MP) mapProvider.clone();
            mpWrapper = new MapProviderWrapper(clonedMapProvider);
            mpWrapper.setIsDisplayedInMap(false);
        } catch (final CloneNotSupportedException e) {
            StatusUtil.log(e);
        }
        return mpWrapper;
    }

    @Override
    public void disposeCachedImages() {
    }

    /**
	 * @return Returns the wrappers for all map providers
	 */
    public ArrayList<MapProviderWrapper> getAllWrappers() {
        return fMpWrappers;
    }

    public int getBackgroundColor() {
        return fBackgroundColor;
    }

    /**
	 * draw all children over each other
	 * 
	 * @param parentTile
	 * @return
	 */
    public ParentImageStatus getParentImage(final Tile parentTile) {
        final ArrayList<Tile> tileChildren = parentTile.getChildren();
        if (tileChildren == null) {
            return null;
        }
        final TileImage parentImage = new TileImage();
        parentImage.setBackgroundColor(fBackgroundColor);
        boolean isFinal = true;
        final StringBuilder sbChildLoadingError = new StringBuilder();
        for (final Tile childTile : tileChildren) {
            final String childLoadingError = childTile.getLoadingError();
            if (childLoadingError != null) {
                sbChildLoadingError.append(childTile.getUrl());
                sbChildLoadingError.append(UI.NEW_LINE);
                sbChildLoadingError.append(childLoadingError);
                sbChildLoadingError.append(UI.NEW_LINE);
                continue;
            }
            final ImageData[] childImageData = childTile.getChildImageData();
            if (childImageData == null || childImageData[0] == null) {
                isFinal = false;
                continue;
            }
            parentImage.drawImage(childImageData[0], childTile);
        }
        final ParentImageStatus parentImageStatus = new ParentImageStatus(new ImageData[] { parentImage.getImageData() }, isFinal, sbChildLoadingError.toString(), fIsSaveImage);
        return parentImageStatus;
    }

    @Override
    public TileFactory getTileFactory() {
        fTileFactory.getInfo();
        return fTileFactory;
    }

    IPath getTileOSPath(final String fullPath, final int x, final int y, final int zoomLevel) {
        final IPath filePath = new Path(fullPath).append(getOfflineFolder()).append(Integer.toString(zoomLevel)).append(Integer.toString(x)).append(Integer.toString(y)).addFileExtension(MapProviderManager.getFileExtension(getImageFormat()));
        return filePath;
    }

    public void setBackgroundColor(final int backgroundColor) {
        fBackgroundColor = backgroundColor;
    }

    public void setBackgroundColor(final RGB rgb) {
        fBackgroundColor = ((rgb.red & 0xFF) << 0) | ((rgb.green & 0xFF) << 8) | ((rgb.blue & 0xFF) << 16);
    }

    public void setIsSaveImage(final boolean isSaveImage) {
        fIsSaveImage = isSaveImage;
    }

    /**
	 * Synchronizes two map providers
	 * 
	 * @param mpWrapper
	 * @param validMapProvider
	 * @return Returns <code>false</code> when the synchronization fails
	 */
    private boolean synchMpWrapper(final MapProviderWrapper mpWrapper, final MP validMapProvider) {
        final MP wrapperMapProvider = mpWrapper.getMapProvider();
        if (wrapperMapProvider == null) {
            try {
                mpWrapper.setMapProvider((MP) validMapProvider.clone());
            } catch (final CloneNotSupportedException e) {
                StatusUtil.showStatus(e.getMessage(), e);
                return false;
            }
            return true;
        }
        final String wrapperClassName = wrapperMapProvider.getClass().getName();
        final String validClassName = validMapProvider.getClass().getName();
        if (wrapperClassName.equals(validClassName) == false) {
            StatusUtil.showStatus(NLS.bind("The map provider '{0}' has the wrong class '{1}' instead of '{2}' and will be ignored", new Object[] { mpWrapper.getMapProviderId(), wrapperClassName, validClassName }), new Exception());
            return false;
        }
        wrapperMapProvider.setName(validMapProvider.getName());
        wrapperMapProvider.setOfflineFolder(validMapProvider.getOfflineFolder());
        return true;
    }

    /**
	 * creates a wrapper for all map providers which are not a map profile
	 */
    public void synchronizeProfileMapProviders() {
        final ArrayList<MP> allMPsWithoutProfile = MapProviderManager.getInstance().getAllMapProviders(false);
        if (fMpWrappers == null) {
            fMpWrappers = new ArrayList<MapProviderWrapper>();
            for (final MP mapProvider : allMPsWithoutProfile) {
                fMpWrappers.add(createWrapper(mapProvider));
            }
        } else {
            final ArrayList<MapProviderWrapper> currentMpWrappers = fMpWrappers;
            final ArrayList<MapProviderWrapper> remainingMpWrappers = new ArrayList<MapProviderWrapper>(fMpWrappers);
            fMpWrappers = new ArrayList<MapProviderWrapper>();
            for (final MP validMapProvider : allMPsWithoutProfile) {
                final String validMapProviderId = validMapProvider.getId();
                boolean isMpValid = false;
                for (final MapProviderWrapper mpWrapper : currentMpWrappers) {
                    if (validMapProviderId.equalsIgnoreCase(mpWrapper.getMapProviderId())) {
                        if (synchMpWrapper(mpWrapper, validMapProvider) == false) {
                            break;
                        }
                        fMpWrappers.add(mpWrapper);
                        remainingMpWrappers.remove(mpWrapper);
                        isMpValid = true;
                        break;
                    }
                }
                if (isMpValid) {
                    continue;
                }
                final MapProviderWrapper mpWrapperClone = createWrapper(validMapProvider);
                fMpWrappers.add(mpWrapperClone);
            }
            if (remainingMpWrappers.size() > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("The map profile \"");
                sb.append(getName());
                sb.append("\" contains map providers, which are not available any more, they will be ignored:");
                sb.append(UI.NEW_LINE);
                sb.append(UI.NEW_LINE);
                for (final MapProviderWrapper mpWrapper : remainingMpWrappers) {
                    sb.append(mpWrapper.getMapProviderId());
                    sb.append(UI.NEW_LINE);
                }
                StatusUtil.showStatus(sb.toString(), new Exception());
            }
        }
        fTileFactory = new TileFactoryProfile(this);
    }
}
