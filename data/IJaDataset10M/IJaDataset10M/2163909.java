package sztaki.trace.impl;

import java.io.*;
import java.net.*;

public class Trace2Png {

    public static native int t2pNewTrace();

    public static native void t2pDeleteTrace(int i);

    public static native int t2pMemoryUsage(int i);

    public static native void t2pProcessLine(int i, String s);

    public static native void t2pHandleEventPairs(int i);

    public static native void t2pExportTrace(int i, String s, int j, int k, int l, int i1, int j1, int k1);

    public static native void t2pSort(int i, int j);

    public static native int t2pGetMinSec(int i);

    public static native int t2pGetMinUSec(int i);

    public static native int t2pGetMaxSec(int i);

    public static native int t2pGetMaxUSec(int i);

    public static native int t2pGetLSec(int i);

    public static native int t2pGetLUSec(int i);

    public static native int t2pGetRSec(int i);

    public static native int t2pGetRUSec(int i);

    public static native int t2pGetAxisOrigoX(int i);

    public static native int t2pGetAxisOrigoY(int i);

    public static native void t2pForgetEvents(int i, int j, int k);

    public static native int t2pGetNumberOfProcesses(int i);

    public static native String t2pGetProcessName(int i, int j);

    public static native int t2pGetProcessYCoord(int i, int j);

    public static native void t2pHideProcess(int i, int j);

    public static native void t2pShowProcess(int i, int j);

    public static native boolean t2pIsProcessHidden(int i, int j);

    public static native boolean t2pExportStatistic(int i, String s, int j, int k, int l);

    public static native int t2pGetSRMatrixByte(int i, int j, int k);

    public static native int t2pGetScaleByteYCoord(int i, int j);

    public static native int t2pGetScaleXCoord(int i);

    public static native int t2pGetNumberOfMigrations(int i, int j);

    public static native String t2pGetHostNameAfterMigration(int i, int j, int k);

    public static native int t2pGetMigrationSec(int i, int j, int k);

    public static native int t2pGetMigrationUSec(int i, int j, int k);

    public Trace2Png() {
        url = null;
        bytes_read = 0;
        last_contentLenght = 0;
        trace = 0;
        sortby = 1;
        Runtime.getRuntime().loadLibrary((new PropertyLoader()).getPropertyFromTheSameDir("Trace2Png.properties", "lib.name"));
    }

    public boolean setTraceUrl(String s) {
        try {
            url = new URL(s);
            istream = url.openConnection();
            last_contentLenght = 0;
            reader = new BufferedReader(new InputStreamReader(istream.getInputStream()));
        } catch (MalformedURLException malformedurlexception) {
            System.out.println("Trace2Png: MalformedURLException: " + s);
            return false;
        } catch (IOException ioexception) {
            System.out.println("Trace2Png: IOException: " + s);
            return false;
        }
        trace = t2pNewTrace();
        return true;
    }

    public int readLines() {
        int i = 0;
        if (istream == null) return 0;
        try {
            String s1;
            if ((new String("http")).compareTo(url.getProtocol()) == 0) {
                istream = url.openConnection();
                if (last_contentLenght != istream.getContentLength()) {
                    last_contentLenght = istream.getContentLength();
                    istream = url.openConnection();
                    istream.setRequestProperty("Range", "bytes=" + Integer.toString(bytes_read) + "-");
                    System.out.println("Trace2Png: ContentLength: " + Integer.toString(istream.getContentLength()));
                    reader = new BufferedReader(new InputStreamReader(istream.getInputStream()));
                    String s;
                    while ((s = reader.readLine()) != null) {
                        bytes_read = bytes_read + s.length() + 1;
                        t2pProcessLine(trace, s);
                        i++;
                    }
                }
            } else {
                while ((s1 = reader.readLine()) != null) {
                    bytes_read = bytes_read + s1.length() + 1;
                    t2pProcessLine(trace, s1);
                    i++;
                }
            }
            t2pHandleEventPairs(trace);
            t2pSort(trace, sortby);
        } catch (IOException ioexception) {
            System.out.println("Trace2Png: IOException !!!");
        }
        return i;
    }

    public void exportTrace(String s, int i, int j, int k, int l, int i1, int j1) {
        if (trace == 0) {
            return;
        } else {
            t2pExportTrace(trace, s, i, j, k, l, i1, j1);
            return;
        }
    }

