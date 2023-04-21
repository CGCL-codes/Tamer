package edu.ksu.cis.bnj.ver3.drivers;

import edu.ksu.cis.bnj.gui.GUIWindow;
import edu.ksu.cis.util.driver.Options;

/**
 * file: MASTER.java
 * 
 * @author Jeffrey M. Barber
 */
public class MASTER {

    public static void main(String[] args) {
        String sys = "";
        if (args.length > 0) sys = args[0].toUpperCase();
        Options options = new Options(args);
        if (sys.equals("LSMT") || sys.equals("MTLS")) {
            MTLSd.exec(options);
        } else if (sys.equals("LS")) {
            LS.exec(options);
        } else if (sys.equals("LSO")) {
            LSO.exec(options);
        } else if (sys.equals("DBN")) {
            DBN.exec(options);
        } else if (sys.equals("ELIMBEL")) {
            Elimbel.exec(options);
        } else if (sys.equals("AIS")) {
            AIS.exec(options);
        } else if (sys.equals("GUI")) {
            String[] arg2 = new String[args.length - 1];
            for (int i = 0; i < arg2.length; i++) arg2[i] = args[i + 1];
            GUIWindow.main(arg2);
        } else if (sys.equals("ZEN")) {
            ZenTestEnv.exec(options);
        } else {
            GUIWindow.main(args);
        }
    }
}
