package Toepen.LoungeFacade;

import java.util.HashMap;

/**
 * Singleton klasse die alle standaard login accounts faciliteert
 * @author Bart Piggen
 */
public class DataEntrance {

    static DataEntrance mInstance;

    HashMap<String, String> inMemoryLogins = new HashMap<String, String>();

    private DataEntrance() {
        inMemoryLogins.put("bart", "");
        inMemoryLogins.put("jonathan", "");
        inMemoryLogins.put("lucas", "");
        inMemoryLogins.put("marcel", "");
        inMemoryLogins.put("michiel", "");
        inMemoryLogins.put("peter", "");
    }

    /**
     * Getter voor Singleton
     * @return DataEntrance singleton
     */
    public static DataEntrance GetInstance() {
        if (mInstance == null) mInstance = new DataEntrance();
        return (mInstance);
    }

    /**
     * Valideert login aan de hand van de hardcoded usename password combinaties
     * @param naam login naam
     * @param wachtwoord wachtwoord waarmee ingelogd wordt
     * @return true als het inloggen geslaagd is
     */
    public boolean ValidateLogin(String naam, String wachtwoord) {
        return (inMemoryLogins.containsKey(naam) && (inMemoryLogins.get(naam).equals(wachtwoord)));
    }
}
