package casdadm.view.financeiro;

import casdadm.core.GenericJPADAO;
import casdadm.core.financeiro.TransacaoDAO;
import casdadm.domain.financeiro.GrupoTransacao;
import casdadm.domain.financeiro.TipoTransacao;
import casdadm.domain.financeiro.Transacao;
import casdadm.view.GenericPanel;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.swingBean.gui.JActButton;

/**
 *
 * @author User H8
 */
public class GrupoTransacaoPanel extends GenericPanel<GrupoTransacao> {

    public GrupoTransacaoPanel(GrupoTransacao grupo_transacao) {
        super(GrupoTransacao.class, grupo_transacao);
    }

    @Override
    public void createButtons() {
        super.createButtons();
        getButtonFilter().setVisible(false);
    }

    @Override
    public void executeRemoveAction() {
        GenericJPADAO<TipoTransacao> daoTrans;
        daoTrans = new GenericJPADAO(TipoTransacao.class);
        List<TipoTransacao> list;
        list = daoTrans.getObjectFromNamedQuery("TipoTransacao.findByGroup", currentObject);
        if ((objectClass == tableObjectClass) && (list.isEmpty())) {
            dao.delete((GrupoTransacao) currentObject);
            setInitialBeanList();
            limparFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "You cannot delete this group: there are dependencies", "Remove Error", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
