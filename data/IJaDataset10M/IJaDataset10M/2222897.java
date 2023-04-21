package org.hugh.eclipse.tools.bundleclassfinder.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.eclipse.core.runtime.IPath;
import org.hugh.eclipse.tools.bundleclassfinder.Activator;
import org.xml.sax.InputSource;

/**
 * 
 * 
 * @author hugh
 */
public class ClassnameHistoryManager {

    static String[] previousClassnames = new String[0];

    public static final int DEFAULT_MAX_CLASSNAMES = 10;

    private static final String CLASSNAME_HIST_FILE = "ClassnameHistory.xml";

    static final String ELEMENT_CLASSNAME = "Classname";

    static final String ELEMENT_CLASSNAME_HISTORY = "Classnames";

    private int maxClassnames = DEFAULT_MAX_CLASSNAMES;

    public void startup() {
        IPath pluginStateLocation = Activator.getDefault().getStateLocation().append(CLASSNAME_HIST_FILE);
        File file = pluginStateLocation.toFile();
        if (!file.exists()) return;
        try {
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            try {
                readCommentHistory(is);
            } finally {
                is.close();
            }
        } catch (Exception e) {
            Activator.log(e);
        }
    }

    private void readCommentHistory(InputStream stream) throws Exception {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(stream), new ClassnameHistoryContentHandler());
        } catch (Exception e) {
            Activator.log(e);
        }
    }

    public void shutdown() throws Exception {
        saveClassnameHistory();
    }

    protected void saveClassnameHistory() throws Exception {
        IPath pluginStateLocation = Activator.getDefault().getStateLocation();
        File tempFile = pluginStateLocation.append(CLASSNAME_HIST_FILE + ".tmp").toFile();
        File histFile = pluginStateLocation.append(CLASSNAME_HIST_FILE).toFile();
        try {
            XMLWriter writer = new XMLWriter(new BufferedOutputStream(new FileOutputStream(tempFile)));
            try {
                writeCommentHistory(writer);
            } finally {
                writer.close();
            }
            if (histFile.exists()) {
                histFile.delete();
            }
            tempFile.renameTo(histFile);
        } catch (IOException e) {
            Activator.log(e);
        }
    }

    private void writeCommentHistory(XMLWriter writer) {
        writer.startTag(ELEMENT_CLASSNAME_HISTORY, null, false);
        for (int i = 0; i < previousClassnames.length && i < maxClassnames; i++) writer.printSimpleTag(ELEMENT_CLASSNAME, previousClassnames[i]);
        writer.endTag(ELEMENT_CLASSNAME_HISTORY);
    }

    public void addClassname(String classname) {
        int index = getClassnameIndex(classname);
        if (index != -1) {
            makeFirstElement(index);
            return;
        }
        String[] newClassname = new String[Math.min(previousClassnames.length + 1, maxClassnames)];
        newClassname[0] = classname;
        for (int i = 1; i < newClassname.length; i++) {
            newClassname[i] = previousClassnames[i - 1];
        }
        previousClassnames = newClassname;
    }

    public String[] getPreviousClassname() {
        return previousClassnames;
    }

    private int getClassnameIndex(String classname) {
        for (int i = 0; i < previousClassnames.length; i++) {
            if (previousClassnames[i].equals(classname)) {
                return i;
            }
        }
        return -1;
    }

    private void makeFirstElement(int index) {
        String[] newComments = new String[previousClassnames.length];
        newComments[0] = previousClassnames[index];
        System.arraycopy(previousClassnames, 0, newComments, 1, index);
        int maxIndex = previousClassnames.length - 1;
        if (index != maxIndex) {
            int nextIndex = (index + 1);
            System.arraycopy(previousClassnames, nextIndex, newComments, nextIndex, (maxIndex - index));
        }
        previousClassnames = newComments;
    }
}
