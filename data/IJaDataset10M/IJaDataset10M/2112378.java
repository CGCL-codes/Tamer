package net.walkingtools.data;

import java.io.*;
import java.util.*;
import java.util.jar.*;

/**
 * The Jad class contains attributes and can format .jad data.
 * @author Brett Stalbaum
 * @version 0.1.1
 * @since 0.0.4
 *
 */
public class Jad {

    private Hashtable<String, String> requiredAttributes = null;

    private Vector<String> midlets = null;

    private Hashtable<String, String> additionalAttributes = null;

    /**
     * The required attributes for a .jad file
     */
    public static final String[] JAD_REQUIRED = { "MIDlet-Jar-Size", "MIDlet-Jar-URL", "MIDlet-Name", "MIDlet-Vendor", "MIDlet-Version", "MicroEdition-Configuration", "MicroEdition-Profile" };

    /**
     * Default constructor
     */
    public Jad() {
        requiredAttributes = new Hashtable<String, String>(7);
        midlets = new Vector<String>(10);
        additionalAttributes = new Hashtable<String, String>(10);
    }

    /**
     * Creates a Jad from a File reference, throwing a IOException if the file
     * does not contain any colon separated data.
     * @param jad String the .jad
     * @throws IOException
     */
    public Jad(String jad) throws IOException {
        this();
        try {
            BufferedReader in = new BufferedReader(new StringReader(jad));
            String str;
            while ((str = in.readLine()) != null) {
                String key = str.substring(0, str.indexOf(": "));
                String value = str.substring(str.indexOf(": ") + 2);
                if (key.matches("^MIDlet-\\d")) {
                    addMIDlet(value);
                } else {
                    boolean foundRequired = false;
                    for (int i = 0; i < JAD_REQUIRED.length; i++) {
                        if (key.equals(JAD_REQUIRED[i])) {
                            requiredAttributes.put(JAD_REQUIRED[i], value);
                            foundRequired = true;
                        }
                    }
                    if (!foundRequired) {
                        addAdditionalAttribute(key, value);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Creates a Jad from a File reference, throwing a IOException if the file
     * does not contain any colon separated data.
     * @param file the .jad file
     * @throws IOException
     */
    public Jad(File file) throws IOException {
        this();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                String key = str.substring(0, str.indexOf(": "));
                String value = str.substring(str.indexOf(": ") + 2);
                if (key.matches("^MIDlet-\\d")) {
                    addMIDlet(value);
                } else {
                    boolean foundRequired = false;
                    for (int i = 0; i < JAD_REQUIRED.length; i++) {
                        if (key.equals(JAD_REQUIRED[i])) {
                            requiredAttributes.put(JAD_REQUIRED[i], value);
                            foundRequired = true;
                        }
                    }
                    if (!foundRequired) {
                        addAdditionalAttribute(key, value);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            throw e;
        }
    }

    /** Constructor initializing from a java.util.jar.Attributes
     * @param attributes the Attributes object will be scraped to initialize any required jad values present
     * @see #JAD_REQUIRED
     * @see java.util.jar.Attributes
     */
    public Jad(Attributes attributes) {
        this();
        scrapeRequiredAttributes(attributes);
    }

    /**
     * Scrapes a java.util.Attributes for any of the 7 required jad attributes, if present:
     * MIDlet-Jar-Size, MIDlet-Jar-URL, MIDlet-Vendor,
     * MIDlet-Version, MicroEdition-Configuration, MIDlet-Name, MicroEdition-Profile,
     * and adds them to this jad. This is used to attain META-MF/MANIFEST.MF for jad.
     * @param attributes
     */
    public void scrapeRequiredAttributes(Attributes attributes) {
        Iterator it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            for (int i = 0; i < JAD_REQUIRED.length; i++) {
                String test = pairs.getKey().toString();
                if (test.equals(JAD_REQUIRED[i])) {
                    requiredAttributes.put(JAD_REQUIRED[i], (String) pairs.getValue());
                }
            }
        }
    }

    /**
     * Scrapes a java.util.Attributes for all attributes and adds them to this
     * jad. This is used to attain META-MF/MANIFEST.MF for jad.
     * @param attributes
     */
    public void scrapeAttributes(Attributes attributes) {
        Iterator it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String key = pairs.getKey().toString();
            boolean found = false;
            for (int i = 0; i < JAD_REQUIRED.length; i++) {
                if (key.equals(JAD_REQUIRED[i])) {
                    requiredAttributes.put(JAD_REQUIRED[i], (String) pairs.getValue());
                    found = true;
                }
            }
            for (int i = 0; i < JAD_REQUIRED.length; i++) {
                if (key.contains("MIDlet-")) {
                    midlets.add((String) pairs.getKey() + ": " + (String) pairs.getValue());
                    found = true;
                }
            }
            if (!found) {
                midlets.add((String) pairs.getKey() + ": " + (String) pairs.getValue());
            }
        }
    }

    /**
     * Adds all of this Jad's attributes to the Attributes object. This is
     * used to add all jad attibutes to META-MF/MANIFEST.MF for jar.
     * @param attributes
     */
    public void updateAttributes(Attributes attributes) {
        if (validate()) {
            for (int i = 0; i < JAD_REQUIRED.length; i++) {
                attributes.remove(JAD_REQUIRED[i]);
                attributes.putValue(JAD_REQUIRED[i], (requiredAttributes.get(JAD_REQUIRED[i])));
            }
            for (int i = 0; i < midlets.size(); i++) {
                int temp = i + 1;
                attributes.putValue("MIDlet-" + temp, midlets.get(i));
            }
            Enumeration<String> elements = additionalAttributes.keys();
            while (elements.hasMoreElements()) {
                String key = elements.nextElement();
                attributes.putValue(key, additionalAttributes.get(key));
            }
        } else {
        }
    }

    /** Sets the MIDlet-Jar-Size attribute
     * @param size the size of the jar file
     * @see #validate()
     */
    public void setMIDletJarSize(long size) {
        requiredAttributes.put("MIDlet-Jar-Size", "" + size);
    }

    /** gets the MIDlet-Jar-Size
     * @return the MIDlet-Jar-Size String
     */
    public String getMIDletJarSize() {
        String attribute = "MIDlet-Jar-Size";
        return attribute + ": " + requiredAttributes.get(attribute);
    }

    /** gets the MIDlet-Jar-Size as an integer
     * @return the MIDlet-Jar-Size String
     */
    public int getMIDletJarSizeInt() {
        String attribute = "MIDlet-Jar-Size";
        return Integer.parseInt(requiredAttributes.get(attribute));
    }

    /** Sets the MIDlet-Jar-URL attribute
     * @param url MIDlet-Jar-URL attribute
     * @see #validate()
     */
    public void setMIDletJarURL(String url) {
        requiredAttributes.put("MIDlet-Jar-URL", url);
    }

    /** gets the MIDlet-Jar-URL
     * @return the MIDlet-Jar-URL String
     */
    public String getMIDletJarURL() {
        String attribute = "MIDlet-Jar-URL";
        return attribute + ": " + requiredAttributes.get(attribute);
    }

    /** Sets the MIDlet-Vendor attribute
     * @param vendor MIDlet-Vendor attribute
     * @see #validate()
     */
    public void setMIDletVendor(String vendor) {
        requiredAttributes.put("MIDlet-Vendor", vendor);
    }

    /** get the MIDlet-Vendor
     * @return the MIDlet-Vendor String
     */
    public String getMIDletVendor() {
        String attribute = "MIDlet-Vendor";
        return attribute + ": " + requiredAttributes.get(attribute);
    }

    /** Sets the MIDlet-Version attribute
     * @param midletVersion MIDlet-Version attribute
     * @see #validate()
     */
    public void setMIDletVersion(String midletVersion) {
        requiredAttributes.put("MIDlet-Version", midletVersion);
    }

    /** gets the MIDlet-Version
     * @return the MIDlet-Version String
     */
    public String getMIDletVersion() {
        String attribute = "MIDlet-Version";
        return attribute + ": " + requiredAttributes.get(attribute);
    }

    /** Sets the MicroEdition-Configuration attribute
     * @param configuration MicroEdition-Configuration attribute
     * @see #validate()
     */
    public void setMicroEditionConfiguration(String configuration) {
        requiredAttributes.put("MicroEdition-Configuration", configuration);
    }

    /** gets the MicroEdition-Configuration
     * @return the MicroEdition-Configuration String
     */
    public String getMicroEditionConfiguration() {
        String attribute = "MicroEdition-Configuration";
        return attribute + ": " + requiredAttributes.get(attribute);
    }

    /** Sets the MIDlet-Name attribute (really the name of the MIDlet suite)
     * @param name MIDlet-Name attribute
     * @see #validate()
     */
    public void setMIDletName(String name) {
        requiredAttributes.put("MIDlet-Name", name);
    }

    /** get the MIDlet-Name (which is really the name of the MIDlet suite)

     * @return MIDlet-Name String
     */
    public String getMIDletName() {
        String attribute = "MIDlet-Name";
        return attribute + ": " + requiredAttributes.get(attribute);
    }

    /** Sets the MicroEdition-Profile attribute
     * @param profile MicroEdition-Profile attribute
     * @see #validate()
     */
    public void setMicroEditionProfile(String profile) {
        requiredAttributes.put("MicroEdition-Profile", profile);
    }

    /** gets the MicroEdition-Profile
     * @return the MicroEdition-Profile String
     */
    public String getMicroEditionProfile() {
        String attribute = "MicroEdition-Profile";
        return attribute + ": " + requiredAttributes.get(attribute);
    }

    /** Adds a MIDlet attribute, specifying the fully qualified package name of a MIDlet class
     * inside of the jar file. At least one MIDlet must be added to validate(). The midlet names
     * are ordered, i.e. the first midlet added will be MIDlet-1, etc.
     * @param appName the name of the application
     * @param iconPath the .png icon relative to /
     * @param classFile the class file of the midlet (including package designation)
     * @see #validate()
     */
    public void addMIDlet(String appName, String iconPath, String classFile) {
        midlets.add(appName + "," + iconPath + "," + classFile);
    }

    /** Adds a MIDlet attribute, specifying the Midlet-N attributes String,
     * in the form of:  "suite name,image icon,fully qualified package name of MIDlet class",
     * as in "transborder,tb.png,edu.ucsd.calit2.TransBorderTool.TransBorderMIDlet"
     * At least one MIDlet must be added to validate().
     * @param midletString "suite name,image icon,fully qualified package name of MIDlet class"
     * @see #validate()
     */
    public void addMIDlet(String midletString) {
        midlets.add(midletString);
    }

    /**
     * Clears the midlet attributes in case of reusing the required and additional attributes
     */
    public void clearMidlets() {
        midlets = new Vector<String>(10);
    }

    /** Returns the requested midlet String, 0 indexed such that index 0 is MIDlet-1, or null if there are none
     * @param i 0 based indexing so 0 is MIDlet-1...
     * @return the String for the associated midlet
     */
    public String getMidlet(int i) {
        if (i < midlets.size() && i > 0) {
            return midlets.get(i);
        } else {
            return null;
        }
    }

    /**
     * Add any additional attribute to to the Jad. If the attribute already exists,
     * throws an IllegalArgumentException.
     * @throws IllegalArgumentException if the attribute has already been added.
     * @see #setAdditionalAttribute(java.lang.String, java.lang.String)
     * @param key the attribute name
     * @param value the attribute value
     */
    public void addAdditionalAttribute(String key, String value) throws IllegalArgumentException {
        if (additionalAttributes.containsKey(key)) {
            throw new IllegalArgumentException();
        }
        additionalAttributes.put(key, value);
    }

    /**
     * Set any additional attribute in the Jad. Updates existing attribute key, or
     * will add it if it does not exist.
     * @see #addAdditionalAttribute(java.lang.String, java.lang.String)
     * @param key the attribute name
     * @param value the attribute value
     */
    public void setAdditionalAttribute(String key, String value) {
        additionalAttributes.put(key, value);
    }

    /** allows an additional attribute to be added
     * @param key
     */
    public void removeAdditionalAttribute(String key) {
        additionalAttributes.remove(key);
    }

    /**
     * Clears all additional attributes in case one wants to start over or reuse the object with
     * current midlets and required attributes
     */
    public void clearAdditionalAttributes() {
        additionalAttributes = new Hashtable<String, String>(10);
    }

    /**
     * Looks up the value of Additonal Attibutes by name(key)
     * @param key the key
     * @return the Attribute
     */
    public String getAdditionalAttribute(String key) {
        return additionalAttributes.get(key);
    }

    /**
     * Returns true if the additional attribute exists in the Jad
     * @param attributeName the name of the attribute
     * @return true if attributeName is in the Jad
     * @since 0.0.6
     */
    public boolean hasAdditionalAttribute(String attributeName) {
        return additionalAttributes.containsKey(attributeName);
    }

    /** Validates that all of the manifest attributes have been set
     * and the object's data is ready to be printed to a Jad file.
     * Note: validate only checks that the required are set.
     * @return true if all attributes are set
     */
    public boolean validate() {
        if (requiredAttributes.size() == 7 && midlets.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /** Format object as Jad data. Uses Windows line breaks.
     * @return a String containing the formatted output proper to a Jad file
     */
    public String formatJad() {
        StringBuffer formattedJad = new StringBuffer();
        if (validate()) {
            for (int i = 0; i < midlets.size(); i++) {
                int temp = i + 1;
                formattedJad.append("MIDlet-" + temp + ": " + midlets.get(i) + "\r\n");
            }
            for (int i = 0; i < JAD_REQUIRED.length; i++) {
                formattedJad.append(JAD_REQUIRED[i] + ": " + requiredAttributes.get(JAD_REQUIRED[i]) + "\r\n");
            }
            Enumeration<String> elements = additionalAttributes.keys();
            while (elements.hasMoreElements()) {
                String key = elements.nextElement();
                formattedJad.append(key + ": " + additionalAttributes.get(key) + "\r\n");
            }
            return formattedJad.toString();
        } else {
            return "invalid jad: bad attributes";
        }
    }

    @Override
    public String toString() {
        return formatJad();
    }
}
