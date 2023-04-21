package kr.ac.kaist.swrc.corenet.data.vo;

import java.util.ArrayList;

/**
 * <p>WnLink.java</p>
 * <p>wnLink object is the mapping data between CoreNet concept and WordNet synset. wnLink contains ArrayList of synsets which is mapped to
 * a particular korterm number and the relation between a concept korterm number indicates and ArrayList of synsets.
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: SWRC, KAIST., Ltd</p>
 * 
 * @author <a href="mailto:his.barnabas@kaist.ac.kr">Kim, Kyoungryol</a>, <a href="mailto:dhjin@world.kaist.ac.kr">Jin, Duhyeon</a>
 * @version v 1.0 2011. 1. 13.
 */
public class WnLink {

    String kortermnum;

    String conceptEn;

    String wnSynsetRel;

    ArrayList<WnSynset> synsetList;

    /**
	 * A Constructor which creates WnLink object.
	 */
    public WnLink() {
    }

    /**
	 * Retrieve Korterm number of WordNet-CoreNet link.
	 * @return korterm number
	 */
    public String getKortermnum() {
        return kortermnum;
    }

    /**
	 * Set korterm number of WordNet-CoreNet link
	 * @param kortermnum korterm number of WordNet-CoreNet link
	 */
    public void setKortermnum(String kortermnum) {
        this.kortermnum = kortermnum;
    }

    /**
	 * Retrieve English concept name
	 * @return English concept name
	 */
    public String getConceptEn() {
        return conceptEn;
    }

    /**
	 * Set English concept name
	 * @param conceptEn English concept name
	 */
    public void setConceptEn(String conceptEn) {
        this.conceptEn = conceptEn;
    }

    /**
	 * Retrieve the relation between CjkConcept and WordNet synset
	 * @return the relation between CjkConcept and WordNet synset
	 */
    public String getWnSynsetRel() {
        return wnSynsetRel;
    }

    /**
	 * Set the relation between CjkConcept and WordNet synset
	 * @param wnSynsetRel the relation between CjkConcept and WordNet synset
	 */
    public void setWnSynsetRel(String wnSynsetRel) {
        this.wnSynsetRel = wnSynsetRel;
    }

    /**
	 * Retrieve the list of synset wnLink contains
	 * @return the list of synset wnLink contains
	 */
    public ArrayList<WnSynset> getSynsetList() {
        return synsetList;
    }

    /**
	 * Set the list of synset wnLink contains
	 * @param synsetList the list of synset wnLink contains
	 */
    public void setSynsetList(ArrayList<WnSynset> synsetList) {
        this.synsetList = synsetList;
    }
}
