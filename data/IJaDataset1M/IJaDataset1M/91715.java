package com.inetmon.jn.statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.inetmon.jn.core.CorePlugin;
import com.inetmon.jn.core.internal.PacketContainer;
import com.inetmon.jn.decoder.DecodedPacket;
import jpcap.packet.Packet;

/**
 * Network utilization statistics
 * 
 * @author Arnaud MARTIN
 */
public class SingleNetUtilizationStat extends PerSecondStat {

    /**
	 * Network bandwidth
	 */
    private long bandwidth = 1000000;

    private String linespeed;

    public SingleNetUtilizationStat() {
        getLineSpeed();
        bandwidth = Long.parseLong(linespeed) * 1000000;
        if (!labels[1].contains("MB")) {
            labels[1] += (linespeed + " MB");
        } else if (!labels[1].contains(linespeed + " MB")) {
            labels[1] = " % of " + linespeed + " MB";
        }
    }

    /**
	 * Displayable statistic labels
	 */
    private static final String[] labels = { Messages.getString("Statistic.SingleNetUtilizationStat.0"), Messages.getString("Statistic.SingleNetUtilizationStat.1") };

    /**
	 * Displayable statistic types
	 */
    private static final String[] types = { Messages.getString("Statistic.SingleNetUtilizationStat.2"), Messages.getString("Statistic.SingleNetUtilizationStat.3"), Messages.getString("Statistic.SingleNetUtilizationStat.4"), Messages.getString("Statistic.SingleNetUtilizationStat.5"), Messages.getString("Statistic.SingleNetUtilizationStat.7") };

    /**
	 * @see com.inetmon.jn.statistic.IStatisticRecorder#addPacket(net.sourceforge.jpcap.net.RawPacket)
	 */
    public synchronized void addPacket(DecodedPacket packet) {
        super.addPacket(packet);
    }

    /**
	 * @see com.inetmon.jn.statistic.IStatisticRecorder#getStatTypes()
	 */
    public String[] getStatTypes() {
        return types;
    }

    /**
	 * @see com.inetmon.jn.statistic.IStatisticRecorder#getLabels()
	 */
    public String[] getLabels() {
        return labels;
    }

    /**
	 * @see com.inetmon.jn.statistic.IStatisticRecorder#getName()
	 */
    public String getName() {
        return Messages.getString("Statistic.SingleNetUtilizationStat.6");
    }

    /**
	 * Get network bandwidth
	 */
    public void getLineSpeed() {
        String temp = "";
        try {
            File output = new File("configuration.ini");
            BufferedReader in = new BufferedReader(new FileReader("configuration.ini"));
            String str;
            str = in.readLine();
            linespeed = str;
            in.close();
        } catch (IOException e) {
        }
        int i = 0;
        while (i < linespeed.length() && Character.isDigit(linespeed.charAt(i))) {
            temp += linespeed.charAt(i);
            i++;
        }
        linespeed = temp;
    }
}
