package fiswidgets.fisutils;

import java.io.*;
import java.util.*;
import java.net.*;

/**
  * fisSystemUtils is used to return system information such as
  * disks available, disk space available, free memory, and free swap.
  */
public class FisSystemUtils {

    private static Vector disks = new Vector(0), avail = new Vector(0);

    private static int freeMem = 0, freeSwap = 0;

    /**
     * Get name of localhost
     */
    public static String getLocalHostName() {
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            return "localhost";
        }
        return addr.getHostName();
    }

    /**
     * This static method will return the diskspace available in kilobytes for a given 
     * partition.  If this partition does not exist then it will return -1.
     * @param partition The partition that you want to know about.
     */
    public static int getDiskSpace(String partition) {
        disks.removeAllElements();
        avail.removeAllElements();
        String system = System.getProperty("os.name");
        if (system.equals("Linux")) getLinuxDiskVals(); else if (system.equals("Irix")) getSgiDiskVals(); else if (system.equals("HP-UX")) getHPUXDiskVals(); else return -1;
        int theVal = -1;
        for (int x = 0; x < disks.size(); x++) if (((String) disks.elementAt(x)).equals(partition)) theVal = ((Integer) avail.elementAt(x)).intValue();
        return theVal;
    }

    /**
     * This static method returns the disks that are currently mounted on the system
     */
    public static String[] getDisks() {
        disks.removeAllElements();
        avail.removeAllElements();
        String system = System.getProperty("os.name");
        if (system.equals("Linux")) getLinuxDiskVals(); else if (system.equals("Irix")) getSgiDiskVals(); else if (system.equals("HP-UX")) getHPUXDiskVals(); else return new String[0];
        String[] theDisks = new String[disks.size()];
        for (int x = 0; x < theDisks.length; x++) theDisks[x] = (String) disks.elementAt(x);
        return theDisks;
    }

    private static void getSgiDiskVals() {
        try {
            Process proc = (Runtime.getRuntime()).exec("/usr/sbin/df -k");
            LineNumberReader ln = new LineNumberReader(new InputStreamReader(proc.getInputStream()));
            String line;
            int num = 0, devnum = 0;
            ln.readLine();
            while ((line = ln.readLine()) != null) {
                num++;
                devnum++;
                StringTokenizer stream = new StringTokenizer(line);
                disks.addElement(stream.nextToken());
                for (int i = 0; i < 3; i++) stream.nextToken();
                avail.addElement(new Integer(stream.nextToken()));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void getHPUXDiskVals() {
        String inString = "", tempString;
        try {
            Process proc = (Runtime.getRuntime()).exec("bdf");
            InputStream in = proc.getInputStream();
            StreamTokenizer instream = new StreamTokenizer(new InputStreamReader(in));
            int token, num = 0, devnum = 0;
            instream.wordChars('/', ':');
            do {
                token = instream.nextToken();
                num++;
                if (num > 8) {
                    devnum++;
                    if (devnum % 7 == 4 && token == instream.TT_NUMBER) avail.addElement(new Integer((int) instream.nval)); else if (devnum % 7 == 0) disks.addElement(instream.sval);
                }
            } while (token != instream.TT_EOF);
        } catch (Exception e) {
        }
    }

    private static void getLinuxDiskVals() {
        try {
            Process proc = (Runtime.getRuntime()).exec("df");
            InputStream in = proc.getInputStream();
            StreamTokenizer instream = new StreamTokenizer(new InputStreamReader(in));
            int token, num = 0, devnum = 0;
            instream.wordChars('/', '/');
            do {
                token = instream.nextToken();
                num++;
                if (num > 9) {
                    devnum++;
                    if (devnum % 7 == 4 && token == instream.TT_NUMBER) avail.addElement(new Integer((int) instream.nval)); else if (devnum % 7 == 0) disks.addElement(instream.sval);
                }
            } while (token != instream.TT_EOF);
        } catch (Exception e) {
        }
    }

    /**
     * This static method returns the free memory of the system in bytes
     */
    public static int getFreeMem() {
        String system = System.getProperty("os.name");
        if (system.equals("Linux")) getLinuxMemVals(); else if (system.equals("Irix")) getSgiMemVals(); else return -1;
        return freeMem;
    }

    /**
     * This static method returns the free swap of the system in kilobytes
     */
    public static int getFreeSwap() {
        String system = System.getProperty("os.name");
        if (system.equals("Linux")) getLinuxMemVals(); else if (system.equals("Irix")) getSgiMemVals(); else return -1;
        return freeSwap;
    }

    private static void getSgiMemVals() {
        try {
            Process proc = (Runtime.getRuntime()).exec("/sbin/swap -sb");
            LineNumberReader ln = new LineNumberReader(new InputStreamReader(proc.getInputStream()));
            StringTokenizer stream = new StringTokenizer(ln.readLine());
            String token;
            for (int i = 0; i < 11; i++) stream.nextToken();
            token = stream.nextToken();
            freeSwap = Integer.parseInt(token);
            freeSwap = freeSwap / 2;
            freeMem = -1;
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void getLinuxMemVals() {
        try {
            Process proc = Runtime.getRuntime().exec("free -ko");
            InputStream in = proc.getInputStream();
            StreamTokenizer stream = new StreamTokenizer(new InputStreamReader(in));
            int token, numnums = 0;
            do {
                token = stream.nextToken();
                if (token == stream.TT_NUMBER) {
                    numnums++;
                    if (numnums == 3) freeMem = (int) stream.nval; else if (numnums == 9) freeSwap = (int) stream.nval;
                }
            } while (token != stream.TT_EOF);
        } catch (Exception e) {
        }
    }

    /**
     * feeble guess as to system endian-ness
     * Note: if FisProperty FORCE_ENDIAN is set to "big" or "little"
     *       this method will use that information to determine endianess.
     *
     * @return  boolean  true = big endian platform, else false
     */
    public static boolean isBigEndian() {
        if (FisProperties.hasProperty("FORCE_ENDIAN")) {
            if (System.getProperty("FORCE_ENDIAN").equals("big")) return true;
            if (System.getProperty("FORCE_ENDIAN").equals("little")) return false;
        }
        String arch = System.getProperty("os.arch");
        if (arch.endsWith("86")) return false;
        return true;
    }

    /**
     * Returns a temporary (non-existent) filename by adding random chars
     * to a stemname passed as parameter.
     * kind of like some unix tempnam() functions
     * @param String f  is the basename for the temp file.  It can have
     * some number of 'X' chars at the end.  These will be replaced with
     * random chars.  If it does not have at least two X chars at the end,
     * five random chars will be appended.
     * @return returns a string that is a unique, non-existent filename
    */
    public static String getTempName(String f) throws IOException {
        int n;
        if (!f.endsWith("XX")) f += "XXXXX";
        for (n = 0; f.endsWith("X"); n++) f = f.substring(0, f.length() - 1);
        RandomChars rc = new RandomChars();
        String name;
        int t = 0;
        do {
            name = f + rc.getRandomChars(n);
            t++;
        } while ((new File(name)).exists() && (t < 62));
        if (t == 62) throw new IOException("Could not create unique name from " + f);
        return name;
    }

    /**
     * This method sleeps for a given number of milliseconds
     * @param
     *   long ms -- number of milliseconds to sleep
     */
    public static void Sleep(long ms) {
        Thread t;
        t = new Thread();
        try {
            t.sleep(ms);
        } catch (InterruptedException ex) {
            ;
        }
    }

    public static void main(String[] args) {
        try {
            FisProperties.loadFisProperties(FisProperties.FISPROPERTIES_SYSTEM_OR_USER);
        } catch (FisPropertyLoadException ex) {
            System.out.println("Warning: could not load fisproperties.");
        }
        if (FisSystemUtils.isBigEndian()) System.out.println("This system appears to be big endian."); else System.out.println("This system appears to be little endian.");
        System.out.println("System is : " + System.getProperty("os.name"));
        System.out.println("free memory is : " + FisSystemUtils.getFreeMem() + " bytes");
        System.out.println("free swap is   : " + FisSystemUtils.getFreeSwap() + " Kbytes");
        String[] thedisks = FisSystemUtils.getDisks();
        for (int x = 0; x < thedisks.length; x++) {
            int space = FisSystemUtils.getDiskSpace(thedisks[x]);
            System.out.println("Space on " + thedisks[x] + " is : " + space + " kb");
        }
    }
}
