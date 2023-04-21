package net.pms.newgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import net.pms.Messages;
import net.pms.PMS;
import net.pms.configuration.PmsConfiguration;
import net.pms.gui.IFrame;
import net.pms.io.WindowsNamedPipe;
import net.pms.newgui.update.AutoUpdateDialog;
import net.pms.update.AutoUpdater;
import net.pms.util.PMSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.sun.jna.Platform;

public class LooksFrame extends JFrame implements IFrame, Observer {

    private static final Logger logger = LoggerFactory.getLogger(LooksFrame.class);

    private final AutoUpdater autoUpdater;

    private final PmsConfiguration configuration;

    public static final String START_SERVICE = "start.service";

    private static final long serialVersionUID = 8723727186288427690L;

    private NavigationShareTab ft;

    private StatusTab st;

    private TracesTab tt;

    private TranscodingTab tr;

    private GeneralTab nt;

    private AbstractButton reload;

    private JLabel status;

    protected static final Dimension PREFERRED_SIZE = new Dimension(1000, 750);

    protected static final Dimension MINIMUM_SIZE = new Dimension(800, 480);

    private static boolean lookAndFeelInitialized = false;

    public TracesTab getTt() {
        return tt;
    }

    public NavigationShareTab getFt() {
        return ft;
    }

    public TranscodingTab getTr() {
        return tr;
    }

    public AbstractButton getReload() {
        return reload;
    }

