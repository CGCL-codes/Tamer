package database;

import java.io.IOException;

public class InvokePalus {

    /**
	 * @author szhang@cs.washington.edu (Sai Zhang)
	 */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        args = new String[] { "--time_limit=10", "--class_file=./toyexperiment/toydatabase.txt", "--trace_file=./toydb_trace.model" };
        palus.main.OfflineMain.main(args);
    }
}
