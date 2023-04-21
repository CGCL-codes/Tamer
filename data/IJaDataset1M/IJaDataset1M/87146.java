package sisi.marche;

import java.util.List;
import javax.persistence.EntityManager;
import org.zkoss.zul.Messagebox;
import sisi.MyEmf;

public class MarcheController {

    private EntityManager getEntityManager() {
        return new MyEmf().getEntityManager();
    }

    @SuppressWarnings("unchecked")
    public Marche[] getMarche(String cFiltro) {
        EntityManager em = getEntityManager();
        try {
            if (cFiltro == null) {
                javax.persistence.Query q = em.createQuery("select c from Marche as c order by c.mades");
                Object[] ar1 = q.getResultList().toArray(new Marche[0]);
                return (Marche[]) ar1;
            } else {
                javax.persistence.Query q = em.createQuery("select c from Marche as c WHERE c.mades LIKE '%" + cFiltro.trim() + "%' OR c.codmar LIKE '%" + cFiltro.trim() + "%' order by c.mades");
                Object[] ar1 = q.getResultList().toArray(new Marche[0]);
                return (Marche[]) ar1;
            }
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List getListMarche(String cFiltro) {
        EntityManager em = getEntityManager();
        try {
            if (cFiltro == null || cFiltro.isEmpty()) {
                javax.persistence.Query q = em.createQuery("select c from Marche as c order by c.mades");
                return (q.getResultList());
            } else {
                javax.persistence.Query q = em.createQuery("select c from Marche as c WHERE UPPER(c.mades) LIKE '%" + cFiltro.trim().toUpperCase() + "%' order by c.mades");
                return (q.getResultList());
            }
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List getListMarche2() {
        EntityManager em = getEntityManager();
        try {
            javax.persistence.Query q = em.createQuery("select c.mades from Marche as c order by c.mades");
            return (q.getResultList());
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List getListMarXCodice(String codMarCerca) {
        codMarCerca = codMarCerca.toUpperCase().trim();
        EntityManager em = getEntityManager();
        try {
            javax.persistence.Query q = em.createQuery("select c from Marche as c where UPPER(c.codmar)='" + codMarCerca + "'");
            return (q.getResultList());
        } finally {
            em.close();
        }
    }

    public Marche getMarcaXCodice(String codCerca, boolean lEcho) {
        Object ar1 = null;
        codCerca = codCerca.toUpperCase().trim();
        EntityManager em = getEntityManager();
        try {
            javax.persistence.Query q = em.createQuery("select c from Marche as c where TRIM(c.codmar)='" + codCerca + "'");
            ar1 = q.getSingleResult();
            return (Marche) ar1;
        } catch (Exception e) {
            if (lEcho) {
                try {
                    Messagebox.show("Marca [" + codCerca + "] NON trovata", "Information", Messagebox.OK, Messagebox.INFORMATION);
                } catch (InterruptedException d) {
                    d.printStackTrace();
                }
            }
            Marche ar2 = new Marche();
            ar2.setCodmar("");
            return ar2;
        } finally {
            em.close();
        }
    }

    public String lastCodicePlus1() {
        EntityManager em = getEntityManager();
        try {
            javax.persistence.Query q = em.createQuery("select MAX(c.codmar) from Marche as c");
            Object risultato = q.getSingleResult();
            String valore;
            if (risultato != null) {
                valore = (q.getSingleResult().toString());
            } else {
                valore = "0";
            }
            int valore2 = Integer.parseInt(valore) + 1;
            valore = Integer.toString(valore2);
            valore = sisi.General.paddingString(valore.trim(), 5, "0".charAt(0), true);
            return valore;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("finally")
    public Marche addMarche(Marche marca) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(marca);
            em.getTransaction().commit();
            marca = refreshMarche(marca);
        } finally {
            em.close();
            return marca;
        }
    }

    @SuppressWarnings("finally")
    public boolean updateMarche(Marche marca) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Marche marcax = em.find(Marche.class, marca.getMaid());
            marcax.setMades(marca.getMades());
            em.getTransaction().commit();
            em.flush();
        } finally {
            em.close();
            return false;
        }
    }

    @SuppressWarnings("finally")
    public Marche refreshMarche(Marche marca) {
        Marche marcax = marca;
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            marcax = em.find(Marche.class, marca.getMaid());
            em.flush();
            em.refresh(marcax);
        } finally {
            em.close();
            return marcax;
        }
    }

    @SuppressWarnings("finally")
    public boolean removeMarche(Marche marca) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Marche marcax = em.find(Marche.class, marca.getMaid());
            em.remove(marcax);
            em.getTransaction().commit();
        } finally {
            em.close();
            return false;
        }
    }
}
