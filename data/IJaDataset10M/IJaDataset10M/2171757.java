package org.dcm4chee.xero.search.filter;

import java.util.Map;
import org.dcm4che2.data.DicomObject;
import org.dcm4chee.xero.metadata.MetaData;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.dcm4chee.xero.metadata.filter.FilterItem;
import org.dcm4chee.xero.metadata.filter.FilterUtil;
import org.dcm4chee.xero.search.AEProperties;
import org.dcm4chee.xero.search.ResultFromDicom;
import org.dcm4chee.xero.search.study.DicomObjectInterface;
import org.dcm4chee.xero.search.study.DicomObjectType;
import org.dcm4chee.xero.search.study.ImageBean;
import org.dcm4chee.xero.search.study.PatientType;
import org.dcm4chee.xero.search.study.ReportBean;
import org.dcm4chee.xero.search.study.ResultsBean;
import org.dcm4chee.xero.search.study.SeriesType;
import org.dcm4chee.xero.search.study.StudyType;
import org.dcm4chee.xero.wado.DicomFilter;
import org.dcm4chee.xero.wado.WadoParams;

/**
 * This filter allows the actual DICOM header to be read to get missing results
 * information from the DICOM header instead of from the C-Find. It is triggered
 * by the image instance having Rows/Columns of -1, AND the AE being configured
 * to run this.
 * 
 * @author bwallace
 * 
 */
public class PartialImageInfoFix implements Filter<ResultsBean> {

    public static final String PartialImageInfoFix_KEY = "partialImageInfoFix";

    AEProperties aeProperties = AEProperties.getInstance();

    /** Adds Rows,Columns back in if required */
    public ResultsBean filter(FilterItem<ResultsBean> filterItem, Map<String, Object> params) {
        ResultsBean ret = filterItem.callNextFilter(params);
        Map<String, Object> aep = AEProperties.getAE(params);
        if (ret != null && aep != null && FilterUtil.getBoolean(aep, PartialImageInfoFix_KEY)) {
            fixPartialImage(params, ret);
        }
        return ret;
    }

    /** Does the iteration over the return results to fix the items */
    protected void fixPartialImage(Map<String, Object> params, ResultsBean ret) {
        String ae = FilterUtil.getString(params, WadoParams.AE);
        for (PatientType pt : ret.getPatient()) {
            for (StudyType st : pt.getStudy()) {
                for (SeriesType se : st.getSeries()) {
                    for (DicomObjectType dot : se.getDicomObject()) {
                        if (dot instanceof ImageBean) {
                            ImageBean ib = (ImageBean) dot;
                            if (ib.getRows() >= 0) continue;
                        } else if (dot instanceof ReportBean) {
                            ReportBean rb = (ReportBean) dot;
                            if (rb.getConceptMeaning() != null) continue;
                        }
                        DicomObject ds = DicomFilter.callInstanceFilter(dicomImageHeader, (DicomObjectInterface) dot, ae);
                        if (ds == null) continue;
                        ((ResultFromDicom) dot).addResult(ds);
                    }
                }
            }
        }
    }

    private Filter<DicomObject> dicomImageHeader;

    /** Gets the filter that returns the dicom object image header */
    public Filter<DicomObject> getDicomImageHeader() {
        return dicomImageHeader;
    }

    @MetaData(out = "${ref:dicomImageHeader}")
    public void setDicomImageHeader(Filter<DicomObject> dicomImageHeader) {
        this.dicomImageHeader = dicomImageHeader;
    }
}
