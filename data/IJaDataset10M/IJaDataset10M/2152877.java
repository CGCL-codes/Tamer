package playground.johannes.teach.telematics.ha2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.matsim.core.utils.collections.Tuple;
import playground.johannes.teach.telematics.Homework2;

/**
 * @author fearonni
 *
 */
public class FindNashGG {

    private static double maxError = 100;

    private static double STEP = 100;

    private static Random random = new Random(0);

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/fearonni/vsp-work/teach/telematik/dynamic-guidance/results.txt"));
        writer.write("tt_pred_1\ttt_pred_2\ttt_real_1\ttt_real_2\terror\taccepted");
        writer.newLine();
        double error_i = Double.MAX_VALUE;
        double error_iminus1 = Double.MAX_VALUE;
        double[] tt_pred_i = new double[] { 470, 470 };
        double[] tt_pred_last = new double[2];
        while (error_i > maxError) {
            Tuple<double[], double[]> results = runMobsim(tt_pred_i);
            double[] tt_real = results.getFirst();
            double[] n_real = results.getSecond();
            double tt_real_min = Math.min(tt_real[0], tt_real[1]);
            double[] delta_tt = new double[2];
            delta_tt[0] = tt_real[0] - tt_real_min;
            delta_tt[1] = tt_real[1] - tt_real_min;
            error_iminus1 = error_i;
            error_i = delta_tt[0] * n_real[0] + delta_tt[1] * n_real[1];
            writer.write(Double.toString(tt_pred_i[0]));
            writer.write("\t");
            writer.write(Double.toString(tt_pred_i[1]));
            writer.write("\t");
            writer.write(Double.toString(tt_real[0]));
            writer.write("\t");
            writer.write(Double.toString(tt_real[1]));
            writer.write("\t");
            writer.write(Double.toString(error_i));
            writer.write("\t");
            if (error_i > error_iminus1) {
                tt_pred_i[0] = tt_pred_last[0];
                tt_pred_i[1] = tt_pred_last[1];
                writer.write("no");
            } else {
                writer.write("yes");
            }
            writer.newLine();
            writer.flush();
            tt_pred_last[0] = tt_pred_i[0];
            tt_pred_last[1] = tt_pred_i[1];
            int k = 0;
            double proba_k = 0.5;
            if (random.nextDouble() < proba_k) {
                k = 1;
            }
            STEP = 100;
            tt_pred_i[k] = tt_pred_i[k] + ((random.nextDouble() * 2 * STEP) - STEP);
        }
        writer.close();
    }

    private static Tuple<double[], double[]> runMobsim(double[] prediction) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/fearonni/vsp-work/teach/telematik/dynamic-guidance/matsim/prediction.txt"));
        writer.write("tt_pred_1\ttt_pred_2");
        writer.newLine();
        writer.write(Double.toString(prediction[0]));
        writer.write("\t");
        writer.write(Double.toString(prediction[1]));
        writer.close();
        Homework2.main(new String[] { "/Users/fearonni/vsp-work/teach/telematik/dynamic-guidance/matsim/config.ha2.xml" });
        double[] tt_real = new double[2];
        double[] n_real = new double[2];
        BufferedReader reader = new BufferedReader(new FileReader("/Users/fearonni/vsp-work/teach/telematik/dynamic-guidance/matsim/output/routeTravelTimes.txt"));
        reader.readLine();
        String line = reader.readLine();
        String[] tokens = line.split("\t");
        n_real[0] = Double.parseDouble(tokens[1]);
        n_real[1] = Double.parseDouble(tokens[2]);
        tt_real[0] = Double.parseDouble(tokens[3]);
        tt_real[1] = Double.parseDouble(tokens[4]);
        return new Tuple<double[], double[]>(tt_real, n_real);
    }
}
