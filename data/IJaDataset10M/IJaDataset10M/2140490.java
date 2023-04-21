package org.opensih.gdq.Actions.Tablas;

import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.opensih.gdq.Modelo.SaqAsse;

@Stateful
@Name("CodigosSAQ_ASSE")
public class TablaSAQ_ASSEAction implements ITablaSAQ_ASSE {

    @PersistenceContext
    private EntityManager em;

    List<SaqAsse> listSAQ_ASSE;

    String filterDesc = "";

    String filterDesc2 = "";

    String filterCod = "";

    String filteryo;

    String item = "0";

    @SuppressWarnings("unchecked")
    @Create
    @Begin(join = true)
    public void create() {
        listSAQ_ASSE = em.createQuery("from SaqAsse").setMaxResults(70).getResultList();
    }

    @SuppressWarnings("unchecked")
    public void actTabla() {
        String fc = "'" + filterCod + "%'";
        String fd = "'%" + filterDesc + "%'";
        String fd2 = "'%" + filterDesc2 + "%'";
        String citem = item;
        if (item.compareTo("0") != 0) {
            if (filteryo.compareTo("y") == 0) listSAQ_ASSE = em.createQuery("from SaqAsse c where c.cod like " + fc + " and substring(c.cod,1,3) = " + citem + " and c.descrip like " + fd + " and c.descrip like " + fd2).getResultList(); else if (filterDesc.compareTo("") == 0) listSAQ_ASSE = em.createQuery("from SaqAsse c where c.cod like " + fc + " and substring(c.cod,1,3) = " + citem + " and c.descrip like " + fd2).getResultList(); else if (filterDesc2.compareTo("") == 0) listSAQ_ASSE = em.createQuery("from SaqAsse c where c.cod like " + fc + " and substring(c.cod,1,3) = " + citem + " and c.descrip like " + fd).getResultList(); else listSAQ_ASSE = em.createQuery("from SaqAsse c where c.cod like " + fc + " and substring(c.cod,1,3) = " + citem + " and (c.descrip like " + fd + " or c.descrip like " + fd2 + ")").getResultList();
        } else {
            if (filteryo.compareTo("y") == 0) listSAQ_ASSE = em.createQuery("from SaqAsse c where c.cod like " + fc + " and c.descrip like " + fd + " and c.descrip like " + fd2).getResultList(); else if (filterDesc.compareTo("") == 0) listSAQ_ASSE = em.createQuery("from SaqAsse c where c.cod like " + fc + " and c.descrip like " + fd2).getResultList(); else if (filterDesc2.compareTo("") == 0) listSAQ_ASSE = em.createQuery("from SaqAsse c where c.cod like " + fc + " and c.descrip like " + fd).getResultList(); else listSAQ_ASSE = em.createQuery("from SaqAsse c where c.cod like " + fc + " and (c.descrip like " + fd + " or c.descrip like " + fd2 + ")").getResultList();
        }
    }

    @SuppressWarnings("unchecked")
    public void cargaItem() {
        String citem = item;
        if (item.compareTo("0") != 0) listSAQ_ASSE = em.createQuery("from SaqAsse c where substring(c.cod,1,3) = " + citem).getResultList(); else listSAQ_ASSE = em.createQuery("from SaqAsse").getResultList();
    }

    @Destroy
    @Remove
    public void destroy() {
    }

    public List<SaqAsse> getListSAQ_ASSE() {
        return listSAQ_ASSE;
    }

    public void setListSAQ_ASSE(List<SaqAsse> listSAQ_ASSE) {
        this.listSAQ_ASSE = listSAQ_ASSE;
    }

    public String getfilterDesc() {
        return filterDesc;
    }

    public void setfilterDesc(String filterDesc) {
        this.filterDesc = filterDesc;
    }

    public String getfilterDesc2() {
        return filterDesc2;
    }

    public void setfilterDesc2(String filterDesc2) {
        this.filterDesc2 = filterDesc2;
    }

    public String getfilterCod() {
        return filterCod;
    }

    public void setfilterCod(String filterCod) {
        this.filterCod = filterCod;
    }

    public String getFilteryo() {
        return filteryo;
    }

    public void setFilteryo(String filteryo) {
        this.filteryo = filteryo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
