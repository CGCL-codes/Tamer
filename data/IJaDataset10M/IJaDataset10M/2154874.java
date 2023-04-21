package src.fileUtilities;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.zip.GZIPOutputStream;
import src.lib.ioInterfaces.FileOut;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.ioInterfaces.MapviewMAQIterator;
import src.lib.objects.AlignedRead;

/**
 * Separate Eland files based on chromosome 
 * output is <chr>.part.eland based
 * upon matthew Bainbridge's SeparateReads.java
 * 
 * @author Genome Sciences Centre
 * @version $Revision: 485 $
 */
public class SeparateMapviewReads {

    private static final int INIT_SIZE = 40;

    private static Log_Buffer LB;

    /** Dummy Constructor */
    private SeparateMapviewReads() {
    }

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("usage: <Mapview file> <output path/> [name]");
            System.exit(0);
        }
        String output_path = args[1];
        if (!output_path.endsWith(System.getProperty("file.separator"))) {
            output_path = output_path.concat(System.getProperty("file.separator"));
        }
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        LB.addLogFile(output_path + "SeparateMapviewRead.log");
        Thread th = new Thread(LB);
        th.start();
        LB.Version("SeparateMapviewReads", "$Revision: 485 $");
        Hashtable<String, BufferedWriter> ht = new Hashtable<String, BufferedWriter>(INIT_SIZE);
        Hashtable<String, int[]> cntht = new Hashtable<String, int[]>(INIT_SIZE);
        String ex_name = "part";
        if (args.length > 2) {
            ex_name = args[2];
        }
        int cnt = 0;
        MapviewMAQIterator ei = new MapviewMAQIterator(LB, "source", args[0]);
        AlignedRead alnrd = null;
        while (ei.hasNext()) {
            cnt++;
            if (cnt % 1000000 == 0) {
                LB.notice(cnt + " lines processed");
            }
            try {
                alnrd = ei.next();
            } catch (NoSuchElementException nsee) {
                continue;
            }
            String chrname = alnrd.get_chromosome();
            chrname = chrname.replace('>', ' ');
            chrname = chrname.trim();
            BufferedWriter bw = ht.get(chrname);
            int[] I = cntht.get(chrname);
            if (bw == null) {
                try {
                    bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(output_path + chrname + "." + ex_name + ".mapview.gz"))));
                } catch (IOException io) {
                    LB.error("Could not create file: " + output_path + chrname + "." + ex_name + ".mapview.gz");
                    LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                    LB.die();
                }
                assert (bw != null);
                ht.put(chrname, bw);
                I = new int[1];
                cntht.put(chrname, I);
            }
            I[0]++;
            try {
                bw.write(alnrd.outMapview() + "\n");
            } catch (IOException io) {
                LB.error("Could not write to file: " + output_path + chrname + "." + ex_name + ".mapview.gz");
                LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                LB.die();
            }
        }
        ei.close(false);
        Enumeration<String> keys = ht.keys();
        LB.notice("Found " + cnt + " records.");
        FileOut meta = new FileOut(LB, output_path + "meta_info.txt", false);
        meta.write("total\t" + cnt + "\n");
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            int[] I = cntht.get(key);
            LB.notice(key + "." + ex_name + ".mapview.gz\t" + I[0]);
            meta.writeln(key + "." + ex_name + ".mapview.gz\t" + I[0]);
            BufferedWriter fw = ht.get(key);
            try {
                fw.close();
            } catch (IOException io) {
                LB.warning("Could not close file.");
                LB.warning("Message from java environment (may be null: " + io.getMessage());
            }
        }
        meta.close();
        LB.close();
    }
}
