package shellkk.qiq.jdm.engine.transformations.numericalbin;

import org.hibernate.Session;
import shellkk.qiq.jdm.engine.persist.ITransformationSettingsAccessObject;
import shellkk.qiq.jdm.transformations.TransformationSettings;
import shellkk.qiq.jdm.transformations.TransformationSettingsSequenceItem;
import shellkk.qiq.jdm.transformations.TransformationType;
import shellkk.qiq.jdm.transformations.numericalbin.NumericalBinSettings;

public class NumericalBinSettingsAO implements ITransformationSettingsAccessObject {

    public TransformationType getTransformationType() {
        return TransformationType.numericalBinning;
    }

    public void deleteTransformationSettings(TransformationSettingsSequenceItem seqItem, Session session) throws Exception {
        String className = NumericalBinSettings.class.getName();
        Long seqId = seqItem.getId();
        String hql = "from " + className + " as m where m.sequenceItem.id=:seqId";
        NumericalBinSettings obj = (NumericalBinSettings) session.createQuery(hql).setParameter("seqId", seqId).uniqueResult();
        if (obj != null) {
            session.delete(obj);
        }
    }

    public TransformationSettings loadTransformationSettings(TransformationSettingsSequenceItem seqItem, Session session) throws Exception {
        String className = NumericalBinSettings.class.getName();
        Long seqId = seqItem.getId();
        String hql = "from " + className + " as m where m.sequenceItem.id=:seqId";
        NumericalBinSettings obj = (NumericalBinSettings) session.createQuery(hql).setParameter("seqId", seqId).uniqueResult();
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
