package org.dcm4chex.archive.ejb.session;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.dcm4che.data.Dataset;
import org.dcm4che.dict.Tags;
import org.dcm4chex.archive.ejb.interfaces.InstanceLocal;
import org.dcm4chex.archive.ejb.interfaces.InstanceLocalHome;
import org.dcm4chex.archive.ejb.interfaces.PatientLocal;
import org.dcm4chex.archive.ejb.interfaces.SeriesLocal;
import org.dcm4chex.archive.ejb.interfaces.SeriesLocalHome;
import org.dcm4chex.archive.ejb.interfaces.StudyLocal;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Nov 22, 2007
 * 
 * @ejb.bean name="UpdateAttributes" type="Stateless" view-type="remote"
 *           jndi-name="ejb/UpdateAttributes"
 * 
 * @ejb.transaction-type type="Container"
 * @ejb.transaction type="Required"
 * 
 * @ejb.ejb-ref ejb-name="Series" view-type="local" ref-name="ejb/Series"
 * @ejb.ejb-ref ejb-name="Instance" view-type="local" ref-name="ejb/Instance"
 */
public abstract class UpdateAttributesBean implements SessionBean {

    private SeriesLocalHome seriesHome;

    private InstanceLocalHome instHome;

    public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
        Context jndiCtx = null;
        try {
            jndiCtx = new InitialContext();
            seriesHome = (SeriesLocalHome) jndiCtx.lookup("java:comp/env/ejb/Series");
            instHome = (InstanceLocalHome) jndiCtx.lookup("java:comp/env/ejb/Instance");
        } catch (NamingException e) {
            throw new EJBException(e);
        } finally {
            if (jndiCtx != null) {
                try {
                    jndiCtx.close();
                } catch (NamingException ignore) {
                }
            }
        }
    }

    public void unsetSessionContext() {
        seriesHome = null;
        instHome = null;
    }

    /**
     * @ejb.interface-method
     */
    public Collection seriesIuidsForAttributesUpdate(int availability, String modality, String srcAET, Timestamp updatedAfter, Timestamp updatedBefore, int offset, int limit) {
        try {
            return seriesHome.seriesIuidsForAttributesUpdate(availability, modality, srcAET, updatedAfter, updatedBefore, offset, limit);
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
     * @ejb.interface-method
     */
    public int countSeriesForAttributesUpdate(int availability, String modality, String srcAET, Timestamp updatedAfter, Timestamp updatedBefore) {
        try {
            return seriesHome.countSeriesForAttributesUpdate(availability, modality, srcAET, updatedAfter, updatedBefore);
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }

    /**
     * @ejb.interface-method
     */
    public void updateInstanceAttributes(Dataset ds) {
        try {
            InstanceLocal instance = instHome.findBySopIuid(ds.getString(Tags.SOPInstanceUID));
            instance.coerceAttributes(ds, null);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /**
     * @ejb.interface-method
     */
    public void updatePatientStudySeriesAttributes(Dataset ds) {
        try {
            SeriesLocal series = seriesHome.findBySeriesIuid(ds.getString(Tags.SeriesInstanceUID));
            StudyLocal study = series.getStudy();
            PatientLocal patient = study.getPatient();
            series.coerceAttributes(ds, null);
            series.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            study.coerceAttributes(ds, null);
            patient.coerceAttributes(ds, null);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }
}
