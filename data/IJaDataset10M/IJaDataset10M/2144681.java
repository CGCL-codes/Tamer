package org.mmt.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.mmt.core.bean.ProcessList;
import org.mmt.core.bean.ProcessListItem;
import org.mmt.gui.Application;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.ArcDef;
import org.rrd4j.core.DsDef;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;

public class DataRecorder implements ProcessListListener {

    public static final String BASE_FILES_PATH = "./files/";

    private static String LINE_SEPARATOR = null;

    private static DataRecorder instance;

    private boolean recording;

    private boolean recordProcessLists;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

    private int recordingServers = 0;

    private DataCell[] cells = new DataCell[10];

    private String serverGroup;

    private boolean deletingRRD;

    private Map<String, RrdDb> rrdDbs = new HashMap<String, RrdDb>();

    static {
        synchronized (DataRecorder.class) {
            instance = new DataRecorder();
        }
    }

    private DataRecorder() {
        recording = false;
        recordProcessLists = false;
        deletingRRD = false;
        LINE_SEPARATOR = System.getProperty("line.separator");
    }

    public static DataRecorder getInstance() {
        return instance;
    }

    public void startRecording() {
        recording = true;
        if (recordProcessLists) addServersListeners();
    }

    public void stopRecording() {
        recording = false;
        removeServersListeners();
    }

    public void setRecordProcessLists(boolean recordProcessLists) {
        this.recordProcessLists = recordProcessLists;
        if (recordProcessLists) {
            if (recording) addServersListeners();
        } else removeServersListeners();
    }

    public boolean isRecordProcessLists() {
        return recordProcessLists;
    }

    private void addServersListeners() {
        if (serverGroup == null) throw new IllegalStateException("No server group defined.");
        MysqlServer[] servers = ServersManager.getInstance().getServersByGroup(serverGroup);
        recordingServers = servers.length;
        if (servers != null) {
            for (MysqlServer server : servers) {
                server.addProcessListListener(this);
            }
        }
    }

    private void removeServersListeners() {
        if (serverGroup == null) throw new IllegalStateException("No server group defined.");
        MysqlServer[] servers = ServersManager.getInstance().getServersByGroup(serverGroup);
        recordingServers = 0;
        if (servers != null) {
            for (MysqlServer server : servers) {
                server.removeProcessListListener(this);
            }
        }
    }

    @Override
    public synchronized void onProcessListGenerated(ProcessList processlist) {
        if (!recording) return;
        if (!recordProcessLists) return;
        long seconds = processlist.getDate().getTime() / 1000;
        int index = (int) (seconds % cells.length);
        if (cells[index] == null) {
            DataCell dc = new DataCell();
            dc.setSeconds(seconds);
            List<ProcessList> list = new ArrayList<ProcessList>();
            list.add(processlist);
            dc.setList(list);
            cells[index] = dc;
        } else {
            if (cells[index].getSeconds() == seconds) {
                List<ProcessList> list = cells[index].getList();
                list.add(processlist);
                if (list.size() == recordingServers) {
                    storeProcessList(processlist.getDate(), list, false);
                    cells[index] = null;
                }
            } else if (cells[index].getSeconds() < seconds) {
                List<ProcessList> list = cells[index].getList();
                storeProcessList(list.get(0).getDate(), list, true);
                List<ProcessList> newList = new ArrayList<ProcessList>();
                newList.add(processlist);
                DataCell dc = new DataCell();
                dc.setSeconds(seconds);
                dc.setList(newList);
                cells[index] = dc;
            } else {
                List<ProcessList> newList = new ArrayList<ProcessList>();
                newList.add(processlist);
                storeProcessList(processlist.getDate(), newList, true);
            }
        }
    }

