package net.sourceforge.ivi.core.iviSimIfc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import net.sourceforge.ivi.core.IBuffer;
import net.sourceforge.ivi.core.iviException;
import net.sourceforge.ivi.core.iviJBuffer;
import net.sourceforge.ivi.core.iviLogPoint;
import net.sourceforge.ivi.core.ddb.IDDBDesignObj;
import net.sourceforge.ivi.core.ddb.IDDBProvider;
import net.sourceforge.ivi.core.ddb.IDDBSignalObj;
import net.sourceforge.ivi.core.dfaa.IDFAA;
import net.sourceforge.ivi.core.dfio.IDFIO;
import net.sourceforge.ivi.core.ivw_filter.iviIvwDFIO;
import net.sourceforge.ivi.core.ivw_filter.iviIvwDFIOException;
import net.sourceforge.ivi.core.remoteDDB.iviRemoteDDBProvider;
import net.sourceforge.ivi.core.remoteDDB.iviRemoteDDBProviderXmitIfc;

public class iviSimIfc {

    public static final String SimStart = "iviSimIfc.SimStart";

    public static final String SimReady = "iviSimIfc.SimReady";

    public static final String SimClose = "iviSimIfc.SimClose";

    public iviSimIfc() {
        d_simFinished = false;
        d_port = -1;
        d_eventListeners = new Vector();
        d_lp = new iviLogPoint("sim_ifc");
    }

    protected void finalize() {
    }

    public void addSimEventListener(iviSimIfcEventListener l) {
        d_eventListeners.add(l);
    }

    public void removeSimEventListener(iviSimIfcEventListener l) {
        d_eventListeners.remove(l);
    }

    /****************************************************************
     * StartServer()
     ****************************************************************/
    public void StartServer() throws iviException {
        d_server = null;
        try {
            d_server = new ServerSocket(0);
        } catch (IOException e) {
            throw new iviException("Cannot start server " + e.getMessage());
        }
        d_port = d_server.getLocalPort();
        d_monitor = new ServerMonitor(d_server);
    }

    /****************************************************************
     * DDBProviderTransport
     ****************************************************************/
    private class DDBProviderTransport implements iviRemoteDDBProviderXmitIfc {

        public DDBProviderTransport(iviCommChannelEndPoint endPoint) {
            d_endPoint = endPoint;
        }

        public void Xmit(byte data[]) {
            IBuffer buffer = new iviJBuffer(data, 0, data.length);
            d_endPoint.xmit(buffer);
        }

        private iviCommChannelEndPoint d_endPoint;
    }

    /****************************************************************
     * Connect()
     ****************************************************************/
    public boolean Connect(int ms_wait) throws iviException {
        IBuffer msg = null;
        Socket sock = null;
        if (d_server != null) {
            sock = d_monitor.wait_connect(ms_wait);
            if (sock == null) {
                return false;
            }
        } else {
            InetAddress localhost = null;
            try {
                localhost = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                throw new iviException("Cannot lookup localhost: " + e.getMessage());
            }
            try {
                sock = new Socket(localhost, d_port);
            } catch (IOException e) {
                throw new iviException("cannot connect socket");
            }
        }
        try {
            InputStream in_stream = sock.getInputStream();
            OutputStream out_stream = sock.getOutputStream();
            d_commChannelMgr = new iviCommChannelMgr(in_stream, out_stream);
        } catch (IOException e) {
            throw new iviException("cannot get streams...");
        }
        d_commChannelMgr.Start();
        d_simCtrlChan = new SimulationMonitor();
        d_commChannelMgr.AddListener(d_simCtrlChan, iviCommChannels.SimCtrlChannel);
        d_simPluginChan = new iviCommChannelEndPoint();
        d_commChannelMgr.AddListener(d_simPluginChan, iviCommChannels.SimPluginChannel);
        iviCommChannelEndPoint ddb_ep = new DDBCommEndPoint();
        d_commChannelMgr.AddListener(ddb_ep, iviCommChannels.SimDDBProviderChannel);
        d_traceCtrlChan = new iviCommChannelEndPoint();
        d_commChannelMgr.AddListener(d_traceCtrlChan, iviCommChannels.SimTraceControlChannel);
        d_ddbTransport = new DDBProviderTransport(ddb_ep);
        d_ddbProvider = new iviRemoteDDBProvider(d_ddbTransport);
        if ((msg = d_simPluginChan.recv()) == null) {
            throw new iviException("Problem while connecting");
        }
        int word1 = msg.ReadUint32();
        String shm_filename = msg.ReadString();
        d_pluginShmIfc = new iviSimPluginShmIfc(new File(shm_filename));
        System.out.println("wavefile: " + d_pluginShmIfc.getWaveFilename());
        try {
            d_dfio = new iviIvwDFIO(new File(d_pluginShmIfc.getWaveFilename()));
        } catch (iviIvwDFIOException e) {
            System.out.println("problem opening wave file: " + e.getMessage());
        }
        d_simDFAA = new iviSimDFAA(this);
        if ((msg = d_simPluginChan.recv()) == null) {
            throw new iviException("Problem while connecting");
        }
        int msg_id = msg.ReadUint32();
        String err_msg;
        switch(msg_id) {
            case iviSimPluginChannelMsgs.DesignLoaded:
                break;
            case iviSimPluginChannelMsgs.CloseResp:
                err_msg = "Simulator exited";
                throw new iviException(err_msg);
            default:
                err_msg = "Unknown response while waiting for " + "DesignLoaded msg " + msg_id;
                throw new iviException(err_msg);
        }
        return true;
    }

