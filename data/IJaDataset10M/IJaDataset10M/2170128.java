package org.spantus.work.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.spantus.core.extractor.DefaultExtractorConfig;
import org.spantus.core.extractor.ExtractorOutputHolder;
import org.spantus.core.extractor.ExtractorVectorOutputHolder;
import org.spantus.core.extractor.IExtractor;
import org.spantus.core.extractor.IExtractorInputReader;
import org.spantus.core.extractor.IExtractorVector;
import org.spantus.core.extractor.dao.ReaderDao;
import org.spantus.exception.ProcessingException;
import org.spantus.extractor.ExtractorInputReader;
import org.spantus.logger.Logger;
import org.spantus.work.services.converter.FrameValues3DConverter;
import org.spantus.work.services.converter.FrameValuesConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.enums.EnumConverter;

public class ReaderXmlDaoImpl implements ReaderDao {

    private XStream xstream = null;

    protected Logger log = Logger.getLogger(getClass());

    public IExtractorInputReader read(File file) {
        IExtractorInputReader reader = null;
        try {
            FileReader inFile = new FileReader(file);
            reader = (IExtractorInputReader) getXsteam().fromXML(inFile);
            log.debug("extractors file read correctly. info: " + file.getAbsolutePath());
        } catch (ConversionException e) {
            log.debug("error while reading: " + file.getAbsolutePath());
            throw new ProcessingException(e);
        } catch (FileNotFoundException e) {
            throw new ProcessingException(e);
        }
        return reader;
    }

    public IExtractorInputReader read(InputStream inputStream) {
        IExtractorInputReader reader = null;
        reader = (IExtractorInputReader) getXsteam().fromXML(inputStream);
        log.debug("extractors file read correctly. info ");
        return reader;
    }

    public void write(IExtractorInputReader holder, File file) {
        try {
            FileWriter outputFile = new FileWriter(file, false);
            getXsteam().toXML(prepareToSave(holder), outputFile);
            log.debug("reader are exported: " + file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(IExtractorInputReader holder, OutputStream outputStream) {
        getXsteam().toXML(prepareToSave(holder), outputStream);
    }

    protected IExtractorInputReader prepareToSave(IExtractorInputReader reader) {
        ExtractorInputReader rtnReader = new ExtractorInputReader();
        for (IExtractor extractor : reader.getExtractorRegister()) {
            ExtractorOutputHolder holder = new ExtractorOutputHolder();
            holder.setName(extractor.getName());
            holder.setOutputValues(extractor.getOutputValues());
            rtnReader.registerExtractor(holder);
        }
        for (IExtractorVector extractor : reader.getExtractorRegister3D()) {
            ExtractorVectorOutputHolder holder = new ExtractorVectorOutputHolder();
            holder.setName(extractor.getName());
            holder.setOutputValues(extractor.getOutputValues());
            rtnReader.registerExtractor(holder);
        }
        rtnReader.setConfig(new DefaultExtractorConfig(reader.getConfig()));
        return rtnReader;
    }

    protected XStream getXsteam() {
        if (xstream == null) {
            xstream = new XStream();
            xstream.registerConverter(new EnumConverter());
            xstream.registerConverter(new FrameValuesConverter());
            xstream.registerConverter(new FrameValues3DConverter());
        }
        return xstream;
    }
}
