package jstella.cart;

/**
 * Cartridge class used for Atari's 8K bankswitched games with
 * 128 bytes of RAM.  There are two 4K banks.
 *
 * @author  Bradford W. Mott
 * @version $Id: CartridgeF8SC.java,v 1.2 2007/08/12 04:51:29 mauvila Exp $
 */
public class CartridgeF8SC extends Cartridge {

    private static final long serialVersionUID = 8192869953213524247L;

    private static final int CART_SIZE = 8192;

    private static final String CART_NAME = "CartridgeF8SC";

    private int myCurrentBank = 0;

    private int[] myImage = new int[CART_SIZE];

    private int[] myRAM = new int[128];

    public CartridgeF8SC(int[] image) {
        myImage = copyImage(image);
        randomizeRAM(myRAM);
    }

    public String name() {
        return CART_NAME;
    }

    public String getCartridgeType() {
        return Cartridge.TYPE_F8SC;
    }

    public void reset() {
        setCurrentBank(1);
    }

    public void install(jstella.core.JSSystem system) {
        mySystem = system;
        addIndirectAccess(0x1FF8, 0x2000);
        addDirectPokeAccess(0x1000, 0x1080, myRAM, 0x007F);
        addDirectPeekAccess(0x1080, 0x1100, myRAM, 0x007F);
        setCurrentBank(1);
    }

    public int peek(int address) {
        int zNewAddress = address & 0x0FFF;
        if (!myBankLocked) {
            switch(zNewAddress) {
                case 0x0FF8:
                    setCurrentBank(0);
                    break;
                case 0x0FF9:
                    setCurrentBank(1);
                    break;
                default:
                    break;
            }
        }
        return myImage[myCurrentBank * 4096 + zNewAddress];
    }

    public void poke(int address, int aByteValue) {
        int zNewAddress = address & 0x0FFF;
        if (!myBankLocked) {
            switch(zNewAddress) {
                case 0x0FF8:
                    setCurrentBank(0);
                    break;
                case 0x0FF9:
                    setCurrentBank(1);
                    break;
                default:
                    break;
            }
        }
    }

    protected void setCurrentBank(int bank) {
        if (myBankLocked) {
            return;
        }
        myCurrentBank = bank;
        addDirectPeekAccess(0x1100, 0x1FF8, myImage, 0x0FFF, myCurrentBank << 12);
    }

    protected int getCurrentBank() {
        return myCurrentBank;
    }

    protected int bankCount() {
        return 2;
    }

    public boolean patch(int address, int aValue) {
        address &= 0xfff;
        myImage[myCurrentBank * 4096 + address] = aValue;
        setCurrentBank(myCurrentBank);
        return true;
    }

    public int[] getImage() {
        return myImage;
    }
}
