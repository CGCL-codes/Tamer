package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import Converter.Client;

public class ImageExamplePanel extends JPanel {

    private JPanel browserPanel;

    private JScrollPane displayArea;

    private JTextField imageUrl;

    private JLabel openFile;

    private JButton browse;

    private ImageLabel imageDisplay;

    private ImageIcon image;

    private JFrame clientFrame;

    public ImageExamplePanel(JFrame frame, Client client) {
        clientFrame = frame;
        this.setLayout(new BorderLayout());
        browserPanel = new JPanel();
        browserPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        openFile = new JLabel("Open file: ");
        imageUrl = new JTextField(40);
        imageUrl.setEditable(false);
        browse = new JButton("Browse");
        browse.addActionListener(new BrowserListener());
        browserPanel.add(openFile);
        browserPanel.add(imageUrl);
        browserPanel.add(browse);
        image = null;
        imageDisplay = new ImageLabel(image, client);
        displayArea = new JScrollPane(imageDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        displayArea.setViewportBorder(BorderFactory.createLineBorder(Color.BLUE));
        this.add(browserPanel, BorderLayout.NORTH);
        this.add(displayArea, BorderLayout.CENTER);
        displayArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        displayArea.setBackground(Color.white);
    }

    public String getImagePath() {
        return imageUrl.getText();
    }

    public void setImagePath(String path) {
        imageUrl.setText(path);
        imageDisplay.setText("");
        imageDisplay.setImageIcon(new ImageIcon(path));
        imageDisplay.setHorizontalAlignment(SwingConstants.LEFT);
        displayArea.revalidate();
    }

    public ImageLabel getImageLabel() {
        return imageDisplay;
    }

    private class BrowserListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            ImageFileFilter filter = new ImageFileFilter();
            chooser.addChoosableFileFilter(filter);
            if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
                try {
                    imageUrl.setText(chooser.getSelectedFile().getCanonicalPath());
                    imageDisplay.setText("");
                    imageDisplay.setImageIcon(new ImageIcon(chooser.getSelectedFile().getCanonicalPath()));
                    imageDisplay.setHorizontalAlignment(SwingConstants.LEFT);
                    displayArea.revalidate();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
