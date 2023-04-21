package simulator.manager.boat.rudder;

import simulator.manager.boat.BoatComponentManager;

/**
 * 
 * 
 * @author Mariano Tepper
 */
public interface RudderManager extends BoatComponentManager {

    /** Gira el timon la cantidad de grados especificada.
	 * Tambien se podria usar como para setear la direccion en tantos grados.
	 * @param degrees - la cantidad de grados que se quiere girar el timon
	 * @return true si se pudo realiza, false si no.
	 */
    void turn(int degrees);
}
