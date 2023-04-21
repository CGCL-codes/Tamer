package com.lepidllama.packageeditor.sims3pack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.lepidllama.packageeditor.core.LogFacade;
import com.lepidllama.packageeditor.fileio.DataReader;
import com.lepidllama.packageeditor.fileio.FileDataReader;
import com.lepidllama.packageeditor.sims3pack.marshall.Dependencies;
import com.lepidllama.packageeditor.sims3pack.marshall.LocalizedDescriptions;
import com.lepidllama.packageeditor.sims3pack.marshall.LocalizedNames;
import com.lepidllama.packageeditor.sims3pack.marshall.PackagedFile;
import com.lepidllama.packageeditor.sims3pack.marshall.Sims3Package;

public class Sims3Pack {

    int headerUnk;

    private Sims3Package xml;

    private FileDataReader input;

    long packStart;

    private static JAXBContext context;

    static {
        try {
            context = JAXBContext.newInstance(Sims3Package.class, Dependencies.class, LocalizedNames.class, LocalizedDescriptions.class, PackagedFile.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void extractPackedFile(PackagedFile packed, File f) throws IOException {
        input.seek(0);
        this.input.seek(packStart + packed.getOffset());
        FileOutputStream fos = new FileOutputStream(f);
        for (long i = 0; i < packed.getLength(); i++) {
            int byt = input.readUnsignedByte();
            fos.write(byt);
        }
        fos.close();
    }

    public byte[] getPackedFileByteArray(PackagedFile packed) {
        input.seek(packStart + packed.getOffset());
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        for (long i = 0; i < packed.getLength(); i++) {
            int byt = input.readByte();
            fos.write(byt);
        }
        return fos.toByteArray();
    }

    /**
	 * This returns the whole file reader with the offset pointing to
	 * the start of the relevant file. As such, it is NOT threadsafe,
	 * and the value should not be cached. Use it every time you need a 
	 * handle to the file.
	 * @param number
	 * @return
	 * @throws IOException
	 */
    public DataReader getPackedFileHandle(int number) {
        long offset = xml.getPackagedFile().get(number).getOffset();
        input.seek(offset);
        return input;
    }

    public long getPackedFileLength(int number) {
        return xml.getPackagedFile().get(number).getLength();
    }

    public Sims3Package getContents() {
        return xml;
    }

    public void read(FileDataReader in) {
        this.input = in;
        in.readLengthString();
        headerUnk = in.readWordInt();
        long size = in.readDwordLong();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nextByte;
        while (--size >= 0 && (nextByte = in.readByte()) != 0) {
            baos.write(nextByte);
        }
        if (size > 0) {
            in.readChunk((int) size);
        }
        try {
            Unmarshaller m = context.createUnmarshaller();
            xml = (Sims3Package) m.unmarshal(new ByteArrayInputStream(baos.toByteArray()));
        } catch (JAXBException e) {
            LogFacade.getLogger(this).log(Level.SEVERE, "Unable to parse XML Index", e);
            throw new RuntimeException(e);
        }
        packStart = in.getFilePointer();
    }
}
