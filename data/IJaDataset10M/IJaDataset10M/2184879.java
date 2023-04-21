package edu.cmu.ece.wnp5;

import com.sun.spot.peripheral.radio.ILowPan;
import com.sun.spot.peripheral.radio.LowPan;
import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.peripheral.radio.mhrp.aodv.AODVManager;
import com.sun.spot.peripheral.radio.mhrp.lqrp.LQRPManager;
import com.sun.spot.util.IEEEAddress;

public class SunSpotHostApplication {

    private static int secondsReceived = 0;

    private static int totalPktReceived = 0;

    public static void main(String[] args) {
        RadioFactory.getRadioPolicyManager().setOutputPower(-12);
        RadioFactory.getRadioPolicyManager().setRxOn(true);
        ILowPan lp = LowPan.getInstance();
        RoutingStrategy rs = new HostGamblerRoutingStrategy();
        RoutingLayer rl = new NathanRoutingLayer(lp, rs);
        runSink(rl);
    }

    private static void runSink(final RoutingLayer rl) {
        System.out.println("I'm the SINK!");
        rl.registerCallbacks(new RoutingLayerCallbacks() {

            public void receive(long source, long[] route, byte[] data) {
                String s = "RCVD: ";
                for (int i = 0; i < route.length; i++) {
                    s += IEEEAddress.toDottedHex(route[i]).substring(15) + ' ';
                }
                System.out.println(s);
                totalPktReceived++;
            }

            public void forwarding() {
            }
        });
        long t0, t1;
        t0 = System.currentTimeMillis();
        while (true) {
            t1 = System.currentTimeMillis();
            if (t1 - t0 > 1000) {
                System.out.println("# " + t1 + " " + totalPktReceived);
                t0 = t1;
            }
            Thread.yield();
        }
    }
}
