package sisi.movimenti;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sisi.MyEmf;
import sisi.movimenti.PrimanotaePagamenti;

public class PrimanotaController {

    private EntityManagerFactory emf;

    private EntityManager getEntityManager() {
        emf = new MyEmf().getEmf();
        return emf.createEntityManager();
    }

    @SuppressWarnings("unchecked")
    public Primanota[] getPrimanota(Date inizio, Date fine, String gruppo, String subgruppo) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "select c from Primanota as c " + " WHERE c.data>='" + inizio + "' AND c.data<='" + fine + "' ";
            if (gruppo != null && !gruppo.isEmpty()) {
                cQuery += " AND c.gruppo='" + gruppo + "' ";
            }
            if (subgruppo != null && !subgruppo.isEmpty()) {
                cQuery += " AND c.subgruppo='" + subgruppo + "' ";
            }
            cQuery += " ORDER BY c.id";
            javax.persistence.Query q = em.createQuery(cQuery);
            Object[] ar1 = q.getResultList().toArray(new Primanota[0]);
            return (Primanota[]) ar1;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List getListPrimanota() {
        EntityManager em = getEntityManager();
        try {
            javax.persistence.Query q = em.createQuery("select c from Primanota as c order by c.id");
            List ar1 = q.getResultList();
            return ar1;
        } finally {
            em.close();
        }
    }

    public Primanota getPrimanotaxId(int id) {
        EntityManager em = getEntityManager();
        try {
            Primanota pn = em.find(Primanota.class, id);
            return pn;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<PrimanotaePagamenti> getPrimanotaListView(int fromIndex, int qta) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "SELECT * FROM primanota ";
            cQuery += " order by id";
            javax.persistence.Query q = em.createNativeQuery(cQuery);
            q.setFirstResult(fromIndex);
            q.setMaxResults(qta);
            List<PrimanotaePagamenti> val = q.getResultList();
            return val;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List getPrimanotaPagListView(Date inizio, Date fine, String gruppo, String subgruppo) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "SELECT * FROM pagamentinominativi ";
            cQuery += " WHERE tddoc >= '" + inizio + "'  AND tddoc <= '" + fine + "' ";
            if (gruppo != null && !gruppo.isEmpty()) {
                cQuery += " AND gruppo='" + gruppo + "' ";
            }
            if (subgruppo != null && !subgruppo.isEmpty()) {
                cQuery += " AND subgruppo='" + subgruppo + "' ";
            }
            cQuery += " order by tddoc ";
            javax.persistence.Query q = em.createNativeQuery(cQuery);
            List val = q.getResultList();
            return val;
        } finally {
            em.close();
        }
    }

    /** 
	 * Calcola il totale di pagamenti fino a una data.
	 * 
	 * @author Pedro
	 * data fine = limite (si calcola fino a la data fine)
	 * contro = 2 --> clienti, contro = 3 --> fornitori
	 * tipo=0 --> cassa, tipo=1 --> banca
	 */
    public BigDecimal getTotPag(Date fine, int contro, int tipo, String gruppo, String subgruppo) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "SELECT SUM(importo) AS importo FROM pagamentinominativi ";
            cQuery += " WHERE tddoc < '" + fine + "' ";
            if (gruppo != null && !gruppo.isEmpty()) {
                cQuery += " AND gruppo='" + gruppo + "' ";
            }
            if (subgruppo != null && !subgruppo.isEmpty()) {
                cQuery += " AND subgruppo='" + subgruppo + "' ";
            }
            cQuery += " AND tmcontro='" + contro + "' AND movimenta=" + tipo;
            javax.persistence.Query q = em.createNativeQuery(cQuery);
            BigDecimal somma = (BigDecimal) q.getSingleResult();
            somma = (somma == null ? BigDecimal.ZERO : somma);
            return somma;
        } finally {
            em.close();
        }
    }

    /** 
	 * Calcola il totale di cassa/banca entrate/uscite ( fino a una data )
	 * 
	 * @author Pedro
	 * data fine = limite (si calcola fino a la data fine)
	 * campo --> cassa_entrata, cassa_uscita, banca_entrata, banca_uscita
	 */
    public BigDecimal getTotCassaBanca(Date fine, String campo, String gruppo, String subgruppo) {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "SELECT SUM(" + campo + ") AS importo FROM primanota ";
            cQuery += " WHERE data < '" + fine + "' ";
            if (gruppo != null && !gruppo.isEmpty()) {
                cQuery += " AND gruppo='" + gruppo + "' ";
            }
            if (subgruppo != null && !subgruppo.isEmpty()) {
                cQuery += " AND subgruppo='" + subgruppo + "' ";
            }
            javax.persistence.Query q = em.createNativeQuery(cQuery);
            BigDecimal somma = (BigDecimal) q.getSingleResult();
            somma = (somma == null ? BigDecimal.ZERO : somma);
            return somma;
        } finally {
            em.close();
        }
    }

    public int getPrimanotaCount() {
        EntityManager em = getEntityManager();
        try {
            String cQuery = "select count(*) from primanota";
            javax.persistence.Query q = em.createNativeQuery(cQuery);
            Long result = ((Long) q.getSingleResult());
            int quantirecords = result.intValue();
            return quantirecords;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("finally")
    public Primanota add(Primanota pn) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pn);
            em.getTransaction().commit();
            pn = refreshPrimanota(pn);
        } finally {
            em.close();
            return pn;
        }
    }

    @SuppressWarnings("finally")
    public Primanota updatePrimanota(Primanota pn) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Primanota pnx = em.find(Primanota.class, pn.getId());
            pnx.setBancaEntrata(pn.getBancaEntrata());
            pnx.setBancaUscita(pn.getBancaUscita());
            pnx.setCassaEntrata(pn.getCassaEntrata());
            pnx.setCassaUscita(pn.getCassaUscita());
            pnx.setData(pn.getData());
            pnx.setDescrizione(pn.getDescrizione());
            pnx.setGruppo(pn.getGruppo());
            pnx.setCodoperazione(pn.getCodoperazione());
            pnx.setSubgruppo(pn.getSubgruppo());
            em.getTransaction().commit();
            em.flush();
        } finally {
            em.close();
            return (pn = refreshPrimanota(pn));
        }
    }

    @SuppressWarnings("finally")
    public Primanota refreshPrimanota(Primanota pn) {
        Primanota pnx = pn;
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            pnx = em.find(Primanota.class, pn.getId());
            em.flush();
            em.refresh(pnx);
        } finally {
            em.close();
            return pnx;
        }
    }

    @SuppressWarnings("finally")
    public boolean removePrimanota(Primanota pn) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Primanota pnx = em.find(Primanota.class, pn.getId());
            em.remove(pnx);
            em.getTransaction().commit();
        } finally {
            em.close();
            return false;
        }
    }
}
