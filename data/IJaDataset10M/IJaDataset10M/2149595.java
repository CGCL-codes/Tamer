package game.featurerank;

import game.gui.visj3d.VNetJ3D;
import game.neurons.GAMEnetwork;
import game.neurons.GMDHnetwork;
import game.neurons.Neuron;
import game.data.TreeData;
import game.data.Instance;
import game.data.GlobalData;
import game.gui.NetworkStructure3D;
import game.gui.InformationWindow;
import java.util.*;

/**
 * User: Ales Pilny
 * Date: 7.10.2008
 * Time: 9:54:13
 * Feature Ranking based on correlations among units
 */
public class FeatureRankingStatistical {

    private TreeData data;

    private GAMEnetwork gamNet;

    private GMDHnetwork gmdhNet;

    private int number_of_inputs;

    private int lastLayer2;

    private int outputNeuronId;

    private Map mapUnits;

    private Map mapCorrel;

    private Map mapNeurons;

    private double[][] vectors;

    private double[] responses;

    private double[][] inputs;

    private InformationWindow infoWnd;

    public double[] frPlus;

    public double[] frMult;

    public double[] frMultPlus;

    public double[] frInOut;

    public double[] frFuzzy;

    public double[] frCFbasic;

    public double[] frCFcombiMax;

    public double[] frCFcombiMin;

    public double[][] crossCorrelationMatrix;

    private boolean[] frMultBoolArr;

    private VNetJ3D vnet;

    public FeatureRankingStatistical(TreeData data, InformationWindow infoWnd, VNetJ3D vnet) {
        if (NetworkStructure3D.getGAMEnet() != null) {
            gamNet = NetworkStructure3D.getGAMEnet();
            this.data = data;
            this.infoWnd = infoWnd;
            this.vnet = vnet;
            mapUnits = new HashMap<String, SpecUnit>();
            mapCorrel = new HashMap<String, Double>();
            mapNeurons = new TreeMap<String, Neuron>();
            init();
            setOutputNeuronId(vnet.getOutputID());
            computeRootCorrelations();
            createMapOfUsedNeurons();
            computeAllCorrelations();
            crossCorrelationMatrix = computeCrossCorrelationsMatrix();
            computeOverAllRanks(outputNeuronId, 0, 1, 1, 1, 1, 1, 0);
            correctMultRanks();
            System.out.println("PLUS overAllranks:      " + Arrays.toString(frPlus));
            System.out.println("MULT overAllranks:      " + Arrays.toString(frMult));
            System.out.println("Mult+Plus overAllranks: " + Arrays.toString(frMultPlus));
        } else {
            System.out.println("Correlation based FR is for GAME network only! ");
            System.out.println("Correlation based FR is for GAME network only! ");
            System.out.println("Correlation based FR is for GAME network only! ");
        }
    }

    private void init() {
        vectors = getInputVectors();
        responses = new double[vectors.length];
        inputs = getInputColumn(vectors);
        number_of_inputs = GlobalData.getInstance().getINumber();
        frMult = new double[number_of_inputs];
        frPlus = new double[number_of_inputs];
        frMultPlus = new double[number_of_inputs];
        frInOut = new double[number_of_inputs];
        frMultBoolArr = new boolean[number_of_inputs];
        frFuzzy = new double[number_of_inputs];
        frCFbasic = new double[number_of_inputs];
        frCFcombiMax = new double[number_of_inputs];
        frCFcombiMin = new double[number_of_inputs];
        for (int i = 0; i < number_of_inputs; i++) {
            frMult[i] = 1;
            frPlus[i] = 0;
            frMultPlus[i] = 0;
            frInOut[i] = 0;
            frMultBoolArr[i] = false;
            frFuzzy[i] = 0;
            frCFbasic[i] = 0;
            frCFcombiMax[i] = 0;
            frCFcombiMin[i] = 1;
        }
    }

    void setOutputNeuronId(int outputNeuronId) {
        this.outputNeuronId = outputNeuronId;
    }

