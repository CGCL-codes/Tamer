package acide.gui.menuBar.configurationMenu.fileEditor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import acide.configuration.workbench.AcideWorkbenchConfiguration;
import acide.gui.listeners.AcideWindowClosingListener;
import acide.gui.mainWindow.AcideMainWindow;
import acide.language.AcideLanguageManager;
import acide.log.AcideLog;
import acide.utils.AcidePreviewPanel;

/**
 * ACIDE - A Configurable IDE file editor display options window.
 * 
 * @version 0.8
 * @see JFrame
 */
public class AcideFileEditorDisplayOptionsWindow extends JFrame {

    /**
	 * ACIDE - A Configurable IDE file editor display options window class
	 * serial version UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * ACIDE - A Configurable IDE file editor display options window image icon.
	 */
    private static final ImageIcon ICON = new ImageIcon("./resources/images/icon.png");

    /**
	 * ACIDE - A Configurable IDE file editor display options window menu item
	 * image icon.
	 */
    private static final ImageIcon COLOR_PALETTE_IMAGE = new ImageIcon("./resources/icons/buttons/colorPalette.png");

    /**
	 * Current font size of the file editor in the main window.
	 */
    private int _initialSize = 12;

    /**
	 * Current font style of the output in the main window.
	 */
    private int _initialStyle = Font.PLAIN;

    /**
	 * Current font name of the output in the main window.
	 */
    private String _initialFontName = "Monospaced";

    /**
	 * Current foreground color of the output in the main window.
	 */
    private Color _initialForegroundColor = Color.BLACK;

    /**
	 * Current background color of the output in the main window.
	 */
    private Color _initialBackgroundColor = Color.WHITE;

    /**
	 * Where the sample text is displayed.
	 */
    private AcidePreviewPanel _displayArea;

    /**
	 * ACIDE - A Configurable IDE file editor display options window preview
	 * panel which contains the display area.
	 */
    private JPanel _previewPanel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window font size
	 * combo box.
	 */
    private JComboBox _fontSizeComboBox;

    /**
	 * ACIDE - A Configurable IDE file editor display options window font name
	 * combo box.
	 */
    private JComboBox _fontNameComboBox;

    /**
	 * ACIDE - A Configurable IDE file editor display options window controls
	 * panel.
	 */
    private JPanel _controlsPanel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window color
	 * buttons panel.
	 */
    private JPanel _colorButtonsPanel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window button
	 * panel.
	 */
    private JPanel _buttonPanel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window accept
	 * button.
	 */
    private JButton _acceptButton;

    /**
	 * ACIDE - A Configurable IDE file editor display options window cancel
	 * button.
	 */
    private JButton _cancelButton;

    /**
	 * ACIDE - A Configurable IDE file editor display options window font name
	 * label.
	 */
    private JLabel _fontNameLabel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window background
	 * color label.
	 */
    private JLabel _backgroundColorLabel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window foreground
	 * color label.
	 */
    private JLabel _foregroundColorLabel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window font size
	 * label.
	 */
    private JLabel _fontSizeLabel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window font type
	 * label.
	 */
    private JLabel _fontStyleLabel;

    /**
	 * ACIDE - A Configurable IDE file editor display options window font style
	 * combo box.
	 */
    private JComboBox _fontStyleComboBox;

    /**
	 * ACIDE - A Configurable IDE file editor display options window foreground
	 * color button.
	 */
    private JButton _foregroundColorButton;

    /**
	 * ACIDE - A Configurable IDE file editor display options window background
	 * color button.
	 */
    private JButton _backgroundColorButton;

    /**
	 * ACIDE - A Configurable IDE console display options window restore default
	 * configuration.
	 */
    private JButton _restoreDefaultConfiguration;

