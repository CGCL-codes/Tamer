package gui.winkel;

import domein.DomeinController;
import gui.GUIController;
import gui.Vertaalbaar;
import gui.Verversbaar;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

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
public class WinkelSchipDetailJPanel extends javax.swing.JPanel implements Verversbaar, Vertaalbaar {

    private JButton btnSchipItems;

    private DomeinController domeinController;

    private String[] geselecteerdSchip;

    private GUIController guiController;

    private JLabel lblMaxAantalKanonnen;

    private JLabel lblMaxAantalVoedsel;

    private JLabel lblMaxAantalZeilen;

    private JLabel lblMaxRompDikte;

    private JLabel lblNaamVar;

    private JLabel lblPrijs;

    public WinkelSchipDetailJPanel(GUIController guiController) {
        super();
        this.guiController = guiController;
        this.domeinController = guiController.getDomeinController();
        initGUI();
    }

    private void initGUI() {
        try {
            GridBagLayout thisLayout = new GridBagLayout();
            this.setPreferredSize(new java.awt.Dimension(371, 297));
            thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1 };
            thisLayout.rowHeights = new int[] { 7, 7, 7, 20, 7 };
            thisLayout.columnWeights = new double[] { 0.0, 0.0, 0.1 };
            thisLayout.columnWidths = new int[] { 178, 184, 7 };
            this.setLayout(thisLayout);
            {
                lblNaamVar = new JLabel();
                this.add(lblNaamVar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                lblPrijs = new JLabel();
                this.add(lblPrijs, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblPrijs.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonGeld")));
            }
            {
                lblMaxAantalKanonnen = new JLabel();
                this.add(lblMaxAantalKanonnen, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblMaxAantalKanonnen.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonMaxAantalKanonnen")));
            }
            {
                lblMaxAantalVoedsel = new JLabel();
                this.add(lblMaxAantalVoedsel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblMaxAantalVoedsel.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonMaxAantalVoedsel")));
            }
            {
                lblMaxRompDikte = new JLabel();
                this.add(lblMaxRompDikte, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblMaxRompDikte.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonMaxRompDikte")));
            }
            {
                lblMaxAantalZeilen = new JLabel();
                this.add(lblMaxAantalZeilen, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblMaxAantalZeilen.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonMaxZeilen")));
            }
            {
                btnSchipItems = new JButton();
                this.add(btnSchipItems, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                btnSchipItems.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        WinkelSchipItemsJFrame schipItems = guiController.getWinkelSchipItemsJFrame();
                        schipItems.ververs();
                        schipItems.setLocationRelativeTo(null);
                        schipItems.setVisible(true);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vertaal() {
        lblNaamVar.setText(domeinController.vertaal("Naam") + ":");
        lblPrijs.setText(domeinController.vertaal("Prijs"));
        lblMaxAantalKanonnen.setText("Maximum aantal kannonen");
        lblMaxRompDikte.setText(domeinController.vertaal("Romp dikte"));
        lblMaxAantalVoedsel.setText(domeinController.vertaal("Maximum aantal voedsel"));
        btnSchipItems.setText(domeinController.vertaal("Koop/Verkoop schip items"));
    }

    public void ververs() {
        vertaal();
        geselecteerdSchip = domeinController.geefOverzichtGeselecteerdItem();
        lblNaamVar.setText(geselecteerdSchip[0]);
        lblNaamVar.setIcon(new ImageIcon(guiController.geefAfbeelding(geselecteerdSchip[10])));
        lblPrijs.setText(geselecteerdSchip[1]);
        lblMaxAantalKanonnen.setText(geselecteerdSchip[6]);
        lblMaxAantalVoedsel.setText(geselecteerdSchip[7]);
        lblMaxRompDikte.setText(geselecteerdSchip[8]);
        lblMaxAantalZeilen.setText(geselecteerdSchip[9]);
    }
}
