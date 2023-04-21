package grid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Derived from http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
 * 
 * @author rkehoe
 *
 */
public class ProcessUtil {

    /**
     * @param cmdStrs 
	 * @return
	 * @throws IOException 
     */
    public static Process create(ArrayList<String> cmdStrs) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(cmdStrs);
        Process process = processBuilder.start();
        StreamGobbler errors = new StreamGobbler(process.getErrorStream());
        errors.setDaemon(true);
        errors.start();
        StreamGobbler output = new StreamGobbler(process.getInputStream());
        output.setDaemon(true);
        output.start();
        return process;
    }

    private static void log(String x) {
        System.out.println(x);
    }

    private static class StreamGobbler extends Thread {

        private InputStream is;

        private volatile StringBuffer sb = new StringBuffer();

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        @SuppressWarnings("unused")
        public String output() {
            return sb.toString();
        }

        public void run() {
            BufferedReader br = null;
            try {
                InputStreamReader isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    log(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                if (br != null) try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
