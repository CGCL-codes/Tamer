package operador;

import utilidad.vo.ColumnaTablaVO;
import utilidad.clasesBase.*;
import operador.dao.*;
import java.util.*;
import javax.swing.*;
import inicio.*;
import utilidad.Ctes;
import utilidad.ModalFrameUtil;
import utilidad.vo.ItemComboVO;

public class OperadorUtil extends BaseUtil {

    public static String PERFIL_SUPERADMIN = "SUPERADMIN";

    public static String PERFIL_ADMIN = "ADMIN";

    public static String PERFIL_USUARIO = "USUARIO";

    public static String PERFIL_EDUCADOR = "EDUCADOR";

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("operador/Bundle");

    public static OperadorVO autentificar(String usuario, String contrasena, Boolean web) {
        OperadorDAO dao = InicioApp.factoriaDAO.getOperadorDAO();
        return dao.autentificar(usuario, contrasena, web);
    }

    public static ArrayList<OperadorVO> listado(OperadorParametros param) {
        OperadorDAO dao = InicioApp.factoriaDAO.getOperadorDAO();
        return dao.listadoOperadores(param);
    }

    public static ArrayList<ColumnaTablaVO> listadoColumnasTabla() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Operador"), "getNombreCompleto", 0, SwingConstants.LEFT, 200));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Usuario"), "getUsuario", 1, SwingConstants.LEFT));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Perfil"), "getPerfil", 2));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Estado"), "getEstado", 3));
        return listaColumnas;
    }

    public static ArrayList<ColumnaTablaVO> listadoColumnasTablaModelo() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        int i = 0;
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Operador"), "getNombreCompleto", i++, SwingConstants.LEFT));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Perfil"), "getPerfil", i++));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Estado"), "getEstado", i++));
        return listaColumnas;
    }

    public static OperadorVO buscarOperador(int id) {
        OperadorVO objOperador = new OperadorVO();
        if (id != -1) {
            OperadorParametros param = new OperadorParametros();
            param.idOperador = id;
            OperadorDAO dao = InicioApp.factoriaDAO.getOperadorDAO();
            ArrayList<OperadorVO> lista = dao.listadoOperadores(param);
            if (lista.size() > 0) {
                objOperador = lista.get(0);
            }
        }
        return objOperador;
    }

    public static OperadorVO seleccionarOperador(java.awt.Frame padre, OperadorVO filtro) {
        return seleccionarOperador(padre, filtro, null, null);
    }

    public static OperadorVO seleccionarOperador(java.awt.Frame padre, OperadorVO filtro, OperadorParametros paramIniciales, OperadorParametros paramFijos) {
        List<BaseVO> listaOperadoresVOSel = seleccionarListaOperadores(padre, filtro, paramIniciales, paramFijos);
        OperadorVO filtroSel = null;
        if (listaOperadoresVOSel.size() > 0) {
            filtroSel = (OperadorVO) listaOperadoresVOSel.get(0);
        }
        return filtroSel;
    }

    public static ArrayList<BaseVO> seleccionarListaOperadores(java.awt.Frame padre, OperadorParametros paramIniciales, OperadorParametros paramFijos) {
        return seleccionarListaOperadores(padre, null, paramIniciales, paramFijos);
    }

    public static ArrayList<BaseVO> seleccionarListaOperadores(java.awt.Frame padre, OperadorVO filtro, OperadorParametros paramIniciales, OperadorParametros paramFijos) {
        ListadoOperadores listadoOperadores = new ListadoOperadores(Ctes.MODO_TABLA_SELECCIONAR, paramIniciales, paramFijos);
        if (filtro != null) {
            listadoOperadores.seleccionarObjeto(filtro);
        }
        ModalFrameUtil.showAsModal(listadoOperadores, padre);
        return listadoOperadores.modelo.getListaObjetosSel();
    }

    public static void generarInicializaciones(OperadorVO operador) {
        if (OperadorUtil.PERFIL_SUPERADMIN.equals(operador.getPerfil())) {
            Ctes.listaPerfilesOperadores.add(new ItemComboVO(OperadorUtil.PERFIL_SUPERADMIN));
        }
    }

    public static Integer insertar(OperadorVO operador) {
        OperadorDAO dao = InicioApp.factoriaDAO.getOperadorDAO();
        return dao.insertar(operador);
    }

    public static Integer actualizar(OperadorVO operador) {
        OperadorDAO dao = InicioApp.factoriaDAO.getOperadorDAO();
        return dao.actualizar(operador);
    }

    public static int eliminar(int idOperador) {
        OperadorDAO dao = InicioApp.factoriaDAO.getOperadorDAO();
        return dao.eliminar(idOperador);
    }
}
