package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import vue.controle.ControlFrame;

public class ControleurMenu implements ActionListener {

    private Menu m_menu = null;

    private JTextPane m_zoneEditable = null;

    private VueModeleDeLangage m_vMDL = null;

    public ControleurMenu(JTextPane zoneEditable, VueModeleDeLangage vMDL) {
        this.m_zoneEditable = zoneEditable;
        this.m_vMDL = vMDL;
        build();
    }

    private void build() {
        this.m_menu = new Menu(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == this.m_menu.M_NOUVEAU) {
        } else if (e.getActionCommand() == this.m_menu.M_IMPORTERMDL) {
            ControleurImportMDL cIMDL = new ControleurImportMDL(m_vMDL);
            cIMDL.getView().setLocationRelativeTo(cIMDL.getView().getParent());
            cIMDL.getView().setVisible(true);
        } else if (e.getActionCommand() == this.m_menu.M_SUPPRIMERMDL) {
            ControleurSupprMDL cSMDL = new ControleurSupprMDL(m_vMDL);
            cSMDL.getView().setLocationRelativeTo(cSMDL.getView().getParent());
            cSMDL.getView().setVisible(true);
        } else if (e.getActionCommand() == this.m_menu.M_ANNULER) {
            Editeur editeur = (Editeur) this.m_zoneEditable;
            editeur.annuler();
        } else if (e.getActionCommand() == this.m_menu.M_RETABLIR) {
            Editeur editeur = (Editeur) this.m_zoneEditable;
            editeur.retablir();
        } else if (e.getActionCommand() == this.m_menu.M_LANCER) {
            Editeur editeur = (Editeur) m_zoneEditable;
            editeur.getControleurEditeur().getControleurArticle().lancerAnalyseComplete();
        } else if (e.getActionCommand() == this.m_menu.M_REGLER) {
            ControlFrame.getInstance().display();
        } else if (e.getActionCommand() == this.m_menu.M_CONCEPTEURS) {
            JOptionPane.showMessageDialog(this.m_menu, "Cr�� par :\n \tFedaouche Nabil \n\t Gautier Gilles \n\t Sabas Romaric \n\t Simon Mathieu", "Concepteurs", JOptionPane.WARNING_MESSAGE);
        } else if (e.getActionCommand() == this.m_menu.M_QUITTER) {
            System.exit(0);
        }
    }

    public Menu getView() {
        return this.m_menu;
    }
}
