package com.orhandemirel.jabif;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 *
 * @author Selcuk Orhan DEMIREL <orhandemirel@msn.com>
 * 
 */
public class Abif {

    private static final byte[] signature = { 'A', 'B', 'I', 'F' };

    private static final int major_version = 1;

    private DirEntry dirEntry;

    private AbifTag[] tags;

    private int numberOfTags;

    RandomAccessFile file = null;

    /**
    *
    * @param filename Name of the file
    * @throws FileNotFoundException
    */
    public Abif(String filename) throws FileNotFoundException, IOException {
        file = new RandomAccessFile(filename, "r");
        readData(file);
    }

    /**
    *
    * @param filename Name of the file
    * @return Abif object
    * @throws FileNotFoundException
    */
    public static Abif open(String filename) throws FileNotFoundException, IOException {
        return new Abif(filename);
    }

    /**
    *
    * @param file
    * @throws IOException
    */
    private void readData(RandomAccessFile file) throws IOException {
        if (file != null) {
            byte[] sig = new byte[4];
            file.read(sig);
            if (!Arrays.equals(signature, sig)) {
                throw new UnsupportedFileException();
            }
            int ver = file.readUnsignedShort();
            if (major_version != (ver / 100)) {
                throw new UnsupportedVersionException();
            }
            dirEntry = new DirEntry();
            readDirEntry(file, dirEntry);
            numberOfTags = dirEntry.getNumelements();
            int offset = dirEntry.getDataoffset();
            tags = new AbifTag[numberOfTags];
            file.seek(offset);
            for (int i = 0; i < numberOfTags; i++) {
                DirEntry entry = new DirEntry();
                readDirEntry(file, entry);
                if (entry.getType().name().equals("UNSUPPORTED")) {
                    tags[i] = new AbifTag(entry, null, AbifTag.TagType.UNSUPPORTED);
                } else {
                    tags[i] = createAbifTag(file, entry);
                }
                offset += 28;
                file.seek(offset);
            }
        } else {
            throw new NullPointerException("Null file pointer!");
        }
    }

    private void readDirEntry(RandomAccessFile file, DirEntry entry) throws IOException {
        char[] name = new char[4];
        int tmp = file.readInt();
        name[0] = (char) (tmp >>> 24);
        name[1] = (char) ((tmp >>> 16) & 0xff);
        name[2] = (char) ((tmp >>> 8) & 0xff);
        name[3] = (char) (tmp & 0xff);
        entry.setName(String.valueOf(name));
        entry.setNumber(file.readInt());
        entry.setElementtype(file.readShort());
        entry.setElementsize(file.readShort());
        entry.setNumelements(file.readInt());
        entry.setDatasize(file.readInt());
        entry.setDataoffset(file.readInt());
        entry.setDatahandle(file.readInt());
    }

    /**
     * @return the numberOfTags
     */
    public int getNumberOfTags() {
        return numberOfTags;
    }

    /**
     * @param numberOfTags the numberOfTags to set
     */
    public void setNumberOfTags(int numberOfTags) {
        this.numberOfTags = numberOfTags;
    }

