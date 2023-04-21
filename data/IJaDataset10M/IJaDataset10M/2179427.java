package internal.techtree;

import internal.GlobalIDManager;
import internal.civilization.Civilization;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class Technology {

    protected Integer techID;

    protected double cost;

    protected HashSet<Integer> preRequisites = new HashSet();

    protected String name;

    public abstract Technology getCopy();

    public Technology(Integer id) {
        this.techID = id;
    }

    public boolean canBeResearched(ArrayList<Integer> alreadyResearched) {
        for (Integer id : preRequisites) {
            if (!alreadyResearched.contains(id)) {
                return false;
            }
        }
        return true;
    }

    public Integer getId() {
        return techID;
    }

    public abstract void updateCivilizationAfterResearch(Civilization civIn);

    public double getCost() {
        return cost;
    }

    public void addPrerequisite(Integer id) {
        this.preRequisites.add(id);
    }

    public String toString() {
        return techID + " " + name;
    }
}
