package prefwork.normalizer;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.configuration.XMLConfiguration;
import prefwork.Attribute;
import prefwork.AttributeValue;
import prefwork.CommonUtils;

public class TextNormalizer implements Normalizer {

    HashMap<String, WordRatings> ratings = new HashMap<String, WordRatings>();

    int index;

    Attribute attr;

    public TextNormalizer() {
    }

    public TextNormalizer(Attribute attr) {
        init(attr);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private AttributeValue findValue(Object val) {
        if (val == null) return null;
        for (AttributeValue tempVal : attr.getValues()) {
            for (Object o : (List<Object>) tempVal.getValue()) {
                if (o == null) return null;
                if (o.equals(val)) return tempVal;
            }
        }
        return null;
    }

    public Double normalize(List<Object> o) {
        String s = (String) o.get(index);
        double rating = 0;
        int count = 0;
        for (String sec : splitString(s)) {
            if (ratings.containsKey(sec)) {
                rating += ratings.get(sec).rating;
                count++;
            }
        }
        if (count == 0) return 0.0;
        return rating / count;
    }

    protected String[] splitString(String s) {
        if (s == null) return new String[0];
        return s.split("[ ,.;!?]");
    }

    public void init(Attribute attr) {
        this.attr = attr;
        index = attr.getIndex();
        for (AttributeValue val : attr.getValues()) {
            double meanRating = 0;
            for (List<Object> rec : val.getRecords()) {
                meanRating += CommonUtils.objectToDouble(rec.get(2));
            }
            meanRating /= val.getRecords().size();
            String s = val.getValue().toString();
            for (String sec : splitString(s)) {
                if (!ratings.containsKey(sec)) {
                    ratings.put(sec, new WordRatings());
                }
                WordRatings wr = ratings.get(sec);
                wr.countRatings++;
                wr.sumRatings += meanRating;
            }
        }
        for (WordRatings wr : ratings.values()) {
            wr.rating = wr.sumRatings / wr.countRatings;
        }
    }

    public Normalizer clone() {
        TextNormalizer l = new TextNormalizer();
        return l;
    }

    public int compare(List<Object> arg0, List<Object> arg1) {
        if (normalize(arg0) > normalize(arg1)) return 1; else if (normalize(arg0) < normalize(arg1)) return -1; else return 0;
    }

    public String toString() {
        return "T";
    }

    public void configClassifier(XMLConfiguration config, String section) {
    }

    @Override
    public double compareTo(Normalizer n) {
        return 0;
    }
}

class WordRatings {

    String word;

    int countRatings = 0;

    double sumRatings = 0;

    double rating = 0;
}
