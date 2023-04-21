package modelo;

import modelo.persistencia.JDBC.NaveDAOJDBC;
import controlador.NaveController;
import modelo.entidades.Nave;
import modelo.persistencia.GenericDAO;

/**
 *
 * @author carlos
 *
 */
public class NaveModelImpl extends AbstractModelImpl<NaveController, Nave, String> implements NaveModel {

    protected GenericDAO obtenerImplementacionDAO() {
        return new NaveDAOJDBC();
    }
}
