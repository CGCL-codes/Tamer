package ch.jester.reportengine.impl.ui.export;

import java.util.HashMap;
import messages.Messages;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.reportengine.IReportResult.ExportType;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Tournament;

/**
 * Page um Reports zu exportieren
 *
 */
public class ReportExportPage extends WizardPage {

    private Text text;

    private HashMap<String, Class<?>> mMap = new HashMap<String, Class<?>>();

    private Class<?>[] mSelectedClasses;

    private SelectionUtility su = new SelectionUtility(null);

    private ServiceUtility mServices = new ServiceUtility();

    ComboViewer exportTypeComboViewer;

    ComboViewer tournamentComboViewer;

    ComboViewer reportComboViewer;

    String currentFilter = "*.*";

    /**
	 * Create the wizard.
	 */
    public ReportExportPage() {
        super(Messages.ReportExportPage_1);
        setTitle(Messages.ReportExportPage_title_export);
        setDescription(Messages.ReportExportPage_export_data);
        mMap.put(Messages.ReportExportPage_tournamentsclass, Tournament.class);
    }

    /**
	 * Create contents of the wizard.
	 * @param parent
	 */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        text = new Text(container, SWT.BORDER);
        text.setBounds(23, 101, 320, 21);
        Button btnNewButton = new Button(container, SWT.NONE);
        btnNewButton.setBounds(365, 98, 75, 25);
        btnNewButton.setText(Messages.ReportExportPage_browse);
        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setBounds(24, 10, 55, 15);
        lblNewLabel.setText(Messages.ReportExportPage_export_as);
        Label lblExportWhat = new Label(container, SWT.NONE);
        lblExportWhat.setBounds(23, 62, 75, 13);
        lblExportWhat.setText(Messages.ReportExportPage_tournament);
        exportTypeComboViewer = new ComboViewer(container, SWT.NONE | SWT.READ_ONLY);
        Combo TypeCombo = exportTypeComboViewer.getCombo();
        TypeCombo.setBounds(113, 7, 320, 21);
        exportTypeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
        tournamentComboViewer = new ComboViewer(container, SWT.NONE | SWT.READ_ONLY);
        Combo TournamentCombo = tournamentComboViewer.getCombo();
        TournamentCombo.setBounds(113, 59, 320, 21);
        Label lblReport = new Label(container, SWT.NONE);
        lblReport.setBounds(23, 37, 49, 13);
        lblReport.setText(Messages.ReportExportPage_resport);
        reportComboViewer = new ComboViewer(container, SWT.NONE | SWT.READ_ONLY);
        Combo combo = reportComboViewer.getCombo();
        combo.setBounds(113, 34, 320, 21);
        reportComboViewer.setContentProvider(ArrayContentProvider.getInstance());
        tournamentComboViewer.setContentProvider(ArrayContentProvider.getInstance());
        text.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                setPageComplete(valid());
                getContainer().updateButtons();
            }
        });
        btnNewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
                fd.setText(Messages.ReportExportPage_open);
                fd.setFilterPath(".");
                String[] filterExt = { currentFilter };
                fd.setFilterExtensions(filterExt);
                String selected = fd.open();
                if (selected == null) {
                    return;
                }
                text.setText(selected);
            }
        });
        PageCompleteListener completeListener = new PageCompleteListener();
        exportTypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            SelectionUtility su = new SelectionUtility(null);

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                String lastFilter = currentFilter;
                su.setSelection(event.getSelection());
                ExportType ex = su.getFirstSelectedAs(IReportResult.ExportType.class);
                currentFilter = "*." + ex.getExtension();
                if (text.getText() != null && text.getText().length() > 0) {
                    String oldText = text.getText();
                    String sold = oldText.substring(0, oldText.length() - lastFilter.length() + 1);
                    text.setText(sold + currentFilter.substring(1));
                }
            }
        });
        exportTypeComboViewer.setInput(IReportResult.ExportType.values());
        tournamentComboViewer.setInput(mServices.getDaoServiceByEntity(Tournament.class).getAll());
        reportComboViewer.setInput(getReports());
        reportComboViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                return ((IReport) element).getVisibleName();
            }
        });
        exportTypeComboViewer.addSelectionChangedListener(completeListener);
        tournamentComboViewer.addSelectionChangedListener(completeListener);
        tournamentComboViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                return ((Tournament) element).getName();
            }
        });
        setPageComplete(false);
    }

    private Object[] getReports() {
        return mServices.getService(IReportEngine.class).getRepository().getVisibleReports().toArray();
    }

    public Class<?>[] getSelectedCalsses() {
        return mSelectedClasses;
    }

    public IReportResult.ExportType getSelectedExportType() {
        su.setSelection(exportTypeComboViewer.getSelection());
        return su.getFirstSelectedAs(IReportResult.ExportType.class);
    }

    public Tournament getSelectedTournament() {
        su.setSelection(tournamentComboViewer.getSelection());
        return su.getFirstSelectedAs(Tournament.class);
    }

    public IReport getSelectedReport() {
        su.setSelection(reportComboViewer.getSelection());
        return su.getFirstSelectedAs(IReport.class);
    }

    public String getSelectedFile() {
        return text.getText();
    }

    public String getFileName() {
        return text.getText();
    }

    public boolean valid() {
        boolean complete = false;
        if (getSelectedExportType() != null && getSelectedTournament() != null && getSelectedReport() != null && getSelectedFile().length() > 0) {
            complete = true;
        } else {
            complete = false;
        }
        return complete;
    }

    class PageCompleteListener implements ISelectionChangedListener {

        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            setPageComplete(valid());
            getContainer().updateButtons();
        }
    }
}
