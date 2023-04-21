package sisi.codicisconto;

import java.util.List;
import javax.persistence.EntityManager;
import org.zkoss.zul.Messagebox;
import sisi.MyEmf;

public class CodiciscontoController {

    private EntityManager getEntityManager() {
        return new MyEmf().getEntityManager();
    }

    @SuppressWarnings("unchecked")
    public Codicisconto[] getCodicisconto(String codsco) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "select c from Codicisconto as c";
            if (codsco != null && !codsco.isEmpty()) {
                cQuery += " WHERE c.codice='" + codsco.trim() + "'";
            }
            cQuery += " ORDER BY c.codice ";
            javax.persistence.Query q = em.createQuery(cQuery);
            Object[] ar1 = q.getResultList().toArray(new Codicisconto[0]);
            return (Codicisconto[]) ar1;
        } finally {
            em.close();
        }
    }

    public int countCodicisconto(String codsco) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "select COUNT(c) from Codicisconto as c";
            if (codsco != null && !codsco.isEmpty()) {
                cQuery += " WHERE c.codice='" + codsco + "'";
            }
            javax.persistence.Query q = em.createQuery(cQuery);
            Long result = ((Long) q.getSingleResult());
            int quantirecords = result.intValue();
            return quantirecords;
        } finally {
            em.close();
        }
    }

    public int countCodiciscontoXRicerca(String codsco) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "select COUNT(c) from Codicisconto as c";
            if (codsco != null && !codsco.isEmpty()) {
                cQuery += " WHERE UPPER(c.codice) LIKE '%" + codsco.trim().toUpperCase() + "%'";
            }
            javax.persistence.Query q = em.createQuery(cQuery);
            Long result = ((Long) q.getSingleResult());
            int quantirecords = result.intValue();
            return quantirecords;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List getLisCodicisconto(String cFiltro, int fromIndex, int toIndex, int qta) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "select c from Codicisconto as c";
            if (cFiltro != null && !cFiltro.isEmpty()) {
                cQuery += " WHERE UPPER(c.codice) LIKE '%" + cFiltro.trim().toUpperCase() + "%'";
            }
            cQuery += " ORDER BY c.codice ";
            javax.persistence.Query q = em.createQuery(cQuery);
            q.setFirstResult(fromIndex);
            if (Integer.valueOf(qta) == null || Integer.valueOf(qta) == 0) {
                q.setMaxResults(toIndex - fromIndex);
            } else {
                q.setMaxResults(qta);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Codicisconto getCodiciscontoXCodice(String codsco) {
        return getCodiciscontoXCodice(codsco, true);
    }

    public Codicisconto getCodiciscontoXCodice(String codCerca, boolean lEcho) {
        Object ar1 = null;
        if (codCerca == null) {
            codCerca = "";
        }
        codCerca = codCerca.toUpperCase().trim();
        EntityManager em = getEntityManager();
        try {
            javax.persistence.Query q = em.createQuery("select c from Codicisconto as c where UPPER(c.codice)='" + codCerca + "'");
            ar1 = q.getSingleResult();
            return (Codicisconto) ar1;
        } catch (Exception e) {
            try {
                if (lEcho) {
                    Messagebox.show("Codice sconto [" + codCerca + "] NON trovato", "Information", Messagebox.OK, Messagebox.INFORMATION);
                }
            } catch (InterruptedException d) {
                d.printStackTrace();
            }
            Codicisconto ar2 = new Codicisconto();
            ar2.setCodice("");
            return ar2;
        } finally {
            em.close();
        }
    }

    public Codicisconto getCodiciscontoxId(int id) {
        EntityManager em = getEntityManager();
        try {
            Codicisconto codiciScontox = em.find(Codicisconto.class, id);
            return codiciScontox;
        } finally {
            em.close();
        }
    }

    public String lastCodicePlus1(int lunghezza) {
        EntityManager em = getEntityManager();
        try {
            javax.persistence.Query q = em.createQuery("select MAX(c.codice) from Codicisconto as c");
            Object risultato = q.getSingleResult();
            String valore;
            if (risultato != null) {
                valore = (q.getSingleResult().toString());
            } else {
                valore = "0";
            }
            if (Integer.valueOf(lunghezza) == null) {
                lunghezza = 5;
            }
            valore = new sisi.articoli.EditArticoli().calcoloNumAutoArt(valore);
            return valore;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("finally")
    public Codicisconto addCodicisconto(Codicisconto codiciSconto) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(codiciSconto);
            em.getTransaction().commit();
            codiciSconto = refreshCodicisconto(codiciSconto);
        } finally {
            em.close();
            return codiciSconto;
        }
    }

    @SuppressWarnings("finally")
    public boolean updateCodicisconto(Codicisconto codiciSconto) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Codicisconto codiciScontox = em.find(Codicisconto.class, codiciSconto.getId());
            codiciScontox.setCodice(codiciSconto.getCodice());
            codiciScontox.setImportoMinimoOrdine(codiciSconto.getImportoMinimoOrdine());
            codiciScontox.setScontoPercentuale(codiciSconto.getScontoPercentuale());
            codiciScontox.setScontoValore(codiciSconto.getScontoValore());
            codiciScontox.setUsatoDa(codiciSconto.getUsatoDa());
            codiciScontox.setUsatoInData(codiciSconto.getUsatoInData());
            codiciScontox.setScadenza(codiciSconto.getScadenza());
            em.getTransaction().commit();
            em.flush();
        } finally {
            em.close();
            return false;
        }
    }

    @SuppressWarnings("finally")
    public Codicisconto refreshCodicisconto(Codicisconto codiciSconto) {
        Codicisconto codiciScontox = codiciSconto;
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            codiciScontox = em.find(Codicisconto.class, codiciSconto.getId());
            em.flush();
            em.refresh(codiciScontox);
        } finally {
            em.close();
            return codiciScontox;
        }
    }

    @SuppressWarnings("finally")
    public boolean removeCodicisconto(Codicisconto codiciSconto) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Codicisconto codiciScontox = em.find(Codicisconto.class, codiciSconto.getId());
            em.remove(codiciScontox);
            em.getTransaction().commit();
        } finally {
            em.close();
            return false;
        }
    }
}