    /**
     * compute correlations between root and inputs and store them into mapCorrel with computed correlations
     * under key joined from root and input name
     */
    private void computeRootCorrelations() {
        SpecUnit root, outNeuron, sunit;
        for (int i = 0; i < vectors.length; i++) {
            responses[i] = gamNet.getOutput(vectors[i]);
        }
        root = new SpecUnit(((Integer) (outputNeuronId * 2)).toString(), responses);
        mapUnits.put(((Integer) (outputNeuronId * 2)).toString(), root);
        outNeuron = (SpecUnit) mapUnits.get(((Integer) (outputNeuronId * 2)).toString());
        infoWnd.actualID = outputNeuronId * 2;
        double correl;
        for (int i = 0; i < inputs.length; i++) {
            sunit = new SpecUnit(((Integer) (i * 2)).toString(), inputs[i]);
            mapUnits.put(((Integer) (i * 2)).toString(), sunit);
            correl = computeCorrelation(outNeuron, sunit);
            mapCorrel.put(outNeuron.getName() + "-" + sunit.getName(), correl);
            frInOut[i] = correl;
            System.out.println("correl " + outNeuron.getName() + " and " + sunit.getName() + " is: " + mapCorrel.get(outNeuron.getName() + "-" + sunit.getName()));
        }
    }

    /**
     * compute correlation between given units uder their ids
     * If already exist, is simply returned
     * @param a (String) is allways id of (temporary)root unit (
     * @param b (String) is id of the second unit
     * @return correlation between a and b units
     */
    public double getCorrelation(String a, String b) {
        double correl = -3;
        String name = a + "-" + b;
        if (mapCorrel.containsKey(name)) {
            return (Double) mapCorrel.get(name);
        } else {
            try {
                if (!mapUnits.containsKey(a)) {
                    System.out.print("unit " + a + " stil does not exists");
                    gamNet.setOutputNeuron((Neuron) mapNeurons.get(a));
                    double[] responsesA = new double[responses.length];
                    for (int i = 0; i < vectors.length; i++) {
                        responsesA[i] = gamNet.getOutput(vectors[i]);
                    }
                    mapUnits.put(a, new SpecUnit(a, responsesA));
                }
                if (!mapUnits.containsKey(b)) {
                    System.out.print("unit " + b + " stil not exists --- ");
                    if ((Integer.parseInt(b) / 2) >= number_of_inputs) {
                        gamNet.setOutputNeuron((Neuron) mapNeurons.get(b));
                        double[] responsesB = new double[responses.length];
                        for (int i = 0; i < vectors.length; i++) {
                            responsesB[i] = gamNet.getOutput(vectors[i]);
                        }
                        mapUnits.put(b, new SpecUnit(b, responsesB));
                    } else {
                        throw new CorrelationException();
                    }
                }
                correl = computeCorrelation((SpecUnit) mapUnits.get(a), (SpecUnit) mapUnits.get(b));
                mapCorrel.put(name, correl);
                if (Math.abs(correl) > 1) {
                    throw new OutOfRangeCorrelationException();
                }
            } catch (OutOfRangeCorrelationException oorce) {
                oorce.getMessage();
                System.out.println("correl(a,b) = " + correl);
                System.out.println("unit a = " + a);
                System.out.println("unit b = " + b);
            } catch (CorrelationException ce) {
                ce.getMessage();
                System.out.println("correlation = " + correl + " (default value before comupting is -3");
                System.out.println("unit a id = " + a);
                System.out.println("unit b id = " + b);
                ce.printStackTrace();
            } catch (Exception e) {
                System.out.println("mapNeuron a toString()= " + mapNeurons.get(a).toString());
                System.out.println("mapNeuron b toString()= " + mapNeurons.get(b).toString());
                System.out.println("unit a getName= " + ((SpecUnit) mapUnits.get(a)).getName());
                e.printStackTrace();
            }
        }
        return correl;
    }

    /**
     *
     * transpon whole input data table - in each row is one variable
     * @param vectors of inputs
     * @return transpon input data table
     */
    private double[][] getInputColumn(double[][] vectors) {
        double[][] columns = new double[vectors[0].length][vectors.length];
        for (int i = 0; i < columns.length; i++) {
            for (int j = 0; j < columns[0].length; j++) {
                columns[i][j] = vectors[j][i];
            }
        }
        return columns;
    }

    /**
     *  Compute nr. of layers in GAME/GMDH network
     * @return nr. of layers minus 2
     */
    private int computeLastLayer() {
        int ll = gmdhNet.getLastLayer();
        if (gmdhNet.getLayer(0) == null) {
            if (gamNet.getLayer(ll + 1) == null) {
                ll--;
            }
        } else if (gmdhNet.getLayer(ll + 1) == null) {
            ll--;
        }
        return ll;
    }

