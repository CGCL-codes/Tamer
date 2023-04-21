package de.exilab.pixmgr.dialog.zip;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import de.exilab.pixmgr.dialog.ExportPanel;
import de.exilab.pixmgr.dialog.FilePanel;

/**
 * Dialog for exporting an album as a zip file
 * @author <a href="andreas@exilab.de">Andreas Heidt</a>
 * @version $Revision: 1.1 $ - $Date: 2004/07/31 06:29:46 $
 */
public class ZipExportDialog extends JDialog implements ActionListener {

    /**
     * Panel for entering the target file name
     */
    private FilePanel m_panelFile;

    /**
     * Panel with the export options
     */
    private ExportPanel m_panelExport;

    /**
     * The OK button
     */
    private JButton m_buttonOk;

    /**
     * The cancel button
     */
    private JButton m_buttonCancel;

    /**
     * <code>true</code> if this dialog was cancelled by the user
     */
    private boolean m_cancelled;

    /**
     * Constructor of the class <code>ZipExportDialog</code>
     * @param parent Reference to the parent frame 
     */
    public ZipExportDialog(Frame parent) {
        super(parent, "ZIP Export", true);
        initGUI();
    }

    /**
     * Initializes the GUI     
     */
    private void initGUI() {
        GridBagConstraints gbc;
        JPanel cp = new JPanel();
        cp.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 5, 5);
        cp.add(getPanelFile(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        cp.add(getPanelExport(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 10, 5);
        cp.add(getButtonOk(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 10, 5);
        cp.add(getButtonCancel(), gbc);
        setContentPane(cp);
        pack();
        setResizable(false);
    }

    /**
     * Returns the panel for entering the target file name
     * @return A <code>JPanel</code>
     */
    public FilePanel getPanelFile() {
        if (m_panelFile == null) {
            m_panelFile = new FilePanel();
        }
        return m_panelFile;
    }

    /**
     * Returns the panel with the export options
     * @return A <code>JPanel</code>
     */
    public ExportPanel getPanelExport() {
        if (m_panelExport == null) {
            m_panelExport = new ExportPanel();
        }
        return m_panelExport;
    }

    /**
     * Returns the OK button
     * @return A <code>JButton</code>
     */
    public JButton getButtonOk() {
        if (m_buttonOk == null) {
            m_buttonOk = new JButton("Ok");
            m_buttonOk.addActionListener(this);
        }
        return m_buttonOk;
    }

    /**
     * Returns the CANCEL button
     * @return A <code>JButton</code>
     */
    public JButton getButtonCancel() {
        if (m_buttonCancel == null) {
            m_buttonCancel = new JButton("Cancel");
            m_buttonCancel.addActionListener(this);
        }
        return m_buttonCancel;
    }

    /**
     * Determines if this dialog was cancelled by the user
     * @return <code>true</code> if the dialog was cancelled
     */
    public boolean isCancelled() {
        return m_cancelled;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed
     * (java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == getButtonCancel()) {
            setVisible(false);
        }
        if (source == getButtonOk()) {
            m_cancelled = false;
            setVisible(false);
        }
    }

    /**
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean b) {
        Dimension ps = getParent().getSize();
        Point pl = getParent().getLocation();
        setLocation(pl.x + ((ps.width - getSize().width) / 2), pl.y + ((ps.height - getSize().height) / 2));
        if (b) {
            m_cancelled = true;
        }
        super.setVisible(b);
    }
}
