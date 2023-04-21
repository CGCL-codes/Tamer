package com.avatal.business.util;

/**
 * Created on 01.07.2003
 * @author m0550
 * Diese Klasse enth�lt lediglich eine Methode deren Zweck es ist,
 * einen gegebenen String in einen Hash-Wert nach RSA MD5 Format 
 * umzuwandeln. Diese Art der Verschl�sselung ist hilfreich um
 * zu verhindern das z.B. Passw�rter im Klartext in der Datenbank 
 * gespeichert werden.    
 */
public class Encryption {

    /** Diese Simple Metode bedient sich lediglich der MD5
	 * GNU Implementierung um den Hash Wert des Strings zu bestimmen.
	 */
    public static String getMD5fromString(String password) {
        MD5 hash;
        String hex;
        hash = new MD5(password);
        return hash.asHex();
    }
}
