package com.seitenbau.testing.addons.dbunit;

import org.dbunit.AbstractDatabaseTester;
import org.dbunit.database.IDatabaseConnection;

/**
 * Interface f�r Connection Provider.
 *
 * <p>Ein Connection Provider stellt die aktuelle Connection zur Datenbank bereit.
 * Gerade w�hrend der Testaufzeichnung ist dies wichtig, da nur so DBUnit auch
 * die 'echten' Daten abgreifen kann, welche innerhalb einer Transaktion vorhanden
 * sind, aber noch nicht commited wurden.
 */
public interface IConnectionProvider {

    /**
   * _
   *
   * @return
   *    Liefert die DBUnit Database Connection. Die Methode wird vom Aufzeichungscode
   *    gerufen, und sollte/muss die aktuelle Verbindung liefern, so dass Daten direkt
   *    aus der Transaktion abgegriffen werden k�nnen.
   */
    IDatabaseConnection getConnection();

    /**
   * _
   *
   * @return
   *    Liefert den DBUnit DatabaseTester, der w�hrend der Testausf�hrung n�tig ist um
   *    um die Datenbank aufzusetzen bzw. zu Vergleichen.
   */
    AbstractDatabaseTester getDatabaseTester();

    /**
   * Setzt die zum Provider zugeh�rigen Konfiguration.
   *
   * XXX: Evtl. in konkrete implementierung schieben, da so auch der Type konkreter wird.
   *
   * @param connectionProviderConfig
   *    Die konkrete Konfigurationsinstanz
   */
    void setConfiguration(IConnectionProviderConfig connectionProviderConfig);
}
