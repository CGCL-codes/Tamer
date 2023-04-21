package net.nutss.stunt;

import java.io.*;
import net.nutss.STUNTSelector;
import java.net.*;

public class STUNTServerSocket extends Thread {

    public STUNTCont con = null;

    ServerSocket s = null;

    STUNTSelector sel = null;

    private Object objLock = new Object();

    public STUNTServerSocket(ServerSocket s, STUNTCont con, STUNTSelector sel) {
        this.s = s;
        this.con = con;
        this.sel = sel;
        if (sel != null) sel.add(this);
    }

    Exception er = null;

    volatile boolean stop = false;

    int il = 0;

    volatile Socket accepeted = null;

    public boolean hasData() {
        synchronized (objLock) {
            return accepeted != null;
        }
    }

    public boolean isError() {
        return er != null;
    }

    public void stopSelect() {
        stop = true;
    }

    public void run() {
        stop = false;
        er = null;
        try {
            il = s.getSoTimeout();
            s.setSoTimeout(25);
        } catch (Exception e) {
            er = e;
            stop = true;
            return;
        }
        while (!stop && er == null) {
            if (s == null || !s.isBound()) {
                stop = true;
                return;
            }
            try {
                if (hasData()) this.sleep(100); else {
                    Socket s2 = s.accept();
                    if (s2 != null) {
                        synchronized (objLock) {
                            accepeted = s2;
                        }
                    }
                }
            } catch (SocketTimeoutException et) {
            } catch (Exception e) {
                er = e;
            }
        }
        try {
            s.setSoTimeout(il);
        } catch (Exception e) {
        }
    }

    public STUNTSocket accept() throws IOException {
        Socket s2 = getAccept();
        if (s2 == null) return null; else {
            STUNTSocket s3 = new STUNTSocket(s2, this.con, this.sel);
            s3.start();
            return s3;
        }
    }

    public Socket getAccept() throws IOException {
        Socket s2 = null;
        synchronized (objLock) {
            s2 = accepeted;
            accepeted = null;
        }
        return s2;
    }

    public void close() throws IOException {
        stopSelect();
        if (s != null && !s.isClosed()) s.close();
        if (sel != null) sel.remove(this);
        synchronized (objLock) {
            if (accepeted != null) accepeted.close();
            accepeted = null;
        }
    }
}
