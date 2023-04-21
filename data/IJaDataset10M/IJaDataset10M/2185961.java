package AdaptadoresSistemaStock;

import Excepciones.ExcepcionErrorConexion;
import Excepciones.ExcepcionSistemaStock;
import Persistencia.Entidades.OrdenTrabajo;

/**
 *
 * @author rustu
 */
public interface AdaptadorSistemaStock {

    public void confirmarStock(OrdenTrabajo orden) throws ExcepcionSistemaStock, ExcepcionErrorConexion;
}
