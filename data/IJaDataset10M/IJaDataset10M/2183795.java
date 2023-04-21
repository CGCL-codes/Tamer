package grobid.data;

import grobid.data.BiblioItem;
import grobid.data.PatentItem;
import grobid.data.Passage;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import grobid.data.PriorArtCitation;

/**
 *  Class for managing citation of patent bibliographical references.
 * 
 *  @author Patrice Lopez
 */
public class PriorArtCitation {

    private PatentItem patent = null;

    private BiblioItem npl = null;

    private List<Passage> passages = null;

    private String category = null;

    private String comment = null;

    private String rawCitation = null;

    private String rawClaims = null;

    public PatentItem getPatent() {
        return patent;
    }

    public void setPatent(PatentItem item) {
        patent = item;
    }

    public BiblioItem getNPL() {
        return npl;
    }

    public void setNPL(BiblioItem item) {
        npl = item;
    }

    public List<Passage> getPassages() {
        return passages;
    }

    public void setPassages(List<Passage> pass) {
        passages = pass;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String cat) {
        category = cat;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comm) {
        comment = comm;
    }

    public String getRawCitation() {
        return rawCitation;
    }

    public void setRawCitation(String raw) {
        rawCitation = raw;
    }

    public String getRawClaims() {
        return rawClaims;
    }

    public void setRawClaims(String raw) {
        rawClaims = raw;
    }
}
