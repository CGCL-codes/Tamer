package de.genodeftest.jk8055gui.driver;

/**
 * This is the super interface for all hardware channels accessed by
 * {@link de.genodeftest.jk8055gui.JK8055GUI2 JK8055GUI}.
 * 
 * @author "genodeftest (Christian Stadelmann)"
 * @param <T> the dataType of the data of this channel, e.g. Boolean for digital input channel
 *        or output channel}
 */
public interface Channel<T> {

    /**
	 * @author "genodeftest (Christian Stadelmann)"
	 * @since 19.01.2010
	 * @return the name of this {@link Channel}, e.g. "Digital Output Channel 4"
	 */
    public String getName();

    /**
	 * @author "genodeftest (Christian Stadelmann)"
	 * @since 19.01.2010
	 * @return the number of this {@link Channel}, e.g. '4' for Digital Output Channel 4
	 */
    public int getChannelNumber();

    /**
	 * @author "genodeftest (Christian Stadelmann)"
	 * @since 23.01.2010
	 * @return the channelCategory being a parent of this channel. See there for detailed info
	 *         on minimum and maximum values, data type and more info.
	 */
    public ChannelCategory<T> getChannelCategory();
}
