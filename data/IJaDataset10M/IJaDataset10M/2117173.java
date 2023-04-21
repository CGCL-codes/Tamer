package org.dcm4chee.web.war.worklist.modality.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4chee.archive.conf.AttributeFilter;
import org.dcm4chee.archive.entity.MWLItem;
import org.dcm4chee.archive.util.JNDIUtils;
import org.dcm4chee.web.dao.worklist.modality.ModalityWorklistLocal;
import org.dcm4chee.web.war.common.model.AbstractDicomModel;
import org.dcm4chee.web.war.common.model.AbstractEditableDicomModel;

/**
 * @author Robert David <robert.david@agfa.com>
 * @version $Revision: 15929 $ $Date: 2011-09-09 07:09:18 -0400 (Fri, 09 Sep 2011) $
 * @since 20.04.2010
 */
public class MWLItemModel extends AbstractEditableDicomModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private DicomObject patAttrs, spsItem;

    private boolean collapsed;

    public MWLItemModel(MWLItem mwlItem) {
        this.collapsed = true;
        setPk(mwlItem.getPk());
        this.dataset = mwlItem.getAttributes();
        patAttrs = mwlItem.getPatient().getAttributes();
        spsItem = dataset.getNestedDicomObject(Tag.ScheduledProcedureStepSequence);
    }

    public MWLItemModel(DicomObject mwl) {
        this.collapsed = true;
        setPk(-1);
        AttributeFilter filter = AttributeFilter.getExcludePatientAttributeFilter();
        this.dataset = filter.filter(mwl);
        patAttrs = new BasicDicomObject();
        DicomElement e;
        for (Iterator<DicomElement> it = mwl.iterator(); it.hasNext(); ) {
            e = it.next();
            if (filter.hasTag(e.tag())) {
                patAttrs.add(e);
            }
        }
        spsItem = dataset.getNestedDicomObject(Tag.ScheduledProcedureStepSequence);
        if (spsItem == null) {
            String msg = "Missing Scheduled Procedure Step Sequence (0040,0100) in MWL Item!";
            log.error(msg + " Dataset:{}", dataset);
            throw new IllegalArgumentException(msg);
        }
    }

    public String getSPSDescription() {
        String desc = getCodeString(spsItem.get(Tag.ScheduledProtocolCodeSequence));
        if (desc == null) desc = spsItem.getString(Tag.ScheduledProcedureStepDescription);
        return desc;
    }

    public String getPatientName() {
        return patAttrs.getString(Tag.PatientName);
    }

    public Date getBirthDate() {
        return patAttrs.getDate(Tag.PatientBirthDate);
    }

    public DicomObject getPatientAttributes() {
        return patAttrs;
    }

    public String getSPSModality() {
        return spsItem.getString(Tag.Modality);
    }

    public Date getStartDate() {
        try {
            Date d = spsItem.getDate(Tag.ScheduledProcedureStepStartDateTime);
            if (d == null) {
                d = spsItem.getDate(Tag.ScheduledProcedureStepStartDate, Tag.ScheduledProcedureStepStartTime);
            }
            return d;
        } catch (Exception x) {
            log.warn("DicomObject contains wrong value in date attribute!:" + dataset);
            return null;
        }
    }

    public String getSPSID() {
        return spsItem.getString(Tag.ScheduledProcedureStepID);
    }

    public String getRequestedProcedureID() {
        return dataset.getString(Tag.RequestedProcedureID);
    }

    public String getAccessionNumber() {
        return dataset.getString(Tag.AccessionNumber);
    }

    public String getReqProcedureDescription() {
        String desc = getCodeString(dataset.get(Tag.RequestedProcedureCodeSequence));
        if (desc == null) {
            desc = dataset.getString(Tag.RequestedProcedureDescription);
        }
        return desc;
    }

    public String getStationAET() {
        return spsItem.getString(Tag.ScheduledStationAETitle);
    }

    public String getStationName() {
        return spsItem.getString(Tag.ScheduledStationName);
    }

    public String getSPSStatus() {
        return spsItem.getString(Tag.ScheduledProcedureStepStatus);
    }

    @Override
    public void collapse() {
        this.collapsed = true;
    }

    @Override
    public void expand() {
        this.collapsed = false;
    }

    @Override
    public List<? extends AbstractDicomModel> getDicomModelsOfNextLevel() {
        return null;
    }

    @Override
    public int getRowspan() {
        return 0;
    }

    @Override
    public boolean isCollapsed() {
        return this.collapsed;
    }

    @Override
    public int levelOfModel() {
        return NO_LEVEL;
    }

    @Override
    public void update(DicomObject dicomObject) {
        ModalityWorklistLocal dao = (ModalityWorklistLocal) JNDIUtils.lookup(ModalityWorklistLocal.JNDI_NAME);
        dataset = dao.updateMWLItem(getPk(), dicomObject).getAttributes();
    }

    @Override
    public AbstractEditableDicomModel refresh() {
        ModalityWorklistLocal dao = (ModalityWorklistLocal) JNDIUtils.lookup(ModalityWorklistLocal.JNDI_NAME);
        dataset = dao.getMWLItem(getPk()).getAttributes();
        return this;
    }
}
