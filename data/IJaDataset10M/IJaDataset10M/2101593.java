package it.javalinux.wise.core.client;

import it.javalinux.wise.core.exceptions.WiseConnectionException;
import it.javalinux.wise.core.exceptions.WiseException;
import it.javalinux.wise.core.utils.IDGenerator;
import it.javalinux.wise.core.utils.IOUtils;
import it.javalinux.wise.core.utils.WiseProperties;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Encoder;

/**
 * WSDynamicClientFactory is a singleton containing a WSDynamicClient cache.
 * It's able to create WSDynamicCient objects, and init then using
 * WiseProperties using wise-core.properties find in classpath as default. This
 * default properties may be overridden using setWiseProperties method.
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 * 
 */
@ThreadSafe
public class WSDynamicClientFactory {

    private static WSDynamicClientFactory me = null;

    private Map<String, WSDynamicClient> clientsMap = new HashMap<String, WSDynamicClient>();

    private WiseProperties wiseProperties;

    private WSDynamicClientFactory() throws WiseException {
        wiseProperties = new WiseProperties("wise-core.properties");
    }

    /**
     * 
     * This method return the singleton instance
     * 
     * @return an Instance of this singleton class
     * @throws WiseException
     */
    public static synchronized WSDynamicClientFactory getInstace() throws WiseException {
        if (me == null) {
            me = new WSDynamicClientFactory();
        }
        return me;
    }

    /**
     * Remove all clients from cache Map
     */
    public void clearCache() {
        this.clientsMap.clear();
    }

    /**
     * Remove an element from cache
     * 
     * @param serviceName
     * @return the client just removed
     */
    public WSDynamicClient removeFromCache(String serviceName) {
        return clientsMap.remove(serviceName);
    }

    /**
     * Return an instance of WSDynamicClient taken from cache if possible,
     * generate and initialise if not.
     * 
     * @param wsdlURL
     *            The URL to retrive wsdl of webservice called
     * @param name
     *            a symbolic name to cache wsdl and generated classes
     * @param userName
     *            we support HTTP BASIC Auth protected wsdls: this is username
     *            used for authentication
     * @param password
     *            we support HTTP BASIC Auth protected wsdls: this is password
     *            used for authentication
     * @return an instance of WSDynamicClient already initialized, ready to call
     *         endpoints
     * @throws IllegalArgumentException
     *             thrown in case of name ord wsdlURL parameter is null
     * @throws WiseConnectionException
     *             thrown in case wsdl isn't accessible at given URL
     */
    public WSDynamicClient getClient(String wsdlURL, String name, String userName, String password) throws IllegalArgumentException, WiseConnectionException {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (wsdlURL == null) {
            throw new IllegalArgumentException("wsdlURL cannot be null");
        }
        WSDynamicClient client = clientsMap.get(name);
        if (client == null) {
            String usableWsdl = wsdlURL;
            if (wsdlURL != null && wsdlURL.startsWith("http://")) {
                usableWsdl = this.getUsableWSDL(wsdlURL, userName, password);
            }
            client = new WSDynamicClient(wiseProperties);
            client.init(usableWsdl, name, userName, password);
            clientsMap.put(name, client);
        }
        return client;
    }

    /**
     * Set a different WisePropeties for this Factory. Note that this
     * WiseProperties will be passed to all WSDynamicClient created and cached
     * will be create and initialised after this call. WSDynamicClient already
     * created and cached into cache map will not be affected.
     * 
     * @see #clearCache() method to have a fresh situation.
     * 
     * @param wiseProperties
     */
    public void setWiseProperties(WiseProperties wiseProperties) {
        this.wiseProperties = wiseProperties;
    }

    /**
     * Gets a WSDL given its url and userName/password if needed.
     * 
     * @param wsdlURL
     *            The wsdl url
     * @param userName
     *            The username; empty string and null mean no username
     * @param password
     *            The password; empty string and null mean no password
     * @return The path to the temp file containing the requested wsdl
     * @throws WiseConnectionException
     *             If an error occurs while downloading the wsdl
     */
    String getUsableWSDL(String wsdlURL, String userName, String password) throws WiseConnectionException {
        if (StringUtils.trimToNull(userName) == null || StringUtils.trimToNull(password) == null) {
            return this.transferWSDL(wsdlURL, null);
        } else {
            return this.transferWSDL(wsdlURL, new StringBuffer(userName).append(":").append(password).toString());
        }
    }

    private String transferWSDL(String wsdlURL, String userPassword) throws WiseConnectionException {
        String filePath = null;
        try {
            URL endpoint = new URL(wsdlURL);
            HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            conn.setRequestProperty("Connection", "close");
            if (userPassword != null) {
                conn.setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode(userPassword.getBytes()));
            }
            InputStream is = null;
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                StringWriter sw = new StringWriter();
                char[] buf = new char[200];
                int read = 0;
                while (read != -1) {
                    read = isr.read(buf);
                    sw.write(buf);
                }
                throw new WiseConnectionException("Remote server's response is an error: " + sw.toString());
            }
            File outputDir = new File(wiseProperties.getProperty("wise.tmpDir"));
            if (!outputDir.exists()) {
                outputDir.mkdir();
                wiseProperties.setProperty("wise.forceImportObject", "true");
            }
            File file = new File(wiseProperties.getProperty("wise.tmpDir"), new StringBuffer("Wise").append(IDGenerator.nextVal()).append(".xml").toString());
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.copyStream(fos, is);
            fos.close();
            is.close();
            filePath = file.getPath();
        } catch (WiseConnectionException wce) {
            throw wce;
        } catch (Exception e) {
            throw new WiseConnectionException("Wsdl download failed!", e);
        }
        return filePath;
    }
}
