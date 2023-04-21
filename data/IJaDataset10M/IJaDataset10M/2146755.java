package ar.com.larreta.procesos.pasos;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ar.com.larreta.procesos.Entrada;
import ar.com.larreta.procesos.PasoConDAO;
import com.extjs.gxt.ui.client.data.ModelData;

public class CriteriaRegion extends PasoConDAO {

    public static final String REGION_PADRE = "regionPadre";

    public static final String TIPO_REGION = "tipoRegion";

    @Override
    protected void accion() {
        Entrada entradaCriteriaLista = entradas.get(CRITERIA_LISTA);
        Entrada entradaCriteriaContar = entradas.get(CRITERIA_CONTAR);
        agregarCriteriosARegion((Criteria) entradaCriteriaLista.getValor());
        agregarCriteriosARegion((Criteria) entradaCriteriaContar.getValor());
        salidas.put(CRITERIA_LISTA, entradaCriteriaLista.getValor());
        salidas.put(CRITERIA_CONTAR, entradaCriteriaContar.getValor());
    }

    public void agregarCriteriosARegion(Criteria criteria) {
        if (criteria != null) {
            Entrada entradaRegionPadre = entradas.get(REGION_PADRE);
            Entrada entradaTipoRegion = entradas.get(TIPO_REGION);
            criteria.add(Restrictions.eq("tipoRegion.descripcion", ((ModelData) entradaTipoRegion.getValor()).get("descripcion")));
            if ((entradaRegionPadre != null) && (entradaRegionPadre.getValor() != null)) {
                criteria.add(Restrictions.eq("padre.descripcion", ((ModelData) entradaRegionPadre.getValor()).get("descripcion")));
            }
        }
    }
}
