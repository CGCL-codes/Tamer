package org.kalypso.nofdpidss.geodata.wizard.gds.add.commmon;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IPath;
import org.kalypso.nofdpidss.core.common.utils.modules.IGeodataPageProvider.DATA_TYPE;
import org.kalypso.nofdpidss.geodata.wizard.gds.add.raster.VerifiyRasterData;
import org.kalypso.nofdpidss.geodata.wizard.gds.add.raster.VerifyImageData;
import org.kalypso.nofdpidss.geodata.wizard.gds.add.shape.VerifiyShapeData;

/**
 * @author Dirk Kuch
 */
public class GeoDataFileVerifier implements IVerifiyGeodata {

    private static final String[] SHAPE_EXTENSION = new String[] { "shp" };

    private static final String[] RASTER_EXTENSION = new String[] { "asc", "dat" };

    private static final String[] IMAGE_EXTENSION = new String[] { "tif", "gif", "png", "jpg", "jpeg", "tiff" };

    private final IPath m_docLocation;

    private IVerifiyGeodata m_delegate;

    public GeoDataFileVerifier(final IPath docLocation) {
        m_docLocation = docLocation;
        determineType();
    }

    private void determineType() {
        String ext = m_docLocation.getFileExtension();
        if (ext != null) ext = ext.toLowerCase();
        if (ArrayUtils.contains(GeoDataFileVerifier.SHAPE_EXTENSION, ext)) m_delegate = new VerifiyShapeData(m_docLocation); else if (ArrayUtils.contains(GeoDataFileVerifier.RASTER_EXTENSION, ext)) m_delegate = new VerifiyRasterData(); else if (ArrayUtils.contains(GeoDataFileVerifier.IMAGE_EXTENSION, ext)) m_delegate = new VerifyImageData();
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.geodata.add.IVerifiyGeodata#getType()
   */
    public DATA_TYPE getType() {
        if (m_delegate == null) return null;
        return m_delegate.getType();
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.geodata.add.IVerifiyGeodata#verify()
   */
    public boolean verify() {
        if (m_delegate == null) return false;
        return m_delegate.verify();
    }
}
