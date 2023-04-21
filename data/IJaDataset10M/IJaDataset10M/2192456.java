package ihm.scenedejeu.controle;

import ihm.scenedejeu.elements.FenetreSceneDecor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import outils.ihm.labels.JLabelLigne;
import outils.ihm.panels.JPanelPerso;
import application.config.Env;

public class FenetreControleDecor extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    public static int taille_x = 250;

    public static int taille_y = 30;

    private FenetreSceneDecor fenetreSceneDecor = null;

    private JPanelPerso panelPrincipal = null;

    public FenetreControleDecor(FenetreSceneDecor fenetreSceneDecor) {
        super();
        this.fenetreSceneDecor = fenetreSceneDecor;
        parametresFenetre();
        generePanelPrincipal();
        genereComportement();
        setVisible(true);
    }

    private void parametresFenetre() {
        setBorder(null);
        setSize(taille_x + 2, taille_y + 2);
        javax.swing.plaf.InternalFrameUI ui = this.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) ui).setNorthPane(null);
        setOpaque(false);
        setBackground(Env.couleur_transparent);
        setLayer(Env.layer_fenetre_menu);
        ToolTipManager.sharedInstance().setInitialDelay(200);
        ToolTipManager.sharedInstance().setDismissDelay(8000);
    }

    private void generePanelPrincipal() {
        panelPrincipal = new JPanelPerso(null);
        panelPrincipal.setBounds(0, 0, taille_x, taille_y);
        panelPrincipal = new JPanelPerso() {

            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Env.couleur_peu_transparent);
                g2d.fillRoundRect(0, 0, taille_x, taille_y, 5, 5);
                super.paintComponent(g);
            }
        };
        setContentPane(panelPrincipal);
        JLabelLigne titre = new JLabelLigne("Supprimer le décor", Env.getIconeAppli("icone_supprimer"), SwingConstants.LEFT);
        titre.setForeground(Color.WHITE);
        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                fenetreSceneDecor.supprimerDecor();
                dispose();
            }
        };
        titre.addMouseListener(mouseAdapter);
        panelPrincipal.add(titre);
    }

    private void genereComportement() {
        MouseAdapter adapterMouse = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        };
        addMouseListener(adapterMouse);
    }

    public FenetreSceneDecor getFenetreSceneDecor() {
        return fenetreSceneDecor;
    }

    public void setFenetreSceneDecor(FenetreSceneDecor fenetreSceneDecor) {
        this.fenetreSceneDecor = fenetreSceneDecor;
    }
}
