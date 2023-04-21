package view;

import model.util.*;

/**
 * This interface defines the methods required to get channel information from the model.
 */
public interface IChannelValueGetter {

    /**
   * This method will return the value for the channel with the given address.
   * 
   * @param address The address of the channel value to return.
   */
    public short getChannelValue(short address);

    /**
   * This method will return all current channels with non-zero values. 
   * 
   * @return The array of all channels with non-zero values.
   */
    public Channel[] getChannels();

    /**
   * This method will return all current channels with non-zero values or a fader value of -100.
   * 
   * @return The array of all channels with non-zero or fadervalue = -100 values.
   */
    public Channel[] getChannelsForCue();

    /**
   * This method will return the values of the given channel addresses.
   * 
   * @param addresses The addresses to return the channel value for.
   * @return The array of values corresponding to the given addresses.
   */
    public short[] getChannelValues(short[] addresses);
}
