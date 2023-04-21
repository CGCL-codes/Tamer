package org.kalypso.nofdpidss.timeseries.wizard.repository.hydrograph.pages;

import java.io.File;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.kalypso.contribs.eclipse.jface.viewers.FCVArrayDelegate;
import org.kalypso.contribs.eclipse.jface.viewers.FacadeComboViewer;
import org.kalypso.nofdpidss.core.view.widgets.MyWizardPage;
import org.kalypso.nofdpidss.timeseries.i18n.Messages;
import org.kalypso.ogc.sensor.view.wizard.cvssheet.CsvSheetImportDataModel.CSV_COLUMN_SEPERATORS;
import org.kalypso.ogc.sensor.view.wizard.cvssheet.CsvSheetImportDataModel.DECIMAL_NUMBER_SEPERATORS;
import org.kalypso.repository.file.FileItem;

/**
 * @author Dirk Kuch
 */
public class PageSelectCSV extends MyWizardPage implements ICSVSettings {

    protected File m_file;

    private CSV_COLUMN_SEPERATORS m_columnSeperator;

    private DECIMAL_NUMBER_SEPERATORS m_decimalSeperator;

    private Text m_name;

    private Text m_description;

    private Text m_river;

    private Text m_position;

    private final FileItem m_item;

    public PageSelectCSV(final FileItem item) {
        super("pageSelectCSV");
        m_item = item;
        setTitle(Messages.PageSelectCSV_0);
        setDescription(Messages.PageSelectCSV_1);
    }

    @Override
    protected void checkPage() {
        final String name = m_name.getText();
        final String description = m_description.getText();
        final String river = m_river.getText();
        final String position = m_position.getText();
        if (name == null || "".equals(name.trim())) {
            isMissing("name");
        } else if (m_file == null) {
            isCustom(Messages.PageSelectCSV_10);
        } else if (!m_file.exists()) {
            isCustom(Messages.PageSelectCSV_11);
        } else if (m_columnSeperator == null) {
            isCustom(Messages.PageSelectCSV_12);
        } else if (m_decimalSeperator == null) {
            isCustom(Messages.PageSelectCSV_13);
        } else {
            setMessage(null);
            setErrorMessage(null);
            setPageComplete(true);
        }
    }

