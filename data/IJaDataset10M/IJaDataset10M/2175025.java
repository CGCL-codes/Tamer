package de.objectinc.samsha.utils.io;

import org.apache.log4j.Logger;
import java.io.*;
import java.util.*;
import java.util.jar.*;

/**
 * @author Christian Schack
 * @since 15.05.2005
 * 
 * <br>
 * <b>Beschreibung:</b>
 * <br>
 * 
 * Die Klasse <code>IOControl</code> stellt n�tzliche Methoden<br> 
 * f�r Ein-und Ausgaben zur Verf�gung. Ausserdem bietet sie Methoden an um<br>
 * JAR Dateien auszulesen und zu bearbeiten.<br>
 * 
 */
public class IOUtil {

    private Logger ioLogger;

    /**
	 * Der Konstruktor der Klasse initialisiert ein Loggerobjekt
	 *
	 */
    public IOUtil() {
        ioLogger = Logger.getLogger(this.getClass());
    }

    /**
	 * Die Methode <code>getAbsolutePath</code> liefert den absoluten Pfad zum<br> 
	 * zur DTaCS Programm zur�ck
	 * @return String absoluter Pfad zum Programm
	 */
    public static String getAbsolutPath() {
        return System.getProperty("user.dir");
    }

    /**
	 * Die Methode exists() �berpr�ft ob der in dem �bergebenen String angebenen Dateiname oder<br>
	 * Pfadname existiert und gibt das Ergebnis der Auswertung zur�ck. <br>
	 * @param fileName
	 * @return <code>TRUE</code> wenn Datei oder Verzecihnis existiert, sonst <code>FALSE</code>
	 */
    public static boolean exists(String fileName) {
        File file = new File(fileName);
        return (file.exists());
    }

    /**
	 * <i>Aktuell nicht implementiert</i>
	 * 
	 * @param src Quelle
	 * @param dst Ziel 	
	 * @return <code>TRUE</code>
	 */
    public boolean saveFile(File src, String dst) {
        return true;
    }

