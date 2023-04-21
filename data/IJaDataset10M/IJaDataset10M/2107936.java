package wilos.presentation.assistant.view.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import wilos.presentation.assistant.control.ExceptionManager;
import wilos.presentation.assistant.ressources.Bundle;
import wilos.presentation.assistant.ressources.ImagesService;

/**
 * 
 * @author nicastel
 *
 */
public class DownLoadFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel infoFile = null;

    private JLabel dLFile = null;

    private JLabel img = null;

    private JPanel mainPanel = null;

    private JPanel buttonPanel = null;

    private JPanel closeButtonPanel = null;

    private JPanel infoFilePanel = null;

    private JButton close = null;

    private static String textInfoFile = Bundle.getText("downloadFrame.FileInfo");

    private static String fileNameInfoFirst = Bundle.getText("downloadFrame.fileNameInfoFirst");

    private static String fileNameInfoLast = Bundle.getText("downloadFrame.fileNameInfoLast");

    /**
	 * Constructor DownLoadFrame
	 * 
	 */
    public DownLoadFrame(String file, String filePathToDownload) {
        super();
        this.initialize();
        this.displayInformation(file, filePathToDownload);
    }

    public void endOfTreatment() {
        this.infoFile.setText(Bundle.getText("downloadFrame.finishDownload"));
        close.setFocusable(true);
        close.setEnabled(true);
        img.setEnabled(false);
    }

    private void displayInformation(String file, String filePathToDownload) {
        this.dLFile.setText(textInfoFile + '\r' + "  " + System.getProperty("line.separator") + filePathToDownload);
        this.infoFile.setText(fileNameInfoFirst + "\n \r" + getFileName(file) + " " + fileNameInfoLast);
    }

    private String getFileName(String path_file) {
        String fileToBeReturn = "";
        int indexSeparator = 0;
        indexSeparator = path_file.lastIndexOf(File.separator);
        fileToBeReturn = path_file.substring(indexSeparator + 1);
        return fileToBeReturn;
    }

    private void initialize() {
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().width / 2 - (605 / 2));
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2 - (220 / 2));
        this.setTitle(Bundle.getText("downloadFrame.downloadTitle"));
        this.setBounds(x, y, 500, 150);
        this.setContentPane(getMainPanel());
        this.setVisible(true);
        try {
            this.setIconImage(ImagesService.getImage("images.frameIcon"));
        } catch (IOException ex) {
            new ExceptionManager(ex);
        }
    }

    private Container getButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(getDLFile(), BorderLayout.NORTH);
        buttonPanel.add(getButtonClosePanel(), BorderLayout.SOUTH);
        return buttonPanel;
    }

    private Component getButtonClosePanel() {
        closeButtonPanel = new JPanel();
        closeButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        closeButtonPanel.setLayout(new GridBagLayout());
        closeButtonPanel.add(getClose(), new GridBagConstraints());
        return closeButtonPanel;
    }

    private Container getMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(getInfoFilePanel(), BorderLayout.NORTH);
        mainPanel.add(getImg(), BorderLayout.CENTER);
        mainPanel.add(getButtonPanel(), BorderLayout.SOUTH);
        return mainPanel;
    }

    private Component getInfoFilePanel() {
        infoFilePanel = new JPanel();
        infoFilePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoFilePanel.setLayout(new BorderLayout());
        infoFilePanel.add(getInfoFile(), BorderLayout.NORTH);
        return infoFilePanel;
    }

    private JButton getClose() {
        if (close == null) {
            close = new JButton("Fermer");
            close.setEnabled(false);
            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    DownLoadFrame.this.dispose();
                }
            });
        }
        return close;
    }

    private Component getDLFile() {
        if (this.dLFile == null) {
            this.dLFile = new JLabel();
            this.dLFile.setBounds(new Rectangle(20, 140, 400, 20));
            this.dLFile.setText("Le fichier est telechargement vers : X");
        }
        return this.dLFile;
    }

    private JLabel getInfoFile() {
        if (this.infoFile == null) {
            this.infoFile = new JLabel();
            this.infoFile.setText("Le fichier X est en cours de telechargement...");
        }
        return this.infoFile;
    }

    private JLabel getImg() {
        if (img == null) {
            img = new JLabel(ImagesService.getImageIcon("images.progressLoading"));
            img.setSize(150, 50);
            img.setBounds(new Rectangle(20, 40, 300, 100));
        }
        return img;
    }
}