    /**
     * prepare input vectors to easy use in evaluation process
     * @return vectors / double[nr.of vectors][size of vector]  / - input data set in array
     */
    private double[][] getInputVectors() {
        double[][] vectors = new double[data.getGroups()][data.getINumber()];
        for (int j = 0; j < vectors.length; j++) {
            for (int k = 0; k < vectors[0].length; k++) {
                vectors[j][k] = ((Instance) data.group.elementAt(j)).getiVal(k);
            }
        }
        return vectors;
    }

    /**
     * Compute correlation between x and y
     * @param x SpecUnit
     * @param y SpecUnit
     * @return   Correlation of x and y variables
     */
    double computeCorrelation(SpecUnit x, SpecUnit y) {
        double r;
        r = computeSxy(x, y) / Math.sqrt(x.getSxx() * y.getSxx());
        return r;
    }

    /**
     *
     * @param x Array for computing of support variable for correlation
     * @param sumX  is sum of all elements in x array
     * @return support variable for correlation
     */
    double computeSxx(double[] x, double sumX) {
        double sxx;
        double sumQuadr = 0;
        for (double aX : x) {
            sumQuadr += aX * aX;
        }
        sxx = sumQuadr - (sumX * sumX / x.length);
        return sxx;
    }

    /**
     *
     * @param a SpecUnit with stored data for Sxy computation
     * @param b SpecUnit with stored data for Sxy computation
     * @return Sxy - support variable for correlation
     */
    double computeSxy(SpecUnit a, SpecUnit b) {
        double sxy;
        double sumMult = 0;
        for (int i = 0; i < a.getX().length; i++) {
            sumMult += a.getX()[i] * b.getX()[i];
        }
        sxy = sumMult - (a.getSumX() * b.getSumX() / a.getX().length);
        return sxy;
    }

    /**
     * Compute and return sum of input double array
     * @param x double[] of responses
     * @return double, SUM(x)
     */
    private double computeSumX(double[] x) {
        double sumX = 0;
        for (double aX : x) {
            sumX += aX;
        }
        return sumX;
    }

    /**
     *  Class for storring of unit parrameters
     */
    public class SpecUnit {

        String name;

        double[] x;

        double sumX;

        double sxx;

        /**
         *
         * @param name - name of the unit
         * @param x    - double[] of unit responses on input data
         */
        private SpecUnit(String name, double[] x) {
            this.name = name;
            this.x = x;
            sumX = computeSumX(x);
            sxx = computeSxx(x, sumX);
        }

        public String getName() {
            return name;
        }

        public double[] getX() {
            return x;
        }

        public double getSumX() {
            return sumX;
        }

        public double getSxx() {
            return sxx;
        }
    }

    public GAMEnetwork getGamNet() {
        return gamNet;
    }

    public Map getMapUnits() {
        return mapUnits;
    }

    public Map getCorrelationMap() {
        return mapCorrel;
    }

    /**
     * set map of all used neurons  -  becaus of prunning process = remove some unused neurons
     *
     */
    private void createMapOfUsedNeurons() {
        Set<Integer> klice = vnet.getHidden().keySet();
        for (int s : klice) {
            mapNeurons.put(Integer.toString(s), ((game.gui.vis.Node) vnet.getHidden().get(s)).getRealRef());
        }
    }

    /**
     * Compute correlation for each node wiht its parents(inputs)
     */
    private void computeAllCorrelations() {
        Set<String> klice = mapNeurons.keySet();
        Neuron n;
        for (String s : klice) {
            n = (Neuron) mapNeurons.get(s);
            int idd;
            double correlation;
            for (int i = 0; i < n.getInputs(); i++) {
                idd = n.getParentId(i);
                correlation = getCorrelation(s, ((Integer) (2 * idd)).toString());
                if (!(correlation <= 1 && correlation >= -1)) {
                    System.out.println("Correlation: " + s + " and " + (2 * idd) + " is " + correlation + " !!!!!!!!!!!!_______!!!!!!!!!!____!!!!!!!!!!");
                }
                System.out.println("Correlation: " + s + " and " + (2 * idd) + " is " + correlation);
            }
        }
    }

