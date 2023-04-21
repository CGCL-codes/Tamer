package com.beimin.evedata.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.beimin.evedata.model.sta.StaStation;
import com.beimin.evedata.repository.StaRepository;

public class HibStaRepository implements StaRepository {

    protected final Logger logger = Logger.getLogger(getClass());

    private final SessionFactory sf;

    public HibStaRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public StaStation getStaStation(int stationID) {
        Session session = sf.getCurrentSession();
        try {
            return (StaStation) session.get(StaStation.class, stationID);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        }
    }
}
