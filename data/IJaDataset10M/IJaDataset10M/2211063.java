package net.sourceforge.lascreen.logic;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.omg.PortableServer.POA;
import net.sourceforge.lascreen.gui.*;

public class Configuration {

    static final String graficpath = "graphicpath";

    static final String leftIP = "leftIP";

    static final String rightIP = "rightIP";

    static final String maxGraphics = "maxGraphics";

    static final String waitTime = "waitTime";

    static Properties properties;

    final String configName = "config.properties";

    public Configuration() throws FileNotFoundException, IOException {
        if (properties == null) {
            properties = new Properties();
            configLoad();
        }
    }

    /**
	 * @return Returns the graficpath.
	 */
    public static String getGraficpath() {
        return graficpath;
    }

    /**
	 * @return Returns the leftIP.
	 */
    public static String getLeftIP() {
        return leftIP;
    }

    /**
	 * @return Returns the rightIP.
	 */
    public static String getRightIP() {
        return rightIP;
    }

    /**
	 * @return Returns the properties.
	 */
    public static Properties getProperties() {
        return properties;
    }

    /**
	 * @param properties The properties to set.
	 */
    public static void setProperties(Properties properties) {
        Configuration.properties = properties;
    }

    /**
	 * @return Returns the maxBalls.
	 */
    public static String getMaxGraphics() {
        return maxGraphics;
    }

    /**
	 * @return Returns the waitTime.
	 */
    public static String getWaitTime() {
        return waitTime;
    }

    public Configuration(HashMap hashMap) throws FileNotFoundException, IOException {
        configSave(hashMap);
    }

    public void configLoad() throws IOException {
        HashMap list = new HashMap();
        try {
            properties.load(new FileInputStream(configName));
        } catch (FileNotFoundException ex) {
            list.put(leftIP, "");
            list.put(rightIP, "");
            list.put(graficpath, "");
            list.put(maxGraphics, "0");
            list.put(waitTime, "10");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void configSave(HashMap hashMap) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.putAll(hashMap);
        prop.store(new FileOutputStream(configName), "LAscreeN Configuration");
    }

    /**
	 * Diese Methode pr�ft die Eingabe in die Textfeder IP-Links und IP-Rechts auf korrekte Syntax
	 * @param ip Ip Adresse in einem String
	 * @return boolean Wenn die IP Adresse eine g�ltige IP ist, wird wahr zur�ck gegeben
	 */
    public boolean ipCheck(String ip) {
        ip = ip + ".";
        boolean b = true;
        int stelleDesLetztenPunktes = -1;
        int stelleDesZeichens;
        int anzahlPunkte = 0;
        char[] zeichen = ip.toCharArray();
        int i = 0;
        while ((i < zeichen.length) && (b == true)) {
            if (zeichen[i] == '.') {
                anzahlPunkte++;
                if (((i - 1 == stelleDesLetztenPunktes) && (!(i + 1 == zeichen.length))) || (i + 2 == zeichen.length) || (anzahlPunkte > 4)) {
                    b = false;
                    stelleDesZeichens = i + 1;
                    System.out.println("Punkt an der Stelle " + stelleDesZeichens + " unzul�ssig!");
                }
                int groesse = 0;
                int z = 0;
                for (int j = i - 1; j > stelleDesLetztenPunktes; j--) {
                    int zeichenwert = ((int) zeichen[j]);
                    zeichenwert -= 48;
                    int multiplikant = 1;
                    for (int y = 0; y < z; y++) {
                        multiplikant = multiplikant * 10;
                    }
                    int summand = zeichenwert * multiplikant;
                    groesse = groesse + summand;
                    z++;
                }
                if ((groesse > 255) || (groesse < 0)) {
                    b = false;
                    stelleDesZeichens = stelleDesLetztenPunktes + 1;
                }
                stelleDesLetztenPunktes = i;
            } else if ((zeichen[i] >= '0') && (zeichen[i] <= '9')) {
            } else {
                b = false;
                stelleDesZeichens = i + 1;
            }
            i++;
        }
        if ((anzahlPunkte < 4) && (b == true)) {
            b = false;
        }
        return b;
    }
}
