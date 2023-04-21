package org.fcrepo.client.utility.validate.process;

import java.io.IOException;
import java.util.Iterator;
import javax.xml.rpc.ServiceException;
import org.fcrepo.client.utility.validate.ObjectSourceException;
import org.fcrepo.client.utility.validate.ObjectValidator;
import org.fcrepo.client.utility.validate.ValidationResults;
import org.fcrepo.client.utility.validate.process.ValidatorProcessParameters.IteratorType;
import org.fcrepo.client.utility.validate.remote.RemoteObjectSource;
import org.fcrepo.client.utility.validate.remote.ServiceInfo;

/**
 * A command-line utility that validates objects in a remote repository,
 * selected by criteria. See the javadoc for {@link ValidatorProcessParameters}
 * for the usage details.
 *
 * @author Jim Blake
 */
public class ValidatorProcess {

    /**
     * Open the connection to the Fedora server.
     */
    private static RemoteObjectSource openObjectSource(ServiceInfo serviceInfo) {
        try {
            return new RemoteObjectSource(serviceInfo);
        } catch (ServiceException e) {
            throw new IllegalStateException("Failed to initialize " + "the ValidatorProcess: ", e);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize " + "the ValidatorProcess: ", e);
        }
    }

    /**
     * The list of PIDs may come from a file, or from a query against the object
     * source.
     */
    private static Iterator<String> getPidIterator(ValidatorProcessParameters parms, RemoteObjectSource objectSource) throws ObjectSourceException {
        if (parms.getIteratorType() == IteratorType.FS_QUERY) {
            return objectSource.findObjectPids(parms.getQuery());
        } else {
            return new PidfileIterator(parms.getPidfile());
        }
    }

    public static void main(String[] args) throws ObjectSourceException {
        System.setProperty("java.awt.headless", "true");
        try {
            ValidatorProcessParameters parms = new ValidatorProcessParameters(args);
            RemoteObjectSource objectSource = openObjectSource(parms.getServiceInfo());
            ValidationResults results = new SimpleValidationResults(parms.getVerbose());
            Iterator<String> pids = getPidIterator(parms, objectSource);
            ObjectValidator validator = new ObjectValidator(objectSource);
            while (pids.hasNext()) {
                results.record(validator.validate(pids.next()));
            }
            results.closeResults();
        } catch (ValidatorProcessUsageException e) {
            System.err.println(e.getMessage());
        }
    }
}
