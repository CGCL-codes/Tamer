package casdadm.view.simulados;

import casdadm.domain.simulados.CursoVestibular;
import casdadm.view.BaseGenericPanel;
import casdadm.view.PanelFactory;

/**
 *
 * @author Imada
 */
public class CursoVestibularPanelFactory extends PanelFactory<CursoVestibular> {

    public CursoVestibularPanelFactory() {
        super(CursoVestibular.class);
    }

    public CursoVestibularPanelFactory(CursoVestibular c) {
        super(c, CursoVestibular.class);
    }

    @Override
    public String getFrameTitle() {
        return "Curso";
    }

    @Override
    public BaseGenericPanel createPanel() {
        return new CursoVestibularPanel(getInitial());
    }
}
