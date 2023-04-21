package ch.ideenarchitekten.vip.gui.prefeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ch.ideenarchitekten.vip.config.*;
import ch.ideenarchitekten.vip.config.Config.*;
import ch.ideenarchitekten.vip.gui.VipLayout;

/**
 * Graphischer Editor f�r das Bearbeiten der Applikationseinstellungen.
 * @author $LastChangedBy: jogeli $
 * @version $LastChangedRevision: 368 $
 */
public class PreferencesEditor extends JDialog implements PreferencesChangeListener {

    /**
	 * Eindeutige Version dieser Klasse (Serialisierung)
	 */
    private static final long serialVersionUID = 20070404L;

    /**
	 * Unter welchem Namen die Einstellungen f�r den Editor zu beziehen sind. 
	 */
    public static final String PREFSETTINGS = "prefsettings";

    /**
	 * Die Einstellungen auf der rechten Seite
	 */
    private PreferencesPage m_preferencesPage;

    /**
	 * Die Auswahl der Kategorien auf der linken Seite
	 */
    private CategoryPanel m_preferencesCategory;

    /**
	 * Der Restore Knopf, der die Standardeinstellungen l�dt.
	 */
    private JButton m_restoreButton;

    /**
	 * Der Save Knopf, der die Einstellungen speichert.
	 */
    private JButton m_saveButton;

    /**
	 * Der Ok Button speichert die Einstellung und verl�sst den Editor.
	 */
    private JButton m_okButton;

    /**
	 * Der Cancel Button verl�sst den Editor, ohne zu Speichern.
	 */
    private JButton m_cancelButton;

