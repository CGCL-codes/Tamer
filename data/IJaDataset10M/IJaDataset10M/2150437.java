package org.highcon.pddpd.plugins.processors;

import java.util.Hashtable;
import org.highcon.pddpd.lib.ConfigurationException;
import org.highcon.pddpd.lib.Processor;

/**
 * @author paul
 *
 */
public class MonParseProcessor extends Processor {

    private boolean alive = true;

    public void run() {
        while (alive) {
            doWork();
        }
        while (!inQueue.isEmpty()) {
            doWork();
        }
        this.myLock.unlock();
    }

    public void doWork() {
        String input;
        Object temp;
        Object[] array;
        String name;
        temp = inQueue.pop();
        if (!(temp instanceof Object[]) && !(((Object[]) temp).length == 2)) {
            messageQ.add("Got something of type " + temp.getClass().getName());
            return;
        }
        array = (Object[]) temp;
        if (!(array[1] instanceof String)) {
            messageQ.add("Got something of type " + temp.getClass().getName());
            return;
        }
        name = (String) array[0];
        input = (String) array[1];
        if (input.startsWith("Filesystem")) {
            String[] lines = input.split("\n");
            String[][] output = new String[lines.length][6];
            output[0][0] = "df";
            output[0][1] = name;
            for (int x = 1; x < lines.length; x++) {
                String[] fields = lines[x].split(" +");
                output[x][0] = fields[0];
                output[x][1] = fields[1];
                output[x][2] = fields[2];
                output[x][3] = fields[3];
                output[x][4] = fields[4];
                output[x][5] = fields[5];
            }
            outQueue.push(output);
        } else if (input.indexOf("total") != -1) {
            String[] lines = input.split("\n");
            String[][] output = new String[3][6];
            output[0][0] = "free";
            output[0][1] = name;
            String[] line = lines[1].split(" +");
            output[1][0] = line[1];
            output[1][1] = line[2];
            output[1][2] = line[3];
            output[1][3] = line[4];
            output[1][4] = line[5];
            output[1][5] = line[6];
            line = lines[2].split(" +");
            output[2][0] = line[1];
            output[2][1] = line[2];
            output[2][2] = line[3];
            outQueue.push(output);
        } else if (input.indexOf("load") != -1) {
            input = input.trim();
            String[][] output = new String[2][6];
            output[0][0] = "uptime";
            output[0][1] = name;
            String[] items = input.split(",");
            int x = items.length - 1;
            output[1][0] = items[0].substring(0, 9).trim();
            output[1][5] = items[x].trim();
            output[1][4] = items[x - 1].trim();
            output[1][3] = items[x - 2].substring(items[x - 2].indexOf(':') + 1).trim();
            output[1][2] = items[x - 3].trim();
            output[1][1] = items[0].substring(items[0].indexOf("up") + 2).trim();
            if (x == 5) {
                output[1][1] += items[1];
            }
            outQueue.push(output);
        } else {
            messageQ.add("Got a bad string");
            System.out.println(input);
        }
    }

    public Hashtable getConfig() {
        return new Hashtable();
    }

    public Hashtable getSchema() {
        return new Hashtable();
    }

    public Object getValue(String name) {
        return null;
    }

    public void setConfig(Hashtable config) throws ConfigurationException {
    }

    public void kill(boolean shutdown) {
        this.alive = false;
        this.myLock.lock();
    }

    public String getConfigFilename() {
        return null;
    }
}
