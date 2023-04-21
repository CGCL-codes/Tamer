package edu.pmfbl.mnr.sem.mcmatp.gui;

import javax.swing.*;
import java.awt.*;
import com.borland.jbcl.layout.*;
import edu.pmfbl.mnr.sem.mcmatp.app.*;

/**
 * <p>Title: MCMA Test Preview</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: KrndijaSoft</p>
 *
 * @author Ninoslav Krndija
 * @version 1.0
 */
public class OdgovorPanel extends JPanel {

    AppDataModule appDataModule = AppDataModule.getDataModule();

    Test test = Test.getInstance();

    long id;

    public OdgovorPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        tacnoImageIcon = new ImageIcon(edu.pmfbl.mnr.sem.mcmatp.mcmatp.class.getResource("app/resource/tacno.gif"));
        netacnoImageIcon = new ImageIcon(edu.pmfbl.mnr.sem.mcmatp.mcmatp.class.getResource("app/resource/netacno.gif"));
        this.setLayout(borderLayout1);
        odgovorCheckBox.setText(appDataModule.getOdgovoriDataSet().getString("odgovor"));
        id = appDataModule.getOdgovoriDataSet().getLong("id");
        leftPanel.setMinimumSize(new Dimension(10, 0));
        leftPanel.setPreferredSize(new Dimension(10, 0));
        infoLabel.setVisible(false);
        infoLabel.setText(appDataModule.getOdgovoriDataSet().getString("info"));
        imageControl.setVisible(false);
        sadrzajPanel.setLayout(boxLayout21);
        this.add(sadrzajPanel, java.awt.BorderLayout.CENTER);
        this.add(leftPanel, java.awt.BorderLayout.WEST);
        sadrzajPanel.add(odgovorCheckBox, null);
        sadrzajPanel.add(imageControl, null);
        sadrzajPanel.add(infoLabel, null);
    }

    public long getId() {
        return id;
    }

    public JCheckBox getOdgovorCheckBox() {
        return odgovorCheckBox;
    }

    public JLabel getImageControl() {
        return imageControl;
    }

    public ImageIcon getTacnoImageIcon() {
        return tacnoImageIcon;
    }

    public ImageIcon getNetacnoImageIcon() {
        return netacnoImageIcon;
    }

    public void setComponentsVisible(boolean visible) {
        infoLabel.setVisible(visible);
        imageControl.setVisible(visible);
    }

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel leftPanel = new JPanel();

    JPanel sadrzajPanel = new JPanel();

    JCheckBox odgovorCheckBox = new JCheckBox();

    JLabel infoLabel = new JLabel();

    BoxLayout2 boxLayout21 = new BoxLayout2();

    JLabel imageControl = new JLabel();

    ImageIcon tacnoImageIcon;

    ImageIcon netacnoImageIcon;
}
