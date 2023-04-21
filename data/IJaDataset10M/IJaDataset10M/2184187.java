package com.thyante.thelibrarian.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.thyante.thelibrarian.components.MultiAutocompleteField;
import com.thyante.thelibrarian.util.I18n;
import com.thyante.thelibrarian.view.printing.PrintSettings;

/**
 * @author Matthias-M. Christen
 *
 */
public class PrintHeaderFooterPanel {

    public static final String[] PLACEHOLDERS = new String[] { "$date$", "$time$", "$page$", "$number_of_pages$", "$filename$" };

    public static final char ACTIVATION_CHAR = '$';

    protected Button m_btnPrintHeader;

    protected Button m_btnPrintFooter;

    protected Text m_rgtxtHeader[];

    protected Text m_rgtxtFooter[];

    private Label m_lblHeader;

    private Label m_lblFooter;

    protected boolean m_bIsUICreated;

    public PrintHeaderFooterPanel() {
        m_bIsUICreated = false;
    }

    public Composite createUI(Composite cmpParent, PrintSettings settings) {
        Group grp = new Group(cmpParent, SWT.NONE);
        grp.setText(I18n.xl8("Header and Footer"));
        grp.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        grp.setLayout(new GridLayout(3, true));
        m_btnPrintHeader = new Button(grp, SWT.CHECK);
        m_btnPrintHeader.setText(I18n.xl8("Print page headers"));
        m_btnPrintHeader.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1));
        m_rgtxtHeader = new Text[3];
        for (int i = 0; i < m_rgtxtHeader.length; i++) {
            m_rgtxtHeader[i] = new Text(grp, SWT.BORDER);
            GridData dataHeaderText = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            if (i == 0) dataHeaderText.horizontalIndent = 24;
            dataHeaderText.widthHint = 80;
            m_rgtxtHeader[i].setLayoutData(dataHeaderText);
            new MultiAutocompleteField(m_rgtxtHeader[i], PLACEHOLDERS, ACTIVATION_CHAR);
        }
        m_rgtxtHeader[0].setToolTipText(I18n.xl8("Header printed on the left side of the page top"));
        m_rgtxtHeader[1].setToolTipText(I18n.xl8("Header printed in the center of the page top"));
        m_rgtxtHeader[2].setToolTipText(I18n.xl8("Header printed on the right side of the page top"));
        m_lblHeader = new Label(grp, SWT.NONE);
        m_lblHeader.setText(I18n.xl8("Type the header line that will be printed at the top of each page.\n" + "Placeholders for page number, date, filename, etc. are available.\n" + "Placeholders start and end with a '$' sign. Type '$' to get a list of available placeholders."));
        GridData dataHeaderLabel = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1);
        dataHeaderLabel.horizontalIndent = 24;
        m_lblHeader.setLayoutData(dataHeaderLabel);
        m_btnPrintHeader.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean bHasHeader = m_btnPrintHeader.getSelection();
                for (int i = 0; i < m_rgtxtHeader.length; i++) m_rgtxtHeader[i].setEnabled(bHasHeader);
                m_lblHeader.setEnabled(bHasHeader);
            }
        });
        m_btnPrintFooter = new Button(grp, SWT.CHECK);
        m_btnPrintFooter.setText(I18n.xl8("Print page footers"));
        GridData dataFooterButton = new GridData();
        dataFooterButton.verticalIndent = 16;
        dataFooterButton.horizontalSpan = 3;
        m_btnPrintFooter.setLayoutData(dataFooterButton);
        m_rgtxtFooter = new Text[3];
        for (int i = 0; i < m_rgtxtFooter.length; i++) {
            m_rgtxtFooter[i] = new Text(grp, SWT.BORDER);
            GridData dataFooterText = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            if (i == 0) dataFooterText.horizontalIndent = 24;
            dataFooterText.widthHint = 80;
            m_rgtxtFooter[i].setLayoutData(dataFooterText);
            new MultiAutocompleteField(m_rgtxtFooter[i], PLACEHOLDERS, ACTIVATION_CHAR);
        }
        m_rgtxtFooter[0].setToolTipText(I18n.xl8("Footer printed on the left side of the page bottom"));
        m_rgtxtFooter[1].setToolTipText(I18n.xl8("Footer printed in the center of the page bottom"));
        m_rgtxtFooter[2].setToolTipText(I18n.xl8("Footer printed on the right side of the page bottom"));
        m_lblFooter = new Label(grp, SWT.NONE);
        m_lblFooter.setText(I18n.xl8("Type the footer line that will be printed at the bottom of each page.\n" + "The same placeholders are available as for the header."));
        GridData dataFooterLabel = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1);
        dataFooterLabel.horizontalIndent = 24;
        m_lblFooter.setLayoutData(dataFooterLabel);
        m_btnPrintFooter.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean bHasFooter = m_btnPrintFooter.getSelection();
                for (int i = 0; i < m_rgtxtFooter.length; i++) m_rgtxtFooter[i].setEnabled(bHasFooter);
                m_lblFooter.setEnabled(bHasFooter);
            }
        });
        readSettings(settings);
        m_bIsUICreated = true;
        return grp;
    }

    /**
	 * Returns <code>true</code> iff the UI has been created.
	 * @return <code>true</code> iff the UI has been created
	 */
    public boolean isUICreated() {
        return m_bIsUICreated;
    }

    public boolean hasHeader() {
        return m_btnPrintHeader.getSelection();
    }

    public String[] getHeader() {
        if (!hasHeader()) return null;
        String[] rgHeaders = new String[m_rgtxtHeader.length];
        for (int i = 0; i < m_rgtxtHeader.length; i++) rgHeaders[i] = m_rgtxtHeader[i].getText();
        return rgHeaders;
    }

    public boolean hasFooter() {
        return m_btnPrintFooter.getSelection();
    }

    public String[] getFooter() {
        if (!hasFooter()) return null;
        String[] rgFooters = new String[m_rgtxtFooter.length];
        for (int i = 0; i < m_rgtxtFooter.length; i++) rgFooters[i] = m_rgtxtFooter[i].getText();
        return rgFooters;
    }

    public void readSettings(PrintSettings settings) {
        m_btnPrintHeader.setSelection(settings.hasHeader());
        for (int i = 0; i < m_rgtxtHeader.length; i++) if (settings.getHeader()[i] != null) m_rgtxtHeader[i].setText(settings.getHeader()[i]);
        for (int i = 0; i < m_rgtxtHeader.length; i++) m_rgtxtHeader[i].setEnabled(settings.hasHeader());
        m_lblHeader.setEnabled(settings.hasHeader());
        m_btnPrintFooter.setSelection(settings.hasFooter());
        for (int i = 0; i < m_rgtxtFooter.length; i++) if (settings.getFooter()[i] != null) m_rgtxtFooter[i].setText(settings.getFooter()[i]);
        for (int i = 0; i < m_rgtxtFooter.length; i++) m_rgtxtFooter[i].setEnabled(settings.hasFooter());
        m_lblFooter.setEnabled(settings.hasFooter());
    }

    public void saveSettings(PrintSettings settings) {
        settings.setHeader(getHeader());
        settings.setFooter(getFooter());
    }
}
