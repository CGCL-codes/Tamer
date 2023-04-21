package org.dcm4che2.iod.module.general;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.iod.module.Module;

/**
 * @author Gunter Zeilinger<gunterze@gmail.com>
 * @version $Revision: 720 $ $Date: 2006-11-26 12:40:54 -0500 (Sun, 26 Nov 2006) $
 * @since 05.07.2006
 */
public class CommonInstanceReferenceModule extends Module {

    public CommonInstanceReferenceModule(DicomObject dcmobj) {
        super(dcmobj);
    }

    public SeriesAndInstanceReference[] getReferencedSeries() {
        return SeriesAndInstanceReference.toSeriesAndInstanceReferences(dcmobj.get(Tag.ReferencedSeriesSequence));
    }

    public void setReferencedSeries(SeriesAndInstanceReference[] seriesRefs) {
        updateSequence(Tag.ReferencedSeriesSequence, seriesRefs);
    }

    public StudyAndSeriesAndInstanceReference[] getStudiesContainingOtherReferencedInstances() {
        return StudyAndSeriesAndInstanceReference.toStudyAndSeriesAndInstanceReferences(dcmobj.get(Tag.StudiesContainingOtherReferencedInstancesSequence));
    }

    public void setStudiesContainingOtherReferencedInstances(StudyAndSeriesAndInstanceReference[] studyRefs) {
        updateSequence(Tag.StudiesContainingOtherReferencedInstancesSequence, studyRefs);
    }
}
