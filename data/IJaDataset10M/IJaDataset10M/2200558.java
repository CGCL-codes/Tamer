package sf.net.sinve.engine;

import java.util.ArrayList;
import java.util.Vector;

public class InvariantClass {

    ArrayList<AssignedInvariantVariable> invariantVariables = new ArrayList<AssignedInvariantVariable>();

    private String name;

    public InvariantClass(String n) {
        this.name = n;
    }

    /**
   * @param name
   *          the name to set
   */
    public void setName(String name) {
        this.name = name;
    }

    public void updatePercents() {
        for (int i = 0; i < invariantVariables.size(); i++) {
            AssignedInvariantVariable ai = invariantVariables.get(i);
            if (invariantVariables.get(i).hasTie()) {
                int sum = iterateTies(ai, 0);
                if (sum == 0) sum = 1;
                System.out.println("sum " + sum);
                ai.setPercent(sum);
            } else ai.setPercent(100);
        }
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public boolean removeVariable(String variableName) {
        for (int i = 0; i < invariantVariables.size(); i++) {
            AssignedInvariantVariable ai = invariantVariables.get(i);
            if (variableName.equals(ai.toString())) {
                invariantVariables.remove(i);
                return true;
            }
        }
        return false;
    }

    public void addVariable(AssignedInvariantVariable i) {
        invariantVariables.add(i);
    }

    public ArrayList<AssignedInvariantVariable> getVariables() {
        return invariantVariables;
    }

    public ArrayList<String> getVariablesAsStringArray() {
        ArrayList<String> variables = new ArrayList<String>();
        int i;
        for (i = 0; i < this.invariantVariables.size(); i++) variables.add(this.invariantVariables.get(i).toString());
        return variables;
    }

    public int getVariableCount() {
        return invariantVariables.size();
    }

    public int getGreenVariableCount() {
        int count = 0;
        for (int i = 0; i < invariantVariables.size(); i++) {
            if (invariantVariables.get(i).hasTie()) {
                iterateTies(invariantVariables.get(i).getTiedVariable(), 0);
            }
            if (invariantVariables.get(i).isGreen()) count++;
        }
        return count;
    }

    public int iterateTies(AssignedInvariantVariable a, int sum) {
        sum += a.getCurrentBaseValue();
        if (a.hasTie()) {
            sum = iterateTies(a.getTiedVariable(), sum);
        } else System.out.print("\n");
        return sum;
    }

    public String getNonValidVariables() {
        String names = "";
        for (int i = 0; i < invariantVariables.size(); i++) {
            if (!invariantVariables.get(i).isGreen()) names += invariantVariables.get(i).toString() + " ";
        }
        return names;
    }

    public boolean isValid() {
        for (int i = 0; i < invariantVariables.size(); i++) {
            if (!invariantVariables.get(i).isGreen()) return false;
        }
        return true;
    }
}
