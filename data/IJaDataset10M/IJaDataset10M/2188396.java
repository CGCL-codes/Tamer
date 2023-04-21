package com.lepidllama.packageeditor.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import com.lepidllama.packageeditor.core.LogFacade;
import com.lepidllama.packageeditor.core.exception.ParsingRuntimeException;
import com.lepidllama.packageeditor.dbpf.Header;
import com.lepidllama.packageeditor.dbpf.IndexBlock;
import com.lepidllama.packageeditor.dbpf2.Tgi64;
import com.lepidllama.packageeditor.fileio.DataReader;
import com.lepidllama.packageeditor.fileio.DataWriter;
import com.lepidllama.packageeditor.resources.interfaces.Scenegraph;

public class Vpxy extends Scenegraph {

    private int version;

    private List<Entry> entries;

    private float[] boundingBox;

    private byte[] unk1;

    private TgiBlockList blockList;

    private int ftptIndex;

    private byte modularFlag;

    public Vpxy() {
    }

    @Override
    public void read(DataReader in, Header header, IndexBlock indexBlock) {
        super.read(in, header, indexBlock);
        String segmentName = in.readDwordString();
        if (!segmentName.equals("VPXY")) {
            LogFacade.getLogger(this).log(Level.WARNING, "This file is not of type VPXY. Expecting 'VPXY', found " + segmentName);
            throw new ParsingRuntimeException("This file is not of type VPXY. Expecting 'VPXY', found " + segmentName);
        }
        version = in.readDwordInt();
        in.readDwordLong();
        in.readDwordLong();
        int entryCount = in.readByte() & 0xFF;
        entries = new ArrayList<Entry>();
        for (int i = 0; i < entryCount; i++) {
            Entry e = new Entry();
            e.read(in);
            entries.add(e);
        }
        byte bbtypecode = in.readByte();
        if (bbtypecode != 0x02) {
            LogFacade.getLogger(this).log(Level.WARNING, "Received an unexpected type code in VPXY. Expecting '2', found " + bbtypecode);
            throw new ParsingRuntimeException("This file is not of type VPXY. Expecting 'VPXY', found " + bbtypecode);
        }
        boundingBox = new float[6];
        boundingBox[0] = in.readFloat();
        boundingBox[1] = in.readFloat();
        boundingBox[2] = in.readFloat();
        boundingBox[3] = in.readFloat();
        boundingBox[4] = in.readFloat();
        boundingBox[5] = in.readFloat();
        unk1 = in.readChunk(4);
        modularFlag = in.readByte();
        if (modularFlag == 0x01) {
            ftptIndex = in.readDwordInt();
        }
        if (entryCount > 0) {
            blockList = new TgiBlockList();
            blockList.read(in, header, indexBlock);
        }
    }

    @Override
    public void write(DataWriter out, Header header, IndexBlock indexBlock) {
        super.write(out, header, indexBlock);
        out.writeDwordString("VPXY");
        out.writeDwordInt(version);
        long entryOffset = out.getFilePointer();
        out.writeDwordInt(0);
        out.writeDwordInt(0);
        out.writeByte((byte) entries.size());
        for (Entry e : entries) {
            e.write(out);
        }
        out.writeByte((byte) 0x02);
        out.writeDwordFloat(boundingBox[0]);
        out.writeDwordFloat(boundingBox[1]);
        out.writeDwordFloat(boundingBox[2]);
        out.writeDwordFloat(boundingBox[3]);
        out.writeDwordFloat(boundingBox[4]);
        out.writeDwordFloat(boundingBox[5]);
        out.writeChunk(unk1);
        out.writeByte(modularFlag);
        if (modularFlag == 1) {
            out.writeDwordInt(ftptIndex);
        }
        long tgiStart = out.getFilePointer();
        if (blockList.getTgis() != null && blockList.getTgis().size() > 0) {
            blockList.write(out, header, indexBlock);
        }
        long endOfFile = out.getFilePointer();
        out.seek(entryOffset);
        out.writeDwordLong(tgiStart - entryOffset);
        out.writeDwordLong(endOfFile - tgiStart);
        out.seek(endOfFile);
    }

    private class Entry {

        public byte type;

        public byte id;

        public long[] indexDwords;

        public void read(DataReader in) {
            type = in.readByte();
            if (type == 0x00) {
                id = in.readByte();
                int count = in.readByte();
                indexDwords = new long[count];
                for (int i = 0; i < count; i++) {
                    indexDwords[i] = in.readDwordLong();
                }
            } else if (type == 0x01) {
                indexDwords = new long[1];
                indexDwords[0] = in.readDwordLong();
            }
        }

        public void write(DataWriter out) {
            out.writeByte(type);
            if (type == 0x00) {
                out.writeByte(id);
                out.writeByte((byte) indexDwords.length);
                ;
                for (int i = 0; i < indexDwords.length; i++) {
                    out.writeDwordLong(indexDwords[i]);
                }
            } else if (type == 0x01) {
                out.writeDwordLong(indexDwords[0]);
            }
        }
    }

    public ScenegraphHeader getScenegraphHeader() {
        return scenegraphHeader;
    }

    public void setScenegraphHeader(ScenegraphHeader scenegraphHeader) {
        this.scenegraphHeader = scenegraphHeader;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public float[] getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(float[] boundingBox) {
        this.boundingBox = boundingBox;
    }

    public List<Tgi64> getReferences() {
        if (this.blockList != null) {
            return this.blockList.getTgis();
        }
        return null;
    }

    public void setReferences(List<Tgi64> references) {
        this.blockList.setTgis(references);
    }

    public byte[] getUnk1() {
        return unk1;
    }

    public void setUnk1(byte[] unk1) {
        this.unk1 = unk1;
    }

    public TgiBlockList getBlockList() {
        return blockList;
    }

    public void setBlockList(TgiBlockList blockList) {
        this.blockList = blockList;
    }

    public int getFtptIndex() {
        return ftptIndex;
    }

    public void setFtptIndex(int ftptIndex) {
        this.ftptIndex = ftptIndex;
    }
}
