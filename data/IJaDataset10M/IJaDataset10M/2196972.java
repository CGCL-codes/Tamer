package com.gochromium.nes.client.emulator;

/**
 * 
 * Base Class for the Mapper Controllers used by NESCafe
 * 
 * @author David de Niese
 * @version 0.56f
 * @final TRUE
 * 
 */
public class Mapper {

    /**
	 * 
	 * <P>
	 * The current Memory Manager.
	 * </p>
	 * 
	 */
    public MemoryManager mm;

    /**
	 * 
	 * <P>
	 * Method to access an address on the Mapper.
	 * </P>
	 * 
	 * @param address
	 *            Address to access.
	 * @param value
	 *            Value to write to the address.
	 * 
	 */
    public void access(int address, int value) {
    }

    /**
	 * 
	 * <P>
	 * Latch Used for MMC5
	 * </P>
	 * 
	 */
    public int PPU_Latch_RenderScreen(int mode, int addr) {
        return 0;
    }

    /**
	 * 
	 * <P>
	 * Method to access an address on the Mapper for writes from 0x4000 to
	 * 0x5FFF.
	 * </P>
	 * 
	 * @param address
	 *            Address to access.
	 * @param value
	 *            Value to write to the address.
	 * 
	 */
    public void accesslow(int address, int value) {
    }

    public int accesslowread(int addr) {
        return (addr >> 8) & 0xFF;
    }

    /**
	 * 
	 * <P>
	 * Determine the number of the Memory Mapper.
	 * </P>
	 * 
	 * @return The number of the Memory Mapper.
	 * 
	 */
    public int getMapperNumber() {
        return 0;
    }

    /**
	 * 
	 * <P>
	 * Method to initialise the Memory Mapper.
	 * </P>
	 * 
	 * @param mm
	 *            Memory Manager to initialise the Mapper with.
	 * 
	 */
    public void init(MemoryManager mm) {
        this.mm = mm;
        reset();
    }

    /**
	 * 
	 * <P>
	 * Latch the Memory Mapper.
	 * </P>
	 * 
	 * @param data
	 *            A Mapper-specific data value that is passed to the Latch.
	 * 
	 */
    public void latch(int data) {
    }

    /**
	 * 
	 * <P>
	 * Reset the Memory Mapper.
	 * </P>
	 * 
	 */
    public void reset() {
        int rombanks = getNum8KRomBanks();
        if (rombanks > 2) {
            setCPUBanks(0, 1, 2, 3);
        } else if (rombanks > 1) {
            setCPUBanks(0, 1, 0, 1);
        } else {
            setCPUBanks(0, 0, 0, 0);
        }
        if (getNum1KVROMBanks() > 0) {
            setPPUBanks(0, 1, 2, 3, 4, 5, 6, 7);
        }
    }

    /**
	 * 
	 * <P>
	 * Syncronise the Memory Mapper Horizontally.
	 * </P>
	 * 
	 * @return Returns non-zero if a Mapper-specific interrupt occurred.
	 * 
	 */
    public int syncH(int scanline) {
        return 0;
    }

    /**
	 * 
	 * <P>
	 * Syncronise the Memory Mapper Vertically.
	 * </P>
	 * 
	 * @return Returns non-zero if a Mapper-specific interrupt occurred.
	 * 
	 */
    public int syncV() {
        return 0;
    }

    /**
	 * 
	 * Determine the Number of 8K ROM Banks in the Program ROM
	 * 
	 */
    public int getNum8KRomBanks() {
        return (mm.getProgramROM().length >> 13);
    }

    /**
	 * 
	 * Determine the Number of 1K ROM Banks in the Video ROM
	 * 
	 */
    public int getNum1KVROMBanks() {
        return (mm.ppu.ppuVROM.length >> 10);
    }

    /**
	 * 
	 * Set the CPU Banks for 0x8000 - 0xFFFF
	 * 
	 */
    public void setCPUBanks(int bank8, int bank9, int bankA, int bankB) {
        mm.setBankStartAddress(0x8, (bank8 << 13));
        mm.setBankStartAddress(0x9, (bank8 << 13) + 0x1000);
        mm.setBankStartAddress(0xA, (bank9 << 13));
        mm.setBankStartAddress(0xB, (bank9 << 13) + 0x1000);
        mm.setBankStartAddress(0xC, (bankA << 13));
        mm.setBankStartAddress(0xD, (bankA << 13) + 0x1000);
        mm.setBankStartAddress(0xE, (bankB << 13));
        mm.setBankStartAddress(0xF, (bankB << 13) + 0x1000);
    }

    /**
	 * 
	 * Set the CPU Banks for 0x8000 - 0x9FFF
	 * 
	 */
    public void setCPUBank8(int bank8) {
        mm.setBankStartAddress(0x8, (bank8 << 13));
        mm.setBankStartAddress(0x9, (bank8 << 13) + 0x1000);
    }

