package org.openXpertya.grid.ed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.openXpertya.apps.AEnv;
import org.openXpertya.apps.ConfirmPanel;
import org.openXpertya.model.MImage;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class VImageDialog extends JDialog implements ActionListener {

    /**
     * Constructor de la clase ...
     *
     *
     * @param owner
     * @param mImage
     */
    public VImageDialog(Frame owner, MImage mImage) {
        super(owner, Msg.translate(Env.getCtx(), "AD_Image_ID"), true);
        log.info("MImage=" + mImage);
        m_mImage = mImage;
        try {
            jbInit();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "VImageDialog", ex);
        }
        fileButton.setText(m_mImage.getName());
        AEnv.positionCenterWindow(owner, this);
    }

    /** Descripción de Campos */
    private MImage m_mImage;

    /** Descripción de Campos */
    private static CLogger log = CLogger.getCLogger(VImageDialog.class);

    /** Descripción de Campos */
    private CPanel mainPanel = new CPanel();

    /** Descripción de Campos */
    private BorderLayout mainLayout = new BorderLayout();

    /** Descripción de Campos */
    private CPanel parameterPanel = new CPanel();

    /** Descripción de Campos */
    private CLabel fileLabel = new CLabel();

    /** Descripción de Campos */
    private CButton fileButton = new CButton();

    /** Descripción de Campos */
    private CLabel imageLabel = new CLabel();

    /** Descripción de Campos */
    private ConfirmPanel confirmPanel = new ConfirmPanel(true);

    /**
     * Descripción de Método
     *
     *
     * @throws Exception
     */
    void jbInit() throws Exception {
        CompiereColor.setBackground(this);
        mainPanel.setLayout(mainLayout);
        fileLabel.setText(Msg.getMsg(Env.getCtx(), "SelectFile"));
        fileButton.setText("-/-");
        imageLabel.setBackground(Color.white);
        imageLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        imageLabel.setPreferredSize(new Dimension(50, 50));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(mainPanel);
        mainPanel.add(parameterPanel, BorderLayout.NORTH);
        parameterPanel.add(fileLabel, null);
        parameterPanel.add(fileButton, null);
        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(confirmPanel, BorderLayout.SOUTH);
        fileButton.addActionListener(this);
        confirmPanel.addActionListener(this);
    }

    /**
     * Descripción de Método
     *
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fileButton) {
            cmd_file();
        } else if (e.getActionCommand().equals(ConfirmPanel.A_OK)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (m_mImage.save()) {
                dispose();
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        } else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL)) {
            dispose();
        }
    }

    /**
     * Descripción de Método
     *
     */
    private void cmd_file() {
        JFileChooser jfc = new JFileChooser();
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showOpenDialog(this);
        File imageFile = jfc.getSelectedFile();
        if ((imageFile == null) || imageFile.isDirectory() || !imageFile.exists()) {
            return;
        }
        try {
            ImageIcon image = new ImageIcon(imageFile.toURI().toURL());
            imageLabel.setIcon(image);
        } catch (Exception e) {
            log.log(Level.SEVERE, "cmd_file", e);
            return;
        }
        fileButton.setText(imageFile.getAbsolutePath());
        pack();
        String fileName = imageFile.getAbsolutePath();
        m_mImage.setName(fileName);
        m_mImage.setImageURL(fileName);
    }
}
