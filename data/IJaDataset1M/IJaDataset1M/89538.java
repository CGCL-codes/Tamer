package ee.fctwister.wc2010.pages;

import java.util.ArrayList;
import org.apache.tapestry.BaseComponent;
import ee.fctwister.wc2010.DAO.AnswerDAO;
import ee.fctwister.wc2010.DAO.CountryDAO;
import ee.fctwister.wc2010.DAO.PlayerDAO;
import ee.fctwister.wc2010.DTO.AnswerDTO;
import ee.fctwister.wc2010.DTO.CountryDTO;
import ee.fctwister.wc2010.DTO.PlayerDTO;
import ee.fctwister.wc2010.DTO.QuestionDTO;
import ee.fctwister.wc2010.DTO.UserDTO;

public abstract class ExtraPredictions extends BaseComponent {

    public abstract QuestionDTO getQuestion();

    public abstract void setQuestion(QuestionDTO question);

    public abstract AnswerDTO getAnswer();

    public abstract void setAnswer(AnswerDTO answer);

    public abstract UserDTO getUser();

    public abstract void setUser(UserDTO venue);

    public ArrayList<AnswerDTO> getAnswers() {
        AnswerDAO dao = new AnswerDAO();
        ArrayList<AnswerDTO> answers = dao.getAllUserAnswers(getUser().getId());
        ArrayList<AnswerDTO> result = new ArrayList<AnswerDTO>();
        for (AnswerDTO answerDTO : answers) {
            if (answerDTO.getId() == getQuestion().getId()) result.add(answerDTO);
        }
        return result;
    }

    public CountryDTO getTeam() {
        CountryDAO dao = new CountryDAO();
        try {
            return dao.getCountryById(getAnswer().getAnswerType_2_1());
        } catch (Exception e) {
            e.printStackTrace();
            return new CountryDTO();
        }
    }

    public PlayerDTO getPlayer() {
        PlayerDAO dao = new PlayerDAO();
        try {
            return dao.getPlayerById(getAnswer().getAnswerType_2_1());
        } catch (Exception e) {
            e.printStackTrace();
            return new PlayerDTO();
        }
    }
}
