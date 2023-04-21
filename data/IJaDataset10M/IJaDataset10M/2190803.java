package com.ensoniq.ensonitron;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * <b>Class EnsoniqDisk</b>
 * <p>
 * This class, when finished, will handle the following things:
 * <ul>
 * <li> provide an abstract disk object which can handle disk image files
 * (different formats, transparent) and (in a later evolution step) disk drives
 * (CDROM, hard disk, removable media etc.)
 * <li> functions to initialize and destroy disk objects
 * <li> read access (on a block basis at 512 bytes per block)
 * <li> write access (if the device or file allows that, also on a block basis)
 * </ul>
 * <p>
 * All higher level of data processing (handling the different blocks,
 * interpreting their content) will be done in the calling class.
 */
public class EnsoniqDisk {

    public static int ERR_OK = 0, ERR_READ = -1, ERR_WRITE = -2, ERR_OPEN = -3, ERR_MEMORY = -4, ERR_NOT_OPEN = -5, ERR_NOT_SUPPORTED = -6, ERR_OUT_OF_RANGE = -7, ERR_FAT = -8, ERR_NO_SIGNATURE = -9;

    public static int TYPE_FILE = 0, TYPE_DEVICE = 1;

    public static int TYPE_FILE_PLAIN = 0, TYPE_FILE_GKH = 1, TYPE_FILE_EDE = 2, TYPE_FILE_CDMODE1 = 3, TYPE_FILE_UNKNOWN = 99;

    private int m_iType = TYPE_FILE;

    private int m_iImageOffset = 0;

    private int m_iImageType = TYPE_FILE_UNKNOWN;

    private int m_iPhysicalBlocks = -1;

    private boolean m_bIsOpen = false;

    private RandomAccessFile m_File = null;

    /**
 * <b>Constructor</b>
 * <p>
 * <code>public EnsoniqDisk()</code>
 * <p>
 * This is the constructor of the class. Does not do anything yet.
 */
    public EnsoniqDisk() {
    }

    /**
 * <b>open</b>
 * <p>
 * <code>public int open(String strName, int iType)</code>
 * <p>
 * This function opens a file/device. If a file/device was already opened, it
 * will first be closed and then the new file/device will be opened.
 * 
 * @param strName
 *            a string holding the disk name (can be a local filename or a
 *            device name)
 * @param iType
 *            <i>TYPE_FILE</i> to open a file, <i>TYPE_DEVICE</i> to open a
 *            disk device
 * @return <i>ERR_OK</i><br>
 *         <i>ERR_OPEN</i><br>
 *         <i>ERR_NOT_SUPPORTED</i><br>
 */
    public int open(String strName, int iType) {
        byte[] ucBuffer = new byte[512];
        int iResult, i;
        if (this.m_bIsOpen) close();
        System.out.printf("EnsoniqDisk.Open(\"%s\", %d\n", strName, iType);
        if (TYPE_FILE == iType) {
            File f = new File(strName);
            if (false == f.exists()) {
                System.out.println("File not found.");
                return ERR_OPEN;
            }
            try {
                System.out.println("Creating new file access object");
                this.m_File = new RandomAccessFile(strName, "rw");
            } catch (FileNotFoundException e) {
                System.err.printf("Error: %s\n", e.getMessage());
                return ERR_OPEN;
            }
            this.m_bIsOpen = true;
            this.m_iType = iType;
            try {
                System.out.println("Reading file length");
                this.m_iPhysicalBlocks = (int) (this.m_File.length() / 512);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                close();
                return ERR_OPEN;
            }
            System.out.printf("File size is %d blocks (%d kB)\n", this.m_iPhysicalBlocks, this.m_iPhysicalBlocks / 2);
            System.out.println("Detecting image file format...");
            this.m_iImageType = TYPE_FILE_PLAIN;
            this.m_iImageOffset = 0;
            if (ERR_OK == checkEnsoniqSignature()) {
                System.out.println("Detected image file format: ISO/plain image");
                return ERR_OK;
            }
            iResult = read(0, 1, ucBuffer);
            if (ERR_OK != iResult) {
                System.out.printf("Error reading from file (error code = %d).\n", iResult);
                return ERR_OPEN;
            }
            this.m_iImageType = TYPE_FILE_EDE;
            this.m_iImageOffset = 512;
            if (ERR_OK == checkEnsoniqSignature()) {
                if ((0x0D == ucBuffer[0x00]) && (0x0A == ucBuffer[0x01]) && (0x0D == ucBuffer[0x4E]) && (0x0A == ucBuffer[0x4F])) {
                    System.out.println("Detected image file format: Giebler image");
                    return ERR_OK;
                }
            }
            this.m_iImageType = TYPE_FILE_GKH;
            for (i = 0; i < 5; i++) {
                if ((0x0B == ucBuffer[0x00 + i * 10]) && (0x0B == ucBuffer[0x01 + i * 10]) && (0x00 == ucBuffer[0x02 + i * 10]) && (0x80 == ucBuffer[0x03 + i * 10]) && (0x0F == ucBuffer[0x04 + i * 10]) && (0x00 == ucBuffer[0x05 + i * 10])) {
                    this.m_iImageOffset = ucBuffer[0x06 + i * 10] + (ucBuffer[0x06 + i * 10] << 8) + (ucBuffer[0x06 + i * 10] << 16) + (ucBuffer[0x06 + i * 10] << 24);
                    if (ERR_OK == checkEnsoniqSignature()) {
                        System.out.println("Detected image file format: Giebler image");
                        return ERR_OK;
                    }
                }
            }
            this.m_iImageType = TYPE_FILE_CDMODE1;
            if ((0x00 == ucBuffer[0]) && (0xFF == ucBuffer[1]) && (0xFF == ucBuffer[2]) && (0xFF == ucBuffer[3]) && (0xFF == ucBuffer[4]) && (0xFF == ucBuffer[5]) && (0xFF == ucBuffer[6]) && (0xFF == ucBuffer[7]) && (0xFF == ucBuffer[8]) && (0xFF == ucBuffer[9]) && (0xFF == ucBuffer[10]) && (0x00 == ucBuffer[11])) {
                if (ERR_OK == checkEnsoniqSignature()) {
                    System.out.println("Detected Mode1CD image file.");
                    return ERR_OK;
                }
            }
            System.out.println("Error: Image file format not supported.");
            return ERR_NOT_SUPPORTED;
        } else if (TYPE_DEVICE == iType) {
            System.err.println("iType=TYPE_DEVICE not supported at the moment.\n");
            return ERR_NOT_SUPPORTED;
        } else {
            System.err.printf("iType=%d not supported at the moment.\n", iType);
            return ERR_NOT_SUPPORTED;
        }
    }

