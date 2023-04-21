package org.qtnew;

import org.jdom.input.SAXBuilder;
import org.qtnew.simulator.Simulator;
import org.qtnew.util.XmlHelper;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Malykh
 * Date: 15.12.2005
 * Time: 12:06:30
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("QTNew 1.0.2");
        System.out.println("2005-2006 (C) Anton Malykh");
        if (args.length != 1) throw new IllegalArgumentException("Should be 1 parameter: xml-file");
        Simulator sim = XmlHelper.resolveSimulator(null, new SAXBuilder().build(new FileInputStream(args[0])).getRootElement());
        sim.simulate();
    }
}
