package apple.core;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

public class Memory {

    static Logger logger = Logger.getLogger(Memory.class);

    public static final int MEMORY_SIZE = 0x10000;

    private byte[] memory = new byte[MEMORY_SIZE];

    private byte[] bankMemory = new byte[0x3000];

    private byte[] bankD1Memory = new byte[0x1000];

    public static final short KEYBOARD_DATA_STROBE = (short) 0xC000;

    public static final short KEYBOARD_DOWN_FLAG = (short) 0xC010;

    public static final short DISPLAY_ALTCHAR_OFF = (short) 0xC00E;

    public static final short DISPLAY_ALTCHAR_ON = (short) 0xC00F;

    public static final short DISPLAY_RDALTCHAR = (short) 0xC01E;

    public static final short DISPLAY_80COL_OFF = (short) 0xC00C;

    public static final short DISPLAY_80COL_ON = (short) 0xC00D;

    public static final short DISPLAY_RD80COL = (short) 0xC01F;

    public static final short DISPLAY_80STORE_OFF = (short) 0xC000;

    public static final short DISPLAY_80STORE_ON = (short) 0xC001;

    public static final short DISPLAY_RD80STORE = (short) 0xC018;

    public static final short DISPLAY_PAGE2_OFF = (short) 0xC054;

    public static final short DISPLAY_PAGE2_ON = (short) 0xC055;

    public static final short DISPLAY_RDPAGE2 = (short) 0xC01C;

    public static final short DISPLAY_TEXT_OFF = (short) 0xC050;

    public static final short DISPLAY_TEXT_ON = (short) 0xC051;

    public static final short DISPLAY_RDTEXT = (short) 0xC01A;

    public static final short DISPLAY_MIXED_OFF = (short) 0xC052;

    public static final short DISPLAY_MIXED_ON = (short) 0xC053;

    public static final short DISPLAY_RDMIXED = (short) 0xC01B;

    public static final short DISPLAY_HIRES_OFF = (short) 0xC056;

    public static final short DISPLAY_HIRES_ON = (short) 0xC057;

    public static final short DISPLAY_RDHIRES = (short) 0xC01D;

    public static final short DISPLAY_IOUDIS_ON = (short) 0xC07E;

    public static final short DISPLAY_IOUDIS_OFF = (short) 0xC07F;

    public static final short DISPLAY_RDIOUDIS = (short) 0xC07E;

    public static final short DISPLAY_DHIRES_ON = (short) 0xC05E;

    public static final short DISPLAY_DHIRES_OFF = (short) 0xC05F;

    public static final short DISPLAY_RDDHIRES = (short) 0xC07F;

    public static final short SPEAKER_TOGGLE = (short) 0xC030;

    public static final short MEMORY_RDBNK2 = (short) 0xC011;

    public static final short MEMORY_RDLCRAM = (short) 0xC012;

    public static final short MEMORY_ALTZP_OFF = (short) 0xC008;

    public static final short MEMORY_ALTZP_ON = (short) 0xC009;

    public static final short MEMORY_RDALTZP = (short) 0xC016;

    private boolean BNK2 = true;

    private boolean LCRAM = false;

    private boolean ALTZP = false;

    private boolean RAM_WRITE_ENABLE = true;

    private KeyboardDecoder keyboardDecoder;

    private VideoGenerator video;

    private Speaker speaker;

    private DiskII disk;

    public Memory() {
    }

    public Memory(KeyboardDecoder keyboardDecoder, VideoGenerator video, Speaker speaker, DiskII disk) {
        this.keyboardDecoder = keyboardDecoder;
        this.video = video;
        this.video.setMemory(this);
        this.speaker = speaker;
        this.disk = disk;
    }

