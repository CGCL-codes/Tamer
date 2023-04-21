package shellkk.qiq.jdm.engine.transformations.outliertreatment;

import org.hibernate.Session;
import shellkk.qiq.jdm.engine.persist.ITransformationSettingsAccessObject;
import shellkk.qiq.jdm.transformations.TransformationSettings;
import shellkk.qiq.jdm.transformations.TransformationSettingsSequenceItem;
import shellkk.qiq.jdm.transformations.TransformationType;
import shellkk.qiq.jdm.transformations.outliertreatment.OutlierTreatmentSettings;

public class OutlierTreatmentSettingsAO implements ITransformationSettingsAccessObject {

    public TransformationType getTransformationType() {
        return TransformationType.outlierTreatment;
    }

    public void deleteTransformationSettings(TransformationSettingsSequenceItem seqItem, Session session) throws Exception {
        String className = OutlierTreatmentSettings.class.getName();
        Long seqId = seqItem.getId();
        String hql = "from " + className + " as m where m.sequenceItem.id=:seqId";
        OutlierTreatmentSettings obj = (OutlierTreatmentSettings) session.createQuery(hql).setParameter("seqId", seqId).uniqueResult();
        if (obj != null) {
            session.delete(obj);
        }
    }

    public TransformationSettings loadTransformationSettings(TransformationSettingsSequenceItem seqItem, Session session) throws Exception {
        String className = OutlierTreatmentSettings.class.getName();
        Long seqId = seqItem.getId();
        String hql = "from " + className + " as m where m.sequenceItem.id=:seqId";
        OutlierTreatmentSettings obj = (OutlierTreatmentSettings) session.createQuery(hql).setParameter("seqId", seqId).uniqueResult();
        if (obj == null) {
            throw new Exception("transformationSettings of sequenceItem[id=" + seqItem.getId() + "] not found!");
        }
        obj.getAttrSettings().size();
        return obj;
    }

    public void saveOrUpdateTransformationSettings(TransformationSettingsSequenceItem seqItem, Session session) throws Exception {
        session.saveOrUpdate(seqItem.getTransSettings());
    }
}