    /****************************************************************
     * getCommChannelMgr()
     ****************************************************************/
    public iviCommChannelMgr getCommChannelMgr() {
        return d_commChannelMgr;
    }

    /****************************************************************
     * getDDBProvider()
     ****************************************************************/
    public IDDBProvider getDDBProvider() {
        return d_ddbProvider;
    }

    public IDFIO getDFIO() {
        return d_dfio;
    }

    public IDFAA getSimDFAA() {
        return d_simDFAA;
    }

    public int getPort() {
        return d_port;
    }

    /****************************************************************
     * RunSimulation()
     ****************************************************************/
    public synchronized void RunSimulation(long run_len) {
        IBuffer buffer = new iviJBuffer();
        if (run_len < 0) {
            buffer.WriteUint32(iviSimCtrlChannelMsgs.RunStart);
            buffer.WriteUint32((int) run_len);
        } else {
            buffer.WriteUint32(iviSimCtrlChannelMsgs.RunInterval);
            buffer.WriteUint32((int) run_len);
        }
        d_simCtrlChan.xmit(buffer);
        d_simRunning = true;
        signalSimEvent(iviSimIfcEventListener.SimEvent_RunStart);
    }

    /****************************************************************
     * StopSimulation()
     ****************************************************************/
    public synchronized void StopSimulation() {
        IBuffer buffer = new iviJBuffer();
        buffer.WriteUint32(iviSimCtrlChannelMsgs.StopRequest);
        d_simCtrlChan.xmit(buffer);
    }

    /****************************************************************
     * isSimulationRunning()
     ****************************************************************/
    public synchronized boolean isSimulationRunning() {
        return d_simRunning;
    }

    /****************************************************************
     * isSimulationFinished()
     ****************************************************************/
    public synchronized boolean isSimulationFinished() {
        return d_simFinished;
    }

    /****************************************************************
     * getSimTime()
     ****************************************************************/
    public long getSimTime() {
        return d_pluginShmIfc.getSimTime();
    }

    /****************************************************************
     * getFormattedSimTime()
     ****************************************************************/
    public String getFormattedSimTime(boolean max_units) {
        String ret = "";
        ret = getSimTime() + " ps";
        return ret;
    }

    /****************************************************************
     * getSimTimeUnit()
     ****************************************************************/
    public int getSimTimeResUnit() {
        return d_pluginShmIfc.getSimTimeResUnit();
    }

    /****************************************************************
     * getSimTimeResExp()
     ****************************************************************/
    public int getSimTimeResExp() {
        return d_pluginShmIfc.getSimTimeResExp();
    }

    /****************************************************************
     * EnableTrace()
     ****************************************************************/
    public void EnableTrace(IDDBSignalObj signal, boolean enable) throws iviException {
        d_lp.enter(1, "EnableTrace(" + signal.getStrProp(IDDBDesignObj.StrProp_FullName) + ", " + enable + ")");
        IBuffer buffer = new iviJBuffer();
        buffer.WriteUint32(iviSimCtrlChannelMsgs.TraceControl_Enable);
        buffer.WriteString(signal.getStrProp(IDDBDesignObj.StrProp_FullName));
        buffer.WriteUint32((enable) ? 1 : 0);
        d_traceCtrlChan.xmit(buffer);
        if ((buffer = d_traceCtrlChan.recv()) == null) {
            throw new iviException("problem receiving trace-message ack");
        }
        int status = buffer.ReadUint32();
        if (status != 0) {
            String msg = buffer.ReadString();
            throw new iviException(msg);
        }
        d_lp.log(1, "pre-S: " + d_dfio.getTraces().length);
        d_dfio.sync();
        d_lp.log(1, "post-sync: " + d_dfio.getTraces().length);
        d_lp.leave(1, "EnableTrace(" + signal.getStrProp(IDDBDesignObj.StrProp_FullName) + ")");
    }

