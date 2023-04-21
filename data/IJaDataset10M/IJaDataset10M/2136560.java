package log4j.ui;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import prisms.arch.PrismsException;
import prisms.arch.PrismsSession;
import prisms.logging.LogEntry;
import prisms.logging.PrismsLogger.LogField;
import prisms.util.ArrayUtils;
import prisms.util.Search;

/** Sends log entries to the client for display to the user */
public class LogViewer implements prisms.arch.DownloadPlugin {

    static final Logger log = Logger.getLogger(LogViewer.class);

    prisms.util.preferences.Preference<Integer> PAGE_PREF;

    private PrismsSession theSession;

    private String theName;

    private prisms.util.SearchableAPI.PreparedSearch<LogField> theCheckSearch;

    private boolean isRunning;

    private long theLastCheckTime;

    private int theStart;

    int thePageSize;

    prisms.util.IntList theSnapshot;

    prisms.util.IntList theSelected;

    prisms.ui.UI.DefaultProgressInformer thePI;

    public void initPlugin(PrismsSession session, prisms.arch.PrismsConfig config) {
        theSession = session;
        theName = config.get("name");
        PAGE_PREF = new prisms.util.preferences.Preference<Integer>(theName, "Entries Per Page", prisms.util.preferences.Preference.Type.NONEG_INT, Integer.class, true);
        Integer val = theSession.getPreferences().get(PAGE_PREF);
        if (val == null) {
            thePageSize = 250;
            theSession.getPreferences().set(PAGE_PREF, Integer.valueOf(thePageSize));
        } else thePageSize = val.intValue();
        session.addEventListener("preferencesChanged", new prisms.arch.event.PrismsEventListener() {

            public void eventOccurred(PrismsSession session2, prisms.arch.event.PrismsEvent evt) {
                if (!(evt instanceof prisms.util.preferences.PreferenceEvent)) return;
                prisms.util.preferences.PreferenceEvent pEvt = (prisms.util.preferences.PreferenceEvent) evt;
                if (!pEvt.getPreference().equals(PAGE_PREF)) return;
                int ps = ((Integer) pEvt.getNewValue()).intValue();
                if (ps == 0) {
                    getSession().runEventually(new Runnable() {

                        public void run() {
                            getSession().getPreferences().set(PAGE_PREF, Integer.valueOf(1));
                        }
                    });
                } else {
                    thePageSize = ps;
                    resend();
                }
            }
        });
        session.addEventListener("prismsUserAuthChangd", new prisms.arch.event.PrismsEventListener() {

            public void eventOccurred(PrismsSession session2, prisms.arch.event.PrismsEvent evt) {
                if (getSession().getUser().equals(evt.getProperty("user"))) initClient();
            }
        });
        final Runnable checker = new Runnable() {

            public void run() {
                check();
            }
        };
        session.getApp().scheduleRecurringTask(checker, 1000);
        session.addEventListener("destroy", new prisms.arch.event.PrismsEventListener() {

            public void eventOccurred(PrismsSession session2, prisms.arch.event.PrismsEvent evt) {
                getSession().getApp().stopRecurringTask(checker);
            }
        });
        session.addPropertyChangeListener(log4j.app.Log4jProperties.search, new prisms.arch.event.PrismsPCL<Search>() {

            public void propertyChange(prisms.arch.event.PrismsPCE<Search> evt) {
                reset();
            }
        });
        theSelected = new prisms.util.IntList(true, true);
    }

    /** @return The session that is using this plugin */
    public PrismsSession getSession() {
        return theSession;
    }

    /** @return The name of this plugin */
    public String getName() {
        return theName;
    }

    public void initClient() {
        JSONObject evt = new JSONObject();
        evt.put("plugin", theName);
        evt.put("method", "setSelectable");
        evt.put("selectable", Boolean.valueOf(theSession.getPermissions().has("Edit Purge")));
        evt.put("exposed", theSession.getApp().getEnvironment().getLogger().getExposedDir());
        theSession.postOutgoingEvent(evt);
        resend();
    }

