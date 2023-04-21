package ebadat.dao;

import ebadat.domain.Datos;
import java.util.List;

/**
 *
 * @author marisol
 */
public interface DatosDAO {

    List<Datos> findByTipoDato(String tipoDato);
}
