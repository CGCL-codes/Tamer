package tarea;

import inicio.InicioApp;
import utilidad.clasesBase.*;
import utilidad.componentes.MiTableCellRendererFecha;
import utilidad.vo.ColumnaTablaVO;
import tarea.dao.*;
import utilidad.*;
import java.util.*;
import inicio.*;

/**
 *
 * @author Sergio
 */
public class TareaUtil extends BaseUtil {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("tarea/Bundle");

    public static ArrayList<TareaVO> listado(TareaParametros param) {
        TareaDAO dao = InicioApp.factoriaDAO.getTareaDAO();
        return dao.listadoTareas(param);
    }

    public static ArrayList<ColumnaTablaVO> listadoColumnasTabla() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        ColumnaTablaVO col = null;
        col = new ColumnaTablaVO(bundle.getString("Hora_Inicio"), "getHoraInicio", 0);
        col.setTablaCellRenderer(new MiTableCellRendererFecha("H:mm"));
        col.setAnchoMax(70);
        listaColumnas.add(col);
        col = new ColumnaTablaVO(bundle.getString("Hora_Fin"), "getHoraFin", 1);
        col.setTablaCellRenderer(new MiTableCellRendererFecha("H:mm"));
        col.setAnchoMax(70);
        listaColumnas.add(col);
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Tarea"), "getTarea", 2));
        col = new ColumnaTablaVO(bundle.getString("Horas"), "getTotalHoras", 3);
        col.setAnchoMax(70);
        listaColumnas.add(col);
        return listaColumnas;
    }

    public static TareaVO buscarTarea(int id) {
        TareaVO objTarea = new TareaVO();
        if (id != -1) {
            TareaParametros param = new TareaParametros();
            param.idTarea = id;
            TareaDAO dao = InicioApp.factoriaDAO.getTareaDAO();
            ArrayList<TareaVO> lista = dao.listadoTareas(param);
            if (lista.size() > 0) {
                objTarea = lista.get(0);
            }
        }
        return objTarea;
    }

    public static TareaVO seleccionarTarea(java.awt.Frame padre, TareaVO tarea) {
        return seleccionarTarea(padre, tarea, null, null);
    }

    public static TareaVO seleccionarTarea(java.awt.Frame padre, TareaVO tarea, TareaParametros paramIniciales, TareaParametros paramFijos) {
        ListadoTareas listadoTareas = new ListadoTareas(Ctes.MODO_TABLA_SELECCIONAR, paramIniciales, paramFijos);
        listadoTareas.seleccionarObjeto(tarea);
        ModalFrameUtil.showAsModal(listadoTareas, padre);
        TareaVO tareaSel = null;
        if (listadoTareas.modelo.getListaObjetosSel().size() > 0) {
            tareaSel = (TareaVO) listadoTareas.modelo.getListaObjetosSel().get(0);
        }
        return tareaSel;
    }

    public static Integer insertar(TareaVO tarea) {
        TareaDAO dao = InicioApp.factoriaDAO.getTareaDAO();
        return dao.insertar(tarea);
    }

    public static Integer actualizar(TareaVO tarea) {
        TareaDAO dao = InicioApp.factoriaDAO.getTareaDAO();
        return dao.actualizar(tarea);
    }

    public static int eliminar(int idTarea) {
        TareaDAO dao = InicioApp.factoriaDAO.getTareaDAO();
        return dao.eliminar(idTarea);
    }
}
