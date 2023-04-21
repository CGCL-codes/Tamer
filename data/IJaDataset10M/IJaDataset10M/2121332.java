package net.sourceforge.insim4j.insim.messages;

import java.nio.ByteBuffer;
import java.util.Set;
import net.sourceforge.insim4j.insim.enums.PacketType;
import net.sourceforge.insim4j.insim.flags.FlagSet;
import net.sourceforge.insim4j.insim.flags.KeyModifierFlag;
import net.sourceforge.insim4j.insim.general.AbstractInSimPacket;
import net.sourceforge.insim4j.insim.interfaces.IInSimPacket;
import net.sourceforge.insim4j.insim.interfaces.IInSimRequestPacket;
import net.sourceforge.insim4j.utils.StringUtils;

/**
 * InSim Single CHaracter packet. <br />
 * To LFS. Send to simulate single character
 * 
 * @author Jiří Sotona
 */
public class InSimSingleChar extends AbstractInSimPacket implements IInSimPacket, IInSimRequestPacket {

    private static final int SIZE = 8;

    private static final PacketType TYPE = PacketType.ISP_SCH;

    private static final StringUtils STRING_UTILS = StringUtils.getInstance();

    /** key to press */
    private char fChar;

    /** bit 0 : SHIFT / bit 1 : CTRL */
    private FlagSet<KeyModifierFlag> fFlagSet;

    private Set<KeyModifierFlag> fFlags;

    /** spare2 */
    private byte fSpare2;

    /** spare3 */
    private byte fSpare3;

    /**
   * Constructor.
   * 
   * @param pChar
   *                key to press
   * @param pFlags
   *                KeyModifierFlag.SHIFT / KeyModifierFlag.CTRL
   */
    public InSimSingleChar(final char pChar, final Set<KeyModifierFlag> pFlags) {
        super(SIZE, TYPE, 0, (byte) 0);
        setChar(pChar);
        setFlags(new FlagSet<KeyModifierFlag>(pFlags));
        setSpare2((byte) 0);
        setSpare3((byte) 0);
    }

    /**
   * @see net.sourceforge.insim4j.insim.general.AbstractInSimPacket#getSIZE()
   */
    @Override
    protected int getSIZE() {
        return SIZE;
    }

    /**
   * @see net.sourceforge.insim4j.insim.general.AbstractInSimPacket#getTYPE()
   */
    @Override
    protected PacketType getTYPE() {
        return TYPE;
    }

    /**
   * Setter.
   * 
   * @param pChar
   *                key to press
   */
    private void setChar(final char pChar) {
        fChar = pChar;
    }

    /**
   * Setter.
   * 
   * @param pFlags
   *                key to press
   */
    private void setFlags(final FlagSet<KeyModifierFlag> pFlags) {
        fFlagSet = pFlags;
    }

    /**
   * @param pSpare2
   *                the spare2 to set
   */
    private void setSpare2(final byte pSpare2) {
        fSpare2 = pSpare2;
    }

    /**
   * @param pSpare3
   *                the spare3 to set
   */
    private void setSpare3(final byte pSpare3) {
        fSpare3 = pSpare3;
    }

    /**
   * Getter.
   * 
   * @return the character
   */
    public char getChar() {
        return fChar;
    }

    /**
   * Getter.
   * 
   * @return flagSet
   */
    private FlagSet<KeyModifierFlag> getFlagSet() {
        return fFlagSet;
    }

    /**
   * Getter. see {@link KeyModifierFlag}
   * 
   * @return flags
   */
    public Set<KeyModifierFlag> getFlags() {
        if (fFlags == null) {
            fFlags = KeyModifierFlag.resolveFlags(getFlagSet());
        }
        return fFlags;
    }

    /**
   * Getter.
   * 
   * @return spare 2
   */
    public byte getSpare2() {
        return fSpare2;
    }

    /**
   * Getter.
   * 
   * @return spare 3
   */
    public byte getSpare3() {
        return fSpare3;
    }

    private void compileTo(final ByteBuffer pBuf) {
        pBuf.put((byte) getChar());
        pBuf.put((byte) getFlagSet().getValue());
        pBuf.put(getSpare2());
        pBuf.put(getSpare3());
    }

    @Override
    public ByteBuffer compile() {
        final ByteBuffer buf = super.compile();
        compileTo(buf);
        return buf;
    }

    @Override
    public String toString() {
        return super.toString() + ", Char: " + getChar() + ", Flags: " + STRING_UTILS.toString(getFlags()) + ", Spare2: " + getSpare2() + ", Spare3: " + getSpare3();
    }
}
