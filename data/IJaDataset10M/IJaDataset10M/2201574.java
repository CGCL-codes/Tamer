package org.garuda.sbsi.simulations.api;

/**
 * A Java interface for the Garuda simulation service. For the specification of the 
 *  interface, see <br/>
 *   https://docs.google.com/document/d/1GLehwPsTb4zghlB9gWpkbOaF0YUE6D6MaHGNbjLqecQ/edit?hl=en_US
 * @author radams
 *
 */
public interface IGarudaSimulationService {

    /**
	 * 
	 * @param model A <code>String</code> of a non-null, valid SBML level 2 model.
	 * @param SEDML A <code>String</code> of a valid Level 1 version 1 SED-ML document describing
	 *  the simulation experiment.
	 * @return A <code>String</code> identifier for tracking progress of the 
	 *   simulation job. If the job could not be launched, will return an identifier starting with the string 'ERROR'. <br/>
	 *   This identifier can be used to the get the log file via <code>getLog(String id)</code> for further information
	 *    on why simulation may have failed to start. 
	 */
    String simulate(String model, String SEDML);

    /**
	 * Queries the progress of the simulation.
	 * @param id A non-null identifier obtained following a call to simulate()
	 * @return A <code>String</code> from  an enumeration of <ul>
	 * <li> RUNNING - job is running.
	 * <li> TERMINATED_SUCCESS - job has completed, results are available.
	 * <li> TERMINATED_FAILURE - job has failed to start or terminated with error.
	 * <li> UNKNOWN - job identifier was not recognized.
	 * </ul>
	 */
    String getStatus(String id);

    /**
	 * Retrieves a log-file of the simulation results. This method can be called at any time,
	 * and the format of the log file is implementation specific.
	 *  @param id A non-null identifier obtained following a call to simulate()
	 * @return A possibly empty but non-null <code>String</code> of a logging message.
	 */
    String getLog(String id);

    /**
	 * Returns an indication of progress of the running simulation. 
	 * @param id A valid job identifier
	 * @return An integer X, where  -1 < X < 100. <br/>
	 * If <code>getStatus()</code> returns
	 *    {@link StatusStringConstants#STATUS_RUNNING}, 0&lt;X&lt;99. <br/> 
	 * If <code>getStatus()</code> returns
	 *    {@link StatusStringConstants#STATUS_TERMINATED_SUCCESS}, X=100. <br/>
	 *  If <code>getStatus()</code> returns
	 *    {@link StatusStringConstants#STATUS_TERMINATED_FAILURE}, X=-1.
	 */
    int getProgress(String id);

    /**
	 * Returns a String of a single time series result, in CSV format. <p/>
	 * Results are only guaranteed to be returned if the job's status is {@link StatusStringConstants#STATUS_TERMINATED_SUCCESS}
	 * @param id A valid job identifier.
	 * @return A CSV delimited file for a single time course, or an empty String
	 *  if no results were obtained.
	 */
    String getResults(String id);
}
