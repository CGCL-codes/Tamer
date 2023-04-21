package net.sf.container;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.MalformedURLException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Listener for {@link PolicyParser} callback events.
 *
 * <p>
 * This interface is abstracting the actual <code>ContainerFactory</code>
 * from the actual <code>PolicyParser</code> implementations.<br/>
 * This way one can reuse various parsing algorithms with various factory
 * implementations.
 *
 * <p>
 * Typically various <code>ContainerFactory</code> implementations would
 * implement this interface as factories are very much responsible about
 * how the policy file will be interpreted into a <code>Container</code> object.
 *
 * created on Jun 10, 2005
 * @author fiykov
 * @version $Revision: 1.1 $
 * @since
 *
 * @see net.sf.container.imp.AbstractParseListener
 */
public interface PolicyParseListener {

    /**
	 * @return the associated with this listener container factory
	 */
    public ContainerFactory getContainerFactory();

    /**
	 * callback to indicate that parsing process begins
	 */
    public void beginParsing();

    /**
	 * callback to indicate that parsing process is over
	 */
    public void endParsing();

    /**
	 * callback when parsing of a new policy file starts
	 *
	 * @param url of the policy file
	 */
    public void newPolicyFile(URL url);

    /**
	 * callback when the parsing of the policy file is over
	 */
    public void endPolicyFile();

    /**
	 * callback when new keystore definition is encountered
	 *
	 * @param ksUrl is mandatory, url is relative to the policy file location
	 * @param ksType is optional, null if not present in the policy file
	 * @throws FileNotFoundException if the keystore does not exists
	 * @throws KeyStoreException while opening the keystore
	 * @throws IOException while reading the keystore
	 * @throws NoSuchAlgorithmException while reading the certificates
	 * @throws CertificateException while reading the certificates
	 */
    public void newKeystore(String ksUrl, String ksType) throws FileNotFoundException, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException;

    /**
	 * callback when new grant definition is encountered
	 *
	 * @param signedbyAliases is optional, list of certificate aliases separated by semicolon
	 * @param codebaseUrl is optional, url pattern compatible with Policy File specification
	 */
    public void newGrant(String signedbyAliases, String codebaseUrl) throws MalformedURLException, KeyStoreException;

    /**
	 * callback when grant definition is over
	 */
    public void endGrant();

    /**
	 * callback when new permission grant is encountered
	 *
	 * @param className is mandatory, fully qualified class name
	 * @param target is mandatory depending on the permission type
	 * @param action is mandatory depending on the permission type
	 * @param signedbyAliases is optional, a list of certificate aliased
	 * separated by semicolon
	 * @throws ClassNotFoundException if the permission class if not found
	 * @throws NoSuchMethodException if the permission class does not have
	 * suitable constructor
	 * @throws SecurityException while instantiating a new permission
	 * @throws InstantiationException while instantiating a new permission
	 * @throws IllegalAccessException while instantiating a new permission
	 * @throws IllegalArgumentException while instantiating a new permission
	 * @throws InvocationTargetException while instantiating a new permission
	 */
    public void newPermission(String className, String target, String action, String signedbyAliases) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
