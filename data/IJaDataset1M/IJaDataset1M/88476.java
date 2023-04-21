package net.jetrix.messages.channel.specials;

import net.jetrix.messages.channel.ChannelMessage;

/**
 * A channel message for special blocks. Specials are attacks applied to
 * another game slot. For specials the first slot is the target of the
 * attack.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 798 $, $Date: 2009-02-18 10:24:28 -0500 (Wed, 18 Feb 2009) $
 */
public abstract class SpecialMessage extends ChannelMessage {

    private int fromSlot;

    private String special;

    public int getFromSlot() {
        return fromSlot;
    }

    public void setFromSlot(int fromSlot) {
        this.fromSlot = fromSlot;
    }
}
