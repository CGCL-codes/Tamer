package ugliML.test;

import java.util.List;
import ugliML.AbstractFeature;

public class EqualityFeature extends AbstractFeature {

    public double value(List x) {
        Object base = x.get(0);
        for (int i = 1; i < x.size(); i++) if (!base.equals(x.get(i))) return 0;
        return 1;
    }

    public EqualityFeature(String name, String domainType) {
        this.name = name;
    }
}
