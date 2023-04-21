package de.mnit.basis.daten.struktur.container;

import de.mnit.basis.daten.struktur.container.DBKiste;

/**
 * @author Michael Nitsche
 * 06.11.2008	Erstellt
 */
public abstract class A_B_DB extends A_DBKistenstapel {

    public final DBKiste<Integer> ID = DBKiste.neuPrimaerSchluessel(this, true);
}