    public byte read(short address) {
        int unsignedAddress = address & 0xFFFF;
        if (unsignedAddress < 0xC000) {
            return memory[unsignedAddress];
        } else if (address == KEYBOARD_DATA_STROBE) {
            return keyboardDecoder.getKeyboardDataAndStrobe();
        } else if (address == KEYBOARD_DOWN_FLAG) {
            return keyboardDecoder.getAnyKeyDownAndClearStrobe();
        } else if (address == DISPLAY_RDALTCHAR) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch.ALTCHAR);
        } else if (address == DISPLAY_RD80COL) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch._80COL);
        } else if (address == DISPLAY_RD80STORE) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch._80STORE);
        } else if (address == DISPLAY_PAGE2_OFF) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.PAGE2, false);
        } else if (address == DISPLAY_PAGE2_ON) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.PAGE2, true);
        } else if (address == DISPLAY_RDPAGE2) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch.PAGE2);
        } else if (address == DISPLAY_TEXT_OFF) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.TEXT, false);
        } else if (address == DISPLAY_TEXT_ON) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.TEXT, true);
        } else if (address == DISPLAY_RDTEXT) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch.TEXT);
        } else if (address == DISPLAY_MIXED_OFF) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.MIXED, false);
        } else if (address == DISPLAY_MIXED_ON) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.MIXED, true);
        } else if (address == DISPLAY_RDMIXED) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch.MIXED);
        } else if (address == DISPLAY_HIRES_OFF) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.HIRES, false);
        } else if (address == DISPLAY_HIRES_ON) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.HIRES, true);
        } else if (address == DISPLAY_RDHIRES) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch.HIRES);
        } else if (address == DISPLAY_RDIOUDIS) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch.IOUDIS);
        } else if (address == DISPLAY_DHIRES_ON) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.DHIRES, true);
        } else if (address == DISPLAY_DHIRES_OFF) {
            return video.setSoftSwitch(VideoGenerator.SoftSwitch.DHIRES, false);
        } else if (address == DISPLAY_RDDHIRES) {
            return video.readSoftSwitch(VideoGenerator.SoftSwitch.DHIRES);
        } else if (address == SPEAKER_TOGGLE) {
            return speaker.toggle();
        } else if (address == MEMORY_RDBNK2) {
            return getSoftSwitchByte(BNK2);
        } else if (address == MEMORY_RDLCRAM) {
            return getSoftSwitchByte(LCRAM);
        } else if (address == MEMORY_RDALTZP) {
            return getSoftSwitchByte(ALTZP);
        } else if ((unsignedAddress & 0xFFF0) == 0xC080) {
            return manageMemoryBanks(address);
        } else if ((unsignedAddress & 0xFFF0) == 0xC0E0) {
            return disk.toggle((byte) (unsignedAddress & 0x000F));
        } else if ((unsignedAddress & 0xFF00) == 0xC600) {
            return DiskII.readProm((byte) unsignedAddress);
        } else if (unsignedAddress < 0xD000) {
            logger.debug(String.format("Read: %04X", unsignedAddress));
            return memory[unsignedAddress];
        } else {
            if (LCRAM) {
                if (unsignedAddress < 0xE000 && !BNK2) {
                    return bankD1Memory[unsignedAddress & 0x0FFF];
                } else {
                    return bankMemory[unsignedAddress - 0xD000];
                }
            } else {
                return memory[unsignedAddress];
            }
        }
    }

    public void write(short address, byte datum) {
        int unsignedAddress = address & 0xFFFF;
        if (unsignedAddress < 0xC000) {
            memory[unsignedAddress] = datum;
        } else if (address == KEYBOARD_DOWN_FLAG) {
            keyboardDecoder.getAnyKeyDownAndClearStrobe();
        } else if (address == DISPLAY_ALTCHAR_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.ALTCHAR, false);
        } else if (address == DISPLAY_ALTCHAR_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.ALTCHAR, true);
        } else if (address == DISPLAY_80COL_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch._80COL, false);
        } else if (address == DISPLAY_80COL_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch._80COL, true);
        } else if (address == DISPLAY_80STORE_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch._80STORE, false);
        } else if (address == DISPLAY_80STORE_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch._80STORE, true);
        } else if (address == DISPLAY_PAGE2_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.PAGE2, false);
        } else if (address == DISPLAY_PAGE2_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.PAGE2, true);
        } else if (address == DISPLAY_TEXT_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.TEXT, false);
        } else if (address == DISPLAY_TEXT_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.TEXT, true);
        } else if (address == DISPLAY_MIXED_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.MIXED, false);
        } else if (address == DISPLAY_MIXED_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.MIXED, true);
        } else if (address == DISPLAY_HIRES_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.HIRES, false);
        } else if (address == DISPLAY_HIRES_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.HIRES, true);
        } else if (address == DISPLAY_IOUDIS_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.IOUDIS, true);
        } else if (address == DISPLAY_IOUDIS_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.IOUDIS, false);
        } else if (address == DISPLAY_DHIRES_ON) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.DHIRES, true);
        } else if (address == DISPLAY_DHIRES_OFF) {
            video.setSoftSwitch(VideoGenerator.SoftSwitch.DHIRES, false);
        } else if (address == MEMORY_ALTZP_OFF) {
            ALTZP = false;
        } else if (address == MEMORY_ALTZP_ON) {
            ALTZP = true;
        } else if (unsignedAddress < 0xD000) {
            logger.debug(String.format("Write: %04X", unsignedAddress));
        } else {
            if (RAM_WRITE_ENABLE) {
                if (unsignedAddress < 0xE000 && !BNK2) {
                    bankD1Memory[unsignedAddress & 0x0FFF] = datum;
                } else {
                    bankMemory[unsignedAddress - 0xD000] = datum;
                }
            } else {
                logger.debug(String.format("Write not enabled: %04X : %02X", unsignedAddress, datum));
                System.exit(-1);
            }
        }
    }

    private byte getSoftSwitchByte(boolean bit7) {
        return bit7 ? (byte) 0x80 : 0x0;
    }

    private byte manageMemoryBanks(short address) {
        assert (address & 0xFFF0) == 0xC080;
        BNK2 = (address & 0x0008) == 0x0000;
        int function = address & 0x0003;
        switch(function) {
            case 0:
                LCRAM = true;
                RAM_WRITE_ENABLE = false;
                break;
            case 1:
                LCRAM = false;
                RAM_WRITE_ENABLE = true;
                break;
            case 2:
                LCRAM = false;
                RAM_WRITE_ENABLE = false;
                break;
            case 3:
                LCRAM = true;
                RAM_WRITE_ENABLE = true;
                break;
            default:
                assert false : String.format("%04X: unable to determine memory bank function%n", address);
                break;
        }
        return 0x00;
    }

    /**
    * Read a 2-byte little-endian word starting at address.
    * 
    * @param address
    *           the address of the low byte of the 2-byte word to be read
    * @return a two-byte word
    */
    public short readWord(short address) {
        byte lowByte = read(address);
        byte highByte = read((short) (address + 1));
        short word = (short) (highByte << 8 | lowByte & 0xFF);
        return word;
    }

    public void initialize(String ROMFile) throws IOException {
        load(ROMFile, 0xD000, 0x3000);
    }

    public void load(String filename, int offset, int length) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            fis.read(memory, offset, length);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public byte[] read(short startAddress, int numBytes) {
        byte[] byteArray = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) {
            int offset = (startAddress + i) & 0xFFFF;
            byteArray[i] = memory[offset];
        }
        return byteArray;
    }

    public void dump(int startPage, int numPages) {
        for (int i = 0; i < numPages * 0x10; i++) {
            int base = startPage * 0x100 + i * 0x10;
            System.out.format("%04X : ", base);
            for (int j = 0xF; j >= 0; j--) {
                System.out.format("%02X ", (int) memory[base + j] & 0xFF);
            }
            System.out.println();
        }
    }
}
