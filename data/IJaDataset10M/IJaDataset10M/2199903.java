package br.ita.sgch.view;

import br.ita.sgch.domain.Quarto;
import br.ita.sgch.view.BaseGenericPanel;
import br.ita.sgch.view.GenericPanel;
import br.ita.sgch.view.PanelFactory;

public class QuartoPanelFactory extends PanelFactory<Quarto> {

    public QuartoPanelFactory() {
        super(Quarto.class);
    }

    public QuartoPanelFactory(Quarto c) {
        super(c, Quarto.class);
    }

    @Override
    public BaseGenericPanel<Quarto, Quarto> createPanel(TelaContainer container) {
        return new GenericPanel<Quarto>(container, Quarto.class, getInitial(), BaseGenericPanel.BOTAO_INSERIR);
    }

    @Override
    public String getFrameTitle() {
        return "Quartos";
    }
}
