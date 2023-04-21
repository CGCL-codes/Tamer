package eu.more.keydistributionservice;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import org.soda.dpws.DPWSException;
import eu.more.keydistributionservice.internal.ioOperations.IOOperations;
import eu.more.keydistributionservice.internal.serialization.Serialization;

public class KeyDistributionManagment {

    protected static Hashtable<String, String> symmetricKeysTable = new Hashtable<String, String>();

    protected static Hashtable<String, X509Certificate> caTable = new Hashtable<String, X509Certificate>();

    /**
	 * Clear internal caches
	 */
    public static void initialize() {
        symmetricKeysTable.clear();
        caTable.clear();
    }

    private static class Container implements Serializable {

        private static final long serialVersionUID = 1300821854939229514L;

        public Hashtable<String, String> symmetricKeysTable;

        public Hashtable<String, X509Certificate> caTable;

        public Container(Hashtable<String, String> sks, Hashtable<String, X509Certificate> cas) {
            this.caTable = cas;
            this.symmetricKeysTable = sks;
        }
    }

    /**
	 * Stores the internal state into the file given by the path
	 * @param path
	 * @throws DPWSException 
	 */
    public static void store(String path) throws DPWSException {
        String serializedTable = Serialization.serialize(new Container(symmetricKeysTable, caTable));
        IOOperations.store(serializedTable, path);
    }

    /**
	 * Constructor: Builds a new CryptoService instance with the information
	 * stored in the file
	 * @param path
	 * @throws DPWSException 
	 */
    public static void load(String path) throws DPWSException {
        String serializedTable = IOOperations.loadToString(path);
        Container c = (Container) Serialization.deserialize(serializedTable);
        symmetricKeysTable = c.symmetricKeysTable;
        caTable = c.caTable;
    }

    /**
	 * Constructor: Do nothing.
	 */
    public KeyDistributionManagment() {
    }
}
