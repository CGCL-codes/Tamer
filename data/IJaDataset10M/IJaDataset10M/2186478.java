package casdadm.view.alunos;

import casdadm.domain.alunos.Parente;
import casdadm.view.BaseGenericPanel;
import casdadm.view.GenericPanel;
import casdadm.view.PanelFactory;

/**
 *
 * @author Thiago B. / Leonardo
 */
public class ParentePanelFactory extends PanelFactory<Parente> {

    public ParentePanelFactory() {
        super(Parente.class);
    }

    public ParentePanelFactory(Parente p) {
        super(p, Parente.class);
    }

    @Override
    public String getFrameTitle() {
        return "Parentes";
    }

    @Override
    public BaseGenericPanel getPanel() {
        return new GenericPanel<Parente>(Parente.class, getObject());
    }
}
