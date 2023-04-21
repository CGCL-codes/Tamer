package org.bellard.qemoon.runtime;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Eric Bellard - eric.bellard@gmail.com
 * 
 */
public class StreamThread implements Runnable {

    protected Thread t;

    protected String pid;

    protected Logger logger;

    protected Level level;

    protected InputStream in;

    public StreamThread(String pid, Logger logger, Level level, InputStream in) {
        this.pid = pid;
        this.logger = logger;
        this.level = level;
        this.in = in;
    }

    public void start() {
        t = new Thread(this);
        t.start();
    }

    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                logger.log(level, pid + " " + line);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                br.close();
            } catch (Exception e) {
            }
        }
    }
}