    /**
     * compute Feature Ranking from partitional correlation among inputs and output inside of whole structure
     * @param actId actual processed neuron id
     * @param prevPlusCorrel array for temporary ranks
     * @param prevMultCorrel array for temporary ranks
     * @param prevMultPlusCorrel array for temporary ranks
     * @param deep deep in network structure from output neuron view
     * @param prevMinFuzzyCorrel
     * @param prevCFbasicValue
     * @param prevCFcombiValue
     */
    private void computeOverAllRanks(int actId, double prevPlusCorrel, double prevMultCorrel, double prevMultPlusCorrel, int deep, double prevMinFuzzyCorrel, double prevCFbasicValue, double prevCFcombiValue) {
        Neuron n = (Neuron) mapNeurons.get(((Integer) (actId * 2)).toString());
        if (n != null) {
            int idd;
            double correlation;
            for (int i = 0; i < n.getInputs(); i++) {
                idd = n.getParentId(i);
                correlation = getCorrelation(((Integer) (2 * n.getId())).toString(), ((Integer) (2 * idd)).toString());
                System.out.println("correl " + i + " je: " + correlation);
                if (idd < number_of_inputs) {
                    if (!frMultBoolArr[idd]) frMultBoolArr[idd] = true;
                    frPlus[idd] = frPlus[idd] + (Math.abs(correlation) + prevPlusCorrel);
                    frMult[idd] = frMult[idd] * Math.abs(correlation) * Math.abs(prevMultCorrel);
                    frMultPlus[idd] = frMultPlus[idd] + Math.abs(correlation) * Math.abs(prevMultPlusCorrel);
                    frFuzzy[idd] = computeAbsMax(frFuzzy[idd], computeAbsMin(correlation, prevMinFuzzyCorrel));
                    frCFbasic[idd] = computeAbsMax(frCFbasic[idd], prevCFbasicValue * correlation);
                    frCFcombiMax[idd] = Math.max(frCFcombiMax[idd], Math.abs(correlation) + (1 - Math.abs(correlation)) * prevCFcombiValue);
                    frCFcombiMin[idd] = Math.min(frCFcombiMin[idd], Math.abs(correlation) + (1 - Math.abs(correlation)) * prevCFcombiValue);
                    System.out.println("End Path______________________ ");
                } else {
                    computeOverAllRanks(idd, prevPlusCorrel + Math.abs(correlation), prevMultCorrel * Math.abs(correlation), prevMultPlusCorrel * Math.abs(correlation), deep++, computeAbsMin(correlation, prevMinFuzzyCorrel), prevCFbasicValue * correlation, Math.abs(correlation) + (1 - Math.abs(correlation)) * prevCFcombiValue);
                }
            }
        }
    }

    /**
     *
     * @param a first double value
     * @param b second double value
     * @return non-abs double value of abs minimum from a and b
     */
    private double computeAbsMin(double a, double b) {
        return Math.abs(a) <= Math.abs(b) ? a : b;
    }

    private double computeAbsMax(double a, double b) {
        return Math.abs(a) >= Math.abs(b) ? a : b;
    }

    private void correctMultRanks() {
        System.out.println("frMultBoolArray: " + Arrays.toString(frMultBoolArr));
        System.out.println("frMult    Array: " + Arrays.toString(frMult));
        System.out.println("frMultPlus     : " + Arrays.toString(frMultPlus));
        for (int i = 0; i < number_of_inputs; i++) {
            if (!frMultBoolArr[i]) {
                frMult[i] = 0;
                frMultPlus[i] = 0;
                frCFcombiMin[i] = 0;
            }
        }
        System.out.println("----- after correction----------------------------");
        System.out.println("frMultBoolArray: " + Arrays.toString(frMultBoolArr));
        System.out.println("frMult    Array: " + Arrays.toString(frMult));
        System.out.println("frMultPlus     : " + Arrays.toString(frMultPlus));
        System.out.println("frFUZZY        : " + Arrays.toString(frFuzzy));
        System.out.println("frCFbasic      : " + Arrays.toString(frCFbasic));
        System.out.println("frCFcombiMax   : " + Arrays.toString(frCFcombiMax));
        System.out.println("frCFcombiMin   : " + Arrays.toString(frCFcombiMin));
    }

    private double[][] computeCrossCorrelationsMatrix() {
        if (number_of_inputs == 0) return null; else {
            double[][] m = new double[number_of_inputs][number_of_inputs];
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j <= i; j++) {
                    if (i == j) m[i][j] = 1; else {
                        m[i][j] = getCorrelation(Integer.toString(2 * i), Integer.toString(2 * j));
                        m[j][i] = m[i][j];
                    }
                }
            }
            return m;
        }
    }

    public class CorrelationException extends Exception {

        public CorrelationException() {
            super("Exception in method getCorrelation( a, b)");
        }
    }

    public class OutOfRangeCorrelationException extends Exception {

        public OutOfRangeCorrelationException() {
            super("Correlation is bigger then abs|1| ");
        }
    }
}