    public void processEvent(JSONObject evt) {
        if ("check".equals(evt.get("method"))) check(); else if ("checkBack".equals(evt.get("method"))) {
            if (isRunning) {
                JSONObject evt2 = new JSONObject();
                evt2.put("plugin", theName);
                evt2.put("method", "checkBack");
                theSession.postOutgoingEvent(evt2);
            }
        } else if ("save".equals(evt.get("method"))) doSave(evt); else if ("first".equals(evt.get("method"))) {
            if (theSnapshot == null) return;
            theStart = 0;
            resend();
        } else if ("previous".equals(evt.get("method"))) {
            if (theSnapshot == null) return;
            theStart -= thePageSize;
            if (theStart < 0) theStart = 0;
            resend();
        } else if ("next".equals(evt.get("method"))) {
            if (theSnapshot == null) return;
            theStart += thePageSize;
            while (theStart >= theSnapshot.size()) theStart -= thePageSize;
            resend();
        } else if ("last".equals(evt.get("method"))) {
            if (theSnapshot == null) return;
            theStart = (theSnapshot.size() / thePageSize) * thePageSize;
            resend();
        } else if ("setSelected".equals(evt.get("method"))) {
            if (!theSession.getPermissions().has("Edit Purge")) throw new IllegalArgumentException("You do not have permission to select entries for anything");
            int id = Integer.parseInt((String) evt.get("entry"), 16);
            if (Boolean.TRUE.equals(evt.get("selected"))) theSelected.add(id); else theSelected.removeValue(id);
        } else if ("selectAll".equals(evt.get("method"))) {
            if (!theSession.getPermissions().has("Edit Purge")) throw new IllegalArgumentException("You do not have permission to select entries for anything");
            if (theSnapshot == null) return;
            theSelected.clear();
            theSelected.addAll(theSnapshot, -1);
            resend();
        } else if ("purge".equals(evt.get("method"))) {
            if (!theSession.getPermissions().has("Edit Purge")) throw new IllegalArgumentException("You do not have permission to purge entries");
            if (theSelected.isEmpty()) {
                theSession.getUI().error("No entries selected");
                return;
            }
            final prisms.logging.PrismsLogger logger = theSession.getApp().getEnvironment().getLogger();
            prisms.ui.UI.DefaultProgressInformer pi = new prisms.ui.UI.DefaultProgressInformer();
            pi.setProgressText("Checking entries");
            theSession.getUI().startTimedTask(pi);
            String[] loggers = null;
            try {
                LogEntry[] entries = logger.getItems(theSelected.toLongArray());
                prisms.logging.AutoPurger purger = logger.getAutoPurger();
                for (LogEntry entry : entries) if (entry != null && ArrayUtils.contains(purger.getExcludeSearches(), entry.getLoggerName()) && !ArrayUtils.contains(loggers, entry.getLoggerName())) loggers = ArrayUtils.add(loggers, entry.getLoggerName());
            } catch (PrismsException e) {
                theSession.getUI().error("Could not check entries against auto-purge: " + e.getMessage());
                log.error("Could not fetch entries", e);
                return;
            } finally {
                pi.setDone();
            }
            if (loggers != null) {
                theSession.getUI().error("Entries by logger" + (loggers.length > 1 ? "s" : "") + " " + ArrayUtils.toString(loggers) + " cannot be purged");
                return;
            }
            theSession.getUI().confirm("Are you sure you want to purge " + (theSelected.size() == 1 ? "this" : "these " + theSelected.size()) + " entr" + (theSelected.size() == 1 ? "y" : "ies") + "? This cannot be undone.", new prisms.ui.UI.ConfirmListener() {

                public void confirmed(boolean confirm) {
                    if (!confirm) return;
                    try {
                        int purged = logger.purge(theSelected.toArray());
                        getSession().getUI().info(purged + " entr" + (purged == 1 ? "y" : "ies") + " purged");
                        if (theSnapshot != null) theSnapshot.removeAll(theSelected);
                        theSelected.clear();
                        resend();
                    } catch (PrismsException e) {
                        getSession().getUI().error("Could not purge entries: " + e.getMessage());
                        log.error("Could not purge entries", e);
                    }
                }
            });
        } else if ("protect".equals(evt.get("method"))) {
            if (!theSession.getPermissions().has("Edit Purge")) throw new IllegalArgumentException("You do not have permission to save entries");
            if (theSelected.isEmpty()) {
                theSession.getUI().error("No entries selected");
                return;
            }
            prisms.ui.UI.DefaultProgressInformer pi = new prisms.ui.UI.DefaultProgressInformer();
            pi.setProgressText("Checking entries");
            theSession.getUI().startTimedTask(pi);
            final prisms.logging.PrismsLogger logger = theSession.getApp().getEnvironment().getLogger();
            String[] loggers = null;
            try {
                LogEntry[] entries = logger.getItems(theSelected.toLongArray());
                prisms.logging.AutoPurger purger = logger.getAutoPurger();
                for (LogEntry entry : entries) if (ArrayUtils.contains(purger.getExcludeSearches(), entry.getLoggerName()) && !ArrayUtils.contains(loggers, entry.getLoggerName())) loggers = ArrayUtils.add(loggers, entry.getLoggerName());
            } catch (PrismsException e) {
                theSession.getUI().error("Could not check entries against auto-purge: " + e.getMessage());
                log.error("Could not fetch entries", e);
                return;
            } finally {
                pi.setDone();
            }
            if (loggers != null) {
                theSession.getUI().error("Entries by logger" + (loggers.length > 1 ? "s" : "") + " " + ArrayUtils.toString(loggers) + " cannot be purged. No need to save.");
                return;
            }
            theSession.getUI().confirm("Are you sure you want to protect " + (theSelected.size() == 1 ? "this" : "these " + theSelected.size()) + " entr" + (theSelected.size() == 1 ? "y" : "ies") + "? " + (theSelected.size() == 1 ? "It" : "They") + " will not be purged for at least 30 days.", new prisms.ui.UI.ConfirmListener() {

                public void confirmed(boolean confirm) {
                    if (!confirm) return;
                    try {
                        long time = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000;
                        logger.save(theSelected.toArray(), time);
                        getSession().getUI().info((theSelected.size() == 1 ? "This" : "These " + theSelected.size()) + " entr" + (theSelected.size() == 1 ? "y" : "ies") + " will not be purged for at least 30 days");
                        resend();
                    } catch (PrismsException e) {
                        getSession().getUI().error("Could not save entries: " + e.getMessage());
                        log.error("Could not save entries", e);
                    }
                }
            });
        } else if ("getFile".equals(evt.get("method"))) doRetrieve(evt); else if ("clear".equals(evt.get("method"))) clear(); else throw new IllegalArgumentException("Unrecognized " + theName + " event: " + evt);
    }

