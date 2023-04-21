package br.ita.doacoes.view.controledoacoes;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import br.ita.doacoes.core.templates.DAO;
import br.ita.doacoes.core.templates.GenericDAOJPA;
import br.ita.doacoes.domain.controledoacoes.LocalArmazenamento;
import br.ita.doacoes.view.cadastrodoacoes.refresher.Refreshable;
import br.ita.doacoes.view.cadastrodoacoes.refresher.Refresher;

public class LocalArmazenamentoCombo extends DefaultComboBoxModel implements Refreshable {

    DAO dao = new GenericDAOJPA(LocalArmazenamento.class);

    public LocalArmazenamentoCombo() {
        refresh();
        Refresher.add(this);
    }

    public void refresh() {
        List lista = dao.getList();
        removeAllElements();
        for (Object obj : lista) {
            addElement(obj);
        }
    }
}
