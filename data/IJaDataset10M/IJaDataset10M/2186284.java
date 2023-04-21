package uniriotec.pm.prova.core;

import java.util.List;
import java.util.Set;
import uniriotec.pm.prova.api.QuestaoService;
import uniriotec.pm.prova.dao.DAOFactory;
import uniriotec.pm.prova.dao.QuestaoDAO;
import uniriotec.pm.prova.dto.OpcaoDTO;
import uniriotec.pm.prova.dto.QuestaoDTO;
import uniriotec.pm.prova.dto.QuestaoDiscursivaDTO;
import uniriotec.pm.prova.dto.QuestaoMultiplaEscolhaDTO;
import uniriotec.pm.prova.util.DAOUtil;

/**
 *
 * @author albertoapr
 */
public class SimpleQuestaoService implements QuestaoService {

    private static DAOFactory factory = DAOUtil.getDAOFactory();

    @Override
    public void create(QuestaoDTO questao) {
        QuestaoDAO dao = factory.getQuestaoDAO();
        dao.create(questao);
    }

    @Override
    public void update(QuestaoDTO questao) {
        QuestaoDAO dao = factory.getQuestaoDAO();
        dao.update(questao);
    }

    @Override
    public void remove(int questaoId) {
        QuestaoDAO dao = factory.getQuestaoDAO();
        dao.remove(questaoId);
    }

    @Override
    public QuestaoDTO searchById(int questaoId) {
        QuestaoDTO questao;
        QuestaoMultiplaEscolhaDTO questaoME;
        QuestaoDiscursivaDTO questaoD;
        QuestaoDAO dao = factory.getQuestaoDAO();
        Set<OpcaoDTO> opcoes = (Set<OpcaoDTO>) dao.listOpcoes(questaoId);
        if (opcoes != null) {
            questaoME = new QuestaoMultiplaEscolhaDTO();
            questaoME = (QuestaoMultiplaEscolhaDTO) dao.searchById(questaoId);
            questaoME.setOpcoes(opcoes);
            questao = questaoME;
        } else {
            questaoD = new QuestaoDiscursivaDTO();
            questaoD = (QuestaoDiscursivaDTO) dao.searchById(questaoId);
            questao = questaoD;
        }
        return questao;
    }

    @Override
    public List<OpcaoDTO> listOpcoes(int questaoId) {
        QuestaoDAO dao = factory.getQuestaoDAO();
        return dao.listOpcoes(questaoId);
    }
}
