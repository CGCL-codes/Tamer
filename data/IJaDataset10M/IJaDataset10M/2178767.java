package es.eucm.eadventure.editor.gui.metadatadialog.lomes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.data.meta.LangString;

public class LOMLangStringPanel extends JPanel {

    private JTextField value;

    private JTextField language;

    private LangString langstring;

    public LOMLangStringPanel(LangString langstring, String border) {
        super();
        String valueData;
        String languageData;
        this.setLayout(new GridBagLayout());
        if (langstring != null) {
            valueData = langstring.getValue(0);
            languageData = langstring.getLanguage(0);
            this.langstring = langstring;
        } else {
            valueData = new String("");
            languageData = new String("");
            langstring = new LangString("");
        }
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel valuePanel = new JPanel(new GridBagLayout());
        value = new JTextField(valueData);
        value.getDocument().addDocumentListener(new TextFieldListener(value));
        value.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), TC.get("LOMES.LangString.Value")));
        valuePanel.add(value, c);
        JPanel languagePanel = new JPanel(new GridBagLayout());
        language = new JTextField(languageData);
        language.getDocument().addDocumentListener(new TextFieldListener(language));
        language.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), TC.get("LOMES.LangString.Language")));
        languagePanel.add(language, c);
        c.gridy = 0;
        this.add(valuePanel, c);
        c.gridy = 1;
        this.add(languagePanel, c);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), border));
    }

    private class TextFieldListener implements DocumentListener {

        private JTextField text;

        public TextFieldListener(JTextField text) {
            this.text = text;
        }

        public void changedUpdate(DocumentEvent e) {
        }

        public void insertUpdate(DocumentEvent e) {
            if (text == value) {
                langstring.setValue(value.getText(), 0);
            } else if (text == language) {
                langstring.setLanguage(language.getText(), 0);
            }
        }

        public void removeUpdate(DocumentEvent e) {
            insertUpdate(e);
        }
    }
}
