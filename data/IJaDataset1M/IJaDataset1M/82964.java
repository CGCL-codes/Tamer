package com.monad.homerun.pkg.webui;

import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Date;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.util.TimeUtil;

/**
 * TimelineServlet manages web requests for date-stamped data by
 * creating a Timeline data source and referencing it in a page.
 * Currently supports only log file data sources.
 * 
 * release 0.1 todos:
 * put in earlier, later buttons
 * put in new date entry & button
 * telescope time by density parse of input files
 */
public class TimelineServlet extends BaseServlet {

    private static final long serialVersionUID = 5376565576006299016L;

    private static final int DTH = 6;

    private static final int HOUR = 1000 * 60 * 60;

    private static final int DAY = HOUR * 24;

    private Logger logger = null;

    private String[] sources = null;

    private int windowSize = 1;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private SimpleDateFormat zbf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");

    private String rawLogDir = null;

    private String cvtLogDir = null;

    private SegmentCache segmentCache = null;

    public TimelineServlet() {
        logger = Activator.logSvc.getLogger();
        appName = "Timeline";
    }

    public void init() {
        super.init();
        if (GlobalProps.DEBUG) {
            logger.log(Level.FINE, "initializing");
        }
        String sourceStr = Activator.getProperty(appName, "sources");
        if (sourceStr != null) {
            sources = sourceStr.split(",");
        }
        String sizeStr = Activator.getProperty(appName, "window");
        if (sizeStr != null) {
            windowSize = Integer.parseInt(sizeStr);
        }
        rawLogDir = GlobalProps.getHomeDir() + File.separator + "logs";
        cvtLogDir = GlobalProps.getHomeDir() + File.separator + "temp" + File.separator + appName;
        File dir = new File(cvtLogDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        segmentCache = new SegmentCache(windowSize * sources.length);
        addNavInfo();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (needsAuthentication(request)) {
            response.sendRedirect(Activator.getProperty("Login", "alias"));
            return;
        }
        String info = request.getPathInfo();
        if (info != null && info.endsWith(".xml")) {
            if (info.startsWith("/")) {
                info = info.substring(1);
            }
            response.setContentType("text/xml");
            if (GlobalProps.DEBUG) {
                System.out.println("Serve events: " + request.getRequestURI());
            }
            serveEvents(response.getWriter(), new EventSpec(info));
            return;
        }
        long today = System.currentTimeMillis();
        String todayStr = TimeUtil.dateNameStr(today);
        String endDate = request.getParameter("date");
        if (endDate == null) {
            endDate = todayStr;
        }
        if (GlobalProps.DEBUG) {
            System.out.println("Timeline page");
        }
        response.setContentType("text/html");
        doTimelinePage(endDate, todayStr, request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (needsAuthentication(request)) {
            response.sendRedirect(Activator.getProperty("Login", "alias"));
            return;
        }
        String date = request.getParameter("date");
        if (date != null) {
            long newEndDate = TimeUtil.dateToTime(date);
            String next = request.getParameter("next");
            if (next != null) {
                newEndDate += DAY;
            } else {
                String prev = request.getParameter("prev");
                if (prev != null) {
                    newEndDate -= DAY;
                }
            }
            String endDate = TimeUtil.dateNameStr(newEndDate);
            String todayStr = TimeUtil.dateNameStr(System.currentTimeMillis());
            response.setContentType("text/html");
            doTimelinePage(endDate, todayStr, request, response);
        }
    }

    private void doTimelinePage(String endDate, String todayStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        EventSpec spec = findSources(endDate, todayStr);
        String hotZoneData = generateHotZones(spec);
        String dateStr = sdf.format(new Date());
        VelocityContext vCtx = getVelocityContext(request);
        vCtx.put("date", dateStr);
        vCtx.put("endDate", endDate);
        vCtx.put("hotZoneData", hotZoneData);
        vCtx.put("specFileName", spec.encodeFileName());
        mergeTemplate("timeline.vt", vCtx, response);
    }

    private String generateHotZones(EventSpec spec) {
        int[] hits = spec.getProfile();
        StringBuffer sb = new StringBuffer();
        long baseTime = TimeUtil.dateToTime(spec.baseDate);
        baseTime += TimeZone.getDefault().getOffset(baseTime);
        baseTime -= DAY * (windowSize - 1);
        for (int i = 0; i < hits.length; i++) {
            if (hits[i] > DTH) {
                if (GlobalProps.DEBUG) {
                    System.out.println("Create zone fpr index: " + i);
                }
                int mult = hits[i] / DTH;
                int rem = hits[i] % DTH;
                if (rem > 0) {
                    ++mult;
                }
                mult *= 10;
                int startHour = i * HOUR;
                sb.append("{  start: \"");
                sb.append(zbf.format(new Date(baseTime + startHour)));
                sb.append("\",");
                sb.append("   end: \"");
                sb.append(zbf.format(new Date(baseTime + startHour + HOUR)));
                sb.append("\",");
                sb.append("   magnify: ");
                sb.append(mult);
                sb.append(",");
                sb.append("   unit: ");
                sb.append("Timeline.DateTime.MINUTE");
                sb.append(" },");
            }
        }
        String zoneData = sb.toString();
        if (zoneData != null && zoneData.length() > 0) {
            zoneData = zoneData.substring(0, zoneData.length() - 1);
            return zoneData;
        }
        return null;
    }

    private EventSpec findSources(String endDate, String sToday) throws IOException {
        long endTime = TimeUtil.dateToTime(endDate);
        EventSpec spec = new EventSpec(endTime);
        String srcPath = rawLogDir + File.separator;
        for (String source : sources) {
            File logDir = new File(srcPath + source);
            if (logDir.exists()) {
                File[] logs = logDir.listFiles(new DateFilter(endTime, windowSize));
                for (File log : logs) {
                    String lName = log.getName();
                    String name = lName.substring(0, lName.length() - 4);
                    EventSource es = new EventSource(source, name, getIndex(name, endDate));
                    if (name.equals(sToday)) {
                        validateSource(es);
                    }
                    if (segmentCache.getSegment(es) == null) {
                        File cvtDir = new File(cvtLogDir);
                        String[] cvtNames = cvtDir.list();
                        String fileName = es.type + "_" + es.name + ".xml";
                        if (!Arrays.asList(cvtNames).contains(fileName)) {
                            logToEventFile(es.type, es.name);
                        }
                        loadSegmentCache(es);
                    }
                    spec.addSource(es);
                }
            }
        }
        return spec;
    }

    private class DateFilter implements FileFilter {

        long endDate = 0L;

        long beginDate = 0L;

        public DateFilter(long endDate, int days) {
            this.endDate = endDate;
            beginDate = endDate - (days * DAY);
        }

        public boolean accept(File file) {
            return (file.lastModified() >= beginDate && file.lastModified() < endDate + DAY);
        }
    }

    private void serveEvents(PrintWriter out, EventSpec spec) throws IOException {
        out.println("<data date-time-format=\"iso8601\">");
        List<EventSource> sources = spec.getSources();
        for (EventSource source : sources) {
            String segment = segmentCache.getSegment(source);
            if (segment == null) {
                if (GlobalProps.DEBUG) {
                    System.out.println("Serve events cache miss source: " + source.name);
                }
                segment = loadSegmentCache(source);
            }
            out.write(segment);
        }
        if (sources.size() == 0) {
            writeEvent(out, new Date(), "No Events", "No events found in current selection");
        }
        out.println("</data>");
        out.flush();
    }

    private void logToEventFile(String type, String name) throws IOException {
        String logPath = rawLogDir + File.separator + type + File.separator + name + ".log";
        String eventPath = cvtLogDir + File.separator + type + "_" + name + ".xml";
        String profPath = cvtLogDir + File.separator + type + "_" + name + ".prof";
        File logFile = new File(logPath);
        if (!logFile.exists()) {
            if (GlobalProps.DEBUG) {
                logger.log(Level.FINEST, "requested log: " + name + " of type: " + type + " does not exist");
            }
            return;
        }
        File eventFile = new File(eventPath);
        if (eventFile.exists()) {
            if (eventFile.lastModified() >= logFile.lastModified()) {
                return;
            } else {
                eventFile.delete();
            }
        }
        BufferedReader in = new BufferedReader(new FileReader(logFile));
        PrintWriter out = new PrintWriter(new FileOutputStream(eventFile));
        Profile prof = new Profile();
        String line = null;
        while ((line = in.readLine()) != null) {
            String[] parts = line.split("\t");
            Date time = new Date(Long.parseLong(parts[0]));
            String[] tokens = tokenize(parts[3]);
            String objName = tokens[tokens.length - 1];
            if (Level.WARNING.getName().equals(parts[2])) {
                objName = tokens[0];
            }
            writeEvent(out, time, objName, parts[3]);
            prof.addEvent(time);
        }
        in.close();
        out.close();
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(profPath));
        for (Integer bucket : prof.getBuckets()) {
            oout.writeObject(bucket);
        }
        oout.close();
    }

    private class Profile {

        int[] buckets = null;

        Calendar cal = null;

        public Profile() {
            buckets = new int[24];
            cal = new GregorianCalendar();
        }

        public void addEvent(Date time) {
            cal.setTime(time);
            buckets[cal.get(Calendar.HOUR_OF_DAY)]++;
        }

        public int[] getBuckets() {
            return buckets;
        }
    }

    private void writeEvent(PrintWriter out, Date time, String title, String desc) {
        out.println("<event start=\"" + sdf.format(time) + "\" title=\"" + title + "\">");
        out.println(desc);
        out.println("</event>");
    }

    private String[] tokenize(String msg) {
        String[] raw = msg.split(" ");
        List<String> tList = new ArrayList<String>();
        String pending = null;
        for (int i = 0; i < raw.length; i++) {
            if (raw[i].startsWith("'")) {
                if (raw[i].endsWith("'")) {
                    tList.add(raw[i].substring(1, raw[i].length() - 1));
                } else if (pending == null) {
                    pending = raw[i].substring(1);
                }
            } else if (raw[i].endsWith("'")) {
                pending += " " + raw[i].substring(0, raw[i].length() - 1);
                tList.add(pending);
                pending = null;
            } else if (pending != null) {
                pending += " " + raw[i];
            }
        }
        return (String[]) tList.toArray(new String[0]);
    }

    private void validateSource(EventSource source) {
        String logPath = rawLogDir + File.separator + source.type + File.separator + source.name + ".log";
        String eventPath = cvtLogDir + File.separator + source.type + "_" + source.name;
        File logFile = new File(logPath);
        boolean remove = false;
        if (!logFile.exists()) {
            if (GlobalProps.DEBUG) {
                logger.log(Level.FINEST, "requested log: " + source.name + " of type: " + source.type + " does not exist");
            }
            remove = true;
        }
        File eventFile = new File(eventPath + ".xml");
        if (eventFile.exists()) {
            if (logFile.lastModified() >= eventFile.lastModified()) {
                remove = true;
            }
            if (remove) {
                eventFile.delete();
                File profFile = new File(eventPath + ".prof");
                if (profFile.exists()) {
                    profFile.delete();
                }
            }
        }
        if (remove) {
            segmentCache.removeSegment(source);
        }
    }

    private String loadSegmentCache(EventSource source) throws IOException {
        String eventPath = cvtLogDir + File.separator + source.type + "_" + source.name;
        FileInputStream in = new FileInputStream(eventPath + ".xml");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int read = in.read(buf);
        while (read != -1) {
            out.write(buf, 0, read);
            read = in.read(buf);
        }
        in.close();
        String segment = out.toString();
        Integer[] buckets = new Integer[24];
        try {
            ObjectInputStream pin = new ObjectInputStream(new FileInputStream(eventPath + ".prof"));
            for (int i = 0; i < 24; i++) {
                buckets[i] = (Integer) pin.readObject();
            }
            pin.close();
        } catch (ClassNotFoundException cnfe) {
            ;
        }
        segmentCache.addSegment(source, segment, buckets);
        return segment;
    }

    private class SegmentCache {

        private int maxSize = 0;

        private Map<String, Entry> cache = null;

        public SegmentCache(int maxSize) {
            this.maxSize = maxSize;
            cache = new HashMap<String, Entry>();
        }

        public String getSegment(EventSource source) {
            Entry hit = cache.get(source.type + source.name);
            if (hit != null) {
                hit.count++;
                return hit.value;
            }
            return null;
        }

        public Integer[] getProfile(EventSource source) {
            Entry hit = cache.get(source.type + source.name);
            if (hit != null) {
                return hit.buckets;
            }
            return null;
        }

        public void addSegment(EventSource source, String segment, Integer[] buckets) {
            if (cache.size() >= maxSize) {
                String lowKey = null;
                int lowCount = Integer.MAX_VALUE;
                for (String cKey : cache.keySet()) {
                    int count = cache.get(cKey).count;
                    if (count < lowCount) {
                        lowCount = count;
                        lowKey = cKey;
                    }
                }
                cache.remove(lowKey);
            }
            cache.put(source.type + source.name, new Entry(segment, buckets));
        }

        public void removeSegment(EventSource source) {
            cache.remove(source.type + source.name);
        }

        private class Entry {

            public String value;

            public Integer[] buckets;

            public int count;

            public Entry(String value, Integer[] buckets) {
                this.value = value;
                this.buckets = buckets;
                count = 0;
            }
        }
    }

    private class EventSpec {

        private String baseDate = null;

        private List<String> types = null;

        private List<EventSource> sources = null;

        private int[] hits = null;

        public EventSpec() {
            baseDate = TimeUtil.dateNameStr(System.currentTimeMillis());
            types = new ArrayList<String>();
            sources = new ArrayList<EventSource>();
            hits = new int[windowSize * 24];
        }

        public EventSpec(long base) {
            this();
            baseDate = TimeUtil.dateNameStr(base);
        }

        public EventSpec(String fileName) {
            this();
            decodeFileName(fileName);
        }

        public void addSource(EventSource source) {
            if (!types.contains(source.type)) {
                types.add(source.type);
            }
            sources.add(source);
            Integer[] prof = segmentCache.getProfile(source);
            int idx = source.offset;
            for (Integer iVal : prof) {
                hits[idx++] += iVal;
            }
        }

        public List<EventSource> getSources() {
            return sources;
        }

        public int[] getProfile() {
            return hits;
        }

        private void decodeFileName(String fileName) {
            if (GlobalProps.DEBUG) {
                System.out.println("EventSpec dcfn enter ");
            }
            fileName = fileName.substring(0, fileName.length() - 4);
            String[] types = fileName.split("_");
            baseDate = types[0];
            for (int i = 1; i < types.length; i++) {
                String[] edits = types[i].split("-");
                String type = edits[0];
                for (int j = 1; j < edits.length; j++) {
                    if ("x".equals(edits[j])) {
                        addSource(new EventSource(type, baseDate, getIndex(baseDate, baseDate)));
                        if (GlobalProps.DEBUG) {
                            System.out.println("EventSpec added x source ");
                        }
                    } else {
                        char[] editBuf = baseDate.toCharArray();
                        for (int k = 0; k < edits[j].length(); k += 2) {
                            int pos = (int) edits[j].charAt(k) - (int) '0';
                            editBuf[pos] = edits[j].charAt(k + 1);
                        }
                        String newDate = new String(editBuf);
                        addSource(new EventSource(type, newDate, getIndex(newDate, baseDate)));
                    }
                }
            }
        }

        public String encodeFileName() {
            char[] cBase = baseDate.toCharArray();
            StringBuffer fnb = new StringBuffer();
            fnb.append(baseDate);
            for (String type : types) {
                fnb.append("_").append(type);
                for (EventSource source : sources) {
                    fnb.append("-");
                    char[] cSrc = source.name.toCharArray();
                    boolean edits = false;
                    for (int i = 0; i < cSrc.length; i++) {
                        if (cBase[i] != cSrc[i]) {
                            fnb.append(i).append(cSrc[i]);
                            edits = true;
                        }
                    }
                    if (!edits) {
                        fnb.append("x");
                    }
                }
            }
            return fnb.append(".xml").toString();
        }
    }

    private class EventSource {

        public String type;

        public String name;

        public int offset;

        public EventSource(String type, String name, int offset) {
            this.type = type;
            this.name = name;
            this.offset = offset;
        }
    }

    private int getIndex(String idxDate, String baseDate) {
        if (idxDate.equals(baseDate)) {
            return ((windowSize - 1) * 24);
        }
        long idxTime = TimeUtil.dateToTime(idxDate);
        long baseTime = TimeUtil.dateToTime(baseDate);
        long diff = baseTime - idxTime;
        long numdays = diff / (1000 * 60 * 60 * 24);
        if ((int) numdays < windowSize) {
            return ((windowSize - (int) numdays - 1) * 24);
        }
        return 0;
    }
}
