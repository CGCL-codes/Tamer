package gov.lanl.Analysis.caddy;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;

/**
 * Symptom cluster parameters.
 * @author $Author: dwokoun $
 * @version $Id: SymptomCluster.java 2724 2003-08-19 20:04:13Z dwokoun $
 */
public class SymptomCluster extends Symptom {

    /** Symptoms making up the cluster */
    private ArrayList symptoms = new ArrayList();

    public SymptomCluster(List symptoms) {
        super();
        StringBuffer code = new StringBuffer();
        Iterator i = symptoms.iterator();
        while (i.hasNext()) {
            Symptom symptom = (Symptom) i.next();
            if (code.length() > 0) code.append('+');
            code.append(symptom.getCode());
        }
        setCode(code.toString());
        this.symptoms.addAll(symptoms);
    }

    public SymptomCluster(String code, String description) {
        super(code, description);
    }

    public int getDimension() {
        return this.symptoms.size();
    }

    public List getSymptoms() {
        return Collections.unmodifiableList(this.symptoms);
    }

    public void addSymptom(Symptom symptom) {
        if (symptom == null) throw new NullPointerException("Cannot add null symptom to cluster.");
        this.symptoms.add(symptom);
        setCode(getCode() + '+' + symptom.getCode());
    }

    public void removeSymptom(Symptom symptom) {
        this.symptoms.remove(symptom);
    }

    public boolean contains(Symptom symptom) {
        return this.symptoms.contains(symptom);
    }

    public SymptomCluster merge(SymptomCluster cluster) {
        ArrayList symptoms = new ArrayList(getSymptoms());
        symptoms.addAll(cluster.getSymptoms());
        return new SymptomCluster(symptoms);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(super.toString());
        result.append('[');
        for (int i = 0; i < this.symptoms.size(); i++) {
            if (i > 0) result.append(',');
            result.append(this.symptoms.get(i).toString());
        }
        result.append(']');
        return result.toString();
    }
}
