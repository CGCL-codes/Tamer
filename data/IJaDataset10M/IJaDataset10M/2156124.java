package wjhk.jupload2.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import wjhk.jupload2.filedata.PictureFileData;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * A maximized modal dialog box, that display the selected picture.
 * 
 * @author etienne_sf
 */
public class PictureDialog extends JDialog implements ActionListener, ComponentListener {

    /**
     * 
     */
    private static final long serialVersionUID = 7802205907550854333L;

    JButton buttonClose;

    PictureFileData pictureFileData = null;

    PicturePanel picturePanel = null;

    UploadPolicy uploadPolicy = null;

    /**
     * Creates a new instance.
     * 
     * @param owner The parent frame.
     * @param pictureFileData The picture to manage.
     * @param uploadPolicy The upload policy which applies.
     */
    public PictureDialog(Frame owner, PictureFileData pictureFileData, UploadPolicy uploadPolicy) {
        super(owner, pictureFileData.getFileName(), true);
        this.uploadPolicy = uploadPolicy;
        this.pictureFileData = pictureFileData;
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.picturePanel = new DialogPicturePanel(this, uploadPolicy, pictureFileData);
        this.buttonClose = new JButton(uploadPolicy.getString("buttonClose"));
        this.buttonClose.setMaximumSize(new Dimension(100, 100));
        this.buttonClose.addActionListener(this);
        getContentPane().add(this.buttonClose, BorderLayout.SOUTH);
        getContentPane().add(this.picturePanel);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        addComponentListener(this);
        setVisible(true);
        this.picturePanel.setPictureFile(null, null, null);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand() == this.buttonClose.getActionCommand()) {
            this.uploadPolicy.displayDebug("[PictureDialog] Before this.dispose()", 10);
            this.dispose();
        }
    }

    /** {@inheritDoc} */
    public void componentHidden(ComponentEvent arg0) {
    }

    /** {@inheritDoc} */
    public void componentMoved(ComponentEvent arg0) {
    }

    /** {@inheritDoc} */
    public void componentResized(ComponentEvent arg0) {
    }

    /** {@inheritDoc} */
    public void componentShown(ComponentEvent arg0) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
