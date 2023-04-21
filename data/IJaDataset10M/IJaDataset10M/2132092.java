package com.astromine.mp3;

import java.util.List;

/**
 * @author stephen
 *
 */
public interface Translator {

    /**
     * Removes streams that the meta data indicates is unavailable
     */
    public void cleanUnavailable();

    /**
     * Composes the contents of a ASX play list
     * @return a ASX play list suitable for the Windows Media Player
     */
    public String composeASX();

    /**
     * Composes the contents of a ASX play list
     * @param count the maximum number of streams to include
     * @return a ASX play list suitable for the Windows Media Player
     */
    public String composeASX(int count);

    /**
     * Composes the contents of a M3U play list
     * @return a M3U play list suitable for the WINAMP or ITUNES media players
     */
    public String composeM3U();

    /**
     * Composes the contents of a M3U play list
     * @param count the maximum number of streams to include
     * @return a M3U play list suitable for the WINAMP or ITUNES media players
     */
    public String composeM3U(int count);

    /**
     * Composes the contents of a PLS play list
     * @return a PLS file suitable for the WINAMP or ITUNES media players
     */
    public String composePLS();

    /**
     * Composes the contents of a PLS play list
     * @param count the maximum number of streams to include
     * @return a PLS file suitable for the WINAMP or ITUNES media players
     */
    public String composePLS(int count);

    /**
     * Composes the contents of an SL play list
     * @return an SL play list suitable for the SL tuner
     */
    public String composeSL();

    /**
     * Composes the contents of an SL play list
     * @param count the maximum number of streams to include
     * @return an SL play list suitable for the SL tuner
     */
    public String composeSL(int count);

    /**
     * Retrieves the meta data from all the streams in a play list
     * @return A list of MP3metadata
     */
    public List<MP3metadata> getMetadata();

    /**
     * Retrieves the the streams in a play list
     * @return A list of URLs contained in the play list
     */
    public List<String> getStreams();

    /**
     * Decomposes a play list into its component streams
     * @return A list of the URL pointing to MP3 streams
     */
    public List<String> parseStreams();

    /**
     * Removes a stream from the play list. 
     * If the meta data is available its record is also removed
     * @param streamURL the address of the URL to be removed
     */
    public void removeStream(String streamURL);

    /**
     * Sets the MP3 meta data for the streams in a play list
     * @param metadata a list of MP3metadata for the play list
     */
    public void setMetadata(List<MP3metadata> metadata);

    /**
     * Sets the streams in a play list
     * @param streams a list of String containing URLs for an MP3 streams
     */
    public void setStreams(List<String> streams);
}
