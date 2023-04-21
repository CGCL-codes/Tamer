package de.schwarzrot.app.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.schwarzrot.app.Client;
import de.schwarzrot.app.ResponseHandler;
import de.schwarzrot.app.errors.ApplicationException;

public class FileClient implements Client {

    enum FileMode {

        READ, WRITE
    }

    public FileClient(File exchangeDirectory) {
        this(exchangeDirectory, Charset.defaultCharset());
    }

    public FileClient(File exchangeDirectory, Charset cs) {
        this.exchangeDirectory = exchangeDirectory;
        serverCharset = cs;
        mappedFiles = new HashMap<String, String>();
    }

    @Override
    public final int getNLSize() {
        return 2;
    }

    @Override
    public boolean isServerReachable() {
        return exchangeDirectory.isDirectory() && exchangeDirectory.canRead();
    }

    @Override
    public void requestResponse(ResponseHandler handler) {
        if (handler == null) {
            throw new ApplicationException("Oups - no handler? - don't know what to do!?!");
        }
        String fileName = handler.getCommand();
        FileMode mode = FileMode.READ;
        if (mappedFiles.containsKey(fileName)) {
            if (fileName.startsWith("G")) mode = FileMode.READ; else if (fileName.startsWith("P")) mode = FileMode.WRITE;
            fileName = mappedFiles.get(fileName);
        }
        if (mode.equals(FileMode.WRITE)) writeFile(fileName, handler); else readFile(fileName, handler);
    }

    public void setMappedFile(String key, String fileName) {
        mappedFiles.put(key, fileName);
    }

    protected final Log getLogger() {
        return LogFactory.getLog(getClass());
    }

    protected void readFile(String fileName, ResponseHandler handler) {
        File requestedFile = new File(exchangeDirectory, fileName);
        if (!(requestedFile.exists() && requestedFile.isFile())) {
            throw new ApplicationException("Oups, invalid file requested [" + requestedFile.getAbsolutePath() + "]");
        }
        if (!requestedFile.canRead()) {
            throw new ApplicationException("Oups, can't read requested file [" + requestedFile.getAbsolutePath() + "]");
        }
        FileInputStream fis = null;
        long lastSuccess = 0;
        if (handler.getArguments() != null && handler.getArguments() instanceof Long) {
            lastSuccess = (Long) handler.getArguments();
        }
        if (requestedFile.lastModified() / 1000 <= lastSuccess) {
            getLogger().info("seems as [" + requestedFile + "] has been processed already, so skip it this time");
            return;
        }
        PushbackReader pbr = null;
        try {
            fis = new FileInputStream(requestedFile);
            pbr = new PushbackReader(new InputStreamReader(fis, serverCharset), 40);
            pbr.unread(String.format("%d %d\n", requestedFile.lastModified() / 1000, requestedFile.length()).toCharArray());
            handler.processResponse(new BufferedReader(pbr));
        } catch (Throwable t) {
            throw new ApplicationException("failed to process server response of command \"" + handler.getCommand() + "\"", t);
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (Throwable t) {
            }
        }
    }

    protected void writeFile(String fileName, ResponseHandler handler) {
        File requestedFile = new File(exchangeDirectory, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(requestedFile, true);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(fos, serverCharset));
            out.println(handler.getArguments().toString());
            out.flush();
            String response = "250 Success\n";
            handler.processResponse(new BufferedReader(new StringReader(response)));
        } catch (Throwable t) {
            throw new ApplicationException("failed to process server request of command \"" + handler.getCommand() + "\"", t);
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Throwable t) {
            }
        }
    }

    private File exchangeDirectory;

    private Charset serverCharset;

    private Map<String, String> mappedFiles;
}
