package main.actions;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import main.rim.entity.PlaceBean;
import main.utils.Converters.SalaConverter;
import org.jboss.annotation.ejb.cache.Cache;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;

@Stateful
@Name("darSalas")
@Cache(org.jboss.ejb3.cache.NoPassivationCache.class)
public class DarSalasAction implements IDarSalas {

    @Logger
    private Log log;

    @PersistenceContext
    private EntityManager em;

    @Out(required = false)
    List<PlaceBean> listsalas;

    Map<String, PlaceBean> salasMap;

    @SuppressWarnings("unchecked")
    @Create
    public void nuevo() {
        log.info("DarSalasAction NUEVO");
        listsalas = (List<PlaceBean>) em.createQuery("select s from PlaceBean s").getResultList();
        Map<String, PlaceBean> results = new TreeMap<String, PlaceBean>();
        for (PlaceBean s : listsalas) {
            String nom = s.getName().toString();
            results.put(nom, s);
        }
        salasMap = results;
    }

    public Map<String, PlaceBean> getSala() {
        return salasMap;
    }

    public Converter getConverterSala() {
        return new SalaConverter(listsalas);
    }

    @SuppressWarnings("unchecked")
    public List<PlaceBean> autoCompletarSalas(Object event) {
        String pref = "%" + event.toString();
        Query q = em.createQuery("select s from PlaceBean s where s.name like :filtro order by s.name").setParameter("filtro", pref.concat("%"));
        listsalas = q.getResultList();
        return listsalas;
    }

    @Destroy
    @Remove
    public void destroy() {
        log.info("DarSalasAction ELIMINADO");
    }

    public void setSala(List<PlaceBean> listsalas) {
        this.listsalas = listsalas;
    }
}
