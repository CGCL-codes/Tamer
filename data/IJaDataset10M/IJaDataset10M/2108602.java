package net.sourceforge.cridmanager.box;

/**
 * @author Olaf
 */
public class BoxBeschreibung {

    private String modell;

    private int tunerAnzahl;

    private String version;

    public BoxBeschreibung(String modell, String version, int tuner) {
        tunerAnzahl = tuner;
        this.modell = modell;
        this.version = version;
    }

    /**
	 * @return Returns the modell.
	 */
    public String getModell() {
        return modell;
    }

    /**
	 * @return Returns the tunerAnzahl.
	 */
    public int getTunerAnzahl() {
        return tunerAnzahl;
    }

    /**
	 * @return Returns the version.
	 */
    public String getVersion() {
        return version;
    }
}
