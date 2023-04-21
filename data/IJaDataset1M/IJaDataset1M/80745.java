package gui.winkel;

import domein.DomeinController;
import gui.GUIController;
import gui.Vertaalbaar;
import gui.Verversbaar;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
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
public class WinkelItemDetailJPanel extends javax.swing.JPanel implements Verversbaar, Vertaalbaar {

    private DomeinController domeinController;

    private GUIController guiController;

    private JLabel lblAanvalspuntenBonus;

    private JLabel lblLevenspuntenBonus;

    private JLabel lblNaam;

    private JLabel lblPrijs;

    private JLabel lblSnelheidspuntenBonus;

    private JLabel lblVerdedigingspuntenBonus;

    public WinkelItemDetailJPanel(GUIController guiController) {
        super();
        this.guiController = guiController;
        this.domeinController = guiController.getDomeinController();
        initGUI();
    }

    private void initGUI() {
        try {
            GridBagLayout thisLayout = new GridBagLayout();
            this.setPreferredSize(new java.awt.Dimension(442, 286));
            thisLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.1 };
            thisLayout.rowHeights = new int[] { 27, -109, 36, 81, 77, 20 };
            thisLayout.columnWeights = new double[] { 0.0, 0.0, 0.1 };
            thisLayout.columnWidths = new int[] { 252, 269, 20 };
            this.setLayout(thisLayout);
            {
                lblNaam = new JLabel();
                this.add(lblNaam, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                lblSnelheidspuntenBonus = new JLabel();
                this.add(lblSnelheidspuntenBonus, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblSnelheidspuntenBonus.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonSnelheidspunten")));
            }
            {
                lblAanvalspuntenBonus = new JLabel();
                this.add(lblAanvalspuntenBonus, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblAanvalspuntenBonus.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonAanvalspunten")));
            }
            {
                lblVerdedigingspuntenBonus = new JLabel();
                this.add(lblVerdedigingspuntenBonus, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblVerdedigingspuntenBonus.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonVerdedigingspunten")));
            }
            {
                lblLevenspuntenBonus = new JLabel();
                this.add(lblLevenspuntenBonus, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblLevenspuntenBonus.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonLevenspunten")));
            }
            {
                lblPrijs = new JLabel();
                this.add(lblPrijs, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                lblPrijs.setIcon(new ImageIcon(guiController.geefAfbeelding("icoonGeld")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ververs() {
        if (domeinController.isErItemGeselecteerd()) {
            String[] item = domeinController.geefOverzichtGeselecteerdItem();
            lblPrijs.setText(item[2]);
            lblAanvalspuntenBonus.setText(item[3]);
            lblLevenspuntenBonus.setText(item[4]);
            lblSnelheidspuntenBonus.setText(item[5]);
            lblVerdedigingspuntenBonus.setText(item[6]);
            ImageIcon afb = new ImageIcon(guiController.geefAfbeelding(item[7]));
            lblNaam.setIcon(afb);
        }
    }

    @Override
    public void vertaal() {
    }
}
