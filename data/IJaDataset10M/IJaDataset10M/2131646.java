package com.umc.gui.content.manage;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.umc.beans.media.Movie;
import com.umc.beans.media.Photo;
import com.umc.gui.GuiController;
import com.umc.helper.UMCImageUtils;

/**
 *
 * @author Administrator
 */
public class ManagePhotoPanel extends javax.swing.JPanel {

    private static Logger log = Logger.getLogger("com.umc.file");

    private static final long serialVersionUID = -1240458817270956378L;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel lblPhoto;

    private javax.swing.JPanel panelPhoto;

    private javax.swing.JTextPane textPaneExif;

    private Photo selectedPhoto = null;

    private LoadPhoto loadPhoto;

    private ImageIcon loaderIcon;

    /** Creates new form ManagePicturePanel */
    public ManagePhotoPanel() {
        initComponents();
    }

    private void initComponents() {
        loaderIcon = new ImageIcon(getClass().getResource("/com/umc/gui/resources/loader30.gif"));
        setMinimumSize(new java.awt.Dimension(620, 0));
        setPreferredSize(new java.awt.Dimension(620, 579));
        panelPhoto = new javax.swing.JPanel();
        lblPhoto = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textPaneExif = new javax.swing.JTextPane();
        panelPhoto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblPhoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.jdesktop.layout.GroupLayout panelPhotoLayout = new org.jdesktop.layout.GroupLayout(panelPhoto);
        panelPhoto.setLayout(panelPhotoLayout);
        panelPhotoLayout.setHorizontalGroup(panelPhotoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(lblPhoto, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE));
        panelPhotoLayout.setVerticalGroup(panelPhotoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(lblPhoto, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE));
        textPaneExif.setEditable(false);
        jScrollPane1.setViewportView(textPaneExif);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, panelPhoto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(207, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(panelPhoto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE).addContainerGap()));
    }

    public void showPhoto(Object obj) {
        if (obj == null) {
            textPaneExif.setText("");
            lblPhoto.setIcon(null);
            return;
        } else {
            lblPhoto.setIcon(loaderIcon);
            selectedPhoto = (Photo) obj;
            textPaneExif.setText(selectedPhoto.getExifInfo());
            loadPhoto = new LoadPhoto();
            loadPhoto.execute();
        }
    }

    private class LoadPhoto extends SwingWorker<ImageIcon, Void> {

        public LoadPhoto() {
        }

        @Override
        protected ImageIcon doInBackground() throws Exception {
            ImageIcon photo = new ImageIcon();
            if (selectedPhoto.getPCPath() != null && new File(selectedPhoto.getPCPath()).exists()) {
                photo = new ImageIcon(GuiController.getInstance().getImageUtils().getScaledInstance(GuiController.getInstance().getImageUtils().loadImage(selectedPhoto.getPCPath()), lblPhoto.getWidth(), lblPhoto.getHeight(), true));
            }
            if (isCancelled()) {
                return photo;
            }
            return photo;
        }

        public void done() {
            try {
                if (!isCancelled()) {
                    ImageIcon photo = get();
                    if (photo != null) {
                        lblPhoto.setIcon(photo);
                    } else {
                        lblPhoto.setIcon(null);
                        lblPhoto.setText("N/A");
                    }
                }
            } catch (CancellationException e) {
                log.info("Load photo cancelled!");
            } catch (InterruptedException e) {
                log.error("Error while loading photo:", e);
            } catch (ExecutionException e) {
                log.error("Error while loading photo:", e);
            }
        }

        @Override
        public String toString() {
            return "tst";
        }
    }
}