    /****************************************************************
     * SimulationMonitor
     ****************************************************************/
    class SimulationMonitor extends iviCommChannelEndPoint {

        public SimulationMonitor() {
            d_modeBlocking = false;
        }

        public synchronized void setMonitorMode(boolean blocking) {
            d_modeBlocking = blocking;
        }

        public void ReceiveData(int index, IBuffer data) {
            if (d_modeBlocking) {
                super.ReceiveData(index, data);
            } else {
                int msg = data.ReadUint32();
                switch(msg) {
                    case iviSimCtrlChannelMsgs.StopResponse:
                        {
                            int reason = data.ReadUint32();
                            d_simRunning = false;
                            if (reason == iviSimCtrlChannelMsgs.StopReason_Finish) {
                                d_simFinished = true;
                                signalSimEvent(iviSimIfcEventListener.SimEvent_RunStop);
                            } else {
                                signalSimEvent(iviSimIfcEventListener.SimEvent_SimFinish);
                            }
                        }
                        break;
                    default:
                        System.out.println("Error: unknown sim-ctrl " + "message: " + msg);
                        break;
                }
            }
        }

        private boolean d_modeBlocking;
    }

    /****************************************************************
     * ServerMonitor
     ****************************************************************/
    class ServerMonitor implements Runnable {

        public ServerMonitor(ServerSocket sock) {
            d_srvSock = sock;
            d_thread = new Thread(this);
            d_thread.start();
        }

        public Socket wait_connect(int ms_wait) {
            if (d_socket != null) {
                return d_socket;
            }
            try {
                if (ms_wait < 0) {
                    d_thread.join();
                } else {
                    d_thread.join(ms_wait);
                }
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            return d_socket;
        }

        public void run() {
            try {
                d_socket = d_srvSock.accept();
            } catch (IOException e) {
                System.out.println("Server.accept error: " + e.getMessage());
            }
        }

        ServerSocket d_srvSock;

        Thread d_thread;

        Socket d_socket;
    }

    /****************************************************************
     * PluginProcMonitor
     ****************************************************************/
    class PluginProcMonitor implements Runnable {

        /************************************************************
         * PluginProcMonitor()
         ************************************************************/
        public PluginProcMonitor() {
            new Thread(this);
        }

        /************************************************************
         * run()
         ************************************************************/
        public void run() {
            d_running = true;
            d_running = false;
        }

        /************************************************************
         * isRunning()
         ************************************************************/
        public synchronized boolean isRunning() {
            return d_running;
        }

        boolean d_running;

        int d_status;
    }

    /****************************************************************
     * SimPluginListener
     ****************************************************************/
    class SimPluginListener extends iviCommChannelEndPoint {

        public synchronized void ReceiveData(int index, IBuffer data) {
        }
    }

    private void signalSimEvent(int event_id) {
        for (int i = 0; i < d_eventListeners.size(); i++) {
            iviSimIfcEventListener l = (iviSimIfcEventListener) d_eventListeners.elementAt(i);
            l.simEventOccurred(event_id);
        }
    }

    private class DDBCommEndPoint extends iviCommChannelEndPoint {

        public DDBCommEndPoint() {
        }

        public void RegisterChannelMgr(iviCommChannelMgr mgr, int idx) {
            super.RegisterChannelMgr(mgr, idx);
        }

        public synchronized void ReceiveData(int idx, IBuffer buffer) {
            byte data[] = buffer.GetData();
            d_ddbProvider.dataReceived(data);
        }

        public synchronized void xmit(IBuffer buffer) {
            super.xmit(buffer);
        }
    }

    /****************************************************************
     * Private Data
     ****************************************************************/
    private iviCommChannelMgr d_commChannelMgr;

    private SimulationMonitor d_simCtrlChan;

    private iviCommChannelEndPoint d_simPluginChan;

    private iviCommChannelEndPoint d_traceCtrlChan;

    private boolean d_isServer;

    private boolean d_simRunning;

    private boolean d_simFinished;

    private IDFIO d_dfio;

    private iviSimPluginShmIfc d_pluginShmIfc;

    private int d_port;

    private ServerSocket d_server;

    private ServerMonitor d_monitor;

    private DDBProviderTransport d_ddbTransport;

    private iviRemoteDDBProvider d_ddbProvider;

    private Vector d_eventListeners;

    private iviSimDFAA d_simDFAA;

    private iviLogPoint d_lp;
}
