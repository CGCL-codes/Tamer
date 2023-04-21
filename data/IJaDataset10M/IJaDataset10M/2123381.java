package org.eclipse.mylyn.internal.bugzilla.ui.tasklist;

import org.eclipse.mylyn.internal.bugzilla.core.BugzillaRepositoryQuery;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiImages;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.search.AbstractRepositoryQueryPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * @author Rob Elves
 * @author Mik Kersten
 */
public class BugzillaCustomQueryWizardPage extends AbstractRepositoryQueryPage {

    private static final String LABEL_CUSTOM_QUERY = "Enter query URL";

    private static final String TITLE = "Create query from URL";

    private Text queryText;

    private Composite composite;

    private BugzillaRepositoryQuery query;

    public BugzillaCustomQueryWizardPage(TaskRepository repository, BugzillaRepositoryQuery query) {
        super(TITLE, query.getSummary());
        this.query = query;
        this.repository = repository;
        setTitle(LABEL_CUSTOM_QUERY);
        setImageDescriptor(TasksUiImages.BANNER_REPOSITORY);
    }

    public BugzillaCustomQueryWizardPage(TaskRepository repository) {
        super(TITLE);
        this.repository = repository;
        setTitle(LABEL_CUSTOM_QUERY);
        setImageDescriptor(TasksUiImages.BANNER_REPOSITORY);
    }

    @Override
    public void createControl(Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        super.createControl(composite);
        createCustomQueryGroup(composite);
        composite.pack();
        setControl(composite);
    }

    private void createCustomQueryGroup(Composite composite) {
        Group group = new Group(composite, SWT.NONE);
        group.setText(LABEL_CUSTOM_QUERY);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        group.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 300;
        group.setLayoutData(gd);
        queryText = new Text(group, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        queryText.setLayoutData(gd);
        if (query != null) {
            queryText.setText(query.getUrl());
        }
        queryText.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                setPageComplete(canFlipToNextPage());
            }
        });
    }

    @Override
    public boolean canFlipToNextPage() {
        return false;
    }

    @Override
    public BugzillaRepositoryQuery getQuery() {
        if (query == null) {
            query = new BugzillaRepositoryQuery(repository.getUrl(), queryText.getText(), this.getQueryTitle());
            query.setCustomQuery(true);
        } else {
            query.setHandleIdentifier(this.getQueryTitle());
            query.setUrl(queryText.getText());
        }
        return query;
    }
}
