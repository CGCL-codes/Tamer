package cz.hdf.cdnavigator.install;

import cz.hdf.cdnavigator.Config;
import cz.hdf.cdnavigator.ConfigFile_v3;
import cz.hdf.exceptions.XMLException;
import cz.hdf.gui.GUI;
import cz.hdf.gui.HCheckBox;
import cz.hdf.gui.HLabel;
import cz.hdf.gui.HLabelComponent;
import cz.hdf.gui.HMessageDialog;
import cz.hdf.gui.HPanel;
import cz.hdf.gui.HRadioButton;
import cz.hdf.i18n.I18N;
import cz.hdf.install.Page;
import cz.hdf.log.LogManager;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Page for configuring CD cache panel.
 *
 * @author hunter
 */
public class CachePage extends Page {

    /** page label */
    private String label;

    /** page panel */
    private HPanel page;

    /** cache directory field */
    protected JTextField cacheDirField;

    /** field browse button */
    protected JButton cacheDirButton;

    /** use automatic cache */
    protected HCheckBox cacheAutoCheck;

    /** use whole disk for caching */
    protected HRadioButton cacheFullRadio;

    /** use maximum cache size for caching */
    protected HRadioButton cacheSizeRadio;

    /** maximum cache size field */
    protected JTextField cacheSizeField;

    /** maximum cache size label (MB) */
    protected HLabel cacheSizeLabel;

    /** cache photos? */
    protected HCheckBox cachePhotosCheck;

    /** cache music? */
    protected HCheckBox cacheMusicCheck;

    /** cache movies? */
    protected HCheckBox cacheMoviesCheck;

    /** Logger. Hierarchy is set to name of this class. */
    Logger logger = Logger.getLogger(this.getClass().getName());

    /**
   * Inititalize page content and installer buttons.
   */
    public CachePage(Frame _frame, String _identifier) {
        frame = _frame;
        identifier = _identifier;
        label = "Cache";
        initLayout();
        loadConfig();
    }

