package es.ait.horas.tareas;

import es.ait.horas.bbdd.DBPool;
import es.ait.horas.bbdd.GenericJPADAO;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/** Clase que contiene los métodos de acceso a la BBDD para objetos del tipo
 * Tarea.
 *
 * @author aitkiar
 */
public class TareaDAO extends GenericJPADAO {

    /** Guarda la tarea pasada como parámetro en la BBDD. El id de la tarea se genera automáticamente
     * haciendo uso de la secuencia sq_tarea_id y machaca lo que pueda haber en el atributo tarea_id
     * del objeto tarea.
     *
     * @param conexion
     * @param tarea
     * @throws Exception
     */
    public void nuevaTarea(Tarea tarea) throws Exception {
        em.persist(tarea);
    }

    /** Modifica una tarea en la BBDD sustituyendo todos sus atributos por los de la tarea pasada
     * como parámetro.
     *
     * @param conexion
     * @param tarea
     * @throws Exception
     */
    public void modificarTarea(Tarea tarea) throws Exception {
        em.merge(tarea);
    }

    /**
     * Elimina una tarea de la BBDD.
     * 
     * @param conexion
     * @param tarea
     * @throws Exception
     */
    public void borrarTarea(Tarea tarea) throws Exception {
        em.remove(tarea);
    }

    /** Busca tareas en la BBDD atendiendo a las condiciones indicadas en el filtro.
     *
     * @param conexion
     * @param filtro
     * @return
     * @throws Exception
     */
    public List<Tarea> buscarTareas(FiltroTareas filtro) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Tarea> resultado = null;
        List<Object> parametros = new ArrayList<Object>();
        String sql = "select c.*, d.horas from tareas c, ( select a.tarea_id, nvl( sum( asig_horas ), 0 ) as horas from tareas a left join asignaciones b on a.tarea_id = b.tarea_id";
        String separador = " where ";
        try {
            if (filtro != null) {
                if (filtro.getTareaId() != null) {
                    sql += separador + "a.tarea_id = ?";
                    parametros.add(filtro.getTareaId());
                    separador = " and ";
                }
                if (filtro.getTareaNombre() != null) {
                    sql += separador + "a.tarea_nombre like ?";
                    parametros.add("%" + filtro.getTareaNombre() + "%");
                    separador = " and ";
                }
                if (filtro.getTareaPadre() != null) {
                    sql += separador + "a.tarea_padre = ?";
                    parametros.add(filtro.getTareaPadre());
                    separador = " and ";
                } else {
                    if (filtro.isSoloPadres()) {
                        sql += separador + "a.tarea_padre is null";
                        separador = " and ";
                    }
                }
                if (filtro.isActivo()) {
                    sql += separador + "a.tarea_activo=?";
                    parametros.add(filtro.getTareaActivo());
                    separador = " and ";
                }
                if (filtro.getTareaFxCreacionInicio() != null) {
                    sql += separador + "a.tarea_fx_creacion >= ?";
                    parametros.add(filtro.getTareaFxCreacionInicio());
                    separador = " and ";
                }
                if (filtro.getTareaFxCreacionFin() != null) {
                    sql += separador + "a.tarea_fx_creacion <= ?";
                    parametros.add(filtro.getTareaFxCreacionFin());
                    separador = " and ";
                }
                if (filtro.getUsuaLoginCreador() != null) {
                    sql += separador + "a.usua_login_creador = ?";
                    parametros.add(filtro.getUsuaLoginCreador());
                    separador = " and ";
                }
                if (filtro.getProyId() != null) {
                    sql += separador + "a.proy_id = ?";
                    parametros.add(filtro.getProyId().getProyId());
                    separador = " and ";
                }
            }
            sql += " group by a.tarea_id ) d where c.tarea_id = d.tarea_id order by c.tarea_nombre";
            Query consulta = em.createNativeQuery(sql, "Tarea.listadoConHoras");
            for (int i = 0; i < parametros.size(); i++) {
                consulta.setParameter(i + 1, parametros.get(i));
            }
            List<Object[]> temp = consulta.getResultList();
            resultado = new <Tarea>ArrayList();
            for (int i = 0; i < temp.size(); i++) {
                resultado.add((Tarea) temp.get(i)[0]);
                resultado.get(i).setHoras(temp.get(i)[1] == null ? null : new Long(((BigDecimal) temp.get(i)[1]).longValue()));
            }
            return resultado;
        } catch (Exception e) {
            throw e;
        } finally {
            DBPool.close(rs, ps, null);
        }
    }
}
