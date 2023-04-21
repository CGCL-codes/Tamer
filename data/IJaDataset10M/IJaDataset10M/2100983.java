package plugins;

/**
 * DOCUMENT ME!
 *
 * @author thomas.werth
 */
public interface IMatchLineupTeam {

    /**
     * Getter for property m_vAufstellung.
     *
     * @return Value of property m_vAufstellung.
     */
    public java.util.Vector<IMatchLineupPlayer> getAufstellung();

    /**
     * Getter for property m_iErfahrung.
     *
     * @return Value of property m_iErfahrung.
     */
    public int getErfahrung();

    /**
     * Liefert Einen Spieler per ID aus der Aufstellung
     *
     * @param id TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public IMatchLineupPlayer getPlayerByID(int id);

    /**
     * Liefert Einen Spieler per PositionsID aus der Aufstellung
     *
     * @param id TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public IMatchLineupPlayer getPlayerByPosition(int id);

    /**
     * Getter for property m_iTeamID.
     *
     * @return Value of property m_iTeamID.
     */
    public int getTeamID();

    /**
     * Getter for property m_sTeamName.
     *
     * @return Value of property m_sTeamName.
     */
    public java.lang.String getTeamName();

    /**
     * determinates played System
     *
     * @return TODO Missing Return Method Documentation
     */
    public byte determinateSystem();

    /**
     * Returns a vector with substitutions. Sorted on minute, orderID.
     *
     * @return TODO Missing Return Method Documentation
     */
    public java.util.ArrayList<ISubstitution> getSubstitutions();
}