    static void initializeLookAndFeel() {
        if (lookAndFeelInitialized) {
            return;
        }
        LookAndFeel selectedLaf = null;
        if (Platform.isWindows()) {
            try {
                selectedLaf = (LookAndFeel) Class.forName("com.jgoodies.looks.windows.WindowsLookAndFeel").newInstance();
            } catch (Exception e) {
                selectedLaf = new PlasticLookAndFeel();
            }
        } else if (System.getProperty("nativelook") == null && !Platform.isMac()) {
            selectedLaf = new PlasticLookAndFeel();
        } else {
            try {
                String systemClassName = UIManager.getSystemLookAndFeelClassName();
                try {
                    String gtkLAF = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
                    Class.forName(gtkLAF);
                    if (systemClassName.equals("javax.swing.plaf.metal.MetalLookAndFeel")) {
                        systemClassName = gtkLAF;
                    }
                } catch (ClassNotFoundException ce) {
                }
                logger.debug("Choosing java look and feel: " + systemClassName);
                UIManager.setLookAndFeel(systemClassName);
            } catch (Exception e1) {
                selectedLaf = new PlasticLookAndFeel();
                logger.error("Error while setting native look and feel: ", e1);
            }
        }
        if (selectedLaf instanceof PlasticLookAndFeel) {
            PlasticLookAndFeel.setPlasticTheme(PlasticLookAndFeel.createMyDefaultTheme());
            PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_DEFAULT_VALUE);
            PlasticLookAndFeel.setHighContrastFocusColorsEnabled(false);
        } else if (selectedLaf != null && selectedLaf.getClass() == MetalLookAndFeel.class) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        }
        JRadioButton radio = new JRadioButton();
        radio.getUI().uninstallUI(radio);
        JCheckBox checkBox = new JCheckBox();
        checkBox.getUI().uninstallUI(checkBox);
        if (selectedLaf != null) {
            try {
                UIManager.setLookAndFeel(selectedLaf);
            } catch (Exception e) {
                System.out.println("Can't change L&F: " + e);
            }
        }
        lookAndFeelInitialized = true;
    }

    /**
	 * Constructs a <code>DemoFrame</code>, configures the UI,
	 * and builds the content.
	 */
    public LooksFrame(AutoUpdater autoUpdater, PmsConfiguration configuration) {
        this.autoUpdater = autoUpdater;
        this.configuration = configuration;
        assert this.configuration != null;
        Options.setDefaultIconSize(new Dimension(18, 18));
        Options.setUseNarrowButtons(true);
        Options.setTabIconsEnabled(true);
        UIManager.put(Options.POPUP_DROP_SHADOW_ENABLED_KEY, null);
        initializeLookAndFeel();
        if (autoUpdater != null) {
            autoUpdater.addObserver(this);
            autoUpdater.pollServer();
        }
        Font sf = null;
        final String language = configuration.getLanguage();
        if (language != null && (language.equals("ja") || language.startsWith("zh"))) {
            sf = new Font("Serif", Font.PLAIN, 12);
        }
        if (sf != null) {
            UIManager.put("Button.font", sf);
            UIManager.put("ToggleButton.font", sf);
            UIManager.put("RadioButton.font", sf);
            UIManager.put("CheckBox.font", sf);
            UIManager.put("ColorChooser.font", sf);
            UIManager.put("ToggleButton.font", sf);
            UIManager.put("ComboBox.font", sf);
            UIManager.put("ComboBoxItem.font", sf);
            UIManager.put("InternalFrame.titleFont", sf);
            UIManager.put("Label.font", sf);
            UIManager.put("List.font", sf);
            UIManager.put("MenuBar.font", sf);
            UIManager.put("Menu.font", sf);
            UIManager.put("MenuItem.font", sf);
            UIManager.put("RadioButtonMenuItem.font", sf);
            UIManager.put("CheckBoxMenuItem.font", sf);
            UIManager.put("PopupMenu.font", sf);
            UIManager.put("OptionPane.font", sf);
            UIManager.put("Panel.font", sf);
            UIManager.put("ProgressBar.font", sf);
            UIManager.put("ScrollPane.font", sf);
            UIManager.put("Viewport", sf);
            UIManager.put("TabbedPane.font", sf);
            UIManager.put("TableHeader.font", sf);
            UIManager.put("TextField.font", sf);
            UIManager.put("PasswordFiled.font", sf);
            UIManager.put("TextArea.font", sf);
            UIManager.put("TextPane.font", sf);
            UIManager.put("EditorPane.font", sf);
            UIManager.put("TitledBorder.font", sf);
            UIManager.put("ToolBar.font", sf);
            UIManager.put("ToolTip.font", sf);
            UIManager.put("Tree.font", sf);
        }
        setTitle("Test");
        setIconImage(readImageIcon("icon-32.png").getImage());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JComponent jp = buildContent();
        String showScrollbars = System.getProperty("scrollbars", "").toLowerCase();
        if (showScrollbars.equals("true")) {
            setContentPane(new JScrollPane(jp, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));
        } else if (showScrollbars.equals("optional")) {
            setContentPane(new JScrollPane(jp, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        } else {
            setContentPane(jp);
        }
        this.setTitle("PS3 Media Server " + PMS.getVersion() + " - FOR TESTING ONLY, POSSIBLY UNSTABLE");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Dimension screenSize = getToolkit().getScreenSize();
        if (screenSize.width < MINIMUM_SIZE.width || screenSize.height < MINIMUM_SIZE.height) {
            setMinimumSize(screenSize);
        } else {
            setMinimumSize(MINIMUM_SIZE);
        }
        if (screenSize.width < PREFERRED_SIZE.width || screenSize.height < PREFERRED_SIZE.height) {
            setSize(screenSize);
        } else {
            setSize(PREFERRED_SIZE);
        }
        setResizable(true);
        Dimension paneSize = getSize();
        setLocation(((screenSize.width > paneSize.width) ? ((screenSize.width - paneSize.width) / 2) : 0), ((screenSize.height > paneSize.height) ? ((screenSize.height - paneSize.height) / 2) : 0));
        if (!configuration.isMinimized() && System.getProperty(START_SERVICE) == null) {
            setVisible(true);
        }
        PMSUtil.addSystemTray(this);
    }

    protected static ImageIcon readImageIcon(String filename) {
        URL url = LooksFrame.class.getResource("/resources/images/" + filename);
        return new ImageIcon(url);
    }

    public JComponent buildContent() {
        JPanel panel = new JPanel(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.add(new JPanel());
        AbstractButton save = createToolBarButton(Messages.getString("LooksFrame.9"), "filesave-48.png", Messages.getString("LooksFrame.9"));
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PMS.get().save();
            }
        });
        toolBar.add(save);
        toolBar.addSeparator();
        reload = createToolBarButton(Messages.getString("LooksFrame.12"), "reload_page-48.png", Messages.getString("LooksFrame.12"));
        reload.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    PMS.get().reset();
                } catch (IOException e1) {
                    logger.error(null, e1);
                }
            }
        });
        toolBar.add(reload);
        toolBar.addSeparator();
        AbstractButton quit = createToolBarButton(Messages.getString("LooksFrame.5"), "exit-48.png", Messages.getString("LooksFrame.5"));
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        toolBar.add(quit);
        if (System.getProperty(START_SERVICE) != null) {
            quit.setEnabled(false);
        }
        toolBar.add(new JPanel());
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(buildMain(), BorderLayout.CENTER);
        status = new JLabel(" ");
        status.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(0, 5, 0, 5)));
        panel.add(status, BorderLayout.SOUTH);
        return panel;
    }

    public JComponent buildMain() {
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        st = new StatusTab();
        tt = new TracesTab();
        tr = new TranscodingTab(configuration);
        nt = new GeneralTab(configuration);
        ft = new NavigationShareTab(configuration);
        tabbedPane.addTab(Messages.getString("LooksFrame.18"), st.build());
        tabbedPane.addTab(Messages.getString("LooksFrame.19"), tt.build());
        tabbedPane.addTab(Messages.getString("LooksFrame.20"), nt.build());
        tabbedPane.addTab(Messages.getString("LooksFrame.22"), ft.build());
        tabbedPane.addTab(Messages.getString("LooksFrame.21"), tr.build());
        tabbedPane.addTab(Messages.getString("LooksFrame.24"), new HelpTab().build());
        tabbedPane.addTab(Messages.getString("LooksFrame.25"), new AboutTab().build());
        tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tabbedPane;
    }

    protected AbstractButton createToolBarButton(String text, String iconName, String toolTipText) {
        JButton button = new JButton(text, readImageIcon(iconName));
        button.setToolTipText(toolTipText);
        button.setFocusable(false);
        return button;
    }

    public void quit() {
        WindowsNamedPipe.loop = false;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        System.exit(0);
    }

    @Override
    public void append(String msg) {
        tt.getList().append(msg);
    }

    @Override
    public void setReadValue(long v, String msg) {
        st.setReadValue(v, msg);
    }

    @Override
    public void setStatusCode(int code, String msg, String icon) {
        st.getJl().setText(msg);
        try {
            st.getImagePanel().set(ImageIO.read(LooksFrame.class.getResourceAsStream("/resources/images/" + icon)));
        } catch (IOException e) {
            logger.error(null, e);
        }
    }

    @Override
    public void setValue(int v, String msg) {
        st.getJpb().setValue(v);
        st.getJpb().setString(msg);
    }

    /**
	 * This method is being called when a configuration change requiring
	 * a restart of the HTTP server has been done by the user. It should notify the user
	 * to restart the server.<br>
	 * Currently the icon as well as the tool tip text of the restart button is being 
	 * changed.<br>
	 * The actions requiring a server restart are defined by {@link PmsConfiguration#NEED_RELOAD_FLAGS}
	 * 
	 * @param b true if the server has to be restarted, false otherwise
	 */
    @Override
    public void setReloadable(boolean b) {
        if (b) {
            reload.setIcon(readImageIcon("reload_page_required-48.png"));
            reload.setToolTipText(Messages.getString("LooksFrame.13"));
        } else {
            reload.setIcon(readImageIcon("reload_page-48.png"));
            reload.setToolTipText(Messages.getString("LooksFrame.12"));
        }
    }

    @Override
    public void addEngines() {
        tr.addEngines();
    }

    public void update(Observable o, Object arg) {
        if (configuration.isAutoUpdate()) {
            checkForUpdates();
        }
    }

    public void checkForUpdates() {
        if (autoUpdater != null) {
            try {
                AutoUpdateDialog.showIfNecessary(this, autoUpdater);
            } catch (NoClassDefFoundError ncdf) {
                logger.info("Class not found: " + ncdf.getMessage());
            }
        }
    }

    public void setStatusLine(String line) {
        if (line == null) {
            line = " ";
        }
        status.setText(line);
    }

    @Override
    public void addRendererIcon(int code, String msg, String icon) {
        st.addRendererIcon(code, msg, icon);
    }

    @Override
    public void serverReady() {
        nt.addPlugins();
    }

    @Override
    public void setScanLibraryEnabled(boolean flag) {
        getFt().setScanLibraryEnabled(flag);
    }
}
