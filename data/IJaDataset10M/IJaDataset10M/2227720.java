package com.training.hibernate.test.mapping;

import com.training.hibernate.mapping.associations.Country;
import com.training.hibernate.mapping.associations.HeadOfState;
import com.training.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 189833
 *
 */
public class ManyToManyMappingTest {

    private static final Logger log = LoggerFactory.getLogger(ManyToManyMappingTest.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Country greece = (Country) session.load(Country.class, 73);
        log.info(greece.getName() + " is in " + greece.getContinent().getName() + " and " + greece.getHeadOfState().getName() + " is the head of state");
        log.info(greece.getName() + " has been visited by ");
        for (HeadOfState hos : greece.getVisitors()) {
            log.info("  " + hos.getName() + " of " + hos.getCountry().getName());
        }
        HeadOfState saakashvili = (HeadOfState) session.load(HeadOfState.class, 1);
        Country gambia = (Country) session.load(Country.class, 147);
        saakashvili.addVisit(gambia);
        saakashvili = (HeadOfState) session.load(HeadOfState.class, 1);
        log.info(saakashvili.getName() + " is head of state for " + saakashvili.getCountry().getName() + " and has visited");
        for (Country country : saakashvili.getVisited()) {
            log.info("  " + country.getName() + " in " + country.getContinent().getName());
        }
        session.getTransaction().commit();
        HibernateUtil.shutdown();
    }
}
