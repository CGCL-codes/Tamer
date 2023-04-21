package com.thyante.thelibrarian.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import com.thyante.thelibrarian.Application;
import com.thyante.thelibrarian.MainWindow;
import com.thyante.thelibrarian.Resources;
import com.thyante.thelibrarian.components.docking.IViewPart;
import com.thyante.thelibrarian.dialogs.ItemProgressDialog;
import com.thyante.thelibrarian.model.Collection;
import com.thyante.thelibrarian.model.Field;
import com.thyante.thelibrarian.model.ICollection;
import com.thyante.thelibrarian.model.specification.IField;
import com.thyante.thelibrarian.model.specification.IItem;
import com.thyante.thelibrarian.model.specification.ITemplate;
import com.thyante.thelibrarian.util.I18n;
import com.thyante.thelibrarian.util.StringUtil;

public class SearchView implements IInteractionView, IViewPart, IExpansionListener {

    /**
	 * Returns the view's title.
	 * @return The view title
	 */
    public static String getText() {
        return I18n.xl8("Search");
    }

    private class SearchTask implements Runnable {

        private ItemProgressDialog m_dialog;

        private List<Field> m_listFields;

        private int m_nStartIndex;

        private int m_nEndIndex;

        private List<IItem> m_listLocalResults;

        private int m_nMonitorStep;

        public SearchTask(ItemProgressDialog dlg, List<Field> listFields, int nStartIndex, int nEndIndex, int nMonitorStep) {
            m_dialog = dlg;
            m_listFields = listFields;
            m_nStartIndex = nStartIndex;
            m_nEndIndex = nEndIndex;
            m_nMonitorStep = nMonitorStep;
            m_listLocalResults = new LinkedList<IItem>();
        }

        public void run() {
            int nCurrentMonitorStep = 0;
            for (int i = m_nStartIndex; i < m_nEndIndex; i++, nCurrentMonitorStep++) {
                final IItem item = m_collection.getItem(i);
                if (m_nStartIndex == 0 && m_dialog != null && (nCurrentMonitorStep == m_nMonitorStep)) m_dialog.step(item);
                boolean bItemMatches = false;
                for (IField field : m_listFields) {
                    for (String strValue : item.getValue(field)) if (matches(strValue)) {
                        m_listLocalResults.add(item);
                        bItemMatches = true;
                        break;
                    }
                    if (bItemMatches) break;
                }
            }
            try {
                m_barrier.await();
            } catch (InterruptedException e) {
            } catch (BrokenBarrierException e) {
            }
        }

        ;

        public List<IItem> getResults() {
            return m_listLocalResults;
        }
    }

    /**
	 * The current collection
	 */
    protected Collection m_collection;

    protected Composite m_cmpPane;

    protected FormToolkit m_toolkit;

    protected Form m_form;

    protected Text m_txtSearchKeyword;

    protected Section m_sectionFieldsChoice;

    protected Composite m_cmpEmptyCollectionFields;

    protected Map<Collection, Composite> m_mapFieldsChoiceComposites;

    protected ListViewer m_lvResults;

    protected List<IItem> m_listResults;

    /**
	 * The search pattern
	 */
    protected Pattern m_ptnSearch;

    /**
	 * The search worker threads
	 */
    private SearchTask[] m_rgThreads;

    /**
	 * The runnable that is executed when all the search threads have hit the barrier
	 */
    private Runnable m_runBarrier;

    /**
	 * The synchronization object, a barrier
	 */
    private CyclicBarrier m_barrier;

    /**
	 * Creates the SearchView object.
	 */
    public SearchView(Composite cmpParent) {
        m_mapFieldsChoiceComposites = new HashMap<Collection, Composite>();
        m_listResults = new LinkedList<IItem>();
        m_ptnSearch = null;
        m_cmpPane = createUI(cmpParent);
    }

    public void dispose() {
    }

    public String getTitle() {
        return SearchView.getText();
    }

    public Image getImage() {
        return Resources.getImage(Resources.IMAGE_SEARCH);
    }

