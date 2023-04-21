package br.unb.bioagents.ontology;

import jade.content.Concept;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Protege name: Sugestion
 * 
 * @author ontology bean generator
 * @version 2010/03/2, 21:51:25
 */
public class Sugestion implements Concept, Serializable {

    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    private static final long serialVersionUID = 1267577485525L;

    /**
	 * Protege name: Id
	 */
    private int id;

    public void setId(int value) {
        pcs.firePropertyChange("id", this.id, value);
        this.id = value;
    }

    public int getId() {
        return this.id;
    }

    /**
	 * Protege name: Hit
	 */
    private AlignmentHit hit;

    public void setHit(AlignmentHit value) {
        pcs.firePropertyChange("hit", (this.hit == null ? new AlignmentHit() : this.hit), value);
        this.hit = value;
    }

    public AlignmentHit getHit() {
        return this.hit;
    }

    /**
	 * Protege name: Rules
	 */
    private List<RuleIdentifier> rulesWrapper = new ArrayList<RuleIdentifier>();

    public void addRules(RuleIdentifier elem) {
        List<RuleIdentifier> oldList = this.rulesWrapper;
        rulesWrapper.add(elem);
        pcs.firePropertyChange("rules", oldList, this.rulesWrapper);
    }

    public boolean removeRules(RuleIdentifier elem) {
        List<RuleIdentifier> oldList = this.rulesWrapper;
        boolean result = rulesWrapper.remove(elem);
        pcs.firePropertyChange("rules", oldList, this.rulesWrapper);
        return result;
    }

    public void clearAllRules() {
        List<RuleIdentifier> oldList = this.rulesWrapper;
        rulesWrapper.clear();
        pcs.firePropertyChange("rules", oldList, this.rulesWrapper);
    }

    public Iterator<RuleIdentifier> getAllRulesWrapper() {
        return rulesWrapper.iterator();
    }

    public List<RuleIdentifier> getRulesWrapper() {
        return rulesWrapper;
    }

    public void setRulesWrapper(List<RuleIdentifier> l) {
        rulesWrapper = l;
    }

    public jade.util.leap.Iterator getAllRules() {
        return getRules().iterator();
    }

    public jade.util.leap.List getRules() {
        ;
        jade.util.leap.List list = new jade.util.leap.ArrayList();
        for (RuleIdentifier e : rulesWrapper) {
            list.add(e);
        }
        return list;
    }

    public void setRules(jade.util.leap.List l) {
        jade.util.leap.Iterator i = l.iterator();
        List<RuleIdentifier> list = new ArrayList<RuleIdentifier>();
        while (i.hasNext()) {
            list.add((RuleIdentifier) i.next());
        }
        rulesWrapper = list;
    }

    /**
	 * Protege name: DataBase
	 */
    private String dataBase;

    public void setDataBase(String value) {
        pcs.firePropertyChange("dataBase", (this.dataBase == null ? new String() : this.dataBase), value);
        this.dataBase = value;
    }

    public String getDataBase() {
        return this.dataBase;
    }

    /**
	 * Protege name: Algorithm
	 */
    private int algorithm;

    public void setAlgorithm(int value) {
        pcs.firePropertyChange("algorithm", this.algorithm, value);
        this.algorithm = value;
    }

    public int getAlgorithm() {
        return this.algorithm;
    }
}
