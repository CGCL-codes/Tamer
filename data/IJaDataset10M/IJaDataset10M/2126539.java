package net.bpfurtado.ljcolligo.persistence;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import net.bpfurtado.ljcolligo.model.Comment;
import net.bpfurtado.ljcolligo.model.Event;
import net.bpfurtado.ljcolligo.model.EventMetadata;
import net.bpfurtado.ljcolligo.model.LJColligoObservable;
import net.bpfurtado.ljcolligo.util.Util;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Events2XMLWriter extends LJColligoObservable {

    public static final Logger logger = Logger.getLogger(Events2XMLWriter.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Element root;

    public Events2XMLWriter() {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("events");
        this.root = root;
    }

    public void write(Collection<Event> events, File outputPath) {
        for (Event e : events) {
            Element n = root.addElement("event");
            n.addAttribute("id", getString(e.getId()));
            n.addAttribute("subject", e.getSubject());
            n.addAttribute("date", getString(e.getDate()));
            n.addElement("body").addCDATA(new String(e.getBody().getBytes(), Charset.forName("UTF-8")));
            n.addAttribute("aNum", getString(e.getANum()));
            n.addAttribute("url", e.getUrl().toString());
            Element metaNode = n.addElement("metadata");
            EventMetadata meta = e.getMetadata();
            metaNode.addAttribute("revisionTime", getString(meta.getRevisionTime()));
            metaNode.addAttribute("revisionNumber", getString(meta.getRevisionNumber()));
            metaNode.addAttribute("music", meta.getMusic());
            metaNode.addAttribute("preformattedOption", getString(meta.getPreformattedOption()));
            metaNode.addAttribute("location", meta.getLocation());
            metaNode.addAttribute("moodId", getString(meta.getMoodId()));
            String tags = meta.getTags().toString();
            if (tags.length() > 2) {
                metaNode.addAttribute("tags", tags.substring(1, tags.length() - 1));
            }
            Element commentsNode = n.addElement("comments");
            for (Comment c : e.getComments()) {
                Element cNode = commentsNode.addElement("comment");
                cNode.addAttribute("id", c.getId());
                cNode.addAttribute("eventId", getString(c.getEventId()));
                cNode.addAttribute("subject", getString(c.getSubject()));
                cNode.addAttribute("date", getString(c.getDate()));
                cNode.addAttribute("user", getString(c.getUser()));
                cNode.addAttribute("posterId", getString(c.getPosterId()));
                cNode.addAttribute("state", getString(c.getState()));
                cNode.addAttribute("parentId", getString(c.getParentId()));
                if (c.getBody() != null) cNode.addElement("body").addCDATA(new String(c.getBody().getBytes(), Charset.forName("UTF-8")));
            }
        }
        Util.save(root, outputPath);
        sendMessageToListeners("All entries saved to file [" + outputPath.getAbsolutePath() + "]\n");
    }

    private String getString(String s) {
        if (s == null) return "";
        return new String(s.getBytes(), Charset.forName("UTF-8"));
    }

    private String getString(Integer i) {
        if (i == null) return "";
        return i.toString();
    }

    private String getString(Boolean b) {
        if (b == null) return "";
        return b.toString();
    }

    private String getString(Date date) {
        if (date == null) return "";
        return sdf.format(date);
    }
}
