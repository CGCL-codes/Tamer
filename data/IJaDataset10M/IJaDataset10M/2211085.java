package flash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class PageVocabulary extends TabPage {

    private Combo cmbLanguage;

    private Text txtFilter;

    private Label lblThisLang;

    private Label lblRelWords;

    private Table tblThisLang;

    private Table tblRelWords;

    /**
	 * creates a new language page using the given container
	 * 
	 * @param panel
	 *            the container where controls will be added
	 */
    public PageVocabulary(MainWindow parent, Composite panel) {
        super(parent, panel);
        initializeControls();
    }

    /**
	 * initializes the language tab page with its controls
	 */
    private void initializeControls() {
        FormLayout formLayout;
        FormData fd;
        formLayout = new FormLayout();
        formLayout.marginWidth = 10;
        formLayout.marginHeight = 10;
        panel.setLayout(formLayout);
        fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.top = new FormAttachment(0, 10);
        fd.right = new FormAttachment(50, -5);
        cmbLanguage = new Combo(panel, SWT.NONE);
        cmbLanguage.setLayoutData(fd);
        fd = new FormData();
        fd.left = new FormAttachment(50, 5);
        fd.top = new FormAttachment(cmbLanguage, 0, SWT.CENTER);
        fd.right = new FormAttachment(100, 0);
        txtFilter = new Text(panel, SWT.BORDER);
        txtFilter.setLayoutData(fd);
        fd = new FormData();
        fd.left = new FormAttachment(cmbLanguage, 0, SWT.LEFT);
        fd.top = new FormAttachment(cmbLanguage, 10);
        fd.right = new FormAttachment(50, -5);
        lblThisLang = new Label(panel, SWT.NONE);
        lblThisLang.setLayoutData(fd);
        lblThisLang.setText("Words in this language");
        fd = new FormData();
        fd.left = new FormAttachment(txtFilter, 0, SWT.LEFT);
        fd.top = new FormAttachment(cmbLanguage, 10);
        fd.right = new FormAttachment(100, 0);
        lblRelWords = new Label(panel, SWT.NONE);
        lblRelWords.setLayoutData(fd);
        lblRelWords.setText("Related words in other languages");
        fd = new FormData();
        fd.left = new FormAttachment(lblThisLang, 0, SWT.LEFT);
        fd.top = new FormAttachment(lblThisLang, 10);
        fd.right = new FormAttachment(50, -5);
        fd.bottom = new FormAttachment(100, -10);
        tblThisLang = new Table(panel, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        tblThisLang.setLayoutData(fd);
        fd = new FormData();
        fd.left = new FormAttachment(lblRelWords, 0, SWT.LEFT);
        fd.top = new FormAttachment(lblRelWords, 10);
        fd.right = new FormAttachment(100, 0);
        fd.bottom = new FormAttachment(100, -10);
        tblRelWords = new Table(panel, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        tblRelWords.setLayoutData(fd);
    }
}
