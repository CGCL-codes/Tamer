package ca.kooki.codesnippet.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;

public class CodeSnippetSearchView extends ViewPart {

    public Label labelSearch;

    public Text inputSearch;

    public Button buttonSearch;

    public Composite outputResults;

    public Composite parent;

    public CodeSnippetSearchView() {
    }

    public void createPartControl(Composite parent) {
        this.parent = parent;
        labelSearch = createLabel(parent, "Search");
        inputSearch = new Text(parent, SWT.LEFT | SWT.SINGLE | SWT.BORDER);
        buttonSearch = new Button(parent, SWT.PUSH);
        buttonSearch.setText("Search");
        outputResults = createResultsComposite(parent);
        GridLayout viewLayout = new GridLayout();
        viewLayout.numColumns = 3;
        GridData layoutData = null;
        layoutData = new GridData();
        labelSearch.setLayoutData(layoutData);
        layoutData = new GridData();
        layoutData.widthHint = 300;
        inputSearch.setLayoutData(layoutData);
        CodeSnippetSearchViewHandler handler = new CodeSnippetSearchViewHandler(inputSearch, outputResults, buttonSearch);
        buttonSearch.addSelectionListener(handler);
        parent.setLayout(viewLayout);
    }

    public void setFocus() {
        inputSearch.setFocus();
    }

    private Label createLabel(Composite parent, String text) {
        Label myLabel = new Label(parent, SWT.LEFT);
        myLabel.setText(text);
        return myLabel;
    }

    private Composite createResultsComposite(Composite parent) {
        ScrolledComposite scrollablePreference = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scrollablePreference.setExpandHorizontal(true);
        scrollablePreference.setExpandVertical(true);
        Composite preference = new Composite(scrollablePreference, SWT.NONE);
        scrollablePreference.setContent(preference);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 3;
        scrollablePreference.setLayoutData(data);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        preference.setLayout(layout);
        return preference;
    }
}
