package vue;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class VueArticle extends JPanel {

    private static final long serialVersionUID = 3813132390018085026L;

    private ControleurEditeur m_cEditeur = null;

    private ControleurArticle m_cA = null;

    public VueArticle(ControleurArticle cA) {
        super();
        this.m_cA = cA;
        build();
    }

    private void build() {
        this.setBackground(new Color(255, 255, 255));
        this.m_cEditeur = new ControleurEditeur(this.m_cA);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gBC = new GridBagConstraints();
        gBC.fill = GridBagConstraints.BOTH;
        gBC.weightx = 1.0;
        gBC.weighty = 1.0;
        this.add(new JScrollPane(this.m_cEditeur.getView()), gBC);
    }

    public JTextPane getEditeur() {
        return this.m_cEditeur.getView();
    }
}