    /**
	 * Die Methode <code>saveObjekt()</code> speichert ein serialisierbares Objekt persistent in dem<br>
	 * �bergebenen Zielordner.<br>
	 * @param src <code>Serializable</code> Objekt  
	 * @param dst Zielort
	 * return <code>TRUE</code> falls Speichervorgang erfolgreich, sonst <code>FALSE</code> 
	 */
    public boolean saveObject(Serializable src, String dst) {
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(dst));
            ooStream.writeObject(src);
        } catch (IOException e) {
            ioLogger.error(e);
            return false;
        }
        return true;
    }

    /**
	 * Die Methode <code>readObjekt()</code> lie�t ein serialisierbares Objekt<br> 
	 * aus. Das Objekt muss sich innerhalb des �bergebenen Pfades befinden.<br>
	 * War das laden erfolgreich wird das Objekt zur�ckgegen ansonsten <code>null</code><br>
	 * @param src Pfadangabe zum auszulesenden Objekt
	 * @return <code>Serializable</code> geladenes Objekt
	 *  
	 */
    public Serializable readObject(String src) {
        try {
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(src));
            return (Serializable) oiStream.readObject();
        } catch (IOException e) {
            ioLogger.error(e);
        } catch (ClassNotFoundException e) {
            ioLogger.error(e);
        } catch (Exception e) {
            ioLogger.error(e);
        }
        return null;
    }

    /**
	 * Die Methode getManifest() liesst die Manifestdatei aus der �bergebenen jar Datei<br>
	 * aus und gibt eine Instanz vom Typ <code>Manifest</code> zur�ck.
	 * @param jarFileName Name der Jar Datei in dem die Manifestdatei ausgelesen werden soll.
	 * @return <code>Manifest</code> Manifestobjekt
	 */
    public Manifest getManifest(String jarFileName) {
        try {
            JarInputStream jiStream = new JarInputStream(new FileInputStream(new File(jarFileName)));
            return jiStream.getManifest();
        } catch (IOException e) {
            ioLogger.error(e.getMessage());
        }
        return null;
    }

    /**
	 * Die Methode <code>getManifestFileEntries()</code> liefert die Eintr�ge zur�ck,<br>
	 * die sich in dem �bergebenen Manifestobjekt befinden.<br>
	 * 
	 * @param manifest 
	 * @return Attributes <code>Attribute</code> -liste
	 */
    public Attributes getManifestFileEntries(Manifest manifest) {
        Attributes entries = manifest.getMainAttributes();
        return entries;
    }

    /**
	 * Die Methode <code>getJarEntries</code> liefert alle Jar Eintr�ge zur�ck, 
	 * die sich innerhalb des �bergebenen Jar Files befinden.<br>
	 * 
	 * @param jarFileName Name des auszulesendene Jar Files
	 * @return <code>Vector</code> mit Jar-Eintr�gen 
	 */
    public Vector getJarEntries(String jarFileName) {
        try {
            JarFile jar = new JarFile(jarFileName);
            Enumeration iter = jar.entries();
            Vector entries = new Vector();
            while (iter.hasMoreElements()) entries.add(iter.nextElement());
            return entries;
        } catch (IOException e) {
        }
        return null;
    }

    /**
	 * Die Methode <code>createManifestFile()</code> erzeugt eine neue Manifestdatei mit<br>
	 * mit den �bergebenen Attributen.<br>
	 * Der Zielort wird als Paramter erwartet.<br>
	 * @param attributes <code>StringBuffer</code> mit Attributen
	 * @param path <code>String</code> Ziel als Pfadangabe 
	 * 
	 */
    public void createManifestFile(StringBuffer attributes, String path) {
        try {
            File maniFile = new File(path + "\\manifest.mf");
            maniFile.createNewFile();
            InputStream fis = new FileInputStream(path + "\\manifest.mf");
            Manifest manifest = new Manifest(fis);
            StringBuffer sbuf = new StringBuffer();
            sbuf.append("Manifest-Version: 1.0\n");
            FileOutputStream oStream = new FileOutputStream(maniFile);
            oStream.write(sbuf.toString().getBytes());
            oStream.write(attributes.toString().getBytes());
        } catch (IOException e) {
        }
        ;
    }

    /**
	 * Die Methode getJarEntyByName liefert den <code>JarEntry</code>, der dem<br> 
	 * �bergebenen Eintrag entspricht. Als Parameter wird ausserdem ein Vector<br>
	 * mit <code>jarEntries</code> erwartet.<br> 
	 * @param entries <code>Vector</code> mit <code>JarEntry</code>-Eintr�gen
	 * @param jarEntryName Name des zu suchenden Eintrags
	 * @return JarEnty gefundener Eintrag
	 */
    public JarEntry getJarEntryByName(Vector entries, String jarEntryName) {
        Iterator iter = entries.iterator();
        while (iter.hasNext()) {
            JarEntry entry = (JarEntry) iter.next();
            if (entry.getName().equals(jarEntryName)) return entry;
        }
        return null;
    }

    /**
	 * Die statische Methode <code>createDir()</code> erzeugt ein neues Verzeichnis. Der Name<br>
	 * des zu erzeugenden Verzeichnisses wird als Parameter erwartet.<br> 
	 * @param dir Name des zu erstellenden Verzeichnisses
	 */
    public static void createDir(String dir) {
        File dirFile = new File(dir);
        dirFile.mkdirs();
    }

    /**
	 * Saves the key value pairs to the specified destination
	 * @param hashtable
	 * @param dest
	 * @return
	 *   
	 * boolean
	 */
    public static boolean savePropertyFileToXML(Hashtable<String, String> hashtable, String dest) {
        Properties props = new Properties();
        try {
            File file = new File(dest);
            FileOutputStream stream = new FileOutputStream(file);
            Iterator<String> iter = hashtable.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                props.put(key, hashtable.get(key));
            }
            props.storeToXML(stream, "Profile");
            stream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * Loads the data of the specified source file into a <code>HashTable</code> 
	 * @param hashtable
	 * @param src
	 * @return a filled HashTable or null if an error occured
	 *   
	 * Hashtable<String,String>
	 */
    public static Hashtable<String, String> loadPropertyFileFromXML(String srcPath) {
        Properties props = new Properties();
        Hashtable<String, String> data = new Hashtable<String, String>();
        if (exists(srcPath)) {
            try {
                File file = new File(srcPath);
                FileInputStream stream = new FileInputStream(file);
                props.loadFromXML(stream);
                Iterator keys = props.keySet().iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    data.put(key, props.getProperty(key));
                }
                stream.close();
                return data;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else return null;
        return null;
    }

    public static String[] getFileNames(String destPath) {
        File file = null;
        if (exists(destPath) && (file = new File(destPath)).isDirectory()) {
            return file.list(new PropertyFileFilter());
        } else return null;
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        file.delete();
    }
}
