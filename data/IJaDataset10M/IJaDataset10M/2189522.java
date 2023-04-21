package org.gems.designer;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.gems.designer.model.actions.ConstraintEventInterest;
import org.gems.designer.model.actions.EventInterestFactory;
import org.gems.designer.model.actions.EventInterestFactoryRepository;
import org.gems.designer.model.actions.HiddenEventInterest;
import org.gems.designer.model.actions.HiddenEventInterestFactory;
import org.gems.designer.model.actions.ModelAction;
import org.gems.designer.model.actions.ModelActionRegistry;
import org.gems.designer.model.actions.ModelEventInterest;
import org.gems.designer.model.actions.PersistentModelEventInterest;
import org.gems.designer.model.meta.MetaInformation;

public class ActionsetsConfigurator extends WizardPage {

    private class TableRemover implements Listener {

        private Table table_;

        private TableItem item_;

        private TableEditorManager manager_;

        public TableRemover(Table t, TableItem item, TableEditorManager mgr) {
            table_ = t;
            item_ = item;
            manager_ = mgr;
        }

        public void handleEvent(Event event) {
            TableItem[] items = table_.getItems();
            int ditch = -1;
            List<ModelEventInterest> interests = model_.getEventInterests();
            for (int i = 0; i < items.length; i++) {
                if (items[i] == item_) {
                    model_.removeEventInterest(interests.get(i));
                    break;
                }
            }
            manager_.disposeAll();
            manager_.clearAll();
            table_.removeAll();
            interests = model_.getEventInterests();
            for (ModelEventInterest intr : interests) if (intr instanceof PersistentModelEventInterest) createEventEntry((PersistentModelEventInterest) intr);
        }
    }

    private class TableEditorManager {

        private List<TableEditor> editors_ = new LinkedList<TableEditor>();

        private List<Control> controls_ = new LinkedList<Control>();

        public TableEditorManager() {
        }

        public void clearAll() {
            editors_.clear();
            controls_.clear();
        }

        public void add(TableEditor t, Control c) {
            editors_.add(t);
            controls_.add(c);
        }

        public void remove(TableEditor t, Control c) {
            editors_.remove(t);
            controls_.remove(c);
        }

        public void disposeAll() {
            for (Control c : controls_) c.dispose();
            for (TableEditor e : editors_) {
                e.dispose();
            }
        }
    }

    private class EnableListener implements MouseListener {

        public PersistentModelEventInterest pei;

        public void mouseUp(MouseEvent e) {
        }

        public void mouseDown(MouseEvent e) {
            pei.setEnabled(!pei.isEnabled());
        }

        public void mouseDoubleClick(MouseEvent e) {
        }
    }

    private class EditorButtonListener implements Listener {

        private PersistentModelEventInterest interest_;

        private int row_;

        public EditorButtonListener(Button b, PersistentModelEventInterest interest, int row) {
            interest_ = interest;
            row_ = row;
            b.addListener(SWT.MouseDown, this);
        }

        public void handleEvent(Event event) {
            EventInterestFactory fact = getFactory(interest_);
            fact.reConfigureInterest(cont_, model_, interest_);
            updateEventEntry(interest_, row_);
        }
    }

    private Label classNameLabel_;

    private Label menuItemLabel_;

    private Text menuItemName_;

    private Text className_;

    private String cName_;

    private String mName_;

    private EventInterestFactory currentFactory_;

    private boolean generateVisitor_ = false;

    private ModelInstance model_;

    private ModelAction[] actions_;

    private Table eventsTable_;

    private Combo factoriesCombo_;

    private EventInterestFactory[] factories_;

    private ModelEventInterest events_;

    private Composite cont_;

    private TableEditorManager editorManager_ = new TableEditorManager();

    public ActionsetsConfigurator(String pageName, String title, ImageDescriptor titleImage) {
        super(pageName, title, titleImage);
    }

    public ActionsetsConfigurator(String pageName) {
        super(pageName);
    }

    public EventInterestFactory getFactory(PersistentModelEventInterest pei) {
        for (EventInterestFactory fact : factories_) {
            if (fact.canConfigure(pei)) {
                return fact;
            }
        }
        return null;
    }