    public void deleteTrace() {
        if (trace == 0) {
            return;
        } else {
            t2pDeleteTrace(trace);
            System.out.println("Trace2Png: Memory usage: " + t2pMemoryUsage(trace) + " byte(s)");
            trace = 0;
            return;
        }
    }

    public int getMinSec() {
        if (trace == 0) return -1; else return t2pGetMinSec(trace);
    }

    public int getMinUSec() {
        if (trace == 0) return -1; else return t2pGetMinUSec(trace);
    }

    public int getMaxSec() {
        if (trace == 0) return -1; else return t2pGetMaxSec(trace);
    }

    public int getMaxUSec() {
        if (trace == 0) return -1; else return t2pGetMaxUSec(trace);
    }

    public int getAxisOrigoX() {
        if (trace == 0) return -1; else return t2pGetAxisOrigoX(trace);
    }

    public int getAxisOrigoY() {
        if (trace == 0) return -1; else return t2pGetAxisOrigoY(trace);
    }

    public int getLSec() {
        if (trace == 0) return -1; else return t2pGetLSec(trace);
    }

    public int getLUSec() {
        if (trace == 0) return -1; else return t2pGetLUSec(trace);
    }

    public int getRSec() {
        if (trace == 0) return -1; else return t2pGetRSec(trace);
    }

    public int getRUSec() {
        if (trace == 0) return -1; else return t2pGetRUSec(trace);
    }

    public void sort(int i) {
        if (trace == 0) {
            return;
        } else {
            sortby = i;
            t2pSort(trace, i);
            return;
        }
    }

    public void forgetEvents(int i, int j) {
        if (trace == 0) {
            return;
        } else {
            t2pForgetEvents(trace, i, j);
            return;
        }
    }

    public boolean collect() {
        return readLines() > 0;
    }

    public int getNumberOfProcesses() {
        if (trace == 0) return -1; else return t2pGetNumberOfProcesses(trace);
    }

    public String getProcessName(int i) {
        if (trace == 0) return null; else return t2pGetProcessName(trace, i);
    }

    public int getProcessYCoord(int i) {
        if (trace == 0) return -1; else return t2pGetProcessYCoord(trace, i);
    }

    public void hideProcess(int i) {
        if (trace == 0) {
            return;
        } else {
            t2pHideProcess(trace, i);
            System.out.println("hide" + i);
            return;
        }
    }

    public void showProcess(int i) {
        if (trace == 0) {
            return;
        } else {
            t2pShowProcess(trace, i);
            return;
        }
    }

    public boolean isProcessHidden(int i) {
        if (trace == 0) return false; else return t2pIsProcessHidden(trace, i);
    }

    public boolean exportStatistic(String s, int i, int j, int k) {
        if (trace == 0) return false; else return t2pExportStatistic(trace, s, i, j, k);
    }

    public int getSRMatrixByte(int i, int j) {
        if (trace == 0) return -1; else return t2pGetSRMatrixByte(trace, i, j);
    }

    public int getScaleByteYCoord(int i) {
        if (trace == 0) return -1; else return t2pGetScaleByteYCoord(trace, i);
    }

    public int getScaleXCoord() {
        if (trace == 0) return -1; else return t2pGetScaleXCoord(trace);
    }

    public int getNumberOfMigrations(int i) {
        if (trace == 0) return -1; else return t2pGetNumberOfMigrations(trace, i);
    }

    public String getHostNameAfterMigration(int i, int j) {
        if (trace == 0) return null; else return t2pGetHostNameAfterMigration(trace, i, j);
    }

    public int getMigrationSec(int i, int j) {
        if (trace == 0) return -1; else return t2pGetMigrationSec(trace, i, j);
    }

    public int getMigrationUSec(int i, int j) {
        if (trace == 0) return -1; else return t2pGetMigrationUSec(trace, i, j);
    }

    public static final int SORTBYCOMMUNICATION = 1;

    public static final int SORTBYPROCNAME = 2;

    public static final int SORTBYHOSTNAME = 3;

    private URL url;

    private URLConnection istream;

    private int bytes_read;

    private int last_contentLenght;

    private int trace;

    private int sortby;

    private BufferedReader reader;
}
