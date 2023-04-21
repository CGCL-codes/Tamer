package gui.opties;

import gui.GUIController;
import gui.ToetsInstellingen;
import gui.Vertaalbaar;
import gui.Verversbaar;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import domein.DomeinController;

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
public class OptiesToetsenJPanel extends JPanel implements Verversbaar, Vertaalbaar {

    private JLabel lblBeurtOverslaan;

    private JLabel lblNieuwSpel;

    private JTextField txtRechts;

    private JTextField txtLinks;

    private JTextField txtOmlaag;

    private JTextField txtOmhoog;

    private JLabel lblRechts;

    private JLabel lblLinks;

    private JLabel lblOmlaag;

    private JLabel lblOmhoog;

    private JTextField txtToetsBeurtOverslaan;

    private JTextField txtNieuwSpel;

    private GUIController guiController;

    private DomeinController domeinController;

    private ToetsInstellingen actieToetsen;

    public OptiesToetsenJPanel(GUIController guiController) {
        this.guiController = guiController;
        this.actieToetsen = guiController.getInstellingen().getActieToetsen();
        this.domeinController = guiController.getDomeinController();
        initGUI();
    }

    private void initGUI() {
        try {
            GridBagLayout beeldPanelLayout = new GridBagLayout();
            beeldPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0 };
            beeldPanelLayout.rowHeights = new int[] { 10, 30, 30, 30, 30, 30, 30, 10 };
            beeldPanelLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
            beeldPanelLayout.columnWidths = new int[] { 20, 100, 120, 20 };
            this.setLayout(beeldPanelLayout);
            this.setPreferredSize(new java.awt.Dimension(350, 200));
            {
                lblBeurtOverslaan = new JLabel();
                this.add(lblBeurtOverslaan, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                txtToetsBeurtOverslaan = new JTextField();
                this.add(txtToetsBeurtOverslaan, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                txtToetsBeurtOverslaan.setText("Space");
                txtToetsBeurtOverslaan.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent e) {
                        try {
                            int keyCode = e.getKeyCode();
                            String keynaam = KeyEvent.getKeyText(keyCode);
                            actieToetsen.zetActieToets(ToetsInstellingen.ActieToets.VOLGENDE_BEURT, keyCode);
                            txtToetsBeurtOverslaan.setText(keynaam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(null, domeinController.vertaal(ex.getMessage()), domeinController.vertaal("Ongeldige toets"), JOptionPane.ERROR_MESSAGE);
                            txtToetsBeurtOverslaan.setText(KeyEvent.getKeyText(actieToetsen.geefActieToets(ToetsInstellingen.ActieToets.VOLGENDE_BEURT)));
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }
            {
                lblNieuwSpel = new JLabel();
                this.add(lblNieuwSpel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                txtNieuwSpel = new JTextField();
                this.add(txtNieuwSpel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                txtNieuwSpel.setText("N");
                txtNieuwSpel.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent e) {
                        try {
                            int keyCode = e.getKeyCode();
                            String keynaam = KeyEvent.getKeyText(keyCode);
                            actieToetsen.zetActieToets(ToetsInstellingen.ActieToets.NIEUW_SPEL, keyCode);
                            txtNieuwSpel.setText(keynaam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(null, domeinController.vertaal(ex.getMessage()), domeinController.vertaal("Ongeldige toets"), JOptionPane.ERROR_MESSAGE);
                            txtNieuwSpel.setText(KeyEvent.getKeyText(actieToetsen.geefActieToets(ToetsInstellingen.ActieToets.NIEUW_SPEL)));
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }
            {
                lblOmhoog = new JLabel();
                this.add(lblOmhoog, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                txtOmhoog = new JTextField();
                this.add(txtOmhoog, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                txtOmhoog.setText("Up");
                txtOmhoog.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent e) {
                        try {
                            int keyCode = e.getKeyCode();
                            String keynaam = KeyEvent.getKeyText(keyCode);
                            actieToetsen.zetActieToets(ToetsInstellingen.ActieToets.NOORD, keyCode);
                            txtOmhoog.setText(keynaam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(null, domeinController.vertaal(ex.getMessage()), domeinController.vertaal("Ongeldige toets"), JOptionPane.ERROR_MESSAGE);
                            txtOmhoog.setText(KeyEvent.getKeyText(actieToetsen.geefActieToets(ToetsInstellingen.ActieToets.NOORD)));
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }
            {
                lblOmlaag = new JLabel();
                this.add(lblOmlaag, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                txtOmlaag = new JTextField();
                this.add(txtOmlaag, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                txtOmlaag.setText("Down");
                txtOmlaag.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent e) {
                        try {
                            int keyCode = e.getKeyCode();
                            String keynaam = KeyEvent.getKeyText(keyCode);
                            actieToetsen.zetActieToets(ToetsInstellingen.ActieToets.ZUID, keyCode);
                            txtOmlaag.setText(keynaam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(null, domeinController.vertaal(ex.getMessage()), domeinController.vertaal("Ongeldige toets"), JOptionPane.ERROR_MESSAGE);
                            txtOmlaag.setText(KeyEvent.getKeyText(actieToetsen.geefActieToets(ToetsInstellingen.ActieToets.ZUID)));
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }
            {
                lblLinks = new JLabel();
                this.add(lblLinks, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                txtLinks = new JTextField();
                this.add(txtLinks, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                txtLinks.setText("Left");
                txtLinks.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent e) {
                        try {
                            int keyCode = e.getKeyCode();
                            String keynaam = KeyEvent.getKeyText(keyCode);
                            actieToetsen.zetActieToets(ToetsInstellingen.ActieToets.WEST, keyCode);
                            txtLinks.setText(keynaam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(null, domeinController.vertaal(ex.getMessage()), domeinController.vertaal("Ongeldige toets"), JOptionPane.ERROR_MESSAGE);
                            txtLinks.setText(KeyEvent.getKeyText(actieToetsen.geefActieToets(ToetsInstellingen.ActieToets.WEST)));
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }
            {
                lblRechts = new JLabel();
                this.add(lblRechts, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                txtRechts = new JTextField();
                this.add(txtRechts, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                txtRechts.setText("Right");
                txtRechts.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent e) {
                        try {
                            int keyCode = e.getKeyCode();
                            String keynaam = KeyEvent.getKeyText(keyCode);
                            actieToetsen.zetActieToets(ToetsInstellingen.ActieToets.OOST, keyCode);
                            txtRechts.setText(keynaam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(null, domeinController.vertaal(ex.getMessage()), domeinController.vertaal("Ongeldige toets"), JOptionPane.ERROR_MESSAGE);
                            txtRechts.setText(KeyEvent.getKeyText(actieToetsen.geefActieToets(ToetsInstellingen.ActieToets.OOST)));
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zetToetsenDefault() {
        actieToetsen.zetStandaardInstellingen();
        txtOmhoog.setText("Up");
        txtOmlaag.setText("Down");
        txtLinks.setText("Left");
        txtRechts.setText("Right");
        txtToetsBeurtOverslaan.setText("Space");
    }

    @Override
    public void vertaal() {
        lblBeurtOverslaan.setText(domeinController.vertaal("Beurt Overslaan"));
        lblNieuwSpel.setText(domeinController.vertaal("Nieuw Spel"));
        lblOmhoog.setText(domeinController.vertaal("Omhoog"));
        lblOmlaag.setText(domeinController.vertaal("Omlaag"));
        lblLinks.setText(domeinController.vertaal("Links"));
        lblRechts.setText(domeinController.vertaal("Rechts"));
    }

    @Override
    public void ververs() {
    }
}
