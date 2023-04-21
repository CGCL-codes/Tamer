package Persistencia.intermediarios;

import Persistencia.Entidades.DenunciaEstadoAgente;
import Persistencia.ExpertosPersistencia.Criterio;
import Persistencia.Entidades.ObjetoPersistente;
import Persistencia.Fabricas.FabricaEntidades;
import Utilidades.ConvertidorBooleanos;
import Utilidades.FormateadorFechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo
 */
public class IntermediarioPersistenciaDenunciaEstado extends IntermediarioRelacional {

    public String armarInsert(ObjetoPersistente obj) {
        String insert = "";
        DenunciaEstadoAgente denunciaEstado = (DenunciaEstadoAgente) obj;
        if (denunciaEstado.getIndicadoresEstadoActual()) insert = "INSERT INTO denunciaestado (OIDDenunciaEstado, OIDDenuncia, OIDEstadoDenuncia, FechaCambioEstado, IndicadoresEstadoActual)" + "VALUES ( '" + denunciaEstado.getOid() + "', '" + denunciaEstado.getOidDenuncia() + "', '" + denunciaEstado.getOidEstadoDenuncia() + "', '" + FormateadorFechas.getInstancia().formatearAMySql(denunciaEstado.getfechacambioestado()) + "'," + ConvertidorBooleanos.getInstancia().convertirBooleanToInt(denunciaEstado.getIndicadoresEstadoActual()) + ")";
        return insert;
    }

    public String armarSelect(List<Criterio> criterios) {
        String select;
        select = "SELECT * FROM denunciaestado";
        if (!criterios.isEmpty()) {
            select = select + " WHERE ";
            for (int i = 0; i < criterios.size(); i++) {
                if (i > 0) {
                    select = select + " AND ";
                }
                select = select + "denunciaestado." + criterios.get(i).getAtributo() + " " + criterios.get(i).getOperador() + " '" + criterios.get(i).getValor() + "'";
            }
        }
        return select;
    }

    public String armarSelectOid(String oid) {
        String selectOid;
        selectOid = "SELECT * FROM denunciaestado WHERE OIDDenunciaEstado = '" + oid + "'";
        return selectOid;
    }

    public String armarUpdate(ObjetoPersistente obj) {
        String update;
        DenunciaEstadoAgente denunciaestado = (DenunciaEstadoAgente) obj;
        update = "UPDATE denunciaestado SET " + "OIDDenunciaEstado = '" + denunciaestado.getOid() + "'," + "OIDDenuncia = '" + denunciaestado.getOidDenuncia() + "', " + "OIDEstadoDenuncia = '" + denunciaestado.getOidEstadoDenuncia() + "', " + "FechaCambioEstado = '" + FormateadorFechas.getInstancia().formatearAMySql(denunciaestado.getfechacambioestado()) + "', " + "IndicadoresEstadoActual = '" + ConvertidorBooleanos.getInstancia().convertirBooleanToString(denunciaestado.getIndicadoresEstadoActual()) + "'" + " WHERE OIDDenunciaEstado = '" + denunciaestado.getOid() + "'";
        return update;
    }

    public void guardarObjetoCompuesto(ObjetoPersistente obj) {
    }

    public List<ObjetoPersistente> convertirRegistrosAObjetos(ResultSet rs) {
        List<ObjetoPersistente> nuevosObjetos = new ArrayList<ObjetoPersistente>();
        try {
            while (rs.next()) {
                DenunciaEstadoAgente nuevaDenunciaEstado = (DenunciaEstadoAgente) FabricaEntidades.getInstancia().crearEntidad("DenunciaEstado");
                nuevaDenunciaEstado.setIsNuevo(false);
                nuevaDenunciaEstado.setOid(rs.getString("OIDDenunciaEstado"));
                nuevaDenunciaEstado.setOidDenuncia(rs.getString("OIDDenuncia"));
                nuevaDenunciaEstado.setOidEstadoDenuncia(rs.getString("OIDEstadoDenuncia"));
                nuevaDenunciaEstado.setfechacambioestado(rs.getDate("FechaCambioEstado"));
                nuevaDenunciaEstado.setindicadorestadoactual(ConvertidorBooleanos.getInstancia().convertirIntToBoolean(rs.getInt("IndicadoresEstadoActual")));
                nuevosObjetos.add(nuevaDenunciaEstado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IntermediarioPersistenciaDenunciaEstado.class.getName()).log(Level.SEVERE, null, ex);
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
