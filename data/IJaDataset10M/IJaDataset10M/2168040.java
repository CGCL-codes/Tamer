package controller.converter;

import java.util.Collection;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import model.dao.EtapaDao;
import model.dao.hibernate.HibernateDAO;
import model.valueObject.EtapaVO;
import org.hibernate.criterion.Restrictions;

public class EtapasConverter implements Converter {

    private HibernateDAO etapaDao = new EtapaDao();

    @Override
    public Object getAsObject(FacesContext context, UIComponent uiComponent, String texto) {
        if (texto == null) return null;
        Collection<EtapaVO> listaEtapas = etapaDao.selectBy(Restrictions.eq("nome", texto));
        Iterator<EtapaVO> it = listaEtapas.iterator();
        return it.next();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent uiComponent, Object objeto) {
        if (objeto == null) return new String();
        EtapaVO etapa = (EtapaVO) objeto;
        return etapa.getNome();
    }
}