    void clear() {
        theSession.setProperty(log4j.app.Log4jProperties.search, null);
    }

    void reset() {
        theSelected.clear();
        isRunning = false;
        theCheckSearch = null;
        theStart = 0;
        JSONObject evt = new JSONObject();
        evt.put("plugin", theName);
        evt.put("method", "clear");
        theSession.postOutgoingEvent(evt);
        research();
    }

    /** Performs a new search */
    void research() {
        isRunning = false;
        Search search = theSession.getProperty(log4j.app.Log4jProperties.search);
        if (search == log4j.app.Log4jProperties.NO_SEARCH) return;
        if (!theSession.getPermissions().has("View All Logs")) {
            if (search == null) search = new prisms.logging.LogEntrySearch.LogUserSearch(theSession.getUser(), false); else search = new Search.ExpressionSearch(true).addOps(search, new prisms.logging.LogEntrySearch.LogUserSearch(theSession.getUser(), false));
        }
        prisms.logging.PrismsLogger logger = theSession.getApp().getEnvironment().getLogger();
        final boolean[] finished = new boolean[1];
        prisms.ui.UI.ProgressInformer pi = new prisms.ui.UI.ProgressInformer() {

            public int getTaskScale() {
                return 0;
            }

            public int getTaskProgress() {
                return 0;
            }

            public boolean isTaskDone() {
                return finished[0];
            }

            public String getTaskText() {
                return "Searching PRISMS logs";
            }

            public boolean isCancelable() {
                return false;
            }

            public void cancel() throws IllegalStateException {
            }
        };
        try {
            theSession.getUI().startTimedTask(pi);
            theLastCheckTime = System.currentTimeMillis();
            prisms.util.Sorter<prisms.logging.PrismsLogger.LogField> sorter = null;
            long[] ids;
            try {
                ids = logger.search(search, sorter);
            } catch (PrismsException e) {
                throw new IllegalStateException("Could not search logs: " + search, e);
            }
            int[] intIDs = new int[ids.length];
            for (int i = 0; i < ids.length; i++) intIDs[i] = (int) ids[i];
            theSnapshot = new prisms.util.IntList(intIDs);
            theSelected.and(theSnapshot);
            theStart = 0;
            resend();
            Search checkSearch;
            if (search == null) checkSearch = new prisms.logging.LogEntrySearch.LogTimeSearch(Search.Operator.GTE, null); else checkSearch = new Search.ExpressionSearch(true).addOps(search.clone(), new prisms.logging.LogEntrySearch.LogTimeSearch(Search.Operator.GTE, null));
            try {
                theCheckSearch = logger.prepare(checkSearch, sorter);
            } catch (PrismsException e) {
                log.error("Could not prepare search updater", e);
                theCheckSearch = null;
            }
        } finally {
            finished[0] = true;
        }
    }

    void resend() {
        JSONObject evt = new JSONObject();
        evt.put("plugin", theName);
        evt.put("method", "clear");
        theSession.postOutgoingEvent(evt);
        if (theSnapshot == null) return;
        prisms.logging.PrismsLogger logger = theSession.getApp().getEnvironment().getLogger();
        final long[] ids;
        if (theSnapshot.size() - theStart < thePageSize) ids = new long[theSnapshot.size() - theStart]; else ids = new long[thePageSize];
        theSnapshot.arrayCopy(theStart, ids, 0, ids.length);
        if (ids.length < 150 || thePageSize < 150) {
            LogEntry[] entries;
            try {
                entries = logger.getItems(ids);
            } catch (PrismsException e) {
                throw new IllegalStateException("Could not get log entries", e);
            }
            JSONArray jsonEntries = new JSONArray();
            for (LogEntry entry : entries) {
                JSONObject jsonEntry = toJson(entry);
                if (jsonEntry != null) jsonEntries.add(jsonEntry);
            }
            evt = new JSONObject();
            evt.put("plugin", theName);
            evt.put("method", "addEntries");
            evt.put("entries", jsonEntries);
            theSession.postOutgoingEvent(evt);
        } else {
            isRunning = true;
            Runnable getter = new Runnable() {

                public void run() {
                    sendEntriesProgressive(ids);
                }
            };
            theSession.getApp().getEnvironment().getWorker().run(getter, new prisms.arch.Worker.ErrorListener() {

                public void error(Error error) {
                    log.error("Getting entries failed", error);
                }

                public void runtime(RuntimeException ex) {
                    log.error("Getting entries failed", ex);
                }
            });
            evt = new JSONObject();
            evt.put("plugin", theName);
            evt.put("method", "checkBack");
            theSession.postOutgoingEvent(evt);
        }
        evt = new JSONObject();
        evt.put("plugin", theName);
        evt.put("method", "setCount");
        evt.put("count", Integer.valueOf(theSnapshot.size()));
        evt.put("start", Integer.valueOf(theStart));
        evt.put("page", Integer.valueOf(thePageSize));
        theSession.postOutgoingEvent(evt);
    }

