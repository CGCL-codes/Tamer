package hypercast.DT;

import hypercast.*;
import hypercast.adapters.*;
import hypercast.util.XmlUtil;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * This class implements GNP mechanism to get logical address for a DT
 * node. It measures the latancies to a set of landmark nodes, and
 * calculates the logical address based on the measurements. This version 
 * use normal UDP packets to measure latancy.
 * 
 * @author HyperCast Team
 * @author Haiyong Wang
 * @author Jianping Wang
 * @version 3.0, Jan. 08, 2004 

 * This version don't use raw ping command to obtain delay measurement,
 * because many PlanetLab hosts don't support ping and traceroute.
 */
class GNP_DT implements I_AdapterCallback {

    /** the total number of probes*/
    static final int probeNumber = 5;

    /** the interval between probes, in terms of mili-second*/
    static final int probeInterval = 3000;

    /** the maximal waiting time of a probe */
    static final int maxReplyTime = 4000;

    /** basic waiting time for starting GNP probe */
    static final int BasicWaitTime = 50;

    /** range of time for differing GNP probe begin time*/
    static final int RangeOfStarttime = 10;

    /** The property prefix, it can be changed by constructors. */
    protected static String PROPERTY_PROTO_PREFIX = "/Public/Node/DTBuddyList/";

    /** Attribute name in file DynamicCacheFile which stores coordinates*/
    static final String InitialCoordPrefix = ".InitialCoords";

    /** Attribute which specifies the name of cache file in the configuration file*/
    static final String ATTR_CACHEFILE = "CacheFile";

    /** The name of cache file  which stores coordinates*/
    private String DynamicCacheFile;

    /** The name of temporary file TemporaryCopyFile which only exists for a shot time*/
    static final String TemporaryCopyFile = ".temp_buffer_file";

    /** the minimum change needed to modify DT logical address */
    static final double updateThreshold = 20;

    /** the update frequencey */
    static final int updateInterval = 400000;

    /** the adapter used for sending/receving GNP probes */
    private I_UnicastAdapter adapter;

    /** the landmarks */
    private I_UnderlayAddress[] landmarks;

    /** the local physical address */
    private I_PhysicalAddress myPhyAddress;

    /** add some randomness between probes to avoid overload a landmark */
    private Random rand = new Random();

    /**
     * the measurement array from this node
     * It should be of # of landmarks * probeNumber dimension
     */
    private long[][] measurements;

    /** config object */
    private HyperCastConfig config;

    /** dimension is equal the # of landmarks */
    private int[] completedProbes;

    private long[] startTime;

    private float[][] lmCoor;

    I_Node dtNode;

    boolean initialProbe;

    boolean probeDone;

    float[] coor;

    long GNP_time, GNP_start_time;

    /** The object which stories the coordinates obtained previously. */
    Properties Cache_Prop = new Properties();

    /**
	 * Constructor.
	 * 
	 * @param	c		an HyperCastConfig object
	 * @param node		Overlay node this object serves
	 * @param prefix	property prefix
	 */
    public GNP_DT(HyperCastConfig c, I_Node node, String prefix) {
        dtNode = node;
        config = c;
        if (prefix != null) PROPERTY_PROTO_PREFIX = prefix;
        AdapterFactory adapterF = new AdapterFactory();
        adapter = adapterF.createAdapter(config, null, "NodeAdapter", null);
        myPhyAddress = adapter.createPhysicalAddress();
        int lmNum = config.getNonNegativeIntAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Coords/USE_LM/LandmarkNum"));
        System.out.println("GNP_DT: property string is:" + PROPERTY_PROTO_PREFIX + "Coords/USE_LM/LandmarkNum" + ", lmNum=" + lmNum);
        if (lmNum < 3) {
            throw new HyperCastFatalRuntimeException(" not enough landmarks to perform the measurement");
        }
        landmarks = new I_UnderlayAddress[lmNum];
        lmCoor = new float[lmNum][2];
        for (int i = 0; i < lmNum; i++) {
            String addrType = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Coords/USE_LM/Landmark[" + (i + 1) + "]/UnderlayAddress"));
            String s = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Coords/USE_LM/Landmark[" + (i + 1) + "]/UnderlayAddress/" + addrType));
            if (null == s) {
                throw new HyperCastFatalRuntimeException("could not read the physical address of landmark " + i);
            }
            landmarks[i] = adapter.createUnderlayAddress(s);
            if (landmarks[i] == null) {
                throw new HyperCastFatalRuntimeException("Exception when creating the physical address of landmark" + i);
            }
            s = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Coords/USE_LM/Landmark[" + (i + 1) + "]/Coordinates"));
            if (null == s) {
                throw new HyperCastFatalRuntimeException("could not read the coordinates of landmark " + i);
            }
            StringTokenizer token = new StringTokenizer(s, " ,\t\n\r\f");
            lmCoor[i][0] = (Float.valueOf(token.nextToken())).floatValue();
            lmCoor[i][1] = (Float.valueOf(token.nextToken())).floatValue();
        }
        measurements = new long[lmNum][probeNumber];
        adapter.setCallback(this);
        adapter.Start();
        coor = new float[2];
        System.out.println("Probe Agent starts at " + adapter.createPhysicalAddress().toString());
    }

