package edu.ucdavis.genomics.metabolomics.binbase.bci.ejb;

import javax.ejb.Remote;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import edu.ucdavis.genomics.metabolomics.binbase.bci.authentification.AuthentificationException;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.exception.SendingException;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.DSL;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.Experiment;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentClass;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.util.SchedulingException;

@Remote
@WebService
@SOAPBinding(style = Style.RPC)
public interface Scheduler {

    /**
	 * imports the given class
	 * 
	 * @param clazz
	 * @param key
	 * @throws AuthentificationException 
	 * @throws SendingException 
	 * @throws SchedulingException 
	 */
    public abstract void scheduleImport(ExperimentClass clazz, String key) throws AuthentificationException, SendingException, SchedulingException;

    /**
	 * exports the given experiment
	 * 
	 * @param experiment
	 * @param key
	 * @throws AuthentificationException
	 * @throws SendingException 
	 * @throws SchedulingException 
	 */
    public abstract void scheduleExport(Experiment experiment, String key) throws AuthentificationException, SendingException, SchedulingException;

    /**
	 * schedules a dsl calculation
	 * @param dslContent
	 * @param key
	 * @throws AuthentificationException
	 * @throws SendingException
	 * @throws SchedulingException
	 */
    public abstract void scheduleDSL(DSL dsl, String key) throws AuthentificationException, SendingException, SchedulingException;

    /**
	 * provides you with a way to calculate a calculation with a defined priority which needs to be > 1
	 * @param classes
	 * @param experiment
	 * @param priority
	 * @param key
	 * @throws AuthentificationException
	 * @throws SendingException
	 * @throws SchedulingException
	 */
    public abstract void schedulePriorityCalculation(Experiment experiment, int priority, String key) throws AuthentificationException, SendingException, SchedulingException;
}
