package main.controladores;

import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import main.rim.act.DocClinBean;
import main.utils.busqueda.Filtro;

@Local
public interface IBusquedaController {

    public List<DocClinBean> consultarEgresos(List<Filtro> filtros, EntityManager em);
}
