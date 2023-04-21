package com.grt192.controller.hauntedhouse;

import com.grt192.core.EventController;
import com.grt192.core.StepController;
import com.grt192.networking.GRTSingleClientServer;
import com.grt192.networking.SocketEvent;
import com.grt192.networking.SocketListener;

/**
 * Network interface for Haunted House control, by console
 * @author data, ajc
 */
public class NetworkController extends EventController implements SocketListener {

    public static final int PORT = 1920;

    private final int NUM_SOLENOIDS = 8;

    private HauntedHouseController[] controls;

    private HHLEDController hlc;

    GRTSingleClientServer srvr;

    final int[] solToSwitch = { 1, 2, 3, 4, 5, 6, 7, 8 };

    /**
     * Delays array, maps Controllers to their delays
     * Format; {minExtend, maxExtend, minRetract, maxRetract}
     * The Controllers will not randomize either the Open or Closed
     * delays if the max values are negative.
     *
     **/
    final int[][] delays = { { 1000, -15000, 1000, -15000 }, { 5000, 15000, 5000, 15000 }, { 10000, 20000, 5000, 15000 }, { 4000, -15000, 4000, -15000 }, { 1000, 15000, 750, -10000 }, { 5000, 15000, 1000, -5000 }, { 3000, 10000, 5000, 15000 }, { 5000, 15000, 2000, -15000 } };

    public NetworkController(HauntedHouseController[] hhcs) {
        controls = hhcs;
        initControl();
    }

    public NetworkController() {
        initControl();
    }

    private void initControl() {
        controls = new HauntedHouseController[NUM_SOLENOIDS];
        for (int i = 0; i < controls.length; i++) {
            controls[i] = new HauntedHouseController(i + 1, solToSwitch[i], delays[i]);
            controls[i].start();
        }
    }

    private void initLED() {
        hlc = new HHLEDController();
    }

    private void initSocket() {
        srvr = new GRTSingleClientServer(PORT);
        srvr.start();
        System.out.println("connected");
        srvr.addSocketListener(this);
    }

    public void onConnect(SocketEvent e) {
        System.out.println("Connected!");
    }

    public void onDisconnect(SocketEvent e) {
        System.out.println("Not connected.");
    }

    public void dataRecieved(SocketEvent e) {
        String s = e.getData();
        if (s == null) {
            System.out.println("Null message");
            return;
        }
        String s1 = null;
        String s2 = null;
        System.out.println("Received message:" + s);
        boolean b = s.indexOf(-1) != -1;
        if (b) {
            s1 = s.substring(0, s.indexOf(' '));
            s2 = s.substring(s.indexOf(' ') + 1);
        }
        System.out.println("Part 1:" + s1);
        System.out.println("Part 2:" + s2);
        if (s.equals("auto")) {
            for (int i = 0; i < controls.length; i++) {
                controls[i].setAuto(true);
            }
            srvr.sendData("Auto enabled.");
            return;
        } else if (s.equals("manual")) {
            for (int i = 0; i < controls.length; i++) {
                controls[i].setAuto(false);
            }
            srvr.sendData("Manual enabled.");
        } else if (s.equals("status")) {
            String str = "";
            for (int i = 0; i < controls.length; i++) {
                str += ("Mechanism " + i + ":" + controls[i].getEnabled()) + "!";
            }
            str += "Master:" + controls[0].getMaster() + "!";
            str += "Auto:" + controls[0].getAuto() + "!";
            srvr.sendData(str);
            return;
        } else if (b && s1.equals("start")) {
            if (s2.equals("all")) {
                for (int i = 0; i < controls.length; i++) {
                    controls[i].setMaster(true);
                }
                srvr.sendData("Master is on.");
                return;
            } else {
                int num = Integer.parseInt(s2);
                controls[num].setEnabled(true);
                srvr.sendData("Mechanism " + num + " started.");
                return;
            }
        } else if (b && s1.equals("stop")) {
            if (s2.equals("all")) {
                for (int i = 0; i < controls.length; i++) {
                    controls[i].setMaster(false);
                }
                srvr.sendData("Master is off.");
                return;
            } else {
                int num = Integer.parseInt(s2);
                controls[num].setEnabled(false);
                srvr.sendData("Mechanism " + num + " stopped.");
                return;
            }
        } else {
            srvr.sendData("Unknown command type.");
            return;
        }
    }
}
