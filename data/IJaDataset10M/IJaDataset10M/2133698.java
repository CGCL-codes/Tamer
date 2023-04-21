package ocupacion.dao;

import java.util.*;
import ocupacion.*;

/**
 *
 * @author Sergio
 */
public interface OcupacionDAO {

    public int insertar(OcupacionVO ocupacion);

    public int eliminar(int id);

    public int eliminar(String entidad, int idEntidad);

    public int actualizar(OcupacionVO ocupacion);

    public String getSQLInsertar(OcupacionVO ocupacion);

    public ArrayList<OcupacionVO> listadoOcupaciones(OcupacionParametros parametros);

    public ArrayList<OcupacionConAsistenciaPrevistaVO> listadoOcupacionesConAsistenciaPrevistaUsuario(int idUsuario, Date fecha, Date hora, int minutosIntervalo);

    public ArrayList<OcupacionConAsistenciaPrevistaVO> listadoOcupacionesConAsistenciaPrevistaEducador(int idEducador, Date fecha, Date hora, int minutosIntervalo);
}
