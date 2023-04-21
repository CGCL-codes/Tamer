package Logica;

import EntidadesCompartidas.*;
import java.util.LinkedList;
import Datos.DatosFachada;

/**
 *
 * @author seanOx
 */
public class LogicaFachada {

    public static synchronized LinkedList<Categoria> getAllCategorias() throws Exception {
        try {
            return Logica_Categoria.getAllCategorias(DatosFachada.getAllCategorias());
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized LinkedList<Publicacion> getPublicacionesByRubro(Rubro rubro) throws Exception {
        try {
            rubro.setId(getRubroIdByName(rubro.getNombre()));
            return LogicaPublicacion.getPublicacionesByRubros(DatosFachada.getPublicacionesByRubro(rubro));
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized int getRubroIdByName(String name) throws Exception {
        try {
            return LogicaRubro.getRubroIdByName(DatosFachada.getRubroIdByName(name));
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized Publicacion getPublicacionById(Publicacion Pub) throws Exception {
        try {
            return LogicaPublicacion.getPublicacionById(DatosFachada.getPublicacionById(Pub));
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized Usuario autenticarUsuario(Usuario usuario) throws Exception {
        try {
            return LogicaUsuario.autenticarUsuario(DatosFachada.autenticarUsuario(usuario));
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized void InsertatComentario(Comentario pComentario) throws Exception {
        try {
            DatosFachada.InsertarComentario(pComentario);
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized LinkedList<Productos_Servicios> getProductosById_Publicacion(Publicacion pPublicacion) throws Exception {
        try {
            return DatosFachada.getProductosById_Publicacion(pPublicacion);
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized LinkedList<Comentario> Getallcomentraios(LinkedList<Productos_Servicios> plistProductos) throws Exception {
        try {
            return DatosFachada.Getallcomentraios(plistProductos);
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized LinkedList<Usuario> GetUsuariosByIds(LinkedList<Integer> plistIsUsuarios) throws Exception {
        try {
            return DatosFachada.GetUsuariosByIds(plistIsUsuarios);
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized void DeleteComentarioById(int pIdComentario) throws Exception {
        try {
            DatosFachada.DeleteComentarioById(pIdComentario);
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }

    public static synchronized Publicacion getPublicacionByPathName(Publicacion pubAux) throws Exception {
        try {
            return DatosFachada.getPublicacionByPathName(pubAux);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
