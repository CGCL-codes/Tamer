package script.neuralNet;

import it.gallo.snarli.wrapper.Approx;
import it.gallo.snarli.wrapper.DelayApprox;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import util.IndiciProcedures;
import util.StoricoProcedures;

public class NetTrainer {

    /**
	 * @param args
	 */
    private int delay = 0, input, output;

    private DelayApprox net = null;

    public NetTrainer(int delay, int input, int output) {
        this.delay = delay;
        this.input = input;
        this.output = output;
    }

    public Approx learn(int nEp, double eta, double mu, double lousyCycle) {
        net.setNep(nEp);
        net.setLousyCycleThreshold(lousyCycle);
        net.setEta(eta);
        net.setMu(mu);
        net.learn();
        return net;
    }

    public DelayApprox getNet() {
        return net;
    }

    public void init() {
        net = new DelayApprox(delay, input, new int[] { 4 * delay * input, delay * input * output }, output);
    }

    public Date[] load(Connection c, String azioneID, String[] indexesID, Date min, Date max) throws SQLException {
        Date[] fromTo = new Date[2];
        ArrayList<HashMap<Date, Double>> list = new ArrayList<HashMap<Date, Double>>(indexesID.length);
        for (int i = 0; i < indexesID.length; i++) {
            HashMap<Date, Double> map = IndiciProcedures.series(c, indexesID[i], min, max);
            list.add(map);
        }
        HashMap<Date, Double> azione = StoricoProcedures.series(c, azioneID, min, max);
        Date[] keys = new Date[list.get(0).keySet().size()];
        keys = list.get(0).keySet().toArray(keys);
        Arrays.sort(keys);
        System.out.println("numero campioni" + keys.length);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        int i = 0;
        while (!azione.containsKey(keys[i])) {
            System.err.println(keys[i]);
            i++;
        }
        fromTo[0] = keys[i];
        fromTo[1] = keys[keys.length - output];
        double[] in = new double[indexesID.length + 1];
        in[in.length - 1] = azione.get(keys[i]);
        for (int j = 0; j < indexesID.length; j++) in[j] = list.get(j).get(keys[i]);
        for (i++; i < keys.length - output; i++) {
            if (azione.containsKey(keys[i])) {
                System.out.println(keys[i]);
                double[] out = new double[output];
                for (int j = 0; j < output; j++) {
                    out[j] = azione.get(keys[i + j]);
                }
                net.addPattern(in, out);
                in = new double[indexesID.length + 1];
                in[in.length - 1] = azione.get(keys[i]);
                for (int j = 0; j < indexesID.length; j++) in[j] = list.get(j).get(keys[i]);
            }
        }
        return fromTo;
    }

    public static void main(String[] args) {
    }
}
