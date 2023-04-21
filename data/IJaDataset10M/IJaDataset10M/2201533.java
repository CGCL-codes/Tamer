package casdadm.view.common;

import casdadm.domain.common.Endereco;
import casdadm.core.FilterContains;
import casdadm.view.BaseGenericPanel;
import casdadm.view.PanelFactory;

/**
 *
 * @author Administrator
 */
public class EnderecoPanelFactory extends PanelFactory<Endereco> {

    public EnderecoPanelFactory() {
        super(Endereco.class);
    }

    public EnderecoPanelFactory(Endereco c) {
        super(c, Endereco.class);
    }

    @Override
    public String getFrameTitle() {
        return "Endereços";
    }

    @Override
    public BaseGenericPanel createPanel() {
        EnderecoPanel panel = new EnderecoPanel(getInitial());
        panel.addFilter(new FilterContains("rua"));
        return panel;
    }
}
