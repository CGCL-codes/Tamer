package br.ita.sgch.view;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

public abstract class Tela extends JPanel {

    private static final long serialVersionUID = 1L;

    private final TelaContainer container;

    public Tela(TelaContainer container) {
        this.container = container;
    }

    protected TelaContainer getTelaContainer() {
        return this.container;
    }

    public abstract JMenuBar getMenuBar();
}
