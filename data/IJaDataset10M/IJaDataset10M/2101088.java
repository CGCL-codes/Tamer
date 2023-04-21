package casdadm.view.alunos;

import casdadm.domain.alunos.Bem;
import casdadm.view.BaseGenericPanel;
import casdadm.view.GenericPanel;
import casdadm.view.PanelFactory;

/**
 *
 * @author root
 */
public class BemPanelFactory extends PanelFactory<Bem> {

    public BemPanelFactory() {
        super(Bem.class);
    }

    public BemPanelFactory(Bem b) {
        super(b, Bem.class);
    }

    @Override
    public String getFrameTitle() {
        return "Bens";
    }

    @Override
    public BaseGenericPanel getPanel() {
        return new GenericPanel<Bem>(Bem.class, getObject());
    }
}