    /**
   * In this method must be implemented layouting components. This is usefull
   * when extended child want changed layouting.
   */
    public void initLayout() {
        page = new HPanel();
        page.setLayout(new BorderLayout());
        HPanel cacheDirPanel = new HPanel();
        cacheDirPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        cacheDirPanel.add(new HLabel("cache directory"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, GUI.COMPONENT_HGAP, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        cacheDirField = new JTextField();
        cacheDirPanel.add(cacheDirField, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        cacheDirButton = new JButton(Config.getIcon(Config.ICON_TYPE_FILE_OPEN, Config.ICON_SIZE_16));
        cacheDirPanel.add(cacheDirButton, gbc);
        cacheDirButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser dirChooser = new JFileChooser();
                dirChooser.setDialogTitle(I18N.translate("Choose directory"));
                dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                dirChooser.setSelectedFile(new File(cacheDirField.getText()));
                int retValChooser = dirChooser.showOpenDialog(page);
                if (retValChooser == JFileChooser.APPROVE_OPTION) {
                    File file = dirChooser.getSelectedFile();
                    logger.finer("File chooser return file " + file.getName() + ".");
                    cacheDirField.setText(file.getAbsolutePath());
                } else {
                    logger.finer("File chooser return nothing - Cancel button.");
                }
            }
        });
        HPanel cacheAutoPanel = new HPanel();
        cacheAutoPanel.setLayout(new BoxLayout(cacheAutoPanel, BoxLayout.Y_AXIS));
        cacheAutoPanel.setOpaque(false);
        cacheAutoCheck = new HCheckBox("automatic cache");
        cacheAutoCheck.setSelected(new Boolean(Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_AUTOMATIC)).booleanValue());
        cacheAutoCheck.setAlignmentX(0.0f);
        cacheAutoCheck.setOpaque(false);
        cacheAutoPanel.add(cacheAutoCheck);
        ButtonGroup radioGroup = new ButtonGroup();
        cacheFullRadio = new HRadioButton("fill cache while there is still enough space");
        radioGroup.add(cacheFullRadio);
        cacheFullRadio.setAlignmentX(0.0f);
        cacheFullRadio.setOpaque(false);
        cacheAutoPanel.add(cacheFullRadio);
        cacheSizeRadio = new HRadioButton("maximum cache size");
        radioGroup.add(cacheSizeRadio);
        cacheSizeRadio.setOpaque(false);
        cacheSizeField = new JTextField();
        int size = 0;
        try {
            size = new Integer(Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_SIZE)).intValue();
            cacheSizeField.setText(Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_SIZE));
        } catch (NumberFormatException e) {
        }
        if (size > 0) {
            cacheSizeRadio.setSelected(true);
            cacheSizeField.setEnabled(true);
        } else {
            cacheFullRadio.setSelected(true);
            cacheSizeField.setEnabled(false);
        }
        HPanel cacheSizePanel = new HPanel();
        cacheSizePanel.setLayout(new GridBagLayout());
        cacheSizePanel.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(GUI.COMPONENT_VGAP, 0, 0, 0);
        cacheSizePanel.add(cacheSizeRadio, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.insets = new Insets(GUI.COMPONENT_VGAP, GUI.COMPONENT_HGAP, 0, 0);
        cacheSizePanel.add(cacheSizeField, gbc);
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        cacheSizeLabel = new HLabel("MB");
        cacheSizePanel.add(cacheSizeLabel, gbc);
        cacheSizePanel.setAlignmentX(0.0f);
        cacheAutoPanel.add(cacheSizePanel);
        cacheAutoPanel.add(Box.createVerticalStrut(GUI.PANEL_VGAP));
        cachePhotosCheck = new HCheckBox("use cache for photos");
        cachePhotosCheck.setAlignmentX(0.0f);
        cachePhotosCheck.setOpaque(false);
        cachePhotosCheck.setSelected(new Boolean(Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_PHOTOS)).booleanValue());
        cacheAutoPanel.add(cachePhotosCheck);
        cacheMusicCheck = new HCheckBox("use cache for music");
        cacheMusicCheck.setAlignmentX(0.0f);
        cacheMusicCheck.setOpaque(false);
        cacheMusicCheck.setSelected(new Boolean(Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_MUSIC)).booleanValue());
        cacheAutoPanel.add(cacheMusicCheck);
        cacheMoviesCheck = new HCheckBox("use cache for movies");
        cacheMoviesCheck.setAlignmentX(0.0f);
        cacheMoviesCheck.setOpaque(false);
        cacheMoviesCheck.setSelected(new Boolean(Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_MOVIES)).booleanValue());
        cacheAutoPanel.add(cacheMoviesCheck);
        JPanel cacheInnerPanel = new JPanel();
        cacheInnerPanel.setLayout(new BoxLayout(cacheInnerPanel, BoxLayout.Y_AXIS));
        cacheDirPanel.setAlignmentX(0.0f);
        cacheAutoPanel.setAlignmentX(0.0f);
        cacheInnerPanel.add(cacheDirPanel);
        cacheInnerPanel.add(Box.createVerticalStrut(GUI.PANEL_VGAP));
        cacheInnerPanel.add(cacheAutoPanel);
        JPanel cachePanel = new HLabelComponent("Cache", cacheInnerPanel, true);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        cachePanel.setAlignmentX(0.0f);
        topPanel.add(cachePanel);
        page.add(topPanel, BorderLayout.NORTH);
        page.add(new JPanel(), BorderLayout.CENTER);
        Dimension dim = page.getPreferredSize();
        if (dim.width < 400) {
            dim.width = 400;
        }
        page.setPreferredSize(dim);
        cacheAutoCheck.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_AUTOMATIC, new Boolean(cacheAutoCheck.isSelected()).toString());
                if (cacheAutoCheck.isSelected()) {
                    cacheFullRadio.setEnabled(true);
                    cacheSizeRadio.setEnabled(true);
                    if (cacheSizeRadio.isSelected()) {
                        cacheSizeField.setEnabled(true);
                    }
                    cacheSizeLabel.setEnabled(true);
                    cachePhotosCheck.setEnabled(true);
                    cacheMusicCheck.setEnabled(true);
                    cacheMoviesCheck.setEnabled(true);
                } else {
                    cacheFullRadio.setEnabled(false);
                    cacheSizeRadio.setEnabled(false);
                    cacheSizeField.setEnabled(false);
                    cacheSizeLabel.setEnabled(false);
                    cachePhotosCheck.setEnabled(false);
                    cacheMusicCheck.setEnabled(false);
                    cacheMoviesCheck.setEnabled(false);
                }
            }
        });
        cacheSizeRadio.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (cacheSizeRadio.isSelected()) {
                    cacheSizeField.setEnabled(true);
                } else {
                    cacheSizeField.setEnabled(false);
                }
            }
        });
    }

    /**
   * Check if size field is a number.
   *
   * @return
   */
    private int checkSizeField() {
        if (!cacheSizeField.isEnabled()) {
            return 0;
        }
        try {
            new Integer(cacheSizeField.getText()).intValue();
            Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_SIZE, cacheSizeField.getText());
            return 0;
        } catch (NumberFormatException ee) {
            return -1;
        }
    }

    /**
   * Load configuration into GUI objects.
   */
    public void loadConfig() {
        if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_DIRECTORY) == null) {
            cacheDirField.setText("/opt/CDnavigator/cache");
        } else {
            cacheDirField.setText(Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_DIRECTORY));
        }
        if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_AUTOMATIC) == null) {
            cacheAutoCheck.setSelected(true);
        } else {
            if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_AUTOMATIC).equalsIgnoreCase("true")) {
                cacheAutoCheck.setSelected(true);
            } else {
                cacheAutoCheck.setSelected(false);
            }
        }
        if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_PHOTOS) == null) {
            cachePhotosCheck.setSelected(true);
        } else {
            if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_PHOTOS).equalsIgnoreCase("true")) {
                cachePhotosCheck.setSelected(true);
            } else {
                cachePhotosCheck.setSelected(false);
            }
        }
        if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_MUSIC) == null) {
            cacheMusicCheck.setSelected(true);
        } else {
            if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_MUSIC).equalsIgnoreCase("true")) {
                cacheMusicCheck.setSelected(true);
            } else {
                cacheMusicCheck.setSelected(false);
            }
        }
        if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_MOVIES) == null) {
            cacheMoviesCheck.setSelected(false);
        } else {
            if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_MOVIES).equalsIgnoreCase("true")) {
                cacheMoviesCheck.setSelected(true);
            } else {
                cacheMoviesCheck.setSelected(false);
            }
        }
        if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_SIZE) == null) {
            cacheSizeField.setText("1000");
            cacheSizeField.setEnabled(false);
            cacheSizeRadio.setSelected(true);
        } else {
            if (Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_SIZE).equals("0")) {
                cacheSizeField.setText("0");
                cacheSizeField.setEnabled(false);
                cacheSizeRadio.setSelected(false);
            } else {
                cacheSizeField.setEnabled(true);
                cacheSizeField.setText(Config.getProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_SIZE));
                cacheSizeRadio.setSelected(true);
            }
        }
    }

    /**
   * Get panel with page content.
   *
   * @return <code>JPanel</code>
   */
    public JPanel getPage() {
        return page;
    }

    /**
   * Get not localized page label.
   *
   * @return string label
   */
    public String getLabel() {
        return label;
    }

    /**
   * This method is called after selecting and painting page.
   */
    public void startPage() {
    }

    /**
   * Discard all changes/values and set original values.
   */
    public void discardChanges() {
    }

    /**
   * Check data and return error code.
   *
   * @return 0 all is OK; -3 isoinfo, -4 cache size problem; , -5 umount
   */
    public int processPage() {
        File file = new File(cacheDirField.getText());
        if (!file.isDirectory()) {
            return -3;
        }
        if (checkSizeField() != 0) {
            return -4;
        }
        Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_DIRECTORY, cacheDirField.getText());
        Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_AUTOMATIC, new Boolean(cacheAutoCheck.isSelected()).toString());
        Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_PHOTOS, new Boolean(cachePhotosCheck.isSelected()).toString());
        Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_MUSIC, new Boolean(cacheMusicCheck.isSelected()).toString());
        Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_MOVIES, new Boolean(cacheMoviesCheck.isSelected()).toString());
        if (cacheSizeRadio.isSelected()) {
            cacheSizeField.setEnabled(true);
            Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_SIZE, cacheSizeField.getText());
        } else {
            cacheSizeField.setEnabled(false);
            Config.setProperty(ConfigFile_v3.NODE_CACHE, ConfigFile_v3.NODE_CACHE_SIZE, "0");
        }
        return 0;
    }

    /**
   * Nothing to do. No errors generated.
   *
   * @param _errorCode error code specifying error
   *
   * @see #processPage()
   */
    public void showErrorDialog(int _errorCode) {
        HMessageDialog dlg;
        switch(_errorCode) {
            case -3:
                dlg = new HMessageDialog(frame, HMessageDialog.MESSAGE_QUESTION_WARNING, I18N.translate("Specified {0} directory '{1}' is not an existing directory.", new Object[] { "cache", cacheDirField.getText() }) + "\n\n" + I18N.translate("Shall I create this folder?"));
                if (dlg.getResult() == HMessageDialog.RESULT_YES) {
                    createFolder(cacheDirField.getText());
                }
                break;
            case -4:
                new HMessageDialog(frame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Size field contains bad number."));
                break;
        }
    }

    private void createFolder(String _folderPath) {
        try {
            File folder = new File(_folderPath);
            if (!folder.mkdirs()) {
                new HMessageDialog(frame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't create folder '{0}'.", new Object[] { _folderPath }));
            }
        } catch (Exception exc) {
            new HMessageDialog(frame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't create folder '{0}'.", new Object[] { _folderPath }) + "\n\n" + exc.getMessage());
        }
    }

    /**
   * Relocalize all components.
   */
    public void relocalize() {
        page.relocalize();
    }

    /**
   * Testing ...
   *
   * @param args
   */
    public static void main(String[] args) {
        new LogManager(Level.FINE);
        try {
            Config.loadFromUserFile();
        } catch (XMLException exc) {
            exc.printStackTrace();
        }
        final JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        CachePage cachePage = new CachePage(frame, "Cache page");
        pane.add(cachePage.getPage(), BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
