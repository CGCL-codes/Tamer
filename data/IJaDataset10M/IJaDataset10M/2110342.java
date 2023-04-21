package objetos;

import java.util.List;

/**
 * This class represents an answer 
 * for the dissertation type of question
 *
 * @author Vinicius Dias Oliveira - vini2208@gmail.com
 */
public class DissertationAnswer extends Answer {

    private List<String> answers;

    /**
	 * Returns answer's results
	 * 
	 * @return answer's results
	 */
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * Set answer's results
     * 
     * @param answers answer's results
     */
    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    /**
	 * Returns the HTML code of this answer
	 * 
	 * @return the HTML code of this answer
	 */
    @Override
    public String getHTMLCode() {
        StringBuilder st = new StringBuilder();
        st.append("<input type=\"text\" size=\"70\" name=\"");
        st.append(getParameterName());
        st.append("\" />");
        return st.toString();
    }

    /**
	 * Returns the parameter used as the HTML form element's name
	 * 
	 * @return the parameter used as the HTML form element's name
	 */
    @Override
    public String getParameterName() {
        return "dissertation" + getQuestion().getCode();
    }
}
