package it.gestioneimmobili.process;

import it.gestioneimmobili.hibernate.HibernateSessionFactory;
import it.gestioneimmobili.hibernate.bean.AnagImm;
import it.gestioneimmobili.hibernate.bean.AnagVisionaImmobile;
import it.gestioneimmobili.hibernate.bean.Anagrafica;
import it.gestioneimmobili.hibernate.bean.view.AnagraficaView;
import it.gestioneimmobili.hibernate.dao.AnagraficaDAO;
import it.gestioneimmobili.util.Util;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProcessAnagrafica {

    private static Logger log = Logger.getLogger(ProcessAnagrafica.class);

    public void insert(Anagrafica an) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = HibernateSessionFactory.getSession();
            tx = sess.beginTransaction();
            log.info("Insert anagrafica start...");
            AnagraficaDAO dao = new AnagraficaDAO();
            dao.save(an);
            tx.commit();
            log.info("Insert anagrafica commited...");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    public void update(Anagrafica an) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = HibernateSessionFactory.getSession();
            tx = sess.beginTransaction();
            log.info("Update anagrafica start...");
            AnagraficaDAO dao = new AnagraficaDAO();
            dao.update(an);
            tx.commit();
            log.info("Update anagrafica commited...");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    public void delete(Anagrafica an) throws Exception {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = HibernateSessionFactory.getSession();
            tx = sess.beginTransaction();
            log.info("Delete anagrafica start...");
            AnagraficaDAO dao = new AnagraficaDAO();
            dao.delete(an);
            tx.commit();
            log.info("Delete anagrafica commited...");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    public List<AnagraficaView> searchView() {
        Session sess = null;
        sess = HibernateSessionFactory.getSession();
        AnagraficaDAO an = new AnagraficaDAO();
        List<AnagraficaView> l;
        l = an.searchFirst();
        sess.close();
        return l;
    }

    public List<AnagraficaView> searchView(Anagrafica ana) {
        Session sess = null;
        sess = HibernateSessionFactory.getSession();
        AnagraficaDAO an = new AnagraficaDAO();
        List<AnagraficaView> l;
        l = an.searchFirst(ana);
        if (Util.isEmptyCollection(l)) {
            l = new LinkedList<AnagraficaView>();
            AnagraficaView ww = new AnagraficaView();
            ww.setNome("Nessun Elemento Trovato");
            l.add(ww);
        }
        sess.close();
        return l;
    }

    public Anagrafica selectPk(Integer id) {
        Session sess = null;
        sess = HibernateSessionFactory.getSession();
        AnagraficaDAO an = new AnagraficaDAO();
        Anagrafica sel = an.findById(id);
        sess.close();
        return sel;
    }

    public Set<AnagImm> searchImmobili(Integer id) {
        Session sess = null;
        sess = HibernateSessionFactory.getSession();
        AnagraficaDAO an = new AnagraficaDAO();
        Anagrafica anBean = an.findById(id);
        Set<AnagImm> anagImmList = anBean.getAnagImms();
        sess.close();
        return anagImmList;
    }

    public Set<AnagVisionaImmobile> searchImmobiliVisionati(Integer id) {
        Session sess = null;
        sess = HibernateSessionFactory.getSession();
        AnagraficaDAO an = new AnagraficaDAO();
        Anagrafica anBean = an.findById(id);
        Set<AnagVisionaImmobile> anagImmList = anBean.getAnagVisionaImmobiles();
        sess.close();
        return anagImmList;
    }
}
