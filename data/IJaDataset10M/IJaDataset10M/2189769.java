package org.mndacs.kernel.nodes.driver;

import org.apache.log4j.Logger;
import org.mndacs.datatobjects.ValueSet;
import org.mndacs.kernel.nodes.*;

/**
 *
 * @author christopherwagner
 */
public class Siko_MA10_4_ETH extends SensorNode {

    private static Logger logger = Logger.getLogger(Siko_MA10_4_ETH.class);

    @Override
    protected void input(String Line) {
        String inputLine = Line.trim();
        if (inputLine.length() > 3) {
            inputLine = inputLine.substring(0, 1).toString() + inputLine.subSequence(3, 6).toString() + "." + inputLine.subSequence(6, 8).toString();
            Double val = Double.valueOf(inputLine);
            setMess("Degree", val);
        }
        synchronized (cm_rec_lock) {
            cm_recieved = true;
        }
    }

    @Override
    public void update() {
        try {
            cm_recieved = false;
            sendCmd(command[0]);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void updateRecent() {
        update();
    }

    @Override
    public void calibrate() {
        try {
            cm_recieved = false;
            sendCmd(command[1]);
            logger.info("kalibrate");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void test() {
        sendCmd(command[0]);
    }

    protected void print() {
        for (int i = 0; i < messw.size(); i++) {
            String print = "";
            print += messw.get(i).info + ": ";
            if (messw.get(i).isString) print += messw.get(i).StringValue; else print += messw.get(i).DoubleValue;
            print += " " + messw.get(i).unit;
            System.err.println(print);
        }
    }

    protected static String[] command = { "Z", "L" };

    @Override
    protected void postInit() {
        logger.info("connect: " + host + "|" + port);
        messw.add(new ValueSet(0.0, "Deg", "Degree", nodeID));
    }

    @Override
    protected boolean isSNMPAccessible() {
        return true;
    }
}
