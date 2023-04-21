package org.tolk.io.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.tolk.io.DataSource;
import org.tolk.io.StoreDataSource;

/**
 * Class to open, read and write to File
 * 
 * @author Werner van Rensburg
 * 
 */
public class TextFileDataSource extends DataSource implements InitializingBean, DisposableBean, StoreDataSource {

    private String path;

    private File file;

    private boolean readAllOnStartup = true;

    private boolean reading = true;

    private boolean resetInputStream = false;

    private boolean append = true;

    private boolean loop = false;

    private final Logger logger = Logger.getLogger(TextFileDataSource.class);

    private int lineIndex = 0;

    private ClientReaderThread readerThread;

    /**
     * see {@link InitializingBean#afterPropertiesSet()}
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.path);
        Assert.isTrue(this.openFile());
        if (this.readAllOnStartup) {
            this.resetInputStream = false;
            this.readerThread = new ClientReaderThread();
            this.readerThread.start();
        }
    }

    /**
     * open the input file and set inputStream scanner
     */
    private boolean openFile() {
        try {
            this.file = new File(this.path);
        } catch (NullPointerException e) {
            this.logger.error(e.getMessage());
            return false;
        }
        try {
            this.outputStream = new PrintWriter(new FileWriter(this.file, this.append));
            this.inputStream = new BufferedReader(new FileReader(this.file));
            this.lineIndex = 0;
        } catch (FileNotFoundException e) {
            this.logger.error(e.getMessage());
            return false;
        } catch (IOException e) {
            this.logger.error(e.getMessage());
        }
        return true;
    }

    /**
     * forward string to next nodes
     */
    public void forwardMessage(String message) {
        this.fwdMessageToAllNextNodes(message);
    }

    /**
     * see {@link StoreDataSource#readAll()}
     */
    public void readAll() {
        String line;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            this.logger.error(e1.getMessage());
        }
        if (this.resetInputStream) {
            this.resetInputStream = false;
            try {
                this.inputStream = new BufferedReader(new FileReader(this.path));
                this.lineIndex = 0;
            } catch (FileNotFoundException e) {
                this.logger.error(e.getMessage());
            }
        }
        try {
            while (this.reading) {
                if ((line = read()) != null) {
                    this.lineIndex++;
                    this.forwardMessage(line);
                } else {
                    if (this.loop) {
                        this.inputStream = new BufferedReader(new FileReader(this.path));
                        this.lineIndex = 0;
                    } else {
                        this.reading = false;
                    }
                }
            }
        } catch (IOException e) {
            this.logger.error(e.getMessage());
        }
    }

    /**
     * Function will readLine without forwarding for "fromIndex" times before passing control to readAll()
     * 
     * see {@link StoreDataSource#readAll(Object)}
     */
    public void readAll(Object fromIndex) {
        int lineNumber = (Integer) fromIndex;
        for (int i = 0; i < lineNumber; i++) {
            try {
                read();
            } catch (IOException e) {
                this.logger.error(e.getMessage());
            }
        }
        this.resetInputStream = false;
        this.reading = true;
        readAll();
    }

    /**
     * @TODO Implemented read of a range
     * 
     * see {@link StoreDataSource#readAll(Object, Object)}
     */
    public void readAll(Object fromIndex, Object toIndex) {
    }

    /**
     * see {@link StoreDataSource#resume()}
     */
    public void resume() {
        this.reading = true;
        this.resetInputStream = false;
        if (this.lineIndex > 0) {
            readAll(this.lineIndex);
        } else {
            readAll();
        }
    }

    /**
     * see {@link StoreDataSource#stop()}
     */
    public void stop() {
        this.reading = false;
    }

    /**
     * see {@link StoreDataSource#reset()}
     */
    public void reset() {
        this.resetInputStream = true;
        this.reading = true;
    }

    /**
     * @return the resetInputStream
     */
    public boolean isResetInputStream() {
        return this.resetInputStream;
    }

    /**
     * @param resetInputStream
     *            the resetInputStream to set
     */
    public void setResetInputStream(boolean resetInputStream) {
        this.resetInputStream = resetInputStream;
    }

    /**
     * @return the reading state
     */
    public boolean isReading() {
        return this.reading;
    }

    /**
     * @param reading
     *            the reading to set
     */
    public void setReading(boolean reading) {
        this.reading = reading;
    }

    /**
     * @return the append
     */
    public boolean isAppend() {
        return this.append;
    }

    /**
     * @param append
     *            the append parameter to define if a textfile should be overwrite of appended
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * see {@link StoreDataSource#getReadAllOnStartup()}
     */
    public boolean getReadAllOnStartup() {
        return this.readAllOnStartup;
    }

    /**
     * see {@link StoreDataSource#setReadAllOnStartup(boolean)}
     */
    public void setReadAllOnStartup(boolean readAllOnStartup) {
        this.readAllOnStartup = readAllOnStartup;
    }

    /**
     * @param loop
     *            the the loop variable
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    /**
     * 
     * @return loop
     */
    public boolean getLoop() {
        return this.loop;
    }

    /**
     * see {@link DisposableBean#destroy()}
     */
    public void destroy() throws Exception {
        stop();
    }

    /**
     * Simple thread that reads all.
     * 
     * @author Johan Roets
     */
    protected class ClientReaderThread extends Thread {

        @Override
        public void run() {
            while (TextFileDataSource.this.reading) {
                readAll();
            }
        }
    }
}
