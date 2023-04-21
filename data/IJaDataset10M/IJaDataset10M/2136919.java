package org.corrib.s3b.sscf.tools.sscf2js;

import java.util.Collection;
import org.corrib.jonto.thesaurus.ThesaurusEntry;
import org.corrib.jonto.wordnet.WordNetContext;
import org.corrib.jonto.wordnet.WordNetEntry;
import org.corrib.jonto.wordnet.WordSense;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author adagze
 *
 */
public class WordNetDesc {

    Collection<ThesaurusEntry> wnc;

    public void toJSON(StringBuilder sb) {
        sb.append(toJSON());
    }

    @SuppressWarnings("unchecked")
    public String toJSON() {
        JSONArray ja = new JSONArray();
        for (ThesaurusEntry resOne : this.wnc) {
            WordNetEntry wne = null;
            if (resOne.getContext().getLabel().equals("WordNet")) {
                wne = (WordNetEntry) resOne;
            }
            JSONObject jo = new JSONObject();
            jo.put("uri", resOne.getURI().toString());
            jo.put("name", resOne.getLabel());
            if (wne != null) {
                WordSense word = new WordSense((WordNetContext) wne.getContext(), wne.getURI(), null);
                jo.put("type", word.getPOS() != null ? word.getPOS() : "");
            } else jo.put("type", "");
            jo.put("mean", resOne.getDescription());
            jo.put("thesUri", resOne.getContext().getUri().toString());
            ja.add(jo);
        }
        if (ja.size() > 0) return ja.toString();
        return null;
    }

    /**
	 * @return Returns the wnc.
	 */
    public Collection<ThesaurusEntry> getWnc() {
        return wnc;
    }

    /**
	 * @param wnc The wnc to set.
	 */
    public void setWnc(Collection<ThesaurusEntry> _wnc) {
        this.wnc = _wnc;
    }
}
