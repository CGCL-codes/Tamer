package ar.com.oddie.core.indexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Esta clase prcesa una lista de terminos agrupandolos y contando la frecuencia
 * de cada uno en dicha lista.
 * </p>
 * 
 * @author hernan
 */
public class TermsProcessor {

    public Map<String, Float> process(List<String> terms) {
        Map<String, Float> result = new HashMap<String, Float>();
        float maxTermFreq = 1;
        for (String term : terms) {
            if (result.containsKey(term)) {
                float newValue = result.get(term).intValue() + 1;
                if (newValue > maxTermFreq) maxTermFreq = newValue;
                result.put(term, newValue);
            } else {
                result.put(term, 1F);
            }
        }
        normalizeTermsFreq(maxTermFreq, result);
        return result;
    }

    private void normalizeTermsFreq(Float maxTermFreq, Map<String, Float> terms) {
        for (String key : terms.keySet()) {
            terms.put(key, terms.get(key) / maxTermFreq);
        }
    }
}
