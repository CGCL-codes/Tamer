package Maquettes_Flo_Gilles;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class DialogOptionsCopie extends javax.swing.JDialog {

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel panelEnTete;

    private JPanel panelCentral;

    private JTextPane textPaneIntro;

    private JPanel margeGauche;

    private JPanel panelMargeDroite;

    private JTree arbreCopie;

    private JButton boutonAnnuler;

    private JButton boutonCopier;

    private JPanel panelBas;

    private DefaultMutableTreeNode racine;

    /**
	* Auto-generated main method to display this JDialog
	*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                DialogOptionsCopie inst = new DialogOptionsCopie(frame);
                inst.setVisible(true);
            }
        });
    }

    public DialogOptionsCopie(JFrame frame) {
        super(frame);
        initGUI();
    }

    private void initGUI() {
        try {
            {
                this.setTitle("Copie");
            }
            {
                panelEnTete = new JPanel();
                getContentPane().add(panelEnTete, BorderLayout.NORTH);
                panelEnTete.setPreferredSize(new java.awt.Dimension(350, 68));
                {
                    textPaneIntro = new JTextPane();
                    panelEnTete.add(textPaneIntro);
                    textPaneIntro.setText("Vous d�sirez copier l'UE Langues2 de l'ann�e 2008-2009 vers le semestre S7. Merci de selectionner les �lements � copier:");
                    textPaneIntro.setPreferredSize(new java.awt.Dimension(299, 55));
                    textPaneIntro.setEditable(false);
                }
            }
            {
                panelCentral = new JPanel();
                BorderLayout panelCentralLayout = new BorderLayout();
                panelCentral.setLayout(panelCentralLayout);
                getContentPane().add(panelCentral, BorderLayout.CENTER);
                {
                    racine = new DefaultMutableTreeNode("Langues2");
                    {
                        DefaultMutableTreeNode matiereAnglais = new DefaultMutableTreeNode("Anglais");
                        {
                            DefaultMutableTreeNode maquetteAnglaisTD = new DefaultMutableTreeNode("30h de TD");
                            matiereAnglais.add(maquetteAnglaisTD);
                        }
                        DefaultMutableTreeNode matiereTOEIC = new DefaultMutableTreeNode("TOEIC");
                        {
                            DefaultMutableTreeNode maquetteTOEICCours = new DefaultMutableTreeNode("20h de Cours");
                            DefaultMutableTreeNode maquetteTOEICTD = new DefaultMutableTreeNode("20h de TD");
                            matiereTOEIC.add(maquetteTOEICCours);
                            matiereTOEIC.add(maquetteTOEICTD);
                        }
                        racine.add(matiereAnglais);
                        racine.add(matiereTOEIC);
                    }
                    arbreCopie = new JTree(racine);
                    arbreCopie.setLayout(null);
                    panelCentral.add(arbreCopie, BorderLayout.CENTER);
                }
                {
                    margeGauche = new JPanel();
                    panelCentral.add(margeGauche, BorderLayout.WEST);
                }
                {
                    panelMargeDroite = new JPanel();
                    panelCentral.add(panelMargeDroite, BorderLayout.EAST);
                }
            }
            {
                panelBas = new JPanel();
                FlowLayout panelBasLayout = new FlowLayout(FlowLayout.RIGHT);
                getContentPane().add(panelBas, BorderLayout.SOUTH);
                panelBas.setPreferredSize(new java.awt.Dimension(350, 31));
                panelBas.setLayout(panelBasLayout);
                {
                    boutonAnnuler = new JButton();
                    panelBas.add(boutonAnnuler);
                    boutonAnnuler.setText("Annuler");
                    boutonAnnuler.setBounds(88, 0, 82, 20);
                    boutonAnnuler.setSize(69, 20);
                }
                {
                    boutonCopier = new JButton();
                    panelBas.add(boutonCopier);
                    boutonCopier.setText("Copier");
                    boutonCopier.setSize(82, 20);
                }
            }
            pack();
            this.setSize(366, 303);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
