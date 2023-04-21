package archivo;

import utilidad.vo.ColumnaTablaVO;
import javax.swing.*;
import inicio.*;
import foto.FotoUtil;
import foto.FotoVO;
import blob.BlobUtil;
import blob.BlobVO;
import inicio.InicioApp;
import utilidad.*;
import utilidad.clasesBase.*;
import archivo.dao.*;
import java.util.*;

/**
 *
 * @author Egalcom - Carlos
 */
public class ArchivoUtil extends BaseUtil {

    public static String rutaArchivos = System.getProperty("user.dir") + "\\archivos\\";

    public static String ENTIDAD_CONTRATO = "CONTRATO";

    public static String ENTIDAD_USUARIO = "USUARIO";

    public static String ENTIDAD_EDUCADOR = "EDUCADOR";

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("archivo/Bundle");

    public static ArrayList<ArchivoVO> listado(ArchivoParametros param) {
        ArchivoDAO dao = InicioApp.factoriaDAO.getArchivoDAO();
        return dao.listadoArchivos(param);
    }

    public static ArrayList<ColumnaTablaVO> listadoColumnasTabla() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Nombre"), "getNombre", 0, SwingConstants.LEFT));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Extension"), "getExtension", 1, SwingConstants.LEFT));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Tamaño"), "getTamano", 2));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Fecha"), "getFecha", 3));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Estado"), "getEstado", 4));
        return listaColumnas;
    }

    public static ArchivoVO buscarArchivo(int id) {
        ArchivoVO objArchivo = new ArchivoVO();
        ArchivoParametros param = new ArchivoParametros();
        param.idArchivo = id;
        ArchivoDAO dao = InicioApp.factoriaDAO.getArchivoDAO();
        ArrayList<ArchivoVO> lista = dao.listadoArchivos(param);
        if (lista.size() > 0) {
            objArchivo = lista.get(0);
        }
        return objArchivo;
    }

    public static Integer insertar(ArchivoVO archivo, BlobVO blob) {
        BlobUtil.insertar(blob);
        archivo.setIdBlob(blob.getId());
        ArchivoDAO dao = InicioApp.factoriaDAO.getArchivoDAO();
        return dao.insertar(archivo);
    }

    public static Integer actualizar(ArchivoVO archivo, BlobVO blob) {
        BlobUtil.actualizar(blob);
        ArchivoDAO dao = InicioApp.factoriaDAO.getArchivoDAO();
        return dao.actualizar(archivo);
    }

    public static int eliminar(int idArchivo) {
        ArchivoVO archivo = ArchivoUtil.buscarArchivo(idArchivo);
        BlobUtil.eliminar(archivo.getIdBlob());
        ArchivoDAO dao = InicioApp.factoriaDAO.getArchivoDAO();
        return dao.eliminar(idArchivo);
    }

    public static ArchivoVO mostrarArchivo(java.awt.Frame padre, String entidad, int idEntidad) {
        ArchivoParametros param = new ArchivoParametros();
        param.ordenSQL = "archivos.nombre ";
        ArchivoParametros paramFijos = new ArchivoParametros();
        paramFijos.entidad = entidad;
        paramFijos.idEntidad = idEntidad;
        ListadoArchivos frmListado = new ListadoArchivos(Ctes.MODO_TABLA_LISTADO, param, paramFijos);
        Util.mostrarVentana(frmListado);
        return getArchivo(frmListado.modelo.getListaObjetos());
    }

    public static ArchivoVO getArchivo(ArrayList<BaseVO> listado) {
        ArchivoVO archivoPpl = new ArchivoVO();
        for (BaseVO obj : listado) {
            ArchivoVO archivo = (ArchivoVO) obj;
            archivoPpl = archivo;
        }
        return archivoPpl;
    }
}
