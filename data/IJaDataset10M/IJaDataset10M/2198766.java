package com.anotherbigidea.flash.readers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFFileSignature;
import com.anotherbigidea.flash.interfaces.SWFTags;
import com.anotherbigidea.flash.structs.Rect;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.io.InStream;

/**
 * Reads a SWF input stream and drives the SWFTags interface.
 * 
 * Automatically decompresses the file if necessary.
 */
public class SWFReader {

    protected SWFTags mConsumer;

    protected InStream mIn;

    protected InputStream mInputstream;

    protected boolean mCompressed;

    protected String mFilename;

    /**
	 * Read from a file.
	 * Must call readFile() in order to properly close the file.
	 * 
	 * @param consumer may also implement the SWFFileSignature interface.
	 */
    public SWFReader(SWFTags consumer, String filename) throws IOException {
        this(consumer, new FileInputStream(filename));
        mFilename = filename;
    }

    /**
     * @param consumer may also implement the SWFFileSignature interface.
     */
    public SWFReader(SWFTags consumer, InputStream inputstream) {
        mConsumer = consumer;
        mInputstream = inputstream;
        mIn = new InStream(inputstream);
    }

    /**
	 * @param consumer may also implement the SWFFileSignature interface.
	 */
    public SWFReader(SWFTags consumer, InStream instream) {
        mConsumer = consumer;
        mIn = instream;
    }

    /**
     * Drive the consumer by reading a SWF File - including the header and all tags
     */
    public void readFile() throws IOException {
        readHeader();
        readTags();
        if (mFilename != null) mInputstream.close();
    }

    /**
     * Drive the consumer by reading SWF tags only.  The full header must have
     * been read prior to this.
     */
    public void readTags() throws IOException {
        while (readOneTag() != SWFConstants.TAG_END) ;
    }

    /**
     * Drive the consumer by reading one tag
     * @return the tag type
     */
    public int readOneTag() throws IOException {
        int header = mIn.readUI16();
        int type = header >> 6;
        int length = header & 0x3F;
        boolean longTag = (length == 0x3F);
        if (longTag) {
            length = (int) mIn.readUI32();
        }
        byte[] contents = mIn.read(length);
        mConsumer.tag(type, longTag, contents);
        return type;
    }

    /**
     * Read and verify just the file signature.
     */
    public void readSignature() throws IOException {
        int[] sig = { mIn.readUI8(), mIn.readUI8(), mIn.readUI8() };
        if ((sig[0] != 0x46 && sig[0] != 0x43) || (sig[1] != 0x57) || (sig[2] != 0x53)) {
            throw new IOException("Invalid SWF File Signature");
        }
        mCompressed = sig[0] == 0x43;
        if (mConsumer instanceof SWFFileSignature) {
            ((SWFFileSignature) mConsumer).signature(mCompressed ? SWFFileSignature.SIGNATURE_COMPRESSED : SWFFileSignature.SIGNATURE_NORMAL);
        }
    }

    /** 
     * Read the SWF file header - including the signature.
     */
    public void readHeader() throws IOException {
        readSignature();
        readRemainderOfHeader();
    }

    /**
     * Read the header after the signature.  The signature must have been read
     * prior to this.
     */
    public void readRemainderOfHeader() throws IOException {
        int version = mIn.readUI8();
        long length = mIn.readUI32();
        if (mCompressed) mIn.readCompressed();
        Rect frameSize = new Rect(mIn);
        int frameRate = mIn.readUI16() >> 8;
        int frameCount = mIn.readUI16();
        mConsumer.header(version, length, frameSize.getMaxX(), frameSize.getMaxY(), frameRate, frameCount);
    }

    /**
     * Test: read from args[0] and write to args[1].
     * 
     * If args[2] is '+' then output is forced to be compressed, if it is '-'
     * then output is forced to be uncompressed - otherwise the output is the
     * same as the input.
     */
    public static void main(String[] args) throws IOException {
        SWFWriter writer = new SWFWriter(args[1]);
        if (args.length > 2) {
            if (args[2].equals("+")) writer.setCompression(true); else if (args[2].equals("-")) writer.setCompression(false);
        }
        SWFReader reader = new SWFReader(writer, args[0]);
        reader.readFile();
    }
}