    public Control getControl() {
        return m_cmpPane;
    }

    private Composite createUI(Composite cmpParent) {
        Composite cmpPane = new Composite(cmpParent, SWT.NONE);
        cmpPane.setLayout(new FillLayout());
        m_toolkit = new FormToolkit(cmpParent.getDisplay());
        m_form = m_toolkit.createForm(cmpPane);
        m_form.getBody().setLayout(new GridLayout(1, false));
        createInputSection();
        createFieldChoiceSection();
        createResultsSection();
        return cmpPane;
    }

    /**
	 * Creates the section containing the input controls for the search.
	 * @param toolkit The form toolkit used to create the controls
	 */
    protected void createInputSection() {
        Section section = m_toolkit.createSection(m_form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
        section.setText(I18n.xl8("Search Keywords"));
        section.setDescription(I18n.xl8("Enter the search keywords"));
        section.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        m_toolkit.createCompositeSeparator(section);
        Composite cmp = m_toolkit.createComposite(section);
        cmp.setLayout(new GridLayout(3, false));
        m_txtSearchKeyword = m_toolkit.createText(cmp, "", SWT.BORDER);
        m_txtSearchKeyword.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1));
        m_txtSearchKeyword.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.character == '\n' || e.character == '\r') doSearch();
            }
        });
        Button btnSearch = m_toolkit.createButton(cmp, I18n.xl8("Search"), SWT.PUSH);
        btnSearch.setImage(Resources.getImage(""));
        btnSearch.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                doSearch();
            }

            public void widgetSelected(SelectionEvent e) {
                doSearch();
            }
        });
        section.setClient(cmp);
    }

    /**
	 * Creates the section containing the controls for choosing the fields to search.
	 */
    protected void createFieldChoiceSection() {
        m_sectionFieldsChoice = m_toolkit.createSection(m_form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE);
        m_sectionFieldsChoice.setText(I18n.xl8("Fields To Search"));
        m_sectionFieldsChoice.setDescription(I18n.xl8("Choose the fields to be searched"));
        m_sectionFieldsChoice.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        m_toolkit.createCompositeSeparator(m_sectionFieldsChoice);
        m_cmpEmptyCollectionFields = m_toolkit.createComposite(m_sectionFieldsChoice);
        m_sectionFieldsChoice.setClient(m_cmpEmptyCollectionFields);
    }

    /**
	 * Creates the section containing the search results table.
	 */
    protected void createResultsSection() {
        final Section section = m_toolkit.createSection(m_form.getBody(), Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        section.setText(I18n.xl8("Search Results"));
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        m_toolkit.createCompositeSeparator(section);
        m_lvResults = new ListViewer(section, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        m_toolkit.adapt(m_lvResults.getControl(), true, true);
        m_lvResults.setContentProvider(new IStructuredContentProvider() {

            @SuppressWarnings("unchecked")
            public Object[] getElements(Object inputElement) {
                return ((List<IItem>) inputElement).toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
        m_lvResults.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                if (element instanceof IItem) return ((IItem) element).getTitle();
                return "";
            }
        });
        m_lvResults.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                MainWindow.getMainWnd().getActiveView().getCollectionView().setSelection((IStructuredSelection) event.getSelection());
            }
        });
        m_lvResults.getList().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == 'a' && e.stateMask == SWT.CONTROL) selectAll();
            }
        });
        m_lvResults.setInput(m_listResults);
        section.setClient(m_lvResults.getControl());
    }

    public void createUIForCollection(ICollection c) {
        m_collection = (Collection) c;
        Composite cmp = c == null ? m_cmpEmptyCollectionFields : m_mapFieldsChoiceComposites.get(c);
        if (cmp == null) {
            cmp = m_toolkit.createComposite(m_sectionFieldsChoice, SWT.NONE);
            cmp.setLayout(new RowLayout(SWT.VERTICAL));
            for (IField f : c.getTemplate()) {
                Button b = m_toolkit.createButton(cmp, f.getName(), SWT.CHECK);
                b.setSelection(true);
                b.setData(f);
            }
            m_mapFieldsChoiceComposites.put(m_collection, cmp);
        }
        m_sectionFieldsChoice.setClient(cmp);
        m_form.layout();
    }

    public void showItem(IItem item, ITemplate template) {
    }

    /**
	 * Selects all the search results.
	 */
    public void selectAll() {
        m_lvResults.getList().selectAll();
        MainWindow.getMainWnd().getActiveView().getCollectionView().setSelection((IStructuredSelection) m_lvResults.getSelection());
    }

    /**
	 * Determines whether the value <code>strValue</code> matches the search string.
	 * @param strValue The value to test
	 * @return <code>true</code> iff the value matches the search string
	 */
    protected boolean matches(String strValue) {
        Matcher m = m_ptnSearch.matcher(strValue);
        return m.find();
    }

    protected void compileSearchPattern() throws PatternSyntaxException {
        try {
            m_ptnSearch = Pattern.compile(StringUtil.wildcard2regex(m_txtSearchKeyword.getText()), Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            throw e;
        }
    }

    /**
	 * Performs the search in the current project.
	 */
    protected void doSearch() {
        String strTitle = I18n.xl8("Searching");
        final ItemProgressDialog dlg = new ItemProgressDialog(MainWindow.getMainWnd().getShell(), strTitle + "...", strTitle);
        dlg.setBlockOnOpen(false);
        dlg.open();
        if (m_collection == null) {
            dlg.setError(I18n.xl8("No collection to search"));
            return;
        }
        Composite cmp = m_mapFieldsChoiceComposites.get(m_collection);
        if (cmp == null) {
            dlg.setError(I18n.xl8("Fields selection not available"));
            return;
        }
        final List<Field> listFields = new LinkedList<Field>();
        for (Control c : cmp.getChildren()) if (c instanceof Button) if (c.getData() instanceof Field) listFields.add((Field) c.getData());
        try {
            compileSearchPattern();
        } catch (PatternSyntaxException e) {
            dlg.setError(e.getMessage());
            return;
        }
        m_listResults.clear();
        m_lvResults.setInput(m_listResults);
        int nTotalItemsCount = m_collection.getItemsCount();
        int nThreadsCount = Math.max(Math.min(Runtime.getRuntime().availableProcessors(), nTotalItemsCount / 100), 1);
        int nItemsPerThreadCount = nTotalItemsCount / nThreadsCount;
        int nMonitorStep = nItemsPerThreadCount > 500 ? 100 : 1;
        m_rgThreads = new SearchTask[nThreadsCount];
        dlg.setMaxSteps(m_collection.getItemsCount() / (nItemsPerThreadCount * nMonitorStep));
        if (m_runBarrier == null) {
            m_runBarrier = new Runnable() {

                public void run() {
                    for (SearchTask thd : m_rgThreads) m_listResults.addAll(thd.getResults());
                    if (m_listResults.isEmpty()) dlg.setError(I18n.xl8("No matching items could be found")); else {
                        dlg.closeUI();
                        Display.getDefault().asyncExec(new Runnable() {

                            public void run() {
                                m_lvResults.setInput(m_listResults);
                                m_lvResults.setSelection(new SingleItemSelection(m_listResults.get(0)));
                            }
                        });
                    }
                }
            };
        }
        m_barrier = new CyclicBarrier(nThreadsCount, m_runBarrier);
        for (int i = 0; i < nThreadsCount; i++) {
            m_rgThreads[i] = new SearchTask(dlg, listFields, i * nItemsPerThreadCount, Math.min((i + 1) * nItemsPerThreadCount - 1, nTotalItemsCount - 1), nMonitorStep);
            Application.getExecutorService().submit(m_rgThreads[i]);
        }
    }

    public void expansionStateChanged(ExpansionEvent e) {
    }

    public void expansionStateChanging(ExpansionEvent e) {
    }
}
