package jlokg.mail;

import fr.nhb.Utilities;
import java.util.Date;
import jlokg.engine.JLoKGEngine;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *  @netbeans.hibernate.facade beanClass=jlokg.mail.LKGLocRappel
 */
public class LKGLocRappelFacade {

    public void saveLKGLocRappel(LKGLocRappel lKGLocRappel) {
        Session session = jlokg.JLoKGHibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        if (lKGLocRappel.getCode().equals("")) {
            lKGLocRappel.setCode(JLoKGEngine.getJLoKGEngine().getKeyFor(LKGLocRappel.class.getName()));
        }
        session.save(lKGLocRappel);
        tx.commit();
        jlokg.JLoKGHibernateUtil.closeSession();
    }

    public void saveOrUpdateLKGAccountLine(LKGLocRappel lKGLocRappel) {
        Session session = jlokg.JLoKGHibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(lKGLocRappel);
        tx.commit();
        jlokg.JLoKGHibernateUtil.closeSession();
    }

    public String getSQLForLodger(String pLodgerCode) {
        String result = "select code," + " dateRelance," + " title" + " from locrappel " + " where locrappel.lodger ='" + pLodgerCode + "'" + " order by dateRelance desc";
        return result;
    }

    public String getSQLForMainControlPanel() {
        String result = "select  distinct(dateRelance) as Date " + " from locrappel " + " order by dateRelance desc";
        return result;
    }

    public String getSQLForRappelForDate(String pDate) {
        String result = "select  locarappel.code," + " lodger.reference ," + " concat(lodger.firstname, ' ' , lodger.lastname) " + " from locarappel, lodger " + " where locarappel.lodger = lodger.code " + " and locrappel.dateRelance = '" + pDate + "'";
        return result;
    }

    public String getSQLForRappelForDate(Date pDate) {
        String strDate = Utilities.getSQLDate(pDate);
        return this.getSQLForRappelForDate(strDate);
    }
}
