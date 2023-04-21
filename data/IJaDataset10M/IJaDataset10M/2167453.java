package org.mndacs.kernel.nodes.driver;

import org.apache.log4j.Logger;
import org.mndacs.datatobjects.ValueSet;
import org.mndacs.kernel.nodes.*;

/**
 *
 * @author christopherwagner
 */
public class FUG_ProbusIV_ETH extends PowerNode {

    private static Logger logger = Logger.getLogger(FUG_ProbusIV_ETH.class);

    @Override
    protected void input(String Line) {
        String inputLine = Line;
        inputLine.trim();
        inputLine.replace(" ", "");
        if (!inputLine.startsWith("E")) {
            synchronized (cm_rec_lock) {
                if (cm_send.equals(command[3])) {
                    inputLine = inputLine.substring(0, inputLine.length() - 2);
                    setMess("Voltage", Double.valueOf(inputLine));
                    cm_recieved = true;
                }
                if (cm_send.equals(command[4])) {
                    inputLine = inputLine.substring(0, inputLine.length() - 2);
                    setMess("Current", Double.valueOf(inputLine));
                    cm_recieved = true;
                }
                if (cm_send.equals(command[5])) {
                    try {
                        setMess("Status", (inputLine));
                        setMess("Power status", Integer.valueOf(inputLine.substring(2, 3)));
                        setMess("Polarity", Integer.valueOf(inputLine.substring(5, 6)));
                    } catch (Exception e) {
                        logger.warn(e);
                    }
                    cm_recieved = true;
                }
            }
        }
    }

    @Override
    public void update() {
        try {
            cm_recieved = false;
            sendCmd("?");
            sendCmd(command[3], true);
            Thread.sleep(300);
            sendCmd(command[4], true);
            Thread.sleep(300);
            sendCmd(command[5], true);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void updateRecent() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    protected synchronized void sendCmd(String cmd, boolean trigger) {
        try {
            cm_send = cmd;
            String sendstring = cmd + CR;
            out.println(sendstring);
            if (trigger) {
                Thread.sleep(100);
                out.println("?" + CR);
            }
            int timeout = 1000, count = 0;
            while (true) {
                Thread.sleep(10);
                count += 10;
                if (count == timeout) {
                    break;
                }
                synchronized (cm_rec_lock) {
                    if (cm_recieved) break;
                }
            }
            synchronized (cm_rec_lock) {
                cm_recieved = false;
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    protected static String[] command = { "U", "I", "F", "N0", "N1", "N2", "N3", "N4", "A", "V" };

    @Override
    protected void postInit() {
        logger.info("connect: " + host + "|" + port);
        messw.add(new ValueSet(0, "V", "Voltage reference", nodeID));
        messw.add(new ValueSet(0, "A", "Current reference", nodeID));
        messw.add(new ValueSet(0, "V", "Voltage", nodeID));
        messw.add(new ValueSet(0, "A", "Current", nodeID));
        messw.add(new ValueSet("", "status", "Status", nodeID));
        messw.add(new ValueSet(0, "Power", "Power status", nodeID));
        messw.add(new ValueSet(0, "Polarity", "Polarity", nodeID));
        sendCmd("", true);
        sendCmd("", true);
    }

    @Override
    public void setCurrent(Double current) {
        sendCmd(command[1] + Double.toString(current));
        setMess("Current reference", current);
    }

    @Override
    public void setVoltage(Double voltage) {
        sendCmd(command[0] + Double.toString(voltage));
        setMess("Voltage reference", voltage);
    }

    /**
     * set polarity of power supplay
     * @param pol - 1 for positive -1 for negative
     */
    @Override
    public void setPolarity(int pol) {
        String pol_string = "";
        if (pol == 1) pol_string = "0";
        if (pol == -1) pol_string = "1";
        sendCmd(command[11] + pol_string);
    }

    @Override
    public void setPowerOn() {
        sendCmd(command[2] + "1");
    }

    @Override
    public void setPowerOff() {
        sendCmd(command[2] + "0");
    }

    @Override
    protected boolean isSNMPAccessible() {
        return true;
    }
}
