package net.sourceforge.insim4j.insim.cartracking;

import java.nio.ByteBuffer;
import net.sourceforge.insim4j.i18n.ExceptionMessages;
import net.sourceforge.insim4j.insim.enums.PacketType;
import net.sourceforge.insim4j.insim.general.AbstractInSimPacket;
import net.sourceforge.insim4j.insim.interfaces.IInSimPacket;
import net.sourceforge.insim4j.insim.interfaces.IInSimResponsePacket;
import net.sourceforge.insim4j.utils.FormatUtils;
import net.sourceforge.insim4j.utils.NumberUtils;
import net.sourceforge.insim4j.utils.StringUtils;

/**
 * InSim Multi Car Info packet. <br />
 * From LFS.
 * Multi Car Info - if more than 8 in race then more than one of these is sent.
 * 
 * @author Jiří Sotona
 */
public class InSimMultiCarInfo extends AbstractInSimPacket implements IInSimPacket, IInSimResponsePacket {

    private static final int SIZE = -1;

    private static final PacketType TYPE = PacketType.ISP_MCI;

    private static final NumberUtils NUM_UTILS = NumberUtils.getInstance();

    private static final FormatUtils FORMAT_UTILS = FormatUtils.getInstance();

    private static final StringUtils STRING_UTILS = StringUtils.getInstance();

    /**  number of valid CompCar structs in this packet */
    private int fNumC;

    /** car info for each player, 1 to 8 of these (NumC) */
    private CompCar[] fCompCars;

    /**
   * Constructor.
   * 
   * @param pBuffer
   *                data buffer
   */
    public InSimMultiCarInfo(final ByteBuffer pBuffer) {
        super(pBuffer);
        final byte numCByte = this.getData();
        setNumC(NUM_UTILS.convert2UByte(numCByte));
        final CompCar[] compCars = new CompCar[getNumC()];
        CompCar compCar;
        for (int i = 0; i < getNumC(); i++) {
            compCar = new CompCar(pBuffer);
            compCars[i] = compCar;
        }
        setCompCars(compCars);
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
   * @param pNumC
   * 
   * @throws IllegalArgumentException
   *                 if NumC is not in interval <0, 255> inclusive
   */
    private void setNumC(final int pNumC) {
        if (!NUM_UTILS.isUByte(pNumC)) {
            throw new IllegalArgumentException(FORMAT_UTILS.format(ExceptionMessages.getString("Number.iae.outOfUByteInterval"), "NumC", pNumC));
        }
        fNumC = pNumC;
    }

    /**
   * Setter.
   * 
   * @param pCompCars
   * 
   * @throws IllegalArgumentException
   *                 if pCompCars.length() != getNumC()
   */
    private void setCompCars(final CompCar[] pCompCars) {
        if (pCompCars.length != getNumC()) {
            throw new IllegalArgumentException(FORMAT_UTILS.format(ExceptionMessages.getString("Array.iae.wrongLength"), 32, pCompCars.length));
        }
        fCompCars = pCompCars;
    }

    /**
   * Getter. <br />
   * number of valid CompCar structs in this packet
   * 
   * @return the NumC
   */
    public int getNumC() {
        return fNumC;
    }

    /**
   * Getter. <br />
   * car info for each player, 1 to 8 of these (NumC)
   * 
   * @return the compCars
   */
    public CompCar[] getCompCars() {
        return fCompCars;
    }

    @Override
    public String toString() {
        return super.toString() + " <=> NumP: " + getNumC() + ", CompCars: " + STRING_UTILS.toString(getCompCars());
    }
}
