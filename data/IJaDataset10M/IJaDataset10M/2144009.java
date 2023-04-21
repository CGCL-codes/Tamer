package com.player.playerclient;

import com.player.*;
import com.player.messages.*;

/**
The {@link com.player.playerclient.PlayerProxy PlayerProxy} that
communicates with a Player sonar device.

@author Jim Kramer
*/
public class PlayerSonarProxy extends PlayerProxy {

    private static String prg = "PlayerSonarProxy";

    private static boolean debug = false;

    /** The number of sonar readings received. */
    int range_count;

    /** The latest sonar scan data. Range is measured in m. */
    public double[] ranges;

    /** Number of valid sonar poses */
    int pose_count;

    /** Sonar poses (m,m,radians) */
    double[][] poses;

    /** Constructor. Leave the access field empty to start unconnected. */
    public PlayerSonarProxy(PlayerClient pc, int i, char a) {
        super(pc, Player.InterfaceCode.SONAR, i, a);
        range_count = 0;
        ranges = new double[Player.PLAYER_SONAR_MAX_SAMPLES];
        pose_count = 0;
        poses = new double[Player.PLAYER_SONAR_MAX_SAMPLES][3];
    }

    /** Enable/disable the sonars. Note that when sonars are disabled the
	 * client will still receive sonar data, but the ranges will always be
	 * the last value read from the sonars before they were disabled. */
    public boolean SetState(int state) {
        if (client == null) return false;
        return client.Request(new PlayerMessageSonarPower(state), false);
    }

    public boolean GetSonarGeom() {
        PlayerMessageSonarGeometry sg;
        if (client == null) return false;
        if (client.Request(new PlayerMessageSonarGeometry(), true)) {
            sg = (PlayerMessageSonarGeometry) client.getMsg();
            pose_count = sg.pose_count;
            for (int i = 0; i < pose_count; i++) {
                poses[i][0] = sg.poses[i][0];
                poses[i][1] = sg.poses[i][1];
                poses[i][2] = sg.poses[i][2];
            }
        }
        return true;
    }

    /** Return a specific sonar reading.
	 * @param w which sonar reading to get */
    public double Ranges(int w) {
        return ranges[w];
    }

    /** Handle the data message. */
    public void FillData(PlayerMessage req) {
        PlayerMessageSonarData sd = (PlayerMessageSonarData) req;
        if (sd.header.size != sd.payload) {
            System.err.print("WARNING: expected " + sd.payload);
            System.err.println(", got " + sd.header.size + " bytes");
        }
        range_count = sd.getRangeCount();
        ranges = new double[range_count];
        try {
            for (int i = 0; i < range_count; i++) {
                ranges[i] = sd.ranges[i] / 1000;
            }
        } catch (NullPointerException npe) {
            System.err.println(prg + ": attempting to fill null array!");
        } catch (ArrayIndexOutOfBoundsException aoobe) {
            System.err.println(prg + ": ranges array too small!");
        }
    }

    /** Return the String produced by the {@link #Print Print} method.
	 * @return the SonarProxy's device information */
    public String GetPrint() {
        StringBuilder sb = new StringBuilder();
        sb.append("#Sonar(");
        sb.append(did.toString());
        sb.append(") - ");
        sb.append(access);
        sb.append("\n");
        for (int i = 0; i < range_count; i++) {
            sb.append(" ");
            sb.append(ranges[i]);
        }
        sb.append("\n");
        return sb.toString();
    }

    /** Print out current sonar range data. */
    public void Print() {
        System.out.println(GetPrint());
    }
}
