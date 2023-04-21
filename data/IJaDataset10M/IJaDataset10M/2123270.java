package org.verus.ngl.sl.bprocess.administration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.verus.ngl.sl.utilities.Connections;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.sl.objectmodel.administration.GENERAL_SETUP_PMT;
import org.verus.ngl.utilities.NGLUtility;

/**
 *
 * @author root
 */
public class General_setup_pmtImpl implements General_setup_pmt {

    NGLUtility utility;

    public General_setup_pmtImpl() {
        utility = NGLUtility.getInstance();
    }

    @Override
    public Integer getRenewDaysOfthisLibrary(Integer libraryId, String databaseId) {
        int renewdays = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("GENERAL_SETUP_PMT.findbyLibraryId");
            query.setParameter("libraryId", libraryId);
            Timestamp cdate = new Timestamp(utility.getDateWithoutTime(Calendar.getInstance().getTimeInMillis()));
            List<GENERAL_SETUP_PMT> list = query.list();
            if (list != null && list.size() > 0) {
                GENERAL_SETUP_PMT temp = null;
                Timestamp temp_date = null;
                for (int j = 0; j < list.size(); j++) {
                    GENERAL_SETUP_PMT general_setup_pmt = (GENERAL_SETUP_PMT) list.get(j);
                    Timestamp wef = (Timestamp) general_setup_pmt.getGENERAL_SETUP_PMTPK().getWef();
                    if (cdate.compareTo(wef) < 0 || cdate.compareTo(wef) == 0) {
                        if (temp_date != null) {
                            if (temp_date.compareTo(wef) > 0) {
                                temp_date = wef;
                                temp = (GENERAL_SETUP_PMT) list.get(j);
                            }
                        } else {
                            temp_date = wef;
                            temp = (GENERAL_SETUP_PMT) list.get(j);
                        }
                    }
                }
                if (temp != null) {
                    renewdays = temp.getRenewDays();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return renewdays;
    }

    @Override
    public Integer bm_getReservationValidityDays(Integer libraryId, Timestamp wef, String databaseId) {
        Integer reservationValidityDays = -1;
        List<GENERAL_SETUP_PMT> list = new ArrayList<GENERAL_SETUP_PMT>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("GENERAL_SETUP_PMT.findbyLibraryIdWef");
            query.setParameter("libraryId", libraryId);
            query.setParameter("wef", wef);
            list = query.list();
            if (list != null && list.size() > 0) {
                long max = 0;
                for (int i = 0; i < list.size(); i++) {
                    GENERAL_SETUP_PMT general_setup_pmt = list.get(i);
                    long time = general_setup_pmt.getGENERAL_SETUP_PMTPK().getWef().getTime();
                    if (time > max) {
                        max = time;
                        reservationValidityDays = general_setup_pmt.getReservationValidityDays();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservationValidityDays;
    }

    @Override
    public Integer bm_getRenewalDays(Integer libraryId, Timestamp wef, String databaseId) {
        Integer renewalDays = -1;
        List<GENERAL_SETUP_PMT> list = new ArrayList<GENERAL_SETUP_PMT>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("GENERAL_SETUP_PMT.findbyLibraryIdWef");
            query.setParameter("libraryId", libraryId);
            query.setParameter("wef", wef);
            list = query.list();
            if (list != null && list.size() > 0) {
                long max = 0;
                for (int i = 0; i < list.size(); i++) {
                    GENERAL_SETUP_PMT general_setup_pmt = list.get(i);
                    long time = general_setup_pmt.getGENERAL_SETUP_PMTPK().getWef().getTime();
                    if (time > max) {
                        max = time;
                        renewalDays = general_setup_pmt.getRenewDays();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return renewalDays;
    }
}
