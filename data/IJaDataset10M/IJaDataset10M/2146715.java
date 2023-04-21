package casdadm.view.alunos;

import casdadm.domain.alunos.Familia;
import casdadm.view.BaseGenericPanel;
import casdadm.view.GenericPanel;
import casdadm.view.PanelFactory;

/**
 *
 * @author root
 */
public class FamiliaPanelFactory extends PanelFactory<Familia> {

    public FamiliaPanelFactory() {
        super(Familia.class);
    }

    public FamiliaPanelFactory(Familia f) {
        super(f, Familia.class);
    }

    @Override
    public String getFrameTitle() {
        return "Família";
    }

    @Override
    public BaseGenericPanel getPanel() {
        return new GenericPanel<Familia>(Familia.class, getObject());
    }
}
