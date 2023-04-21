package net.sf.ulmac.ui.dialogs;

import net.sf.ulmac.core.searchsite.ISearchSite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class SearchSiteDialog {

    private Button fBtnCancel;

    private Button fBtnOk;

    private Shell fParentShell;

    private Shell fShell;

    private Text fTxtDisplay;

    private Text fTxtPreUrl;

    private Text fTxtPostUrl;

    private Text fTxtSpaceReplacement;

    private ISearchSite fSearchSite;

    public SearchSiteDialog(final ISearchSite searchSite) {
        fSearchSite = searchSite;
        fParentShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        fShell = new Shell(PlatformUI.getWorkbench().getDisplay(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.RESIZE);
        if (searchSite.getDisplay().length() > 0) {
            fShell.setText("Editing " + searchSite.getDisplay());
        } else {
            fShell.setText("Adding New Search Site");
        }
        fShell.setLayout(new GridLayout(1, false));
        Composite container = new Composite(fShell, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        container.setLayout(new GridLayout(1, false));
        Label lblDisplay = new Label(container, SWT.NONE);
        lblDisplay.setText("Name:");
        fTxtDisplay = new Text(container, SWT.BORDER);
        fTxtDisplay.setText(searchSite.getDisplay());
        fTxtDisplay.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        Label lblPreUrl = new Label(container, SWT.NONE);
        lblPreUrl.setText("Pre Url:");
        fTxtPreUrl = new Text(container, SWT.BORDER);
        fTxtPreUrl.setText(searchSite.getUrlPre());
        fTxtPreUrl.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        Label lblPostUrl = new Label(container, SWT.NONE);
        lblPostUrl.setText("Post Url:");
        fTxtPostUrl = new Text(container, SWT.BORDER);
        fTxtPostUrl.setText(searchSite.getUrlPost());
        fTxtPostUrl.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        Label lblSpaceReplacement = new Label(container, SWT.NONE);
        lblSpaceReplacement.setText("Space Replacement:");
        fTxtSpaceReplacement = new Text(container, SWT.BORDER);
        fTxtSpaceReplacement.setText(searchSite.getSpaceReplacement());
        fTxtSpaceReplacement.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        addDialogButtons(container);
    }

    private void addDialogButtons(Composite container) {
        Composite dialogButtonsComposite = new Composite(container, SWT.NONE);
        dialogButtonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        dialogButtonsComposite.setLayout(new GridLayout(3, false));
        Label lblWhiteSpace = new Label(dialogButtonsComposite, SWT.NONE);
        lblWhiteSpace.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true));
        fBtnOk = new Button(dialogButtonsComposite, SWT.PUSH);
        fBtnOk.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true));
        fBtnOk.setText("   OK   ");
        fShell.setDefaultButton(fBtnOk);
        fBtnOk.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                fSearchSite.setDisplay(fTxtDisplay.getText().replace(";", ""));
                fSearchSite.setUrlPre(fTxtPreUrl.getText().replace(";", ""));
                fSearchSite.setUrlPost(fTxtPostUrl.getText().replace(";", ""));
                fSearchSite.setSpaceReplacement(fTxtSpaceReplacement.getText().replace(";", ""));
                fShell.close();
            }
        });
        fBtnCancel = new Button(dialogButtonsComposite, SWT.PUSH);
        fBtnCancel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true));
        fBtnCancel.setText("Cancel");
        fBtnCancel.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                fShell.close();
            }
        });
    }

    public void addSelectionListener(Listener listener) {
        fBtnOk.addListener(SWT.Selection, listener);
    }

    public void close() {
        if (fShell != null && !fShell.isDisposed()) {
            fShell.close();
        }
    }

    public void open() {
        GC gc = new GC(fShell);
        gc.dispose();
        fShell.setSize(450, 275);
        Rectangle parentSize = fParentShell.getBounds();
        Rectangle size = fShell.getBounds();
        fShell.setLocation(parentSize.x + ((parentSize.width / 2) - (size.width / 2)), parentSize.y + ((parentSize.height / 2) - (size.height / 2)));
        fShell.setImages(PlatformUI.getWorkbench().getDisplay().getActiveShell().getImages());
        fShell.open();
    }
}
