package Versandhausagentur;

import java.util.Hashtable;
import java.io.Serializable;
import Sale.Catalog;
import Sale.DictionaryCreator;
import Sale.DuplicateKeyException;

/**
 * Im Versandhauskatalog werden alle Artikel eines Versandhauses gespeichert, die �ber die
 * Versandhausagentur durch einen Kunden bestellt werden k�nnen. Bei der aktuellen
 * Implementierung kann nur ein einziger Katalog angelegt werden.
 *
 * @version     1.0
 * @author      Rene Baum, Maik Boden
 * @see         Versandhausagentur.Agentur
**/
public class Versandhauskatalog extends Catalog implements Serializable {

    /**
     * Das Versandhaus, dessen Waren als Artikel in den Versandhauskatalog aufgenommen werden.
    **/
    private Versandhaus versandhaus;

    /**
     * Der Constructor erzeugt eine neue Instanz von Versandhauskatalog. Der neue Katalog tr�gt den Namen <b>name</b>
     * und verweist auf sein Versandhaus <b>versandhaus</b>
     *
     * @param       name Name des Kataloges
     * @param       versandhaus Das Versandhaus, dessen Artikel gespeichert werden.
    **/
    private Versandhauskatalog(String name, Versandhaus versandhaus) {
        super(name);
        this.versandhaus = versandhaus;
    }

    /**
     * Erstellt einen neuen Versandhauskatalog mit dem angegebenen Namen, der mit dem Versandhaus verkn�pft wird.
     * Der erstellte Katalog wird in die globale Katalogliste aufgenommen.
     *
     * @param       name Name des Kataloges, der aufgenommen werden soll.
     * @param       versandhaus Das Versandhaus zu dem dieser Katalog gehoert.
     * @exception   DuplicateKeyException Diese Exception wird ausgel�st, wenn bereit ein Katalog mit diesem Namen existiert.
    **/
    public static synchronized void addKatalog(String name, Versandhaus versandhaus) throws DuplicateKeyException {
        Versandhauskatalog katalog = new Versandhauskatalog(name, versandhaus);
        Catalog.addGlobalCatalog((Catalog) katalog);
    }

    /**
     * Diese Methode entfernt einen Katalog aus der globalen Katalogliste.
     *
     * @param       name Name des Kataloges, der aus der Katalogliste entfernt werden soll.
     * @return      Als R�ckgabewert wird der entnommene Katalog zur�ckgeliefert.
    **/
    public static synchronized Versandhauskatalog removeKatalog(String name) {
        Versandhauskatalog katalog = getKatalog(name);
        if (katalog == null) {
            return null;
        }
        Catalog.deleteCatalog(name);
        return katalog;
    }

    /**
     * Diese Methode durchsucht das Archiv aller Catalogs nach einem Versandhauskatalog mit gegebenen Namen.
     * Wird keine Versandhauskatalog mit diesem Namen gefunden, ist der R�ckgabe werde null.
     *
     * @param       name Name des Katalog, der gesucht wird.
    **/
    public static synchronized Versandhauskatalog getKatalog(String name) {
        if (Catalog.existsGlobalCatalog(name)) {
            Catalog c = Catalog.forName(name);
            if (c instanceof Versandhauskatalog) {
                return (Versandhauskatalog) c;
            }
        }
        return null;
    }
}
