package Persistencia.intermediarios;

import Persistencia.Entidades.Caso;
import Persistencia.Entidades.CasoAgente;
import Persistencia.Entidades.Denuncia;
import Persistencia.Entidades.DenunciaAgente;
import Persistencia.Entidades.DenunciaEstado;
import Persistencia.Entidades.DenunciaEstadoAgente;
import Persistencia.Entidades.EstadoDenuncia;
import Persistencia.Entidades.FallaTecnica;
import Persistencia.ExpertosPersistencia.Criterio;
import Persistencia.Entidades.ObjetoPersistente;
import Persistencia.Entidades.Reclamo;
import Persistencia.Entidades.ReclamoAgente;
import Persistencia.Entidades.SuperDruperInterfaz;
import Persistencia.ExpertosPersistencia.FachadaInterna;
import Persistencia.Fabricas.FabricaCriterios;
import Persistencia.Fabricas.FabricaEntidades;
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
public class IntermediarioPersistenciaDenuncia extends IntermediarioRelacional {

    public String armarInsert(ObjetoPersistente obj) {
        String insert;
        DenunciaAgente denuncia = (DenunciaAgente) obj;
        insert = "INSERT INTO denuncia (OIDCaso, CodigoDenuncia, Prioridad)" + "VALUES ('" + denuncia.getOid() + "' ,'" + denuncia.getcodigoDenuncia() + "' ,'" + denuncia.getprioridad() + "') ";
        return insert;
    }

    public String armarSelect(List<Criterio> criterios) {
        String select = "";
        String join = "";
        String condicion = " WHERE ";
        select = "SELECT * FROM denuncia ";
        if (!criterios.isEmpty()) {
            for (int i = 0; i < criterios.size(); i++) {
                if (i > 0) {
                    condicion = condicion + " AND ";
                }
                if (criterios.get(i).getAtributo().equalsIgnoreCase("Reclamo")) {
                    join = " JOIN reclamo ON reclamo.OIDDenuncia = denuncia.OIDCaso ";
                    condicion = condicion + "reclamo.OIDCaso" + " " + criterios.get(i).getOperador() + " '" + criterios.get(i).getValor() + "'";
                } else if (criterios.get(i).getAtributo().equalsIgnoreCase("Semaforo")) {
                    join = " JOIN casosemaforo ON casosemaforo.OIDCaso = denuncia.OIDCaso ";
                    criterios.get(i).setAtributo("OIDSemaforo");
                    condicion = condicion + "casosemaforo." + criterios.get(i).getAtributo() + " " + criterios.get(i).getOperador() + " '" + criterios.get(i).getValor() + "'";
                } else condicion = condicion + "denuncia." + criterios.get(i).getAtributo() + " " + criterios.get(i).getOperador() + " '" + criterios.get(i).getValor() + "'";
            }
        }
        return select + join + condicion;
    }

    public String armarSelectOid(String oid) {
        String selectOid;
        return selectOid = "SELECT * FROM denuncia WHERE OIDCaso = '" + oid + "'";
    }

    public String armarUpdate(ObjetoPersistente obj) {
        String update;
        DenunciaAgente denuncia = (DenunciaAgente) obj;
        update = "UPDATE denuncia SET " + " OIDCaso = '" + denuncia.getOid() + "'," + " CodigoDenuncia = '" + denuncia.getcodigoDenuncia() + "'," + " Prioridad = '" + denuncia.getprioridad() + "'" + " WHERE OIDCaso = '" + denuncia.getOid() + "'";
        return update;
    }

    public void guardarObjetoCompuesto(ObjetoPersistente obj) {
        Denuncia den = (Denuncia) obj;
        for (DenunciaEstado aux : den.getDenunciaEstado()) {
            DenunciaEstadoAgente denAgEst = (DenunciaEstadoAgente) aux;
            denAgEst.setOidDenuncia(obj.getOid());
            FachadaInterna.getInstancia().guardar("DenunciaEstado", (ObjetoPersistente) denAgEst);
        }
        for (Reclamo rec : den.getReclamo()) {
            ((ReclamoAgente) rec).setOidDenuncia(obj.getOid());
            FachadaInterna.getInstancia().guardar("Reclamo", (ObjetoPersistente) rec);
        }
    }

    public List<ObjetoPersistente> convertirRegistrosAObjetos(ResultSet rs) {
        List<ObjetoPersistente> nuevosObjetos = new ArrayList<ObjetoPersistente>();
        try {
            while (rs.next()) {
                DenunciaAgente nuevaDenuncia = (DenunciaAgente) FabricaEntidades.getInstancia().crearEntidad("Denuncia");
                nuevaDenuncia.setIsNuevo(false);
                nuevaDenuncia.setOid(rs.getString("OIDCaso"));
                nuevaDenuncia.setcodigoDenuncia(Integer.valueOf(rs.getString("CodigoDenuncia")));
                nuevaDenuncia.setprioridad(Float.valueOf(rs.getString("Prioridad")));
                nuevaDenuncia.setDenunciaEstadoBuscado(false);
                nuevosObjetos.add(nuevaDenuncia);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IntermediarioPersistenciaDenuncia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nuevosObjetos;
    }

    @Override
    public void guardarObjetosRelacionados(ObjetoPersistente obj) {
    }

    @Override
    public void buscarObjRelacionados(ObjetoPersistente obj) {
        List<Criterio> listaCriterios = new ArrayList<Criterio>();
        listaCriterios.add(FachadaInterna.getInstancia().crearCriterio("Denuncia", "=", obj.getOid()));
        for (SuperDruperInterfaz fallaTecnica : FachadaInterna.getInstancia().buscar("FallaTecnica", listaCriterios)) {
            ((DenunciaAgente) obj).addOidFallaTecnica(((ObjetoPersistente) fallaTecnica).getOid());
        }
    }

    @Override
    public void setearDatosPadre(ObjetoPersistente objPer, List<Criterio> listaCriterios) {
        CasoAgente padre = (CasoAgente) FachadaInterna.getInstancia().buscar("Caso", objPer.getOid());
        ((CasoAgente) objPer).setOidDenunciante(padre.getOidDenunciante());
        ((CasoAgente) objPer).setDenuncianteBuscado(false);
        ((CasoAgente) objPer).setOidOperador(padre.getOidOperador());
        ((CasoAgente) objPer).setOperadorBuscado(false);
        ((CasoAgente) objPer).setfechacaso(padre.getfechacaso());
        ((CasoAgente) objPer).settipocaso(padre.gettipocaso());
        ((CasoAgente) objPer).setSemaforoBuscado(false);
        ((CasoAgente) objPer).setOidSemaforo(padre.getOidSemaforo());
    }

    @Override
    public void guardarDatosPadre(ObjetoPersistente obj) {
        FachadaInterna.getInstancia().guardar("Caso", obj);
    }
}