    /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
    public void createControl(final Composite parent) {
        setPageComplete(false);
        final Composite base = new Composite(parent, SWT.NULL);
        base.setLayout(new GridLayout());
        final Group grDetails = new Group(base, SWT.NULL);
        grDetails.setLayout(new GridLayout(2, false));
        grDetails.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        grDetails.setText(Messages.PageSelectCSV_14);
        new Label(grDetails, SWT.NONE).setText(Messages.PageSelectCSV_15);
        m_name = new Text(grDetails, SWT.BORDER);
        m_name.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        m_name.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                checkPage();
            }
        });
        new Label(grDetails, SWT.NONE).setText(Messages.PageSelectCSV_16);
        m_description = new Text(grDetails, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        m_description.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        m_description.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                checkPage();
            }
        });
        new Label(grDetails, SWT.NONE).setText(Messages.PageSelectCSV_17);
        m_river = new Text(grDetails, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        m_river.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        m_river.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                checkPage();
            }
        });
        new Label(grDetails, SWT.NONE).setText(Messages.PageSelectCSV_18);
        m_position = new Text(grDetails, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        m_position.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        m_position.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                checkPage();
            }
        });
        final Group grFile = new Group(base, SWT.NULL);
        grFile.setLayout(new GridLayout(2, false));
        grFile.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        grFile.setText(Messages.PageSelectCSV_19);
        final Text tZml = new Text(grFile, SWT.READ_ONLY | SWT.BORDER);
        tZml.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        final Button bZml = new Button(grFile, SWT.NONE);
        bZml.setText("...");
        bZml.addSelectionListener(new SelectionAdapter() {

            /**
       * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
       */
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final FileDialog fileDialog = new FileDialog(bZml.getShell());
                fileDialog.setFilterNames(new String[] { Messages.PageSelectCSV_20 });
                fileDialog.setFilterExtensions(new String[] { "*.csv" });
                final String text = fileDialog.open();
                if (text != null) {
                    tZml.setText(text);
                    m_file = new File(text);
                }
                checkPage();
            }
        });
        final Group grSeperator = new Group(base, SWT.NULL);
        grSeperator.setLayout(new GridLayout(2, false));
        grSeperator.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        grSeperator.setText(Messages.PageSelectCSV_21);
        final Label lColumnSeperator = new Label(grSeperator, SWT.NONE);
        lColumnSeperator.setText("CSV column seperator");
        final CSV_COLUMN_SEPERATORS[] columnSeperators = new CSV_COLUMN_SEPERATORS[] { CSV_COLUMN_SEPERATORS.eSemicolon, CSV_COLUMN_SEPERATORS.eTab, CSV_COLUMN_SEPERATORS.eSpace };
        final FacadeComboViewer wColumnSeperators = new FacadeComboViewer(new FCVArrayDelegate(columnSeperators));
        wColumnSeperators.draw(grSeperator, new GridData(GridData.FILL, GridData.FILL, true, false), SWT.BORDER | SWT.READ_ONLY | SWT.SINGLE);
        wColumnSeperators.addSelectionChangedListener(new Runnable() {

            public void run() {
                updateColumnSeperator((IStructuredSelection) wColumnSeperators.getSelection());
                checkPage();
            }
        });
        updateColumnSeperator((IStructuredSelection) wColumnSeperators.getSelection());
        final Label lDecimalSeperator = new Label(grSeperator, SWT.NONE);
        lDecimalSeperator.setText("Decimal Number seperator");
        final DECIMAL_NUMBER_SEPERATORS[] decimalSeperators = new DECIMAL_NUMBER_SEPERATORS[] { DECIMAL_NUMBER_SEPERATORS.ePoint, DECIMAL_NUMBER_SEPERATORS.eComma };
        final FacadeComboViewer wDecimalSeperators = new FacadeComboViewer(new FCVArrayDelegate(decimalSeperators));
        wDecimalSeperators.draw(grSeperator, new GridData(GridData.FILL, GridData.FILL, true, false), SWT.BORDER | SWT.READ_ONLY | SWT.SINGLE);
        wDecimalSeperators.addSelectionChangedListener(new Runnable() {

            public void run() {
                updateDecimalSeperator((IStructuredSelection) wDecimalSeperators.getSelection());
                checkPage();
            }
        });
        updateDecimalSeperator((IStructuredSelection) wDecimalSeperators.getSelection());
        setControl(base);
        checkPage();
    }

    private void updateColumnSeperator(final IStructuredSelection selection) {
        final Object element = selection.getFirstElement();
        if (element instanceof CSV_COLUMN_SEPERATORS) {
            m_columnSeperator = (CSV_COLUMN_SEPERATORS) element;
        } else m_columnSeperator = null;
    }

    private void updateDecimalSeperator(final IStructuredSelection selection) {
        final Object element = selection.getFirstElement();
        if (element instanceof DECIMAL_NUMBER_SEPERATORS) {
            m_decimalSeperator = (DECIMAL_NUMBER_SEPERATORS) element;
        } else m_decimalSeperator = null;
    }

    public File getFile() {
        return m_file;
    }

    public CSV_COLUMN_SEPERATORS getColumnSeperator() {
        return m_columnSeperator;
    }

    public DECIMAL_NUMBER_SEPERATORS getNumberSeperator() {
        return m_decimalSeperator;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.repository.hydrograph.pages.ICSVSettings#getDestinationFolder()
   */
    public File getDestinationFolder() {
        return m_item.getFile();
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.repository.hydrograph.pages.ICSVSettings#getFileItem()
   */
    public FileItem getFileItem() {
        return m_item;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.repository.hydrograph.pages.ICSVSettings#getPosition()
   */
    public String getPosition() {
        return m_position.getText();
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.repository.hydrograph.pages.ICSVSettings#getRiver()
   */
    public String getRiver() {
        return m_river.getText();
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.repository.hydrograph.pages.ICSVSettings#getReposDescription()
   */
    public String getReposDescription() {
        return m_description.getText();
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.repository.hydrograph.pages.ICSVSettings#getReposName()
   */
    public String getReposName() {
        return m_name.getText();
    }
}
