package playground.scnadine.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;

public class JoinFiles {

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        System.out.println("Joining files...");
        Config config = ConfigUtils.loadConfig(args[0]);
        final String CONFIG_MODULE = "Tools";
        System.out.println("config module: " + config.getModule(CONFIG_MODULE));
        File sourcedir = new File(config.findParam(CONFIG_MODULE, "sourcedir"));
        File[] allFiles = sourcedir.listFiles();
        File targetFile = new File(config.findParam(CONFIG_MODULE, "targetFile"));
        boolean firstFile = true;
        for (File file : allFiles) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                BufferedWriter out = new BufferedWriter(new FileWriter(targetFile, !firstFile));
                String headerline = in.readLine();
                if (firstFile) {
                    out.write(headerline);
                    out.newLine();
                    out.flush();
                }
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    out.write(inputLine);
                    out.newLine();
                }
                out.close();
            } catch (IOException e) {
                System.out.println("Error while processing file " + file.getName());
            }
            firstFile = false;
        }
        System.out.println("... done.");
    }
}
