package Persistencia.intermediarios;

import Persistencia.ExpertosPersistencia.Criterio;
import Persistencia.Entidades.ObjetoPersistente;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public class IntermediarioPersistenciaPesoDeReclamo extends IntermediarioRelacional {

    private String oid;

    public String armarInsert(ObjetoPersistente obj) {
        String insert;
        return insert = "insert into pesodereclamo (OIDPesoDeReclamo, CantidadReclamosDesde, CantidadReclamosHasta, FechaDesde, FechaHasta, Peso) values (OIDPesoDeReclamo, CantidadReclamosDesde, CantidadReclamosHasta, FechaDesde, FechaHasta, Peso)";
    }

    public String armarSelect(List<Criterio> criterios) {
        List<Criterio> listaCriterios;
        String select;
        listaCriterios = criterios;
        return select = "select * from pesodereclamo where ";
    }

    public String armarSelectOid(String oid) {
        String selectOid;
        this.oid = oid;
        return selectOid = "select * from pesodereclamo where OIDPesoDeReclamo = " + oid;
    }

    public String armarUpdate(ObjetoPersistente obj) {
        String update;
        return update = "update pesodereclamo set OIDPesoDeReclamo =" + ",CantidadReclamosDesde = " + "CantidadReclamosHasta = " + "FechaDesde =" + "FechaHasta =" + "Peso =";
    }

    public void guardarObjetoCompuesto(ObjetoPersistente obj) {
    }

    public List<ObjetoPersistente> convertirRegistrosAObjetos(ResultSet rs) {
        return null;
    }

    @Override
    public void guardarObjetosRelacionados(ObjetoPersistente obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void buscarObjRelacionados(ObjetoPersistente obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setearDatosPadre(ObjetoPersistente objPer, List<Criterio> listaCriterios) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void guardarDatosPadre(ObjetoPersistente obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
