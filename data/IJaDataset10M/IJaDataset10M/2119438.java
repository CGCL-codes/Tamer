package calclipse.caldron.gui.script.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import calclipse.Resource;
import calclipse.caldron.ExceptionHandlers;
import calclipse.caldron.data.DataItem;
import calclipse.caldron.data.DataProject;
import calclipse.caldron.data.DataProjectListener;
import calclipse.caldron.gui.GUIUtil;
import calclipse.caldron.gui.script.ScriptItem;
import calclipse.caldron.gui.script.actions.EditAction;
import calclipse.caldron.gui.script.actions.ExecutableAction;
import calclipse.caldron.gui.script.actions.FileAction;
import calclipse.caldron.gui.script.actions.RedoAction;
import calclipse.caldron.gui.script.actions.UndoAction;
import calclipse.caldron.search.SearchHit;
import calclipse.caldron.search.TextSearch;
import calclipse.core.gui.GUI;
import calclipse.core.gui.Win;
import calclipse.core.gui.WinVetoException;

/**
 * The default script editor.
 * @author T. Sommerland
 */
public class DefaultScriptEditor extends ScriptContainer implements DataProjectListener, DocumentListener, ActionListener {

    private static final int NAME_FIELD_COLUMNS = 15;

    private static final Dimension PREFERRED_SIZE = new Dimension(200, 200);

    private static final String DEFAULT_URL_PREFIX = "script.item://";

    private static long editorNumber;

    private final ScriptItem item;

    private final Container contentPane = new JPanel(new BorderLayout());

    private final JToolBar toolBar = new JToolBar();

    private final JMenuBar menuBar = new JMenuBar();

    private final JLabel nameLabel = new JLabel("Name");

    private final JLabel urlLabel = new JLabel("URL");

    private final JTextField nameField = new JTextField(NAME_FIELD_COLUMNS);

    private final JTextField urlField = new JTextField(DEFAULT_URL_PREFIX + (++editorNumber));

    private final JToggleButton executableButton = new JCheckBox(new ExecutableAction());

    public DefaultScriptEditor(final ScriptItem item, final JTextComponent scriptPane) {
        super(scriptPane);
        this.item = item;
        nameLabel.setLabelFor(nameField);
        urlLabel.setLabelFor(urlField);
        contentPane.setPreferredSize(PREFERRED_SIZE);
        nameField.setText(DefaultScriptName.getName());
        executableButton.setSelected(true);
        scriptPane.getDocument().addDocumentListener(this);
        nameField.getDocument().addDocumentListener(this);
        nameField.addActionListener(this);
        executableButton.addActionListener(this);
        DataProject.PROJECT.addDataProjectListener(this);
        initContentPane();
        initToolBar();
        initMenuBar();
        GUIUtil.localize(this);
    }

