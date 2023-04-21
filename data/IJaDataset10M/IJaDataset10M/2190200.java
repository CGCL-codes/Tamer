package ade;

import ade.ADERemoteExecutor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.StringReader;

/**
Superclass for gathering information about hosts. Subclassed according to
appropriate operating system, due to platform dependent shell scripts.

@author Jim Kramer
*/
public abstract class ADEHostStatus {

    /** Default period for repeated execution. */
    public static final long DEF_PERIOD = 5000;

    private static String prg = "ADEHostStatus";

    private static boolean debug = false;

    protected static final String probe = "probe";

    protected static final String gather = "gather";

    private String probeScript, gatherScript;

    private boolean createdProbeScript = false;

    private boolean createdGatherScript = false;

    private boolean copiedProbeScript = false;

    private boolean copiedGatherScript = false;

    protected long period = DEF_PERIOD;

    protected ADERemoteExecutor are;

    protected String cmdsh;

    protected String cmdrm;

    protected String ip;

    protected String tmpdirLocal;

    protected String tmpdirRemote;

    private boolean available = false;

    private int numcpus;

    private double avgcpuload;

    private int memmb;

    private int memavail;

    /** Write the host check shell script to the local temporary directory. */
    public abstract String createProbeScript() throws IOException;

    /** Write the host stats shell script to the local temporary directory. */
    public abstract String createStatsScript() throws IOException;

    private ADEHostStatus() {
    }

    /** Constructor with no ssh or scp arguments. */
    public ADEHostStatus(String ip, String uid, String hc, String pc) {
        this(ip, uid, hc, new String[] { "" }, pc, new String[] { "" });
    }

    /** Constructor with all parameters, <tt>ssh</tt> and <tt>scp</tt>
	 * arguments are Strings. */
    public ADEHostStatus(String ip, String uid, String hc, String ha, String pc, String pa) {
        are = new ADERemoteExecutor(ip, uid, hc, ha, pc, pa);
    }

    /** Constructor with all parameters, <tt>ssh</tt> and <tt>scp</tt>
	 * arguments are String arrays. */
    public ADEHostStatus(String ip, String uid, String hc, String[] ha, String pc, String[] pa) {
        are = new ADERemoteExecutor(ip, uid, hc, ha, pc, pa);
    }

    /** Returns the IP address (used as an identifier). */
    public String getIP() {
        return ip;
    }

    /** Whether the host is available (i.e., the last remote copy or command
	 * succeeded).
	 * @return <tt>true</tt> if host is available, <tt>false</tt> otherwise */
    public boolean isAvailable() {
        return available;
    }

    /** Get the period (in ms) for repeated execution. */
    public long getPeriod() {
        return period;
    }

    /** Set the period (in ms) for repeated execution. */
    public void setPeriod(long p) {
        period = p;
    }

    /** Set the ssh argument string.
	 * @param args The ssh arguments */
    protected void setSSHArgs(String args) {
        are.setSSHArgs(args);
    }

    /** Set the scp argument string.
	 * @param args The scp arguments */
    protected void setSCPArgs(String args) {
        are.setSCPArgs(args);
    }

    /** Create the probe and stats gathering scripts, which must be defined
	 * by subclass. */
    public boolean createScripts() throws IOException {
        if (!createdProbeScript) {
            try {
                probeScript = createProbeScript();
                createdProbeScript = true;
                if (debug) System.out.println(prg + ": created probe script for " + ip);
            } catch (IOException e) {
                throw e;
            }
        }
        if (!createdGatherScript) {
            try {
                gatherScript = createStatsScript();
                createdGatherScript = true;
                if (debug) System.out.println(prg + ": created stats script for " + ip);
            } catch (IOException e) {
                throw e;
            }
        }
        return (createdProbeScript && createdGatherScript);
    }

    /** Remove the probe and stats gathering scripts from both hosts. */
    public void removeScripts() throws IOException {
        removeLocalScripts();
        removeRemoteScripts();
    }

    /** Remove the scripts from the local host. */
    private void removeLocalScripts() throws IOException {
        File fi;
        fi = new File(tmpdirLocal + probeScript);
        if (fi.exists()) {
            fi.delete();
        }
        fi = new File(tmpdirLocal + gatherScript);
        if (fi.exists()) {
            fi.delete();
        }
    }

    /** Remove the scripts from the remote host. */
    private void removeRemoteScripts() throws IOException {
        String rmcmd = cmdrm + " " + tmpdirRemote;
        if (copiedProbeScript) {
            are.execute(rmcmd + probeScript);
        }
        if (copiedGatherScript) {
            are.execute(rmcmd + gatherScript);
        }
    }

