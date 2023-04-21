package org.gestionabierta.dominio.persistencia.presupuesto;

import java.util.List;
import org.gestionabierta.dominio.modelo.presupuesto.UnidadEjecutora;
import org.gestionabierta.dominio.persistencia.IGenericDao;
import org.gestionabierta.utilidades.excepciones.DataAccessLayerException;

/**
 *
 * @author Franky Villadiego
 */
public interface IUnidadEjecutoraDao extends IGenericDao {

    /**
     * <p>Retorna la unidad ejecutora correspondiente.
     *
     * @param codigo
     * @return unidad ejecutora o null si no existe
     * @throws DataAccessLayerException
     */
    public UnidadEjecutora traerPorCodigo(String codigo) throws DataAccessLayerException;

    /**
     * <p>Retorna una lista de unidades ejecutoras de un nivel en particular.
     *
     * @param nivel
     * @return lista de unidades ejecutoras
     * @throws DataAccessLayerException
     */
    public List<UnidadEjecutora> listarPorNivel(int nivel) throws DataAccessLayerException;

    /**
     * Retorna un lista de todos las unidades ejecutoras hijas.
     *  
     * @param padre
     * @return lista de subcategorias de padre
     * @throws DataAccessLayerException
     */
    public List<UnidadEjecutora> listarSubcategorias(UnidadEjecutora padre) throws DataAccessLayerException;

    /**
     * <p>Retorna una lista de unidades ejecutoras hijas.
     * <p>Util cuando pueden ser demasiados registros y se necesita paginacion.
     *
     * @param padre
     * @param filaInicio
     * @param cantidadRegistros
     * @return lista de subcategorias de padre
     * @throws DataAccessLayerException
     */
    public List<UnidadEjecutora> listarSubcategorias(UnidadEjecutora padre, int filaInicio, int cantidadRegistros) throws DataAccessLayerException;
}
