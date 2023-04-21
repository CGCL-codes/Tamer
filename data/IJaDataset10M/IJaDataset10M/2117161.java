package unbbayes.learning;

import java.util.ArrayList;
import unbbayes.prs.Node;
import unbbayes.prs.bn.LearningNode;
import unbbayes.util.SetToolkit;

public abstract class BToolkit extends PonctuationToolkit {

    protected double[][] gMatrix;

    protected boolean[] forefathers;

    protected boolean[] descendants;

    protected ArrayList<Node> variablesVector;

    protected double[][] getGMatrix() {
        return new double[variablesVector.size()][variablesVector.size()];
    }

    protected void constructGMatrix() {
        double gi = 0;
        double gk = 0;
        LearningNode variable;
        LearningNode aux;
        ArrayList<Node> parentsAux;
        for (int i = 0; i < variablesVector.size(); i++) {
            variable = (LearningNode) variablesVector.get(i);
            gi = getG(variable, variable.getPais());
            for (int j = 0; j < variablesVector.size(); j++) {
                if (i != j) {
                    aux = (LearningNode) variablesVector.get(j);
                    if (isMember(aux, variable.getPais())) {
                        gMatrix[i][j] = 0;
                    } else {
                        parentsAux = SetToolkit.clone(variable.getPais());
                        parentsAux.add(aux);
                        gk = getG(variable, parentsAux);
                        gMatrix[i][j] = gk - gi;
                    }
                } else {
                    gMatrix[i][j] = Double.NEGATIVE_INFINITY;
                }
            }
        }
    }

    protected boolean isMember(LearningNode variable, ArrayList<Node> list) {
        for (int i = 0; i < list.size(); i++) {
            if (variable.getName().equals(((LearningNode) list.get(i)).getName())) {
                return true;
            }
        }
        return false;
    }

    protected int[] maxMatrix() {
        double max;
        double maxAux;
        int vector[] = new int[2];
        max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < variablesVector.size(); i++) {
            for (int j = 0; j < variablesVector.size(); j++) {
                maxAux = gMatrix[i][j];
                if (maxAux >= max) {
                    vector[0] = i;
                    vector[1] = j;
                    max = maxAux;
                }
            }
        }
        return vector;
    }

    protected void setForefathers(LearningNode variable) {
        LearningNode aux;
        ArrayList<Node> list = variable.getPais();
        for (int j = 0; j < variablesVector.size(); j++) if (variable.getName().equals(((LearningNode) variablesVector.get(j)).getName())) {
            forefathers[j] = true;
            break;
        }
        for (int i = 0; i < list.size(); i++) {
            aux = (LearningNode) list.get(i);
            setForefathers(aux);
        }
    }

    protected void setDescendants(LearningNode variable) {
        LearningNode aux;
        LearningNode aux2;
        ArrayList<Node> list;
        for (int j = 0; j < variablesVector.size(); j++) {
            if (variable.getName().equals(((LearningNode) variablesVector.get(j)).getName())) {
                descendants[j] = true;
                break;
            }
        }
        for (int i = 0; i < variablesVector.size(); i++) {
            aux = (LearningNode) variablesVector.get(i);
            list = aux.getPais();
            for (int j = 0; j < list.size(); j++) {
                aux2 = (LearningNode) list.get(j);
                if (variable.getName().equals(aux2.getName())) {
                    setDescendants(aux);
                }
            }
        }
    }
}
