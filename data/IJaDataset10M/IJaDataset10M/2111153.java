package net.sf.filePiper.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.log4j.Logger;

/**
 * Base implementation for FileProcessor with ONE to ONE cardinality and processing text streams. <br>
 * The original streams are converted to BufferedReader and BufferedWriter to allow to read and write text easily. There is only
 * 3 remaining abstract methods very simple to implement.
 * 
 * @author BEROL
 */
public abstract class OneToOneTextFileProcessor extends OneToOneByteFileProcessor {

    private Logger log = Logger.getLogger(OneToOneTextFileProcessor.class);

    public void process(InputStream is, InputFileInfo info, FileProcessorEnvironment env) throws IOException {
        try {
            inputFileStarted();
            setCurrentInputFileInfo(info);
            setProposedFilePath(info);
            OutputStream os = env.getOutputStream(info);
            process(is, os, env);
            setCurrentInputFileInfo(null);
        } catch (Exception e) {
            IOException ioe = new IOException("Exception while processing input " + info);
            ioe.initCause(e);
            throw ioe;
        }
    }

    public void process(InputStream is, OutputStream os, FileProcessorEnvironment env) throws Exception {
        BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(os));
        process(rdr, wr, env);
        if (log.isDebugEnabled()) log.debug("Processor " + this + " closes output stream");
        wr.close();
    }

    public abstract void process(BufferedReader in, BufferedWriter out, FileProcessorEnvironment env) throws Exception;
}