    /**
     * This is a blocking call, which will return only when the logical address
     * is obtained.
     */
    DT_LogicalAddress getLogicalAddress() {
        initialProbe = true;
        probeDone = false;
        startProbe();
        waitUntilDone();
        return new DT_LogicalAddress((int) coor[0], (int) coor[1]);
    }

    /**
     * This is a non-blocking call, it firstly try to read record InitialCoordPrefix
     * in the file DynamicCacheFile. If the file DynamicCacheFile exists and the 
     * record is found, it return obtained coordinates immediatelly. Otherwise it 
     * creates a initial logical address for the node, and sets a short-time timer
     * and returns immediately. 
     * When the timer expires, GNP_DT will probe and get a new logical address
     * and update DT_Node's logical address if the difference between new address
     * and old address surpasses the predefined threshold. The new logical address
     * will also be written into file DynamicCacheFile.
     */
    public int[] getLogicalAddress_nonblock() {
        FileInputStream cache_finputstream = null;
        float[] defaultCoords = new float[2];
        int[] Coords_int = new int[2];
        int dynamic_coodrs_flag;
        Coords_int[0] = 0;
        Coords_int[1] = 0;
        dynamic_coodrs_flag = 0;
        DynamicCacheFile = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + ATTR_CACHEFILE));
        try {
            cache_finputstream = new FileInputStream(DynamicCacheFile);
            dynamic_coodrs_flag++;
        } catch (FileNotFoundException e) {
            System.err.println("#### Config: dynamic coordinates property file not found!");
            cache_finputstream = null;
        }
        if (dynamic_coodrs_flag > 0) {
            try {
                Cache_Prop.load(cache_finputstream);
                String initialcoords_str = (String) Cache_Prop.getProperty(PROPERTY_PROTO_PREFIX + InitialCoordPrefix, null);
                if (initialcoords_str == null) {
                    System.out.println("No dynamical Coordinates is found!");
                } else {
                    try {
                        Coords_int[0] = Integer.parseInt(initialcoords_str.substring(0, initialcoords_str.indexOf(",")).trim());
                        Coords_int[1] = Integer.parseInt(initialcoords_str.substring(initialcoords_str.indexOf(",") + 1).trim());
                        System.out.println("Obtain from dynamic coordinates file: Coords_int[0]=" + Coords_int[0] + ", Coords_int[1]= " + Coords_int[1]);
                        dynamic_coodrs_flag++;
                    } catch (NumberFormatException excpt) {
                        System.err.println("Format of coords is wrong:" + excpt);
                    }
                }
            } catch (IOException e) {
                System.err.println("#### Config: dynamic coordinates property file load error: " + e.getMessage());
            }
        }
        if (dynamic_coodrs_flag < 2) {
            Coords_int = createInitialCoords();
            defaultCoords[0] = (float) Coords_int[0];
            defaultCoords[1] = (float) Coords_int[1];
            set_default_coords(defaultCoords);
            initialProbe = false;
            adapter.setTimer(new Integer(landmarks.length * 2 + 2), BasicWaitTime + (int) (RangeOfStarttime * rand.nextDouble()));
        }
        update_cached_coordinates(Coords_int[0], Coords_int[1]);
        try {
            if (!(cache_finputstream == null)) {
                cache_finputstream.close();
            }
        } catch (IOException ee) {
            System.err.println("IO exception in FileInputStream:" + ee);
        }
        return Coords_int;
    }

    /**
     * Specify default logical addresses for DT_Node 
     */
    public void set_default_coords(float[] default_coords) {
        coor[0] = default_coords[0];
        coor[1] = default_coords[1];
    }

    /**
	 * Block until new coordinates are created.
	 *
	 */
    synchronized void waitUntilDone() {
        while (probeDone == false) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * This is a non-blocking call, which can be triggered periodically to update
     * the DT Address. A threshhold can be configured so that only when there is
     * a significant change in the address, will the DT_Node's address is modified
     */
    void startPeriodicProbe() {
        initialProbe = false;
        adapter.setTimer(new Integer(landmarks.length * 2 + 1), updateInterval + (int) (maxReplyTime * rand.nextDouble()));
    }

    /**
	 * Start the process of probing and calculating new coordinates.
	 *
	 */
    public void startProbe() {
        GNP_start_time = System.currentTimeMillis();
        System.out.println("GNP probe starts.");
        for (int i = 0; i < landmarks.length; i++) for (int j = 0; j < probeNumber; j++) measurements[i][j] = -1;
        completedProbes = new int[landmarks.length];
        startTime = new long[landmarks.length];
        for (int i = 0; i < landmarks.length; i++) completedProbes[i] = 0;
        for (int i = 0; i < landmarks.length; i++) {
            adapter.setTimer(new Integer(i * 2), (int) (100 * rand.nextDouble()));
        }
        adapter.setTimer(new Integer(landmarks.length * 2), (probeInterval + maxReplyTime) * probeNumber);
    }

    /** Handle the message received from the Landmark applications. */
    public void messageArrivedFromAdapter(I_Message msg) {
        GNP_Message gmsg = (GNP_Message) msg;
        int hostIndex;
        if (gmsg.getType() != GNP_Message.Pong) {
            System.out.println("wrong type message received");
            return;
        }
        System.out.println("pong message recevied from " + gmsg.getSrcAddress().toString());
        hostIndex = getHostIndex(gmsg.getSrcAddress());
        if (hostIndex == -1) {
            throw new HyperCastFatalRuntimeException("incorrect pong message received");
        }
        adapter.clearTimer(new Integer(hostIndex * 2 + 1));
        measurements[hostIndex][completedProbes[hostIndex]++] = adapter.getCurrentTime() - startTime[hostIndex];
        if (completedProbes[hostIndex] < probeNumber) {
            adapter.setTimer(new Integer(hostIndex * 2), (int) (100 * rand.nextDouble()));
            return;
        }
        if (measurementCompleted()) {
            adapter.clearTimer(new Integer(landmarks.length * 2));
            float[] output = outputCoordinates();
            if (initialProbe) initialProbeOutput(output); else periodicProbeOutput(output);
        }
    }

    /**
	 * Handle timer-related events.
	 */
    public void timerExpired(Object Timer_ID) {
        int timerID = ((Integer) Timer_ID).intValue();
        System.out.println("timer id " + timerID);
        if (timerID == landmarks.length * 2) {
            System.out.println("the final safeguard timer expires");
            for (int i = 0; i < landmarks.length; i++) {
                if (completedProbes[i] < probeNumber) System.out.println("measurement not completely received from landmark " + i);
            }
            System.exit(1);
        }
        if (timerID == landmarks.length * 2 + 1) {
            startProbe();
            adapter.setTimer(new Integer(timerID), (int) (updateInterval + maxReplyTime * rand.nextDouble()));
            return;
        }
        if (timerID == landmarks.length * 2 + 2) {
            startProbe();
            return;
        }
        int hostIndex = timerID / 2;
        if (completedProbes[hostIndex] == probeNumber) return;
        startTime[hostIndex] = adapter.getCurrentTime();
        GNP_Message gmsg = new GNP_Message(GNP_Message.Ping, myPhyAddress, null);
        System.out.println("ping message sends to " + landmarks[hostIndex].toString());
        adapter.sendUnicastMessage(landmarks[hostIndex], gmsg);
        adapter.setTimer(new Integer(hostIndex * 2 + 1), maxReplyTime);
    }

    /**
	 * Reconstruct GNP message from byte array.
	 */
    public I_Message restoreMessage(byte[] receiveBuffer, int[] validBytesStart, int validBytesEnd) {
        return GNP_Message.restoreMessage(receiveBuffer, validBytesStart, validBytesEnd, adapter);
    }

    /** Return the index of Landmark nodes in the landmark table. */
    private int getHostIndex(I_PhysicalAddress addr) {
        int i;
        for (i = 0; i < landmarks.length; i++) {
            if (addr.equals(landmarks[i])) break;
        }
        if (i == landmarks.length) return -1; else return i;
    }

    /** Check if all measurements are finished. */
    private boolean measurementCompleted() {
        for (int i = 0; i < landmarks.length; i++) {
            if (completedProbes[i] < probeNumber) return false;
        }
        return true;
    }

    /** Calculate the coordinated using measurement results. */
    private float[] outputCoordinates() {
        GNP_Optimization opt = new GNP_Optimization(lmCoor);
        float[] targetDelay = new float[landmarks.length];
        for (int i = 0; i < landmarks.length; i++) {
            targetDelay[i] = 0;
            for (int j = 0; j < probeNumber; j++) {
                if (measurements[i][j] > 0) targetDelay[i] += measurements[i][j];
            }
            targetDelay[i] /= probeNumber;
            if (targetDelay[i] < 0.1f) {
                targetDelay[i] = 0.1f + 0.1f * rand.nextFloat();
            }
            System.out.println("landmark " + i + " delay:" + targetDelay[i]);
        }
        boolean[] validDelay = new boolean[landmarks.length];
        for (int i = 0; i < landmarks.length; i++) validDelay[i] = true;
        float[] targetCoor = opt.fitTargetData(targetDelay, validDelay);
        System.out.println("coordinates: " + targetCoor[0] + "," + targetCoor[1]);
        return targetCoor;
    }

    /** Set initial coordinates values. */
    private synchronized void initialProbeOutput(float[] output) {
        probeDone = true;
        coor[0] = output[0];
        coor[1] = output[1];
        notifyAll();
    }

    /** If periodic update is enabled. This method is called to update the logical 
	 *  address and the coordinates in the cache file.
	 */
    private void periodicProbeOutput(float[] output) {
        double diff = Math.sqrt((coor[0] - output[0]) * (coor[0] - output[0]) + (coor[1] - output[1]) * (coor[1] - output[1]));
        if (diff > updateThreshold) System.out.println("logical coordinates is updated"); else return;
        coor[0] = output[0];
        coor[1] = output[1];
        System.out.println("DT node coordinates updated");
        if (dtNode instanceof DT_Node) {
            ((DT_Node) dtNode).updateLogicalCoordinate(new DT_LogicalAddress((int) coor[0], (int) coor[1]));
        } else if (dtNode instanceof DT_Node_BuddyList) {
            ((DT_Node_BuddyList) dtNode).updateLogicalCoordinate(new DT_LogicalAddress((int) coor[0], (int) coor[1]));
        } else if (dtNode instanceof DT_Node_Multicast) {
            ((DT_Node_Multicast) dtNode).updateLogicalCoordinate(new DT_LogicalAddress((int) coor[0], (int) coor[1]));
        } else {
            System.err.println("The type of Node is wrong!");
        }
        update_cached_coordinates((int) output[0], (int) output[1]);
    }

    /** Calculate a normalized coordinates for new joining node based on the coordinates
    *  of landmarks.
    */
    public int[] createInitialCoords() {
        int[] coords_output = new int[2];
        float min_x, max_x, min_y, max_y, avg_x, avg_y;
        min_x = 65535;
        max_x = -65535;
        min_y = 65535;
        max_y = -65535;
        avg_x = 0;
        avg_y = 0;
        for (int i = 0; i < landmarks.length; i++) {
            avg_x += lmCoor[i][0];
            avg_y += lmCoor[i][1];
            if (lmCoor[i][0] < min_x) {
                min_x = lmCoor[i][0];
            }
            if (lmCoor[i][0] > max_x) {
                max_x = lmCoor[i][0];
            }
            if (lmCoor[i][1] < min_y) {
                min_y = lmCoor[i][1];
            }
            if (lmCoor[i][1] > max_y) {
                max_y = lmCoor[i][1];
            }
        }
        System.out.println("min_x=" + min_x + ", max_x=" + max_x + ", min_y=" + min_y + ", max_y=" + max_y);
        avg_x /= landmarks.length;
        avg_y /= landmarks.length;
        System.out.println("avg_x=" + avg_x + ", avg_y=" + avg_y);
        coords_output[0] = (int) (avg_x + (max_x - min_x) * 0.5 * rand.nextDouble());
        coords_output[1] = (int) (avg_y + (max_y - min_y) * 0.5 * rand.nextDouble());
        return coords_output;
    }

    /** Update the coordinated stored in the cache file. */
    public void update_cached_coordinates(int new_x, int new_y) {
        String New_Coord_Str = Integer.toString(new_x) + "," + Integer.toString(new_y);
        System.out.println("new coordinates string is : " + New_Coord_Str);
        CopyToTemporaryFile(New_Coord_Str);
        CopyBackToOriginalFile();
    }

    /** Copy the contents of cache file to a tempory file. */
    public void CopyToTemporaryFile(String new_str) {
        String String_From_File;
        BufferedReader in = null;
        PrintWriter out = null;
        int flag;
        flag = 0;
        try {
            in = new BufferedReader(new FileReader(DynamicCacheFile));
            flag++;
        } catch (FileNotFoundException ee) {
            System.out.println("File " + DynamicCacheFile + " is not found.");
            in = null;
        }
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(TemporaryCopyFile)));
        } catch (IOException ee) {
            throw new HyperCastFatalRuntimeException(ee);
        }
        try {
            if (flag > 0) {
                while (true) {
                    String_From_File = in.readLine();
                    if (!(String_From_File == null)) {
                        if (String_From_File.startsWith(PROPERTY_PROTO_PREFIX + InitialCoordPrefix)) {
                            System.out.println("The string read is : " + PROPERTY_PROTO_PREFIX + InitialCoordPrefix + "=" + new_str);
                            out.println(PROPERTY_PROTO_PREFIX + InitialCoordPrefix + "=" + new_str);
                            flag++;
                        } else {
                            System.out.println("The string read is : " + String_From_File);
                            out.println(String_From_File);
                        }
                    } else {
                        break;
                    }
                }
            }
            if (flag < 2) {
                System.out.println("Add a line : " + PROPERTY_PROTO_PREFIX + InitialCoordPrefix + "=" + new_str);
                out.println(PROPERTY_PROTO_PREFIX + InitialCoordPrefix + "=" + new_str);
            }
        } catch (IOException ee) {
            throw new HyperCastFatalRuntimeException(ee);
        }
        try {
            if (!(out == null)) {
                out.close();
            }
            if (!(in == null)) {
                in.close();
            }
        } catch (IOException ee) {
            System.err.println("IO exception in closing files:" + ee);
        }
    }

    /** Copy the contents in the tempory file to the original cache file. */
    public void CopyBackToOriginalFile() {
        String String_From_File;
        File temp_in = null;
        BufferedReader in = null;
        PrintWriter out = null;
        int flag;
        flag = 0;
        try {
            temp_in = new File(TemporaryCopyFile);
            in = new BufferedReader(new FileReader(temp_in));
        } catch (FileNotFoundException ee) {
            System.err.println("Temporary File is not found :" + ee);
            in = null;
            flag = 1;
        } catch (NullPointerException eee) {
            System.err.println("Temporary File is not correctly specified :" + eee);
            in = null;
            flag = 1;
        }
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(DynamicCacheFile)));
        } catch (IOException ee) {
            System.err.println("IO exception :" + ee);
            out = null;
        }
        if (flag == 0) {
            try {
                while (true) {
                    String_From_File = in.readLine();
                    if (!(String_From_File == null)) {
                        System.out.println("The string read is : " + String_From_File);
                        out.println(String_From_File);
                    } else {
                        break;
                    }
                }
            } catch (IOException ee) {
                throw new HyperCastFatalRuntimeException(ee);
            }
            try {
                if (!(out == null)) {
                    out.close();
                }
                if (!(in == null)) {
                    in.close();
                }
                temp_in.delete();
            } catch (IOException ee) {
                System.err.println("IO exception in closing files:" + ee);
            } catch (SecurityException eee) {
                System.err.println("Security exception in deleting files:" + eee);
            }
        }
    }

    public static void main(String[] args) {
        HyperCastConfig config = HyperCastConfig.createConfig("hypercast.xml");
        GNP_DT t = new GNP_DT(config, null, null);
        DT_LogicalAddress addr = t.getLogicalAddress();
        System.out.println("initial logical address is:" + addr.toString());
        t.startPeriodicProbe();
    }
}
