package vse.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import vse.wizards.vseWizardDataPageSelect.enumSelectType;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (stem).
 */
public class vseWizardPageSelect extends WizardPage {

    private ISelection selection;

    private Button rd_app, rd_gui, rd_control;

    private vseWizard vse = (vseWizard) getWizard();

    /**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
    public vseWizardPageSelect(ISelection selection) {
        super("wizardPage");
        setTitle("Stem File");
        setDescription("��ѡ������ļ�");
        this.selection = selection;
    }

    /**
	 * @see IDialogPage#createControl(Composite)
	 */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        layout.verticalSpacing = 9;
        Label label = new Label(container, SWT.NULL);
        label.setText("����:");
        rd_app = new Button(container, SWT.RADIO);
        rd_app.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (rd_app.getSelection()) {
                    ((vseWizard) getWizard()).data().m_page_select.e_SelectType = enumSelectType.EST_APP;
                }
            }
        });
        rd_app.setText("App Config");
        rd_app.setSelection(true);
        rd_gui = new Button(container, SWT.RADIO);
        rd_gui.setText("Gui Config");
        rd_gui.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (rd_gui.getSelection()) {
                    ((vseWizard) getWizard()).data().m_page_select.e_SelectType = enumSelectType.EST_GUI;
                }
            }
        });
        rd_control = new Button(container, SWT.RADIO);
        rd_control.setText("Control Config");
        rd_control.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (rd_control.getSelection()) {
                    ((vseWizard) getWizard()).data().m_page_select.e_SelectType = enumSelectType.EST_CONTROL;
                }
            }
        });
    }
}
