package codestyle;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * @author todwong
 * 
 */
public class Main {

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws Exception {
        Policy policylet = new Policy("default");
        for (File f : new File("./data/").listFiles(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String fn = f.getName().toLowerCase();
                return f.isFile() && (fn.endsWith("cpp") || fn.endsWith("c"));
            }
        })) {
            EvaluateResult er = policylet.evaluate(f);
            System.out.println(f + "=" + er.grade);
            System.out.println(er.badCountsOfEachEvaluator);
            System.out.println(er.problems);
        }
    }
}
