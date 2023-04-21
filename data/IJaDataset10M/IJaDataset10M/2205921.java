package de.beas.explicanto.distribution.model.surveys;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import de.beas.explicanto.distribution.util.HTMLEntities;

/**
 * The SurveyReportItem class.
 *
 */
public class SurveyReportItem implements Serializable {

    private String questionName;

    private Map answerWeight;

    private int totalHits;

    public SurveyReportItem(String questionName) {
        this.questionName = questionName;
        answerWeight = new TreeMap();
        totalHits = 0;
    }

    public String getQuestionName() {
        return questionName;
    }

    public String getHTMLQuestionName() {
        return HTMLEntities.htmlentities(questionName);
    }

    /**
	 * Returns the answer weight map structured as follows:
	 * <code>key</code> - the Answer string
	 * <code>value</code> - the hits ot the corresponding Answer as Integer object
	 */
    public Map getAnswerWeight() {
        return answerWeight;
    }

    public void addAnswer(String answer) {
        Integer hits = (Integer) answerWeight.get(answer);
        if (hits == null) {
            hits = new Integer(0);
        }
        hits = new Integer(hits.intValue() + 1);
        answerWeight.put(answer, hits);
        totalHits++;
    }

    /**
	 * Returns the total number of hits for this question
	 * in order to be used in % representation on UI by the following rule:
	 * % str = (answerHit * 100)/totalHits
	 */
    public int getTotalHits() {
        return totalHits;
    }

    /**
	 * @author mirceac
	 * 
	 * Returns a string representing the percentage 
	 * corresponding to the answer in the survey. 
	 */
    public String getPercentage(Object value) {
        int valueInt = ((Integer) value).intValue();
        String percentage = null;
        if (totalHits != 0) {
            percentage = String.valueOf((valueInt * 100) / totalHits);
        } else {
            percentage = "0";
        }
        return percentage;
    }
}
