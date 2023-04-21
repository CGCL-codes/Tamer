package com.lepidllama.packageeditor.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import com.lepidllama.packageeditor.core.LogFacade;
import com.lepidllama.packageeditor.core.exception.ParsingRuntimeException;
import com.lepidllama.packageeditor.core.exception.WritingRuntimeException;
import com.lepidllama.packageeditor.dbpf.Header;
import com.lepidllama.packageeditor.dbpf.IndexBlock;
import com.lepidllama.packageeditor.fileio.DataReader;
import com.lepidllama.packageeditor.fileio.DataWriter;
import com.lepidllama.packageeditor.resources.interfaces.TextInputting;
import com.lepidllama.packageeditor.resources.interfaces.TextOutputting;
import com.lepidllama.packageeditor.utility.StringUtils;

public abstract class AbstractXmlResource<T> extends Resource implements TextOutputting, TextInputting {

    private JAXBContext context;

    String typeName;

    protected T unmarshalled;

    private String backup;

    private long start;

    public AbstractXmlResource(Class[] jaxbClass, String typeName) {
        try {
            context = JAXBContext.newInstance(jaxbClass);
            this.typeName = typeName;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void read(DataReader in, Header header, IndexBlock indexBlock) {
        backup = null;
        start = in.getFilePointer();
        ByteArrayInputStream bais = new ByteArrayInputStream(in.readChunk((int) indexBlock.getDecompressedSize()));
        try {
            unmarshalled = (T) (context.createUnmarshaller().unmarshal(bais));
        } catch (JAXBException e) {
            LogFacade.getLogger(this).log(Level.SEVERE, "Unable to parse XML in " + typeName, e);
            in.seek(start);
            backup = in.readFixedString((int) indexBlock.getDecompressedSize());
        }
    }

    public void write(DataWriter out, Header header, IndexBlock indexBlock) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(unmarshalled, baos);
        } catch (JAXBException e) {
            LogFacade.getLogger(this).log(Level.SEVERE, "Unable to write XML in Community CC Manifest", e);
            throw new WritingRuntimeException(e);
        }
        out.writeChunk(baos.toByteArray());
    }

    public void setText(String text) {
        ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
        try {
            unmarshalled = (T) context.createUnmarshaller().unmarshal(bais);
        } catch (JAXBException e) {
            LogFacade.getLogger(this).log(Level.SEVERE, "Unable to parse XML in Community CC Manifest", e);
            throw new ParsingRuntimeException(e);
        }
    }

    public String getText() {
        if (backup != null) {
            return backup;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            context.createMarshaller().marshal(unmarshalled, baos);
        } catch (JAXBException e) {
            LogFacade.getLogger(this).log(Level.SEVERE, "Unable to write XML in Community CC Manifest", e);
            throw new WritingRuntimeException(e);
        }
        return new String(baos.toByteArray());
    }
}
