package org.eclipse.releng.generators.rss;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.releng.util.rss.Messages;
import org.eclipse.releng.util.rss.RSSFeedUtil;

/**
 * Parameters: 
 *   debug - more output to console - eg., 0|1|2
 *   
 *   file - path to the XML file that will be read - eg., /path/to/file.to.read.xml
 *   xpath - xpath string representing the object to read
 * 
 * @author nickb
 *
 */
public class RSSFeedGetPropertyTask extends Task {

    private int debug = 0;

    private File file;

    private String xpath;

    public void setDebug(int debug) {
        this.debug = debug;
    }

    public void setFile(String file) {
        if (isNullString(file)) {
            System.err.println(Messages.getString("RSSFeedCommon.FileError"));
        } else {
            this.file = new File(file);
        }
    }

    public void setXpath(String xpath) {
        if (isNullString(xpath)) {
            System.err.println(Messages.getString("RSSFeedCommon.XpathError"));
        } else {
            this.xpath = xpath;
        }
    }

    public void execute() throws BuildException {
        RSSFeedUpdateEntryTask updater = new RSSFeedUpdateEntryTask();
        updater.setFile(file.toString());
        updater.setXpath(xpath);
        updater.setDebug(debug);
        updater.execute();
    }

    private static boolean isNullString(String str) {
        return RSSFeedUtil.isNullString(str);
    }
}
