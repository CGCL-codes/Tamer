package org.leviatan.dataharbour.core.model.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.leviatan.common.xml.XMLIO;
import org.leviatan.dataharbour.core.model.DataEntity;
import org.leviatan.dataharbour.core.model.HarbourRequest;
import org.leviatan.dataharbour.core.persistance.impl.DefaultHibernateController;
import org.w3c.dom.Document;

public class HibernatePersistenceResourceImpl extends PersistenceResourceImpl {

    private SessionFactory hibernateSession;

    private String confStr;

    private Document cfgDoc;

    public String getConfString() {
        return this.confStr;
    }

    public void setConfString(String confString) {
        this.confStr = confString;
    }

    public SessionFactory getSessionFactory() {
        return this.hibernateSession;
    }

    public void applyConfiguration() {
        cfgDoc = XMLIO.getInstance().readXMLFile(this.confStr);
        try {
            hibernateSession = new Configuration().configure(cfgDoc).buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void persist(HarbourRequest harbourRequest) {
        Session session = DefaultHibernateController.getSessionFactory().getCurrentSession();
        Map variableMap = harbourRequest.getDataEntityMap();
        Vector<DataEntityImpl> vars = new Vector<DataEntityImpl>();
        for (Iterator i = variableMap.keySet().iterator(); i.hasNext(); ) {
            String dataName = i.next().toString();
            String dataValue = (String) variableMap.get(dataName);
            DataEntityImpl perDataEnt = new DataEntityImpl();
            perDataEnt.setDataName(dataName);
            perDataEnt.setDataValue(dataValue);
            perDataEnt.setDataType(true);
            vars.add(perDataEnt);
        }
        session.beginTransaction();
        session.save(harbourRequest);
        for (Iterator i = vars.iterator(); i.hasNext(); ) {
            DataEntity perDataEnt = (DataEntity) i.next();
            perDataEnt.setHarbourRequestId(harbourRequest.getId());
            session.save(perDataEnt);
        }
        session.getTransaction().commit();
    }

    public HarbourRequest restoreHarbourRequest(String id) {
        Session session = DefaultHibernateController.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        HarbourRequest req = (HarbourRequest) session.load(HarbourRequestImpl.class, id);
        List res = session.createQuery("from PersistableDataEntityImpl as data where data.harbourRequestId = '" + id + "'").list();
        DataEntity data;
        HashMap<String, DataEntity> variables = new HashMap<String, DataEntity>();
        for (Iterator i = res.iterator(); i.hasNext(); ) {
            data = (DataEntity) i.next();
            variables.put(data.getId(), data);
        }
        req.setDataEntityMap(variables);
        session.getTransaction().commit();
        return req;
    }
}
