package logic;

/**
 * Abbildung von Würfeltypnamen auf Würfeltypnummern.
 */
public interface TypeIDSource {

    /**
     * Bildet einen Würfeltypnamen auf eine Würfeltypnummer ab.
     * 
     * @param name
     * @return
     */
    int mapTypeString(String name);

    /**
     * Gibt die Anzahl der verschiedenen Typenummern zurück.
     * 
     * @return Anzahl der verschiedenen Typenummern.
     */
    int getNumberOfTypes();

    /**
     * Eine primitive TypeIDSource, die immer 1 als Würfeltyp zurück gibt.
     */
    static TypeIDSource dummySource = new TypeIDSource() {

        public int getNumberOfTypes() {
            return 1;
        }

        public int mapTypeString(String name) {
            return 1;
        }
    };
}
