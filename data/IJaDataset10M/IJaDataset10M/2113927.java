package com.soramaki.fna.test;

import java.util.Hashtable;
import com.soramaki.fna.commands.*;
import com.soramaki.fna.network.Network;

/**
 * Some ad hoc test methods, done before the GUI was done, now it's easier to
 * do checks on the command line.
 */
public class Test implements StatusReceiver {

    public void receive(String report) {
        System.out.println(report);
    }

    private void expressionTest() {
        Network net = new Network("tmp");
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("from", "A1");
        props.put("to", "B1");
        props.put("value", "200.5");
        props.put("hundred", "100");
        props.put("date", "20091215");
        props.put("date2", "20091216");
        props.put("time", "080000");
        props.put("time2", "091020");
        checkExpression(net, props, "from = A1", "true");
        checkExpression(net, props, "a = A1", "false");
        checkExpression(net, props, "from = A1 & to = B1", "true");
        checkExpression(net, props, "value = 2 * hundred + 0.5", "true");
        checkExpression(net, props, "value < 200", "false");
        checkExpression(net, props, "date < 20091224", "true");
        checkExpression(net, props, "date < date2", "true");
        checkExpression(net, props, "time >= 075959", "true");
        checkExpression(net, props, "time >= time2", "false");
        checkExpression(net, props, "time >= 07:59:59", "error");
    }

    private void checkExpression(Network net, Hashtable<String, String> props, String expression, String correct) {
        String out = expression + " : " + net.checkExpression(props, expression) + " (" + correct + ")";
        System.out.println(out);
    }

    private void runTest() {
        String line = "run -file data/script.txt";
        commandTest(line);
    }

    private void buildTest() {
        String line = "bu -file data/payments.txt " + "-decimalseparator , -fieldseparator ; " + "-fromname sender -toname receiver -t 3600";
        commandTest(line);
    }

    private void listTest() {
        String line = "list -fjile data/list.txt -n";
        commandTest(line);
    }

    private void dropTest() {
        String line = "drop -n id < Net_1_T_3600_7";
        commandTest(line);
    }

    private void commandTest(String line) {
        Command cmd = Command.buildCommand(line);
        cmd.setStatusReceiver(this);
        if (cmd.ok()) {
            if (cmd.execute()) {
                System.out.println(cmd.getInfo());
            } else {
                System.out.println(cmd.getError());
            }
        } else {
            System.out.println(cmd.getError());
        }
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.expressionTest();
    }
}
