package ihm.biens.listes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import application.config.Env;
import moteur.biens.Vetement;
import outils.ihm.labels.JLabelColonne;
import outils.ihm.labels.JLabelDonnee;
import outils.ihm.labels.JLabelTitre;
import outils.ihm.parchemins.FrameParcheminSimple;

public class FenetreListeVetement extends FrameParcheminSimple {

    private static final long serialVersionUID = 1L;

    public FenetreListeVetement() {
        super(Env.bdd.getParcheminSimple("Rouleau vertical 800x500"), "Vêtements", false, true, false, true);
        this.setFrameIcon(Env.getIconeEquipement("icone_vetement"));
        this.setOpaque(false);
        this.setLayer(Env.layer_fenetre_standard);
        afficheInfos();
    }

    private void afficheInfos() {
        int hauteur_ligne = 27;
        JPanel panelBound = new JPanel();
        panelBound.setBackground(new Color(0, 0, 0, 0));
        panelBound.setOpaque(false);
        panelBound.setLayout(null);
        JScrollPane panelScroll = new JScrollPane(panelBound);
        panelScroll.setBackground(new Color(0, 0, 0, 0));
        panelScroll.getHorizontalScrollBar().setUnitIncrement(20);
        panelScroll.getVerticalScrollBar().setUnitIncrement(20);
        panelScroll.setOpaque(false);
        panelScroll.getViewport().setOpaque(false);
        int debut_x = 5;
        int debut_y = 5;
        int x = debut_x;
        int y = debut_y;
        JLabelTitre lbTitre = new JLabelTitre("VETEMENTS");
        panelHautPage.add(lbTitre);
        String[] nomColonnes = { "Vêtement", "Effet", "Poids" };
        int[] tailleColonnes = { 150, 300, 60 };
        JLabelColonne[] labelColonnes = new JLabelColonne[nomColonnes.length];
        for (int cpt = 0; cpt < nomColonnes.length; cpt++) {
            labelColonnes[cpt] = new JLabelColonne(nomColonnes[cpt]);
        }
        for (int cpt = 0; cpt < nomColonnes.length; cpt++) {
            labelColonnes[cpt].setBounds(x, y, tailleColonnes[cpt], hauteur_ligne);
            x = x + tailleColonnes[cpt] + 10;
            panelBound.add(labelColonnes[cpt]);
        }
        x = 5;
        y = y + 20;
        for (final Vetement a : Env.bdd.getVetements()) {
            final JLabelDonnee[] valeurs = new JLabelDonnee[nomColonnes.length];
            int col = 0;
            valeurs[col++] = new JLabelDonnee(a.getLibelle(), a.getIcone().getPetiteIconePourJLabel(), SwingConstants.LEFT);
            valeurs[col++] = new JLabelDonnee(a.getEffet());
            valeurs[col++] = new JLabelDonnee(Double.toString(a.getPoids()));
            for (int cpt = 0; cpt < nomColonnes.length; cpt++) {
                valeurs[cpt].setBounds(x, y, tailleColonnes[cpt], hauteur_ligne);
                valeurs[cpt].setToolTipText(a.toStringToolTip());
                ajouteComportement(valeurs[cpt], valeurs);
                x = x + tailleColonnes[cpt] + 10;
                panelBound.add(valeurs[cpt]);
            }
            x = 5;
            y = y + hauteur_ligne;
            ToolTipManager.sharedInstance().setInitialDelay(200);
            ToolTipManager.sharedInstance().setDismissDelay(8000);
        }
        y = y + 10;
        panelBound.setPreferredSize(new Dimension(panelBound.getWidth(), y + 40));
        panelCentrePage.add(panelScroll);
        JButton btFermer = new JButton("Fermer", Env.getIconeAppli("icone_fermer"));
        btFermer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                fermerFenetre();
            }
        });
        panelBasPage.add(btFermer);
    }

    private void ajouteComportement(JLabelDonnee lab, final JLabelDonnee[] valeurs) {
        lab.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                for (int cpt = 0; cpt < valeurs.length; cpt++) valeurs[cpt].setSelectionne();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                for (int cpt = 0; cpt < valeurs.length; cpt++) valeurs[cpt].setNormal();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
    }
}
