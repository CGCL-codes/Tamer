package org.gtdfree.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.gtdfree.ApplicationHelper;
import org.gtdfree.model.Action.ActionType;
import org.gtdfree.model.Folder.FolderType;

/**
 * @author ikesan
 *
 */
public final class GTDDataXMLTools {

    public static class DataHeader {

        public DataHeader(File file, String ver, String mod) {
            this.file = file;
            version = ver;
            if (mod != null) {
                try {
                    modified = ApplicationHelper.parseLongISO(mod);
                } catch (ParseException e) {
                    Logger.getLogger(this.getClass()).error("Parse error.", e);
                }
            }
        }

        public DataHeader(File f) throws FileNotFoundException, XMLStreamException, javax.xml.stream.FactoryConfigurationError {
            file = f;
            InputStream in = null;
            XMLStreamReader r = null;
            try {
                in = new BufferedInputStream(new FileInputStream(f));
                r = XMLInputFactory.newInstance().createXMLStreamReader(in);
                r.nextTag();
                if ("gtd-data".equals(r.getLocalName())) {
                    version = r.getAttributeValue(null, "version");
                    try {
                        modified = ApplicationHelper.parseLongISO(r.getAttributeValue(null, "modified"));
                    } catch (ParseException e) {
                        Logger.getLogger(this.getClass()).error("Parse error.", e);
                    }
                    if (modified == null) {
                        modified = new Date(f.lastModified());
                    }
                }
            } finally {
                if (r != null) {
                    try {
                        r.close();
                    } catch (Exception e) {
                        Logger.getLogger(this.getClass()).debug("I/O error.", e);
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass()).debug("I/O error.", e);
                        }
                    }
                }
            }
        }

        private File file;

        String version;

        private Date modified;

        /**
		 * @return the version
		 */
        public String getVersion() {
            return version;
        }

        /**
		 * @return the modified
		 */
        public Date getModified() {
            return modified;
        }

        public File getFile() {
            return file;
        }

        @Override
        public String toString() {
            return file.toString() + " " + ApplicationHelper.toISODateTimeString(modified) + " " + version;
        }
    }

    private static final String EOL = "\n";

    private static final String SKIP = "  ";

    private static final String SKIPSKIP = "    ";

    private static void _load_1_0(GTDModel model, XMLStreamReader r) throws XMLStreamException {
        if (checkTagStart(r, "folders")) {
            r.nextTag();
            while (checkTagStart(r, "folder")) {
                String type = r.getAttributeValue(null, "type").trim();
                Folder ff = null;
                if ("NOTE".equals(type)) {
                    ff = model.getInBucketFolder();
                } else {
                    ff = model.createFolder(r.getAttributeValue(null, "name"), FolderType.valueOf(type));
                }
                r.nextTag();
                while (checkTagStart(r, "action")) {
                    int i = Integer.parseInt(r.getAttributeValue(null, "id"));
                    Date cr = new Date(Long.parseLong(r.getAttributeValue(null, "created")));
                    Date re = r.getAttributeValue(null, "resolved") == null ? null : new Date(Long.parseLong(r.getAttributeValue(null, "resolved")));
                    String d = r.getAttributeValue(null, "description");
                    if (d != null) {
                        d = d.replace("\\n", "\n");
                    }
                    Action a = new Action(i, cr, re, d);
                    a.setResolution(Action.Resolution.toResolution(r.getAttributeValue(null, "resolution")));
                    String s = r.getAttributeValue(null, "start");
                    if (s != null) a.setStart(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "remind");
                    if (s != null) a.setRemind(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "due");
                    if (s != null) a.setDue(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "type");
                    if (s != null) a.setType(ActionType.valueOf(s));
                    s = r.getAttributeValue(null, "url");
                    if (s != null) {
                        try {
                            a.setUrl(new URL(s));
                        } catch (Exception e) {
                            Logger.getLogger(GTDDataXMLTools.class).debug("Internal error.", e);
                        }
                    }
                    ff.add(a);
                    if (a.getId() > model.getLastActionID()) {
                        model.setLastActionID(a.getId());
                    }
                    findTagEnd(r, "action");
                    r.nextTag();
                }
                findTagEnd(r, "folder");
                r.nextTag();
            }
        }
    }

    private static void _load_2_0(GTDModel model, XMLStreamReader r) throws XMLStreamException {
        HashMap<Integer, Action> withProject = new HashMap<Integer, Action>();
        HashMap<Integer, Action> queued = new HashMap<Integer, Action>();
        if (checkTagStart(r, "folders")) {
            r.nextTag();
            while (checkTagStart(r, "folder")) {
                Folder ff;
                String id = r.getAttributeValue(null, "id");
                if (id != null) {
                    ff = model.createFolder(Integer.parseInt(id), r.getAttributeValue(null, "name"), FolderType.valueOf(r.getAttributeValue(null, "type")));
                } else {
                    String s = r.getAttributeValue(null, "type").replace("NOTE", "INBUCKET");
                    ff = model.createFolder(r.getAttributeValue(null, "name"), FolderType.valueOf(s));
                }
                String s = r.getAttributeValue(null, "closed");
                if (s != null) ff.setClosed(Boolean.parseBoolean(s));
                s = r.getAttributeValue(null, "description");
                if (s != null) {
                    s = s.replace("\\n", "\n");
                }
                if (!ff.isInBucket()) {
                    ff.setDescription(s);
                }
                r.nextTag();
                while (checkTagStart(r, "action")) {
                    int i = Integer.parseInt(r.getAttributeValue(null, "id"));
                    Date cr = new Date(Long.parseLong(r.getAttributeValue(null, "created")));
                    Date re = r.getAttributeValue(null, "resolved") == null ? null : new Date(Long.parseLong(r.getAttributeValue(null, "resolved")));
                    String d = r.getAttributeValue(null, "description");
                    if (d != null) {
                        d = d.replace("\\n", "\n");
                    }
                    Action a = new Action(i, cr, re, d);
                    s = r.getAttributeValue(null, "type");
                    if (s != null) a.setType(ActionType.valueOf(s));
                    s = r.getAttributeValue(null, "url");
                    if (s != null) {
                        try {
                            a.setUrl(new URL(s));
                        } catch (Exception e) {
                            Logger.getLogger(GTDDataXMLTools.class).debug("Internal error.", e);
                        }
                    }
                    s = r.getAttributeValue(null, "start");
                    if (s != null) a.setStart(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "remind");
                    if (s != null) a.setRemind(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "due");
                    if (s != null) a.setDue(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "queued");
                    if (s != null) a.setQueued(Boolean.parseBoolean(s));
                    s = r.getAttributeValue(null, "project");
                    if (s != null) a.setProject(Integer.parseInt(s));
                    s = r.getAttributeValue(null, "priority");
                    if (s != null) a.setPriority(Priority.valueOf(s));
                    a.setResolution(Action.Resolution.toResolution(r.getAttributeValue(null, "resolution")));
                    ff.add(a);
                    if (a.getProject() != null) {
                        withProject.put(a.getId(), a);
                    }
                    if (a.isQueued()) {
                        queued.put(a.getId(), a);
                    }
                    if (a.getId() > model.getLastActionID()) {
                        model.setLastActionID(a.getId());
                    }
                    findTagEnd(r, "action");
                    r.nextTag();
                }
                findTagEnd(r, "folder");
                r.nextTag();
            }
            findTagEnd(r, "folders");
        }
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        r.nextTag();
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        if (checkTagStart(r, "projects")) {
            r.nextTag();
            while (checkTagStart(r, "project")) {
                Project pp;
                String id = r.getAttributeValue(null, "id");
                if (id != null) {
                    pp = (Project) model.createFolder(Integer.parseInt(id), r.getAttributeValue(null, "name"), FolderType.PROJECT);
                } else {
                    pp = (Project) model.createFolder(r.getAttributeValue(null, "name"), FolderType.PROJECT);
                }
                pp.setClosed(Boolean.parseBoolean(r.getAttributeValue(null, "closed")));
                pp.setGoal(r.getAttributeValue(null, "goal"));
                String s = r.getAttributeValue(null, "actions");
                if (s != null && s.trim().length() > 0) {
                    String[] ss = s.trim().split(",");
                    for (int i = 0; i < ss.length; i++) {
                        if (ss[i].trim().length() > 0) {
                            int ii = Integer.parseInt(ss[i].trim());
                            Action a = withProject.remove(ii);
                            if (a != null) {
                                pp.add(a);
                            }
                        }
                    }
                }
                r.nextTag();
                findTagEnd(r, "project");
                r.nextTag();
            }
            findTagEnd(r, "projects");
        }
        for (Action a : withProject.values()) {
            if (a.getProject() != null) {
                Project p = model.getProject(a.getProject());
                if (p != null) {
                    p.add(a);
                } else {
                    System.err.println("Project " + p + " in action " + a + " does not exsist.");
                    a.setProject(null);
                }
            }
        }
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        r.nextTag();
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        if (checkTagStart(r, "queue")) {
            Folder f = model.getQueue();
            String s = r.getAttributeValue(null, "actions");
            if (s != null && s.trim().length() > 0) {
                String[] ss = s.trim().split(",");
                for (int i = 0; i < ss.length; i++) {
                    if (ss[i].trim().length() > 0) {
                        int ii = Integer.parseInt(ss[i].trim());
                        Action a = queued.remove(ii);
                        if (a != null) {
                            f.add(a);
                        }
                    }
                }
            }
            r.nextTag();
            findTagEnd(r, "queue");
            r.nextTag();
        }
        for (Action a : queued.values()) {
            if (a.isQueued()) {
                System.err.println("Action " + a + " is queued but not in queue list.");
                model.getQueue().add(a);
            }
        }
    }

    private static void _load_2_1(GTDModel model, XMLStreamReader r) throws XMLStreamException {
        HashMap<Integer, Action> withProject = new HashMap<Integer, Action>();
        HashMap<Integer, Action> queued = new HashMap<Integer, Action>();
        if (checkTagStart(r, "lists")) {
            r.nextTag();
            while (checkTagStart(r, "list")) {
                Folder ff;
                String id = r.getAttributeValue(null, "id");
                if (id != null) {
                    ff = model.createFolder(Integer.parseInt(id), r.getAttributeValue(null, "name"), FolderType.valueOf(r.getAttributeValue(null, "type")));
                } else {
                    String s = r.getAttributeValue(null, "type").replace("NOTE", "INBUCKET");
                    ff = model.createFolder(r.getAttributeValue(null, "name"), FolderType.valueOf(s));
                }
                String s = r.getAttributeValue(null, "closed");
                if (s != null) ff.setClosed(Boolean.parseBoolean(s));
                s = r.getAttributeValue(null, "description");
                if (s != null) {
                    s = s.replace("\\n", "\n");
                }
                if (!ff.isInBucket()) {
                    ff.setDescription(s);
                }
                r.nextTag();
                while (checkTagStart(r, "action")) {
                    int i = Integer.parseInt(r.getAttributeValue(null, "id"));
                    Date cr = new Date(Long.parseLong(r.getAttributeValue(null, "created")));
                    Date re = r.getAttributeValue(null, "resolved") == null ? null : new Date(Long.parseLong(r.getAttributeValue(null, "resolved")));
                    String d = r.getAttributeValue(null, "description");
                    if (d != null) {
                        d = d.replace("\\n", "\n");
                    }
                    Action a = new Action(i, cr, re, d);
                    s = r.getAttributeValue(null, "type");
                    if (s != null) a.setType(ActionType.valueOf(s));
                    s = r.getAttributeValue(null, "url");
                    if (s != null) {
                        try {
                            a.setUrl(new URL(s));
                        } catch (Exception e) {
                            Logger.getLogger(GTDDataXMLTools.class).debug("Internal error.", e);
                        }
                    }
                    s = r.getAttributeValue(null, "start");
                    if (s != null) a.setStart(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "remind");
                    if (s != null) a.setRemind(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "due");
                    if (s != null) a.setDue(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "queued");
                    if (s != null) a.setQueued(Boolean.parseBoolean(s));
                    s = r.getAttributeValue(null, "project");
                    if (s != null) a.setProject(Integer.parseInt(s));
                    s = r.getAttributeValue(null, "priority");
                    if (s != null) a.setPriority(Priority.valueOf(s));
                    a.setResolution(Action.Resolution.toResolution(r.getAttributeValue(null, "resolution")));
                    ff.add(a);
                    if (a.getProject() != null) {
                        withProject.put(a.getId(), a);
                    }
                    if (a.isQueued()) {
                        queued.put(a.getId(), a);
                    }
                    if (a.getId() > model.getLastActionID()) {
                        model.setLastActionID(a.getId());
                    }
                    findTagEnd(r, "action");
                    r.nextTag();
                }
                findTagEnd(r, "list");
                r.nextTag();
            }
            findTagEnd(r, "lists");
        }
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        r.nextTag();
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        if (checkTagStart(r, "projects")) {
            r.nextTag();
            while (checkTagStart(r, "project")) {
                Project pp;
                String id = r.getAttributeValue(null, "id");
                if (id != null) {
                    pp = (Project) model.createFolder(Integer.parseInt(id), r.getAttributeValue(null, "name"), FolderType.PROJECT);
                } else {
                    pp = (Project) model.createFolder(r.getAttributeValue(null, "name"), FolderType.PROJECT);
                }
                pp.setClosed(Boolean.parseBoolean(r.getAttributeValue(null, "closed")));
                pp.setGoal(r.getAttributeValue(null, "goal"));
                String s = r.getAttributeValue(null, "actions");
                if (s != null && s.trim().length() > 0) {
                    String[] ss = s.trim().split(",");
                    for (int i = 0; i < ss.length; i++) {
                        if (ss[i].trim().length() > 0) {
                            int ii = Integer.parseInt(ss[i].trim());
                            Action a = withProject.remove(ii);
                            if (a != null) {
                                pp.add(a);
                            }
                        }
                    }
                }
                r.nextTag();
                findTagEnd(r, "project");
                r.nextTag();
            }
            findTagEnd(r, "projects");
        }
        for (Action a : withProject.values()) {
            if (a.getProject() != null) {
                Project p = model.getProject(a.getProject());
                if (p != null) {
                    p.add(a);
                } else {
                    System.err.println("Project " + p + " in action " + a + " does not exsist.");
                    a.setProject(null);
                }
            }
        }
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        r.nextTag();
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        if (checkTagStart(r, "queue")) {
            Folder f = model.getQueue();
            String s = r.getAttributeValue(null, "actions");
            if (s != null && s.trim().length() > 0) {
                String[] ss = s.trim().split(",");
                for (int i = 0; i < ss.length; i++) {
                    if (ss[i].trim().length() > 0) {
                        int ii = Integer.parseInt(ss[i].trim());
                        Action a = queued.remove(ii);
                        if (a != null) {
                            f.add(a);
                        }
                    }
                }
            }
            r.nextTag();
            findTagEnd(r, "queue");
            r.nextTag();
        }
        for (Action a : queued.values()) {
            if (a.isQueued()) {
                System.err.println("Action " + a + " is queued but not in queue list.");
                model.getQueue().add(a);
            }
        }
    }

    private static void _load_2_2(GTDModel model, XMLStreamReader r) throws XMLStreamException {
        HashMap<Integer, Action> withProject = new HashMap<Integer, Action>();
        HashMap<Integer, Action> queued = new HashMap<Integer, Action>();
        if (checkTagStart(r, "lists")) {
            r.nextTag();
            while (checkTagStart(r, "list")) {
                Folder ff;
                String id = r.getAttributeValue(null, "id");
                if (id != null) {
                    ff = model.createFolder(Integer.parseInt(id), r.getAttributeValue(null, "name"), FolderType.valueOf(r.getAttributeValue(null, "type")));
                } else {
                    String s = r.getAttributeValue(null, "type").replace("NOTE", "INBUCKET");
                    ff = model.createFolder(r.getAttributeValue(null, "name"), FolderType.valueOf(s));
                }
                String s = r.getAttributeValue(null, "closed");
                if (s != null) ff.setClosed(Boolean.parseBoolean(s));
                s = StringEscapeUtils.unescapeJava(r.getAttributeValue(null, "description"));
                if (!ff.isInBucket()) {
                    ff.setDescription(s);
                }
                Date cr = null, mo = null, re = null;
                s = r.getAttributeValue(null, "created");
                if (s != null) {
                    cr = new Date(Long.parseLong(s));
                }
                s = r.getAttributeValue(null, "modified");
                if (s != null) {
                    mo = new Date(Long.parseLong(s));
                }
                s = r.getAttributeValue(null, "resolved");
                if (s != null) {
                    re = new Date(Long.parseLong(s));
                }
                ff.setDates(cr, mo, re);
                r.nextTag();
                while (checkTagStart(r, "action")) {
                    int i = Integer.parseInt(r.getAttributeValue(null, "id"));
                    cr = new Date(Long.parseLong(r.getAttributeValue(null, "created")));
                    re = r.getAttributeValue(null, "resolved") == null ? null : new Date(Long.parseLong(r.getAttributeValue(null, "resolved")));
                    mo = r.getAttributeValue(null, "modified") == null ? null : new Date(Long.parseLong(r.getAttributeValue(null, "modified")));
                    String d = StringEscapeUtils.unescapeJava(r.getAttributeValue(null, "description"));
                    Action a = new Action(i, cr, re, d, mo);
                    s = r.getAttributeValue(null, "type");
                    if (s != null) a.setType(ActionType.valueOf(s));
                    s = r.getAttributeValue(null, "url");
                    if (s != null) {
                        try {
                            a.setUrl(new URL(s));
                        } catch (Exception e) {
                            Logger.getLogger(GTDDataXMLTools.class).debug("Internal error.", e);
                        }
                    }
                    s = r.getAttributeValue(null, "start");
                    if (s != null) a.setStart(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "remind");
                    if (s != null) a.setRemind(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "due");
                    if (s != null) a.setDue(new Date(Long.parseLong(s)));
                    s = r.getAttributeValue(null, "queued");
                    if (s != null) a.setQueued(Boolean.parseBoolean(s));
                    s = r.getAttributeValue(null, "project");
                    if (s != null) a.setProject(Integer.parseInt(s));
                    s = r.getAttributeValue(null, "priority");
                    if (s != null) a.setPriority(Priority.valueOf(s));
                    a.setResolution(Action.Resolution.toResolution(r.getAttributeValue(null, "resolution")));
                    ff.add(a);
                    if (a.getProject() != null) {
                        withProject.put(a.getId(), a);
                    }
                    if (a.isQueued()) {
                        queued.put(a.getId(), a);
                    }
                    if (a.getId() > model.getLastActionID()) {
                        model.setLastActionID(a.getId());
                    }
                    findTagEnd(r, "action");
                    r.nextTag();
                }
                findTagEnd(r, "list");
                r.nextTag();
            }
            findTagEnd(r, "lists");
        }
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        r.nextTag();
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        if (checkTagStart(r, "projects")) {
            r.nextTag();
            while (checkTagStart(r, "project")) {
                Project pp;
                String id = r.getAttributeValue(null, "id");
                if (id != null) {
                    pp = (Project) model.createFolder(Integer.parseInt(id), r.getAttributeValue(null, "name"), FolderType.PROJECT);
                } else {
                    pp = (Project) model.createFolder(r.getAttributeValue(null, "name"), FolderType.PROJECT);
                }
                pp.setClosed(Boolean.parseBoolean(r.getAttributeValue(null, "closed")));
                pp.setGoal(r.getAttributeValue(null, "goal"));
                String s = StringEscapeUtils.unescapeJava(r.getAttributeValue(null, "description"));
                if (s != null) {
                    pp.setDescription(s);
                }
                Date cr = null, mo = null, re = null;
                s = r.getAttributeValue(null, "created");
                if (s != null) {
                    cr = new Date(Long.parseLong(s));
                }
                s = r.getAttributeValue(null, "modified");
                if (s != null) {
                    mo = new Date(Long.parseLong(s));
                }
                s = r.getAttributeValue(null, "resolved");
                if (s != null) {
                    re = new Date(Long.parseLong(s));
                }
                pp.setDates(cr, mo, re);
                s = r.getAttributeValue(null, "actions");
                if (s != null && s.trim().length() > 0) {
                    String[] ss = s.trim().split(",");
                    for (int i = 0; i < ss.length; i++) {
                        if (ss[i].trim().length() > 0) {
                            int ii = Integer.parseInt(ss[i].trim());
                            Action a = withProject.remove(ii);
                            if (a != null) {
                                pp.add(a);
                            }
                        }
                    }
                }
                r.nextTag();
                findTagEnd(r, "project");
                r.nextTag();
            }
            findTagEnd(r, "projects");
        }
        for (Action a : withProject.values()) {
            if (a.getProject() != null) {
                Project p = model.getProject(a.getProject());
                if (p != null) {
                    p.add(a);
                } else {
                    System.err.println("Project " + p + " in action " + a + " does not exsist.");
                    a.setProject(null);
                }
            }
        }
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        r.nextTag();
        if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return;
        }
        if (checkTagStart(r, "queue")) {
            Folder f = model.getQueue();
            String s = r.getAttributeValue(null, "actions");
            if (s != null && s.trim().length() > 0) {
                String[] ss = s.trim().split(",");
                for (int i = 0; i < ss.length; i++) {
                    if (ss[i].trim().length() > 0) {
                        int ii = Integer.parseInt(ss[i].trim());
                        Action a = queued.remove(ii);
                        if (a != null) {
                            f.add(a);
                        }
                    }
                }
            }
            r.nextTag();
            findTagEnd(r, "queue");
            r.nextTag();
        }
        for (Action a : queued.values()) {
            if (a.isQueued()) {
                System.err.println("Action " + a + " is queued but not in queue list.");
                model.getQueue().add(a);
            }
        }
    }

    private static boolean checkTagStart(XMLStreamReader r, String tag) throws XMLStreamException {
        return tag.equals(r.getLocalName()) && r.getEventType() == XMLStreamReader.START_ELEMENT;
    }

    private static void findTagEnd(XMLStreamReader r, String tag) throws XMLStreamException {
        while (!r.getLocalName().equals(tag) || XMLStreamReader.END_ELEMENT != r.getEventType()) {
            if (r.getEventType() == XMLStreamReader.END_DOCUMENT) {
                return;
            }
            r.nextTag();
        }
    }

    public static void importFile(GTDModel model, InputStream in) throws XMLStreamException, FactoryConfigurationError, IOException {
        GTDModel m = new GTDModel(null);
        load(m, in);
        model.importData(m);
    }

    public static void importFile(GTDModel model, File file) throws XMLStreamException, FactoryConfigurationError, IOException {
        InputStream r = new FileInputStream(file);
        try {
            importFile(model, r);
        } finally {
            try {
                r.close();
            } catch (IOException e) {
                Logger.getLogger(GTDDataXMLTools.class).debug("I/O error.", e);
            }
        }
    }

    public static void load(GTDModel model, File f) throws XMLStreamException, IOException {
        InputStream r = new FileInputStream(f);
        try {
            load(model, r);
        } finally {
            try {
                r.close();
            } catch (IOException e) {
                Logger.getLogger(GTDDataXMLTools.class).debug("I/O error.", e);
            }
        }
    }

    public static DataHeader load(GTDModel model, InputStream in) throws XMLStreamException, IOException {
        model.setSuspendedForMultipleChanges(true);
        model.getDataRepository().suspend(true);
        XMLStreamReader r;
        try {
            BufferedInputStream bin = new BufferedInputStream(in, 8192);
            bin.mark(8191);
            Reader rr = new InputStreamReader(bin);
            CharBuffer b = CharBuffer.allocate(96);
            rr.read(b);
            b.position(0);
            Pattern pattern = Pattern.compile("<\\?.*?encoding\\s*?=.*?\\?>", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(b);
            bin.reset();
            if (matcher.find()) {
                r = XMLInputFactory.newInstance().createXMLStreamReader(bin);
                Logger.getLogger(GTDDataXMLTools.class).info("XML declared encoding: " + r.getEncoding() + ", system default encoding: " + Charset.defaultCharset());
            } else {
                r = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(bin));
                Logger.getLogger(GTDDataXMLTools.class).info("XML assumed system default encoding: " + Charset.defaultCharset());
            }
            r.nextTag();
            if ("gtd-data".equals(r.getLocalName())) {
                DataHeader dh = new DataHeader(null, r.getAttributeValue(null, "version"), r.getAttributeValue(null, "modified"));
                if (dh.version != null) {
                    if (dh.version.equals("2.0")) {
                        r.nextTag();
                        _load_2_0(model, r);
                        return dh;
                    }
                }
                String s = r.getAttributeValue(null, "lastActionID");
                if (s != null) {
                    try {
                        model.setLastActionID(Integer.parseInt(s));
                    } catch (Exception e) {
                        Logger.getLogger(GTDDataXMLTools.class).debug("Internal error.", e);
                    }
                }
                if (dh.version != null) {
                    if (dh.version.equals("2.1")) {
                        r.nextTag();
                        _load_2_1(model, r);
                        return dh;
                    }
                    if (dh.version.startsWith("2.2")) {
                        r.nextTag();
                        _load_2_2(model, r);
                        return dh;
                    }
                }
                throw new IOException("XML gtd-free data with version number " + dh.version + " can not be imported. Data version is newer then supported versions. Update your GTD-Free application to latest version.");
            }
            _load_1_0(model, r);
            return null;
        } catch (XMLStreamException e) {
            if (e.getNestedException() != null) {
                Logger.getLogger(GTDDataXMLTools.class).debug("Parse error.", e.getNestedException());
            } else {
                Logger.getLogger(GTDDataXMLTools.class).debug("Parse error.", e);
            }
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            model.setSuspendedForMultipleChanges(false);
            model.getDataRepository().suspend(false);
        }
    }

    public static void store(GTDModel model, File f, ActionFilter filter) throws IOException, XMLStreamException, FactoryConfigurationError {
        BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(f));
        store(model, bw, filter);
        bw.close();
    }

    public static void store(GTDModel model, File f) throws IOException, XMLStreamException, FactoryConfigurationError {
        store(model, f, new DummyFilter(true));
    }

    public static void store(GTDModel model, OutputStream out, ActionFilter filter) throws IOException, XMLStreamException, FactoryConfigurationError {
        if (filter == null) {
            filter = new DummyFilter(true);
        }
        XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter(out, "UTF-8");
        w.writeStartDocument("UTF-8", "1.0");
        w.writeCharacters(EOL);
        w.writeCharacters(EOL);
        w.writeStartElement("gtd-data");
        w.writeAttribute("version", "2.2");
        w.writeAttribute("modified", ApplicationHelper.formatLongISO(new Date()));
        w.writeAttribute("lastActionID", Integer.toString(model.getLastActionID()));
        w.writeCharacters(EOL);
        w.writeCharacters(EOL);
        Folder[] fn = model.toFoldersArray();
        w.writeStartElement("lists");
        w.writeCharacters(EOL);
        for (int i = 0; i < fn.length; i++) {
            Folder ff = fn[i];
            if (ff.isMeta() || !filter.isAcceptable(ff, null)) {
                continue;
            }
            w.writeCharacters(SKIP);
            w.writeStartElement("list");
            w.writeAttribute("id", String.valueOf(ff.getId()));
            w.writeAttribute("name", ff.getName());
            w.writeAttribute("type", ff.getType().toString());
            w.writeAttribute("closed", Boolean.toString(ff.isClosed()));
            if (ff.getCreated() != null) w.writeAttribute("created", Long.toString(ff.getCreated().getTime()));
            if (ff.getModified() != null) w.writeAttribute("modified", Long.toString(ff.getModified().getTime()));
            if (ff.getResolved() != null) w.writeAttribute("resolved", Long.toString(ff.getResolved().getTime()));
            if (!ff.isInBucket() && ff.getDescription() != null) {
                w.writeAttribute("description", ApplicationHelper.escapeControls(ff.getDescription()));
            }
            w.writeCharacters(EOL);
            for (Action a : ff) {
                if (!filter.isAcceptable(ff, a)) {
                    continue;
                }
                w.writeCharacters(SKIPSKIP);
                w.writeStartElement("action");
                w.writeAttribute("id", Integer.toString(a.getId()));
                w.writeAttribute("created", Long.toString(a.getCreated().getTime()));
                w.writeAttribute("resolution", a.getResolution().toString());
                if (a.getResolved() != null) w.writeAttribute("resolved", Long.toString(a.getResolved().getTime()));
                if (a.getModified() != null) w.writeAttribute("modified", Long.toString(a.getModified().getTime()));
                if (a.getDescription() != null) w.writeAttribute("description", ApplicationHelper.escapeControls(a.getDescription()));
                if (a.getStart() != null) w.writeAttribute("start", Long.toString(a.getStart().getTime()));
                if (a.getRemind() != null) w.writeAttribute("remind", Long.toString(a.getRemind().getTime()));
                if (a.getDue() != null) w.writeAttribute("due", Long.toString(a.getDue().getTime()));
                if (a.getType() != null) w.writeAttribute("type", a.getType().toString());
                if (a.getUrl() != null) w.writeAttribute("url", a.getUrl().toString());
                if (a.isQueued()) w.writeAttribute("queued", Boolean.toString(a.isQueued()));
                if (a.getProject() != null) w.writeAttribute("project", a.getProject().toString());
                if (a.getPriority() != null) w.writeAttribute("priority", a.getPriority().toString());
                w.writeEndElement();
                w.writeCharacters(EOL);
            }
            w.writeCharacters(SKIP);
            w.writeEndElement();
            w.writeCharacters(EOL);
        }
        w.writeEndElement();
        w.writeCharacters(EOL);
        Project[] pn = model.toProjectsArray();
        w.writeStartElement("projects");
        w.writeCharacters(EOL);
        for (int i = 0; i < pn.length; i++) {
            Project ff = pn[i];
            if (!filter.isAcceptable(ff, null)) {
                continue;
            }
            w.writeCharacters(SKIP);
            w.writeStartElement("project");
            w.writeAttribute("id", String.valueOf(ff.getId()));
            w.writeAttribute("name", ff.getName());
            w.writeAttribute("closed", String.valueOf(ff.isClosed()));
            if (ff.getCreated() != null) w.writeAttribute("created", Long.toString(ff.getCreated().getTime()));
            if (ff.getModified() != null) w.writeAttribute("modified", Long.toString(ff.getModified().getTime()));
            if (ff.getResolved() != null) w.writeAttribute("resolved", Long.toString(ff.getResolved().getTime()));
            if (ff.getDescription() != null) {
                w.writeAttribute("description", ApplicationHelper.escapeControls(ff.getDescription()));
            }
            StringBuilder sb = new StringBuilder();
            for (Action a : ff) {
                if (!filter.isAcceptable(ff, a)) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(a.getId());
            }
            w.writeAttribute("actions", sb.toString());
            w.writeEndElement();
            w.writeCharacters(EOL);
        }
        w.writeEndElement();
        w.writeCharacters(EOL);
        Folder f = model.getQueue();
        if (filter.isAcceptable(f, null)) {
            w.writeStartElement("queue");
            w.writeAttribute("id", String.valueOf(f.getId()));
            w.writeAttribute("name", f.getName());
            StringBuilder sb = new StringBuilder();
            Iterator<Action> i = f.iterator();
            if (i.hasNext()) {
                sb.append(i.next().getId());
            }
            while (i.hasNext()) {
                sb.append(",");
                sb.append(i.next().getId());
            }
            w.writeAttribute("actions", sb.toString());
            w.writeEndElement();
            w.writeCharacters(EOL);
        }
        w.writeEndElement();
        w.writeEndDocument();
        w.flush();
        w.close();
    }
}
