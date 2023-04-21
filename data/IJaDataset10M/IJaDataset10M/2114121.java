package com.lepidllama.packageeditor.dbpf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import com.lepidllama.packageeditor.core.LogFacade;
import com.lepidllama.packageeditor.core.exception.ParsingRuntimeException;
import com.lepidllama.packageeditor.core.exception.UnexpectedValueParsingRuntimeException;
import com.lepidllama.packageeditor.fileio.ByteArrayDataReader;
import com.lepidllama.packageeditor.fileio.ByteArrayDataWriter;
import com.lepidllama.packageeditor.fileio.CountOnlyDataWriter;
import com.lepidllama.packageeditor.fileio.DataReader;
import com.lepidllama.packageeditor.fileio.DataWriter;
import com.lepidllama.packageeditor.fileio.Decompressor;
import com.lepidllama.packageeditor.fileio.FileDataReader;
import com.lepidllama.packageeditor.resources.Resource;
import com.lepidllama.packageeditor.resources.ResourceFactory;
import com.lepidllama.packageeditor.resources.ResourceSupport;

public class ResourceLookupPack extends ResourceSupport {

    private static int CHUNK_SIZE = 256;

    private File file;

    private IndexBlock indexBlock;

    private Header header;

    private DataReader bufferedStream;

    private int streamStart;

    public ResourceLookupPack(File file, Header header, IndexBlock indexBlock) {
        this.file = file;
        this.bufferedStream = null;
        this.header = header;
        this.indexBlock = indexBlock;
    }

    public ResourceLookupPack(DataReader stream, Header header, IndexBlock indexBlock) {
        this.file = null;
        this.bufferedStream = stream;
        this.header = header;
        this.indexBlock = indexBlock;
    }

    /**
	 * If this function returns a FileDataReader, it is the caller's responsibility
	 * to ensure that that FileDataReader file handle is closed.
	 * @return A DataReader ready to start reading at the start of the resource
	 * @throws FileNotFoundException if the file which originally contained this
	 * 			resource has been moved/renamed/deleted
	 */
    private DataReader getDataReader() throws FileNotFoundException {
        if (bufferedStream != null) {
            bufferedStream.seek(streamStart);
            return getBufferedStream();
        } else {
            FileDataReader in = new FileDataReader(file);
            DataReader decompressed;
            try {
                decompressed = Decompressor.seekAndDecompress(in, indexBlock.isCompressed(), indexBlock);
            } catch (IOException e) {
                LogFacade.getLogger(this).log(Level.SEVERE, "Could not decompress content - IO exception during write");
                throw new ParsingRuntimeException("Could not decompress content - IO exception during write", e);
            }
            if (in != decompressed) {
                in.close();
            }
            return decompressed;
        }
    }

    private DataReader getDataReaderMaintainCompression() throws FileNotFoundException, IOException {
        if (bufferedStream != null) {
            bufferedStream.seek(streamStart);
            return getBufferedStream();
        } else {
            FileDataReader fdr = new FileDataReader(file);
            fdr.seek(indexBlock.getLocation());
            return fdr;
        }
    }

    private DataReader getBufferedStream() {
        return bufferedStream;
    }

    public void setBufferedStream(DataReader stream) {
        file = null;
        Object old = this.bufferedStream;
        this.bufferedStream = stream;
        this.propChangeSupp.firePropertyChange("stream", old, stream);
    }

