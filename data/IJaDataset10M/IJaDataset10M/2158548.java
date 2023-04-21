package vinculacionUnidad;

import utilidad.clasesBase.*;
import utilidad.vo.ColumnaTablaVO;
import vinculacionUnidad.dao.*;
import java.util.*;
import javax.swing.*;
import inicio.*;
import ocupacion.OcupacionVO;
import utilidad.Ctes;
import utilidad.ModalFrameUtil;

public class VinculacionUnidadUtil extends BaseUtil {

    public static String ENTIDAD_ACTIVIDAD = "ACTIVIDAD";

    public static String ENTIDAD_GRUPO = "GRUPO";

    public static String ENTIDAD_RESERVA = "RESERVA";

    public static ArrayList<VinculacionUnidadVO> listado(VinculacionUnidadParametros param) {
        VinculacionUnidadDAO dao = InicioApp.factoriaDAO.getVinculacionUnidadDAO();
        return dao.listadoVinculacionesUnidad(param);
    }

    public static ArrayList<ColumnaTablaVO> listadoColumnasTabla() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        listaColumnas.add(new ColumnaTablaVO("Unidad", "getNombreUnidad", 0, SwingConstants.LEFT));
        return listaColumnas;
    }

    public static VinculacionUnidadVO buscarVinculacionUnidad(int id) {
        VinculacionUnidadVO objVinculacionUnidad = new VinculacionUnidadVO();
        if (id != -1) {
            VinculacionUnidadParametros param = new VinculacionUnidadParametros();
            param.idVinculacionUnidad = id;
            VinculacionUnidadDAO dao = InicioApp.factoriaDAO.getVinculacionUnidadDAO();
            ArrayList<VinculacionUnidadVO> lista = dao.listadoVinculacionesUnidad(param);
            if (lista.size() > 0) {
                objVinculacionUnidad = lista.get(0);
            }
        }
        return objVinculacionUnidad;
    }

    public static void vincularUnidades(ArrayList<OcupacionVO> listaOcupacionesUnidad, String entidad, int idEntidad) {
        for (OcupacionVO ocupacion : listaOcupacionesUnidad) {
            VinculacionUnidadVO vincUnidad = new VinculacionUnidadVO();
            vincUnidad.setEntidad(entidad);
            vincUnidad.setIdEntidad(idEntidad);
            vincUnidad.setIdUnidad(ocupacion.getIdUnidad());
            VinculacionUnidadUtil.insertar(vincUnidad);
        }
    }

    public static VinculacionUnidadVO seleccionarVinculacionUnidad(java.awt.Frame padre, VinculacionUnidadVO vinculacionUnidad) {
        return seleccionarVinculacionUnidad(padre, vinculacionUnidad, null, null);
    }

    public static VinculacionUnidadVO seleccionarVinculacionUnidad(java.awt.Frame padre, VinculacionUnidadVO vinculacionUnidad, VinculacionUnidadParametros paramIniciales, VinculacionUnidadParametros paramFijos) {
        ArrayList<BaseVO> listaVinculacionUnidadVOSel = seleccionarListaVinculacionesUnidad(padre, vinculacionUnidad, paramIniciales, paramFijos);
        VinculacionUnidadVO vinculacionUnidadSel = null;
        if (listaVinculacionUnidadVOSel.size() > 0) {
            vinculacionUnidadSel = (VinculacionUnidadVO) listaVinculacionUnidadVOSel.get(0);
        }
        return vinculacionUnidadSel;
    }

    public static ArrayList<BaseVO> seleccionarListaVinculacionesUnidad(java.awt.Frame padre, VinculacionUnidadParametros paramIniciales, VinculacionUnidadParametros paramFijos) {
        return seleccionarListaVinculacionesUnidad(padre, null, paramIniciales, paramFijos);
    }

    public static ArrayList<BaseVO> seleccionarListaVinculacionesUnidad(java.awt.Frame padre, VinculacionUnidadVO vinculacionUnidad, VinculacionUnidadParametros paramIniciales, VinculacionUnidadParametros paramFijos) {
        ListadoVinculacionesUnidad listadoVinculacionesUnidad = new ListadoVinculacionesUnidad(Ctes.MODO_TABLA_SELECCIONAR, paramIniciales, paramFijos);
        if (vinculacionUnidad != null) {
            listadoVinculacionesUnidad.seleccionarObjeto(vinculacionUnidad);
        }
        ModalFrameUtil.showAsModal(listadoVinculacionesUnidad, padre);
        return listadoVinculacionesUnidad.modelo.getListaObjetosSel();
    }

    public static String getStringUnidades(ArrayList<BaseVO> listadoUnidadesVinculadas) {
        String cadena = "";
        int tamLista = listadoUnidadesVinculadas.size();
        int pos = 0;
        for (BaseVO obj : listadoUnidadesVinculadas) {
            VinculacionUnidadVO und = (VinculacionUnidadVO) obj;
            cadena += und.getNombreUnidad();
            pos++;
            if (pos < tamLista) {
                cadena += ", ";
            }
        }
        return cadena;
    }

    public static Integer insertar(VinculacionUnidadVO vinculacionUnidad) {
        VinculacionUnidadDAO dao = InicioApp.factoriaDAO.getVinculacionUnidadDAO();
        return dao.insertar(vinculacionUnidad);
    }

    public static Integer actualizar(VinculacionUnidadVO vinculacionUnidad) {
        VinculacionUnidadDAO dao = InicioApp.factoriaDAO.getVinculacionUnidadDAO();
        return dao.actualizar(vinculacionUnidad);
    }

    public static int eliminar(int idVinculacionUnidad) {
        VinculacionUnidadDAO dao = InicioApp.factoriaDAO.getVinculacionUnidadDAO();
        return dao.eliminar(idVinculacionUnidad);
    }

    public static int eliminar(String entidad, int idEntidad) {
        VinculacionUnidadDAO dao = InicioApp.factoriaDAO.getVinculacionUnidadDAO();
        return dao.eliminar(entidad, idEntidad);
    }
}
