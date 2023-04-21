package ai.backbrop;

import ai.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;
import uk.ac.bath.util.Tweakable;
import uk.ac.bath.util.TweakableDouble;

public class BackPropF implements Serializable {

    float out[][];

    float delta[][];

    float weight[][][];

    int numlayer;

    int layersizes[];

    double beta;

    double alpha;

    float prevDwt[][][];

    transient Vector<Tweakable> tweaks = new Vector<Tweakable>();

    private transient TweakableDouble betaTweak;

    private transient TweakableDouble alphaTweak;

    BackPropF(int sz[], double b, double a, Random rand) {
        beta = b;
        alpha = a;
        makeTweaks();
        numlayer = sz.length;
        layersizes = new int[numlayer];
        for (int i = 0; i < numlayer; i++) {
            layersizes[i] = sz[i];
        }
        out = new float[numlayer][];
        for (int i = 0; i < numlayer; i++) {
            out[i] = new float[layersizes[i]];
        }
        delta = new float[numlayer][];
        for (int i = 1; i < numlayer; i++) {
            delta[i] = new float[layersizes[i]];
        }
        weight = new float[numlayer][][];
        for (int i = 1; i < numlayer; i++) {
            weight[i] = new float[layersizes[i]][];
        }
        for (int i = 1; i < numlayer; i++) {
            for (int j = 0; j < layersizes[i]; j++) {
                weight[i][j] = new float[layersizes[i - 1] + 1];
            }
        }
        prevDwt = new float[numlayer][][];
        for (int i = 1; i < numlayer; i++) {
            prevDwt[i] = new float[layersizes[i]][];
        }
        for (int i = 1; i < numlayer; i++) {
            for (int j = 0; j < layersizes[i]; j++) {
                prevDwt[i][j] = new float[layersizes[i - 1] + 1];
            }
        }
        for (int i = 1; i < numlayer; i++) {
            for (int j = 0; j < layersizes[i]; j++) {
                for (int k = 0; k < layersizes[i - 1] + 1; k++) {
                    weight[i][j][k] = (float) (rand.nextDouble() - .5);
                }
            }
        }
        for (int i = 1; i < numlayer; i++) {
            for (int j = 0; j < layersizes[i]; j++) {
                for (int k = 0; k < layersizes[i - 1] + 1; k++) {
                    prevDwt[i][j][k] = 0.0f;
                }
            }
        }
    }

    public Vector<Tweakable> getTweaks() {
        return tweaks;
    }

    float mse(float tgt[]) {
        float mse = 0;
        for (int i = 0; i < layersizes[numlayer - 1]; i++) {
            mse += (tgt[i] - out[numlayer - 1][i]) * (tgt[i] - out[numlayer - 1][i]);
        }
        return mse / 2;
    }

    float Out(int i) {
        return out[numlayer - 1][i];
    }

    float[] output() {
        return out[numlayer - 1];
    }

    void ffwd(float in[]) {
        float sum;
        for (int i = 0; i < layersizes[0]; i++) {
            out[0][i] = in[i];
        }
        for (int i = 1; i < numlayer; i++) {
            for (int j = 0; j < layersizes[i]; j++) {
                sum = 0.0f;
                for (int k = 0; k < layersizes[i - 1]; k++) {
                    sum += out[i - 1][k] * weight[i][j][k];
                }
                sum += weight[i][j][layersizes[i - 1]];
                out[i][j] = Util.sigmoid(sum);
            }
        }
    }

    void bpgt(float in[], float tgt[]) {
        float sum;
        ffwd(in);
        for (int i = 0; i < layersizes[numlayer - 1]; i++) {
            delta[numlayer - 1][i] = out[numlayer - 1][i] * (1 - out[numlayer - 1][i]) * (tgt[i] - out[numlayer - 1][i]);
        }
        for (int i = numlayer - 2; i > 0; i--) {
            for (int j = 0; j < layersizes[i]; j++) {
                sum = 0.0f;
                for (int k = 0; k < layersizes[i + 1]; k++) {
                    sum += delta[i + 1][k] * weight[i + 1][k][j];
                }
                delta[i][j] = out[i][j] * (1 - out[i][j]) * sum;
            }
        }
        for (int i = 1; i < numlayer; i++) {
            for (int j = 0; j < layersizes[i]; j++) {
                for (int k = 0; k < layersizes[i - 1]; k++) {
                    weight[i][j][k] += alpha * prevDwt[i][j][k];
                }
                weight[i][j][layersizes[i - 1]] += alpha * prevDwt[i][j][layersizes[i - 1]];
            }
        }
        for (int i = 1; i < numlayer; i++) {
            for (int j = 0; j < layersizes[i]; j++) {
                for (int k = 0; k < layersizes[i - 1]; k++) {
                    prevDwt[i][j][k] = (float) (beta * delta[i][j] * out[i - 1][k]);
                    weight[i][j][k] += prevDwt[i][j][k];
                }
                prevDwt[i][j][layersizes[i - 1]] = (float) (beta * delta[i][j]);
                weight[i][j][layersizes[i - 1]] += prevDwt[i][j][layersizes[i - 1]];
            }
        }
    }

    void makeTweaks() {
        betaTweak = new TweakableDouble(0., 20., beta, .00001, "beta");
        alphaTweak = new TweakableDouble(0., 1000., alpha, .1, "alpha");
        betaTweak.addObserver(new Observer() {

            public void update(Observable o, Object arg) {
                beta = betaTweak.doubleValue();
            }
        });
        alphaTweak.addObserver(new Observer() {

            public void update(Observable o, Object arg) {
                alpha = alphaTweak.doubleValue();
            }
        });
        tweaks.add(alphaTweak);
        tweaks.add(betaTweak);
    }

    void dump() {
        for (int i = 0; i < numlayer; i++) {
            System.out.println("Layer " + i);
            for (int j = 0; j < layersizes[i]; j++) {
                if (i > 0) {
                    System.out.println(" Neuron " + j + " =" + out[i][j] + "  " + delta[i][j]);
                    for (int k = 0; k < layersizes[i - 1] + 1; k++) {
                        System.out.println("w prev [" + k + "]" + weight[i][j][k] + " " + prevDwt[i][j][k]);
                    }
                } else {
                    System.out.println(" Neuron " + j + " =" + out[i][j]);
                }
            }
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        makeTweaks();
    }
}
