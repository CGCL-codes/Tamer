package moduledefault.classify.naivebayes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author evaristowb
 */
public class NaiveBayes {

    private ArrayList input;

    private ArrayList output;

    private ArrayList classes;

    private String[] atributes;

    private int f[][];

    private double fd[][];

    private int nd[][];

    private double fd2[][];

    private double mu[][];

    private double sigma[][];

    public NaiveBayes() {
        input = new ArrayList();
        output = new ArrayList();
    }

    public void inicialize(Collection classes, String[] atributes) {
        this.classes = new ArrayList(classes);
        this.atributes = atributes;
        f = new int[classes.size()][atributes.length - 1];
        fd = new double[classes.size()][atributes.length - 1];
        nd = new int[classes.size()][atributes.length - 1];
        fd2 = new double[classes.size()][atributes.length - 1];
        mu = new double[classes.size()][atributes.length - 1];
        sigma = new double[classes.size()][atributes.length - 1];
    }

    private void addExamples(Object[] o1, Object o2) {
        if (!input.contains(o1)) {
            input.add(o1);
            output.add(o2);
            int i = classes.indexOf(o2);
            for (int j = 0; j < o1.length; j++) {
                if (o1[j] instanceof Double || o1[j] instanceof Float || o1[j] instanceof BigDecimal) {
                    Double d = new Double(o1[j].toString());
                    fd[i][j] += d;
                    fd2[i][j] += Math.pow(d, 2);
                    nd[i][j]++;
                } else {
                    f[i][j]++;
                }
            }
        }
    }

    public void train(Object[][] o1, Object[] o2) {
        input.clear();
        output.clear();
        for (int i = 0; i < o1.length; i++) {
            addExamples(o1[i], o2[i]);
        }
        calcAverage();
    }

    public void calcAverage() {
        if (input.size() > 1) {
            Object[] o = (Object[]) input.get(0);
            for (int j = 0; o != null && j < o.length; j++) {
                if (o[j] instanceof Double || o[j] instanceof Float || o[j] instanceof BigDecimal) {
                    for (int i = 0; i < classes.size(); i++) {
                        mu[i][j] = fd[i][j] / nd[i][j];
                        if (Double.isNaN(mu[i][j])) mu[i][j] = 0.0;
                        sigma[i][j] = Math.sqrt(1.0 / (nd[i][j] - 1) * (fd2[i][j] - fd2[i][j] / nd[i][j]));
                        if (Double.isNaN(sigma[i][j])) sigma[i][j] = 0.0;
                    }
                }
            }
        }
    }

    public Object test(Object[] o) {
        return prob(o);
    }

    private Object prob(Object[] o) {
        double prob[] = new double[classes.size()];
        for (int i = 0; i < classes.size(); i++) prob[i] = 1.0;
        int fo[][] = new int[classes.size()][atributes.length - 1];
        for (int i = 0; i < input.size(); i++) {
            Object[] in = (Object[]) input.get(i);
            Object out = output.get(i);
            for (int j = 0; j < atributes.length - 1; j++) {
                if (!(in[j] instanceof Double) && !(in[j] instanceof Float) && !(in[j] instanceof BigDecimal) && in[j].equals(o[j])) fo[classes.indexOf(out)][j]++;
            }
        }
        for (int j = 0; j < atributes.length - 1; j++) {
            if (o[j] instanceof Double || o[j] instanceof Float || o[j] instanceof BigDecimal) {
                Double d = new Double(o[j].toString());
                for (int i = 0; i < classes.size(); i++) {
                    prob[i] *= Gaussian.phi(d.doubleValue(), mu[i][j], sigma[i][j]);
                }
            } else {
                for (int i = 0; i < classes.size(); i++) prob[i] *= ((double) fo[i][j]) / f[i][j];
            }
        }
        Object c = null;
        double probM = 0.0;
        for (int i = 0; i < classes.size(); i++) if (i == 0 || probM <= prob[i]) {
            probM = prob[i];
            c = classes.get(i);
        }
        return c;
    }
}