    /**
	 * Decompress and parse the resource in this RLP into a new Resource object. The subclass of Resource you
	 * are returned depends on the Type code specified in the index block. If no subclass of Resource is known
	 * to parse this type of resource, then it will return an UnknownResource.
	 * @return a new instance of some subclass of Resource, matching the type of this RLP and containing the data
	 * represented by this RLP.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public Resource getResource() throws FileNotFoundException, IOException {
        Resource res = ResourceFactory.getEmptyResource(indexBlock.getType());
        return getResourceAsType(res);
    }

    /**
	 * Decompress and parse the resource in this RLP into the provided Resource object. This should be a subclass of
	 * Resource which is capable of decoding the contents of this RLP.
	 * @return The original baseResource object, having parsed the data referenced by this RLP.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public <T extends Resource> T getResourceAsType(T baseResource) throws FileNotFoundException {
        DataReader in = getDataReader();
        try {
            baseResource.read(in, header, indexBlock);
        } catch (UnexpectedValueParsingRuntimeException uvpre) {
            LogFacade.getLogger(this).log(Level.SEVERE, "Could not parse resource: " + uvpre.getMessage(), uvpre);
            throw uvpre;
        } catch (ParsingRuntimeException pre) {
            LogFacade.getLogger(this).log(Level.SEVERE, "Could not parse resource", pre);
            throw pre;
        }
        if (in instanceof FileDataReader) {
            ((FileDataReader) in).close();
        }
        return baseResource;
    }

    public IndexBlock getIndexBlock() {
        return indexBlock;
    }

    public Header getHeader() {
        return header;
    }

    public void setIndexBlock(IndexBlock indexBlock) {
        this.indexBlock = indexBlock;
    }

    public String getFilename() {
        if (bufferedStream != null) {
            return "In-Memory Stream";
        }
        return file.getName();
    }

    public void setFile(File file) {
        bufferedStream = null;
        Object old = this.file;
        this.file = file;
        this.propChangeSupp.firePropertyChange("file", old, file);
    }

    public void setHeader(Header header) {
        Object old = this.header;
        this.header = header;
        this.propChangeSupp.firePropertyChange("header", old, header);
    }

    public void transferUncompressed(OutputStream out) throws FileNotFoundException, IOException {
        DataReader in = getDataReader();
        long size = indexBlock.getDecompressedSize();
        if (!indexBlock.isCompressed()) {
            size = indexBlock.getSize();
        }
        while (size > CHUNK_SIZE) {
            out.write(in.readChunk(CHUNK_SIZE));
            size -= CHUNK_SIZE;
        }
        while (size > 0) {
            out.write(in.readByte());
            size--;
        }
        if (in instanceof FileDataReader) {
            ((FileDataReader) in).close();
        }
    }

    public void transferDirect(DataWriter out) throws IOException {
        DataReader in = getDataReaderMaintainCompression();
        in.seek(indexBlock.getLocation());
        long size = indexBlock.getDecompressedSize();
        if (indexBlock.isCompressed()) {
            size = indexBlock.getSize();
        }
        while (size > CHUNK_SIZE) {
            out.writeChunk(in.readChunk(CHUNK_SIZE));
            size -= CHUNK_SIZE;
        }
        while (size > 0) {
            out.writeByte(in.readByte());
            size--;
        }
        if (in instanceof FileDataReader) {
            ((FileDataReader) in).close();
        }
    }

    public void setBufferedStream(DataReader badr, long maxLength, boolean b) {
        indexBlock.setCompressed(b);
        indexBlock.setDecompressedSize(maxLength);
        indexBlock.setSize(maxLength);
        indexBlock.setLocation(0);
        setBufferedStream(badr);
    }

    /**
	 * This takes any Resource, serializes it to a buffered stream,
	 * and then sets this rlp to point to that buffered stream 
	 * @param resource The resource to be serialized
	 */
    public void buildAndSetBufferedStream(Resource resource) {
        CountOnlyDataWriter counter = new CountOnlyDataWriter();
        resource.write(counter, getHeader(), getIndexBlock());
        ByteArrayDataWriter write = new ByteArrayDataWriter((int) counter.getMaxLength());
        resource.write(write, getHeader(), getIndexBlock());
        ByteArrayDataReader badr = new ByteArrayDataReader(write.getContent());
        setBufferedStream(badr, counter.getMaxLength(), false);
    }
}