    private void initContentPane() {
        final JScrollPane scrollPane = new JScrollPane(scriptPane);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        final JPanel urlPane = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        urlPane.add(urlLabel, gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 2;
        urlPane.add(urlField, gbc);
        contentPane.add(urlPane, BorderLayout.SOUTH);
    }

    private void initToolBar() {
        toolBar.add(executableButton);
        toolBar.addSeparator();
        toolBar.add(nameLabel);
        toolBar.add(nameField);
        toolBar.addSeparator();
        toolBar.add(item.getScriptActions().getExecuteScriptAction());
    }

    private void initMenuBar() {
        final JMenu fileMenu = new JMenu(new FileAction());
        fileMenu.add(item.getScriptActions().getLoadScriptAction());
        fileMenu.add(item.getScriptActions().getSaveScriptAction());
        menuBar.add(fileMenu);
        final JMenu editMenu = new JMenu(new EditAction());
        final JMenuItem undoItem = editMenu.add(new UndoAction());
        final JMenuItem redoItem = editMenu.add(new RedoAction());
        menuBar.add(editMenu);
        ScriptUndoRedoSupport.enable(scriptPane, undoItem, redoItem);
    }

    @Resource("calclipse.caldron.gui.script.editor.DefaultScriptEditor.nameLabelText")
    public void setNameLabelText(final String text) {
        nameLabel.setText(text);
    }

    @Resource("calclipse.caldron.gui.script.editor.DefaultScriptEditor.nameLabelIcon")
    public void setNameLabelIcon(final Icon icon) {
        nameLabel.setIcon(icon);
    }

    @Resource("calclipse.caldron.gui.script.editor.DefaultScriptEditor.nameLabelMnemonic")
    public void setNameLabelMnemonic(final String keyStroke) {
        if (keyStroke == null) {
            nameLabel.setDisplayedMnemonic(0);
        } else {
            final KeyStroke stroke = GUIUtil.getKeyStroke(keyStroke);
            nameLabel.setDisplayedMnemonic(stroke.getKeyChar());
        }
    }

    @Resource("calclipse.caldron.gui.script.editor.DefaultScriptEditor.urlLabelText")
    public void setURLLabelText(final String text) {
        urlLabel.setText(text);
    }

    @Resource("calclipse.caldron.gui.script.editor.DefaultScriptEditor.urlLabelIcon")
    public void setURLLabelIcon(final Icon icon) {
        urlLabel.setIcon(icon);
    }

    @Resource("calclipse.caldron.gui.script.editor.DefaultScriptEditor.urlLabelMnemonic")
    public void setURLLabelMnemonic(final String keyStroke) {
        if (keyStroke == null) {
            urlLabel.setDisplayedMnemonic(0);
        } else {
            final KeyStroke stroke = GUIUtil.getKeyStroke(keyStroke);
            urlLabel.setDisplayedMnemonic(stroke.getKeyChar());
        }
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    @Override
    public String getURL() {
        return urlField.getText();
    }

    @Override
    public boolean isExecutable() {
        return executableButton.isSelected();
    }

    @Override
    public void selectName() {
        nameField.requestFocusInWindow();
        nameField.selectAll();
    }

    @Override
    public void setExecutable(final boolean executable) {
        executableButton.setSelected(executable);
        DataProject.PROJECT.setModified(true);
    }

    @Override
    public void setName(final String name) {
        nameField.setText(name);
    }

    @Override
    public void setURL(final String url) {
        urlField.setText(url);
        DataProject.PROJECT.setModified(true);
    }

    @Override
    public Component getComponent() {
        return contentPane;
    }

    @Override
    public Component getFocusComponent() {
        return scriptPane;
    }

    @Override
    public void winCreated(final Win win) {
        win.setJToolBar(toolBar);
        win.setJMenuBar(menuBar);
        win.setResizable(true);
        win.setIconifiable(true);
        win.setMaximizable(true);
        win.setClosable(true);
        win.setDefaultCloseOperation(Win.HIDE_ON_CLOSE);
        updateTitleAndIcon();
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        DataProject.PROJECT.setModified(true);
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        DataProject.PROJECT.setModified(true);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        if (evt.getSource() == executableButton) {
            DataProject.PROJECT.setModified(true);
        } else {
            scriptPane.requestFocusInWindow();
        }
    }

    @Override
    public void modifiedSet(final DataProject project, final boolean oldModified, final boolean newModified) {
        updateTitleAndIcon();
    }

    @Override
    public void itemRemoved(final DataProject project, final int index, final DataItem itm) {
        if (item == itm) {
            close();
        }
    }

    @Override
    public void projectCleared(final DataProject project) {
        close();
    }

    @Override
    public void projectLoaded(final DataProject project) {
        if (DataProject.PROJECT.indexOf(item) < 0) {
            close();
        }
    }

    @Override
    public void itemAdded(final DataProject project, final int index, final DataItem itm) {
    }

    @Override
    public void nameChanged(final DataProject project, final String oldName, final String newName) {
    }

    @Override
    public void projectSaved(final DataProject project) {
    }

    @Override
    public Collection<? extends SearchHit> find(final Object what) {
        if (what instanceof TextSearch) {
            final TextSearch search = (TextSearch) what;
            final Collection<SearchHit> hits = new ArrayList<SearchHit>();
            hits.addAll(DefaultScriptSearchHit.getMatches(item, false, nameField, search));
            hits.addAll(DefaultScriptSearchHit.getMatches(item, true, scriptPane, search));
            return hits;
        }
        return Collections.emptyList();
    }

    private void close() {
        try {
            GUI.getWin(this).close();
            DataProject.PROJECT.removeDataProjectListener(this);
        } catch (final WinVetoException ex) {
            ExceptionHandlers.getGeneralHandler().handle(ex);
        }
    }

    private void updateTitleAndIcon() {
        if (!GUI.getGUI().getWinManager().isWinClosed(this)) {
            final Win win = GUI.getWin(this);
            win.setTitle(nameField.getText());
            win.setIcon(item.getIcon());
        }
    }
}
