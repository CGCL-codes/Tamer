package org.charvolant.tmsnet.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import org.charvolant.properties.annotations.Property;

/**
 * A list of channels for a particular channel type.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelList extends ContextHolder<PVRState> {

    /** The type of channel this list is for */
    @XmlAttribute
    @Property
    private ChannelType type;

    /** The list of channels */
    @XmlElementRef
    @Property
    private List<ChannelInformation> channels;

    /**
   * Construct a channel list.
   *
   * @param context The context
   * @param type The channel type
   * @param channels The list of channels
   */
    public ChannelList(PVRState context, ChannelType type, List<ChannelInformation> channels) {
        super(context);
        this.type = type;
        this.setChannels(channels);
    }

    /**
   * Construct an empty channel list.
   *
   * @param context The context
   * @param type The channel type
   */
    public ChannelList(PVRState context, ChannelType type) {
        this(context, type, new ArrayList<ChannelInformation>());
    }

    /**
   * Construct an empty channel list.
   *
   * @param type The channel type
   */
    public ChannelList() {
        this(null, ChannelType.TV);
    }

    /**
   * Get the channels.
   *
   * @return the channels
   */
    public List<ChannelInformation> getChannels() {
        return this.channels;
    }

    /**
   * Set the channels.
   *
   * @param channels the channels to set
   */
    public void setChannels(List<ChannelInformation> channels) {
        for (ChannelInformation channel : channels) channel.setContext(this);
        this.channels = channels;
    }

    /** 
   * Get a channel by index.
   * 
   * @param index The channel index
   * 
   * @return The channel or null for not found
   */
    public ChannelInformation getChannel(int index) {
        return index >= this.channels.size() ? null : this.channels.get(index);
    }

    /**
   * Get a channel by service id.
   * <p>
   * It should really be onid/sid, but that information is sometimes hard to come by.
   * 
   * @param sid The service id
   * 
   * @return The channel, or null for not found
   */
    public ChannelInformation findChannel(int sid) {
        for (ChannelInformation channel : this.channels) if (channel.getServiceId() == sid) return channel;
        return null;
    }

    /**
   * Get the type.
   *
   * @return the type
   */
    public ChannelType getType() {
        return this.type;
    }

    /**
   * Set the type.
   *
   * @param type the type to set
   */
    public void setType(ChannelType type) {
        this.type = type;
    }
}
