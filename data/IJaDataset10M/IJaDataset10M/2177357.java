package unbbayes.learning;

import java.util.ArrayList;
import unbbayes.prs.Node;
import unbbayes.prs.bn.LearningNode;

public class CBLB extends CBLToolkit {

    private ArrayList<int[]> esFinal;

    public CBLB(ArrayList<Node> variables, int[][] dataBase, int[] vector, long caseNumber, String param, boolean compacted) {
        this.variablesVector = variables;
        double epsilon;
        LearningNode variable;
        esFinal = new ArrayList<int[]>();
        this.separators = new ArrayList<Object[]>();
        this.es = new ArrayList<int[]>();
        this.variablesVector = variables;
        this.dataBase = dataBase;
        this.vector = vector;
        this.vector = vector;
        this.caseNumber = caseNumber;
        try {
            this.epsilon = Double.parseDouble(param);
            expand(scketch());
            refine();
            findVStructures();
            ruleCBL1();
            ruleCBL2();
            mapStructure();
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    public CBLB(ArrayList<Node> variables, int[][] dataBase, int[] vector, long caseNumber, String param, boolean compacted, int classex) {
        this.variablesVector = variables;
        esFinal = new ArrayList();
        this.separators = new ArrayList();
        this.es = new ArrayList();
        this.variablesVector = variables;
        this.dataBase = dataBase;
        this.vector = vector;
        this.vector = vector;
        this.caseNumber = caseNumber;
        try {
            this.epsilon = Double.parseDouble(param);
            expand(scketch(classex), classex);
            refine(classex);
            findVStructures();
            ruleCBL1();
            ruleCBL2();
            mapStructure();
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    private void refine() {
        int[] peace;
        ArrayList<int[]> esx;
        for (int i = 0; i < es.size(); i++) {
            peace = es.get(i);
            esx = (ArrayList) es.clone();
            esx.remove(i);
            if (findWays(peace[0], peace[1], esx).size() > 0 && !needConnect(peace[0], peace[1], esx, 1)) {
                es.remove(i);
                i--;
            }
        }
    }

    private void refine(int classex) {
        int[] peace;
        ArrayList esx;
        for (int i = 0; i < es.size(); i++) {
            peace = (int[]) es.get(i);
            esx = (ArrayList) es.clone();
            esx.remove(i);
            if (findWays(peace[0], peace[1], esx).size() > 0 && !needConnect(peace[0], peace[1], esx, 1, classex)) {
                es.remove(i);
                i--;
            }
        }
    }

    private void mapStructure() {
        int[] peace;
        LearningNode var1;
        LearningNode var2;
        for (int i = 0; i < es.size(); i++) {
            peace = (int[]) es.get(i);
            var1 = (LearningNode) variablesVector.get(peace[1]);
            var2 = (LearningNode) variablesVector.get(peace[0]);
            var1.adicionaPai(var2);
            var2.adicionaPai(var1);
        }
        for (int i = 0; i < esFinal.size(); i++) {
            peace = esFinal.get(i);
            var1 = (LearningNode) variablesVector.get(peace[1]);
            var2 = (LearningNode) variablesVector.get(peace[0]);
            var1.adicionaPai(var2);
        }
    }

    private ArrayList scketch() {
        int n = this.variablesVector.size();
        double imAux;
        ArrayList<double[]> ls = new ArrayList<double[]>();
        for (int i = 0; i < n; i++) {
            for (int k = i + 1; k < n; k++) {
                imAux = mutualInformation((LearningNode) variablesVector.get(i), (LearningNode) variablesVector.get(k));
                if (imAux > epsilon) {
                    ls.add(new double[] { imAux, i, k });
                }
            }
        }
        sort(ls);
        double[] peace;
        for (int i = 0; i < ls.size(); i++) {
            peace = (double[]) ls.get(i);
            if (findWays((int) peace[1], (int) peace[2], es).size() == 0) {
                es.add(new int[] { (int) peace[1], (int) peace[2] });
                ls.remove(i);
                i--;
            }
        }
        return ls;
    }

    protected double conditionalMutualInformation(int v1, int v2, int classe) {
        int ri = ((LearningNode) variablesVector.get(v1)).getEstadoTamanho();
        int rk = ((LearningNode) variablesVector.get(v2)).getEstadoTamanho();
        int rj = ((LearningNode) variablesVector.get(classe)).getEstadoTamanho();
        double pjik;
        double cpjik;
        double im = 0.0;
        int[] nj = new int[rj];
        int[][][] njik = new int[rj][ri][rk];
        int[][] nji = new int[rj][ri];
        int[][] njk = new int[rj][rk];
        double[][] pji = new double[rj][ri];
        double[][] pjk = new double[rj][rk];
        int j = 0;
        int f;
        int il;
        int kl;
        int nt = 0;
        for (int id = 0; id < caseNumber; id++) {
            f = compacted ? vector[id] : 1;
            il = dataBase[id][v1];
            kl = dataBase[id][v2];
            j = dataBase[id][classe];
            njik[j][il][kl] += f;
            nji[j][il] += f;
            njk[j][kl] += f;
            nj[j] += f;
            nt += f;
        }
        for (j = 0; j < rj; j++) {
            for (il = 0; il < ri; il++) {
                pji[j][il] = (1 + nji[j][il]) / (double) (ri + nj[j]);
            }
            for (kl = 0; kl < rk; kl++) {
                pjk[j][kl] = (1 + njk[j][kl]) / (double) (rk + nj[j]);
            }
            for (il = 0; il < ri; il++) {
                for (kl = 0; kl < rk; kl++) {
                    pjik = (1 + njik[j][il][kl]) / (double) (ri * rk * rj + nt);
                    cpjik = (1 + njik[j][il][kl]) / (double) (ri * rk + nj[j]);
                    im += pjik * (log2(cpjik) - log2(pji[j][il]) - log2(pjk[j][kl]));
                }
            }
        }
        nj = null;
        njk = null;
        nji = null;
        njik = null;
        pji = null;
        pjk = null;
        return im;
    }

    private ArrayList scketch(int classex) {
        int n = this.variablesVector.size();
        double imAux;
        ArrayList ls = new ArrayList();
        for (int i = 0; i < n; i++) {
            for (int k = i + 1; k < n; k++) {
                imAux = conditionalMutualInformation(i, k, classex);
                if (imAux > epsilon) {
                    ls.add(new double[] { imAux, i, k });
                }
            }
        }
        sort(ls);
        double[] peace;
        for (int i = 0; i < ls.size(); i++) {
            peace = (double[]) ls.get(i);
            if (findWays((int) peace[1], (int) peace[2], es).size() == 0) {
                es.add(new int[] { (int) peace[1], (int) peace[2] });
                ls.remove(i);
                i--;
            }
        }
        return ls;
    }

    private void expand(ArrayList ls) {
        double[] peace;
        for (int i = 0; i < ls.size(); i++) {
            peace = (double[]) ls.get(i);
            System.out.println("Tentativa = " + (int) peace[1] + ", " + (int) peace[2]);
            if (needConnect((int) peace[1], (int) peace[2], es, 1)) {
                es.add(new int[] { (int) peace[1], (int) peace[2] });
            }
        }
    }

    private void expand(ArrayList ls, int classex) {
        double[] peace;
        for (int i = 0; i < ls.size(); i++) {
            peace = (double[]) ls.get(i);
            System.out.println("Tentativa = " + (int) peace[1] + ", " + (int) peace[2]);
            if (needConnect((int) peace[1], (int) peace[2], es, 1, classex)) {
                es.add(new int[] { (int) peace[1], (int) peace[2] });
            }
        }
    }

    private void findVStructures() {
        boolean flag = false;
        ArrayList sep;
        Object[] aux;
        int[] vars1;
        int[] vars;
        int[] indexes;
        for (int i = 0; i < variablesVector.size(); i++) {
            for (int j = 0; j < es.size(); j++) {
                vars = (int[]) es.get(j);
                if (vars[0] == i) {
                    for (int k = 0; k < es.size(); k++) {
                        if (k != j) {
                            vars1 = (int[]) es.get(k);
                            if (vars1[1] == vars[1]) {
                                for (int w = 0; !flag && w < separators.size(); w++) {
                                    aux = (Object[]) separators.get(w);
                                    indexes = (int[]) aux[0];
                                    sep = (ArrayList) aux[1];
                                    if (indexes.equals(new int[] { vars[0], vars1[0] })) {
                                        if (notContain(sep, vars[1])) {
                                            esFinal.add(new int[] { vars[0], vars[1] });
                                            esFinal.add(new int[] { vars1[0], vars1[1] });
                                            remove(new int[] { vars1[0], vars1[1] });
                                            remove(new int[] { vars[0], vars[1] });
                                            flag = true;
                                        }
                                    }
                                }
                                if (!flag) {
                                    esFinal.add(new int[] { vars[0], vars[1] });
                                    esFinal.add(new int[] { vars1[0], vars1[1] });
                                    remove(new int[] { vars1[0], vars1[1] });
                                    remove(new int[] { vars[0], vars[1] });
                                    flag = false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean notContain(ArrayList sep, int k) {
        for (int i = 0; i < sep.size(); i++) {
            if (((Integer) sep.get(i)).intValue() == k) {
                return false;
            }
        }
        return true;
    }

    private void ruleCBL1() {
        int[] vars;
        int[] vars1;
        for (int i = 0; i < variablesVector.size(); i++) {
            for (int j = 0; j < esFinal.size(); j++) {
                vars = (int[]) esFinal.get(j);
                if (vars[0] == i) {
                    for (int k = 0; k < es.size(); k++) {
                        if (k != j) {
                            vars1 = (int[]) es.get(k);
                            if (vars1[0] == vars[1]) {
                                if (notHave(new int[] { vars[0], vars1[1] })) {
                                    remove(vars1);
                                    esFinal.add(vars1);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean notHave(int[] peace) {
        for (int i = 0; i < es.size(); i++) {
            if (((int[]) es.get(i)).equals(peace)) {
                return false;
            }
        }
        return true;
    }

    private void remove(int[] peace) {
        int[] aux;
        for (int i = 0; i < es.size(); i++) {
            aux = (int[]) es.get(i);
            if (aux[0] == peace[0] && aux[1] == peace[1]) {
                es.remove(i);
                return;
            }
        }
    }

    private void ruleCBL2() {
        int[] peace;
        for (int i = 0; i < es.size(); i++) {
            peace = (int[]) es.get(i);
            if (orientedWay(peace)) {
                remove(peace);
            }
        }
    }

    private boolean orientedWay(int[] peace) {
        for (int i = 0; i < esFinal.size(); i++) {
            if (((int[]) esFinal.get(i)).equals(peace)) {
                return true;
            }
        }
        return false;
    }
}
