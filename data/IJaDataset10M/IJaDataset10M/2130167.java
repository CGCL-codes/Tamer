package gate.util.persistence;

import gate.Corpus;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.persist.PersistenceException;

/**
 * Persistence handler for {@link SerialAnalyserController}.
 * Adds handling of the corpus memeber to the {@link ControllerPersistence}
 * class
 */
public class SerialAnalyserControllerPersistence extends ControllerPersistence {

    /**
   * Populates this Persistence with the data that needs to be stored from the
   * original source object.
   */
    public void extractDataFromSource(Object source) throws PersistenceException {
        if (!(source instanceof SerialAnalyserController)) {
            throw new UnsupportedOperationException(getClass().getName() + " can only be used for " + SerialAnalyserController.class.getName() + " objects!\n" + source.getClass().getName() + " is not a " + SerialAnalyserController.class.getName());
        }
        super.extractDataFromSource(source);
        SerialAnalyserController sac = (SerialAnalyserController) source;
        corpus = PersistenceManager.getPersistentRepresentation(sac.getCorpus());
    }

    /**
   * Creates a new object from the data contained. This new object is supposed
   * to be a copy for the original object used as source for data extraction.
   */
    public Object createObject() throws PersistenceException, ResourceInstantiationException {
        SerialAnalyserController sac = (SerialAnalyserController) super.createObject();
        sac.setCorpus((Corpus) PersistenceManager.getTransientRepresentation(corpus));
        return sac;
    }

    protected Object corpus;

    static final long serialVersionUID = -4116973147963269225L;
}
