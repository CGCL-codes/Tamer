package de.beas.explicanto.client.sec.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.dialogs.ExplicantoMessageDialog;
import de.beas.explicanto.client.sec.Languages;

public class TranslationLangDialog extends Dialog {

    protected Composite contentZone;

    protected Label toLangInfo;

    protected Combo toLangList;

    protected String fromLang = "";

    protected String toLang = "";

    protected String[] languages;

    public TranslationLangDialog(Shell parentShell, String fromLang) {
        super(parentShell);
        languages = Languages.getLanguages();
        this.fromLang = fromLang;
    }

    public TranslationLangDialog(Shell parentShell, String[] languages, String fromLang) {
        super(parentShell);
        this.languages = languages;
        this.fromLang = fromLang;
        if (languages == null) {
            this.languages = Languages.getLanguages();
        }
    }

    protected Control createDialogArea(Composite parent) {
        getShell().setText(I18N.translate("sec.dialog.languages"));
        Composite dialog = (Composite) super.createDialogArea(parent);
        dialog.setLayout(new GridLayout(1, true));
        contentZone = new Composite(dialog, SWT.NONE);
        contentZone.setLayout(new GridLayout(2, false));
        toLangInfo = new Label(contentZone, SWT.NONE);
        toLangInfo.setText(I18N.translate("sec.dialog.toLang"));
        toLangList = new Combo(contentZone, SWT.READ_ONLY);
        toLangList.setLayoutData(new GridData(200, SWT.DEFAULT));
        fillLanguageCombo();
        contentZone.setLayoutData(new GridData(GridData.FILL_BOTH));
        return dialog;
    }

    private void fillLanguageCombo() {
        toLangList.removeAll();
        for (int i = 0, count = languages.length; i < count; i++) {
            if (!fromLang.equalsIgnoreCase(languages[i])) {
                toLangList.add(languages[i]);
            }
        }
        toLangList.select(0);
    }

    protected void okPressed() {
        if (toLangList.getSelectionIndex() >= 0) toLang = toLangList.getItem(toLangList.getSelectionIndex()); else {
            ExplicantoMessageDialog.openWarning(getShell(), I18N.translate("sec.screenPlay.translate.msg.noDestLang"));
            return;
        }
        super.okPressed();
    }

    public String getToLang() {
        return toLang;
    }
}
