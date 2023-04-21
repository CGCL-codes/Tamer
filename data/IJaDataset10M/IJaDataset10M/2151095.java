package org.charvolant.tmsnet.networks;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A channel identification within a network.
 * <p>
 * Channels have a single logical channel number.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelIdentification extends AbstractIdentifiable<NetworkIdentification> {

    /** The logical channel number */
    @XmlElement(name = "lcn")
    private int logicalChannelNumber;

    /**
   * Construct a channel identification.
   *
   */
    public ChannelIdentification() {
    }

    /**
   * Get the logical channel number.
   *
   * @return the logical channel number
   */
    public int getLogicalChannelNumber() {
        return this.logicalChannelNumber;
    }

    /**
   * Set the logical channel number.
   *
   * @param logicalChannelNumber the logical channel number to set
   */
    public void setLogicalChannelNumber(int logicalChannelNumber) {
        this.logicalChannelNumber = logicalChannelNumber;
    }
}
