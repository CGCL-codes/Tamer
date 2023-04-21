package com.swabunga.spell.swing;

import static org.makagiga.commons.UI._;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellCheckEvent;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.Map;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.makagiga.commons.MButton;
import org.makagiga.commons.MButtonPanel;
import org.makagiga.commons.MComboBox;
import org.makagiga.commons.MDisposable;
import org.makagiga.commons.MIcon;
import org.makagiga.commons.MLabel;
import org.makagiga.commons.MList;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.MMessage;
import org.makagiga.commons.MPanel;
import org.makagiga.commons.MRenderer;
import org.makagiga.commons.MScrollPane;
import org.makagiga.commons.MText;
import org.makagiga.commons.MTextField;
import org.makagiga.commons.TK;
import org.makagiga.commons.UI;

/**
 * Implementation of a spell check form.
 *
 * @author Jason Height (jheight@chariot.net.au)
 * @author Konrad Twardowski (Makagiga version)
 */
public final class JSpellForm extends MPanel implements ActionListener, ListSelectionListener, MDisposable {

    private static final String IGNORE_CMD = "IGNORE";

    private static final String IGNOREALL_CMD = "IGNOREALL";

    private static final String ADD_CMD = "ADD";

    private static final String REPLACE_CMD = "REPLACE";

    private static final String REPLACEALL_CMD = "REPLACEALL";

    private static Map<Locale, File> localeMap = TK.newHashMap();

    private MComboBox<Locale> localeComboBox;

    private MLabel wrongWordLabel;

    private MList<Word> suggestionList;

    private static final MLogger LOG = MLogger.get("spell");

    private MTextField checkText;

    /**
	 * The current spell check event.
	 */
    private SpellCheckEvent spellEvent;

    /**
	 * Panel constructor.
	 */
    public JSpellForm(final MPanel buttonsPanel) {
        super(DEFAULT_CONTENT_MARGIN, DEFAULT_CONTENT_MARGIN);
        MButtonPanel ignorePanel = new MButtonPanel(MButtonPanel.VERTICAL | MButtonPanel.NO_REVERSE, createButton(IGNORE_CMD, _("Ignore"), this, "ui/cancel"), createButton(IGNOREALL_CMD, _("Ignore All"), this, null));
        buttonsPanel.add(ignorePanel);
        buttonsPanel.addContentGap();
        buttonsPanel.add(createButton(ADD_CMD, _("Add to Dictionary"), this, "ui/plus"));
        buttonsPanel.addContentGap();
        MButtonPanel changePanel = new MButtonPanel(MButtonPanel.VERTICAL | MButtonPanel.NO_REVERSE, createButton(REPLACE_CMD, _("Change"), this, "ui/apply"), createButton(REPLACEALL_CMD, _("Change All"), this, null));
        buttonsPanel.add(changePanel);
        addCenter(makeCenterPanel());
        localeComboBox = new MComboBox<Locale>();
        localeComboBox.addAllItems(localeMap.keySet());
        localeComboBox.setRenderer(new MRenderer<Locale>() {

            @Override
            protected void onRender(final Locale value) {
                this.setText(value.getDisplayName() + " - " + value);
            }
        });
        if (localeComboBox.getItemCount() == 1) localeComboBox.setEnabled(false);
        addSouth(MPanel.createHLabelPanel(localeComboBox, _("Language:")));
    }

    /**
	 * Fired when a button is clicked.
	 */
    @Override
    public void actionPerformed(final ActionEvent e) {
        LOG.debugFormat("actionPerformed: %s", e);
        Window dialog = UI.windowFor(this);
        if (IGNORE_CMD.equals(e.getActionCommand())) {
            spellEvent.ignoreWord(false);
        } else if (IGNOREALL_CMD.equals(e.getActionCommand())) {
            spellEvent.ignoreWord(true);
        } else if (REPLACE_CMD.equals(e.getActionCommand())) {
            checkText.saveAutoCompletion();
            spellEvent.replaceWord(checkText.getText(), false);
        } else if (REPLACEALL_CMD.equals(e.getActionCommand())) {
            checkText.saveAutoCompletion();
            spellEvent.replaceWord(checkText.getText(), true);
        } else if (ADD_CMD.equals(e.getActionCommand())) {
            checkText.saveAutoCompletion();
            String inField = checkText.getText();
            Word selectedWord = suggestionList.getSelectedItem();
            String selected = (selectedWord == null) ? "" : selectedWord.toString();
            String addString = inField.equals(selected) ? spellEvent.getInvalidWord() : inField;
            if (MMessage.simpleConfirm(dialog)) {
                LOG.debugFormat("spellEvent.addToDictionary: %s", addString);
                spellEvent.addToDictionary(addString);
            } else {
                return;
            }
        }
        if (dialog instanceof JSpellDialog) JSpellDialog.class.cast(dialog).setVisible(false);
    }

    @Override
    public Object dispose(final Object... args) {
        LOG.debug("dispose (form)");
        checkText = null;
        spellEvent = null;
        suggestionList = null;
        wrongWordLabel = null;
        return null;
    }

    public static Map<Locale, File> getLocaleMap() {
        return localeMap;
    }

    /**
	 * Sets the current spell check event that is being shown to the user.
	 */
    public void setSpellEvent(final SpellCheckEvent event) {
        LOG.debugFormat("setSpellEvent: %s", event);
        spellEvent = event;
        try {
            suggestionList.putClientProperty("com.swabunga.spell.swing.JSpellForm.inUpdate", true);
            suggestionList.clear();
            suggestionList.addAllItems(event.getSuggestions());
        } finally {
            suggestionList.putClientProperty("com.swabunga.spell.swing.JSpellForm.inUpdate", null);
        }
        wrongWordLabel.setHTML(_("Not in Dictionary: <b>{0}</b>", TK.escapeXML(event.getInvalidWord())));
        checkText.setText(event.getInvalidWord());
        MText.selectAll(checkText);
    }

    /**
	 * Fired when a value in the list is selected.
	 */
    @Override
    public void valueChanged(final ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && !Boolean.TRUE.equals(suggestionList.getClientProperty("com.swabunga.spell.swing.JSpellForm.inUpdate"))) {
            LOG.debug("valueChanged");
            Word word = suggestionList.getSelectedItem();
            if (word != null) {
                checkText.setText(word.toString());
            }
        }
    }

    /**
	 * Helper method to create a MButton with a command, a text label and a listener.
	 */
    private static MButton createButton(final String command, final String text, final ActionListener listener, final String iconName) {
        MButton b = new MButton(text);
        b.setActionCommand(command);
        b.setIcon(MIcon.small(iconName));
        b.addActionListener(listener);
        return b;
    }

    private MPanel makeCenterPanel() {
        MPanel p = MPanel.createVBoxPanel();
        wrongWordLabel = new MLabel("");
        p.add(wrongWordLabel);
        p.addGap();
        checkText = new MTextField();
        checkText.setAutoCompletion("spell");
        wrongWordLabel.setLabelFor(checkText);
        p.add(checkText);
        p.addGap();
        suggestionList = new MList<Word>();
        suggestionList.setSingleSelectionMode();
        suggestionList.setText(_("No Suggestions"));
        suggestionList.addListSelectionListener(this);
        p.add(MLabel.createFor(suggestionList, _("Suggestions:")));
        p.addGap();
        p.add(new MScrollPane(suggestionList));
        return p;
    }
}
