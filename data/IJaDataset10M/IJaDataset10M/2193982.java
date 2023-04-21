package de.laidback.racoon.interfaces;

/**
 * 
 * Konfigurationsinterface, welches als Parametertyp f�r Komponenten
 * benutzt wird.
 * 
 * @author Thomas
 *
 */
public interface IConfiguration {

    /**
	 * Gibt den Wert eines Parameters als String zur�ck.
	 * 
	 * @param param
	 * @return
	 */
    public String getStringValue(String param);

    public int getIntValue(String param);
}
