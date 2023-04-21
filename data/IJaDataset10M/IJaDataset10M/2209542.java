package net.sourceforge.x360mediaserve.plugins.audioTranscoder.impl.streamers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses external commands to output a file in a compatible stream type
 * 
 * @author tom
 * 
 */
public class StreamExternal implements StreamStreamer {

    Logger logger = LoggerFactory.getLogger(StreamExternal.class);

    private String command;

    private File scriptDir;

    /**
	 * @param command
	 *            The command in the script directory to be executed
	 * @param handler
	 *            The format handler
	 */
    public StreamExternal(String command, File scriptDir) {
        this.command = command;
        this.scriptDir = scriptDir;
    }

    /**
	 * Simple thread that reads from a stream as fast as it can, needed to
	 * prevent stderr buffer from getting full and blocking the decoder
	 * 
	 * @author tom
	 * 
	 */
    private class Consumer implements Runnable {

        BufferedInputStream inputStream;

        public Consumer(InputStream is) {
            inputStream = new BufferedInputStream(is);
        }

        public void run() {
            if (inputStream != null) {
                try {
                    byte[] input = new byte[4096];
                    while (inputStream.read(input) != -1) {
                    }
                } catch (IOException e) {
                    logger.debug("Error in consumer:", e);
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    protected String getCommand() {
        return command;
    }

    protected void setCommand(String command) {
        this.command = command;
    }

    protected File getScriptDir() {
        return scriptDir;
    }

    public void writeToStream(String url, OutputStream os) {
        logger.debug("Writing stream for file " + url);
        Process p = null;
        BufferedInputStream is = null;
        try {
            String convertcommand;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                convertcommand = this.getScriptDir().toString() + "\\" + getCommand() + ".bat";
            } else {
                convertcommand = this.getScriptDir().toString() + "/" + getCommand();
            }
            String[] cmd = { convertcommand, "\"" + url.toString() + "\"" };
            byte input[] = new byte[1000];
            logger.debug(convertcommand);
            logger.debug(url.toString());
            p = Runtime.getRuntime().exec(cmd, null, this.getScriptDir());
            is = new BufferedInputStream(p.getInputStream());
            ;
            BufferedInputStream es = new BufferedInputStream(p.getErrorStream());
            ;
            Consumer a = new Consumer(es);
            new Thread(a).start();
            int count;
            while ((count = is.read(input)) != -1) {
                os.write(input, 0, count);
            }
        } catch (Exception e) {
            logger.debug(e.toString());
        } finally {
            if (p != null) p.destroy();
            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }
}
