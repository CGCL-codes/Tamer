package jgibblda;

public class Inferencer {

    public Model trnModel;

    public Dictionary globalDict;

    private LDACmdOption option;

    private Model newModel;

    public int niters = 100;

    public boolean init(LDACmdOption option) {
        this.option = option;
        trnModel = new Model();
        if (!trnModel.initEstimatedModel(option)) return false;
        globalDict = trnModel.data.localDict;
        computeTrnTheta();
        computeTrnPhi();
        return true;
    }

    public Model inference(LDADataset newData) {
        System.out.println("init new model");
        Model newModel = new Model();
        newModel.initNewModel(option, newData, trnModel);
        this.newModel = newModel;
        System.out.println("Sampling " + niters + " iteration for inference!");
        for (newModel.liter = 1; newModel.liter <= niters; newModel.liter++) {
            for (int m = 0; m < newModel.M; ++m) {
                for (int n = 0; n < newModel.data.docs[m].length; n++) {
                    int topic = infSampling(m, n);
                    newModel.z[m].set(n, topic);
                }
            }
        }
        System.out.println("Gibbs sampling for inference completed!");
        computeNewTheta();
        computeNewPhi();
        newModel.liter--;
        newModel.saveModel("test." + newModel.modelName);
        return this.newModel;
    }

    public Model inference(String[] strs) {
        Model newModel = new Model();
        LDADataset dataset = LDADataset.readDataSet(strs, globalDict);
        return inference(dataset);
    }

    public Model inference() {
        newModel = new Model();
        if (!newModel.initNewModel(option, trnModel)) return null;
        System.out.println("Sampling " + niters + " iteration for inference!");
        for (newModel.liter = 1; newModel.liter <= niters; newModel.liter++) {
            for (int m = 0; m < newModel.M; ++m) {
                for (int n = 0; n < newModel.data.docs[m].length; n++) {
                    int topic = infSampling(m, n);
                    newModel.z[m].set(n, topic);
                }
            }
        }
        System.out.println("Gibbs sampling for inference completed!");
        System.out.println("Saving the inference outputs!");
        computeNewTheta();
        computeNewPhi();
        newModel.liter--;
        newModel.saveModel(newModel.dfile + "." + newModel.modelName);
        return newModel;
    }

    /**
	 * do sampling for inference
	 * m: document number
	 * n: word number?
	 */
    protected int infSampling(int m, int n) {
        int topic = newModel.z[m].get(n);
        int _w = newModel.data.docs[m].words[n];
        int w = newModel.data.lid2gid.get(_w);
        newModel.nw[_w][topic] -= 1;
        newModel.nd[m][topic] -= 1;
        newModel.nwsum[topic] -= 1;
        newModel.ndsum[m] -= 1;
        double Vbeta = trnModel.V * newModel.beta;
        double Kalpha = trnModel.K * newModel.alpha;
        for (int k = 0; k < newModel.K; k++) {
            newModel.p[k] = (trnModel.nw[w][k] + newModel.nw[_w][k] + newModel.beta) / (trnModel.nwsum[k] + newModel.nwsum[k] + Vbeta) * (newModel.nd[m][k] + newModel.alpha) / (newModel.ndsum[m] + Kalpha);
        }
        for (int k = 1; k < newModel.K; k++) {
            newModel.p[k] += newModel.p[k - 1];
        }
        double u = Math.random() * newModel.p[newModel.K - 1];
        for (topic = 0; topic < newModel.K; topic++) {
            if (newModel.p[topic] > u) break;
        }
        newModel.nw[_w][topic] += 1;
        newModel.nd[m][topic] += 1;
        newModel.nwsum[topic] += 1;
        newModel.ndsum[m] += 1;
        return topic;
    }

    protected void computeNewTheta() {
        for (int m = 0; m < newModel.M; m++) {
            for (int k = 0; k < newModel.K; k++) {
                newModel.theta[m][k] = (newModel.nd[m][k] + newModel.alpha) / (newModel.ndsum[m] + newModel.K * newModel.alpha);
            }
        }
    }

    protected void computeNewPhi() {
        for (int k = 0; k < newModel.K; k++) {
            for (int _w = 0; _w < newModel.V; _w++) {
                Integer id = newModel.data.lid2gid.get(_w);
                if (id != null) {
                    newModel.phi[k][_w] = (trnModel.nw[id][k] + newModel.nw[_w][k] + newModel.beta) / (newModel.nwsum[k] + newModel.nwsum[k] + trnModel.V * newModel.beta);
                }
            }
        }
    }

    protected void computeTrnTheta() {
        for (int m = 0; m < trnModel.M; m++) {
            for (int k = 0; k < trnModel.K; k++) {
                trnModel.theta[m][k] = (trnModel.nd[m][k] + trnModel.alpha) / (trnModel.ndsum[m] + trnModel.K * trnModel.alpha);
            }
        }
    }

    protected void computeTrnPhi() {
        for (int k = 0; k < trnModel.K; k++) {
            for (int w = 0; w < trnModel.V; w++) {
                trnModel.phi[k][w] = (trnModel.nw[w][k] + trnModel.beta) / (trnModel.nwsum[k] + trnModel.V * trnModel.beta);
            }
        }
    }
}