    /** Checks for new log entries that match the current search */
    void check() {
        prisms.util.SearchableAPI.PreparedSearch<LogField> checkSearch = theCheckSearch;
        if (checkSearch == null) return;
        prisms.logging.PrismsLogger logger = theSession.getApp().getEnvironment().getLogger();
        long preCheck = System.currentTimeMillis();
        long lastCheck = theLastCheckTime;
        long[] ids;
        try {
            ids = logger.execute(theCheckSearch, Long.valueOf(lastCheck));
        } catch (PrismsException e) {
            throw new IllegalStateException("Could not search for updated logs: ", e);
        }
        if (ids.length == 0) return;
        theLastCheckTime = preCheck;
        int[] intIDs = new int[ids.length];
        for (int i = 0; i < ids.length; i++) intIDs[i] = (int) ids[i];
        synchronized (theSnapshot) {
            theSnapshot.addAll(intIDs, 0, intIDs.length, 0);
        }
        if (theStart > 0) {
            theStart += ids.length;
            JSONObject evt = new JSONObject();
            evt.put("plugin", theName);
            evt.put("method", "setCount");
            evt.put("count", Integer.valueOf(theSnapshot.size()));
            evt.put("start", Integer.valueOf(theStart));
            evt.put("page", Integer.valueOf(thePageSize));
            theSession.postOutgoingEvent(evt);
            return;
        }
        LogEntry[] entries;
        try {
            entries = logger.getItems(ids);
        } catch (PrismsException e) {
            throw new IllegalStateException("Could not get log entries", e);
        }
        if (theCheckSearch != checkSearch) return;
        JSONArray jsonEntries = new JSONArray();
        for (LogEntry entry : entries) {
            JSONObject jsonEntry = toJson(entry);
            if (jsonEntry != null) jsonEntries.add(jsonEntry);
        }
        JSONObject evt = new JSONObject();
        evt.put("plugin", theName);
        evt.put("method", "addNewEntries");
        evt.put("entries", jsonEntries);
        if (theCheckSearch != checkSearch) return;
        theSession.postOutgoingEvent(evt);
        evt = new JSONObject();
        evt.put("plugin", theName);
        evt.put("method", "setCount");
        evt.put("count", Integer.valueOf(theSnapshot.size()));
        evt.put("start", Integer.valueOf(theStart));
        evt.put("page", Integer.valueOf(thePageSize));
        theSession.postOutgoingEvent(evt);
    }

    /** Used to send entries in chunks instead of all at once */
    void sendEntriesProgressive(long[] ids) {
        try {
            prisms.logging.PrismsLogger logger = theSession.getApp().getEnvironment().getLogger();
            long[] subIDs = new long[100];
            for (int i = 0; i < ids.length; i += subIDs.length) {
                if (!isRunning) return;
                if (subIDs.length > ids.length - i) subIDs = new long[ids.length - i];
                System.arraycopy(ids, i, subIDs, 0, subIDs.length);
                LogEntry[] entries;
                try {
                    entries = logger.getItems(subIDs);
                } catch (PrismsException e) {
                    log.error("Could not get log entries", e);
                    return;
                }
                if (!isRunning) return;
                JSONArray jsonEntries = new JSONArray();
                for (LogEntry entry : entries) {
                    JSONObject jsonEntry = toJson(entry);
                    if (jsonEntry != null) jsonEntries.add(jsonEntry);
                }
                JSONObject evt = new JSONObject();
                evt.put("plugin", theName);
                evt.put("method", "addEntries");
                evt.put("entries", jsonEntries);
                theSession.postOutgoingEvent(evt);
                if (!isRunning) return;
            }
        } finally {
            isRunning = false;
        }
    }

