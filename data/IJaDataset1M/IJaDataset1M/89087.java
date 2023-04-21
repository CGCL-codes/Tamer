package foto;

import utilidad.clasesBase.*;
import utilidad.vo.ColumnaTablaVO;
import foto.dao.*;
import java.awt.Image;
import java.util.*;
import javax.swing.*;
import java.io.*;
import inicio.*;
import blob.BlobUtil;
import blob.BlobVO;
import utilidad.AlmacenPropiedades;
import utilidad.Ctes;
import utilidad.ModalFrameUtil;
import utilidad.Util;
import utilidad.RutasUtil;

public class FotoUtil extends BaseUtil {

    public static String ENTIDAD_MARCA = "MARCA";

    public static String ENTIDAD_USUARIO = "USUARIO";

    public static String ENTIDAD_EDUCADOR = "EDUCADOR";

    public static String ENTIDAD_CLIENTE = "CLIENTE";

    public static String ENTIDAD_INSTALACION = "INSTALACION";

    public static String ENTIDAD_ACTIVIDAD = "ACTIVIDAD";

    public static String RutaFotos = AlmacenPropiedades.getPropiedad("Fotos.Ruta");

    public static String RutaOrigenFotos = AlmacenPropiedades.getPropiedad("Fotos.RutaOrigen");

    public static final String MostrarFotoUsuario = AlmacenPropiedades.getPropiedad("Fotos.MostrarFotoUsuario");

    public static final String RedimensionarFoto = AlmacenPropiedades.getPropiedad("Fotos.Redimensionar");

    public static final int AltoFoto = Util.transformaEntero(AlmacenPropiedades.getPropiedad("Fotos.AltoFotoRedimensionada"), 150);

    public static final int AnchoFoto = Util.transformaEntero(AlmacenPropiedades.getPropiedad("Fotos.AnchoFotoRedimensionada"), 200);

    public static int ladoMayorImagenListado = 50;

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("foto/Bundle");

    public static ArrayList<FotoVO> listado(FotoParametros param) {
        FotoDAO dao = InicioApp.factoriaDAO.getFotoDAO();
        return dao.listadoFotos(param);
    }

    public static ArrayList<ColumnaTablaVO> listadoColumnasTabla() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Nombre"), "getNombre", 0, SwingConstants.LEFT, 250));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Fecha"), "getFecha", 1));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("PPL"), "esPpl", 2));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Estado"), "getEstado", 3));
        listaColumnas.add(new ColumnaTablaVO(bundle.getString("Entidad"), "getEntidad", 4));
        return listaColumnas;
    }

    public static FotoVO buscarFoto(int id) {
        FotoVO objFoto = new FotoVO();
        if (id != -1) {
            FotoParametros param = new FotoParametros();
            param.idFoto = id;
            FotoDAO dao = InicioApp.factoriaDAO.getFotoDAO();
            ArrayList<FotoVO> lista = dao.listadoFotos(param);
            if (lista.size() > 0) {
                objFoto = lista.get(0);
            }
        }
        return objFoto;
    }

    public static FotoVO mostrarFotos(java.awt.Frame padre, String entidad, int idEntidad, String nombre) {
        FotoParametros param = new FotoParametros();
        param.ordenSQL = "fotos.fecha ";
        FotoParametros paramFijos = new FotoParametros();
        paramFijos.entidad = entidad;
        paramFijos.idEntidad = idEntidad;
        ListadoFotos frmListado = new ListadoFotos(Ctes.MODO_TABLA_LISTADO, param, paramFijos, nombre);
        ModalFrameUtil.showAsModal(frmListado, padre);
        return getPrimeraFotoPpl(frmListado.modelo.getListaObjetos());
    }

    public static FotoVO getPrimeraFotoPpl(String entidad, int idEntidad) {
        FotoParametros param = new FotoParametros();
        param.ordenSQL = "fotos.fecha ";
        param.entidad = entidad;
        param.idEntidad = idEntidad;
        ArrayList listadoFotos = FotoUtil.listado(param);
        return getPrimeraFotoPpl(listadoFotos);
    }

    public static FotoVO getPrimeraFotoPpl(ArrayList<BaseVO> listado) {
        FotoVO fotoPpl = new FotoVO();
        for (BaseVO obj : listado) {
            FotoVO foto = (FotoVO) obj;
            if (foto.esPpl()) {
                fotoPpl = foto;
                break;
            }
        }
        return fotoPpl;
    }

    public static Image getImagenFotoPpl(String entidad, int idEntidad) {
        Image imagen = null;
        FotoVO foto = FotoUtil.getPrimeraFotoPpl(entidad, idEntidad);
        if (foto.getId() != -1) {
            BlobVO blob = BlobUtil.buscarBlob(foto.getIdBlob());
            imagen = blob.getImagen();
        }
        return imagen;
    }

    public static String getRutaFotoDisco(FotoVO foto) {
        String rutaFotoDisco = "";
        rutaFotoDisco = FotoUtil.RutaFotos + foto.getId() + "." + foto.getExtension();
        File archivoFoto = new File(rutaFotoDisco);
        if (!archivoFoto.exists()) {
            foto.setArchivo(rutaFotoDisco);
        }
        return rutaFotoDisco;
    }

    public static Image redimensionar(Image imagen) {
        return Util.redimensionarImagen(imagen, AnchoFoto, AltoFoto);
    }

    public static Integer insertar(FotoVO foto, BlobVO blob) {
        BlobUtil.insertar(blob);
        foto.setIdBlob(blob.getId());
        FotoDAO dao = InicioApp.factoriaDAO.getFotoDAO();
        return dao.insertar(foto);
    }

    public static Integer actualizar(FotoVO foto, BlobVO blob) {
        BlobUtil.actualizar(blob);
        FotoDAO dao = InicioApp.factoriaDAO.getFotoDAO();
        return dao.actualizar(foto);
    }

    public static int eliminar(int idFoto) {
        FotoVO foto = FotoUtil.buscarFoto(idFoto);
        BlobUtil.eliminar(foto.getIdBlob());
        FotoDAO dao = InicioApp.factoriaDAO.getFotoDAO();
        return dao.eliminar(idFoto);
    }
}
