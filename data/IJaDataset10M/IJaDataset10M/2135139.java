package wand.graphicsChooser;

import java.io.File;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PatternAmbientButtons extends JPanel {

    private File[] files;

    private int buttonWidth = 90, buttonHeight = 70;

    private int gridWidth = 9, gridHeight = 5;

    public PatternAmbientButtons() {
        setLayout(new GridLayout(gridHeight, gridWidth));
        setPreferredSize(new Dimension(buttonWidth * gridWidth, buttonHeight * gridHeight));
        File dir = new File("icons/patterns/ambient");
        files = dir.listFiles();
        makeButtons();
        this.setBorder(wand.ChannelFrame.chooserBorder);
    }

    private int buttonCount = 0;

    private void makeButtons() {
        for (int i = 0; i < files.length; i++) {
            String file = files[i].getName();
            String ext = (file.lastIndexOf(".") == -1) ? "" : file.substring(file.lastIndexOf("."), file.length());
            final String fileName = file.substring(0, file.lastIndexOf('.'));
            if (ext.equals(".GIF") || ext.equals(".JPG") || ext.equals(".JPEG") || ext.equals(".PNG") || ext.equals(".gif") || ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png")) {
                JButton b = new JButton();
                b.setText("");
                ImageIcon icon = new ImageIcon(files[i].getPath());
                b.setIcon(icon);
                b.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonPushed(fileName);
                        hideFrame();
                    }
                });
                this.add(b);
                buttonCount++;
            }
        }
        for (int i = buttonCount; i < gridWidth * gridHeight; i++) {
            this.add(new JLabel());
        }
    }

    private void hideFrame() {
        wand.ChannelFrame.patternChooserFrame.setVisible(false);
    }

    private void buttonPushed(String choice) {
        wand.ChannelFrame.patternChooserFrame.choiceMade(choice);
    }
}
