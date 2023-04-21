package org.antdepo.tasks.session;

import org.apache.tools.ant.BuildException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @ant.task name="session-getinstance"
 */
public class SessionGetInstance extends BaseSessionTask {

    /**
     * reference to file property
     */
    private File file;

    /**
     * setter to file property
     *
     * @param file file to store session data
     */
    public void setFile(final File file) {
        this.file = file;
    }

    private boolean pristine;

    /**
     * setter to new property
     * @param b true to ensure a pristine new session;
     */
    public void setNew(final boolean b) {
        pristine = b;
    }

    /**
     * implementions should validate their required input
     */
    void validate() {
        if (null == session) {
            throw new BuildException("session attribute not set");
        }
    }

    /**
     * Executes the task
     */
    public void execute() {
        validate();
        final Session session;
        if (pristine) {
            session = newSessionInstance();
        } else {
            session = getSessionInstance();
        }
        if (null != file && file.exists()) {
            final Properties properties = new Properties();
            try {
                FileInputStream fis = new FileInputStream(file);
                try {
                    properties.load(fis);
                } finally {
                    if (null != fis) {
                        fis.close();
                    }
                }
                session.putAll(properties);
            } catch (IOException e) {
                throw new BuildException("failed loading file", e);
            }
        }
        storeResult(session.getId());
    }
}
