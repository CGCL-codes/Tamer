package unbbayes.io.mebn;

import unbbayes.io.mebn.exceptions.IOMebnException;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * This interface is mainly used by classes which shall have some knowledge about
 * what OWL Model (class which stores OWL ontology informations) is being
 * used by currently running process, but details should be hidden.
 * For example, a MEBN might need to know the last OWL Model used to load an
 * ontology, in order to perform "save current" action; or to store 
 * whole parts of OWL ontology "in-memory" so that, when saving, reusing
 * the stored OWL model allows us to store also the .
 * @author Shou Matsumoto
 *
 */
public interface IProtegeOWLModelUser {

    /**
	 * 
	 * @return the last used OWL model
	 */
    public OWLModel getLastOWLModel();

    /**
	 * 
	 * @param model OWLModel to set
	 * @throws IOMebnException
	 */
    public void setOWLModelToUse(OWLModel model) throws IOMebnException;
}
