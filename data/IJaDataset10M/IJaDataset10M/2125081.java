package org.hibnet.lune.ui.composite;

import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.hibnet.lune.core.Searcher;
import org.hibnet.lune.ui.LuneUIPlugin;
import org.hibnet.lune.ui.model.ChangeListener;
import org.hibnet.lune.ui.model.Reference;
import org.hibnet.lune.ui.model.SearcherReference;
import org.hibnet.lune.ui.model.SearcherStatus;

/**
 * This composite presents the status and some stats about the index.
 */
public class SearcherStatusComposite extends Composite implements ChangeListener<Searcher> {

    private Label nbDocs;

    private Label hasDeletion;

    private Label indexVersion;

    private Button reopenSearcher;

    private Button undeleteAll;

    private Button commit;

    private SearcherStatus status;

    /**
     * Constructor
     * 
     * @param parent
     *            the parent composite
     * @param style
     *            the SWT style
     */
    public SearcherStatusComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(2, false));
        setBackground(parent.getBackground());
        Label label = new Label(this, SWT.NONE);
        label.setText("Index version : ");
        indexVersion = new Label(this, SWT.BORDER);
        indexVersion.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        label = new Label(this, SWT.NONE);
        label.setText("Number of documents : ");
        nbDocs = new Label(this, SWT.BORDER);
        nbDocs.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        label = new Label(this, SWT.NONE);
        label.setText("Has deletion : ");
        hasDeletion = new Label(this, SWT.BORDER);
        hasDeletion.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        reopenSearcher = new Button(this, SWT.PUSH);
        reopenSearcher.setText("Reopen Searcher");
        reopenSearcher.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                reopenSearcher();
            }
        });
        undeleteAll = new Button(this, SWT.PUSH);
        undeleteAll.setText("Undelete all");
        undeleteAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                undeleteAll();
            }
        });
        commit = new Button(this, SWT.PUSH);
        commit.setText("Commit");
        commit.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                commit();
            }
        });
    }

    public void init(SearcherStatus s) {
        status = s;
        status.addListener(this);
        changed(status.getWorkingModel());
    }

    void reopenSearcher() {
        try {
            status.getWorkingModel().get().reopen();
        } catch (IOException e) {
            LuneUIPlugin.openError("Failed to reopen the searcher", e);
        }
    }

    void commit() {
        try {
            ((SearcherReference) status.getWorkingModel()).renew();
        } catch (IOException e) {
            LuneUIPlugin.openError("Failed to reopen the searcher", e);
        }
    }

    void undeleteAll() {
        try {
            status.getWorkingModel().get().undeleteAll();
        } catch (IOException e) {
            LuneUIPlugin.openError("Failed to reopen the searcher", e);
        }
    }

    private String yesOrNo(boolean b) {
        return b ? "Yes" : "No";
    }

    public void changed(Reference<? extends Searcher> source) {
        if (source != status.getWorkingModel()) {
            return;
        }
        if (!status.isAvailable()) {
            indexVersion.setText("-");
            nbDocs.setText("-");
            hasDeletion.setText("-");
            reopenSearcher.setEnabled(false);
            undeleteAll.setEnabled(false);
            commit.setEnabled(false);
        } else {
            indexVersion.setText(Long.toString(status.getVersion()));
            nbDocs.setText(Integer.toString(status.getNumDocs()));
            hasDeletion.setText(yesOrNo(status.hasDeletions()));
            undeleteAll.setEnabled(status.hasPendingDelete() || status.hasDeletions());
            commit.setEnabled(status.hasPendingDelete());
            reopenSearcher.setEnabled(true);
        }
    }

    @Override
    public void dispose() {
        status.removeListener(this);
        super.dispose();
    }
}
