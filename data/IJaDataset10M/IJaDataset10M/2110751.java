package mx.unam.ecologia.gye.coalescence.app;

import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Allows to run a set of experiments.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class RunExperiments {

    private static final Log log = LogFactory.getLog(RunExperiments.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SimulationParameters params = new SimulationParameters(args);
        int num_beta = params.getBetaCount();
        int num_k = params.getKCount();
        int num_N = params.getNCount();
        int num_u = params.getUCount();
        PrintWriter pw;
        try {
            File csv = new File(params.getOutput());
            FileOutputStream fout = new FileOutputStream(csv);
            pw = new PrintWriter(fout);
        } catch (Exception ex) {
            pw = new PrintWriter(System.out);
        }
        for (int l = 0; l < num_beta; l++) {
            params.selectBeta(l);
            for (int m = 0; m < num_N; m++) {
                params.selectN(m);
                for (int n = 0; n < num_k; n++) {
                    params.selectK(n);
                    for (int o = 0; o < num_u; o++) {
                        params.selectU(o);
                        MicrosatelliteExperiment exp = new MicrosatelliteExperiment(params);
                        if (m + n + o == 0) {
                            pw.println(exp.getCSVHeader());
                            pw.flush();
                        }
                        exp.init();
                        exp.run();
                        pw.println(exp.resultsToCSV());
                        pw.flush();
                        System.gc();
                    }
                }
            }
        }
    }
}
