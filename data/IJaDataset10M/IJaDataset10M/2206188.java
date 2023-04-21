package jar;

/**
 * Test for questionnaire ingest.
 * @author Patrick King (patrick_king@unc.edu)
 * @version 1.0
 */
public class QuestionnaireTest {

    /**
	 * Main method
	 * @param args Command line arguments (unused)
	 */
    public static void main(String[] args) throws Exception {
        QuestionnaireLoader loader = new QuestionnaireLoader();
        loader.loadQuestionnaire("questionnaire.xml");
        System.out.println(loader.getQuestion(1) + "\n\n" + loader.getQuestion(0));
    }
}
