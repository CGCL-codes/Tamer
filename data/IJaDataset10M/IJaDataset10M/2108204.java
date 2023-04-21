package org.dcm4chex.archive.web.maverick.model;

import org.dcm4che.data.Dataset;
import org.dcm4che.dict.Tags;

/**
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 2010 $ $Date: 2005-10-06 15:55:27 -0400 (Thu, 06 Oct 2005) $
 * @since 05.10.2004
 *
 */
public class ImageModel extends InstanceModel {

    public ImageModel(Dataset ds) {
        super(ds);
    }

    public final String getImageType() {
        return ds.getString(Tags.ImageType);
    }

    public final String getPhotometricInterpretation() {
        return ds.getString(Tags.PhotometricInterpretation);
    }

    public final int getBitsAllocated() {
        return ds.getInt(Tags.BitsAllocated, 0);
    }

    public final int getRows() {
        return ds.getInt(Tags.Rows, 0);
    }

    public final int getColumns() {
        return ds.getInt(Tags.Columns, 0);
    }

    public final int getNumberOfFrames() {
        return ds.getInt(Tags.NumberOfFrames, 1);
    }
}
