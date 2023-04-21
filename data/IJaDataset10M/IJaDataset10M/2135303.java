package Action.lineMode.accessionSearch;

import java.io.Serializable;
import java.util.ArrayList;
import Action.Keyword;

/**
 * @author moroda
 * @version 3.1
 *  
 */
public class AccessionSearchCriteria implements Serializable {

    private boolean isMatchFull;

    private boolean isTargetAccession;

    private boolean isTargetAnnotation;

    private ArrayList targetDataList;

    public AccessionSearchCriteria() {
        isMatchFull = true;
        isTargetAccession = true;
        isTargetAnnotation = true;
        targetDataList = new ArrayList();
    }

    public final String matchAccessionCriteria(String expression) {
        return Keyword.getKeywordMatchSQL(expression, "g.name", isMatchFull);
    }

    public final String matchCriteria(String expression, String[] targetColumnName) {
        return Keyword.getKeywordRegexpMatchSQL(expression, targetColumnName, isMatchFull);
    }

    public final String matchCriteria(String[] expressions, String[] targetColumnName) {
        if (expressions == null || expressions.length == 0) return null;
        return Keyword.getKeywordRegexpMatchSQL(expressions[0], targetColumnName, isMatchFull);
    }

    /**
	 * @return Returns the isPerfectMatch.
	 */
    public boolean isMatchFull() {
        return isMatchFull;
    }

    /**
	 * @param isPerfectMatch
	 *            The isPerfectMatch to set.
	 */
    public void setMatchFull(boolean isPerfectMatch) {
        this.isMatchFull = isPerfectMatch;
    }

    /**
	 * @return Returns the targetDataList.
	 */
    public ArrayList getTargetDataList() {
        return targetDataList;
    }

    /**
	 * @param targetDataList
	 *            The targetDataList to set.
	 */
    public void setTargetDataList(ArrayList targetDataList) {
        this.targetDataList = targetDataList;
    }

    /**
	 * @return Returns the isTargetAccession.
	 */
    public boolean isTargetAccession() {
        return isTargetAccession;
    }

    /**
	 * @param isTargetAccession
	 *            The isTargetAccession to set.
	 */
    public void setTargetAccession(boolean isTargetAccession) {
        this.isTargetAccession = isTargetAccession;
    }

    /**
	 * @return Returns the isTargetAnnotation.
	 */
    public boolean isTargetAnnotation() {
        return isTargetAnnotation;
    }

    /**
	 * @param isTargetAnnotation
	 *            The isTargetAnnotation to set.
	 */
    public void setTargetAnnotation(boolean isTargetAnnotation) {
        this.isTargetAnnotation = isTargetAnnotation;
    }
}
