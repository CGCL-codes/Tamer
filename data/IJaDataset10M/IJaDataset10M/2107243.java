package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
import Converter.ModifyPdf;

public class PdfExamplePanel extends JPanel {

    private JPanel browserPanel;

    private JScrollPane displayArea;

    private JTextField pdfUrl;

    private JLabel openFile;

    private JButton browse;

    private PdfLabel pdfDisplay;

    private ImageIcon ii;

    private JFrame clientFrame;

    public PdfExamplePanel(JFrame frame, Client client) {
        clientFrame = frame;
        this.setLayout(new BorderLayout());
        browserPanel = new JPanel();
        browserPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        openFile = new JLabel("Open file: ");
        pdfUrl = new JTextField(40);
        pdfUrl.setEditable(false);
        browse = new JButton("Browse");
        browse.addActionListener(new BrowserListener());
        browserPanel.add(openFile);
        browserPanel.add(pdfUrl);
        browserPanel.add(browse);
        ii = null;
        pdfDisplay = new PdfLabel(ii, client);
        displayArea = new JScrollPane(pdfDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        displayArea.setViewportBorder(BorderFactory.createLineBorder(Color.BLUE));
        this.add(browserPanel, BorderLayout.NORTH);
        this.add(displayArea, BorderLayout.CENTER);
        displayArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        displayArea.setBackground(Color.white);
    }

    public String getImagePath() {
        return pdfUrl.getText();
    }

    public void setPdfPath(String path) {
        pdfUrl.setText(path);
        pdfDisplay.setText("");
        if (ModifyPdf.PdfImage(path) == null) {
            return;
        }
        pdfDisplay.setImageIcon(new ImageIcon(ModifyPdf.PdfImage(path).get(0)));
        pdfDisplay.setHorizontalAlignment(SwingConstants.LEFT);
        displayArea.revalidate();
    }

    public PdfLabel getImageLabel() {
        return pdfDisplay;
    }

    private class BrowserListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            PdfFileFilter filter = new PdfFileFilter();
            chooser.addChoosableFileFilter(filter);
            if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
                try {
                    pdfUrl.setText(chooser.getSelectedFile().getCanonicalPath());
                    pdfDisplay.setText("");
                    BufferedImage bi = null;
                    pdfDisplay.setImageIcon(new ImageIcon(bi));
                    pdfDisplay.setHorizontalAlignment(SwingConstants.LEFT);
                    displayArea.revalidate();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