    /** Run the host probing script and retrieve the results. */
    public void probeHost() throws IOException, NumberFormatException {
        String results;
        if (!copiedProbeScript) {
            if (debug) System.out.println(prg + ": probing " + ip + " by copying file");
            try {
                results = are.copyFileToRemote(tmpdirLocal + probeScript, tmpdirRemote);
                if (are.exitCode() == 0) {
                    available = true;
                    copiedProbeScript = true;
                }
            } catch (IOException ioe) {
                available = false;
                throw ioe;
            }
        }
        if (copiedProbeScript) {
            if (debug) System.out.println(prg + ": probing " + ip + " by executing file");
            try {
                results = are.execute(tmpdirRemote + probeScript);
                if (are.exitCode() == 0) processHostInfo(results);
            } catch (IOException ioe) {
                available = false;
                throw ioe;
            }
        }
    }

    /** Rerun the host probing script. */
    public void reprobeHost() throws IOException {
        copiedProbeScript = false;
        probeHost();
    }

    /** Parse the host information, consisting of two lines:
	 * <ol>
	 * <li>The number of CPUs</li>
	 * <li>The total system memory</li>
	 * </ol>
	 * @param info The two line host information
	 * @return <tt>true</tt> if successful, <tt>false</tt> otherwise */
    private void processHostInfo(String info) throws IOException, NumberFormatException {
        String line;
        BufferedReader br = new BufferedReader(new StringReader(info));
        line = br.readLine();
        numcpus = Integer.parseInt(line.replaceAll(" ", ""));
        line = br.readLine();
        memmb = Integer.parseInt(line);
    }

    /** Run the host stats script and retrieve the results. */
    public void getHostStats() throws IOException {
        String results;
        if (!copiedGatherScript) {
            if (debug) System.out.println(prg + ": copying stats script to " + ip);
            try {
                are.copyFileToRemote(tmpdirLocal + gatherScript, tmpdirRemote);
                if (are.exitCode() == 0) {
                    copiedGatherScript = true;
                }
            } catch (IOException ioe) {
                available = false;
                throw ioe;
            }
        }
        if (copiedGatherScript) {
            if (debug) System.out.println(prg + ": executing stats script for " + ip);
            try {
                results = are.execute(tmpdirRemote + gatherScript);
                if (are.exitCode() == 0) processStats(results);
            } catch (IOException ioe) {
                available = false;
                throw ioe;
            }
        }
    }

    /** Parse the stats information. Expected as a single string with new lines
	 * separating sections (or info within a section). Parseable sections at
	 * this point include:
	 * <ol>
	 * <li>System memory</li>
	 * <li>System CPU load</li>
	 * <li>Battery</li>
	 * </ol>
	 * @param info The status information
	 * @return <tt>true</tt> if successful, <tt>false</tt> otherwise */
    private void processStats(String info) throws IOException, NumberFormatException {
        String line;
        BufferedReader br = new BufferedReader(new StringReader(info));
        line = br.readLine();
        avgcpuload = Double.parseDouble(line) / numcpus;
        line = br.readLine();
        memavail = Integer.parseInt(line);
    }

    /** Get the minimal stats information object (for data transfer). */
    public ADEMiniHostStats getADEMiniHostStats() {
        return new ADEMiniHostStats(ip, available, avgcpuload, memavail);
    }

    public void print() {
        System.out.println(toString());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Host Status of: ");
        sb.append(ip);
        if (!available) {
            sb.append(", DOWN");
            sb.append("\nPeriod (ms): ");
            sb.append(period);
            sb.append("\nProbe script: ");
            sb.append(tmpdirRemote);
            sb.append(probeScript);
            sb.append(", ");
            if (copiedProbeScript) sb.append("copied"); else sb.append("not copied");
            sb.append("\nGather script: ");
            sb.append(tmpdirRemote);
            sb.append(gatherScript);
            sb.append(", ");
            if (copiedGatherScript) sb.append("copied"); else sb.append("not copied");
        } else {
            sb.append(", UP");
            sb.append("\nPeriod (ms): ");
            sb.append(period);
            if (copiedProbeScript && copiedGatherScript) {
                sb.append("\nNumber CPUs:  ");
                sb.append(numcpus);
                sb.append(", avg. load: ");
                sb.append(avgcpuload);
                sb.append("\nTotal memory: ");
                sb.append(memmb);
                sb.append(", free: ");
                sb.append(memavail);
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
