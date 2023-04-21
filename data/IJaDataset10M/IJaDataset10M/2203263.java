package opennlp.grok.ml.dectree;

import java.util.*;

/**
 * A special map that computes values on the fly basied on FeatureComputers.
 * So you don't have to compute a feature value until it is asked for and
 * therefore save some work.
 *
 * @author      Gann Bierner
 * @version $Revision: 1.3 $, $Date: 2002/02/08 12:13:36 $
 */
public class DTreeFeatureMap extends HashMap {

    Map computers = new HashMap();

    Object[] data;

    public DTreeFeatureMap(Collection c) {
        this();
        for (Iterator i = c.iterator(); i.hasNext(); ) addFeatureComputer((DTreeFeatureComputer) i.next());
    }

    public DTreeFeatureMap() {
    }

    public void addFeatureComputer(DTreeFeatureComputer comp) {
        String[] features = comp.getFeatures();
        for (int i = 0; i < features.length; i++) computers.put(features[i], comp);
    }

    public void setData(Object[] d) {
        clear();
        data = d;
    }

    public Object get(Object o) {
        if (!containsKey(o)) {
            DTreeFeatureComputer computer = (DTreeFeatureComputer) computers.get((String) o);
            computer.compute(data, this);
        }
        return super.get(o);
    }
}
