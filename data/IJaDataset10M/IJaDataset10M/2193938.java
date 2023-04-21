package org.dcm4chee.xero.wado;

import java.io.IOException;
import java.util.Map;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.imageio.plugins.dcm.DicomStreamMetaData;
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReader;
import org.dcm4chee.xero.metadata.MetaData;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.dcm4chee.xero.metadata.filter.FilterItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter just knows how to convert a dicom image reader into a dicom
 * object
 */
public class DicomImageReaderToDicomObject implements Filter<DicomObject> {

    private static final Logger log = LoggerFactory.getLogger(DicomImageReaderToDicomObject.class);

    Filter<DicomImageReader> imageReaderFilter;

    /** Just read the dicom object from the header */
    public DicomObject filter(FilterItem<DicomObject> filterItem, Map<String, Object> params) {
        DicomImageReader dir = imageReaderFilter.filter(null, params);
        if (dir == null) return null;
        try {
            DicomObject ds = null;
            synchronized (dir) {
                ds = ((DicomStreamMetaData) dir.getStreamMetadata()).getDicomObject();
            }
            return ds;
        } catch (IOException e) {
            log.warn("Unable to read dicom file:", e);
            throw new RuntimeException("Unable to read dicom file", e);
        }
    }

    /** Gets the image reader filter that reads the dicom object in */
    public Filter<DicomImageReader> getImageReaderFilter() {
        return imageReaderFilter;
    }

    /** Sets the default dicom reader to be "dicom" */
    @MetaData(out = "${ref:dicomImageReader}")
    public void setImageReaderFilter(Filter<DicomImageReader> imageReaderFilter) {
        this.imageReaderFilter = imageReaderFilter;
    }
}
