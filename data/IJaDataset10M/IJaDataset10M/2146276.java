package com.ourrosary.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import com.ourrosary.dao.PrayerDAO;
import com.ourrosary.main.Run;
import com.ourrosary.parts.BeadVO;
import com.ourrosary.parts.RosaryVO;
import com.ourrosary.swing.base.ImagePanel;
import com.ourrosary.swing.base.RosaryFrame;
import com.ourrosary.tray.SysTrayAPI;
import com.ourrosary.util.Lang2ISO;
import com.ourrosary.util.RosaryUtil;

/**
 * Rosary with Prayers
 *
 * @author Bhavani P Polimetla
 * @since Aug-15-2009
 */
public class Rosary extends RosaryFrame {

    private static final long serialVersionUID = -1559247217297317227L;

    private static Font fontArial = new Font("ARIAL", Font.PLAIN, 14);

    /**
	 * Enable this for logging
	 *
	 * @param str
	 */
    private static void printLog(String str) {
    }

    private JButton jButtonFontMinus;

    private JButton jButtonFontPlus;

    private static int fontSize = 12;

    private static int ClickCount = 0;

    private JMenuBar mainMenuBar;

    private JMenu About;

    private JMenuItem menuHelp;

    private JMenuItem menuAbout;

    private JMenuItem menuHidePrayers;

    private JMenuItem menuSettings;

    private JMenuItem menuExit;

    private JMenu File;

    private String strRosaryImageSelected = "";

    private String strLookandFeelSelected = "";

    private String strMisteriesSelected = "";

    private String strLanguageSelected = "";

    private String strLanguageFont = "";

    public static final String ROSARY_IMAGE_PATH = "/resources/images/";

    private String rosaryImageFile = "rosary_blue.jpg";

    private String rosaryDataFile = "rosary_blue.data";

    private boolean bNewImageFile = false;

    private JPanel jPanelFooter;

    private ImagePanel RosaryImagePane;

    private JScrollPane jScrollPane1;

    private JTextArea jtaPrayer;

    private JSplitPane splitPane;

    private String strLookandFeel_new = "";

    private static RosaryVO rosary = null;

    private static PrayerDAO prayers = null;

    public Rosary() {
        super();
        createSwingComponents();
        setScreenInCenter();
        rosary = new RosaryVO(rosaryDataFile);
        Calendar cal = new GregorianCalendar();
        String lang = RosaryUtil.getDefaultLanguageCode();
        String fontName = RosaryUtil.getLanguageFont(lang);
        String mistery = RosaryUtil.getMistery(cal);
        printLog("lang==>" + lang + " font==>" + fontName + " mistery==>" + mistery);
        jtaPrayer.setFont(new Font(fontName, Font.PLAIN, 16));
        prayers = new PrayerDAO(lang, mistery, fontName);
    }

