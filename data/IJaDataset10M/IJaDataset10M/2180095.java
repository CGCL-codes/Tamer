package org.jrobin.core.jrrd;

import java.io.IOException;
import org.jrobin.core.RrdException;

/**
 * Show some of the things jRRD can do.
 *
 * @author <a href="mailto:ciaran@codeloop.com">Ciaran Treanor</a>
 * @version $Revision$
 */
public class Main {

    public Main(String rrdFile) {
        RRDatabase rrd = null;
        DataChunk chunk = null;
        try {
            rrd = new RRDatabase(rrdFile);
            chunk = rrd.getData(ConsolidationFunctionType.AVERAGE);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            rrd.toXml(System.out);
        } catch (RrdException e) {
            e.printStackTrace();
            return;
        }
        rrd.printInfo(System.out);
        System.out.println(rrd);
        System.out.println(chunk);
        try {
            rrd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void usage(int status) {
        System.err.println("Usage: " + Main.class.getName() + " rrdfile");
        System.exit(status);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage(1);
        }
        new Main(args[0]);
    }
}
