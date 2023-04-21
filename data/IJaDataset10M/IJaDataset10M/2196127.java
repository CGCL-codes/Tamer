package plugins;

import java.util.Vector;

/**
 * DOCUMENT ME!
 *
 * @author thomas.werth
 */
public interface ILigaTabelle {

    /**
     * DOCUMENT ME!
     *
     * @return vector with ILigaTabellenEintrag
     */
    public Vector<ILigaTabellenEintrag> getEintraege();

    /**
     * liefert den Eintrag zu einem Team
     *
     * @param teamId TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public ILigaTabellenEintrag getEintragByTeamId(int teamId);

    /**
     * Getter for property m_iLigaId.
     *
     * @return Value of property m_iLigaId.
     */
    public int getLigaId();

    /**
     * Getter for property m_iLigaLandId.
     *
     * @return Value of property m_iLigaLandId.
     */
    public int getLigaLandId();

    /**
     * Getter for property m_sLigaLandName.
     *
     * @return Value of property m_sLigaLandName.
     */
    public java.lang.String getLigaLandName();

    /**
     * Getter for property m_sLigaName.
     *
     * @return Value of property m_sLigaName.
     */
    public java.lang.String getLigaName();

    /**
     * Getter for property m_iMaxAnzahlSpielklassen.
     *
     * @return Value of property m_iMaxAnzahlSpielklassen.
     */
    public int getMaxAnzahlSpielklassen();

    /**
     * Getter for property m_iSpielklasse.
     *
     * @return Value of property m_iSpielklasse.
     */
    public int getSpielklasse();

    /**
     * liefert tendenz zur Platzierung des Teams im Vergleich mit der angegebenen Tabelle -1 ==
     * abgerutscht, 0 = gleich, 1 == aufgestiegen
     *
     * @param teamId TODO Missing Constructuor Parameter Documentation
     * @param compare TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public byte getTeamPlatzTendenz(int teamId, ILigaTabelle compare);

    public void sort();
}
