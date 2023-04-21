package org.skycastle.texture;

import java.nio.FloatBuffer;
import java.util.Collection;

/**
 * A field consists of one or more channels of data.
 */
public interface Field {

    /**
     * @return number of cells along x axis.
     */
    int getXSize();

    /**
     * @return number of cells along y axis.
     */
    int getYSize();

    /**
     * Resizes the field to the desired size.
     */
    void resize(int xSize, int ySize);

    /**
     * @return read only list with the channels available for this Field.
     */
    Collection<Channel> getChannels();

    /**
     * @return the channel with the specified name, or null if not found.
     */
    Channel getChannel(String channelName);

    /**
     * @return the float buffer for the channel with the specified name, or null if not found.
     */
    FloatBuffer getDataBuffer(String channelName);

    /**
     * Adds the specified Channel.
     *
     * @param addedChannel should not be null or already added.
     */
    void addChannel(Channel addedChannel);

    /**
     * Adds a new Channel with the specified name.
     */
    void addChannel(String newChannelName);

    /**
     * Adds a new Channel with the specified name, filled with the specified default value.
     */
    void addChannel(final String newChannelName, final float defaultValue);

    /**
     * Removes the specified Channel.
     *
     * @param removedChannel should not be null, and should be present.
     */
    void removeChannel(Channel removedChannel);
}
