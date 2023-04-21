package org.jdna.sagetv.networkencoder.dummy;

import java.io.File;
import org.apache.log4j.Logger;
import org.jdna.sagetv.networkencoder.CommandException;
import org.jdna.sagetv.networkencoder.GetFileSizeCommand;
import org.jdna.sagetv.networkencoder.INetworkEncoder;
import org.jdna.sagetv.networkencoder.StartCommand;
import org.jdna.sagetv.networkencoder.StopCommand;
import org.jdna.sagetv.networkencoder.util.CopyEncoder;

/**
 * When a recording starts, it simply copies the source file to the destination file to provider a dummy recording.
 * 
 * @author seans
 */
public class DummyFileCopyEncoder implements INetworkEncoder {

    private Logger log = Logger.getLogger(DummyFileCopyEncoder.class);

    private boolean active = false;

    private File srcFile = null;

    private File destFile = null;

    private CopyEncoder copyEncoder = null;

    private long size = 0;

    public DummyFileCopyEncoder(String srcFile) {
        this.srcFile = new File(srcFile);
        if (!this.srcFile.exists()) {
            throw new RuntimeException("Dummy Encoder will fail since src file does not exist: " + srcFile);
        }
        log.info("Copy Encoder Created, using file: " + srcFile);
    }

    @Override
    public long getFileSize(GetFileSizeCommand command) throws CommandException {
        return size += 10000;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void start(StartCommand command) throws CommandException {
        if (isActive()) {
            throw new CommandException("Already Recording...");
        }
        size = 0;
        active = true;
        destFile = new File(command.getFilename());
        copyEncoder = new CopyEncoder();
        try {
            copyEncoder.encode(srcFile, destFile);
        } catch (Exception e) {
            throw new CommandException("Failed to start the copy encoder");
        }
    }

    @Override
    public void stop(StopCommand command) throws CommandException {
        active = false;
        if (copyEncoder != null) {
            copyEncoder.stop();
        }
        destFile = null;
    }
}
