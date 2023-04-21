package ch.bbv.mda.persistence;

import java.io.File;
import ch.bbv.mda.MetaModel;
import com.jgoodies.validation.ValidationResult;

/**
 * The persistency handler is responsible to write and read a complete MDA
 * project. The classes implementing the interface support various persistency
 * schemes. Methods are provided to read, import, write and update projects.
 * Complex operations are realized with a concrete implementation of the
 * persistency policy.
 * @author MarcelBaumann
 * @version $Revision: 1.4 $
 */
public interface PersistenceHandler {

    /**
   * Reads a file, extracts the information and return the meta model stored in
   * the file.
   * @param file file containing the MDA model to extract
   * @param vResults containing warnings and errors discovered during reading the model
   * @return the read meta model graph
   * @throws PersistenceException A input/output error occured. The exception
   *           contains the original thrown exception
   */
    MetaModel readModel(File file, ValidationResult vResults) throws PersistenceException;

    /**
   * Writes a model to a file. The previous content of the file is discarded.
   * @param model mode to persist in the file
   * @param file file which should contain the persistent representation of the
   *          model
   * @param vResults containing warnings and errors discovered during reading the model
   * @throws PersistenceException A input/output error occured. The exception
   *           contains the original thrown exception
   */
    void writeModel(MetaModel model, File file, ValidationResult vResults) throws PersistenceException;

    /**
   * Updates the model stored in the given file.
   * @param model meta model used to update the UML model stored in the file
   * @param file file containing the model to update
   * @param vResults containing warnings and errors discovered during reading the model
   * @throws PersistenceException A input/output error occured. The exception
   *           contains the original thrown exception
   */
    void updateModel(MetaModel model, File file, ValidationResult vResults) throws PersistenceException;
}
