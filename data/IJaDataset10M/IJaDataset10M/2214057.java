package org.isi.monet.modelling.core.dialogs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class AddLanguageDialog extends Dialog {

    private HashMap<String, String> languages;

    private HashMap<String, String> defaultLanguages;

    public AddLanguageDialog(Shell parentShell, HashMap<String, String> languages) {
        super(parentShell);
        this.languages = languages;
        defaultLanguages = new HashMap<String, String>();
        defaultLanguages.put(Messages.AddLanguageDialog_LanguageSpanish, Messages.AddLanguageDialog_LanguageSpanish);
        defaultLanguages.put(Messages.AddLanguageDialog_LanguageEnglish, Messages.AddLanguageDialog_LanguageEnglish);
        defaultLanguages.put(Messages.AddLanguageDialog_LanguageGerman, Messages.AddLanguageDialog_LanguageGerman);
        defaultLanguages.put(Messages.AddLanguageDialog_LanguageItalian, Messages.AddLanguageDialog_LanguageItalian);
        defaultLanguages.put(Messages.AddLanguageDialog_LanguageFrench, Messages.AddLanguageDialog_LanguageFrench);
    }

    protected Control createDialogArea(Composite parent) {
        Composite languagesGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        languagesGroup.setLayout(layout);
        languagesGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Group gLanguages = new Group(languagesGroup, SWT.SHADOW_ETCHED_IN);
        GridLayout lContents = new GridLayout();
        lContents.numColumns = 2;
        gLanguages.setText(Messages.AddLanguageDialog_Group_Languages_Title);
        gLanguages.setLayout(lContents);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gLanguages.setLayoutData(gd);
        Table tableLanguages = new Table(gLanguages, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tableLanguages.setLayoutData(new GridData(GridData.FILL_BOTH));
        UpdateItems(tableLanguages);
        tableLanguages.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    if (languages == null) languages = new HashMap<String, String>();
                    TableItem item = (TableItem) event.item;
                    if (item.getChecked() == Boolean.FALSE) {
                        languages.remove(item.getText());
                    } else languages.put(item.getText(), item.getText());
                    String text = ((TableItem) event.item).getText();
                    System.out.println(text);
                }
            }
        });
        return languagesGroup;
    }

    private void UpdateItems(Table table) {
        if (languages != null) {
            Collection<String> values = languages.values();
            Iterator<String> iter = values.iterator();
            while (iter.hasNext()) {
                String sLanguage = iter.next();
                defaultLanguages.remove(sLanguage);
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(sLanguage);
                item.setChecked(true);
            }
        }
        Collection<String> values = defaultLanguages.values();
        Iterator<String> iter = values.iterator();
        while (iter.hasNext()) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(iter.next());
        }
    }

    public HashMap<String, String> getLanguagesSelected() {
        return languages;
    }
}
