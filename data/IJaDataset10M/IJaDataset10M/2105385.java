package ytex.umls.dao;

import gnu.trove.set.TIntSet;
import gnu.trove.set.TShortSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import ytex.umls.model.UmlsAuiFirstWord;

public interface UMLSDao {

    /**
	 * pattern to match cuis and parse out their number
	 */
    public static final Pattern cuiPattern = Pattern.compile("\\AC(\\d{7})\\Z");

    /**
	 * get all aui, str from mrconso
	 */
    public List<Object[]> getAllAuiStr(String lastAui);

    public void deleteAuiFirstWord();

    public void insertAuiFirstWord(List<UmlsAuiFirstWord> listAuiFirstWord);

    public abstract Map<String, String> getNames(List<String> subList);

    /**
	 * Get the 'last' UmlsAuiFirstWord. We insert them in ascending order of
	 * auis.
	 * 
	 * @return
	 */
    public abstract String getLastAui();

    /**
	 * get a set of all cuis in RXNORM. used for DrugNer - need to set the
	 * coding scheme to RXNORM. Convert the cui into a numeric representation
	 * (chop off the preceding 'C') to save memory.
	 * 
	 * @return
	 */
    public abstract TIntSet getRXNORMCuis();

    public abstract boolean isRXNORMCui(String cui);
}
