package org.sunspotworld;

import com.sun.spot.io.j2me.radiogram.Radiogram;
import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.io.j2me.radiostream.RadiostreamConnection;
import com.sun.spot.peripheral.Spot;
import com.sun.spot.util.IEEEAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.microedition.io.Connector;

/**
 *
 * @author Ohad
 */
public class ActiveBandwidthBenchmark {

    private long ASSURED_BANDWIDTH;

    private long MAXIMUM_BANDWIDTH;

    private double ABOVE_BANDWIDTH_PACKET_LOSS_RATIO;

    private long duration;

    private long otherSpotAddress;

    private String TRANSMIT_PORT;

    public ActiveBandwidthBenchmark(long ASSURED_BANDWIDTH, long MAXIMUM_BANDWIDTH, double ABOVE_BANDWIDTH_PACKET_LOSS_RATIO, long DURATION_OF_EACH_ITERATION, long otherSpotAddress, String TRANSMIT_PORT) {
        this.ASSURED_BANDWIDTH = ASSURED_BANDWIDTH;
        this.MAXIMUM_BANDWIDTH = MAXIMUM_BANDWIDTH;
        this.ABOVE_BANDWIDTH_PACKET_LOSS_RATIO = ABOVE_BANDWIDTH_PACKET_LOSS_RATIO;
        duration = DURATION_OF_EACH_ITERATION;
        this.otherSpotAddress = otherSpotAddress;
        this.TRANSMIT_PORT = TRANSMIT_PORT;
    }

    public void FindTheBandwidthBenchmarkActive() {
        MyLogger.logLn("Starting bandwidth benchmark - active side");
        double normalPacketLoss = findPacketLoss(ASSURED_BANDWIDTH);
        MyLogger.logLn("Normal packet loss is:" + normalPacketLoss);
        long currentBW;
        long lowBW = ASSURED_BANDWIDTH;
        long highBW = MAXIMUM_BANDWIDTH;
        while (lowBW < highBW) {
            currentBW = (lowBW + highBW) / 2;
            MyLogger.logLn("Testing bandwidth " + currentBW + " between " + lowBW + " - " + highBW);
            double currentPacketLoss = findPacketLoss(currentBW);
            if (currentPacketLoss > ABOVE_BANDWIDTH_PACKET_LOSS_RATIO * normalPacketLoss) {
                highBW = currentBW - 1;
                MyLogger.logLn("Decreasing high BW limit to " + highBW);
            } else {
                lowBW = currentBW + 1;
                MyLogger.logLn("Increasing low BW limit to " + lowBW);
            }
        }
        long projectedBandwidth = (lowBW + highBW) / 2;
        MyLogger.logLn("Bandwidth is " + projectedBandwidth + "bps");
        sendInformationFrame_BandwidthBenchmark_BeforeTransfer_Quit();
    }

    private double findPacketLoss(long projectedBandwidth) {
        boolean sentInitialData = false;
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            MyLogger.logLn("Could not sleep");
        }
        while (!sentInitialData) {
            MyLogger.logLn("Trying to send the information frame before benchmark");
            sentInitialData = sendInformationFrame_BandwidthBenchmark_BeforeTransfer(duration, projectedBandwidth);
        }
        MyLogger.logLn("Done sending information frame before benchmark. Now sending data.");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            MyLogger.logLn("Could not sleep");
        }
        sendData(duration, projectedBandwidth);
        long packetsSent = projectedBandwidth * duration;
        MyLogger.logLn("Done sending data. Receiving transmission statistics");
        long packetsReceived = receiveInformationFrame_BandwidthBenchmark_AfterTransfer();
        double packetLoss = (packetsSent - packetsReceived) / packetsSent;
        MyLogger.logLn("Iteration packet loss: " + Double.toString(packetLoss));
        return (packetLoss);
    }

    private boolean sendInformationFrame_BandwidthBenchmark_BeforeTransfer(long duration, long projectedBandwidth) {
        boolean res = false;
        RadiostreamConnection conn = null;
        DataOutputStream output = null;
        DataInputStream input = null;
        try {
            MyLogger.logLn("Start sending information before transfer");
            conn = (RadiostreamConnection) Connector.open("radiostream://" + IEEEAddress.toDottedHex(otherSpotAddress) + ":" + TRANSMIT_PORT);
            MyLogger.logLn("Connection opened");
            output = conn.openDataOutputStream();
            input = conn.openDataInputStream();
            MyLogger.logLn("Sending data");
            output.writeByte(1);
            output.writeLong(duration);
            output.writeLong(projectedBandwidth);
            output.flush();
            MyLogger.logLn("Data Sent, disconnecting");
            res = true;
        } catch (IOException e) {
            MyLogger.logLn("Problem found sending initial information frames:" + e.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (IOException e) {
                MyLogger.logLn("Could not disconnect:" + e.getMessage());
            }
        }
        MyLogger.logLn("Done sending information before transfer");
        return (res);
    }

    private void sendData(long duration, long projectedBandwidth) {
    }

    private long receiveInformationFrame_BandwidthBenchmark_AfterTransfer() {
        return (0);
    }

    private boolean sendInformationFrame_BandwidthBenchmark_BeforeTransfer_Quit() {
        boolean res = false;
        RadiostreamConnection conn = null;
        DataOutputStream output = null;
        DataInputStream input = null;
        try {
            MyLogger.logLn("Start Quit request");
            conn = (RadiostreamConnection) Connector.open("radiostream://" + IEEEAddress.toDottedHex(otherSpotAddress) + ":" + TRANSMIT_PORT);
            MyLogger.logLn("Connection opened");
            output = conn.openDataOutputStream();
            input = conn.openDataInputStream();
            MyLogger.logLn("Sending quit data");
            output.writeByte(2);
            output.flush();
            MyLogger.logLn("Quit Data Sent, disconnecting");
            res = true;
        } catch (IOException e) {
            MyLogger.logLn("Problem found sending quit request:" + e.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (IOException e) {
                MyLogger.logLn("Could not disconnect:" + e.getMessage());
            }
        }
        MyLogger.logLn("Done sending quit request");
        return (res);
    }
}
