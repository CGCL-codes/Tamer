package com.proyecto.tropero.core.service.model.Interface;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.proyecto.tropero.core.bd.LogDTO;
import com.proyecto.tropero.core.bd.SincroDTO;
import com.proyecto.tropero.core.domain.Campo;
import com.proyecto.tropero.core.excepciones.ProcesoSincronizacionException;
import com.proyecto.tropero.core.service.IGenericService;

public interface ISincroService extends IGenericService {

    /**
	 * Retorna los datos para la sincronizacion
	 * @return
	 */
    SincroDTO getDatosSincronizacion();

    /**
	 * Graba en todas las tablas que posen el id de campo el id pasado por parametro
	 * @param id
	 * @throws SQLException 
	 */
    void actualizarId(Long id) throws SQLException;

    /**
	 * Obtiene los campos para la sincronizacion en el administrador
	 * @return
	 * @throws SQLException 
	 */
    List<Campo> getCampos() throws SQLException;

    /**
	 * sincroniza el campo Maestro
	 * @param id
	 * @throws ProcesoSincronizacionException 
	 * @throws SQLException 
	 */
    void sincronizarMaster(Integer id) throws ProcesoSincronizacionException, SQLException;

    /**
	 * trae los logs con los filtros aplicados
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param nombreUsuario
	 * @return
	 * @throws SQLException
	 */
    List<LogDTO> getLogs(Date fechaDesde, Date fechaHasta, Integer nombreUsuario) throws SQLException;

    /**
	 * trae TODOS los logs
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param nombreUsuario
	 * @return
	 * @throws SQLException
	 */
    List<LogDTO> getAllLogs() throws SQLException;

    /**
	 * RETORNA TODOS LOS LOGS DEL USUARIO
	 * @param nombreUsuario
	 * @return
	 * @throws SQLException
	 */
    List<LogDTO> getLogs(Integer nombreUsuario) throws SQLException;

    /**
	 * Retorna todos los log por fecha
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return
	 * @throws SQLException 
	 */
    List<LogDTO> getLogs(Date fechaDesde, Date fechaHasta) throws SQLException;
}