    /**
 * <b>checkEnsoniqSignature</b>
 * <p>
 * <code>public int checkEnsoniqSignature()</code>
 * <p>
 * This function checks if an image file/device is Ensoniq formatted. It uses
 * the already opened file object <i>m_File</i> and pays attention to the
 * currently selected file/device type.
 * 
 * @return <i>ERR_NOT_OPEN</i><br>
 *         <i>ERR_READ</i><br>
 *         <i>ERR_NOT_SUPPORTED</i><br>
 *         <i>ERR_OUT_OF_RANGE</i><br>
 *         <i>ERR_NO_SIGNATURE</i><br>
 *         <i>ERR_OK</i> in case of success (signature found)<br>
 */
    public int checkEnsoniqSignature() {
        byte ucBuf[] = new byte[6 * 512];
        int iResult;
        iResult = read(0, 6, ucBuf);
        if (ERR_OK != iResult) return iResult;
        if (('I' != ucBuf[38 + 512 * 1]) || ('D' != ucBuf[39 + 512 * 1]) || ('O' != ucBuf[28 + 512 * 2]) || ('S' != ucBuf[29 + 512 * 2]) || ('F' != ucBuf[510 + 512 * 5]) || ('B' != ucBuf[511 + 512 * 5]) || ('D' != ucBuf[510 + 512 * 4]) || ('R' != ucBuf[511 + 512 * 4])) return ERR_NO_SIGNATURE;
        return ERR_OK;
    }

    /**
 * <b>close</b>
 * <p>
 * <code>public int close()</code>
 * <p>
 * This function closes a file/device. All buffers will be flushed before
 * closing.
 * 
 * @return <i>ERR_OK</i><br>
 *         <i>ERR_NOT_OPEN</i><br>
 *         {@link}
 */
    public int close() {
        if (!this.m_bIsOpen) return ERR_NOT_OPEN;
        System.out.println("Closing disk.");
        if (TYPE_FILE == this.m_iType) {
            try {
                this.m_File.close();
            } catch (IOException e) {
            }
        } else if (TYPE_DEVICE == this.m_iType) {
            return ERR_NOT_SUPPORTED;
        } else {
            return ERR_NOT_SUPPORTED;
        }
        this.m_bIsOpen = false;
        return ERR_OK;
    }