    public void createControl(Composite parent) {
        parent.setSize(600, 600);
        List<ModelEventInterest> interests = model_.getEventInterests();
        List<ModelEventInterest> ditch = new LinkedList<ModelEventInterest>();
        ditch.addAll(interests);
        for (ModelEventInterest i : ditch) {
            if (i instanceof HiddenEventInterest) {
                interests.remove(i);
            }
        }
        List<EventInterestFactory> factories = EventInterestFactoryRepository.getInstance().getEventInterestFactories(model_.getModelID());
        List<EventInterestFactory> toremove = new LinkedList<EventInterestFactory>();
        toremove.addAll(factories);
        for (EventInterestFactory f : toremove) {
            if (f instanceof HiddenEventInterestFactory) {
                factories.remove(f);
            }
        }
        factories_ = factories.toArray(new EventInterestFactory[0]);
        GridData gd = new GridData();
        gd.horizontalAlignment = GridData.BEGINNING;
        gd.widthHint = 25;
        Composite composite = new Composite(parent, SWT.NULL);
        cont_ = composite;
        GridLayout gl = new GridLayout();
        int ncol = 8;
        gl.numColumns = ncol;
        composite.setLayout(gl);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = ncol;
        Table table = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setSize(400, 400);
        eventsTable_ = table;
        String[] titles = { " ", "ID", "Action Type", "Enabled", "Description", "Remove" };
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(titles[i]);
        }
        int row = 0;
        for (ModelEventInterest interest : interests) {
            if (!(interest instanceof PersistentModelEventInterest)) continue;
            MetaInformation meta = model_.getMetaInformation();
            if (interest instanceof ConstraintEventInterest) {
                ConstraintEventInterest ce = (ConstraintEventInterest) interest;
                ce.setMetaInformation(meta);
            }
            PersistentModelEventInterest pei = (PersistentModelEventInterest) interest;
            createEventEntry(pei);
            row++;
        }
        for (int i = 0; i < titles.length; i++) {
            table.getColumn(i).pack();
        }
        table.setSize(table.computeSize(SWT.DEFAULT, 200));
        table.setLayoutData(gd);
        createLine(composite, ncol);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        Button addact = new Button(composite, SWT.NONE);
        addact.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                int index = factoriesCombo_.getSelectionIndex();
                if (index > -1 && index < factories_.length) {
                    PersistentModelEventInterest pei = factories_[index].createInterest(factoriesCombo_.getParent(), model_);
                    if (pei != null) {
                        createEventEntry(pei);
                        ModelActionRegistry reg = getActionRegistry(model_);
                        if (reg == null) {
                            reg = new ModelActionRegistry();
                            model_.addInstanceListener(reg);
                        }
                        reg.addInterest(pei);
                    }
                }
            }
        });
        addact.setText("Add");
        addact.setLayoutData(gd);
        gd.horizontalSpan = 2;
        Combo combo = new Combo(composite, SWT.READ_ONLY);
        combo.setLayoutData(gd);
        factoriesCombo_ = combo;
        for (EventInterestFactory factory : factories) combo.add(factory.getName());
        setControl(composite);
        getSavedData();
    }

    private void addListeners() {
    }

    public ModelActionRegistry getActionRegistry(ModelInstance inst) {
        for (InstanceListener l : inst.getInstanceListeners()) {
            if (l instanceof ModelActionRegistry) {
                return (ModelActionRegistry) l;
            }
        }
        return null;
    }

    public void handleEvent(Event event) {
        Status status = new Status(IStatus.OK, "not_used", 0, "", null);
    }

    /**
	 * @see IWizardPage#canFlipToNextPage()
	 */
    public boolean canFlipToNextPage() {
        cName_ = className_.getText();
        mName_ = menuItemName_.getText();
        return true;
    }

    public boolean validateData() {
        return true;
    }

    public void getSavedData() {
    }

    public void saveDataToModel() {
        cName_ = className_.getText();
        mName_ = menuItemName_.getText();
    }

    /**
	 * Applies the status to the status line of a dialog page.
	 */
    private void applyToStatusLine(IStatus status) {
        String message = status.getMessage();
        if (message.length() == 0) message = null;
        switch(status.getSeverity()) {
            case IStatus.OK:
                setErrorMessage(null);
                setMessage(message);
                break;
            case IStatus.WARNING:
                setErrorMessage(null);
                setMessage(message, WizardPage.WARNING);
                break;
            case IStatus.INFO:
                setErrorMessage(null);
                setMessage(message, WizardPage.INFORMATION);
                break;
            default:
                setErrorMessage(message);
                setMessage(null);
                break;
        }
    }

    private void createLine(Composite parent, int ncol) {
        Label line = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BOLD);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = ncol;
        line.setLayoutData(gridData);
    }

    public boolean generateVisitor() {
        return generateVisitor_;
    }

    public String getVisitorClassName() {
        return cName_;
    }

    public String getMenuItemName() {
        return mName_;
    }

    public ModelInstance getModel() {
        return model_;
    }

    public void setModel(ModelInstance model) {
        this.model_ = model;
    }

    public void createEventEntry(PersistentModelEventInterest pei) {
        TableItem item = new TableItem(eventsTable_, SWT.NONE);
        EventInterestFactory fact = EventInterestFactoryRepository.getInstance().getFactoryByID(pei.getFactoryID());
        item.setText(1, pei.getID());
        item.setText(2, fact.getName());
        item.setText(4, pei.getDescription());
        TableEditor editor = new TableEditor(eventsTable_);
        Button button = new Button(eventsTable_, SWT.CHECK);
        button.pack();
        editor.minimumWidth = button.getSize().x;
        editor.horizontalAlignment = SWT.LEFT;
        editor.setEditor(button, item, 3);
        editorManager_.add(editor, button);
        if (pei.isEnabled()) button.setSelection(true);
        EnableListener l = new EnableListener();
        l.pei = pei;
        button.addMouseListener(l);
        editor = new TableEditor(eventsTable_);
        Button buttona = new Button(eventsTable_, SWT.NONE);
        new EditorButtonListener(buttona, pei, eventsTable_.getItemCount() - 1);
        buttona.setText(">");
        button.pack();
        editor.minimumWidth = button.getSize().x;
        editor.horizontalAlignment = SWT.LEFT;
        editor.setEditor(buttona, item, 0);
        editorManager_.add(editor, buttona);
        TableEditor editor2 = new TableEditor(eventsTable_);
        Button remove = new Button(eventsTable_, SWT.PUSH);
        remove.setText("Delete");
        remove.pack();
        editor2.minimumWidth = remove.getSize().x;
        editor2.horizontalAlignment = SWT.LEFT;
        editor2.setEditor(remove, item, 5);
        editorManager_.add(editor2, remove);
        remove.addListener(SWT.MouseDown, new TableRemover(eventsTable_, item, editorManager_));
    }

    public void updateEventEntry(PersistentModelEventInterest pei, int row) {
        TableItem item = eventsTable_.getItem(row);
        item.setText(1, pei.getID());
        item.setText(2, getFactory(pei).getName());
        item.setText(4, pei.getDescription());
    }
}