    /**
	 * Der JDialog muss das dahinterliegende Fenster verdecken. Sonst
	 * k�nnte der Dialog einfach in den Hintergrund, vor das dahinterstehende 
	 * Fenster, geschoben werden.
	 * @param owner Das Fenster vor das sich dieses stellt. 
	 */
    public PreferencesEditor(JFrame owner) {
        super(owner);
        setLayout(null);
        Config config = Config.getInstance();
        int windowHeight = Integer.parseInt(config.getConstant("preferencesEditorHeight"));
        int windowWidth = Integer.parseInt(config.getConstant("preferencesEditorWidth"));
        int x = Integer.parseInt(config.getConstant("preferencesEditorX"));
        int y = Integer.parseInt(config.getConstant("preferencesEditorY"));
        setBounds(x, y, windowWidth, windowHeight);
        Container c = getContentPane();
        c.setBackground(VipLayout.getBackgroundColor());
        setTitle(config.getLiteral("preferencesEditorTitle").getText());
        CategoryController controller = new CategoryController();
        m_preferencesCategory = new CategoryPanel(controller);
        m_preferencesPage = new PreferencesPage();
        m_preferencesPage.setBackground(VipLayout.getBackgroundColor());
        m_restoreButton = new JButton(config.getLiteral("preferencesEditorRestoreButton").getText());
        m_saveButton = new JButton(config.getLiteral("preferencesEditorSaveButton").getText());
        m_okButton = new JButton(config.getLiteral("preferencesEditorOkButton").getText());
        m_cancelButton = new JButton(config.getLiteral("preferencesEditorCancelButton").getText());
        setSizeChildViews();
        m_restoreButton.setBackground(VipLayout.getShadowColor());
        m_restoreButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                m_preferencesPage.resetPage();
            }
        });
        m_saveButton.setBackground(VipLayout.getShadowColor());
        m_saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_preferencesPage.saveCurrentPage();
            }
        });
        m_okButton.setBackground(VipLayout.getShadowColor());
        m_okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_preferencesPage.saveCurrentPage();
                saveCurrentWindowSettings();
                dispose();
            }
        });
        m_cancelButton.setBackground(VipLayout.getShadowColor());
        m_cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (m_preferencesPage.hasChanged()) {
                    Config c = Config.getInstance();
                    Literal text = c.getLiteral("preferencesEditorCancelWithoutSave");
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(PreferencesEditor.this, text.getText(), text.getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                        saveCurrentWindowSettings();
                        dispose();
                    }
                } else {
                    saveCurrentWindowSettings();
                    dispose();
                }
            }
        });
        PreferencesChangeController prefController = PreferencesChangeController.getInstance();
        prefController.addPreferencesChangeLister(this);
        controller.addCategoryListener(m_preferencesPage);
        prefController.addPreferencesChangeLister(m_preferencesPage);
        controller.addCategoryListener(m_preferencesCategory);
        prefController.addPreferencesChangeLister(m_preferencesCategory);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent arg0) {
                m_preferencesPage.saveCurrentPage();
                saveCurrentWindowSettings();
            }
        });
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent arg0) {
                setSizeChildViews();
            }
        });
        setSizeChildViews();
        add(m_preferencesCategory);
        add(m_preferencesPage);
        add(m_restoreButton);
        add(m_saveButton);
        add(m_okButton);
        add(m_cancelButton);
        setVisible(true);
        setSizeChildViews();
    }

    /**
	 * Speichert die �nderungen am Fenster (Gr�sse und Position)
	 */
    private void saveCurrentWindowSettings() {
        Config config = Config.getInstance();
        config.setConstant("preferencesEditorX", Integer.toString(getX()));
        config.setConstant("preferencesEditorY", Integer.toString(getY()));
        config.setConstant("preferencesEditorWidth", Integer.toString(getWidth()));
        config.setConstant("preferencesEditorHeight", Integer.toString(getHeight()));
    }

    /**
	 * Positioniert die Componenten, welche sich in diesem Panel befinden.
	 * Diese kann z.B. bei einer Gr�ssen�nderung des Containers verwendet werden.
	 */
    private void setSizeChildViews() {
        Config config = Config.getInstance();
        int buttonSpace = Integer.parseInt(config.getConstant("preferencesButtonSpace"));
        int borderSpace = Integer.parseInt(config.getConstant("lowerSpaceWindow"));
        int buttonHeight = Integer.parseInt(config.getConstant("preferencesButtonHeight"));
        int buttonWidth = Integer.parseInt(config.getConstant("preferencesButtonWidth"));
        int leftSide = Integer.parseInt(config.getConstant("preferencesEditorLeftWidth"));
        int windowHeight = getContentPane().getHeight();
        int windowWidth = getContentPane().getWidth();
        m_preferencesCategory.setBounds(0, 0, leftSide, windowHeight - borderSpace);
        m_preferencesPage.setBounds(leftSide, 0, windowWidth - leftSide - borderSpace, windowHeight - borderSpace - borderSpace - getInsets().top - getInsets().bottom);
        int buttonXPos = buttonSpace + leftSide;
        int buttonYPos = windowHeight - borderSpace - buttonSpace - buttonHeight;
        m_restoreButton.setBounds(buttonXPos, buttonYPos, buttonWidth, buttonHeight);
        buttonXPos += buttonSpace + buttonWidth;
        m_saveButton.setBounds(buttonXPos, buttonYPos, buttonWidth, buttonHeight);
        buttonXPos += buttonSpace + buttonSpace + buttonWidth;
        m_okButton.setBounds(buttonXPos, buttonYPos, buttonWidth, buttonHeight);
        buttonXPos += buttonSpace + buttonWidth;
        m_cancelButton.setBounds(buttonXPos, buttonYPos, buttonWidth, buttonHeight);
    }

    /**
	 * Anpassen der Beschriftung 
	 */
    public void preferencesChange() {
        Config config = Config.getInstance();
        setTitle(config.getLiteral("preferencesEditorTitle").getText());
        m_restoreButton.setText(config.getLiteral("preferencesEditorRestoreButton").getText());
        m_saveButton.setText(config.getLiteral("preferencesEditorSaveButton").getText());
        m_okButton.setText(config.getLiteral("preferencesEditorOkButton").getText());
        m_cancelButton.setText(config.getLiteral("preferencesEditorCancelButton").getText());
    }
}