    /**
 * <b>read</b>
 * <p>
 * <code>public int read(int iStart, int iCount, byte ucBuf[])</code>
 * <p>
 * This function reads blocks from a file/device. One block is 512 bytes.
 * Attention is being paid to the currently selected device/disk format.
 * 
 * @param iStart
 *            First block to read (0...0x00FFFFFF)
 * @param iCount
 *            number of blocks to read
 * @param cBuf
 *            destination buffer, must be <i>iCount</i>*512 bytes large
 * @return <i>ERR_OK</i><br>
 *         <i>ERR_NOT_OPEN</i><br>
 *         <i>ERR_NOT_SUPPORTED</i><br>
 *         <i>ERR_OUT_OF_RANGE</i><br>
 *         <i>ERR_READ</i><br>
 */
    public int read(int iStart, int iCount, byte ucBuf[]) {
        int iBytesRead;
        if (!this.m_bIsOpen) return ERR_NOT_OPEN;
        if ((iStart < 0) || (iStart > 0x00FFFFFF)) return ERR_OUT_OF_RANGE;
        if (iStart > this.m_iPhysicalBlocks) return ERR_OUT_OF_RANGE;
        if (TYPE_FILE == this.m_iType) {
            try {
                if (TYPE_FILE_PLAIN == this.m_iImageType) {
                    iBytesRead = this.m_File.read(ucBuf, iStart * 512, iCount * 512);
                    if (iBytesRead != iCount * 512) {
                        System.out.printf("Error: RandomAccessFile.read() returned %d bytes" + "instead of %d bytes requested.\n", iBytesRead, iCount * 512);
                        return ERR_READ;
                    }
                } else if (TYPE_FILE_CDMODE1 == this.m_iImageType) {
                    iBytesRead = this.m_File.read(ucBuf, (iStart >> 2) * 2352 + 16 + (iStart & 3) * 512, iCount * 512);
                    if (iBytesRead != iCount * 512) {
                        System.out.printf("Error: RandomAccessFile.read() returned %d bytes" + "instead of %d bytes requested.\n", iBytesRead, iCount * 512);
                        return ERR_READ;
                    }
                } else if (TYPE_FILE_EDE == this.m_iImageType) {
                    System.out.println("EnsoniqDisk.read(): TYPE_FILE_EDE is not implemented.");
                    return ERR_NOT_SUPPORTED;
                } else if (TYPE_FILE_GKH == this.m_iImageType) {
                    iBytesRead = this.m_File.read(ucBuf, iStart * 512 + this.m_iImageOffset, iCount * 512);
                    if (iBytesRead != iCount * 512) {
                        System.out.printf("Error: RandomAccessFile.read() returned %d bytes" + "instead of %d bytes requested.\n", iBytesRead, iCount * 512);
                        return ERR_READ;
                    }
                } else {
                    return ERR_NOT_SUPPORTED;
                }
            } catch (IOException e) {
                System.out.printf("EnsoniqDisk.read(): %s\n", e.getMessage());
                return ERR_READ;
            }
        } else if (TYPE_DEVICE == this.m_iType) {
            return ERR_NOT_SUPPORTED;
        } else {
            return ERR_NOT_SUPPORTED;
        }
        return ERR_OK;
    }

    /**
 * <b>write</b>
 * <p>
 * <code>public int write(int iStart, int iCount, byte ucBuf[])</code>
 * <p>
 * This function writes blocks to a file/device. One block is 512 bytes.
 * Attention is being paid to the currently selected device/disk format.
 * 
 * @param iStart
 *            first block to write (0...0x00FFFFFF)
 * @param iCount
 *            number of blocks to write
 * @param cBuf
 *            buffer holding data to write, must be <i>iCount</i>*512 bytes
 *            large
 * @return <i>ERR_OK</i><br>
 *         <i>ERR_NOT_OPEN</i><br>
 *         <i>ERR_NOT_SUPPORTED</i><br>
 *         <i>ERR_OUT_OF_RANGE</i><br>
 *         <i>ERR_WRITE</i><br>
 */
    public int write(int iStart, int iCount, byte ucBuf[]) {
        if (!this.m_bIsOpen) return ERR_NOT_OPEN;
        if ((iStart < 0) || (iStart > 0x00FFFFFF)) return ERR_OUT_OF_RANGE;
        if (iStart > this.m_iPhysicalBlocks) return ERR_OUT_OF_RANGE;
        if (TYPE_FILE == this.m_iType) {
            try {
                if (TYPE_FILE_PLAIN == this.m_iImageType) {
                    this.m_File.write(ucBuf, iStart * 512, iCount * 512);
                } else if (TYPE_FILE_CDMODE1 == this.m_iImageType) {
                    System.out.println("EnsoniqDisk.write(): TYPE_FILE_CDMODE1 is not implemented.");
                    return ERR_NOT_SUPPORTED;
                } else if (TYPE_FILE_EDE == this.m_iImageType) {
                    System.out.println("EnsoniqDisk.write(): TYPE_FILE_EDE is not implemented.");
                    return ERR_NOT_SUPPORTED;
                } else if (TYPE_FILE_GKH == this.m_iImageType) {
                    this.m_File.write(ucBuf, iStart * 512 + this.m_iImageOffset, iCount * 512);
                } else {
                    return ERR_NOT_SUPPORTED;
                }
            } catch (IOException e) {
                return ERR_WRITE;
            }
        } else if (TYPE_DEVICE == this.m_iType) {
            return ERR_NOT_SUPPORTED;
        } else {
            return ERR_NOT_SUPPORTED;
        }
        return ERR_OK;
    }

