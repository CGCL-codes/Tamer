package Persistencia.intermediarios;

import Persistencia.Entidades.EstadoDenunciaAgente;
import Persistencia.ExpertosPersistencia.Criterio;
import Persistencia.Entidades.ObjetoPersistente;
import Persistencia.Fabricas.FabricaEntidades;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public class IntermediarioPersistenciaEstadoDenuncia extends IntermediarioRelacional {

    public String armarInsert(ObjetoPersistente obj) {
        EstadoDenunciaAgente estadoDenuncia = (EstadoDenunciaAgente) obj;
        String insert;
        insert = "INSERT INTO estadodenuncia (OIDEstadoDenuncia, CodigoEstadoDenuncia, NombreEstado) " + "VALUES ('" + estadoDenuncia.getOid() + "', " + String.valueOf(estadoDenuncia.getcodigoestadodenuncia()) + ", '" + estadoDenuncia.getnombreestado() + "')";
        return insert;
    }

    public String armarSelect(List<Criterio> criterios) {
        String select;
        select = "SELECT * FROM estadodenuncia";
        if (!criterios.isEmpty()) {
            select = select + " WHERE ";
            for (int i = 0; i < criterios.size(); i++) {
                if (i > 0) {
                    select = select + " AND ";
                }
                select = select + "estadodenuncia." + criterios.get(i).getAtributo() + " " + criterios.get(i).getOperador() + " '" + criterios.get(i).getValor() + "'";
            }
        }
        return select;
    }

    public String armarSelectOid(String oid) {
        String selectOid;
        selectOid = "SELECT * FROM estadodenuncia WHERE OIDEstadoDenuncia = '" + oid + "'";
        return selectOid;
    }

    public String armarUpdate(ObjetoPersistente obj) {
        EstadoDenunciaAgente estadoDenuncia = (EstadoDenunciaAgente) obj;
        String update;
        update = "UPDATE estadodenuncia " + "SET OIDEstadoDenuncia = '" + estadoDenuncia.getOid() + "', " + "CodigoEstadoDenuncia = " + String.valueOf(estadoDenuncia.getcodigoestadodenuncia()) + ", " + "NombreEstado = '" + estadoDenuncia.getnombreestado() + "'";
        return update;
    }

    public void guardarObjetoCompuesto(ObjetoPersistente obj) {
    }

    public List<ObjetoPersistente> convertirRegistrosAObjetos(ResultSet rs) {
        List<ObjetoPersistente> nuevosObjetos = new ArrayList<ObjetoPersistente>();
        try {
            while (rs.next()) {
                EstadoDenunciaAgente nuevoEstadoDenuncia = (EstadoDenunciaAgente) FabricaEntidades.getInstancia().crearEntidad("EstadoDenuncia");
                nuevoEstadoDenuncia.setOid(rs.getString("OIDEstadoDenuncia"));
                nuevoEstadoDenuncia.setIsNuevo(false);
                nuevoEstadoDenuncia.setcodigoestadodenuncia(Integer.valueOf(rs.getString("CodigoEstadoDenuncia")));
                nuevoEstadoDenuncia.setnombreestado(rs.getString("NombreEstado"));
                nuevosObjetos.add(nuevoEstadoDenuncia);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return nuevosObjetos;
    }

    @Override
    public void guardarObjetosRelacionados(ObjetoPersistente obj) {
    }

    @Override
    public void buscarObjRelacionados(ObjetoPersistente obj) {
    }

    @Override
    public void setearDatosPadre(ObjetoPersistente objPer, List<Criterio> listaCriterios) {
    }

    @Override
    public void guardarDatosPadre(ObjetoPersistente obj) {
    }
}