    /**
	 * 
	 * Set the CPU Banks for 0xA000 - 0xBFFF
	 * 
	 */
    public void setCPUBankA(int bankA) {
        mm.setBankStartAddress(0XA, (bankA << 13));
        mm.setBankStartAddress(0XB, (bankA << 13) + 0x1000);
    }

    /**
	 * 
	 * Set the CPU Banks for 0xC000 - 0xDFFF
	 * 
	 */
    public void setCPUBankC(int bankC) {
        mm.setBankStartAddress(0xC, (bankC << 13));
        mm.setBankStartAddress(0xD, (bankC << 13) + 0x1000);
    }

    /**
	 * 
	 * Set the CPU Banks for 0xC000 - 0xDFFF
	 * 
	 */
    public void setCPUBankE(int bankE) {
        mm.setBankStartAddress(0xE, (bankE << 13));
        mm.setBankStartAddress(0xF, (bankE << 13) + 0x1000);
    }

    /**
	 * 
	 * Set the PPU Banks
	 * 
	 */
    public void setPPUBanks(int bank0, int bank1, int bank2, int bank3, int bank4, int bank5, int bank6, int bank7) {
        mm.ppu.setPPUBankStartAddress(0, bank0 << 10);
        mm.ppu.setPPUBankStartAddress(1, bank1 << 10);
        mm.ppu.setPPUBankStartAddress(2, bank2 << 10);
        mm.ppu.setPPUBankStartAddress(3, bank3 << 10);
        mm.ppu.setPPUBankStartAddress(4, bank4 << 10);
        mm.ppu.setPPUBankStartAddress(5, bank5 << 10);
        mm.ppu.setPPUBankStartAddress(6, bank6 << 10);
        mm.ppu.setPPUBankStartAddress(7, bank7 << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 0
	 * 
	 */
    public void setPPUBank0(int bankNum) {
        mm.ppu.setPPUBankStartAddress(0, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 1
	 * 
	 */
    public void setPPUBank1(int bankNum) {
        mm.ppu.setPPUBankStartAddress(1, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 2
	 * 
	 */
    public void setPPUBank2(int bankNum) {
        mm.ppu.setPPUBankStartAddress(2, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 3
	 * 
	 */
    public void setPPUBank3(int bankNum) {
        mm.ppu.setPPUBankStartAddress(3, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 4
	 * 
	 */
    public void setPPUBank4(int bankNum) {
        mm.ppu.setPPUBankStartAddress(4, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 5
	 * 
	 */
    public void setPPUBank5(int bankNum) {
        mm.ppu.setPPUBankStartAddress(5, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 6
	 * 
	 */
    public void setPPUBank6(int bankNum) {
        mm.ppu.setPPUBankStartAddress(6, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 7
	 * 
	 */
    public void setPPUBank7(int bankNum) {
        mm.ppu.setPPUBankStartAddress(7, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 8
	 * 
	 */
    public void setPPUBank8(int bankNum) {
        mm.ppu.setPPUBankStartAddress(8, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank 9
	 * 
	 */
    public void setPPUBank9(int bankNum) {
        mm.ppu.setPPUBankStartAddress(9, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank A
	 * 
	 */
    public void setPPUBankA(int bankNum) {
        mm.ppu.setPPUBankStartAddress(10, bankNum << 10);
    }

    /**
	 * 
	 * Set the PPU Bank B
	 * 
	 */
    public void setPPUBankB(int bankNum) {
        mm.ppu.setPPUBankStartAddress(11, bankNum << 10);
    }

    /**
	 * 
	 * Set Mirroring Vertical
	 * 
	 */
    public void setMirroringVertical() {
        mm.ppu.setMirroring(0, 1, 0, 1);
    }

    /**
	 * 
	 * Set Mirroring Horizontal
	 * 
	 */
    public void setMirroringHorizontal() {
        mm.ppu.setMirroring(0, 0, 1, 1);
    }

    /**
	 * 
	 * Set Mirroring One-Screen High
	 * 
	 */
    public void setMirroringOneScreenHigh() {
        mm.ppu.setMirroring(1, 1, 1, 1);
    }

    /**
	 * 
	 * Set Mirroring One-Screen Low
	 * 
	 */
    public void setMirroringOneScreenLow() {
        mm.ppu.setMirroring(0, 0, 0, 0);
    }

    /**
	 * 
	 * Set Arbitary Mirroring
	 * 
	 */
    public void setMirroring(int a, int b, int c, int d) {
        mm.ppu.setMirroring(a, b, c, d);
    }

    /**
	 * 
	 * Set VRAM Bank
	 * 
	 * Required for Mappers 1,4,5,6,13,19,80,85,96,119
	 * 
	 */
    public void setVRAMBank(int bank, int banknum) {
        mm.ppu.setVRAMBank(bank, banknum);
    }

    /**
	 * 
	 * Provide Mapper with CRC32 for Cartridge
	 * 
	 */
    public void setCRC(long crc) {
    }
}