    JSONObject toJson(LogEntry entry) {
        if (entry == null) return null;
        JSONObject jsonEntry = new JSONObject();
        jsonEntry.put("id", Integer.toHexString(entry.getID()));
        boolean dup = entry.getDuplicateRef() >= 0;
        if (dup) jsonEntry.put("duplicate", Integer.toHexString(entry.getDuplicateRef()));
        jsonEntry.put("instance", entry.getInstanceLocation());
        jsonEntry.put("app", entry.getApp());
        jsonEntry.put("client", entry.getClient());
        if (entry.getUser() != null) jsonEntry.put("user", entry.getUser().getName());
        jsonEntry.put("session", entry.getSessionID());
        jsonEntry.put("level", entry.getLevel().toString());
        if (entry.getLevel().equals(org.apache.log4j.Level.FATAL) || entry.getLevel().equals(org.apache.log4j.Level.ERROR)) jsonEntry.put("levelColor", "#ff0000"); else if (entry.getLevel().equals(org.apache.log4j.Level.WARN)) jsonEntry.put("levelColor", "#c0c000"); else if (entry.getLevel().equals(org.apache.log4j.Level.INFO)) jsonEntry.put("levelColor", "#0000ff"); else jsonEntry.put("levelColor", "#000000");
        jsonEntry.put("logger", entry.getLoggerName());
        jsonEntry.put("time", prisms.util.PrismsUtils.TimePrecision.MILLIS.print(entry.getLogTime(), false));
        jsonEntry.put("tracking", entry.getTrackingData());
        if (entry.getSaveTime() > 0) jsonEntry.put("saveTime", prisms.util.PrismsUtils.TimePrecision.MINUTES.print(entry.getSaveTime(), false));
        jsonEntry.put("message", entry.getMessage());
        jsonEntry.put("stackTrace", entry.getStackTrace());
        if (theSelected.contains(entry.getID())) jsonEntry.put("selected", Boolean.TRUE);
        return jsonEntry;
    }

    void doSave(final JSONObject event) {
        final String[] options = new String[] { "Plain Text", "Rich Text Format", "HTML" };
        theSession.getUI().select("Select the format to download the data in.", options, 1, new prisms.ui.UI.SelectListener() {

            public void selected(String option) {
                if (option == null) return;
                String format;
                switch(ArrayUtils.indexOf(options, option)) {
                    case 0:
                        format = "text/plain";
                        break;
                    case 1:
                        format = "text/rtf";
                        break;
                    case 2:
                        format = "text/html";
                        break;
                    default:
                        return;
                }
                thePI = new prisms.ui.UI.DefaultProgressInformer();
                thePI.setProgressText("Starting log download");
                getSession().getUI().startTimedTask(thePI);
                JSONObject toSend = new JSONObject();
                toSend.put("downloadPlugin", getName());
                toSend.remove("plugin");
                toSend.put("downloadMethod", "downloadLogs");
                toSend.put("method", "doDownload");
                toSend.put("settings", event.get("settings"));
                toSend.put("format", format);
                getSession().postOutgoingEvent(toSend);
            }
        });
    }

    void doRetrieve(JSONObject event) {
        String fileName = (String) event.get("file");
        java.io.File file = new java.io.File(theSession.getApp().getEnvironment().getLogger().getExposedDir() + fileName);
        String cp;
        try {
            cp = file.getCanonicalPath();
        } catch (IOException e) {
            cp = null;
        }
        if (cp == null || file.isHidden() || file.getName().startsWith(".") || !cp.startsWith(theSession.getApp().getEnvironment().getLogger().getExposedDir())) {
            theSession.getUI().error("The file referred to (" + fileName + ") does not exist or is not exposed");
            return;
        }
        JSONObject toSend = new JSONObject();
        toSend.put("downloadPlugin", getName());
        toSend.remove("plugin");
        toSend.put("downloadMethod", "exposedFile");
        toSend.put("method", "doDownload");
        toSend.put("file", event.get("file"));
        getSession().postOutgoingEvent(toSend);
    }

