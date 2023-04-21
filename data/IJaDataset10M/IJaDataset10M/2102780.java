package ee.fctwister.wc2010.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.naming.NamingException;
import ee.fctwister.wc2010.DTO.AnswerDTO;
import ee.fctwister.wc2010.DTO.QuestionDTO;

public class AnswerDAO extends AbstractDAO {

    public AnswerDTO getAnswerType_1(int questionId, int userId) throws NamingException, SQLException {
        AnswerDTO result = new AnswerDTO();
        QuestionDAO questionDAO = new QuestionDAO();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement("SELECT * FROM answer_type1 WHERE question_id=? AND user_id=?");
            ps.setInt(1, questionId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            QuestionDTO question = questionDAO.getQuestionById(questionId);
            result.setId(question.getId());
            result.setQuestion(question.getQuestion());
            result.setComment(question.getComment());
            if (rs.next()) {
                result.setAnswerType_1(rs.getInt(4));
                result.setAnswerId(rs.getInt(1));
            } else {
                System.out.println("Answer for question id=" + questionId + " and user id=" + userId + "does not exist");
            }
        } finally {
            endDbCommunication(ps, rs, conn);
        }
        return result;
    }

    public void submitAnswerType_1(int questionId, int userId, int answer) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            int existingAnswer = getExistingAnswerType_1(questionId, userId);
            if (existingAnswer == 0) {
                ps = conn.prepareStatement("INSERT INTO answer_type1(question_id,user_id,answer) VALUES (?,?,?)");
                ps.setInt(1, questionId);
                ps.setInt(2, userId);
                ps.setInt(3, answer);
            } else {
                ps = conn.prepareStatement("UPDATE answer_type1 SET answer=? WHERE answer_id=?");
                ps.setInt(1, answer);
                ps.setInt(2, existingAnswer);
            }
            ps.execute();
            conn.commit();
        } finally {
            endDbCommunication(ps, rs, conn);
        }
    }

    public AnswerDTO getAnswerType_2(int questionId, int userId, int team_position) throws NamingException, SQLException {
        AnswerDTO result = new AnswerDTO();
        QuestionDAO questionDAO = new QuestionDAO();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement("SELECT * FROM answer_type2 WHERE question_id=? AND user_id=? AND answer_2=?");
            ps.setInt(1, questionId);
            ps.setInt(2, userId);
            ps.setInt(3, team_position);
            rs = ps.executeQuery();
            QuestionDTO question = questionDAO.getQuestionById(questionId);
            result.setId(question.getId());
            result.setQuestion(question.getQuestion());
            result.setComment(question.getComment());
            if (rs.next()) {
                result.setAnswerType_2_1(rs.getInt(4));
                result.setAnswerType_2_2(rs.getInt(5));
                result.setAnswerId(rs.getInt(1));
            } else {
                System.out.println("Answer for question id=" + questionId + ", user id=" + userId + " and team position=" + team_position + " does not exist");
            }
        } finally {
            endDbCommunication(ps, rs, conn);
        }
        return result;
    }

    public AnswerDTO getAnswerType_2(int questionId, int userId) throws NamingException, SQLException {
        AnswerDTO result = new AnswerDTO();
        QuestionDAO questionDAO = new QuestionDAO();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement("SELECT * FROM answer_type2 WHERE question_id=? AND user_id=?");
            ps.setInt(1, questionId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            QuestionDTO question = questionDAO.getQuestionById(questionId);
            result.setId(question.getId());
            result.setQuestion(question.getQuestion());
            result.setComment(question.getComment());
            if (rs.next()) {
                result.setAnswerType_2_1(rs.getInt(4));
                result.setAnswerType_2_2(rs.getInt(5));
                result.setAnswerId(rs.getInt(1));
            } else {
                System.out.println("Answer for question id=" + questionId + "and user id=" + userId + " does not exist");
            }
        } finally {
            endDbCommunication(ps, rs, conn);
        }
        return result;
    }

    public void submitAnswerType_2(int questionId, int userId, int teamId, int teamPosition) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            int existingAnswer = getExistingAnswerType_2(questionId, userId, teamPosition);
            if (existingAnswer == 0) {
                ps = conn.prepareStatement("INSERT INTO answer_type2(question_id,user_id,answer_1,answer_2) VALUES (?,?,?,?)");
                ps.setInt(1, questionId);
                ps.setInt(2, userId);
                ps.setInt(3, teamId);
                ps.setInt(4, teamPosition);
            } else {
                ps = conn.prepareStatement("UPDATE answer_type2 SET answer_1=? WHERE answer_id=?");
                ps.setInt(1, teamId);
                ps.setInt(2, existingAnswer);
            }
            ps.execute();
            conn.commit();
        } finally {
            endDbCommunication(ps, rs, conn);
        }
    }

    public void submitAnswerType_2_1(int questionId, int userId, int teamId, int goals) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            int existingAnswer = getExistingAnswerType_2_1(questionId, userId);
            if (existingAnswer == 0) {
                ps = conn.prepareStatement("INSERT INTO answer_type2(question_id,user_id,answer_1,answer_2) VALUES (?,?,?,?)");
                ps.setInt(1, questionId);
                ps.setInt(2, userId);
                ps.setInt(3, teamId);
                ps.setInt(4, goals);
            } else {
                ps = conn.prepareStatement("UPDATE answer_type2 SET answer_1=?, answer_2=? WHERE answer_id=?");
                ps.setInt(1, teamId);
                ps.setInt(2, goals);
                ps.setInt(3, existingAnswer);
            }
            ps.execute();
            conn.commit();
        } finally {
            endDbCommunication(ps, rs, conn);
        }
    }

    public int getExistingAnswerType_1(int questionId, int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            return getAnswerType_1(questionId, userId).getAnswerId();
        } catch (Exception e) {
            System.out.println("Answer for question id=" + questionId + " and user id=" + userId + " does not exist");
            return 0;
        } finally {
            endDbCommunication(ps, rs, conn);
        }
    }

    public int getExistingAnswerType_2(int questionId, int userId, int teamPosition) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            return getAnswerType_2(questionId, userId, teamPosition).getAnswerId();
        } catch (Exception e) {
            System.out.println("Answer for question id=" + questionId + ", user id=" + userId + " and team position=" + teamPosition + " does not exist");
            return 0;
        } finally {
            endDbCommunication(ps, rs, conn);
        }
    }

    public int getExistingAnswerType_2_1(int questionId, int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            return getAnswerType_2(questionId, userId).getAnswerId();
        } catch (Exception e) {
            System.out.println("Answer for question id=" + questionId + "and user id=" + userId + " does not exist");
            return 0;
        } finally {
            endDbCommunication(ps, rs, conn);
        }
    }

    public boolean deleteUserAnswers(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement("DELETE FROM answer_type2 WHERE user_id=?");
            ps.setInt(1, userId);
            ps.execute();
            ps = conn.prepareStatement("DELETE FROM answer_type1 WHERE user_id=?");
            ps.setInt(1, userId);
            ps.execute();
            ps = conn.prepareStatement("DELETE FROM user_result WHERE user_id=?");
            ps.setInt(1, userId);
            ps.execute();
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            endDbCommunication(ps, rs, conn);
        }
    }

    public ArrayList<AnswerDTO> getAllUserAnswerType1(int user) {
        ArrayList<AnswerDTO> result = new ArrayList<AnswerDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement("SELECT question_id,answer FROM answer_type1 WHERE user_id=? ORDER BY question_id");
            ps.setInt(1, user);
            rs = ps.executeQuery();
            while (rs.next()) {
                AnswerDTO answer = new AnswerDTO();
                answer.setId(rs.getInt(1));
                answer.setAnswerType_1(rs.getInt(2));
                result.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endDbCommunication(ps, rs, conn);
        }
        return result;
    }

    public ArrayList<AnswerDTO> getAllUserAnswerType2(int user) {
        ArrayList<AnswerDTO> result = new ArrayList<AnswerDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement("SELECT question_id,answer_1,answer_2 FROM answer_type2 WHERE user_id=? ORDER BY question_id,answer_2");
            ps.setInt(1, user);
            rs = ps.executeQuery();
            while (rs.next()) {
                AnswerDTO answer = new AnswerDTO();
                answer.setId(rs.getInt(1));
                answer.setAnswerType_2_1(rs.getInt(2));
                answer.setAnswerType_2_2(rs.getInt(3));
                result.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endDbCommunication(ps, rs, conn);
        }
        return result;
    }

    public ArrayList<AnswerDTO> getAllUserAnswers(int user) {
        ArrayList<AnswerDTO> result = new ArrayList<AnswerDTO>();
        result.addAll(getAllUserAnswerType1(user));
        result.addAll(getAllUserAnswerType2(user));
        return result;
    }

    public Timestamp getLastUpdateTs() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp result = new Timestamp(0);
        Timestamp ts2 = new Timestamp(0);
        try {
            conn = openConnection();
            ps = conn.prepareStatement("SELECT max(ts) FROM answer");
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getTimestamp(1);
            }
            ps = conn.prepareStatement("SELECT max(ts) FROM result");
            rs = ps.executeQuery();
            if (rs.next()) {
                ts2 = rs.getTimestamp(1);
            }
            if (result.compareTo(ts2) <= 0) result = ts2;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endDbCommunication(ps, rs, conn);
        }
        return result;
    }
}
