package org.opennms.web.assets;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * @author brozow
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OperatingSystemConverter implements Converter {

    public OperatingSystem getOperatingSystemById(String idAsString) throws HibernateException {
        Session session = null;
        Transaction tx = null;
        try {
            OperatingSystem operatingSystem = null;
            session = HibernateUtil.currentSession();
            tx = session.beginTransaction();
            Integer id = Integer.valueOf(idAsString);
            operatingSystem = (OperatingSystem) session.load(OperatingSystem.class, id);
            tx.commit();
            HibernateUtil.closeSession();
            return operatingSystem;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) HibernateUtil.closeSession();
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            Object retVal = getOperatingSystemById(value);
            return retVal;
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        OperatingSystem operatingSystem = (OperatingSystem) value;
        String retVal = "" + operatingSystem.getId();
        return retVal;
    }
}