    /**
	 * Creates a new ACIDE - A Configurable IDE file editor display options
	 * window.
	 */
    public AcideFileEditorDisplayOptionsWindow() {
        super();
        if (AcideMainWindow.getInstance().getFileEditorManager().getNumberOfFileEditorPanels() > 0) {
            _initialSize = AcideMainWindow.getInstance().getFileEditorManager().getSelectedFileEditorPanel().getActiveTextEditionArea().getFont().getSize();
            _initialStyle = AcideMainWindow.getInstance().getFileEditorManager().getSelectedFileEditorPanel().getActiveTextEditionArea().getFont().getStyle();
            _initialFontName = AcideMainWindow.getInstance().getFileEditorManager().getSelectedFileEditorPanel().getActiveTextEditionArea().getFont().getFamily();
            _initialForegroundColor = AcideMainWindow.getInstance().getFileEditorManager().getSelectedFileEditorPanel().getActiveTextEditionArea().getForeground();
            _initialBackgroundColor = AcideMainWindow.getInstance().getFileEditorManager().getSelectedFileEditorPanel().getActiveTextEditionArea().getBackground();
        }
        buildComponents();
        setListeners();
        addComponents();
        setWindowConfiguration();
        AcideLog.getLog().info(AcideLanguageManager.getInstance().getLabels().getString("s1042"));
    }

    /**
	 * Sets the ACIDE - A Configurable IDE file editor display options window
	 * configuration.
	 */
    private void setWindowConfiguration() {
        setTitle(AcideLanguageManager.getInstance().getLabels().getString("s1041"));
        setIconImage(ICON.getImage());
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        AcideMainWindow.getInstance().setEnabled(false);
    }

