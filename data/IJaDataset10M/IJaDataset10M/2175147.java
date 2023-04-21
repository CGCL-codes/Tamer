package net.fuhrparkservice.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.fuhrparkservice.Fahrzeug;
import net.fuhrparkservice.Kunde;

/**
 * 
 * @author Thomas G&ouml;tzinger
 *
 */
public class FuhrparkManagement implements IFuhrparkManagement {

    private Map<Kunde, Fahrzeug> kundeMietetFahrzeug = new HashMap<Kunde, Fahrzeug>();

    public boolean containsKunde(Kunde kunde) {
        return kundeMietetFahrzeug.containsKey(kunde);
    }

    public Fahrzeug getFahrzeugeVonKunde(Kunde kunde) {
        return kundeMietetFahrzeug.get(kunde);
    }

    public Set<Kunde> kunden() {
        return kundeMietetFahrzeug.keySet();
    }

    public Fahrzeug put(Kunde kunde, Fahrzeug fahrzeug) {
        return kundeMietetFahrzeug.put(kunde, fahrzeug);
    }
}
