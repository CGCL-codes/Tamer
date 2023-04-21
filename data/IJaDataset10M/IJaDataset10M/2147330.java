package org.gestionabierta.dominio.persistencia.presupuesto;

import java.math.BigDecimal;
import java.util.List;
import org.gestionabierta.dominio.modelo.presupuesto.Asignacion;
import org.gestionabierta.dominio.modelo.presupuesto.CodigoPresupuestario;
import org.gestionabierta.dominio.modelo.presupuesto.UnidadEjecutora;
import org.gestionabierta.dominio.persistencia.IGenericDao;
import org.gestionabierta.utilidades.excepciones.DataAccessLayerException;

/**
 *
 * @author Franky Villadiego
 */
public interface IAsignacionDao extends IGenericDao {

    public Asignacion traerAsignacion(UnidadEjecutora unidadEjecutora, CodigoPresupuestario codigoPresupuestario) throws DataAccessLayerException;

    public boolean existeAsignacion(UnidadEjecutora unidadEjecutora, CodigoPresupuestario codigoPresupuestario) throws DataAccessLayerException;

    public List<Asignacion> listarAsignacionesPorUnidadEjecutora(UnidadEjecutora unidadEjecutora) throws DataAccessLayerException;

    public List<Asignacion> listarAsignacionesPorCodigoPresupuestario(CodigoPresupuestario codigoPresupuestario) throws DataAccessLayerException;

    public BigDecimal montoAsignadoPorUnidadEjecutora(UnidadEjecutora unidadEjecutora) throws DataAccessLayerException;

    public BigDecimal montoAsignadoPorCodigoPresupuestario(CodigoPresupuestario codigoPresupuestario) throws DataAccessLayerException;

    public BigDecimal montoTotalAsignado() throws DataAccessLayerException;
}
