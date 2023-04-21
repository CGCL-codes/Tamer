package fr.ana.anaballistics.gui.window;

import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class AdvancedOptionsWindow extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private MainWindow mw;

    private JPanel jContentPane = null;

    private JButton OKButton = null;

    private JButton CancelButton = null;

    private JLabel zminLabel = null;

    private JSpinner zminField = null;

    private JLabel tairLabel = null;

    private JSpinner tairField = null;

    private JLabel deltaTLabel = null;

    private JSpinner deltaTField = null;

    private JLabel altLabel = null;

    private JSpinner altField = null;

    private JLabel latLabel = null;

    private JSpinner latField = null;

    private JLabel phiLabel = null;

    private JSpinner phiField = null;

    public AdvancedOptionsWindow(MainWindow mw) {
        super(mw);
        this.mw = mw;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(590, 232);
        this.setContentPane(getJContentPane());
        this.setTitle(mw.getLangPak().get("AdvancedOptions"));
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            int x = 24, y = 24, step = 35, height = 24, width = 128;
            zminLabel = new JLabel(mw.getLangMap().get("zmin"));
            zminLabel.setBounds(new Rectangle(x, y + step * 0, width + width / 2, height));
            zminLabel.setToolTipText(mw.getLangMap().get("zmin+"));
            jContentPane.add(zminLabel, null);
            zminField = new JSpinner(new SpinnerNumberModel(mw.getCurrentUsedTDC().getZmin(), 0, 10, 0.10));
            zminField.setBounds(new Rectangle(x + width + width / 2, y + step * 0, width / 2, height));
            zminField.setToolTipText(mw.getLangMap().get("zmin+"));
            jContentPane.add(zminField, null);
            tairLabel = new JLabel(mw.getLangMap().get("tair"));
            tairLabel.setBounds(new Rectangle(x, y + step * 1, width + width / 2, height));
            tairLabel.setToolTipText(mw.getLangMap().get("tair+"));
            jContentPane.add(tairLabel, null);
            tairField = new JSpinner(new SpinnerNumberModel(mw.getCurrentUsedTDC().getTair(), 0, 50, 0.5));
            tairField.setBounds(new Rectangle(x + width + width / 2, y + step * 1, width / 2, height));
            tairField.setToolTipText(mw.getLangMap().get("tair+"));
            jContentPane.add(tairField, null);
            deltaTLabel = new JLabel(mw.getLangMap().get("deltaT"));
            deltaTLabel.setBounds(new Rectangle(x, y + step * 2, width + width / 2, height));
            deltaTLabel.setToolTipText(mw.getLangMap().get("deltaT+"));
            jContentPane.add(deltaTLabel, null);
            deltaTField = new JSpinner(new SpinnerNumberModel(mw.getCurrentUsedTDC().getDeltaT(), 0, 1, 0.001));
            deltaTField.setBounds(new Rectangle(x + width + width / 2, y + step * 2, width / 2, height));
            deltaTField.setToolTipText(mw.getLangMap().get("deltaT+"));
            jContentPane.add(deltaTField, null);
            CancelButton = new JButton();
            CancelButton.setText(mw.getLangPak().get("Cancel"));
            CancelButton.setBounds(new Rectangle(x + width / 2, y + step * 4, width, height));
            CancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
            jContentPane.add(CancelButton);
            x = x * 2 + width * 2;
            altLabel = new JLabel(mw.getLangMap().get("alt"));
            altLabel.setBounds(new Rectangle(x, y + step * 0, width + width / 2, height));
            altLabel.setToolTipText(mw.getLangMap().get("alt+"));
            jContentPane.add(altLabel, null);
            altField = new JSpinner(new SpinnerNumberModel(mw.getCurrentUsedTDC().getAlt(), 0, 10, 0.1));
            altField.setBounds(new Rectangle(x + width + width / 2, y + step * 0, width / 2, height));
            altField.setToolTipText(mw.getLangMap().get("alt+"));
            jContentPane.add(altField, null);
            latLabel = new JLabel(mw.getLangMap().get("lat"));
            latLabel.setBounds(new Rectangle(x, y + step * 1, width + width / 2, height));
            latLabel.setToolTipText(mw.getLangMap().get("lat+"));
            jContentPane.add(latLabel, null);
            latField = new JSpinner(new SpinnerNumberModel(mw.getCurrentUsedTDC().getLat(), 0, Math.PI * 2, 0.1));
            latField.setBounds(new Rectangle(x + width + width / 2, y + step * 1, width / 2, height));
            latField.setToolTipText(mw.getLangMap().get("lat+"));
            jContentPane.add(latField, null);
            phiLabel = new JLabel(mw.getLangMap().get("phi"));
            phiLabel.setBounds(new Rectangle(x, y + step * 2, width + width / 2, height));
            phiLabel.setToolTipText(mw.getLangMap().get("phi+"));
            jContentPane.add(phiLabel, null);
            phiField = new JSpinner(new SpinnerNumberModel(mw.getCurrentUsedTDC().getPhi(), 0, 1, 0.1));
            phiField.setBounds(new Rectangle(x + width + width / 2, y + step * 2, width / 2, height));
            phiField.setToolTipText(mw.getLangMap().get("phi+"));
            jContentPane.add(phiField, null);
            OKButton = new JButton();
            OKButton.setText("Ok");
            OKButton.setBounds(new Rectangle(x + width / 2, y + step * 4, width, height));
            OKButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    mw.getCurrentUsedTDC().setZmin(getZminValue());
                    mw.getCurrentUsedTDC().setTair(getTairValue());
                    mw.getCurrentUsedTDC().setDeltaT(getDeltaTValue());
                    mw.getCurrentUsedTDC().setAlt(getAltValue());
                    mw.getCurrentUsedTDC().setLat(getLatValue());
                    mw.getCurrentUsedTDC().setPhi(getPhiValue());
                    mw.getIniReaderWriter().write();
                    dispose();
                }
            });
            jContentPane.add(OKButton);
        }
        return jContentPane;
    }

    public double getZminValue() {
        return (Double) zminField.getModel().getValue();
    }

    public double getTairValue() {
        return (Double) tairField.getModel().getValue();
    }

    public double getAltValue() {
        return (Double) altField.getModel().getValue();
    }

    public double getDeltaTValue() {
        return (Double) deltaTField.getModel().getValue();
    }

    public double getLatValue() {
        return (Double) latField.getModel().getValue();
    }

    public double getPhiValue() {
        return (Double) phiField.getModel().getValue();
    }
}
