package eu.redseeds.sc.editor.rsl.editors.domain.controls;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import eu.redseeds.sc.current.repository.SCProjectContainer;
import eu.redseeds.sc.editor.rsl.editors.domain.controls.views.TermsContentProvider;
import eu.redseeds.sc.editor.rsl.editors.domain.controls.views.TermsDecoratingLabelProvider;
import eu.redseeds.sc.editor.rsl.editors.domain.controls.views.TermsLabelProvider;
import eu.redseeds.scl.model.BusinessLayerFacade;
import eu.redseeds.scl.model.rsl.rsldomainelements.phrases.PhraseDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.terms.TermDTO;
import eu.redseeds.scl.sclkernel.SCLElement;

public class TerminologyWidget extends Composite {

    public static final String[] columnNamesStatements = new String[] { "nazwa" };

    private TermDTO term;

    private Table table;

    private TableViewer sensesTableViewer;

    private Button assignButton, addButton;

    private PhraseDTO phrase;

    private boolean isWizard = false;

    private boolean isClipboard = false;

    public TerminologyWidget(Composite parent, TermDTO term) {
        super(parent, SWT.NONE);
        this.term = term;
        createContent();
    }

    private ISelectionChangedListener selListener = new ISelectionChangedListener() {

        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            if (isWizard) {
                assignButton.setEnabled(false);
                if (table.getSelectionCount() == 1) {
                    if (term.getSynonymUid() == 0) assignButton.setEnabled(true);
                }
                addButton.setEnabled(false);
                if (term.getSynonymUid() == 0 && !term.getName().equalsIgnoreCase("")) addButton.setEnabled(true);
            }
            if (!isWizard) {
                if (table.getSelectionCount() == 1) {
                    assignButton.setEnabled(true);
                } else assignButton.setEnabled(false);
            }
            if (isClipboard) disableElements();
        }
    };

    private void createContent() {
        table = new Table(this, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setBounds(0, 10, 520, 100);
        TableColumn column = new TableColumn(table, SWT.LEFT, 0);
        column.setWidth(66);
        column.setText("Term type");
        column = new TableColumn(table, SWT.LEFT, 1);
        column.setWidth(70);
        column.setText("Basic form");
        column = new TableColumn(table, SWT.LEFT, 2);
        column.setWidth(301);
        column.setText("Term senses");
        sensesTableViewer = new TableViewer(table);
        sensesTableViewer.setUseHashlookup(true);
        sensesTableViewer.setColumnProperties(columnNamesStatements);
        sensesTableViewer.setContentProvider(new TermsContentProvider());
        sensesTableViewer.setLabelProvider(new TermsDecoratingLabelProvider(new TermsLabelProvider(), null, term));
        sensesTableViewer.setInput(term);
        sensesTableViewer.addSelectionChangedListener(selListener);
        assignButton = new Button(this, SWT.None);
        assignButton.setText("Assign");
        assignButton.setEnabled(false);
        assignButton.addSelectionListener(new AssignSenseListener(this));
        assignButton.setBounds(530, 10, 60, 20);
        addButton = new Button(this, SWT.None);
        addButton.setText("Add sense");
        addButton.addSelectionListener(new AddSenseListener(this));
        addButton.setBounds(530, 35, 60, 20);
        disableElements();
    }

    @Override
    public void setEnabled(boolean enable) {
        table.setEnabled(enable);
        if (isWizard) {
            if (!enable) {
                table.setEnabled(enable);
                assignButton.setEnabled(enable);
                addButton.setEnabled(enable);
            } else {
                assignButton.setEnabled(false);
                if (table.getSelectionCount() == 1) {
                    if (term.getSynonymUid() == 0) assignButton.setEnabled(true);
                }
                addButton.setEnabled(false);
                if (term.getSynonymUid() == 0 && !term.getName().equalsIgnoreCase("")) addButton.setEnabled(true);
            }
        }
        if (!isWizard) {
            if (table.getSelectionCount() == 1) {
                assignButton.setEnabled(true);
            } else assignButton.setEnabled(false);
            addButton.setEnabled(enable);
        }
        if (isClipboard) disableElements();
    }

    public void setButtonsEnabled(boolean enable) {
        if (isWizard) {
            assignButton.setEnabled(false);
            if (table.getSelectionCount() == 1) {
                if (term.getSynonymUid() == 0) assignButton.setEnabled(true);
            }
            addButton.setEnabled(false);
            if (term.getSynonymUid() == 0 && !term.getName().equalsIgnoreCase("")) addButton.setEnabled(true);
        }
        if (!isWizard) {
            if (table.getSelectionCount() == 1) {
                assignButton.setEnabled(true);
            } else assignButton.setEnabled(false);
            addButton.setEnabled(enable);
        }
        if (isClipboard) disableElements();
    }

    public TermDTO getTerm() {
        return term;
    }

    public void setTerm(TermDTO term) {
        this.term = term;
        sensesTableViewer.setLabelProvider(new TermsDecoratingLabelProvider(new TermsLabelProvider(), null, term));
        sensesTableViewer.setInput(term);
        checkClipboard();
        disableElements();
    }

    public void refresh() {
        if (sensesTableViewer != null) {
            sensesTableViewer.refresh();
        }
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public PhraseDTO getPhrase() {
        return phrase;
    }

    public void setPhrase(PhraseDTO phrase) {
        this.phrase = phrase;
        checkClipboard();
        disableElements();
    }

    public void addAssignSenseListener(SelectionListener externalListener) {
        assignButton.addSelectionListener(externalListener);
    }

    public boolean isWizard() {
        return isWizard;
    }

    public void setWizard(boolean isWizard) {
        this.isWizard = isWizard;
    }

    private void disableElements() {
        if (assignButton != null) assignButton.setEnabled(false);
    }

    private void checkClipboard() {
        SCLElement sclElement = null;
        if (phrase != null) {
            sclElement = (SCLElement) phrase;
        } else if (term != null) sclElement = (SCLElement) term;
        if (sclElement != null) {
            BusinessLayerFacade facade = SCProjectContainer.instance().getSCProject(sclElement).getBusinessLayerFacade();
            if (facade.isAnyClipboardMember(sclElement)) {
                isClipboard = true;
            } else isClipboard = false;
        }
    }
}
