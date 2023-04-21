package tcpmon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import Tux2Java.Posix;
import tcpmon.gui.TableModel;

/**
 * @author Chris Boesch
 * 
 * Created on 04.01.2008
 */
public class Monitor extends Thread {

    private static final int NET_PROT = 0;

    private static final int NET_ADRLOC = 2;

    private static final int NET_ADRREM = 3;

    private static final int NET_STAT = 4;

    private static final int NET_INODE = 18;

    private JTable table;

    private TableModel tm;

    private Vector<String> netLines;

    private String[][] netCols;

    private HashMap<String, String> adrList;

    private HashMap<String, String> localIpList;

    private HashMap<String, String> solvedLocalAdrList;

    private Vector<String> unsolvedIpList;

    private HashMap<String, String> solvedRemoteAdrList;

    private Thread adrSolver;

    private HashMap<String, String> stateList;

    public Monitor(JTable table) {
        try {
            this.table = table;
            tm = new TableModel();
            this.table.setModel(this.tm);
            TableColumnModel cm = table.getColumnModel();
            cm.getColumn(0).setPreferredWidth(Prefs.getInt(Prefs.PREF_COL_0, 150));
            cm.getColumn(1).setPreferredWidth(Prefs.getInt(Prefs.PREF_COL_1, 70));
            cm.getColumn(2).setPreferredWidth(Prefs.getInt(Prefs.PREF_COL_2, 110));
            cm.getColumn(3).setPreferredWidth(Prefs.getInt(Prefs.PREF_COL_3, 170));
            cm.getColumn(4).setPreferredWidth(Prefs.getInt(Prefs.PREF_COL_4, 85));
            adrList = new HashMap<String, String>();
            localIpList = new HashMap<String, String>();
            solvedLocalAdrList = new HashMap<String, String>();
            fillLocalAdr();
            unsolvedIpList = new Vector<String>();
            solvedRemoteAdrList = new HashMap<String, String>();
            adrSolver = new AdrSolver(unsolvedIpList, solvedRemoteAdrList);
            stateList = new HashMap<String, String>();
            fillStateList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                fillNetList();
                fillAdrList();
                Vector<String> vec = new Vector<String>(netLines.size());
                for (int n = 0; n < netLines.size(); n++) {
                    if (netCols[n][NET_ADRREM].equals("00000000:0000")) continue;
                    vec.add(getProcName(netCols[n][NET_ADRLOC]) + ";" + netCols[n][NET_PROT] + ";" + solveLocAdr(netCols[n][NET_ADRLOC]) + ";" + solveRemAdr(netCols[n][NET_ADRREM]) + ";" + solveStat(netCols[n][NET_STAT]) + ";" + netCols[n][NET_ADRLOC]);
                }
                if (unsolvedIpList.size() > 0) {
                    synchronized (adrSolver) {
                        if (adrSolver.getState() == Thread.State.WAITING) adrSolver.notify();
                    }
                }
                String[][] lines = new String[vec.size()][6];
                for (int n = 0; n < vec.size(); n++) lines[n] = vec.get(n).split(";");
                tm.setData(lines);
                table.repaint();
                sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fillLocalAdr() {
        try {
            Enumeration<NetworkInterface> netInter = NetworkInterface.getNetworkInterfaces();
            while (netInter.hasMoreElements()) {
                NetworkInterface ni = netInter.nextElement();
                Enumeration<InetAddress> adrs = ni.getInetAddresses();
                while (adrs.hasMoreElements()) {
                    Object obj = adrs.nextElement();
                    if (obj.getClass() == Inet4Address.class) {
                        Inet4Address in4adr = (Inet4Address) obj;
                        localIpList.put(in4adr.getHostAddress(), ni.getDisplayName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String hexToIp(String adr) {
        int size = adr.length() / 2;
        byte[] bytes = new byte[4];
        String[] buf = new String[size];
        for (int n = 0; n < size; n++) buf[n] = adr.substring(n * 2, n * 2 + 2);
        for (int n = 0; n < 4; n++) bytes[n] = Integer.valueOf(buf[size - n - 1], 16).byteValue();
        return bytes[0] + "." + bytes[1] + "." + bytes[2] + "." + bytes[3];
    }

    private void fillStateList() {
        stateList.put("01", "ESTABLISHED");
        stateList.put("06", "TIME_WAIT");
        stateList.put("08", "CLOSE_WAIT");
        stateList.put("09", "LAST_ACK");
        stateList.put("0B", "0B");
    }

    private String solveStat(String stat) {
        String state = stateList.get(stat);
        if (state == null) return stat; else return state;
    }

    private String solveLocAdr(String adr) {
        try {
            int sep = adr.indexOf(":");
            String port = adr.substring(sep + 1);
            adr = adr.substring(0, sep);
            String host = solvedLocalAdrList.get(adr);
            if (host == null) {
                host = localIpList.get(hexToIp(adr));
                if (host == null) return "unknown:" + PortName.get(port);
                solvedLocalAdrList.put(adr, host);
            }
            return host + ":" + PortName.get(port);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String solveRemAdr(String adr) {
        try {
            int sep = adr.indexOf(":");
            String port = adr.substring(sep + 1);
            adr = adr.substring(0, sep);
            String host = solvedRemoteAdrList.get(adr);
            if (host == null) {
                unsolvedIpList.add(adr);
                host = hexToIp(adr);
            }
            return host + ":" + PortName.get(port);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getProcName(String procAdr) {
        String procName = "unknown";
        try {
            String pid = adrList.get(procAdr);
            if (pid != null) {
                File procStatus = new File("/proc/" + pid + "/status");
                if (procStatus.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(procStatus));
                    try {
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            if (line.indexOf("Name:") > -1) {
                                procName = line.substring(line.indexOf(":") + 1).trim() + ":" + pid;
                            }
                        }
                    } finally {
                        br.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return procName;
    }

    private void fillNetList() throws Exception {
        netLines = new Vector<String>();
        fillTcpLines("/proc/net/tcp", netLines, "TCP");
        fillTcpLines("/proc/net/tcp6", netLines, "TCP");
        fillTcpLines("/proc/net/udp", netLines, "UDP");
        fillTcpLines("/proc/net/udp6", netLines, "UDP");
        netCols = new String[netLines.size()][];
        for (int n = 0; n < netLines.size(); n++) netCols[n] = netLines.get(n).split(" ");
    }

    private void fillTcpLines(String listName, Vector<String> lines, String prot) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(listName));
        try {
            int cnt = -1;
            String line = null;
            while ((line = br.readLine()) != null) {
                cnt++;
                if (cnt < 1) continue;
                lines.add(prot + " " + line.trim());
            }
        } finally {
            br.close();
        }
    }

    private void fillAdrList() {
        File procDir = new File("/proc");
        String[] procList = procDir.list();
        for (int k = 0; k < procList.length; k++) {
            File pidDir = new File(procDir.getAbsolutePath() + "/" + procList[k]);
            if (!pidDir.isDirectory()) continue;
            File fdDir = new File(pidDir.getAbsolutePath() + "/fd");
            if (!fdDir.isDirectory()) continue;
            String[] fdList = fdDir.list();
            if (fdList == null) continue;
            for (int n = 0; n < fdList.length; n++) {
                File fd = new File(fdDir.getAbsolutePath() + "/" + fdList[n]);
                if (!fd.exists()) continue;
                String link = Posix.getLinkTarget(fd.getAbsolutePath());
                if (link.startsWith("socket")) {
                    String socket = link.substring(link.indexOf(":[") + 2, link.indexOf("]"));
                    for (int i = 0; i < netLines.size(); i++) {
                        if (socket.equals(netCols[i][NET_INODE])) {
                            adrList.put(netCols[i][NET_ADRLOC], pidDir.getName());
                            break;
                        }
                    }
                }
            }
            Vector<String> invalidAdr = new Vector<String>(adrList.entrySet().size());
            for (String adr : adrList.keySet()) {
                boolean found = false;
                for (int n = 0; n < netLines.size(); n++) {
                    found = adr.equals(netCols[n][NET_ADRLOC]);
                    if (found) break;
                }
                if (found) continue;
                invalidAdr.add(adr);
            }
            for (int n = 0; n < invalidAdr.size(); n++) adrList.remove(invalidAdr.get(n));
        }
    }

    public void killSelected() {
        String proc = (String) tm.getValueAt(table.getSelectedRow(), 5);
        System.out.println("kill " + proc);
        JOptionPane.showMessageDialog(null, "kill " + proc, "Test", JOptionPane.INFORMATION_MESSAGE);
    }
}