    /**
	 * Adds the components to the ACIDE - A Configurable IDE file editor display
	 * options window with the layout.
	 */
    private void addComponents() {
        setLayout(new BorderLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        _controlsPanel.add(_fontNameLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        _controlsPanel.add(_fontNameComboBox, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        _controlsPanel.add(_fontSizeLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        _controlsPanel.add(_fontSizeComboBox, constraints);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 2;
        _controlsPanel.add(_fontStyleLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        _controlsPanel.add(_fontStyleComboBox, constraints);
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        _colorButtonsPanel.add(_foregroundColorLabel, constraints);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        constraints.gridy = 0;
        _colorButtonsPanel.add(_foregroundColorButton, constraints);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 1;
        _colorButtonsPanel.add(_backgroundColorLabel, constraints);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        constraints.gridy = 1;
        _colorButtonsPanel.add(_backgroundColorButton, constraints);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 4;
        _controlsPanel.add(_colorButtonsPanel, constraints);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 5;
        _controlsPanel.add(_restoreDefaultConfiguration, constraints);
        add(_controlsPanel, BorderLayout.NORTH);
        add(_previewPanel, BorderLayout.CENTER);
        add(_buttonPanel, BorderLayout.SOUTH);
    }

    /**
	 * Builds the ACIDE - A Configurable IDE file editor display options window
	 * components.
	 */
    private void buildComponents() {
        _controlsPanel = new JPanel(new GridBagLayout());
        _controlsPanel.setBorder(BorderFactory.createTitledBorder(AcideLanguageManager.getInstance().getLabels().getString("s1010")));
        _colorButtonsPanel = new JPanel(new GridBagLayout());
        _fontNameLabel = new JLabel(AcideLanguageManager.getInstance().getLabels().getString("s981"));
        getFontNameComboBox();
        _fontStyleLabel = new JLabel(AcideLanguageManager.getInstance().getLabels().getString("s983"), JLabel.CENTER);
        getFontStyleComboBox();
        _fontSizeLabel = new JLabel(AcideLanguageManager.getInstance().getLabels().getString("s982"));
        createFontSizeComboBox();
        _foregroundColorLabel = new JLabel(AcideLanguageManager.getInstance().getLabels().getString("s984"));
        _foregroundColorButton = new JButton(COLOR_PALETTE_IMAGE);
        _backgroundColorLabel = new JLabel(AcideLanguageManager.getInstance().getLabels().getString("s985"));
        _backgroundColorButton = new JButton(COLOR_PALETTE_IMAGE);
        _previewPanel = new JPanel();
        _previewPanel.setBorder(BorderFactory.createTitledBorder(AcideLanguageManager.getInstance().getLabels().getString("s1011")));
        _displayArea = new AcidePreviewPanel(_initialFontName, _initialStyle, _initialSize, _initialForegroundColor, _initialBackgroundColor);
        _previewPanel.add(_displayArea);
        _restoreDefaultConfiguration = new JButton(AcideLanguageManager.getInstance().getLabels().getString("s1095"));
        buildButtonPanel();
    }

    /**
	 * Builds the ACIDE - A Configurable IDE file editor display options window
	 * button panel.
	 */
    private void buildButtonPanel() {
        _buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        _acceptButton = new JButton(AcideLanguageManager.getInstance().getLabels().getString("s445"));
        _buttonPanel.add(_acceptButton);
        _cancelButton = new JButton(AcideLanguageManager.getInstance().getLabels().getString("s446"));
        _buttonPanel.add(_cancelButton);
    }

    /**
	 * Sets the listeners of the ACIDE - A Configurable IDE file editor display
	 * options window components.
	 */
    public void setListeners() {
        _foregroundColorButton.addActionListener(new ForegroundColorButtonAction());
        _backgroundColorButton.addActionListener(new BackgroundColorButtonAction());
        _acceptButton.addActionListener(new AcceptButtonAction());
        _cancelButton.addActionListener(new CancelButtonAction());
        _fontStyleComboBox.addActionListener(new FontStyleComboBoxAction());
        _fontNameComboBox.addActionListener(new FontComboBoxAction());
        _fontSizeComboBox.addActionListener(new FontSizeComboBoxListener());
        _restoreDefaultConfiguration.addActionListener(new RestoreDefaultConfigurationButtonAction());
        addWindowListener(new AcideWindowClosingListener());
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "EscapeKey");
        getRootPane().getActionMap().put("EscapeKey", new EscapeKeyAction());
    }

    /**
	 * Creates the font style combo box.
	 */
    public void getFontStyleComboBox() {
        _fontStyleComboBox = new JComboBox();
        _fontStyleComboBox.addItem(AcideLanguageManager.getInstance().getLabels().getString("s413"));
        _fontStyleComboBox.addItem(AcideLanguageManager.getInstance().getLabels().getString("s414"));
        _fontStyleComboBox.addItem(AcideLanguageManager.getInstance().getLabels().getString("s415"));
        _fontStyleComboBox.addItem(AcideLanguageManager.getInstance().getLabels().getString("s416"));
        _fontStyleComboBox.setToolTipText(AcideLanguageManager.getInstance().getLabels().getString("s400"));
        _fontStyleComboBox.setSelectedItem(_initialStyle);
        _fontStyleComboBox.setEnabled(true);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                switch(_initialStyle) {
                    case Font.PLAIN:
                        _fontStyleComboBox.setSelectedItem(AcideLanguageManager.getInstance().getLabels().getString("s413"));
                        break;
                    case Font.ITALIC:
                        _fontStyleComboBox.setSelectedItem(AcideLanguageManager.getInstance().getLabels().getString("s414"));
                        break;
                    case Font.BOLD:
                        _fontStyleComboBox.setSelectedItem(AcideLanguageManager.getInstance().getLabels().getString("s415"));
                        break;
                    case Font.BOLD + Font.ITALIC:
                        _fontStyleComboBox.setSelectedItem(AcideLanguageManager.getInstance().getLabels().getString("s416"));
                        break;
                }
            }
        });
    }

    /**
	 * Creates and configures the font size combo box.
	 */
    public void createFontSizeComboBox() {
        String[] values = { "8", "9", "10", "11", "12", "14", "16", "20", "24", "32", "48", "72" };
        _fontSizeComboBox = new JComboBox(values);
        _fontSizeComboBox.setEditable(true);
        _fontSizeComboBox.setSelectedItem(String.valueOf(_initialSize));
    }

    /**
	 * Creates and configures the font name combo box.
	 */
    public void getFontNameComboBox() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Vector<String> availableFonts = new Vector<String>(fontNames.length);
        for (String fontName : fontNames) {
            Font font = new Font(fontName, Font.PLAIN, 12);
            if (font.canDisplay('a')) availableFonts.add(fontName);
        }
        _fontNameComboBox = new JComboBox(availableFonts);
        _fontNameComboBox.setSelectedItem(_initialFontName);
    }

