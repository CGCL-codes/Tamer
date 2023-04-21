package malictus.klang.chunk;

import java.io.IOException;
import java.io.RandomAccessFile;
import malictus.klang.file.*;
import malictus.klang.*;
import malictus.klang.primitives.*;

/**
 * A class that represents the FMT chunk in a WAV file.
 * @author Jim Halliday
 */
public class FMTChunk extends Chunk {

    public FMTChunk(KlangFile parentFile, long startpos, long endpos, long datastartpos, long dataendpos, int chunkType, String chunkName, ContainerChunk parentChunk, Boolean isBigEndian, RandomAccessFile raf) throws IOException {
        super(parentFile, startpos, endpos, datastartpos, dataendpos, chunkType, chunkName, parentChunk, isBigEndian, raf);
        raf.seek(datastartpos);
        Primitive compcode;
        Primitive numchannels;
        Primitive samprate;
        Primitive bytespersecond;
        Primitive blockalign;
        Primitive sigbits;
        Primitive bpsspb;
        Primitive channelmask;
        Primitive subformat;
        if (isBigEndian) {
            compcode = new Int2ByteUnsignedBE();
            numchannels = new Int2ByteUnsignedBE();
            samprate = new Int4ByteUnsignedBE();
            bytespersecond = new Int4ByteUnsignedBE();
            blockalign = new Int2ByteUnsignedBE();
            sigbits = new Int2ByteUnsignedBE();
            bpsspb = new Int2ByteUnsignedBE();
            channelmask = new Int4ByteUnsignedBE();
            subformat = new Int2ByteUnsignedBE();
        } else {
            compcode = new Int2ByteUnsignedLE();
            numchannels = new Int2ByteUnsignedLE();
            samprate = new Int4ByteUnsignedLE();
            bytespersecond = new Int4ByteUnsignedLE();
            blockalign = new Int2ByteUnsignedLE();
            sigbits = new Int2ByteUnsignedLE();
            bpsspb = new Int2ByteUnsignedLE();
            channelmask = new Int4ByteUnsignedLE();
            subformat = new Int2ByteUnsignedLE();
        }
        PrimitiveData compcodeData;
        PrimitiveData numchannelsData;
        PrimitiveData samprateData;
        PrimitiveData bytespersecondData;
        PrimitiveData blockalignData;
        PrimitiveData sigbitsData;
        PrimitiveData bpsspbData;
        PrimitiveData channelmaskData;
        PrimitiveData subformatData;
        boolean keepgoing = true;
        if (endpos >= (raf.getFilePointer() + 2)) {
            try {
                long bytepos = raf.getFilePointer();
                compcode.setValueFromFile(raf);
                compcodeData = new PrimitiveData(compcode, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_COMP_CODE, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                compcodeData = new PrimitiveData(compcode, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_COMP_CODE, null, null);
            }
        } else {
            compcodeData = new PrimitiveData(compcode, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_COMP_CODE, null, null);
        }
        if (keepgoing && (endpos >= (raf.getFilePointer() + 2))) {
            try {
                long bytepos = raf.getFilePointer();
                numchannels.setValueFromFile(raf);
                numchannelsData = new PrimitiveData(numchannels, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_NUM_CHANNELS, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                numchannelsData = new PrimitiveData(numchannels, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_NUM_CHANNELS, null, null);
            }
        } else {
            numchannelsData = new PrimitiveData(numchannels, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_NUM_CHANNELS, null, null);
        }
        if (keepgoing && (endpos >= (raf.getFilePointer() + 4))) {
            try {
                long bytepos = raf.getFilePointer();
                samprate.setValueFromFile(raf);
                samprateData = new PrimitiveData(samprate, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SAMP_RATE, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                samprateData = new PrimitiveData(samprate, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SAMP_RATE, null, null);
            }
        } else {
            samprateData = new PrimitiveData(samprate, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SAMP_RATE, null, null);
        }
        if (keepgoing && (endpos >= (raf.getFilePointer() + 4))) {
            try {
                long bytepos = raf.getFilePointer();
                bytespersecond.setValueFromFile(raf);
                bytespersecondData = new PrimitiveData(bytespersecond, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BPS, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                bytespersecondData = new PrimitiveData(bytespersecond, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BPS, null, null);
            }
        } else {
            bytespersecondData = new PrimitiveData(bytespersecond, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BPS, null, null);
        }
        if (keepgoing && (endpos >= (raf.getFilePointer() + 2))) {
            try {
                long bytepos = raf.getFilePointer();
                blockalign.setValueFromFile(raf);
                blockalignData = new PrimitiveData(blockalign, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BLOCK_ALIGN, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                blockalignData = new PrimitiveData(blockalign, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BLOCK_ALIGN, null, null);
            }
        } else {
            blockalignData = new PrimitiveData(blockalign, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BLOCK_ALIGN, null, null);
        }
        if (keepgoing && (endpos >= (raf.getFilePointer() + 2))) {
            try {
                long bytepos = raf.getFilePointer();
                sigbits.setValueFromFile(raf);
                sigbitsData = new PrimitiveData(sigbits, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SIGBITS, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                sigbitsData = new PrimitiveData(sigbits, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SIGBITS, null, null);
            }
        } else {
            sigbitsData = new PrimitiveData(sigbits, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SIGBITS, null, null);
        }
        raf.skipBytes(2);
        if (keepgoing && (endpos >= (raf.getFilePointer() + 2))) {
            try {
                long bytepos = raf.getFilePointer();
                bpsspb.setValueFromFile(raf);
                bpsspbData = new PrimitiveData(bpsspb, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BPSSPB, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                bpsspbData = new PrimitiveData(bpsspb, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BPSSPB, null, null);
            }
        } else {
            bpsspbData = new PrimitiveData(bpsspb, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_BPSSPB, null, null);
        }
        if (keepgoing && (endpos >= (raf.getFilePointer() + 4))) {
            try {
                long bytepos = raf.getFilePointer();
                channelmask.setValueFromFile(raf);
                channelmaskData = new PrimitiveData(channelmask, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_CHANNEL_MASK, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                channelmaskData = new PrimitiveData(channelmask, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_CHANNEL_MASK, null, null);
            }
        } else {
            channelmaskData = new PrimitiveData(channelmask, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_CHANNEL_MASK, null, null);
        }
        if (keepgoing && (endpos >= (raf.getFilePointer() + 2))) {
            try {
                long bytepos = raf.getFilePointer();
                subformat.setValueFromFile(raf);
                subformatData = new PrimitiveData(subformat, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SUBFORMAT, bytepos, raf.getFilePointer());
            } catch (BadValueException er) {
                keepgoing = false;
                subformatData = new PrimitiveData(subformat, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SUBFORMAT, null, null);
            }
        } else {
            subformatData = new PrimitiveData(subformat, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_SUBFORMAT, null, null);
        }
        this.primitives.add(compcodeData);
        this.primitives.add(numchannelsData);
        this.primitives.add(samprateData);
        this.primitives.add(bytespersecondData);
        this.primitives.add(blockalignData);
        this.primitives.add(sigbitsData);
        this.primitives.add(bpsspbData);
        this.primitives.add(channelmaskData);
        this.primitives.add(subformatData);
    }

    /**
	 * Overwrite of this method in Chunk class to account for skipped bytes in WAVEFORMATEXTENSIBLE struct.
	 * @param raff a pointer to the file (position is not important)
	 * @throws IOException if any read/write error occurs
	 */
    public void reparseChunkPrimitives(RandomAccessFile raff) throws IOException {
        if ((this.primitives == null) || (this.primitives.size() == 0)) {
            return;
        }
        boolean isBasic = true;
        int counter = 6;
        while (counter < this.getPrimitiveData().size()) {
            if (this.getPrimitiveData().get(counter).getPrimitive().valueExists()) {
                isBasic = false;
            }
            counter = counter + 1;
        }
        int total = 0;
        counter = 0;
        int end = primitives.size();
        if (isBasic) {
            end = 6;
        }
        while (counter < end) {
            Primitive p = primitives.get(counter).getPrimitive();
            if (!(p instanceof PrimitiveFixedByte)) {
                throw new IOException(KlangConstants.ERROR_NOT_FIXED_BYTE);
            }
            total = total + ((PrimitiveFixedByte) p).getFixedLength();
            counter = counter + 1;
        }
        if (!isBasic) {
            total = total + 16;
        }
        long newsize = this.getDataEndPosition() - this.getDataStartPosition();
        if (newsize < total) {
            newsize = total;
        }
        if (this.getParentChunk() != null) {
            if (this.getParentChunk().usesPadByte()) {
                newsize = KlangUtil.adjustForPadByte(newsize);
            }
        }
        if (newsize > (this.getDataEndPosition() - this.getDataStartPosition())) {
            long totnewsize = newsize + (this.getEndPosition() - this.getStartPosition()) - (this.getDataEndPosition() - this.getDataStartPosition());
            this.chunkIsAboutToChangeSize(totnewsize, raff);
            int amt = total - (int) (this.getDataEndPosition() - this.getDataStartPosition());
            byte[] newbyte = new byte[amt];
            KlangUtil.insertIntoFile(newbyte, this.getParentFile().getFile(), this.getDataStartPosition());
        }
        raff.seek(this.getDataStartPosition());
        counter = 0;
        while (counter < end) {
            if (counter == 6) {
                try {
                    if (isBigEndian) {
                        Int2ByteUnsignedBE size = new Int2ByteUnsignedBE();
                        size.setValue((int) newsize - 18);
                        size.writeValueToFile(raff);
                    } else {
                        Int2ByteUnsignedLE size = new Int2ByteUnsignedLE();
                        size.setValue((int) newsize - 18);
                        size.writeValueToFile(raff);
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                    throw new IOException(KlangConstants.ERROR_GENERAL_FILE);
                }
            }
            Primitive p = primitives.get(counter).getPrimitive();
            if (p.valueExists()) {
                try {
                    p.writeValueToFile(raff);
                } catch (BadValueException err) {
                    err.printStackTrace();
                }
            } else {
                raff.write(new byte[((PrimitiveFixedByte) p).getFixedLength()]);
            }
            counter = counter + 1;
        }
        if (!isBasic) {
            byte[] endpart = new byte[14];
            endpart[4] = 0x10;
            endpart[6] = -128;
            endpart[9] = -86;
            endpart[11] = 0x38;
            endpart[12] = -101;
            endpart[13] = 0x71;
            raff.write(endpart);
        }
        ((EditableFileBase) this.getParentFile()).reparseFile(raff);
        this.chunkJustChanged(raff);
    }
}