    private void storeProcessList(Date date, List<ProcessList> processListes, boolean doNotOverrideFile) {
        final int BUFFER = 2048;
        String filename = BASE_FILES_PATH + serverGroup + "/processlist/" + sdf.format(date) + ".zip";
        if (doNotOverrideFile && new File(filename).exists()) return;
        try {
            FileOutputStream dest = new FileOutputStream(filename);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            for (ProcessList processList : processListes) {
                ByteArrayInputStream fi = new ByteArrayInputStream(buildProcessListText(processList).getBytes());
                BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(processList.getMysqlServer().getName() + ".txt");
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildProcessListText(ProcessList processlist) {
        StringBuilder sb = new StringBuilder();
        sb.append("ProcessList generated on ").append(processlist.getDate()).append(LINE_SEPARATOR);
        sb.append("Server: ").append(processlist.getMysqlServer().getName()).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        sb.append("PID\t\tUser\t\tHost\t\tDb\t\tCommand\t\tTime\t\tState\t\tInfo").append(LINE_SEPARATOR);
        List<ProcessListItem> items = processlist.getItems();
        if (items != null) {
            int sleeping = 0;
            for (ProcessListItem item : items) {
                String itemCommand = item.getCommand();
                if (itemCommand == null || !itemCommand.equals("Sleep")) {
                    sb.append(item.getPid()).append("\t\t").append(item.getUser()).append("\t\t").append(item.getHost()).append("\t\t").append(item.getDb()).append("\t\t").append(item.getCommand()).append("\t\t").append(item.getTime()).append("\t\t").append(item.getState()).append("\t\t").append(item.getInfo()).append(LINE_SEPARATOR);
                } else sleeping++;
            }
            if (sleeping > 0) sb.append("Plus ").append(sleeping).append(" sleeping connections.");
        }
        return sb.toString();
    }

    public void storeChartDatum(String chartname, MysqlServer server, double datum, Date date) {
        if (!recording) return;
        if (deletingRRD) return;
        String filepath = BASE_FILES_PATH + serverGroup + "/rrd/" + server.getName() + "-" + chartname + ".rrd";
        RrdDb rrd = rrdDbs.get(filepath);
        try {
            if (rrd == null) {
                if (new File(filepath).exists()) rrd = new RrdDb(filepath); else rrd = createRrd(filepath);
                rrdDbs.put(filepath, rrd);
            }
            try {
                rrd.createSample(date.getTime() / 1000).setValues(datum).update();
            } catch (IllegalArgumentException e) {
            }
        } catch (IOException e) {
            Application.showError(e.getMessage());
        }
    }

    private RrdDb createRrd(String aPath) throws IOException {
        int step = 1;
        DsDef data = new DsDef("data", DsType.ABSOLUTE, step, Double.NaN, Double.NaN);
        ArcDef secondAvarage = new ArcDef(ConsolFun.AVERAGE, 0.5, 1, 60 * 60 * 24);
        ArcDef minuteAverage = new ArcDef(ConsolFun.AVERAGE, 0.5, 60, 60 * 24 * 7);
        ArcDef hourAverage = new ArcDef(ConsolFun.AVERAGE, 0.5, 60 * 60, 24 * 30);
        RrdDef definition = new RrdDef(aPath, step);
        definition.addDatasource(data);
        definition.addArchive(secondAvarage);
        definition.addArchive(minuteAverage);
        definition.addArchive(hourAverage);
        RrdDb result = new RrdDb(definition);
        return result;
    }

    public void setServerGroup(String serverGroup) {
        this.serverGroup = serverGroup;
        File f = new File(BASE_FILES_PATH + serverGroup + "/processlist");
        if (!f.exists()) f.mkdirs();
        f = new File(BASE_FILES_PATH + serverGroup + "/rrd");
        if (!f.exists()) f.mkdirs();
    }

    public String getServerGroup() {
        return serverGroup;
    }

    public long getProcessListsDiskUsageStatistics() {
        return getDirectoryDiskUsage(BASE_FILES_PATH + serverGroup + "/processlist");
    }

    public long getChartsDataDiskUsageStatistics() {
        return getDirectoryDiskUsage(BASE_FILES_PATH + serverGroup + "/rrd");
    }

    private long getDirectoryDiskUsage(String directory) {
        long size = 0;
        File dir = new File(directory);
        File[] files = dir.listFiles();
        if (files == null) return 0;
        for (File file : files) {
            if (file.isDirectory()) size += getDirectoryDiskUsage(file.getAbsolutePath()); else size += file.length();
        }
        return size;
    }

    private void deleteDirectoryFiles(String directory) {
        File f = new File(directory);
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    public void deleteRecordedData() {
        deleteDirectoryFiles(BASE_FILES_PATH + serverGroup + "/processlist");
        deletingRRD = true;
        Collection<RrdDb> dbs = rrdDbs.values();
        Iterator<RrdDb> it = dbs.iterator();
        while (it.hasNext()) {
            RrdDb db = it.next();
            try {
                db.close();
            } catch (IOException e) {
                Application.showError(e.getMessage());
            }
            it.remove();
        }
        deleteDirectoryFiles(BASE_FILES_PATH + serverGroup + "/rrd");
        deletingRRD = false;
    }

    public String[] getAvailableChartData() {
        List<String> charts = new ArrayList<String>();
        File dir = new File(BASE_FILES_PATH + serverGroup + "/rrd");
        File[] files = dir.listFiles();
        if (files == null) return null;
        for (File file : files) {
            if (file.getName().endsWith(".rrd")) charts.add(file.getName().substring(0, file.getName().length() - 4));
        }
        if (charts.size() > 0) return charts.toArray(new String[0]);
        return null;
    }

    public boolean processlistExists(Date date) {
        String filename = BASE_FILES_PATH + serverGroup + "/processlist/" + sdf.format(date) + ".zip";
        return new File(filename).exists();
    }

    public Map<String, String> getStoredProcesslists(Date date) {
        String filename = BASE_FILES_PATH + serverGroup + "/processlist/" + sdf.format(date) + ".zip";
        if (!new File(filename).exists()) return null;
        Map<String, String> map = new HashMap<String, String>();
        ZipFile zf = null;
        try {
            zf = new ZipFile(filename);
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = entries.nextElement();
                long size = ze.getSize();
                if (size > 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append(LINE_SEPARATOR);
                    }
                    br.close();
                    map.put(ze.getName().substring(0, ze.getName().length() - 4), sb.substring(0, sb.length() - LINE_SEPARATOR.length()));
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (zf != null) zf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DataCell {

        private long seconds;

        private List<ProcessList> list;

        public long getSeconds() {
            return seconds;
        }

        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }

        public List<ProcessList> getList() {
            return list;
        }

        public void setList(List<ProcessList> list) {
            this.list = list;
        }
    }
}
