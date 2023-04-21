package jstella.cart;

import jstella.core.*;

/**
This is the cartridge class for Tigervision's bankswitched 
games.  In this bankswitching scheme the 2600's 4K cartridge 
address space is broken into two 2K segments.  The last 2K 
segment always points to the last 2K of the ROM image.  The 
desired bank number of the first 2K segment is selected by 
storing its value into $3F.  Actually, any write to location
$00 to $3F will change banks.  Although, the Tigervision games 
only used 8K this bankswitching scheme supports up to 512K.

@author  Bradford W. Mott
@version $Id: Cartridge3F.java,v 1.3 2007/10/22 09:01:13 mauvila Exp $
 */
public class Cartridge3F extends Cartridge {

    private static final long serialVersionUID = 4078181695753196546L;

    private int myCurrentBank;

    private int[] myImage;

    Cartridge3F(int[] image) {
        myImage = copyImage(image);
    }

    public String name() {
        return "Cartridge3F";
    }

    public String getCartridgeType() {
        return Cartridge.TYPE_3F;
    }

    public void reset() {
        setCurrentBank(0);
    }

    public void install(JSSystem system) {
        mySystem = system;
        addIndirectAccess(0x00, 0x40);
        addDirectPeekAccess(0x1800, 0x2000, myImage, 0x07FF, myImage.length - 2048);
        setCurrentBank(0);
    }

    public int peek(int address) {
        address = address & 0x0FFF;
        if (address < 0x0800) {
            return myImage[(address & 0x07FF) + myCurrentBank * 2048];
        } else {
            return myImage[(address & 0x07FF) + myImage.length - 2048];
        }
    }

    public void poke(int address, int value) {
        address = address & 0x0FFF;
        if (address <= 0x003F) {
            setCurrentBank(value);
        }
        myConsole.getTIA().poke(address, value);
    }

    protected void setCurrentBank(int bank) {
        if (myBankLocked == true) {
            return;
        }
        if (bank * 2048 < myImage.length) {
            myCurrentBank = bank;
        } else {
            myCurrentBank = bank % (myImage.length / 2048);
        }
        addDirectPeekAccess(0x1000, 0x1800, myImage, 0x07FF, myCurrentBank * 2048);
    }

    protected int getCurrentBank() {
        return myCurrentBank;
    }

    protected int bankCount() {
        return myImage.length / 2048;
    }

    public boolean patch(int address, int value) {
        address = address & 0x0FFF;
        if (address < 0x0800) {
            myImage[(address & 0x07FF) + myCurrentBank * 2048] = value;
        } else {
            myImage[(address & 0x07FF) + myImage.length - 2048] = value;
        }
        return true;
    }

    protected int[] getImage() {
        return myImage;
    }
}