    /**
 * <b>getFatEntry</b>
 * <p>
 * <code>public int getFatEntry(int iBlock)</code>
 * <p>
 * This function retrieves the FAT entry associated with a given block.
 * 
 * @param iBlock
 *            block number (0...0x00FFFFFF)
 * @return FAT entry for block or one of the following error codes:<br>
 *         <i>ERR_NOT_OPEN</i><br>
 *         <i>ERR_NOT_SUPPORTED</i><br>
 *         <i>ERR_READ</i><br>
 *         <i>ERR_OUT_OF_RANGE</i><br>
 *         <i>ERR_FAT</i><br>
 */
    public int getFatEntry(int iBlock) {
        byte ucFATBuffer[] = new byte[512];
        int iResult;
        if (!this.m_bIsOpen) return ERR_NOT_OPEN;
        if ((iBlock < 0) || (iBlock > 0x00FFFFFF)) return ERR_OUT_OF_RANGE;
        if (iBlock >= this.m_iPhysicalBlocks) return ERR_OUT_OF_RANGE;
        iResult = read((iBlock / 170) + 5, 1, ucFATBuffer);
        if (ERR_OK != iResult) return iResult;
        if ((ucFATBuffer[510] != 'F') || (ucFATBuffer[511] != 'B')) {
            return ERR_FAT;
        }
        iResult = (ucFATBuffer[(iBlock % 170) * 3 + 0] << 16) + (ucFATBuffer[(iBlock % 170) * 3 + 1] << 8) + (ucFATBuffer[(iBlock % 170) * 3 + 2]);
        return iResult;
    }

    /**
 * <b>setFatEntry</b>
 * <p>
 * <code>public int setFatEntry(int iBlock, int iNewValue)</code>
 * <p>
 * This function sets the FAT entry associated with a given block to a new
 * value. The previous value of the FAT entry is returned.
 * 
 * @param iBlock
 *            block number (0...0x00FFFFFF)
 * @param iNewValue
 *            new content of given FAT entry (0...0x00FFFFFF)
 * @return previous value of the FAT entry or one of the following error codes:<br>
 *         <i>ERR_NOT_OPEN</i><br>
 *         <i>ERR_NOT_SUPPORTED</i><br>
 *         <i>ERR_READ</i><br>
 *         <i>ERR_WRITE</i><br>
 *         <i>ERR_OUT_OF_RANGE</i><br>
 *         <i>ERR_FAT_ERROR</i><br>
 */
    public int setFatEntry(int iBlock, int iNewValue) {
        byte ucFATBuffer[] = new byte[512];
        int iResult, iFATEntry;
        if (!this.m_bIsOpen) return ERR_NOT_OPEN;
        if ((iBlock < 0) || (iBlock > 0x00FFFFFF)) return ERR_OUT_OF_RANGE;
        if (iBlock >= this.m_iPhysicalBlocks) return ERR_OUT_OF_RANGE;
        iResult = read((iBlock / 170) + 5, 1, ucFATBuffer);
        if (ERR_OK != iResult) return iResult;
        if ((ucFATBuffer[510] != 'F') || (ucFATBuffer[511] != 'B')) {
            return ERR_FAT;
        }
        iFATEntry = (ucFATBuffer[(iBlock % 170) * 3 + 0] << 16) + (ucFATBuffer[(iBlock % 170) * 3 + 1] << 8) + (ucFATBuffer[(iBlock % 170) * 3 + 2]);
        ucFATBuffer[(iBlock % 170) * 3 + 0] = (byte) ((iNewValue >> 16) & 0xFF);
        ucFATBuffer[(iBlock % 170) * 3 + 1] = (byte) ((iNewValue >> 8) & 0xFF);
        ucFATBuffer[(iBlock % 170) * 3 + 2] = (byte) ((iNewValue) & 0xFF);
        iResult = write((iBlock / 170) + 5, 1, ucFATBuffer);
        if (ERR_OK != iResult) return iResult;
        return iFATEntry;
    }
}
