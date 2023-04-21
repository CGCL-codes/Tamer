package be.kuleuven.VTKfakbarCWA1.model.sales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  @author Simon
 *	An action is a list of actionrules, with a start en endDate
 */
public class Action {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((rulesList == null) ? 0 : rulesList.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((stopDate == null) ? 0 : stopDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Action other = (Action) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (rulesList == null) {
            if (other.rulesList != null) return false;
        } else if (!rulesList.equals(other.rulesList)) return false;
        if (startDate == null) {
            if (other.startDate != null) return false;
        } else if (!startDate.equals(other.startDate)) return false;
        if (stopDate == null) {
            if (other.stopDate != null) return false;
        } else if (!stopDate.equals(other.stopDate)) return false;
        return true;
    }

    private String name;

    private Date startDate;

    private Date stopDate;

    private ArrayList<ActionRule> rulesList;

    public Action(String name, Date start, Date stop) {
        this.name = name;
        startDate = start;
        stopDate = stop;
        rulesList = new ArrayList<ActionRule>();
    }

    public String getName() {
        return name;
    }

    public Date GetStartDate() {
        return startDate;
    }

    public Date GetStopDate() {
        return stopDate;
    }

    public void AddRule(ActionRule rule) {
        rulesList.add(rule);
    }

    public void DeleteRule(ActionRule rule) {
        if (rulesList.contains(rule)) {
            rulesList.remove(rule);
        }
    }

    public ArrayList<ActionRule> getRules() {
        return rulesList;
    }
}
