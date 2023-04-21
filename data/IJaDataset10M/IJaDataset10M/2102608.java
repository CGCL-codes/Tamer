package ru.syktsu.projects.oko2.server.managers;

import java.util.List;
import ru.syktsu.projects.oko2.server.exceptions.GeneralServerException;
import ru.syktsu.projects.oko2.server.interfaces.IProblem;
import ru.syktsu.projects.oko2.server.jaxb.JaxbProblemImage;
import ru.syktsu.projects.oko2.server.objects.Problem;

/**
 * Здесь будет реализация интерфейса взаимодействия с задачами
 * 
 * @author XupyprMV
 */
public class ProblemManager implements IProblem {

    public String createProblem(Problem problem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Problem getProblem(String problemId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void publicate(String problemId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void hide(String problemId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<String> listImages(String problemId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void reCheck(String problemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateProblem(Problem problem) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteProblem(String problemId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Problem> listProblems() throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Problem> listProblemsByContest(String contestId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addProblemToContest(String problemId, String contestId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeProblemFromContest(String problemId, String contestId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String addImage(String problemId, JaxbProblemImage image) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeImage(String imageId) throws GeneralServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
