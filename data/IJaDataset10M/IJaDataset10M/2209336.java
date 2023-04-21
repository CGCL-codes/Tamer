package quizgen;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <code>QuizGenerator</code> generates quizzes from a given database.
 * @author Ann Marie Steichmann
 *
 */
public class QuizGenerator extends QuizDBConnection {

    private static final long serialVersionUID = 8524576268728306254L;

    private Statement stmt1;

    private Statement stmt2;

    private List<Question> all = new ArrayList<Question>();

    /**
	 * Constructor for QuizGenerator
	 * @param dbName The name of the database to connect to
	 */
    public QuizGenerator(String dbName) {
        super(dbName);
        buildQuestions();
    }

    protected void initStatements() {
        try {
            if (conn != null && !conn.isClosed()) {
                stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                System.out.println("Successfully created statements");
            }
        } catch (SQLException e) {
            System.out.println("Unable to create statements: " + e);
            e.printStackTrace();
            freeResources();
        }
    }

    @Override
    public void freeResources() {
        super.freeResources();
        stmt1 = null;
        stmt2 = null;
    }

    private void buildQuestions() {
        if (stmt1 != null && stmt2 != null) {
            try {
                String sql = "select * from question";
                ResultSet questionRS = stmt1.executeQuery(sql);
                while (questionRS.next()) {
                    String id = questionRS.getString("id");
                    String qValue = questionRS.getString("value");
                    sql = "select * from choice where qid='" + id + "'";
                    ResultSet choiceRS = stmt2.executeQuery(sql);
                    List<Choice> choices = new ArrayList<Choice>();
                    while (choiceRS.next()) {
                        InputStream letterStream = choiceRS.getAsciiStream("letter");
                        char letter = (char) letterStream.read();
                        String cValue = choiceRS.getString("value");
                        boolean correct = choiceRS.getBoolean("correct");
                        choices.add(new Choice(letter, cValue, correct));
                    }
                    all.add(new Question(id, qValue, choices));
                }
            } catch (SQLException e) {
                System.out.println("Unable to retrieve questions: " + e);
                e.printStackTrace();
                freeResources();
            } catch (IOException e) {
                System.out.println("Unable to retrieve letter for choice: " + e);
                e.printStackTrace();
                freeResources();
            }
        }
    }

    /**
	 * @return A shuffled list of all the questions in the DB
	 */
    public List<Question> listShuffled() {
        List<Question> shuffled = new ArrayList<Question>(all);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    /**
	 * @return A list of all the questions in the DB in their
	 * original order
	 */
    public List<Question> listAll() {
        return all;
    }
}
