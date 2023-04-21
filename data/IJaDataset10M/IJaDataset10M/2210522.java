package net.kano.joscar.snaccmd.icbm;

import net.kano.joscar.BinaryTools;
import net.kano.joscar.ByteBlock;
import net.kano.joscar.DefensiveTools;
import net.kano.joscar.LiveWritable;
import net.kano.joscar.MiscTools;
import net.kano.joscar.snaccmd.FullUserInfo;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A data structure used to transmit information about messages that could not
 * be sent for some reason.
 */
public class MissedMsgInfo implements LiveWritable {

    /**
     * A reason code indicating that the message was too large according to
     * your {@linkplain SetParamInfoCmd ICBM parameters}.
     */
    public static final int REASON_TOO_LARGE = 0x0001;

    /**
     * A reason code indicating that the message was sent too fast according to
     * your {@linkplain SetParamInfoCmd ICBM parameters}.
     */
    public static final int REASON_TOO_FAST = 0x0002;

    /**
     * A reason code indicating that the sender has a warning level higher than
     * the "sender" warning level set in your {@linkplain SetParamInfoCmd ICBM
     * parameters}.
     */
    public static final int REASON_SENDER_WARNING_LEVEL = 0x0003;

    /**
     * A reason code indicating that your warning level is higher than the
     * sender's "receiver warning level" in his {@linkplain SetParamInfoCmd
     * ICBM parameters}.
     */
    public static final int REASON_YOUR_WARNING_LEVEL = 0x0004;

    /** The ICBM channel on which these messages were missed. */
    private final int channel;

    /** A user information block for the user whose messages were missed. */
    private final FullUserInfo userInfo;

    /** The number of messages missed. */
    private final int number;

    /** A code indicating why the messages were missed. */
    private final int reason;

    /**
     * The total size, in bytes, of this object, as read from a block of binary
     * data.
     */
    private final int totalSize;

    /**
     * Returns a missed message information object read from the given block
     * of bytes.
     *
     * @param block the block of bytes contaoning missed message information
     * @return a missed message information object read from the given block of
     *         data
     */
    @Nullable
    protected static MissedMsgInfo readMissedMsgInfo(ByteBlock block) {
        DefensiveTools.checkNull(block, "block");
        if (block.getLength() < 7) return null;
        int channel = BinaryTools.getUShort(block, 0);
        ByteBlock userBlock = block.subBlock(2);
        FullUserInfo userInfo = FullUserInfo.readUserInfo(userBlock);
        ByteBlock rest = userBlock.subBlock(userInfo.getTotalSize());
        if (rest.getLength() < 4) return null;
        int missed = BinaryTools.getUShort(rest, 0) - 1;
        int reason = BinaryTools.getUShort(rest, 2);
        ByteBlock next = rest.subBlock(4);
        return new MissedMsgInfo(channel, userInfo, missed, reason, next.getOffset() - block.getOffset());
    }

    /**
     * Creates a new missed message block with the given properties.
     *
     * @param channel the ICBM channel on which the message was missed
     * @param userInfo an object describing the user whose messages were missed
     * @param number the number of messages missed
     * @param reason a code indicating the reason the messages were missed,
     *        like {@link #REASON_TOO_LARGE}
     */
    public MissedMsgInfo(int channel, FullUserInfo userInfo, int number, int reason) {
        this(channel, userInfo, number, reason, -1);
    }

    /**
     * Creates a new missed-message information block with the given properties.
     *
     * @param channel the ICBM channel on which the message was missed
     * @param userInfo an object describing the user whose messages were missed
     * @param number the number of messages missed
     * @param reason a code indicating the reason the messages were missed,
     *        like {@link #REASON_TOO_LARGE}
     * @param totalSize the total size of this object, in bytes, as read from
     *        a block of binary data
     */
    private MissedMsgInfo(int channel, FullUserInfo userInfo, int number, int reason, int totalSize) {
        DefensiveTools.checkRange(channel, "channel", 0);
        DefensiveTools.checkRange(number, "number", 0);
        DefensiveTools.checkRange(reason, "reason", 0);
        DefensiveTools.checkRange(totalSize, "totalSize", -1);
        this.channel = channel;
        this.userInfo = userInfo;
        this.number = number;
        this.reason = reason;
        this.totalSize = totalSize;
    }

    /**
     * Returns the ICBM channel on which the messages were missed. Normally
     * {@link net.kano.joscar.snaccmd.AbstractIcbm#CHANNEL_IM}.
     *
     * @return the ICBM channel on which the messages were missed
     */
    public final int getChannel() {
        return channel;
    }

    /**
     * Returns a user information block for the user whose messages were missed.
     *
     * @return user information for the user whose messages were missed
     */
    public final FullUserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * Returns the number of messages missed.
     *
     * @return the number of messages missed
     */
    public final int getNumberMissed() {
        return number;
    }

    /**
     * Returns a code indicating why the messages were missed. Normally one of
     * {{@link #REASON_TOO_FAST}, {@link #REASON_TOO_LARGE}, {@link
     * #REASON_SENDER_WARNING_LEVEL}, and {@link #REASON_YOUR_WARNING_LEVEL}.
     *
     * @return a code indicating why the messages were missed
     */
    public final int getReasonCode() {
        return reason;
    }

    /**
     * Returns the total size of this structure, as read from a block of binary
     * data. Will be <code>-1</code> if this object was not read in from a block
     * of data.
     *
     * @return the total size, in bytes, of this object
     */
    public final int getTotalSize() {
        return totalSize;
    }

    public void write(OutputStream out) throws IOException {
        BinaryTools.writeUShort(out, channel);
        userInfo.write(out);
        BinaryTools.writeUShort(out, number + 1);
        BinaryTools.writeUShort(out, reason);
    }

    public String toString() {
        String reasonValue = MiscTools.findIntField(MissedMsgInfo.class, reason, "REASON_.*");
        if (reasonValue == null) {
            reasonValue = "0x" + Integer.toHexString(reason);
        }
        return "MissedMsgInfo from " + userInfo.getScreenname() + ": " + number + " missed on channel " + channel + ", reason=" + reasonValue;
    }
}
