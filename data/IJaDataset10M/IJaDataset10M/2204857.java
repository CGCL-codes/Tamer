package org.auramp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * A class to manipulate the Windows registry.  This is not the most efficient
 * way to manipulate the registry, however it does not require any JNI libraries.
 * @author David Cawley <sakrag@gmail.com>
 */
public class WinRegistry {

    /**
	 * Represents the common registry roots of a Windows registry.
	 * @author David Cawley <sakrag@gmail.com>
	 */
    public enum Roots {

        HKEY_CLASSES_ROOT("HKCR"), HKEY_CURRENT_USER("HKCU"), HKEY_LOCAL_MACHINE("HKLM"), HKEY_USERS("HKU"), HKEY_CURRENT_CONFIG("HKCC");

        private String id;

        private Roots(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
	 * Represents the types of values that exist in the Windows registry.
	 * @author David Cawley <sakrag@gmail.com>
	 */
    public enum KeyTypes {

        REG_SZ, REG_MULTI_SZ, REG_EXPAND_SZ, REG_DWORD, REG_BINARY, REG_NONE
    }

    private WinRegistry() {
    }

    /**
	 * Queries the Windows registry for the data contained in a specific value element
	 * of the specified registry key.
	 * @param root the root the registry key is contained in
	 * @param path the canonical path to the registry key excluding the root 
	 * @param name the name of the registry value
	 * @return the data stored in the registry value
	 * @throws IOException thrown when there is a problem invoking the REG command line utility
	 */
    public static RegistryKeyValue queryRegistry(Roots root, String path, String name) throws IOException {
        String qpath = "\"" + root.getId() + "\\" + path + "\"";
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("cmd", "/c", "reg", "query", qpath, "/v", name);
        Process proc = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = reader.readLine().trim();
        while (line != null) {
            if (line.length() == 0) {
                line = reader.readLine().trim();
                continue;
            }
            if (line.equals("ERROR: Invalid key name.")) {
                throw new IOException("Invalid key name / path!");
            }
            if (line.startsWith(name)) {
                StringTokenizer tok = new StringTokenizer(line);
                tok.nextToken();
                String stype = tok.nextToken();
                KeyTypes type = KeyTypes.valueOf(stype);
                String value = tok.nextToken("").trim();
                return new RegistryKeyValue(name, path, type, root, value);
            }
            line = reader.readLine().trim();
        }
        throw new IOException("Error parsing output from REG CLI utility.");
    }

    /**
	 * Updates the Windows registry value to contain the specified data.
	 * @param key the RegistrykeyValue representing the value to update
	 * @throws IOException thrown when there is a problem invoking the REG command line utility
	 */
    public static void updateRegistry(RegistryKeyValue key) throws IOException {
        String qpath = "\"" + key.getRoot().getId() + "\\" + key.getPath() + "\"";
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("cmd", "/c", "reg", "add", qpath, "/v", key.getName(), "/t", key.getType().toString(), "/d", key.getValue(), "/f");
        Process proc = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = reader.readLine().trim();
        while (line != null) {
            if (line.length() == 0) {
                line = reader.readLine().trim();
                continue;
            }
            if (line.equals("The operation completed successfully.")) {
                return;
            }
            line = reader.readLine().trim();
        }
        throw new IOException("Error parsing output from REG CLI utility.");
    }

    /**
	 * Represents a value existing in the Windows registry.
	 * @author David Cawley <sakrag@gmail.com>
	 */
    public static class RegistryKeyValue {

        private String path;

        private String name;

        private String value;

        private KeyTypes type;

        private Roots root;

        /**
		 * Creates a new object representing a registry key value in the Windows registry.
		 * @param name the name of the value
		 * @param path the path to the key excluding the root
		 * @param type the type of data stored in this value
		 * @param root the root containing this key
		 * @param value the data stored in this value
		 */
        public RegistryKeyValue(String name, String path, KeyTypes type, Roots root, String value) {
            this.name = name;
            this.path = path;
            this.type = type;
            this.root = root;
            this.value = value;
        }

        /**
		 * Gets the path to this registry key.
		 * @return the path to this key
		 */
        public String getPath() {
            return path;
        }

        /**
		 * Gets the name of this value.
		 * @return the name of this value
		 */
        public String getName() {
            return name;
        }

        /**
		 * Gets the type of data stored in this value
		 * @return the data type
		 */
        public KeyTypes getType() {
            return type;
        }

        /**
		 * Gets the root key containing this key
		 * @return the root of this key
		 */
        public Roots getRoot() {
            return root;
        }

        /**
		 * Gets the data stored in this registry value
		 * @return the data of this registry value
		 */
        public String getValue() {
            return value;
        }
    }
}
