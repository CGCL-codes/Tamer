package main;

/** Die Klasse Personen beinhaltet die Felder der Personenobjekte. */
public class Personen {

    private int id;

    private String name;

    private String vorname;

    /** Instantiates a new personen.	 * 
	 * @param id the id
	 * @param name the name
	 */
    public Personen(int id, String name, String vorname) {
        this.id = id;
        this.name = name;
        this.vorname = vorname;
    }

    /** Gets the vorname. 
	 * @return the vorname */
    public String getVorname() {
        return vorname;
    }

    /** Gets the name. 
	 * @return the name */
    public String getName() {
        return name;
    }

    /** Gets the id.	  
	 * @return the id */
    public int getid() {
        return id;
    }

    /** Dient dazu die Personenobjekte als Name und Vorname in der Liste anzuzeigen */
    public String toString() {
        return " " + name + " " + vorname;
    }
}
