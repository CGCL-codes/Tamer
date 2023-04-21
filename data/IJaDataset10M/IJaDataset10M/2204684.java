package plugins;

/**
 * EPV Manager Interface
 *
 * @author draghetto
 */
public interface IEPV {

    /**
     * DOCUMENT ME!
     *
     * @param spieler Spieler object to convert for EPV use
     *
     * @return IEPVData object with the spieler data
     */
    public IEPVData getEPVData(ISpieler spieler);

    /**
	 * DOCUMENT ME!
	 *
	 * @param IEPVData Spieler object to convert for EPV use
	 *
	 * @return IEPVData object with the spieler data
	 */
    public IEPVData getEPVData(IPlayerData player);

    /**
     * Returns the estimated player value at the current week
     *
     * @param data the data holder
     *
     * @return The price of the player
     */
    public double getPrice(IEPVData data);

    /**
     * Returns the estimated player value at the specified week
     *
     * @param data the data holder
     * @param week the week
     *
     * @return The price of the player at the week
     */
    public double getPrice(IEPVData data, int week);
}
