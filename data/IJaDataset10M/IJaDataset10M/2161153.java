package Logica;

import EntidadesCompartidas.*;
import Datos.DatosFachada;
import java.util.LinkedList;

/**
 *
 * @author seanOx
 */
public class Logica_Categoria {

    protected static synchronized LinkedList<Categoria> getAllCategorias(LinkedList<Categoria> categorias) throws Exception {
        try {
            return categorias;
        } catch (Exception Ex) {
            throw new Exception("Error :" + Ex.getMessage());
        }
    }
}
