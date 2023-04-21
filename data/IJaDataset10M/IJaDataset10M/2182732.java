package provincia;

import inicio.InicioApp;
import utilidad.vo.ColumnaTablaVO;
import utilidad.clasesBase.*;
import provincia.dao.*;
import java.util.*;
import inicio.*;

/**
 *
 * @author Sergio
 */
public class ProvinciaUtil extends BaseUtil {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("provincia/Bundle");

    public static ArrayList<ProvinciaVO> listado(ProvinciaParametros param) {
        ProvinciaDAO dao = InicioApp.factoriaDAO.getProvinciaDAO();
        return dao.listadoProvincias(param);
    }

    public static ArrayList<ColumnaTablaVO> listadoColumnasTabla() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Código"), "getCodigo", 0));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Nombre"), "getNombre", 1));
        return listaColumnas;
    }

    public static ProvinciaVO buscarProvincia(int id) {
        ProvinciaVO objProvincia = new ProvinciaVO();
        ProvinciaParametros param = new ProvinciaParametros();
        param.idProvincia = id;
        ProvinciaDAO dao = InicioApp.factoriaDAO.getProvinciaDAO();
        ArrayList<ProvinciaVO> lista = dao.listadoProvincias(param);
        if (lista.size() > 0) {
            objProvincia = lista.get(0);
        }
        return objProvincia;
    }

    public static int guardar(ProvinciaVO provincia) {
        int num = -1;
        if (provincia.getId() == -1) {
            num = insertar(provincia);
        } else {
            num = actualizar(provincia);
        }
        return num;
    }

    public static Integer insertar(ProvinciaVO provincia) {
        ProvinciaDAO dao = InicioApp.factoriaDAO.getProvinciaDAO();
        return dao.insertar(provincia);
    }

    public static Integer actualizar(ProvinciaVO provincia) {
        ProvinciaDAO dao = InicioApp.factoriaDAO.getProvinciaDAO();
        return dao.actualizar(provincia);
    }

    public static Integer eliminar(ProvinciaVO provincia) {
        ProvinciaDAO dao = InicioApp.factoriaDAO.getProvinciaDAO();
        return dao.actualizar(provincia);
    }
}
