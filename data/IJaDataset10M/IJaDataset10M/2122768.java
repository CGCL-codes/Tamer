package org.tzi.wr;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class LogSender extends Thread implements SleeperListener, ProgressListener {

    ProgressListener mListener;

    String mUrl;

    LogSendable mSendable;

    int mMax;

    public LogSender(String aUrl, LogSendable aSendable, ProgressListener aListener, int aMax) {
        mUrl = aUrl;
        mSendable = aSendable;
        mListener = aListener;
        mMax = aMax;
    }

    private void transmitLog() throws IOException, SecurityException {
        WirelessRope.mConsole.writeLog("LogSender.transmitLog: open " + mUrl);
        StreamConnection streamCon = (StreamConnection) javax.microedition.io.Connector.open(mUrl, Connector.READ_WRITE, true);
        OutputStream out = streamCon.openOutputStream();
        BufferedOutputStream bufOut = new BufferedOutputStream(out, 256);
        WirelessRope.mConsole.writeLog("LogSender.transmitLog: writeLog");
        mSendable.writeLog(bufOut, mMax, this);
        bufOut.flush();
        WirelessRope.mConsole.writeLog("LogSender.transmitLog: close");
        bufOut.close();
        out.close();
        streamCon.close();
        WirelessRope.mConsole.writeLog("LogSender.transmitLog: OK");
    }

    public boolean checkOK() throws IOException {
        WirelessRope.mConsole.writeLog("LogSender.checkOK: open " + mUrl);
        StreamConnection streamCon = (StreamConnection) javax.microedition.io.Connector.open(mUrl, Connector.READ_WRITE, true);
        OutputStream out = streamCon.openOutputStream();
        WirelessRope.mConsole.writeLog("LogSender.checkOK: write command");
        out.write("transferstatus\n".getBytes());
        out.flush();
        out.close();
        WirelessRope.mConsole.writeLog("LogSender.checkOK: read status");
        DataInputStream in = streamCon.openDataInputStream();
        byte[] buf = new byte[1];
        int len = in.read(buf);
        WirelessRope.mConsole.writeLog("LogSender.checkOK: read " + len + " bytes");
        String status = "";
        if (len > 0) {
            status = new String(buf, 0, len);
        } else {
            WirelessRope.mConsole.writeLog("LogSender.checkOK: throw IOEx");
            throw new IOException();
        }
        in.close();
        WirelessRope.mConsole.writeLog("LogSender.checkOK: close");
        streamCon.close();
        if (status.compareTo("A") == 0) {
            WirelessRope.mConsole.writeLog("LogSender.checkOK: OK");
            return true;
        } else {
            WirelessRope.mConsole.writeLog("LogSender.checkOK: NOT OK");
            return false;
        }
    }

    public void run() {
        try {
            transmitLog();
            boolean check = true;
            if (check) {
                mSendable.deleteLog(mMax);
                WirelessRope.mConsole.writeLog("LogSender.run check OK, deleteLog");
                mListener.succeededProgress("Jippie");
            } else {
                WirelessRope.mConsole.writeLog("LogSender.run check NOT OK");
                mListener.abortedProgress("check NOT OK");
            }
        } catch (IOException e) {
            WirelessRope.mConsole.writeLog("LogSender.run IOException");
            mListener.abortedProgress("IOException");
            e.printStackTrace();
        } catch (SecurityException es) {
            WirelessRope.mConsole.writeLog("LogSender.run SecrurityException");
            mListener.abortedProgress("SecurityException");
            es.printStackTrace();
        }
    }

    public void sleeperFinished() {
        WirelessRope.mConsole.writeLog("RopeComm.sleeperFinished INTERRUPT");
    }

    public void madeProgress(int aProgress) {
        mListener.madeProgress(aProgress);
    }

    public void succeededProgress(String aMsg) {
    }

    public void abortedProgress(String aMsg) {
    }
}
