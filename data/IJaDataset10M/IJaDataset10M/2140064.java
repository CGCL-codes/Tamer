package de.javagimmicks.swing;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class AutoComboBoxEditor extends BasicComboBoxEditor {

    public static void install(JComboBox comboBox, boolean markOnFocus, boolean caseSensitive, boolean strict) {
        AutoComboBoxEditor editor = new AutoComboBoxEditor(comboBox);
        editor.setCaseSensitive(caseSensitive);
        editor.setStrict(strict);
        editor.setMarkOnFocus(markOnFocus);
        comboBox.setEditor(editor);
        comboBox.setEditable(true);
    }

    public static void install(JComboBox comboBox, boolean markOnFocus, boolean caseSensitive) {
        install(comboBox, markOnFocus, caseSensitive, true);
    }

    public static void install(JComboBox comboBox, boolean markOnFocus) {
        install(comboBox, markOnFocus, false);
    }

    public static void install(JComboBox comboBox) {
        install(comboBox, false);
    }

    private final JComboBox _comboBox;

    private boolean _caseSensitive = false;

    private boolean _strict = true;

    private MarkOnFocusListener _markOnFocusListener;

    public AutoComboBoxEditor(JComboBox comboBox) {
        _comboBox = comboBox;
        editor = new AutoTextField();
    }

    public boolean isCaseSensitive() {
        return _caseSensitive;
    }

    public boolean isStrict() {
        return _strict;
    }

    public boolean isMarkOnFocus() {
        return _markOnFocusListener != null;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        _caseSensitive = caseSensitive;
    }

    public void setStrict(boolean strict) {
        _strict = strict;
    }

    public void setMarkOnFocus(boolean markOnFocus) {
        if (markOnFocus == isMarkOnFocus()) {
            return;
        }
        if (markOnFocus) {
            _markOnFocusListener = new MarkOnFocusListener(editor);
            editor.addFocusListener(_markOnFocusListener);
        } else {
            editor.removeFocusListener(_markOnFocusListener);
            _markOnFocusListener = null;
        }
    }

    private ComboBoxModel getModel() {
        return _comboBox.getModel();
    }

    private class AutoTextField extends JTextField {

        private static final long serialVersionUID = -2404637927639461570L;

        public AutoTextField() {
            setDocument(new AutoDocument());
            setBorder(null);
            if (_strict && getModel().getSize() > 0) {
                setText(getModel().getElementAt(0).toString());
            }
        }

        public void replaceSelection(String sText) {
            AutoDocument oAutoDocument = (AutoDocument) getDocument();
            if (oAutoDocument != null) {
                try {
                    int iStart = Math.min(getCaret().getDot(), getCaret().getMark());
                    int iEnd = Math.max(getCaret().getDot(), getCaret().getMark());
                    oAutoDocument.replace(iStart, iEnd - iStart, sText, null);
                } catch (Exception exception) {
                }
            }
        }

        private String getMatch(String sText) {
            for (int i = 0; i < getModel().getSize(); i++) {
                String sListEntry = getModel().getElementAt(i).toString();
                if (sListEntry != null) {
                    if (!_caseSensitive && sListEntry.toLowerCase().startsWith(sText.toLowerCase())) {
                        return sListEntry;
                    }
                    if (_caseSensitive && sListEntry.startsWith(sText)) {
                        return sListEntry;
                    }
                }
            }
            return null;
        }

        private class AutoDocument extends PlainDocument {

            private static final long serialVersionUID = -7796598731233922182L;

            public void replace(int iOffset, int iLength, String sText, AttributeSet oAttributeSet) throws BadLocationException {
                super.remove(iOffset, iLength);
                insertString(iOffset, sText, oAttributeSet);
            }

            public void insertString(int iOffset, String sText, AttributeSet oAttributeSet) throws BadLocationException {
                if (sText == null || "".equals(sText)) return;
                String sStart = getText(0, iOffset);
                String sMatch = getMatch(sStart + sText);
                int iLength = (iOffset + sText.length()) - 1;
                if (_strict && sMatch == null) {
                    sMatch = getMatch(sStart);
                    iLength--;
                } else if (!_strict && sMatch == null) {
                    super.insertString(iOffset, sText, oAttributeSet);
                    return;
                }
                if (sMatch != null) {
                    getModel().setSelectedItem(sMatch);
                }
                super.remove(0, getLength());
                super.insertString(0, sMatch, oAttributeSet);
                setSelectionStart(iLength + 1);
                setSelectionEnd(getLength());
            }

            public void remove(int iOffset, int iLength) throws BadLocationException {
                int iStart = getSelectionStart();
                if (iStart > 0) {
                    iStart--;
                }
                String sMatch = getMatch(getText(0, iStart));
                if (!_strict && sMatch == null) {
                    super.remove(iOffset, iLength);
                } else {
                    super.remove(0, getLength());
                    super.insertString(0, sMatch, null);
                }
                if (sMatch != null) {
                    getModel().setSelectedItem(sMatch);
                }
                try {
                    setSelectionStart(iStart);
                    setSelectionEnd(getLength());
                } catch (Exception exception) {
                }
            }
        }
    }
}
