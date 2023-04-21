package org.exmaralda.folker.data;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.exmaralda.common.jdomutilities.IOUtilities;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author thomas
 */
public class TranscriptionHead {

    Element headElement;

    public TranscriptionHead(File f) throws IOException, JDOMException {
        Document transcription = IOUtilities.readDocumentFromLocalFile(f.getAbsolutePath());
        headElement = (Element) XPath.newInstance("//head").selectSingleNode(transcription);
        headElement.detach();
    }

    public TranscriptionHead() {
        headElement = new Element("head");
        Element logElement = new Element("transcription-log");
        headElement.addContent(logElement);
        Element firstLogEntry = new Element("log-entry");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        firstLogEntry.setAttribute("start", formattedDate);
        firstLogEntry.setAttribute("end", formattedDate);
        firstLogEntry.setAttribute("who", "system");
        firstLogEntry.setText("transcription-log created");
        logElement.addContent(firstLogEntry);
    }

    Element getHeadElement() {
        return headElement;
    }

    public void appendLog(Element newLog) {
        getLogElement().addContent(newLog);
    }

    Element getLogElement() {
        return headElement.getChild("transcription-log");
    }
}
