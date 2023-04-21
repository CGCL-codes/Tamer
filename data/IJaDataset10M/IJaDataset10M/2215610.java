package ru.pit.tvlist.epgparser;

import java.util.GregorianCalendar;
import java.util.List;
import ru.pit.tvlist.epgparser.data.BroadcastData;
import ru.pit.tvlist.epgparser.exceptions.EPGParserException;

public interface IEPGSource {

    /**
     * Parse channel source html data to List of {@link BroadcastData}
     * @param channelSource channel html source
     * @param date date for this html source
     * @return List of {@link BroadcastData}
     */
    public List parseChannelSource(String channelSource, GregorianCalendar date) throws EPGParserException;

    /**
     * Fill description for broadcast
     * @param broadcastData {@link BroadcastData}
     * @param broadcastSource broadcast html source
     */
    public void fillBroadcastData(BroadcastData broadcastData, String broadcastSource) throws EPGParserException;

    /**
     * Generates URL for given channel extId and date
     * @param extId channel external id
     * @param date date
     * @return channel URL
     */
    public String getChannelUrl(String extId, GregorianCalendar date) throws EPGParserException;

    /**
     * Generates URL for given broadcast extId and date
     * @param extId broadcast external id
     * @param date date
     * @return broadcast URL
     */
    public String getBroadcastUrl(String extId) throws EPGParserException;
}
