package org.jabusuite.webclient.controls.language;

import echopointng.ContainerEx;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import org.jabusuite.core.language.JbsLanguage;
import org.jabusuite.webclient.controls.JbsButton;
import org.jabusuite.webclient.controls.JbsExtent;
import org.jabusuite.webclient.controls.richtext.JbsRichTextArea;
import org.jabusuite.webclient.main.JbsL10N;
import org.jabusuite.webclient.windows.JbsDialogWindowOKCancel;

/**
 *
 * @author hilwers
 */
public class JbsLangRichTextArea extends ContainerEx {

    private JbsLanguage mainLanguage;

    private JbsLangStrings langStrings;

    private JbsButton btnSelect;

    private JbsRichTextArea textArea;

    public JbsLangRichTextArea(JbsLanguage language) {
        this.setMainLanguage(language);
        langStrings = new JbsLangStrings();
        this.setTextArea(new JbsRichTextArea());
        this.getTextArea().setDisabledBackground(this.getTextArea().getBackground());
        this.getTextArea().setDisabledForeground(this.getTextArea().getForeground());
        this.getTextArea().setWidth(new JbsExtent(100, JbsExtent.PERCENT));
        this.getTextArea().setHeight(new JbsExtent(200, JbsExtent.PX));
        this.setBtnSelect(new JbsButton("..."));
        this.getBtnSelect().setWidth(new JbsExtent(100, JbsExtent.PERCENT));
        this.getBtnSelect().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                editTexts();
            }
        });
        Column column = new Column();
        column.add(this.getTextArea());
        column.add(this.getBtnSelect());
        this.add(column);
    }

    public void editTexts() {
        final FmLangStringsEditLong fmLangStringsEdit = new FmLangStringsEditLong(JbsL10N.getString("Language.editStrings"));
        fmLangStringsEdit.setLangStrings(this.getLangStrings());
        fmLangStringsEdit.addActionListener(new ActionListener() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JbsDialogWindowOKCancel.ACTION_OK)) {
                    setLangStrings(fmLangStringsEdit.getLangStrings());
                }
            }
        });
        fmLangStringsEdit.showForm();
    }

    /**
     * Sets the <code>JbsLangStrings</code> that shall be edited with this field:
     * @param langStrings
     */
    public void setLangStrings(JbsLangStrings langStrings) {
        this.langStrings = langStrings;
        if (this.langStrings != null) {
            JbsLangString mainLangString = this.langStrings.getLangString(this.getMainLanguage());
            if (mainLangString != null) {
                this.getTextArea().setText(mainLangString.getText());
            }
        }
    }

    /**
     * 
     * @return The <code>JbsLangStrings</code> represented by this field
     */
    public JbsLangStrings getLangStrings() {
        if (this.langStrings != null) {
            this.langStrings.setLangString(this.getMainLanguage(), this.getTextArea().getText());
        }
        return this.langStrings;
    }

    public JbsLanguage getMainLanguage() {
        return mainLanguage;
    }

    public void setMainLanguage(JbsLanguage mainLanguage) {
        this.mainLanguage = mainLanguage;
    }

    public JbsButton getBtnSelect() {
        return btnSelect;
    }

    public void setBtnSelect(JbsButton btnSelect) {
        this.btnSelect = btnSelect;
    }

    public JbsRichTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JbsRichTextArea textArea) {
        this.textArea = textArea;
    }
}
