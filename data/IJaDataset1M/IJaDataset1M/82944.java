package org.archive.crawler.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import org.archive.settings.jmx.JMXSheetManager;

public class Sheet {

    /**
	 * Job that owns this sheet.
	 */
    private Job job;

    /**
	 * Name of the sheet.
	 */
    private String name;

    public Sheet(Job job, String name) {
        this.job = job;
        this.name = name;
    }

    protected JMXSheetManager getManager() {
        return getJob().getJMXSheetManager();
    }

    public Job getJob() {
        return job;
    }

    public String getName() {
        return name;
    }
}
