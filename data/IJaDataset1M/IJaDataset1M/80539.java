package cz.cvut.fel.mvod.persistence.derby;

import cz.cvut.fel.mvod.persistence.DAOException;
import cz.cvut.fel.mvod.persistence.QuestionDAO;
import cz.cvut.fel.mvod.common.Alternative;
import cz.cvut.fel.mvod.common.Question;
import cz.cvut.fel.mvod.common.Voting;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO object for accesing Questions in database.
 * @author jakub
 */
class DerbyQuestionDAO extends DerbyDAO implements QuestionDAO {

    private static final int ID = 1;

    private static final int VOTING_ID = 2;

    private static final int TEXT = 3;

    private static final int MIN_PERCENT = 4;

    private static final int MAX_SELECT = 5;

    private static final int MIN_SELECT = 6;

    private static final int MAX_WINNERS = 7;

    private static final int EVALUATION = 8;

    private static DerbyQuestionDAO instance = null;

    private PreparedStatement saveQuestion = null;

    private PreparedStatement deleteVotingQuestions = null;

    private PreparedStatement deleteQuestion = null;

    private PreparedStatement getVotingQuestions = null;

    private PreparedStatement getQuestions = null;

    private PreparedStatement getQuestionIDs = null;

    private String saveQuestionSQL = "INSERT INTO Question " + "(voting_id, text, min_percent, max_select, min_select, " + "max_winners, evaluation) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private String deleteVotingQuestionsSQL = "DELETE FROM Question WHERE voting_id = ?";

    private String deleteQuestionSQL = "DELETE FROM Question WHERE id = ?";

    private String getVotingQuestionsSQL = "SELECT * FROM Question WHERE voting_id = ?";

    private String getQuestionsSQL = "SELECT * FROM Question";

    private String getQuestionIDsSQL = "SELECT id FROM Question WHERE voting_id = ?";

    private DerbyQuestionDAO() {
    }

    /**
	 * Returns the only instance of <code>DerbyQuestionDAO</code>.
	 * @return singleton
	 */
    public static DerbyQuestionDAO getInstance() {
        if (instance == null) {
            instance = new DerbyQuestionDAO();
        }
        return instance;
    }

    /**
	 * Saves question to database.
	 * @param question to save
	 * @param votingId id of voting which containsthis question
	 * @throws DerbyDatabaseException
	 */
    public void saveQuestion(Question question, int votingId) throws DerbyDatabaseException {
        if (saveQuestion == null) {
            saveQuestion = prepareStatement(saveQuestionSQL);
        }
        Object[] attributes = { votingId, question.getText(), question.getMinPercent(), question.getMaxSelect(), question.getMinSelect(), question.getMaxWinners(), question.getEvaluation() };
        ResultSet results = execute(saveQuestion, attributes);
        try {
            if (results.next()) {
                question.setId(results.getInt(1));
            }
            for (Alternative alternative : question.getAlternatives()) {
                DerbyAlternativeDAO.getInstance().saveAlternative(alternative, question.getId());
            }
        } catch (SQLException ex) {
            throw new DerbyDatabaseException(ex);
        }
    }

    /**
	 * Deletes all voting questions.
	 * @param voting
	 * @throws DerbyDatabaseException
	 */
    public void deleteQuestions(Voting voting) throws DerbyDatabaseException {
        if (deleteVotingQuestions == null) {
            deleteVotingQuestions = prepareStatement(deleteVotingQuestionsSQL);
        }
        if (getQuestionIDs == null) {
            getQuestionIDs = prepareStatement(getQuestionIDsSQL);
        }
        ResultSet results = executeQuery(getQuestionIDs, (Object) voting.getId());
        try {
            while (results.next()) {
                DerbyAlternativeDAO.getInstance().deleteAlternatives(results.getInt(1));
            }
        } catch (SQLException ex) {
            throw new DerbyDatabaseException(ex);
        }
        execute(deleteVotingQuestions, (Object) voting.getId());
    }

    /**
	 * Deletes question by id.
	 * @param id of question to delete
	 * @throws DerbyDatabaseException
	 */
    public void deleteQuestion(int id) throws DerbyDatabaseException {
        if (deleteQuestion == null) {
            deleteQuestion = prepareStatement(deleteQuestionSQL);
        }
        DerbyAlternativeDAO.getInstance().deleteAlternatives(id);
        execute(deleteQuestion, (Object) id);
    }

    /**
	 * Returns list of question by voting.
	 * @param votingID id of voting
	 * @return voting's questions
	 * @throws DerbyDatabaseException
	 */
    public List<Question> getQuestions(int votingID) throws DerbyDatabaseException {
        if (getVotingQuestions == null) {
            getVotingQuestions = prepareStatement(getVotingQuestionsSQL);
        }
        return parseQuestions(executeQuery(getVotingQuestions, (Object) votingID));
    }

    /**
	 * Returns all questions stored in database.
	 * @return all questions
	 * @throws DerbyDatabaseException
	 */
    public List<Question> getQuestions() throws DerbyDatabaseException {
        if (getQuestions == null) {
            getQuestions = prepareStatement(getQuestionsSQL);
        }
        return parseQuestions(executeQuery(getQuestions));
    }

    private List<Question> parseQuestions(ResultSet results) throws DerbyDatabaseException {
        try {
            List<Question> questions = new ArrayList<Question>();
            while (results.next()) {
                Question question = new Question();
                question.setAlternatives(DerbyAlternativeDAO.getInstance().getAlternatives(results.getInt(ID)));
                question.setText(results.getString(TEXT));
                question.setMinPercent(results.getInt(MIN_PERCENT));
                question.setMaxSelect(results.getInt(MAX_SELECT));
                question.setMinSelect(results.getInt(MIN_SELECT));
                question.setMaxWinners(results.getInt(MAX_WINNERS));
                question.setEvaluation(results.getInt(EVALUATION));
                questions.add(question);
            }
            return questions;
        } catch (SQLException ex) {
            throw new DerbyDatabaseException(ex);
        }
    }

    public void updateQuestion(Question question, int votingId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