    public String getContentType(JSONObject event) {
        if ("exposedFile".equals(event.get("method"))) {
            String fileName = (String) event.get("file");
            int idx = fileName.lastIndexOf('.');
            if (idx < 0) return null;
            String ext = fileName.substring(idx + 1);
            if ("txt".equalsIgnoreCase(ext)) return "text/plain"; else if ("json".equalsIgnoreCase(ext)) return "text/json"; else if ("html".equalsIgnoreCase(ext)) return "text/html"; else if ("xml".equalsIgnoreCase(ext)) return "text/xml"; else if ("jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)) return "image/jpeg"; else if ("gif".equalsIgnoreCase(ext)) return "image/gif"; else if ("png".equalsIgnoreCase(ext)) return "image/png"; else return null;
        } else return (String) event.get("format");
    }

    public String getFileName(JSONObject event) {
        if ("exposedFile".equals(event.get("method"))) return new java.io.File((String) event.get("file")).getName(); else {
            String ret = "";
            try {
                ret += new java.net.URL(theSession.getApp().getEnvironment().getIDs().getLocalInstance().location).getHost();
            } catch (Exception e) {
                log.warn("Could not append host name to file: " + e);
                ret += "PRISMS Logs";
            }
            ret += " Log";
            if ("text/plain".equals(event.get("format"))) ret += ".txt"; else if ("text/rtf".equals(event.get("format"))) ret += ".rtf"; else ret += ".html";
            return ret;
        }
    }

    public int getDownloadSize(JSONObject event) {
        if ("exposedFile".equals(event.get("method"))) {
            String fileName = (String) event.get("file");
            java.io.File file = new java.io.File(theSession.getApp().getEnvironment().getLogger().getExposedDir() + fileName);
            String cp;
            try {
                cp = file.getCanonicalPath();
            } catch (IOException e) {
                cp = null;
            }
            if (cp == null || file.isHidden() || file.getName().startsWith(".") || !cp.startsWith(theSession.getApp().getEnvironment().getLogger().getExposedDir())) {
                return -1;
            }
            long ret = file.length();
            if (ret > Integer.MAX_VALUE) return -1;
            return (int) ret;
        } else return -1;
    }

    private static class Settings {

        boolean expanded;

        boolean dupExpanded;

        boolean stackExpanded;

        boolean dupStackExpanded;

        boolean ids;

        boolean instance;

        boolean app;

        boolean client;

        boolean tracking;

        int linesNoCollapse = 2;

        int linesInCollapsed = 2;

        Settings() {
        }

        void fromJson(JSONObject json) {
            expanded = Boolean.TRUE.equals(json.get("expanded"));
            dupExpanded = expanded && Boolean.TRUE.equals(json.get("dupExpanded"));
            stackExpanded = Boolean.TRUE.equals(json.get("stackExpanded"));
            dupStackExpanded = stackExpanded && Boolean.TRUE.equals(json.get("dupStackExpanded"));
            ids = Boolean.TRUE.equals(json.get("ids"));
            boolean meta = Boolean.TRUE.equals(json.get("metaData"));
            app = client = tracking = meta;
        }

        void print(LogEntry entry, java.io.PrintWriter writer, String format) throws IOException {
            if ("text/plain".equals(format)) {
                if (entry == null) {
                    writer.write("(Entry Unavailable)\n");
                    return;
                }
                if (ids) writer.write(Integer.toHexString(entry.getID()) + " ");
                writer.write(prisms.util.PrismsUtils.TimePrecision.MILLIS.print(entry.getLogTime(), true) + " ");
                writer.write(entry.getLevel().toString() + " ");
                writer.write(entry.getLoggerName() + " ");
                if (entry.getUser() != null) writer.write(entry.getUser().getName() + " "); else writer.write("(No User) ");
                if (instance) writer.write(entry.getInstanceLocation() + " ");
                if (app) {
                    if (entry.getApp() != null) {
                        writer.write(entry.getApp() + " ");
                        if (client && entry.getClient() != null) writer.write(entry.getClient() + " ");
                    } else writer.write("(No App) ");
                }
                if (tracking && entry.getTrackingData() != null) writer.write("tracking:" + entry.getTrackingData() + " ");
                if (entry.getDuplicateRef() >= 0) {
                    writer.write(" (duplicate");
                    if (ids) writer.write(" of " + Integer.toHexString(entry.getDuplicateRef()));
                    writer.write(")\n");
                } else writer.write("\n");
                String msg = entry.getMessage();
                msg = trim(msg, entry, false);
                msg = "\t" + msg.replaceAll("\n", "\n\t");
                writer.write(msg);
                if (!msg.endsWith("\n")) writer.write("\n");
                msg = entry.getStackTrace();
                if (msg != null) {
                    msg = trim(msg, entry, true);
                    msg = "\t" + msg.replaceAll("\n", "\n\t");
                    writer.write(msg);
                    if (!msg.endsWith("\n")) writer.write("\n");
                }
            } else if ("text/rtf".equals(format)) {
                if (entry == null) {
                    writer.write("(Entry Unavailable)\\par\n");
                    return;
                }
                if (ids) writer.write(Integer.toHexString(entry.getID()) + " ");
                writer.write("\\b");
                writer.write(prisms.util.PrismsUtils.TimePrecision.MILLIS.print(entry.getLogTime(), true) + " ");
                if (entry.getLevel().equals(org.apache.log4j.Level.FATAL) || entry.getLevel().equals(org.apache.log4j.Level.ERROR)) writer.write("\\cf2"); else if (entry.getLevel().equals(org.apache.log4j.Level.WARN)) writer.write("\\cf3"); else if (entry.getLevel().equals(org.apache.log4j.Level.INFO)) writer.write("\\cf4"); else writer.write("\\cf1");
                writer.write(entry.getLevel().toString() + " ");
                writer.write("\\cf5");
                writer.write(entry.getLoggerName() + " ");
                writer.write("\\cf4");
                if (entry.getUser() != null) writer.write(entry.getUser().getName() + " "); else writer.write("(No User) ");
                writer.write("\\cf1");
                if (instance) writer.write(entry.getInstanceLocation() + " ");
                if (app) {
                    if (entry.getApp() != null) {
                        writer.write(entry.getApp() + " ");
                        if (client && entry.getClient() != null) writer.write(entry.getClient() + " ");
                    } else writer.write("(No App) ");
                }
                writer.write("\\b0");
                if (tracking && entry.getTrackingData() != null) writer.write("tracking:" + entry.getTrackingData() + " ");
                if (entry.getDuplicateRef() >= 0) {
                    writer.write(" (duplicate");
                    if (ids) writer.write(" of " + Integer.toHexString(entry.getDuplicateRef()));
                    writer.write(")\\par\n");
                } else writer.write("\\par\n");
                String msg = entry.getMessage();
                msg = trim(msg, entry, false);
                msg = "\t" + msg.replaceAll("\\n", "\\\\par\n\t");
                writer.write(msg);
                writer.write("\\par\n");
                msg = entry.getStackTrace();
                if (msg != null) {
                    msg = trim(msg, entry, true);
                    msg = "\t" + msg.replaceAll("\\n", "\\\\par\n\t");
                    writer.write(msg);
                    writer.write("\\par\n");
                }
            } else {
                if (entry == null) {
                    writer.write("(Entry Unavailable)<br />\n");
                    return;
                }
                writer.write("<div title=\"");
                String title = "Instance:" + entry.getInstanceLocation() + "            \n";
                title += "Application:" + (entry.getApp() != null ? entry.getApp() : "None") + "            \n";
                if (entry.getApp() != null) title += "Client:" + (entry.getClient() != null ? entry.getClient() : "None") + "            \n";
                if (entry.getClient() != null) title += "User:" + (entry.getUser() != null ? entry.getUser() : "None") + "            \n";
                if (entry.getClient() != null) title += "SessionID:" + (entry.getSessionID() != null ? entry.getSessionID() : "None") + "            \n";
                title += "Logger:" + entry.getLoggerName() + "            \n";
                title += "TrackingInfo:" + (entry.getTrackingData() != null ? entry.getTrackingData() : "Not Available") + "            \n";
                writer.write(title);
                writer.write("\">\n");
                if (ids) writer.write(Integer.toHexString(entry.getID()) + " ");
                writer.write("<b>");
                writer.write(prisms.util.PrismsUtils.TimePrecision.MILLIS.print(entry.getLogTime(), true) + " ");
                writer.write("<span style=\"color:");
                if (entry.getLevel().equals(org.apache.log4j.Level.FATAL) || entry.getLevel().equals(org.apache.log4j.Level.ERROR)) writer.write("#ff0000"); else if (entry.getLevel().equals(org.apache.log4j.Level.WARN)) writer.write("#c0c000"); else if (entry.getLevel().equals(org.apache.log4j.Level.INFO)) writer.write("#0000ff"); else writer.write("#000000");
                writer.write("\">");
                writer.write(entry.getLevel().toString() + " ");
                writer.write("</span>");
                writer.write("<span style=\"color:#00d000\">");
                writer.write(entry.getLoggerName() + " ");
                writer.write("</span>");
                writer.write("<span style=\"color:#0000ff\">");
                if (entry.getUser() != null) writer.write(entry.getUser().getName() + " "); else writer.write("(No User) ");
                writer.write("</span>");
                if (instance) writer.write(entry.getInstanceLocation() + " ");
                if (app) {
                    if (entry.getApp() != null) {
                        writer.write(entry.getApp() + " ");
                        if (client && entry.getClient() != null) writer.write(entry.getClient() + " ");
                    } else writer.write("(No App) ");
                }
                writer.write("</b>");
                if (tracking && entry.getTrackingData() != null) writer.write("tracking:" + entry.getTrackingData() + " ");
                if (entry.getDuplicateRef() >= 0) {
                    writer.write(" (duplicate");
                    if (ids) writer.write(" of " + Integer.toHexString(entry.getDuplicateRef()));
                    writer.write(")<br />\n");
                } else writer.write("<br />\n");
                String msg = entry.getMessage();
                msg = trim(msg, entry, false);
                msg = "&nbsp;&nbsp;&nbsp;&nbsp;" + msg.replaceAll("\\n", "<br />\n&nbsp;&nbsp;&nbsp;&nbsp;");
                msg = msg.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
                writer.write(msg);
                writer.write("<br />\n");
                msg = entry.getStackTrace();
                if (msg != null) {
                    msg = trim(msg, entry, true);
                    msg = "&nbsp;&nbsp;&nbsp;&nbsp;" + msg.replaceAll("\\n", "<br />\n&nbsp;&nbsp;&nbsp;&nbsp;");
                    writer.write(msg);
                    writer.write("<br />\n");
                }
                writer.write("</div>\n");
            }
        }

        private String trim(String st, LogEntry entry, boolean stack) {
            StringBuilder str = new StringBuilder(st);
            while (str.length() > 0 && (str.charAt(0) == '\n' || str.charAt(0) == '\r')) str.delete(0, 1);
            while (str.length() > 0 && (str.charAt(str.length() - 1) == '\n' || str.charAt(str.length() - 1) == '\r')) str.delete(str.length() - 1, str.length());
            boolean delLines;
            if (stack) delLines = !stackExpanded || (entry.getDuplicateRef() >= 0 && !dupStackExpanded); else delLines = !expanded || (entry.getDuplicateRef() >= 0 && !dupExpanded);
            if (!delLines) return str.toString();
            int ret = 0;
            int i;
            for (i = 0; ret < linesNoCollapse && i < str.length(); i++) if (str.charAt(i) == '\n') ret++;
            if (ret >= linesNoCollapse) {
                if (linesInCollapsed != linesNoCollapse) {
                    ret = 0;
                    for (i = 0; ret < linesInCollapsed && i < str.length(); i++) if (str.charAt(i) == '\n') ret++;
                }
                str.setLength(i);
            }
            while (str.length() > 0 && (str.charAt(str.length() - 1) == '\n' || str.charAt(str.length() - 1) == '\r')) str.delete(str.length() - 1, str.length());
            return str.toString();
        }
    }

    public void doDownload(JSONObject event, OutputStream stream) throws IOException {
        if ("exposedFile".equals(event.get("method"))) {
            String fileName = (String) event.get("file");
            java.io.File file = new java.io.File(theSession.getApp().getEnvironment().getLogger().getExposedDir() + fileName);
            String cp;
            try {
                cp = file.getCanonicalPath();
            } catch (IOException e) {
                cp = null;
            }
            if (cp == null || file.isHidden() || file.getName().startsWith(".") || !cp.startsWith(theSession.getApp().getEnvironment().getLogger().getExposedDir())) {
                log.error("The file referred to (" + fileName + ") does not exist or is not exposed");
                return;
            }
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            int read = fis.read();
            while (read >= 0) {
                stream.write(read);
                read = fis.read();
            }
            fis.close();
            stream.close();
            return;
        } else {
            try {
                int[] ids;
                synchronized (theSnapshot) {
                    ids = theSnapshot.toArray();
                }
                Settings settings = new Settings();
                settings.fromJson((JSONObject) event.get("settings"));
                prisms.logging.PrismsLogger logger = theSession.getApp().getEnvironment().getLogger();
                java.io.PrintWriter writer = new java.io.PrintWriter(stream);
                String format = (String) event.get("format");
                String title = "Logs";
                try {
                    title += " on " + new java.net.URL(theSession.getApp().getEnvironment().getIDs().getLocalInstance().location).getHost();
                } catch (Exception e) {
                }
                title += " for search \"";
                if (theSession.getProperty(log4j.app.Log4jProperties.search) == null) title += "*"; else title += theSession.getProperty(log4j.app.Log4jProperties.search);
                title += "\" on " + prisms.util.PrismsUtils.print(theLastCheckTime);
                if ("text/plain".equals(format)) writer.write(title + "\n\n"); else if ("text/rtf".equals(format)) {
                    writer.write("{\\rtf\\ansi\\deff0{\\fonttbl{\\f0\\fswiss\\fcharset0 Arial;}}\n");
                    writer.write("{\\colortbl ");
                    writer.write(";\\red0\\green0\\blue0");
                    writer.write(";\\red255\\green0\\blue0");
                    writer.write(";\\red192\\green192\\blue0");
                    writer.write(";\\red0\\green0\\blue255");
                    writer.write(";\\red0\\green208\\blue0;}\n");
                    writer.write("{\\*\\generator PRISMS Logging " + theSession.getApp().getVersionString() + ";}");
                    writer.write("\\viewkind4\\uc1\\pard\n");
                    writer.write("\\cf1\\f0\\fs32");
                    writer.write(title);
                    writer.write("\\par\\fs20\n\\par\n");
                } else writer.write("<html><body>\n<h1>" + title + "</h1>\n<br />\n");
                thePI.setProgressScale(ids.length);
                thePI.setProgressText("Retrieving and writing log entries");
                long[] tempIDs = new long[ids.length <= 100 ? ids.length : 100];
                for (int i = 0; i < ids.length; i += tempIDs.length) {
                    thePI.setProgress(i);
                    if (tempIDs.length > ids.length - i) tempIDs = new long[ids.length - i];
                    for (int j = 0; j < tempIDs.length; j++) tempIDs[j] = ids[i + j];
                    LogEntry[] entries = logger.getItems(tempIDs);
                    for (int j = 0; j < entries.length; j++) {
                        thePI.setProgress(i + j);
                        settings.print(entries[j], writer, format);
                    }
                }
                if ("text/plain".equals(format)) writer.write("\n"); else if ("text/rtf".equals(format)) writer.write("\\par\n}"); else writer.write("</body></html>\n");
                writer.close();
                stream.close();
            } finally {
                thePI.setDone();
                thePI = null;
            }
        }
    }
}
