package fear.jdiag.core;

import fear.jdiag.resource.*;
import java.io.*;

/**
 * The processor class handles the processing of Resources, as well as file output.
 * 
 * @author TheFearow
 * @version 1.0
 * @since 1.0
 *
 */
public class Processor {

    /**
	 * The output file PrintWriter, used for outputting Resource results.
	 * 
	 * @since 1.0
	 */
    protected PrintWriter out = null;

    /**
	 * Initiates the Processor, by initialising the PrintWriter out.
	 * 
	 * @since 1.0
	 *
	 */
    public void init() {
        try {
            out = new PrintWriter(new FileWriter("report.txt"));
        } catch (Exception e) {
            return;
        }
    }

    /**
	 * Runs this processor. This usually involves executing each of the Resources passed to the method.
	 * 
	 * @since 1.0
	 * @param reslist The list of resources to run (can contain duplicates, to run multiple times, e.g. if different params required)
	 * @param resargs The parameters for each resource, accessed like resargs[resid][0...n]
	 * @see Resource
	 */
    public void run(String[] reslist, String[][] resargs) {
        Resource[] resources = new Resource[reslist.length];
        int i = 0;
        for (String res : reslist) {
            if (!res.contains(".")) {
                res = "fear.jdiag.resource." + res;
            }
            System.out.println("Loading " + res + " (" + (i + 1) + "): " + Util.arrayToString(resargs[i]));
            Object resobj = null;
            try {
                resobj = Class.forName(res).newInstance();
            } catch (Exception e) {
                resobj = null;
                e.printStackTrace();
            }
            if (resobj == null) {
                continue;
            }
            if (resobj instanceof Resource) {
                resources[i] = (Resource) resobj;
            } else {
                resources[i] = null;
            }
            i++;
        }
        if (out == null) {
            init();
        }
        if (out == null) {
            return;
        }
        i = 0;
        for (Resource r : resources) {
            try {
                if (r == null) {
                    return;
                }
                System.out.println("Processing " + r.getClass().getName() + " (" + (i + 1) + "): " + Util.arrayToString(resargs[i]));
                out.println("Resource " + (i + 1) + " (" + reslist[i].replace("_", " ") + "): " + Util.arrayToString(resargs[i]));
                String[] output = null;
                try {
                    output = r.execute(resargs[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                for (String s : output) {
                    out.println(s);
                }
                out.println();
                out.println();
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
