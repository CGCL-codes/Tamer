package org.gerhardb.lib.dirtree.filelist.popup;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.gerhardb.lib.image.ImageChangeUtil;
import org.gerhardb.lib.image.ImageFactory;
import org.gerhardb.lib.io.FileUtil;
import org.gerhardb.lib.swing.JPanelRows;
import org.gerhardb.lib.util.startup.AppStarter;
import org.gerhardb.lib.dirtree.filelist.*;

/**
 *
 * @author  Gerhard Beck
 */
public class FileNameChangeSingleTab extends JPanel {

    File myFile;

    JTextField myTextFld = new JTextField(33);

    JLabel myPicture = new JLabel(AppStarter.getString("SingleFileNameChanger.0"));

    FileListPlugins myPlugins;

    FileSingleNameChangeDialog myDialog;

    JButton myOkBtn = new JButton(AppStarter.getString("ok"));

    FileNameChangeSingleTab(FileSingleNameChangeDialog dialog, File file, FileListPlugins plubins) {
        super(new BorderLayout());
        this.myDialog = dialog;
        this.myFile = file;
        this.myPlugins = plubins;
        this.myTextFld.setText(this.myFile.getName());
        try {
            BufferedImage image = ImageFactory.getImageFactory().makeImage(this.myFile).getImage();
            BufferedImage sized = ImageChangeUtil.fitAspectDown(image, 100, 100);
            ImageIcon imageIcon = new ImageIcon(sized);
            this.myPicture = new JLabel(imageIcon);
            this.myPicture.setSize(100, 100);
            Border raisedbevel = BorderFactory.createRaisedBevelBorder();
            Border loweredbevel = BorderFactory.createLoweredBevelBorder();
            Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
            this.myPicture.setBorder(compound);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        layoutDialog();
    }

    public JButton getDefaultButton() {
        return this.myOkBtn;
    }

    void ok() {
        String newName = this.myTextFld.getText();
        if (newName == null) {
            return;
        }
        newName = newName.trim();
        if (newName.length() == 0) {
            return;
        }
        String newEnding = FileUtil.getExtension(newName);
        if (newEnding == null) {
            String oldEnding = FileUtil.getExtension(this.myFile.getName());
            newName = newName + "." + oldEnding;
        }
        File newFile = this.myFile;
        try {
            String dir = this.myFile.getParentFile().getCanonicalPath();
            String newPathName = dir + "/" + newName;
            newFile = new File(newPathName);
            if (newFile.exists()) {
                JOptionPane.showMessageDialog(this, "There is already a file named:\n" + newFile.getName(), AppStarter.getString("problem"), JOptionPane.ERROR_MESSAGE);
            } else {
                this.myDialog.iRenamed = true;
                this.myFile.renameTo(newFile);
            }
        } catch (Exception ex) {
            JOptionPane.showInputDialog(this, AppStarter.getString("FileList.0") + " " + this.myFile.getName() + ".\n" + ex.getMessage(), AppStarter.getString("FileList.6"), JOptionPane.WARNING_MESSAGE);
            this.myPlugins.reloadScroller();
            this.myPlugins.getScroller().selectFile(this.myFile);
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.myPlugins.reloadScroller();
        this.myPlugins.getScroller().selectFile(newFile);
        this.myDialog.done();
    }

    void cancel() {
        this.myTextFld.setText(null);
        this.myDialog.done();
    }

    private void layoutDialog() {
        this.myOkBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                ok();
            }
        });
        JButton cancelBtn = new JButton(AppStarter.getString("cancel"));
        cancelBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                cancel();
            }
        });
        JPanelRows myCenterPanel = new JPanelRows();
        JPanel aRow = myCenterPanel.topRow();
        aRow = myCenterPanel.nextRow();
        aRow.add(new JLabel(AppStarter.getString("FileList.1") + AppStarter.getString("colon")));
        aRow.add(this.myTextFld);
        aRow = myCenterPanel.nextRow();
        aRow.add(new JLabel(AppStarter.getString("SingleFileNameChanger.3")));
        aRow = myCenterPanel.nextRow(new FlowLayout(FlowLayout.CENTER));
        aRow.add(this.myOkBtn);
        aRow.add(cancelBtn);
        this.add(this.myPicture, BorderLayout.WEST);
        this.add(myCenterPanel, BorderLayout.CENTER);
        this.myOkBtn.setMnemonic(KeyEvent.VK_K);
    }
}
