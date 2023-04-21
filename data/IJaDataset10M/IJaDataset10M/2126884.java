package tico.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Vector;
import tico.components.resources.TFileUtils;
import tico.configuration.TResourceBundle;

public class TEnvironment implements Comparator {

    private static String ENVIRONMENT_DIRECTORY = "environment";

    private static String currentEnvironment = "entorno";

    private static Map environmentMap = getEnvironment();

    private static TResourceBundle ENVIRONMENT_BUNDLE = null;

    /**	
	 * Inits the environment file loading the file of the specified
	 * <code>environment</code> to the <code>ENVIRONMENT_BUNDLE</code>.
	 * 
	 * @param environment
	 *            The current <code>environment</code>
	 * @throws IOException
	 */
    public static void initEnvironment(String environment) throws IOException {
        environmentMap = getEnvironment();
        if ((environmentMap == null) || environmentMap.isEmpty()) throw new FileNotFoundException();
        currentEnvironment = "entorno";
        if ((currentEnvironment == null) || !environmentMap.containsKey(currentEnvironment)) {
            Iterator environIterator = environmentMap.entrySet().iterator();
            Map.Entry environEntry = (Map.Entry) environIterator.next();
            currentEnvironment = (String) environEntry.getKey();
        }
        InputStream i = new FileInputStream((File) environmentMap.get(currentEnvironment));
        ENVIRONMENT_BUNDLE = new TResourceBundle(i);
    }

    /**
	 * Determines if exists the <code>environment</code> file.
	 * 
	 * @param environment
	 *            The <code>environment</code> file to check
	 * @return <i>true</i> if the <code>environment</code> file exists
	 */
    public static boolean environmentExists(String environment) {
        Map environmentMap = getEnvironment();
        return environmentMap.containsKey(environment);
    }

    /**
	 * Generates a <code>map</code> that contains all the environment files of
	 * <i>entorno</i> directory and its corresponding language names.
	 * 
	 * @return The generated <code>map</code>
	 */
    public static Map getEnvironment() {
        Map environmentMap = new Hashtable();
        File environmentDirectory = new File(ENVIRONMENT_DIRECTORY);
        File[] fileList = environmentDirectory.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            try {
                if (TFileUtils.getExtension(fileList[i]).equals("txt")) {
                    TResourceBundle currentEnvironmentBundle = new TResourceBundle(new FileInputStream(fileList[i]));
                    environmentMap.put(currentEnvironmentBundle.getString("entorno"), fileList[i]);
                }
            } catch (IOException e) {
            }
        }
        return environmentMap;
    }

    /**
	 * Returns the string of the specified <code>key</code> in the current
	 * environment resource bundle.
	 * 
	 * @param key
	 *            The specified <code>key</code>
	 * @return The string of the specified <code>key</code>
	 */
    public static String getCode(String key) {
        try {
            return ENVIRONMENT_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        } catch (NullPointerException e) {
            return '!' + key + '!';
        }
    }

    public TResourceBundle getEnvironmentBundle() {
        return ENVIRONMENT_BUNDLE;
    }

    public static Vector getAllKeys() {
        try {
            Enumeration algo = ENVIRONMENT_BUNDLE.getKeys();
            Vector<String> keys = new Vector<String>();
            int i = 0;
            keys.add(i, " ");
            i++;
            while (algo.hasMoreElements()) {
                String cadena = algo.nextElement().toString();
                if (!cadena.equals("entorno")) {
                    keys.add(i, cadena);
                    i++;
                }
            }
            Collections.sort(keys, new TEnvironment());
            return keys;
        } catch (NullPointerException e) {
            return new Vector<String>();
        }
    }

    public int compare(Object o1, Object o2) {
        int result = 0;
        String name1 = o1.toString().trim().toLowerCase();
        String name2 = o2.toString().trim().toLowerCase();
        if (name1.length() > 0 && name2.length() > 0) {
            String firstName1 = name1.substring(0, 1);
            String firstName2 = name2.substring(0, 1);
            if (firstName1.matches("[a-z]") && firstName2.matches("[a-z]")) {
                result = name1.compareTo(name2);
            } else if (firstName1.matches("[a-z]") && !firstName2.matches("[a-z]")) {
                result = -1;
            } else if (!firstName1.matches("[a-z]") && firstName2.matches("[a-z]")) {
                result = 1;
            }
        }
        return result;
    }
}