    private void changeSkin(String strSkinGroup) {
        try {
            String strLookandFeelClass = javax.swing.UIManager.getCrossPlatformLookAndFeelClassName();
            if ("windows".equalsIgnoreCase(strSkinGroup)) strLookandFeelClass = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; else if ("Default".equalsIgnoreCase(strSkinGroup)) strLookandFeelClass = javax.swing.UIManager.getCrossPlatformLookAndFeelClassName(); else if ("Motif".equalsIgnoreCase(strSkinGroup)) strLookandFeelClass = "com.sun.java.swing.plaf.motif.MotifLookAndFeel"; else if ("Metal".equalsIgnoreCase(strSkinGroup)) strLookandFeelClass = "javax.swing.plaf.metal.MetalLookAndFeel";
            javax.swing.UIManager.setLookAndFeel(strLookandFeelClass);
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
            this.setSize(900, 600);
            this.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMenuComponent() {
        try {
            mainMenuBar = new JMenuBar();
            setJMenuBar(mainMenuBar);
            {
                File = new JMenu();
                mainMenuBar.add(File);
                File.setText("File");
                {
                    menuSettings = new JMenuItem();
                    File.add(menuSettings);
                    menuSettings.setText("Rosary Settings");
                    menuSettings.addMouseListener(new MouseAdapter() {

                        public void mousePressed(MouseEvent evt) {
                            menuSettingsPressed(evt);
                        }
                    });
                }
                {
                    menuHidePrayers = new JMenuItem();
                    File.add(menuHidePrayers);
                    menuHidePrayers.setText("Hide Prayers");
                    menuHidePrayers.addMouseListener(new MouseAdapter() {

                        public void mousePressed(MouseEvent evt) {
                            menuHidePrayersPressed(evt);
                        }
                    });
                }
                {
                    menuExit = new JMenuItem();
                    File.add(menuExit);
                    menuExit.setText("Exit");
                    menuExit.addMouseListener(new MouseAdapter() {

                        public void mousePressed(MouseEvent evt) {
                            menuExitPressed(evt);
                        }
                    });
                }
            }
            {
                About = new JMenu();
                mainMenuBar.add(About);
                About.setText("About");
                {
                    menuHelp = new JMenuItem();
                    About.add(menuHelp);
                    menuHelp.setText("Help");
                    menuHelp.addMouseListener(new MouseAdapter() {

                        public void mousePressed(MouseEvent evt) {
                            menuHelpPressed(evt);
                        }
                    });
                }
                {
                    menuAbout = new JMenuItem();
                    About.add(menuAbout);
                    menuAbout.setText("About Holy Rosary");
                    menuAbout.addMouseListener(new MouseAdapter() {

                        public void mousePressed(MouseEvent evt) {
                            menuAboutPressed(evt);
                        }
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createSwingComponents() {
        try {
            addWindowListener(new WindowAdapter() {

                public void windowClosed(WindowEvent windowevent) {
                }

                public void windowDeactivated(WindowEvent windowevent) {
                }

                public void windowIconified(WindowEvent evt) {
                    thisWindowIconified(evt);
                }
            });
            createMenuComponent();
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            prepareRosaryImagePanel();
            prepareRosaryPrayerPanel();
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, RosaryImagePane, jScrollPane1);
            splitPane.setPreferredSize(new java.awt.Dimension(900, 600));
            getContentPane().add(splitPane, BorderLayout.CENTER);
            getContentPane().add(getJPanelFooter(), BorderLayout.SOUTH);
            strLookandFeel_new = "Default";
            this.setTitle("Holy Rosary");
            this.setPreferredSize(new java.awt.Dimension(900, 600));
            this.setResizable(true);
            this.setIconImage(SysTrayAPI.getImageIcon());
            pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel getJPanelFooter() {
        if (null == jPanelFooter) {
            jPanelFooter = new JPanel();
            FlowLayout jPanelFooterLayout = new FlowLayout();
            jPanelFooterLayout.setAlignment(FlowLayout.RIGHT);
            jPanelFooter.setLayout(jPanelFooterLayout);
            jButtonFontPlus = new JButton("Font +");
            jButtonFontPlus.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent evt) {
                    jButtonFontPlusMouseClicked(evt);
                }
            });
            jButtonFontMinus = new JButton("Font -");
            jButtonFontMinus.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent evt) {
                    jButtonFontMinusMouseClicked(evt);
                }
            });
            jPanelFooter.add(getRemarks());
            jPanelFooter.add(jButtonFontPlus);
            jPanelFooter.add(jButtonFontMinus);
        }
        return jPanelFooter;
    }

    /**
	 * Remarks to display on footer.
	 * @return
	 */
    public JLabel getRemarks() {
        String demoText = "";
        if (RosaryUtil.isDemoMode()) demoText = "Demo / Testing Purpose only. Not for real use. Please send your feed back.";
        JLabel jlFooterNotes = new JLabel();
        jlFooterNotes.setText(demoText);
        jlFooterNotes.setFont(new java.awt.Font("Arial", 1, 12));
        jlFooterNotes.setForeground(new java.awt.Color(255, 0, 0));
        return jlFooterNotes;
    }

    public JTextArea getJTextArea1() {
        return jtaPrayer;
    }

    private void jButtonFontMinusMouseClicked(MouseEvent evt) {
        if (fontSize > 12) fontSize--;
        fontArial = jtaPrayer.getFont().deriveFont(Font.PLAIN, fontSize);
        jtaPrayer.setFont(fontArial);
        jtaPrayer.repaint();
    }

    private void jButtonFontPlusMouseClicked(MouseEvent evt) {
        if (fontSize < 28) fontSize++;
        fontArial = jtaPrayer.getFont().deriveFont(Font.PLAIN, fontSize);
        jtaPrayer.setFont(fontArial);
        jtaPrayer.repaint();
    }

    /**
	 * This is used when user changes settings.
	 *
	 * @param ImageFileName
	 */
    private void loadImageToPanel(String ImageFileName) {
        printLog("image file-->" + ROSARY_IMAGE_PATH + rosaryImageFile);
        try {
            BufferedImage bufferedImage = new BufferedImage(450, 500, Transparency.OPAQUE);
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            Image image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource(ROSARY_IMAGE_PATH + rosaryImageFile), rosaryImageFile));
            g.drawImage(image, 0, 0, null);
            RosaryImagePane.getGraphics().drawImage(bufferedImage, 0, 0, null);
            RosaryImagePane.setBufferedImage(bufferedImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Load rosary data for given settings.
	 *
	 * @param rosaryImage
	 */
    private void loadRosaryData(String rosaryImage) {
        if ("Red Beads".equalsIgnoreCase(rosaryImage)) {
            rosaryImageFile = "rosary_red.jpg";
            rosaryDataFile = "rosary_red.data";
        } else if ("Green Beads".equalsIgnoreCase(rosaryImage)) {
            rosaryImageFile = "rosary_green.jpg";
            rosaryDataFile = "rosary_green.data";
        } else if ("Blue Beads".equalsIgnoreCase(rosaryImage)) {
            rosaryImageFile = "rosary_blue.jpg";
            rosaryDataFile = "rosary_blue.data";
        } else {
            rosaryImageFile = "rosary_blue.jpg";
            rosaryDataFile = "rosary_blue.data";
        }
        Lang2ISO lang2Iso = new Lang2ISO();
        String langISOCode = (String) lang2Iso.get(strLanguageSelected.toLowerCase());
        prayers = new PrayerDAO(langISOCode, strMisteriesSelected, strLanguageFont);
        rosary = new RosaryVO(rosaryDataFile);
        loadImageToPanel(rosaryImageFile);
        setPrayerFont();
    }

    private void menuAboutPressed(MouseEvent evt) {
        AboutDialog ad = new AboutDialog(this);
        ad.show();
    }

    private void menuExitPressed(MouseEvent evt) {
        System.exit(0);
    }

    private void menuHelpPressed(MouseEvent evt) {
        HelpDialog hd = new HelpDialog(this);
        hd.show();
    }

    private void menuHidePrayersPressed(MouseEvent evt) {
        FingerRosary inst = new FingerRosary();
        inst.setVisible(true);
        Run.setFrame(inst);
        this.dispose();
    }

    private void menuSettingsPressed(MouseEvent evt) {
        RosarySettingsDialog dialog1 = new RosarySettingsDialog(this);
        dialog1.show();
        if ("cancel".equalsIgnoreCase(dialog1.getStrOperation())) return;
        strLookandFeelSelected = dialog1.getStrLookandFeelSelected();
        strLanguageSelected = dialog1.getStrLanguageSelected();
        strLanguageFont = dialog1.getStrLanguageFont();
        strRosaryImageSelected = dialog1.getStrRosaryImageSelected();
        strMisteriesSelected = dialog1.getStrMisteriesSelected();
        jtaPrayer.setText("");
        printLog("strLookandFeelSelected==>" + strLookandFeelSelected);
        printLog("strLanguageSelected==>" + strLanguageSelected);
        printLog("strRosaryImageSelected==>" + strRosaryImageSelected);
        printLog("strMisteriesSelected==>" + strMisteriesSelected);
        bNewImageFile = true;
        strLookandFeel_new = strLookandFeelSelected;
        changeSkin(strLookandFeel_new);
        loadRosaryData(strRosaryImageSelected);
    }

    private void prepareRosaryImagePanel() {
        try {
            Image image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource(ROSARY_IMAGE_PATH + rosaryImageFile), rosaryImageFile));
            RosaryImagePane = new ImagePanel(image);
            RosaryImagePane.setBackground(Color.white);
            if (null == RosaryImagePane.getGraphics()) printLog("getGraphics is null");
            RosaryImagePane.setSize(450, 500);
            RosaryImagePane.setPreferredSize(new java.awt.Dimension(450, 500));
            RosaryImagePane.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent evt) {
                    RosaryImagePaneMouseClicked(evt);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void prepareRosaryPrayerPanel() {
        try {
            jScrollPane1 = new JScrollPane();
            getContentPane().add(jScrollPane1, BorderLayout.CENTER);
            {
                jtaPrayer = new JTextArea();
                jScrollPane1.setViewportView(getJTextArea1());
                jtaPrayer.setName("Prayer");
                jtaPrayer.setEditable(false);
                jtaPrayer.setWrapStyleWord(true);
                jtaPrayer.setLineWrap(true);
                jtaPrayer.setFont(fontArial);
            }
            jScrollPane1.setSize(300, 600);
            jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 600));
            jScrollPane1.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printRosaryBeadandPrayer(MouseEvent evt) {
        if (null == evt) return;
        printLog("Mouse clicked on: " + evt.getX() + ";" + evt.getY() + ";");
        printLog("image file-->" + ROSARY_IMAGE_PATH + rosaryImageFile);
        try {
            int x = evt.getX();
            int y = evt.getY();
            int radius = 15;
            Point2D.Double point1 = new Point2D.Double(x, y);
            BeadVO bead = rosary.getBeadForGivenPoint(point1);
            if (bNewImageFile) {
                bNewImageFile = false;
            } else if (null == bead) return;
            BufferedImage bufferedImage = new BufferedImage(450, 500, Transparency.OPAQUE);
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            Image image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource(ROSARY_IMAGE_PATH + rosaryImageFile), rosaryImageFile));
            g.drawImage(image, 0, 0, null);
            if (null != bead) {
                g.setColor(Color.BLUE);
                Ellipse2D.Double circle = new Ellipse2D.Double(bead.getX() - 7, bead.getY() - 7, radius, radius);
                g.fill(circle);
                String temp = prayers.getPrayer(bead.getBeadType(), bead.getBeadSequence());
                temp = temp.replace("null", "");
                printLog("temp prayer==>" + temp);
                temp = prayers.convertUnicodeToChar(temp);
                jtaPrayer.setText(temp);
                jtaPrayer.repaint();
                jtaPrayer.setCaretPosition(1);
                printLog("font name==>" + jtaPrayer.getFont().getFontName());
            }
            RosaryImagePane.getGraphics().drawImage(bufferedImage, 0, 0, null);
            RosaryImagePane.setBufferedImage(bufferedImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Used to plot data from file for testing purpose.
	 *
	 * @param beads
	 */
    private void printRosarySeedDataToCanvas(BeadVO[] beads) {
        if (null == beads || beads.length <= 0) return;
        try {
            BufferedImage bufferedImage = new BufferedImage(450, 500, Transparency.OPAQUE);
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            Image image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource(ROSARY_IMAGE_PATH + rosaryImageFile), rosaryImageFile));
            g.drawImage(image, 0, 0, null);
            g.setColor(Color.RED);
            for (int i = 0; i < beads.length; i++) {
                BeadVO bead = beads[i];
                Ellipse2D.Double circle = null;
                if (bead.getBeadType().equalsIgnoreCase("SMALL")) circle = new Ellipse2D.Double(bead.getX() - 7, bead.getY() - 7, bead.getRadius(), bead.getRadius()); else circle = new Ellipse2D.Double(bead.getX() - 10, bead.getY() - 10, bead.getRadius(), bead.getRadius());
                g.fill(circle);
            }
            RosaryImagePane.getGraphics().drawImage(bufferedImage, 0, 0, null);
            RosaryImagePane.setBufferedImage(bufferedImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printRosarySeedDataToConsole(MouseEvent evt) {
        if (null == evt) return;
        printLog(ClickCount + ";" + evt.getX() + ";" + evt.getY() + ";");
        ClickCount++;
        try {
            int x = evt.getX();
            int y = evt.getY();
            int radius = 15;
            BufferedImage bufferedImage = new BufferedImage(450, 500, Transparency.OPAQUE);
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            Image image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource(ROSARY_IMAGE_PATH + rosaryImageFile), rosaryImageFile));
            g.drawImage(image, 0, 0, null);
            g.setColor(Color.BLACK);
            Ellipse2D.Double circle = new Ellipse2D.Double(x - 7, y - 7, radius, radius);
            g.fill(circle);
            RosaryImagePane.getGraphics().drawImage(bufferedImage, 0, 0, null);
            RosaryImagePane.setBufferedImage(bufferedImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 *
	 * @param evt
	 */
    private void RosaryImagePaneMouseClicked(MouseEvent evt) {
        try {
            printRosaryBeadandPrayer(evt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * PrayerDAO is having font information.
	 */
    private void setPrayerFont() {
        Font fontPrayer = prayers.getFont();
        jtaPrayer.setFont(fontPrayer);
        jtaPrayer.repaint();
    }

    private void thisWindowIconified(WindowEvent evt) {
        printLog((new StringBuilder()).append("this.windowIconified, event=").append(evt).toString());
        setVisible(false);
    }
}
