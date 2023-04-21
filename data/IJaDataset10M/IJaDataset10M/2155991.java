package org.foafrealm.tools.sscf2js;

import java.util.Collection;
import org.corrib.jonto.wordnet.WordNetClassification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author adagze
 *
 */
public class WordNetDesc {

    Collection<WordNetClassification> wnc;

    public void toJSON(StringBuilder sb) {
        sb.append(toJSON());
    }

    @SuppressWarnings("unchecked")
    public String toJSON() {
        JSONArray ja = new JSONArray();
        for (WordNetClassification wncOne : this.wnc) {
            JSONObject jo = new JSONObject();
            jo.put("uri", wncOne.getURI());
            jo.put("name", wncOne.getName());
            jo.put("type", wncOne.getWordSense().getPOS().getLabel());
            jo.put("mean", wncOne.getDescription());
            ja.add(jo);
        }
        if (ja != null && ja.size() > 0) return ja.toString();
        return null;
    }

    /**
	 * @return Returns the wnc.
	 */
    public Collection<WordNetClassification> getWnc() {
        return wnc;
    }

    /**
	 * @param wnc The wnc to set.
	 */
    public void setWnc(Collection<WordNetClassification> wnc) {
        this.wnc = wnc;
    }
}
