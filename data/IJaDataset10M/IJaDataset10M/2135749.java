package prefwork.normalizer;

import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import prefwork.Attribute;
import prefwork.AttributeValue;
import prefwork.CommonUtils;
import prefwork.representant.AvgRepresentant;
import prefwork.representant.Representant;

public class THBooleanNormalizer implements Normalizer {

    public static final int SIMILARITY = 1;

    public static final int MEAN = 2;

    public static final int NULL = 3;

    Attribute attr;

    SimilaritySearch sim = new SimilaritySearch();

    int useSim = MEAN;

    Representant representant = new AvgRepresentant();

    int index;

    Double mean;

    public THBooleanNormalizer() {
    }

    public THBooleanNormalizer(Attribute attr) {
        this.attr = attr;
        sim.init(attr);
    }

    public Double normalize(List<Object> o) {
        if (o == null) return null;
        List<AttributeValue> values = attr.getValues();
        if (values == null) {
            if (useSim == MEAN) return mean; else if (useSim == SIMILARITY) return sim.normalize(o); else if (useSim == NULL) return null;
            return null;
        }
        for (AttributeValue attrVal : values) {
            if (attrVal.getValue().equals(o.get(index))) return attrVal.getRepresentant();
        }
        if (useSim == MEAN) return mean; else if (useSim == SIMILARITY) return sim.normalize(o); else if (useSim == NULL) return null;
        return null;
    }

    public int compare(List<Object> arg0, List<Object> arg1) {
        if (normalize(arg0) > normalize(arg1)) return 1; else if (normalize(arg0) < normalize(arg1)) return -1; else return 0;
    }

    public void init(Attribute attribute) {
        index = attribute.getIndex();
        this.attr = attribute;
        mean = 0.0;
        for (AttributeValue val : attr.getValues()) {
            val.setRepresentant(representant.getRepresentant(val.getRatings()));
            mean += representant.getRepresentant(val.getRatings());
        }
        if (attr.getValues().size() != 0) mean /= attr.getValues().size();
        if (useSim == SIMILARITY) sim.init(attr);
    }

    public Normalizer clone() {
        THBooleanNormalizer representantNormalizer = new THBooleanNormalizer();
        representantNormalizer.representant = representant;
        representantNormalizer.useSim = useSim;
        return representantNormalizer;
    }

    public String toString() {
        return "B" + representant.toString() + useSim;
    }

    public Representant getRepresentant() {
        return representant;
    }

    public void setRepresentant(Representant representant) {
        this.representant = representant;
    }

    public void configClassifier(XMLConfiguration config, String section) {
        Configuration methodConf = config.configurationAt(section);
        if (methodConf.containsKey("representant.class")) {
            String normalizerName = methodConf.getString("representant.class");
            representant = (Representant) CommonUtils.getInstance(normalizerName);
            representant.configClassifier(config, section + ".representant");
        } else representant = new AvgRepresentant();
        if (methodConf.containsKey("useSim")) {
            useSim = methodConf.getInt("useSim");
        }
    }

    @Override
    public double compareTo(Normalizer n) {
        if (!(n instanceof THBooleanNormalizer)) return 0;
        THBooleanNormalizer n2 = (THBooleanNormalizer) n;
        List<AttributeValue> values = attr.getValues();
        List<AttributeValue> n2values = n2.attr.getValues();
        double diff = 0;
        int count = 0;
        for (AttributeValue av1 : values) {
            for (AttributeValue av2 : n2values) {
                if (av1.getValue().equals(av2.getValue())) {
                    count++;
                    diff += Math.abs(av1.getRepresentant() - av2.getRepresentant());
                }
            }
        }
        if (count == 0) return 0;
        return 1 - diff / (5 * count);
    }
}