    private AbifTag createAbifTag(RandomAccessFile file, DirEntry entry) throws IOException {
        int dataSize = entry.getDatasize();
        int numElem = entry.getNumelements();
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bas);
        ByteArrayInputStream bis;
        DataInputStream dis;
        if (dataSize <= 4) {
            int d = entry.getDataoffset();
            dos.writeInt(d);
        } else {
            file.seek(entry.getDataoffset());
            for (int i = 0; i < dataSize; i++) {
                dos.writeByte(file.readByte());
            }
        }
        bis = new ByteArrayInputStream(bas.toByteArray());
        dis = new DataInputStream(bis);
        Object[] data = new Object[numElem];
        AbifTag.TagType type = null;
        switch(entry.getType()) {
            case BYTE:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (int) (dis.readUnsignedByte());
                }
                type = AbifTag.TagType.INT;
                break;
            case WORD:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (int) (dis.readUnsignedShort());
                }
                type = AbifTag.TagType.INT;
                break;
            case SHORT:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (int) (dis.readShort());
                }
                type = AbifTag.TagType.INT;
                break;
            case LONG:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (int) (dis.readInt());
                }
                type = AbifTag.TagType.INT;
                break;
            case CHAR:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (char) (dis.readUnsignedByte());
                }
                type = AbifTag.TagType.CHAR;
                break;
            case FLOAT:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (float) (dis.readFloat());
                }
                type = AbifTag.TagType.FLOAT;
                break;
            case DOUBLE:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (double) (dis.readDouble());
                }
                type = AbifTag.TagType.DOUBLE;
                break;
            case DATE:
                for (int i = 0; i < numElem; i++) {
                    data[i] = new AbifTag.Date(dis.readShort(), dis.readUnsignedByte(), dis.readUnsignedByte());
                }
                type = AbifTag.TagType.DATE;
                break;
            case TIME:
                for (int i = 0; i < numElem; i++) {
                    data[i] = new AbifTag.Time(dis.readUnsignedByte(), dis.readUnsignedByte(), dis.readUnsignedByte(), dis.readUnsignedByte());
                }
                type = AbifTag.TagType.TIME;
                break;
            case PSTRING:
                char[] arr = new char[entry.getNumelements()];
                dis.readUnsignedByte();
                for (int i = 0; i < numElem - 1; i++) {
                    arr[i] = (char) (dis.readUnsignedByte());
                }
                arr[numElem - 1] = '\0';
                data[0] = String.valueOf(arr);
                type = AbifTag.TagType.STRING;
                break;
            case CSTRING:
                char[] arr2 = new char[numElem];
                for (int i = 0; i < numElem - 1; i++) {
                    arr2[i] = (char) (dis.readUnsignedByte());
                }
                data[0] = String.valueOf(arr2);
                type = AbifTag.TagType.STRING;
                break;
        }
        return new AbifTag(entry, data, type);
    }

    /**
     * @return all the tags
     */
    public AbifTag[] getTags() {
        return tags;
    }

    /**
     *
     * @param id The ID searched for
     * @return the matched tag
     */
    public AbifTag getTagById(String id) {
        for (AbifTag tag : tags) {
            if (tag.getId().equals(id)) {
                return tag;
            }
        }
        return null;
    }

    /**
     *
     * @param channel Channel Number between 1-4 (5 is not always exists)
     * @return  raw data of the selected channel
     */
    public int[] getRawData(int channel) {
        Object[] data = null;
        int[] intData = null;
        boolean found = false;
        switch(channel) {
            case 1:
            case 2:
            case 3:
            case 4:
                data = getTagById("DATA" + channel).getData();
                found = true;
                break;
            case 5:
                data = getTagById("DATA105").getData();
                found = true;
                break;
        }
        if (found) {
            intData = new int[data.length];
            int j = 0;
            for (Object integer : data) {
                intData[j] = (int) ((Integer) integer);
                j++;
            }
            return intData;
        } else {
            return null;
        }
    }

    /**
     *
     * @param channel Channel Number between 1-4 (5 is not always exists)
     * @return  analyzed data of the selected channel
     */
    public int[] getAnalyzedData(int channel) {
        Object[] data = null;
        int[] intData = null;
        boolean found = false;
        switch(channel) {
            case 1:
                data = getTagById("DATA9").getData();
                found = true;
                break;
            case 2:
                data = getTagById("DATA10").getData();
                found = true;
                break;
            case 3:
                data = getTagById("DATA11").getData();
                found = true;
                break;
            case 4:
                data = getTagById("DATA12").getData();
                found = true;
                break;
            case 5:
                data = getTagById("DATA205").getData();
                found = true;
                break;
        }
        if (found) {
            intData = new int[data.length];
            int j = 0;
            for (Object integer : data) {
                intData[j] = (int) ((Integer) integer);
                j++;
            }
            return intData;
        } else {
            return null;
        }
    }

    /**
     *
     * @return array of base chars shorted by channel numbers
     */
    public char[] getBaseOrder() {
        Object[] data = null;
        char[] charData = null;
        data = getTagById("FWO_1").getData();
        charData = new char[data.length];
        int j = 0;
        for (Object character : data) {
            charData[j] = (char) ((Character) character);
            j++;
        }
        return charData;
    }
}
