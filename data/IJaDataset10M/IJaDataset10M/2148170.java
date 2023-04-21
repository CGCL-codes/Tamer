package gui_menu;

import gui_mainFrame.PatientWindow_existingPatient;
import gui_mainFrame.PatientWindow_tabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import language.Messages;
import main.ISettings;

/**
 * 
 * @author debous
 * @Create 02/04/2011
 * @lastUpdate 02/04/2011
 * 
 * About frame
 */
public class About extends JFrame implements ISettings {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2535068339456727801L;

    /**
	 * Exit button
	 */
    private JButton jbExit;

    /**
	 * Label information
	 */
    private JLabel labelLogo;

    /**
	 * The text area
	 */
    private JTextArea textArea;

    /**
	 * The container
	 */
    private Container paneNewWindow;

    /**
	 * The button panel that contains send and exit button
	 */
    private JPanel panelButtons;

    /**
	 * The scroll pane
	 */
    private JScrollPane scrollPane;

    /**
	 * The previous frame
	 */
    private PatientWindow_existingPatient previousFrame;

    /**
	 * Close button title
	 */
    private static final String CLOSE_BUTTON_TITLE = Messages.getInstance().getString("About.0");

    /**
	 * Frame title
	 */
    private static final String ABOUT_TITLE = Messages.getInstance().getString("About.1");

    /**
	 * Frame height
	 */
    private static final int FRAME_HEIGHT_SIZE = 500;

    /**
	 * Frame width
	 */
    private static final int FRAME_WIDTH_SIZE = 800;

    /**
	 * About text
	 */
    final String aboutText = Messages.getInstance().getString("About.2") + Messages.getInstance().getString("About.3") + Messages.getInstance().getString("About.4") + Messages.getInstance().getString("About.5") + Messages.getInstance().getString("About.6") + Messages.getInstance().getString("About.7") + Messages.getInstance().getString("About.8") + Messages.getInstance().getString("About.9") + Messages.getInstance().getString("About.10") + Messages.getInstance().getString("About.11") + Messages.getInstance().getString("About.12") + Messages.getInstance().getString("About.13") + Messages.getInstance().getString("About.14") + Messages.getInstance().getString("About.16") + Messages.getInstance().getString("About.17") + Messages.getInstance().getString("About.18") + Messages.getInstance().getString("About.19") + Messages.getInstance().getString("About.21") + Messages.getInstance().getString("About.22") + Messages.getInstance().getString("About.23") + Messages.getInstance().getString("About.24");

    /**
	 * Constructor
	 * @param previousFrame The previous frame
	 */
    public About(PatientWindow_existingPatient previousFrame) {
        super(ABOUT_TITLE);
        this.previousFrame = previousFrame;
        this.createComponents();
        this.createPanels();
    }

    /**
	 * Create components
	 */
    private void createComponents() {
        labelLogo = new JLabel();
        this.textArea = new JTextArea();
        this.textArea.setEditable(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 500));
        scrollPane.setAutoscrolls(false);
        labelLogo.setIcon(createImageIcon(PATH_ASCLEPIUS_LOGO));
        labelLogo.setBackground(Color.WHITE);
        labelLogo.setHorizontalAlignment(JLabel.CENTER);
        textArea.setText(aboutText);
        jbExit = new JButton(CLOSE_BUTTON_TITLE);
        jbExit.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                jbExit.setForeground(MENU_MOUSE_ENTERED);
            }

            public void mouseExited(MouseEvent arg0) {
                jbExit.setForeground(MENU_MOUSE_EXITED);
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                jbExit.setForeground(MENU_MOUSE_PRESSED);
            }
        });
        jbExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                About.this.dispose();
            }
        });
    }

    /**
	 * Get an icon
	 * @param path The icon path
	 * @return an icon
	 */
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = PatientWindow_tabbedPane.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println(Messages.getInstance().getString("About.15") + path);
            return null;
        }
    }

    /**
	 * Create all panels
	 */
    private void createPanels() {
        this.paneNewWindow = this.getContentPane();
        panelButtons = new JPanel();
        paneNewWindow.setLayout(new BorderLayout());
        paneNewWindow.setBackground(Color.WHITE);
        panelButtons.setLayout(new FlowLayout());
        panelButtons.add(jbExit);
        paneNewWindow.add(labelLogo, BorderLayout.PAGE_START);
        paneNewWindow.add(scrollPane, BorderLayout.CENTER);
        paneNewWindow.add(panelButtons, BorderLayout.PAGE_END);
        this.setSize(FRAME_WIDTH_SIZE, FRAME_HEIGHT_SIZE);
        this.setResizable(false);
        this.setLocationRelativeTo(previousFrame);
        this.setVisible(true);
    }
}
