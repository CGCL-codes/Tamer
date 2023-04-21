package info.joseluismartin.service;

import info.joseluismartin.model.TableState;
import info.joseluismartin.model.User;

/**
 * Read and Save TableStates 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface TableService {

    /**
	 * Get TableState for user and table name
	 * @param name the table name
	 * @return the table state, null if none
	 */
    TableState getState(String name);

    /**
	 * Save the given TableState
	 * @param state the state 
	 */
    void saveState(TableState state);
}
