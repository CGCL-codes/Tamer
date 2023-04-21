package libsidplay.components.cart;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class SDCard {

    protected static final byte MMC_CARD_IDLE = 0;

    protected static final byte MMC_CARD_RESET = 1;

    protected static final byte MMC_CARD_INIT = 2;

    protected static final byte MMC_CARD_READ = 3;

    protected static final byte MMC_CARD_DUMMY_READ = 4;

    protected static final byte MMC_CARD_WRITE = 5;

    protected static final byte MMC_CARD_DUMMY_WRITE = 6;

    protected static final byte MMC_CARD_RETURN_WRITE = 7;

    protected static final byte MMC_CARD_INSERTED = 0;

    protected static final byte MMC_CARD_NOTINSERTED = 1;

    protected static final byte MMC_SPIMODE_READ = 1;

    protected static final int CARD_TYPE_MMC = 1;

    protected static final int CARD_TYPE_SDHC = 3;

    protected int cardType = CARD_TYPE_MMC;

    protected boolean cardRw;

    /**
	 * Image file.
	 */
    protected RandomAccessFile imageFile;

    /**
	 * Pointer inside image.
	 */
    protected long imagePointer;

    /**
	 * write sequence counter.
	 */
    protected int writeSequence;

    protected byte cardInserted;

    protected byte cardState;

    protected byte cardResetCount;

    protected int blockSize;

    protected int readFirstbyte;

    /**
	 * Command buffer.
	 */
    protected byte[] cmdBuffer = new byte[9];

    protected int cmdBufferPointer;

    protected void clearCmdBuffer() {
        Arrays.fill(cmdBuffer, (byte) 0);
        cmdBufferPointer = 0;
    }

    protected int readBufferReadptr, readBufferWriteptr;

    byte[] readBuffer = new byte[0x1000];

    protected void readBufferSet(byte[] data, int size) {
        int dataPos = 0;
        while (size != 0) {
            byte value = data[dataPos++];
            readBuffer[readBufferWriteptr] = value;
            readBufferWriteptr++;
            readBufferWriteptr &= 0xfff;
            size--;
        }
    }

    protected byte readBufferGetbyte() {
        byte value = 0;
        if (readBufferReadptr != readBufferWriteptr) {
            value = readBuffer[readBufferReadptr];
            readBufferReadptr++;
            readBufferReadptr &= 0xfff;
        }
        return value;
    }

    protected void resetCard() {
        triggerModeWrite((byte) 0);
        cardSelectedWrite((byte) 0);
        cardResetCount = 0;
        imagePointer = 0;
        blockSize = 512;
        clearCmdBuffer();
    }

    public byte cardInserted() {
        return cardInserted;
    }

    byte setCardInserted(byte value) {
        byte oldvalue = cardInserted();
        cardInserted = value;
        return oldvalue;
    }

    public int setCardType(int value) {
        int oldvalue = cardType;
        cardType = value;
        return oldvalue;
    }

    public byte mmcBusy() {
        return 0;
    }

    public boolean cardWriteEnabled() {
        return cardRw;
    }

    protected byte cardSelected = 0;

    public byte cardSelectedRead() {
        return cardSelected;
    }

    public void cardSelectedWrite(byte value) {
        cardSelected = value;
    }

    public byte enable8mhzRead() {
        return 0;
    }

    public void spi_mmc_enable_8mhz_write(byte value) {
    }

    protected byte triggerMode = 0;

    public byte triggerModeRead() {
        return triggerMode;
    }

    public void triggerModeWrite(byte value) {
        triggerMode = value;
    }

    public byte dataRead() {
        switch(cardState) {
            case MMC_CARD_RETURN_WRITE:
                cardState = MMC_CARD_IDLE;
                return (byte) 0xff;
            case MMC_CARD_RESET:
                switch(cardResetCount) {
                    case 0:
                        cardResetCount++;
                        return 0x00;
                    case 1:
                        cardResetCount++;
                        return 0x01;
                    case 2:
                        cardResetCount++;
                        return 0x01;
                    case 3:
                        cardResetCount++;
                        return 0x00;
                    case 4:
                        cardResetCount++;
                        return 0x01;
                    case 5:
                        cardResetCount = 0;
                        return 0x01;
                }
                break;
            case MMC_CARD_INIT:
                return 0x00;
            case MMC_CARD_READ:
            case MMC_CARD_DUMMY_READ:
                if (triggerModeRead() == MMC_SPIMODE_READ) {
                    if (readFirstbyte != blockSize + 5) {
                        readFirstbyte++;
                    }
                    if (readFirstbyte == blockSize + 3) {
                        return 0x00;
                    }
                    if (readFirstbyte == blockSize + 4) {
                        return 0x01;
                    }
                    if (readFirstbyte == blockSize + 5) {
                        return 0x00;
                    }
                } else {
                    if (readFirstbyte != blockSize + 2) {
                        readFirstbyte++;
                    }
                    if (readFirstbyte == blockSize + 1) {
                        return 0x00;
                    }
                    if (readFirstbyte == blockSize + 2) {
                        return 0x01;
                    }
                }
                if (readFirstbyte == 0) {
                    return (byte) 0xFF;
                }
                if (readFirstbyte == 1) {
                    return (byte) 0xFE;
                }
                if (readFirstbyte == 2 && triggerModeRead() == MMC_SPIMODE_READ) {
                    return (byte) 0xFE;
                }
                if (0 == cardInserted() && cardState != MMC_CARD_DUMMY_READ) {
                    byte val = readBufferGetbyte();
                    return val;
                } else {
                    return 0x00;
                }
        }
        return 0;
    }

    protected long getAddr() {
        long addr;
        if (cardType == CARD_TYPE_SDHC) {
            addr = (cmdBuffer[5] * 0x100L) + (cmdBuffer[4] * 0x10000L) + (cmdBuffer[3] * 0x1000000L) + (cmdBuffer[2] * 0x100000000L);
            addr <<= 1;
        } else {
            addr = cmdBuffer[5] + (cmdBuffer[4] * 0x100) + (cmdBuffer[3] * 0x10000) + (cmdBuffer[2] * 0x1000000);
        }
        return addr;
    }

    protected void executeCmd() {
        long currentAddressPointer;
        switch(cmdBuffer[1]) {
            case (byte) 0xff:
                cardState = MMC_CARD_IDLE;
                break;
            case 0x40:
                resetCard();
                cardState = MMC_CARD_RESET;
                break;
            case 0x41:
                cardState = MMC_CARD_INIT;
                break;
            case 0x48:
                if (cardType == CARD_TYPE_MMC) {
                    byte[] cmdresp = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 0;
                    readBufferSet(cmdresp, 0x200);
                } else {
                    byte[] cmdresp = new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 1;
                    readBufferSet(cmdresp, 0x200);
                }
                break;
            case 0x49:
                if (0 == cardInserted()) {
                    byte[] csdresp = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 0;
                    readBufferSet(csdresp, 0x200);
                } else {
                    cardState = MMC_CARD_DUMMY_READ;
                    readFirstbyte = 0;
                }
                break;
            case 0x4a:
                if (0 == cardInserted()) {
                    byte[] cidresp = new byte[] { 0, 0, 0, 0, 1 + 'v' - 'a', 1 + 'i' - 'a', 1 + 'c' - 'a', 1 + 'e' - 'a', '2', '3', 0, 0, 0, 0, 0 };
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 0;
                    readBufferReadptr = 0;
                    readBufferWriteptr = 0;
                    readBufferSet(cidresp, 0x10);
                } else {
                    cardState = MMC_CARD_DUMMY_READ;
                    readFirstbyte = 0;
                }
                break;
            case 0x4c:
                cardState = MMC_CARD_IDLE;
                break;
            case 0x50:
                cardState = MMC_CARD_IDLE;
                blockSize = cmdBuffer[5] + (cmdBuffer[4] * 0x100) + (cmdBuffer[3] * 0x10000) + (cmdBuffer[2] * 0x1000000);
                break;
            case 0x51:
                if (0 == cardInserted()) {
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 0;
                    currentAddressPointer = getAddr();
                    try {
                        imageFile.seek(currentAddressPointer);
                        byte[] readbuf = new byte[0x1000];
                        imageFile.seek(currentAddressPointer);
                        if (currentAddressPointer < imageFile.length()) {
                            if (imageFile.read(readbuf, 1, blockSize) > 0) {
                                readBufferReadptr = 0;
                                readBufferWriteptr = 0;
                                readBufferSet(readbuf, blockSize);
                            } else {
                            }
                        }
                    } catch (IOException e) {
                        cardState = MMC_CARD_DUMMY_READ;
                    }
                } else {
                    cardState = MMC_CARD_DUMMY_READ;
                    readFirstbyte = 0;
                }
                break;
            case 0x58:
                if (0 == cardInserted() && blockSize > 0) {
                    currentAddressPointer = getAddr();
                    writeSequence = 0;
                    cardState = MMC_CARD_WRITE;
                } else {
                    writeSequence = 0;
                    cardState = MMC_CARD_DUMMY_WRITE;
                }
                break;
            case 0x69:
                {
                    byte[] cmdresp = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 0;
                    readBufferSet(cmdresp, 0x200);
                }
                break;
            case 0x77:
                if (cardType != CARD_TYPE_MMC) {
                    byte[] cmdresp = new byte[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 0;
                    readBufferSet(cmdresp, 0x200);
                }
                break;
            case 0x7a:
                if (cardType == CARD_TYPE_SDHC) {
                    byte[] cmdresp = new byte[] { 0, (byte) 0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 0;
                    readBufferSet(cmdresp, 0x200);
                } else {
                    byte[] cmdresp = new byte[] { 0, (byte) 0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    cardState = MMC_CARD_READ;
                    readFirstbyte = 0;
                    readBufferSet(cmdresp, 0x200);
                }
                break;
        }
    }

    protected void writeToCmdBuffer(byte mmcreplayCmdChar) {
        if (cmdBufferPointer == 0) {
            if (mmcreplayCmdChar < (byte) 0xff) {
                if (mmcreplayCmdChar == 0x51) {
                    cmdBuffer[0] = (byte) 0xff;
                    cmdBufferPointer++;
                } else {
                    return;
                }
            }
        }
        if (cmdBufferPointer == 1) {
            if (mmcreplayCmdChar == (byte) 0xff) {
                cmdBufferPointer = 0;
                return;
            }
        }
        cmdBuffer[cmdBufferPointer] = mmcreplayCmdChar;
        cmdBufferPointer++;
        if ((cmdBufferPointer > 8) || (cmdBufferPointer > 7 && cmdBuffer[1] == 0x40) || (cmdBufferPointer > 8 && cmdBuffer[1] == 0x48) || (cmdBufferPointer > 8 && cmdBuffer[1] == 0x49) || (cmdBufferPointer > 8 && cmdBuffer[1] == 0x4a) || (cmdBufferPointer > 8 && cmdBuffer[1] == 0x50) || (cmdBufferPointer > 8 && cmdBuffer[1] == 0x51)) {
            executeCmd();
            clearCmdBuffer();
        }
    }

    protected void writeToMMC(byte value) {
        switch(writeSequence) {
            case 0:
                if (value == (byte) 0xfe) {
                    writeSequence++;
                    imagePointer = 0;
                }
                break;
            case 1:
                if (cardState == MMC_CARD_WRITE) {
                    try {
                        imageFile.write(value);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("could not write to mmc image file");
                    }
                }
                imagePointer++;
                if (imagePointer == blockSize) {
                    writeSequence++;
                }
                break;
            case 2:
                writeSequence++;
                break;
            case 3:
                cardState = MMC_CARD_RETURN_WRITE;
                break;
        }
    }

    public void dataWrite(byte value) {
        if (cardState == MMC_CARD_WRITE || cardState == MMC_CARD_DUMMY_WRITE) {
            writeToMMC(value);
        } else {
            writeToCmdBuffer(value);
        }
    }

    public int mmc_open_card_image(String name, boolean rw) throws IOException {
        String imageFilename = name;
        setCardInserted(MMC_CARD_NOTINSERTED);
        if (imageFilename == null) {
            System.out.println("sd card image name not set");
            return 1;
        }
        if (imageFile != null) {
            closeCardImage();
        }
        if (rw) {
            imageFile = new RandomAccessFile(imageFilename, "rw");
        }
        if (imageFile == null) {
            imageFile = new RandomAccessFile(imageFilename, "r");
            if (imageFile == null) {
                System.out.printf("could not open sd card image: %s\n", imageFilename);
                return 1;
            } else {
                setCardInserted(MMC_CARD_INSERTED);
                System.out.printf("opened sd card image (ro): %s\n", imageFilename);
            }
        } else {
            setCardInserted(MMC_CARD_INSERTED);
            System.out.printf("opened sd card image (rw): %s\n", imageFilename);
        }
        cardRw = rw;
        return 0;
    }

    public void closeCardImage() {
        if (imageFile != null) {
            try {
                imageFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageFile = null;
            setCardInserted(MMC_CARD_NOTINSERTED);
        }
    }
}
