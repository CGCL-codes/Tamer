package uk.ac.ed.rapid.data.filesystem.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import uk.ac.ed.rapid.data.filesystem.AbstractFileSystem;
import uk.ac.ed.rapid.exception.RapidException;
import uk.ac.ed.rapid.jobdata.JobData;

/**
 * File System extends AbstractFileSystem and represents the local file system.
 *
 * @author Jos Koetsier
 */
public class LocalFileSystem extends AbstractFileSystem {

    public LocalFileSystem() {
    }

    @Override
    public String toString() {
        String result = "";
        result = result + "Local SERVER\n";
        result = result + "description " + getName() + "\n";
        result = result + "URL: " + getURL() + "\n";
        return result;
    }

    @Override
    public String execute(String command, JobData table, int subJob) throws RapidException {
        return execute(command);
    }

    @Override
    public String execute(String command) throws RapidException {
        InputStream stderr = null;
        InputStream stdout = null;
        try {
            URL fileURL = new URL(this.getURL());
            Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command }, null, new File(fileURL.getPath()));
            stderr = process.getErrorStream();
            stdout = process.getInputStream();
            byte[] buf = new byte[100];
            StringBuffer output = new StringBuffer();
            int n = 0;
            while ((n = stdout.read(buf)) != -1) output.append(new String(buf, 0, n));
            if (process.waitFor() != 0) {
                String error = "";
                n = 0;
                while ((n = stderr.read(buf)) != -1) error += new String(buf, 0, n);
                throw new RapidException("fail. Command failed to execute: " + error);
            } else return output.toString();
        } catch (InterruptedException ex) {
            throw new RapidException("fail: Process interrupted: " + ex.getMessage());
        } catch (IOException ex) {
            throw new RapidException("fail: IO Error: " + ex.getMessage());
        } finally {
            if (stderr != null) try {
                stderr.close();
            } catch (IOException ex) {
                throw new RapidException("Could not close stderr!");
            }
        }
    }
}
