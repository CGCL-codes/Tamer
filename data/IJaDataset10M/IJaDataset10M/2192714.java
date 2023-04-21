package de.mnit.schnipsel.db;

import de.mnit.basis.daten.struktur.container.A_B_DB;
import de.mnit.basis.daten.struktur.container.DBKiste;

/**
 * @author Michael Nitsche
 * 10.06.2010	Erstellt
 */
public class B_Konfig extends A_B_DB {

    public static final B_Konfig info = new B_Konfig();

    public final DBKiste<String> SCHLUESSEL = DBKiste.neuString(this, 256, true);

    public final DBKiste<String> WERT = DBKiste.neuString(this, 256, false);
}
