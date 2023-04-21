package java.security;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import java.security.InvalidParameterException;
import sun.security.util.Debug;
import sun.security.util.PropertyExpander;

/**
 * <p>This class centralizes all security properties and common security
 * methods. One of its primary uses is to manage providers.
 *
 * @author Benjamin Renaud
 * @version 1.101 10/17/00
 */
public final class Security {

    private static boolean reloadProviders = true;

    static final boolean debug = false;

    private static final Debug sdebug = Debug.getInstance("properties");

    static final boolean error = true;

    private static Properties props;

    private static Vector providers;

    private static Hashtable providerPropertiesCache;

    private static Hashtable engineCache;

    private static Hashtable searchResultsCache;

    private static Hashtable providerLoads;

    private static class ProviderProperty {

        String className;

        Provider provider;
    }

    private static int numOfStaticProviders = 0;

    private static Vector providerMasterClassNames = new Vector(6);

    private static int indexStaticProviders = 0;

    private static boolean resetProviderIndex = false;

    static {
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                initialize();
                return null;
            }
        });
    }

    private static void initialize() {
        props = new Properties();
        providers = new Vector();
        providerPropertiesCache = new Hashtable();
        engineCache = new Hashtable();
        searchResultsCache = new Hashtable(5);
        providerLoads = new Hashtable(1);
        boolean loadedProps = false;
        boolean overrideAll = false;
        File propFile = securityPropFile("java.security");
        if (propFile.exists()) {
            try {
                FileInputStream is = new FileInputStream(propFile);
                props.load(is);
                is.close();
                loadedProps = true;
                if (sdebug != null) {
                    sdebug.println("reading security properties file: " + propFile);
                }
            } catch (IOException e) {
                if (sdebug != null) {
                    sdebug.println("unable to load security properties from " + propFile);
                    e.printStackTrace();
                }
            }
        }
        if ("true".equalsIgnoreCase(props.getProperty("security.overridePropertiesFile"))) {
            String extraPropFile = System.getProperty("java.security.properties");
            if (extraPropFile != null && extraPropFile.startsWith("=")) {
                overrideAll = true;
                extraPropFile = extraPropFile.substring(1);
            }
            if (overrideAll) {
                props = new Properties();
                if (sdebug != null) {
                    sdebug.println("overriding other security properties files!");
                }
            }
            if (extraPropFile != null) {
                try {
                    URL propURL;
                    extraPropFile = PropertyExpander.expand(extraPropFile);
                    propFile = new File(extraPropFile);
                    if (propFile.exists()) {
                        propURL = new URL("file:" + propFile.getCanonicalPath());
                    } else {
                        propURL = new URL(extraPropFile);
                    }
                    BufferedInputStream bis = new BufferedInputStream(propURL.openStream());
                    props.load(bis);
                    bis.close();
                    loadedProps = true;
                    if (sdebug != null) {
                        sdebug.println("reading security properties file: " + propURL);
                        if (overrideAll) {
                            sdebug.println("overriding other security properties files!");
                        }
                    }
                } catch (Exception e) {
                    if (sdebug != null) {
                        sdebug.println("unable to load security properties from " + extraPropFile);
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!loadedProps) {
            initializeStatic();
            if (sdebug != null) {
                sdebug.println("unable to load security properties " + "-- using defaults");
            }
        }
        countProviders();
    }

    private static void initializeStatic() {
        props.put("security.provider.1", "sun.security.provider.Sun");
    }

    /**
     * Don't let anyone instantiate this. 
     */
    private Security() {
    }

    /**
     * Loops through provider declarations, which are expected to be
     * of the form:
     *
     * security.provider.1=sun.security.provider.Sun
     * security.provider.2=sun.security.jsafe.Jsafe
     * etc.
     *
     * The order determines the default search order when looking for 
     * an algorithm.
     */
    private static synchronized void countProviders() {
        int i = 1;
        while (true) {
            String name = props.getProperty("security.provider." + i);
            if (name == null) {
                break;
            } else {
                String fullClassName = name.trim();
                if (fullClassName.length() == 0) {
                    System.err.println("invalid entry for " + "security.provider." + i);
                    break;
                } else {
                    if (!providerMasterClassNames.contains(fullClassName)) {
                        providerMasterClassNames.add(fullClassName);
                    }
                    i++;
                }
            }
        }
        numOfStaticProviders = providerMasterClassNames.size();
    }

    private static synchronized void reloadProviders() {
        if (reloadProviders) {
            sun.misc.Launcher l = sun.misc.Launcher.getLauncher();
            if (l != null) {
                synchronized (Security.class) {
                    reloadProviders = false;
                    indexStaticProviders = numOfStaticProviders;
                    resetProviderIndex = false;
                    providers.removeAllElements();
                    int i = 0;
                    while (i < numOfStaticProviders) {
                        final String name = (String) providerMasterClassNames.elementAt(i);
                        i++;
                        Provider prov = (Provider) AccessController.doPrivileged(new PrivilegedAction() {

                            public Object run() {
                                return Provider.loadProvider(name);
                            }
                        });
                        if (prov != null) {
                            providers.addElement(prov);
                        }
                    }
                    providerPropertiesCache.clear();
                    engineCache.clear();
                    searchResultsCache.clear();
                }
            }
        }
    }

    /**
     * Try our best to load one more statically registered provider.
     * This is used by getEngineClassName(String algName, String engineType).
     */
    private static synchronized void loadOneMoreProvider() {
        boolean restore = false;
        if (reloadProviders) {
            restore = true;
            reloadProviders = false;
        }
        try {
            sun.misc.Launcher l = sun.misc.Launcher.getLauncher();
            if (indexStaticProviders >= numOfStaticProviders) {
                return;
            }
            Provider prov = null;
            while (indexStaticProviders < numOfStaticProviders) {
                final String name = (String) providerMasterClassNames.elementAt(indexStaticProviders);
                if (providerLoads.get(name) != null) {
                    indexStaticProviders++;
                    continue;
                } else {
                    providerLoads.put(name, name);
                }
                prov = (Provider) AccessController.doPrivileged(new PrivilegedAction() {

                    public Object run() {
                        return Provider.loadProvider(name);
                    }
                });
                indexStaticProviders++;
                providerLoads.remove(name);
                if (prov != null) {
                    providers.addElement(prov);
                    providerPropertiesCache.clear();
                    engineCache.clear();
                    searchResultsCache.clear();
                    break;
                } else {
                    if (l == null) {
                        resetProviderIndex = true;
                    }
                }
            }
        } finally {
            if (restore) {
                reloadProviders = true;
            }
        }
    }

    private static File securityPropFile(String filename) {
        String sep = File.separator;
        return new File(System.getProperty("java.home") + sep + "lib" + sep + "security" + sep + filename);
    }

    /**
     * Looks up providers, and returns the property (and its associated
     * provider) mapping the key, if any.
     * The order in which the providers are looked up is the
     * provider-preference order, as specificed in the security
     * properties file.
     */
    private static ProviderProperty getProviderProperty(String key) {
        ProviderProperty entry = (ProviderProperty) providerPropertiesCache.get(key);
        if (entry != null) {
            return entry;
        }
        for (int i = 0; i < providers.size(); i++) {
            String matchKey = null;
            Provider prov = (Provider) providers.elementAt(i);
            String prop = prov.getProperty(key);
            if (prop == null) {
                for (Enumeration enum_ = prov.keys(); enum_.hasMoreElements() && prop == null; ) {
                    matchKey = (String) enum_.nextElement();
                    if (key.equalsIgnoreCase(matchKey)) {
                        prop = prov.getProperty(matchKey);
                        break;
                    }
                }
            }
            if (prop != null) {
                ProviderProperty newEntry = new ProviderProperty();
                newEntry.className = prop;
                newEntry.provider = prov;
                providerPropertiesCache.put(key, newEntry);
                if (matchKey != null) {
                    providerPropertiesCache.put(matchKey, newEntry);
                }
                return newEntry;
            }
        }
        return entry;
    }

    /**
     * Returns the property (if any) mapping the key for the given provider.
     */
    private static String getProviderProperty(String key, Provider provider) {
        String prop = provider.getProperty(key);
        if (prop == null) {
            for (Enumeration enum_ = provider.keys(); enum_.hasMoreElements() && prop == null; ) {
                String matchKey = (String) enum_.nextElement();
                if (key.equalsIgnoreCase(matchKey)) {
                    prop = provider.getProperty(matchKey);
                    break;
                }
            }
        }
        return prop;
    }

    /**
     * We always map names to standard names
     */
    private static String getStandardName(String alias, String engineType, Provider prov) {
        return getProviderProperty("Alg.Alias." + engineType + "." + alias, prov);
    }

    private static ProviderProperty getEngineClassName(String algName, String engineType) throws NoSuchAlgorithmException {
        ProviderProperty pp;
        String key = engineType;
        if (algName != null) key += "." + algName;
        pp = (ProviderProperty) engineCache.get(key);
        if (pp != null) return pp;
        synchronized (Security.class) {
            sun.misc.Launcher l = sun.misc.Launcher.getLauncher();
            if ((reloadProviders == true) && (l != null) && (resetProviderIndex == true)) {
                resetProviderIndex = false;
                indexStaticProviders = 0;
                providers.removeAllElements();
                providerPropertiesCache.clear();
                engineCache.clear();
                searchResultsCache.clear();
                providerLoads.clear();
            }
            if (providers.size() == 0) {
                loadOneMoreProvider();
            }
            for (int i = 0; i < providers.size(); i++) {
                Provider prov = (Provider) providers.elementAt(i);
                try {
                    pp = getEngineClassName(algName, prov, engineType);
                } catch (NoSuchAlgorithmException e) {
                    if (i == providers.size() - 1) {
                        loadOneMoreProvider();
                    }
                    continue;
                }
                engineCache.put(key, pp);
                return pp;
            }
        }
        throw new NoSuchAlgorithmException(algName.toUpperCase() + " " + engineType + " not available");
    }

    private static ProviderProperty getEngineClassName(String algName, String provider, String engineType) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (provider == null) {
            return getEngineClassName(algName, engineType);
        }
        Provider prov = getProvider(provider);
        if (prov == null) {
            throw new NoSuchProviderException("no such provider: " + provider);
        }
        return getEngineClassName(algName, prov, engineType);
    }

    /**
     * The parameter provider cannot be null.
     */
    private static ProviderProperty getEngineClassName(String algName, Provider provider, String engineType) throws NoSuchAlgorithmException {
        String key;
        if (engineType.equalsIgnoreCase("SecureRandom") && algName == null) key = engineType; else key = engineType + "." + algName;
        String className = getProviderProperty(key, provider);
        if (className == null) {
            if (engineType.equalsIgnoreCase("SecureRandom") && algName == null) throw new NoSuchAlgorithmException("SecureRandom not available for provider " + provider.getName()); else {
                String stdName = getStandardName(algName, engineType, provider);
                if (stdName != null) key = engineType + "." + stdName;
                if ((stdName == null) || (className = getProviderProperty(key, provider)) == null) throw new NoSuchAlgorithmException("no such algorithm: " + algName + " for provider " + provider.getName());
            }
        }
        ProviderProperty entry = new ProviderProperty();
        entry.className = className;
        entry.provider = provider;
        return entry;
    }

    /**
     * Adds a new provider, at a specified position. The position is
     * the preference order in which providers are searched for
     * requested algorithms. Note that it is not guaranteed that this
     * preference will be respected. The position is 1-based, that is,
     * 1 is most preferred, followed by 2, and so on.
     * 
     * <p>If the given provider is installed at the requested position,
     * the provider that used to be at that position, and all providers
     * with a position greater than <code>position</code>, are shifted up
     * one position (towards the end of the list of installed providers).
     * 
     * <p>A provider cannot be added if it is already installed.
     * 
     * <p>First, if there is a security manager, its
     * <code>checkSecurityAccess</code> 
     * method is called with the string
     * <code>"insertProvider."+provider.getName()</code> 
     * to see if it's ok to add a new provider. 
     * If the default implementation of <code>checkSecurityAccess</code> 
     * is used (i.e., that method is not overriden), then this will result in
     * a call to the security manager's <code>checkPermission</code> method
     * with a
     * <code>SecurityPermission("insertProvider."+provider.getName())</code>
     * permission.
     *
     * @param provider the provider to be added.
     *
     * @param position the preference position that the caller would
     * like for this provider.
     * 
     * @return the actual preference position in which the provider was 
     * added, or -1 if the provider was not added because it is
     * already installed.
     *
     * @throws  SecurityException
     *          if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkSecurityAccess}</code> method
     *          denies access to add a new provider
     *
     * @see #getProvider
     * @see #removeProvider 
     * @see java.security.SecurityPermission
     */
    public static synchronized int insertProviderAt(Provider provider, int position) {
        reloadProviders();
        check("insertProvider." + provider.getName());
        Provider already = getProvider(provider.getName());
        if (already != null) {
            return -1;
        }
        int size = providers.size();
        if (position > size || position <= 0) {
            position = size + 1;
        }
        providers.insertElementAt(provider, position - 1);
        providerPropertiesCache.clear();
        engineCache.clear();
        searchResultsCache.clear();
        return position;
    }

    /**
     * Adds a provider to the next position available.
     *
     * <p>First, if there is a security manager, its
     * <code>checkSecurityAccess</code> 
     * method is called with the string
     * <code>"insertProvider."+provider.getName()</code> 
     * to see if it's ok to add a new provider. 
     * If the default implementation of <code>checkSecurityAccess</code> 
     * is used (i.e., that method is not overriden), then this will result in
     * a call to the security manager's <code>checkPermission</code> method
     * with a
     * <code>SecurityPermission("insertProvider."+provider.getName())</code>
     * permission.
     * 
     * @param provider the provider to be added.
     *
     * @return the preference position in which the provider was 
     * added, or -1 if the provider was not added because it is
     * already installed.
     *
     * @throws  SecurityException
     *          if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkSecurityAccess}</code> method
     *          denies access to add a new provider
     * 
     * @see #getProvider
     * @see #removeProvider
     * @see java.security.SecurityPermission
     */
    public static int addProvider(Provider provider) {
        return insertProviderAt(provider, 0);
    }

    /**
     * Removes the provider with the specified name.
     *
     * <p>When the specified provider is removed, all providers located
     * at a position greater than where the specified provider was are shifted
     * down one position (towards the head of the list of installed
     * providers).
     *
     * <p>This method returns silently if the provider is not installed.
     * 
     * <p>First, if there is a security manager, its
     * <code>checkSecurityAccess</code> 
     * method is called with the string <code>"removeProvider."+name</code> 
     * to see if it's ok to remove the provider. 
     * If the default implementation of <code>checkSecurityAccess</code> 
     * is used (i.e., that method is not overriden), then this will result in
     * a call to the security manager's <code>checkPermission</code> method
     * with a <code>SecurityPermission("removeProvider."+name)</code>
     * permission.
     *
     * @param name the name of the provider to remove.
     *
     * @throws  SecurityException
     *          if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkSecurityAccess}</code> method
     *          denies
     *          access to remove the provider
     *
     * @see #getProvider
     * @see #addProvider
     */
    public static synchronized void removeProvider(String name) {
        reloadProviders();
        check("removeProvider." + name);
        Provider provider = getProvider(name);
        if (provider != null) {
            for (Iterator i = providers.iterator(); i.hasNext(); ) if (i.next() == provider) i.remove();
            providerPropertiesCache.clear();
            engineCache.clear();
            searchResultsCache.clear();
        }
    }

    /**
     * Returns an array containing all the installed providers. The order of
     * the providers in the array is their preference order.
     * 
     * @return an array of all the installed providers.
     */
    public static synchronized Provider[] getProviders() {
        reloadProviders();
        Provider[] result = new Provider[providers.size()];
        providers.copyInto(result);
        return result;
    }

    /**
     * Returns the provider installed with the specified name, if
     * any. Returns null if no provider with the specified name is
     * installed.
     * 
     * @param name the name of the provider to get.
     * 
     * @return the provider of the specified name.
     *
     * @see #removeProvider
     * @see #addProvider
     */
    public static synchronized Provider getProvider(String name) {
        reloadProviders();
        Enumeration enum_ = providers.elements();
        while (enum_.hasMoreElements()) {
            Provider prov = (Provider) enum_.nextElement();
            if (prov.getName().equals(name)) {
                return prov;
            }
        }
        return null;
    }

    /**
    * Returns an array containing all installed providers that satisfy the
    * specified selection criterion, or null if no such providers have been
    * installed. The returned providers are ordered
    * according to their <a href=
    * "#insertProviderAt(java.security.Provider, int)">preference order</a>. 
    * 
    * <p> A cryptographic service is always associated with a particular
    * algorithm or type. For example, a digital signature service is
    * always associated with a particular algorithm (e.g., DSA),
    * and a CertificateFactory service is always associated with
    * a particular certificate type (e.g., X.509).
    * NOTE: <B>java.security.cert.CertificateFactory</B> is found in J2ME CDC 
    * profiles such as J2ME Foundation Profile.
    *
    * <p>The selection criterion must be specified in one of the following two formats:
    * <ul>
    * <li> <i>&lt;crypto_service>.&lt;algorithm_or_type></i> <p> The
    * cryptographic service name must not contain any dots.
    * <p> A 
    * provider satisfies the specified selection criterion iff the provider implements the 
    * specified algorithm or type for the specified cryptographic service.
    * <p> For example, "CertificateFactory.X.509" 
    * would be satisfied by any provider that supplied
    * a CertificateFactory implementation for X.509 certificates.
    * NOTE: <B>java.security.cert.CertificateFactory</B> is found in J2ME CDC 
    * profiles such as J2ME Foundation Profile.
    * <li> <i>&lt;crypto_service>.&lt;algorithm_or_type> &lt;attribute_name>:&lt attribute_value></i>
    * <p> The cryptographic service name must not contain any dots. There
     * must be one or more space charaters between the the <i>&lt;algorithm_or_type></i>
     * and the <i>&lt;attribute_name></i>.
    * <p> A provider satisfies this selection criterion iff the
    * provider implements the specified algorithm or type for the specified 
    * cryptographic service and its implementation meets the
    * constraint expressed by the specified attribute name/value pair.
    * <p> For example, "Signature.SHA1withDSA KeySize:1024" would be
    * satisfied by any provider that implemented
    * the SHA1withDSA signature algorithm with a keysize of 1024 (or larger).
    * NOTE: <B>java.security.Signature</B> is found in J2ME CDC profiles such as 
    * J2ME Foundation Profile.
    *  
    * </ul>
    *
    * <p> See Appendix A in the <a href=
    * "../../../guide/security/CryptoSpec.html#AppA">
    * Java Cryptogaphy Architecture API Specification &amp; Reference </a>
    * for information about standard cryptographic service names, standard
    * algorithm names and standard attribute names.
    *
    * @param filter the criterion for selecting
    * providers. The filter is case-insensitive.
    *
    * @return all the installed providers that satisfy the selection
    * criterion, or null if no such providers have been installed.
    *
    * @throws InvalidParameterException
    *         if the filter is not in the required format
    *
    * @see #getProviders(java.util.Map)
    */
    public static Provider[] getProviders(String filter) {
        String key = null;
        String value = null;
        int index = filter.indexOf(':');
        if (index == -1) {
            key = new String(filter);
            value = "";
        } else {
            key = filter.substring(0, index);
            value = filter.substring(index + 1);
        }
        Hashtable hashtableFilter = new Hashtable(1);
        hashtableFilter.put(key, value);
        return (getProviders(hashtableFilter));
    }

    /**
     * Returns an array containing all installed providers that satisfy the specified
     * selection criteria, or null if no such providers have been installed. 
     * The returned providers are ordered
     * according to their <a href=
     * "#insertProviderAt(java.security.Provider, int)">preference order</a>. 
     * 
     * <p>The selection criteria are represented by a map.
     * Each map entry represents a selection criterion. 
     * A provider is selected iff it satisfies all selection
     * criteria. The key for any entry in such a map must be in one of the
     * following two formats:
     * <ul>
     * <li> <i>&lt;crypto_service>.&lt;algorithm_or_type></i>
     * <p> The cryptographic service name must not contain any dots.
     * <p> The value associated with the key must be an empty string.
     * <p> A provider
     * satisfies this selection criterion iff the provider implements the 
     * specified algorithm or type for the specified cryptographic service.
     * <li>  <i>&lt;crypto_service>.&lt;algorithm_or_type> &lt;attribute_name></i>
     * <p> The cryptographic service name must not contain any dots. There
     * must be one or more space charaters between the <i>&lt;algorithm_or_type></i>
     * and the <i>&lt;attribute_name></i>.
     * <p> The value associated with the key must be a non-empty string.
     * A provider satisfies this selection criterion iff the
     * provider implements the specified algorithm or type for the specified 
     * cryptographic service and its implementation meets the
     * constraint expressed by the specified attribute name/value pair. 
     * </ul>
     *
     * <p> See Appendix A in the <a href=
     * "../../../guide/security/CryptoSpec.html#AppA">
     * Java Cryptogaphy Architecture API Specification &amp; Reference </a>
     * for information about standard cryptographic service names, standard
     * algorithm names and standard attribute names.
     *
     * @param filter the criteria for selecting
     * providers. The filter is case-insensitive.
     *
     * @return all the installed providers that satisfy the selection
     * criteria, or null if no such providers have been installed. 
     *
     * @throws InvalidParameterException
     *         if the filter is not in the required format
     *
     * @see #getProviders(java.lang.String)
     */
    public static Provider[] getProviders(Map filter) {
        Provider[] allProviders = Security.getProviders();
        Set keySet = filter.keySet();
        LinkedHashSet candidates = new LinkedHashSet(5);
        if ((keySet == null) || (allProviders == null)) {
            return allProviders;
        }
        boolean firstSearch = true;
        for (Iterator ite = keySet.iterator(); ite.hasNext(); ) {
            String key = (String) ite.next();
            String value = (String) filter.get(key);
            LinkedHashSet newCandidates = getAllQualifyingCandidates(key, value, allProviders);
            if (firstSearch) {
                candidates = newCandidates;
                firstSearch = false;
            }
            if ((newCandidates != null) && !newCandidates.isEmpty()) {
                for (Iterator cansIte = candidates.iterator(); cansIte.hasNext(); ) {
                    Provider prov = (Provider) cansIte.next();
                    if (!newCandidates.contains(prov)) {
                        cansIte.remove();
                    }
                }
            } else {
                candidates = null;
                break;
            }
        }
        if ((candidates == null) || (candidates.isEmpty())) return null;
        Object[] candidatesArray = candidates.toArray();
        Provider[] result = new Provider[candidatesArray.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (Provider) candidatesArray[i];
        }
        return result;
    }

    private static boolean checkSuperclass(Class subclass, Class superclass) {
        while (!subclass.equals(superclass)) {
            subclass = subclass.getSuperclass();
            if (subclass == null) {
                return false;
            }
        }
        return true;
    }

    static Object[] getImpl(String algorithm, String type, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        ProviderProperty pp = getEngineClassName(algorithm, provider, type);
        return doGetImpl(algorithm, type, pp);
    }

    static Object[] getImpl(String algorithm, String type, String provider, Object params) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        ProviderProperty pp = getEngineClassName(algorithm, provider, type);
        return doGetImpl(algorithm, type, pp, params);
    }

    static Object[] getImpl(String algorithm, String type, Provider provider) throws NoSuchAlgorithmException {
        ProviderProperty pp = getEngineClassName(algorithm, provider, type);
        return doGetImpl(algorithm, type, pp);
    }

    static Object[] getImpl(String algorithm, String type, Provider provider, Object params) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        ProviderProperty pp = getEngineClassName(algorithm, provider, type);
        return doGetImpl(algorithm, type, pp, params);
    }

    private static Object[] doGetImpl(String algorithm, String type, ProviderProperty pp) throws NoSuchAlgorithmException {
        try {
            return doGetImpl(algorithm, type, pp, null);
        } catch (InvalidAlgorithmParameterException e) {
            throw new NoSuchAlgorithmException(e.getMessage());
        }
    }

    private static Object[] doGetImpl(String algorithm, String type, ProviderProperty pp, Object params) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String className = pp.className;
        String providerName = pp.provider.getName();
        try {
            Class typeClass;
            if (type.equals("CertificateFactory") || type.equals("CertPathBuilder") || type.equals("CertPathValidator") || type.equals("CertStore")) {
                typeClass = Class.forName("java.security.cert." + type + "Spi");
            } else {
                typeClass = Class.forName("java.security." + type + "Spi");
            }
            ClassLoader cl = pp.provider.getClass().getClassLoader();
            Class implClass;
            if (cl != null) {
                implClass = cl.loadClass(className);
            } else {
                implClass = Class.forName(className);
            }
            if (checkSuperclass(implClass, typeClass)) {
                Object obj;
                if (type.equals("CertStore")) {
                    Constructor cons = implClass.getConstructor(new Class[] { Class.forName("java.security.cert.CertStoreParameters") });
                    obj = cons.newInstance(new Object[] { params });
                } else obj = implClass.newInstance();
                return new Object[] { obj, pp.provider };
            } else {
                throw new NoSuchAlgorithmException("class configured for " + type + ": " + className + " not a " + type);
            }
        } catch (ClassNotFoundException e) {
            throw new NoSuchAlgorithmException("class configured for " + type + "(provider: " + providerName + ")" + "cannot be found.\n" + e.getMessage());
        } catch (InstantiationException e) {
            throw (NoSuchAlgorithmException) new NoSuchAlgorithmException("class " + className + " configured for " + type + "(provider: " + providerName + ") cannot be " + "instantiated.\n").initCause(e);
        } catch (IllegalAccessException e) {
            throw new NoSuchAlgorithmException("class " + className + " configured for " + type + "(provider: " + providerName + ") cannot be accessed.\n" + e.getMessage());
        } catch (SecurityException e) {
            throw new NoSuchAlgorithmException("class " + className + " configured for " + type + "(provider: " + providerName + ") cannot be accessed.\n" + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new NoSuchAlgorithmException("constructor for " + "class " + className + " configured for " + type + "(provider: " + providerName + ") cannot be instantiated.\n" + e.getMessage());
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t != null && t instanceof InvalidAlgorithmParameterException) throw (InvalidAlgorithmParameterException) t; else throw new InvalidAlgorithmParameterException("constructor " + "for class " + className + " configured for " + type + "(provider: " + providerName + ") cannot be instantiated.\n" + e.getMessage());
        }
    }

    /**
     * Gets a security property value.
     *
     * <p>First, if there is a security manager, its
     * <code>checkPermission</code>  method is called with a 
     * <code>java.security.SecurityPermission("getProperty."+key)</code>
     * permission to see if it's ok to retrieve the specified
     * security property value.. 
     *
     * @param key the key of the property being retrieved.
     *
     * @return the value of the security property corresponding to key.
     *
     * @throws  SecurityException
     *          if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkPermission}</code> method
     *          denies
     *          access to retrieve the specified security property value
     * 
     * @see #setProperty
     * @see java.security.SecurityPermission
     */
    public static String getProperty(String key) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SecurityPermission("getProperty." + key));
        }
        String name = props.getProperty(key);
        if (name != null) name = name.trim();
        return name;
    }

    /**
     * Sets a security property value.
     *
     * <p>First, if there is a security manager, its
     * <code>checkPermission</code> method is called with a 
     * <code>java.security.SecurityPermission("setProperty."+key)</code>
     * permission to see if it's ok to set the specified
     * security property value.
     *
     * @param key the name of the property to be set.
     *
     * @param datum the value of the property to be set.
     *
     * @throws  SecurityException
     *          if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkPermission}</code> method
     *          denies access to set the specified security property value
     * 
     * @see #getProperty
     * @see java.security.SecurityPermission
     */
    public static void setProperty(String key, String datum) {
        check("setProperty." + key);
        props.put(key, datum);
        invalidateSMCache(key);
    }

    private static void invalidateSMCache(String key) {
        final boolean pa = key.equals("package.access");
        final boolean pd = key.equals("package.definition");
        if (pa || pd) {
            AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    try {
                        Class cl = Class.forName("java.lang.SecurityManager", false, null);
                        Field f = null;
                        boolean accessible = false;
                        if (pa) {
                            f = cl.getDeclaredField("packageAccessValid");
                            accessible = f.isAccessible();
                            f.setAccessible(true);
                        } else {
                            f = cl.getDeclaredField("packageDefinitionValid");
                            accessible = f.isAccessible();
                            f.setAccessible(true);
                        }
                        f.setBoolean(f, false);
                        f.setAccessible(accessible);
                    } catch (Exception e1) {
                    }
                    return null;
                }
            });
        }
    }

    private static void check(String directive) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkSecurityAccess(directive);
        }
    }

    /**
     * Print an error message that may be significant to a user.
     */
    static void error(String msg) {
        if (debug) {
            System.err.println(msg);
        }
    }

    /**
     * Print an error message that may be significant to a user.
     */
    static void error(String msg, Throwable t) {
        error(msg);
        if (debug) {
            t.printStackTrace();
        }
    }

    /**
     * Print an debugging message that may be significant to a developer.
     */
    static void debug(String msg) {
        if (debug) {
            System.err.println(msg);
        }
    }

    /**
     * Print an debugging message that may be significant to a developer.
     */
    static void debug(String msg, Throwable t) {
        if (debug) {
            t.printStackTrace();
            System.err.println(msg);
        }
    }

    private static LinkedHashSet getAllQualifyingCandidates(String filterKey, String filterValue, Provider[] allProviders) {
        String[] filterComponents = getFilterComponents(filterKey, filterValue);
        String serviceName = filterComponents[0];
        String algName = filterComponents[1];
        String attrName = filterComponents[2];
        String cacheKey = serviceName + '.' + algName;
        LinkedHashSet candidates = (LinkedHashSet) searchResultsCache.get(cacheKey);
        LinkedHashSet forCache = getProvidersNotUsingCache(serviceName, algName, null, null, null, allProviders);
        if ((forCache == null) || (forCache.isEmpty())) {
            return null;
        } else {
            searchResultsCache.put(cacheKey, forCache);
            if (attrName == null) {
                return forCache;
            }
            return getProvidersNotUsingCache(serviceName, algName, attrName, filterValue, candidates, allProviders);
        }
    }

    private static LinkedHashSet getProvidersNotUsingCache(String serviceName, String algName, String attrName, String filterValue, LinkedHashSet candidates, Provider[] allProviders) {
        if ((attrName != null) && (candidates != null) && (!candidates.isEmpty())) {
            for (Iterator cansIte = candidates.iterator(); cansIte.hasNext(); ) {
                Provider prov = (Provider) cansIte.next();
                if (!isCriterionSatisfied(prov, serviceName, algName, attrName, filterValue)) {
                    cansIte.remove();
                }
            }
        }
        if ((candidates == null) || (candidates.isEmpty())) {
            if (candidates == null) candidates = new LinkedHashSet(5);
            for (int i = 0; i < allProviders.length; i++) {
                if (isCriterionSatisfied(allProviders[i], serviceName, algName, attrName, filterValue)) {
                    candidates.add(allProviders[i]);
                }
            }
        }
        return candidates;
    }

    private static boolean isCriterionSatisfied(Provider prov, String serviceName, String algName, String attrName, String filterValue) {
        String key = serviceName + '.' + algName;
        if (attrName != null) {
            key += ' ' + attrName;
        }
        String propValue = getProviderProperty(key, prov);
        if (propValue == null) {
            String standardName = getProviderProperty("Alg.Alias." + serviceName + "." + algName, prov);
            if (standardName != null) {
                key = serviceName + "." + standardName;
                if (attrName != null) {
                    key += ' ' + attrName;
                }
                propValue = getProviderProperty(key, prov);
            }
            if (propValue == null) {
                return false;
            }
        }
        if (attrName == null) {
            return true;
        }
        if (isStandardAttr(attrName)) {
            return isConstraintSatisfied(attrName, filterValue, propValue);
        } else {
            return filterValue.equalsIgnoreCase(propValue);
        }
    }

    private static boolean isStandardAttr(String attribute) {
        if (attribute.equalsIgnoreCase("KeySize")) return true;
        if (attribute.equalsIgnoreCase("ImplementedIn")) return true;
        return false;
    }

    private static boolean isConstraintSatisfied(String attribute, String value, String prop) {
        if (attribute.equalsIgnoreCase("KeySize")) {
            int requestedSize = (new Integer(value)).intValue();
            int maxSize = (new Integer(prop)).intValue();
            if (requestedSize <= maxSize) {
                return true;
            } else {
                return false;
            }
        }
        if (attribute.equalsIgnoreCase("ImplementedIn")) {
            return value.equalsIgnoreCase(prop);
        }
        return false;
    }

    static String[] getFilterComponents(String filterKey, String filterValue) {
        int algIndex = filterKey.indexOf('.');
        if (algIndex < 0) {
            throw new InvalidParameterException("Invalid filter");
        }
        String serviceName = filterKey.substring(0, algIndex);
        String algName = null;
        String attrName = null;
        if (filterValue.length() == 0) {
            algName = filterKey.substring(algIndex + 1).trim();
            if (algName.length() == 0) {
                throw new InvalidParameterException("Invalid filter");
            }
        } else {
            int attrIndex = filterKey.indexOf(' ');
            if (attrIndex == -1) {
                throw new InvalidParameterException("Invalid filter");
            } else {
                attrName = filterKey.substring(attrIndex + 1).trim();
                if (attrName.length() == 0) {
                    throw new InvalidParameterException("Invalid filter");
                }
            }
            if ((attrIndex < algIndex) || (algIndex == attrIndex - 1)) {
                throw new InvalidParameterException("Invalid filter");
            } else {
                algName = filterKey.substring(algIndex + 1, attrIndex);
            }
        }
        String[] result = new String[3];
        result[0] = serviceName;
        result[1] = algName;
        result[2] = attrName;
        return result;
    }

    /**
    * Returns a Set of Strings containing the names of all available
    * algorithms or types for the specified Java cryptographic service
    * (e.g., Signature, MessageDigest, Cipher, Mac, KeyStore). Returns
    * an empty Set if there is no provider that supports the  
    * specified service. For a complete list of Java cryptographic
    * services, please see the 
    * <a href="../../../guide/security/CryptoSpec.html">Java 
    * Cryptography Architecture API Specification &amp; Reference</a>.
    * Note: the returned set is immutable.
    *
    * @param serviceName the name of the Java cryptographic 
    * service (e.g., Signature, MessageDigest, Cipher, Mac, KeyStore).
    * Note: this parameter is case-insensitive.
    *
    * NOTE: <B>java.security.Signature, java.security.KeyStore</B> are found 
    * in J2ME CDC profiles such as J2ME Foundation Profile. <B> Cipher,
    * Mac </B> are found in J2ME CDC optional packages such as J2ME Security
    * Optional Package.
    *
    * @return a Set of Strings containing the names of all available 
    * algorithms or types for the specified Java cryptographic service
    * or an empty set if no provider supports the specified service.
    *
    * @since 1.4
    **/
    public static Set getAlgorithms(String serviceName) {
        HashSet result = new HashSet();
        if ((serviceName == null) || (serviceName.length() == 0) || (serviceName.endsWith("."))) {
            return result;
        }
        Provider[] providers = Security.getProviders();
        for (int i = 0; i < providers.length; i++) {
            for (Enumeration e = providers[i].keys(); e.hasMoreElements(); ) {
                String currentKey = ((String) e.nextElement()).toUpperCase();
                if (currentKey.startsWith(serviceName.toUpperCase())) {
                    if (currentKey.indexOf(" ") < 0) {
                        result.add(currentKey.substring(serviceName.length() + 1));
                    }
                }
            }
        }
        return Collections.unmodifiableSet(result);
    }
}