    /**
	 * Closes the ACIDE - A Configurable IDE file editor display options window.
	 */
    private void closeWindow() {
        AcideMainWindow.getInstance().setEnabled(true);
        dispose();
        AcideMainWindow.getInstance().setAlwaysOnTop(true);
        AcideMainWindow.getInstance().setAlwaysOnTop(false);
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window foreground
	 * color button action listener.
	 * 
	 * @version 0.8
	 * @see ActionListener
	 */
    class ForegroundColorButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Color foregroundColor = JColorChooser.showDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s992"), _initialForegroundColor);
            if (foregroundColor != null) _displayArea.setForegroundColor(foregroundColor);
        }
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window background
	 * color button action listener.
	 * 
	 * @version 0.8
	 * @see ActionListener
	 */
    class BackgroundColorButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Color backgroundColor = JColorChooser.showDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s991"), _initialBackgroundColor);
            if (backgroundColor != null) _displayArea.setBackgroundColor(backgroundColor);
        }
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window accept
	 * button action listener.
	 * 
	 * @version 0.8
	 * @see ActionListener
	 */
    class AcceptButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            AcideLog.getLog().info("1043");
            for (int index = 0; index < AcideMainWindow.getInstance().getFileEditorManager().getNumberOfFileEditorPanels(); index++) {
                AcideWorkbenchConfiguration.getInstance().getFileEditorConfiguration().setFontName(_displayArea.getFontName());
                AcideWorkbenchConfiguration.getInstance().getFileEditorConfiguration().setFontStyle(_displayArea.getFontStyle());
                AcideWorkbenchConfiguration.getInstance().getFileEditorConfiguration().setFontSize(_displayArea.getFontSize());
                AcideWorkbenchConfiguration.getInstance().getFileEditorConfiguration().setBackgroundColor(_displayArea.getBackground());
                AcideWorkbenchConfiguration.getInstance().getFileEditorConfiguration().setForegroundColor(_displayArea.getForeground());
                AcideMainWindow.getInstance().getFileEditorManager().getFileEditorPanelAt(index).setLookAndFeel();
                AcideMainWindow.getInstance().getFileEditorManager().getSelectedFileEditorPanel().resetStyledDocument();
            }
            closeWindow();
        }
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window cancel
	 * button action listener.
	 * 
	 * @version 0.8
	 * @see ActionListener
	 */
    class CancelButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            AcideLog.getLog().info("1044");
            closeWindow();
        }
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window escape key
	 * action.
	 * 
	 * @version 0.8
	 * @see AbstractAction
	 */
    class EscapeKeyAction extends AbstractAction {

        /**
		 * Escape key action serial version UID.
		 */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            closeWindow();
        }
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window font style
	 * combo box action listener.
	 * 
	 * @version 0.8
	 * @see ActionListener
	 */
    class FontStyleComboBoxAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String selectedItem = (String) _fontStyleComboBox.getSelectedItem();
            if (selectedItem.equals(AcideLanguageManager.getInstance().getLabels().getString("s413"))) _displayArea.setFontStyle(Font.PLAIN); else if (selectedItem.equals(AcideLanguageManager.getInstance().getLabels().getString("s414"))) _displayArea.setFontStyle(Font.ITALIC); else if (selectedItem.equals(AcideLanguageManager.getInstance().getLabels().getString("s415"))) _displayArea.setFontStyle(Font.BOLD); else if (selectedItem.equals(AcideLanguageManager.getInstance().getLabels().getString("s416"))) _displayArea.setFontStyle(Font.BOLD + Font.ITALIC);
        }
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window font size
	 * combo box action listener.
	 * 
	 * @version 0.8
	 * @see ActionListener
	 */
    class FontSizeComboBoxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                int newValue = Integer.parseInt((String) _fontSizeComboBox.getSelectedItem());
                if (newValue > 0) _displayArea.setFontSize(newValue); else JOptionPane.showMessageDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s2003"), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s2003"), "Error", JOptionPane.ERROR_MESSAGE);
                AcideLog.getLog().info(exception.getMessage());
            }
        }
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window font combo
	 * box action listener.
	 * 
	 * @version 0.8
	 * @see ActionListener
	 */
    class FontComboBoxAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            _displayArea.setFontName((String) _fontNameComboBox.getSelectedItem());
        }
    }

    /**
	 * ACIDE - A Configurable IDE file editor display options window restore
	 * default configuration action listener.
	 * 
	 * @version 0.8
	 * @see ActionListener
	 */
    class RestoreDefaultConfigurationButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            _displayArea.setFontName("monospaced");
            _displayArea.setFontStyle(Font.PLAIN);
            _displayArea.setFontSize(12);
            _displayArea.setBackground(Color.WHITE);
            _displayArea.setForeground(Color.BLACK);
        }
    }
}
