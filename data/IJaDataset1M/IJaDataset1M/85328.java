package sisi.operazioni;

import java.util.List;
import javax.persistence.EntityManager;
import org.zkoss.zk.ui.Executions;
import sisi.MyEmf;

public class OperazioniController {

    private EntityManager getEntityManager() {
        return new MyEmf().getEntityManager();
    }

    @SuppressWarnings("unchecked")
    public Operazioni[] getOperazioni(String cFiltro) {
        EntityManager em = getEntityManager();
        try {
            if (cFiltro == null) {
                javax.persistence.Query q = em.createQuery("select c from Operazioni as c order by c.dataora");
                Object[] ar1 = q.getResultList().toArray(new Operazioni[0]);
                return (Operazioni[]) ar1;
            } else {
                javax.persistence.Query q = em.createQuery("select c from Categ as c WHERE c.descrizione LIKE '%" + cFiltro.trim() + "%' OR c.codcat LIKE '%" + cFiltro.trim() + "%' order by c.dataora");
                Object[] ar1 = q.getResultList().toArray(new Operazioni[0]);
                return (Operazioni[]) ar1;
            }
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List getListOperazioni(String cFiltro) {
        EntityManager em = getEntityManager();
        try {
            if (cFiltro == null) {
                javax.persistence.Query q = em.createQuery("select c from Operazioni as c order by c.dataora DESC");
                return (q.getResultList());
            } else {
                javax.persistence.Query q = em.createQuery("select c from Operazioni as c WHERE UPPER(c.descrizione) LIKE '%" + cFiltro.trim().toUpperCase() + "%' order by c.dataora DESC");
                return (q.getResultList());
            }
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("finally")
    public boolean addOperazioni(Operazioni operazione) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(operazione);
            em.getTransaction().commit();
        } finally {
            em.close();
            return false;
        }
    }

    @SuppressWarnings("finally")
    public boolean updateOperazioni(Operazioni operazione) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Operazioni operazionex = em.find(Operazioni.class, operazione.getId());
            operazionex.setDataora(operazione.getDataora());
            em.getTransaction().commit();
            em.flush();
        } finally {
            em.close();
            return false;
        }
    }

    @SuppressWarnings("finally")
    public boolean removeOperazioni(Operazioni operazione) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Operazioni operazionex = em.find(Operazioni.class, operazione.getId());
            em.remove(operazionex);
            em.getTransaction().commit();
        } finally {
            em.close();
            return false;
        }
    }

    public boolean nuovaOperazione(String descrizione, String tabella) {
        String cIPClient = Executions.getCurrent().getRemoteAddr();
        Operazioni operazione = new Operazioni();
        operazione.setDescrizione(descrizione);
        operazione.setTabella(tabella);
        operazione.setIpclient(cIPClient);
        java.util.Date today = new java.util.Date();
        java.sql.Timestamp now = new java.sql.Timestamp(today.getTime());
        operazione.setDataora(now);
        addOperazioni(operazione);
        return true;
    }
}
