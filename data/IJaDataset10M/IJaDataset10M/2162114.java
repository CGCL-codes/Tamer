package at.neckam.rezepte.service;

import at.neckam.rezepte.shared.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class KategorieService {

    public List listAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List kategorien = session.createQuery("from Kategorie k order by k.spalte").list();
        tx.commit();
        return kategorien;
    }
}
