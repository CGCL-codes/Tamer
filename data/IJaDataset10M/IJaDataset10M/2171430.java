package dryven.unittest;

import java.io.File;
import dryven.discovery.fs.DirectoryIterable;

public class TestDirectoryIterator {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        for (File f : new DirectoryIterable(new File("/Users/bruno/"), false, false)) {
            System.out.println("*** " + f.getAbsolutePath());
        }
    }
}
