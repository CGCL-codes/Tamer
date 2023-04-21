package es.ulpgc.dis.heuristicide.rcp.ui.population;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import es.ulpgc.dis.heuriskein.model.solver.IndividualInitializer;
import es.ulpgc.dis.heuriskein.model.solver.Population;
import es.ulpgc.dis.heuristicide.control.LoadSaveController;
import es.ulpgc.dis.heuristicide.project.ConfigurationManager;
import es.ulpgc.dis.heuristicide.project.INameable;
import es.ulpgc.dis.heuristicide.rcp.HeuriskeinApplication;
import es.ulpgc.dis.heuristicide.rcp.HeuriskeinPlugin;
import es.ulpgc.dis.heuristicide.rcp.ui.commons.MessageError;

public class PopulationEditor extends EditorPart implements Observer {

    private MenuManager menuManager;

    public static final String ID = "Heuriskein.Population";

    private Composite composite;

    private Text popName;

    private Text popSize;

    private Text popType;

    private Population population;

    private Combo combo;

    private Boolean dirty = false;

    public void createPartControl(Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        GridLayout layout;
        GridData td;
        td = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(td);
        layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);
        createGeneralView();
        population.addObserver(this);
        createPopupMenu(composite);
        getEditorSite().getWorkbenchWindow().getWorkbench().getHelpSystem().setHelp(composite, "Heuriskein.populationEditor");
    }

    private void createGeneralView() {
        GridData td;
        Label nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setText("Population Name");
        popName = new Text(composite, SWT.BORDER);
        popName.setText(population.getName());
        popName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        td = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        td.horizontalSpan = 2;
        popName.setLayoutData(td);
        popName.setEnabled(false);
        Label popTypeLabel = new Label(composite, SWT.NONE);
        popTypeLabel.setText("Population Type");
        popType = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.FLAT);
        if (population.isInitialized()) {
            popType.setText(ConfigurationManager.getRepresentation(population.getType()));
        } else {
            popType.setText("Not defined yet");
        }
        td = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        td.horizontalSpan = 2;
        popType.setLayoutData(td);
        Label popSizeLabel = new Label(composite, SWT.NONE);
        popSizeLabel.setText("Population Size");
        popSize = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
        popSize.setText(String.valueOf(population.getPopulationSize()));
        td = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        td.horizontalSpan = 2;
        td.minimumWidth = 100;
        td.widthHint = 100;
        popSize.setLayoutData(td);
    }

    protected void initializePopulation() {
        if (isDirty()) {
            if (!MessageDialog.openConfirm(composite.getShell(), "Confirm", "Population has changed or is already initialized.\n Do you want to continue?")) {
                return;
            }
        }
        IndividualInitializer individualInitializer = ConfigurationManager.getIndividualInitializer(getSelectedInitializer());
        if (individualInitializer == null) {
            MessageDialog.openError(composite.getShell(), "Error", "Select an initializer first");
        } else {
            try {
                population.clear();
                population.setPopulationSize(Integer.valueOf(popSize.getText()));
                population.setInitializer(individualInitializer);
                individualInitializer.initialize(population);
                popType.setText(population.get(0).getClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dirty = true;
        firePropertyChange(PROP_DIRTY);
    }

    public void changePopulationSize() {
        population.setPopulationSize((Integer.valueOf(popSize.getText())).intValue());
        dirty = true;
        firePropertyChange(PROP_DIRTY);
    }

    protected void dialogChanged() {
    }

    public void setFocus() {
        composite.setFocus();
    }

    public void update(Observable o, Object arg) {
        popName.setText(population.getName());
        popSize.setText(String.valueOf(population.getPopulationSize()));
        popType.setText(ConfigurationManager.getRepresentation(population.getType()));
        composite.layout();
        popType.setSize(popType.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        setPartName(population.getName());
        dirty = true;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        dirty = false;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    public void doSaveAs() {
        dirty = false;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        try {
            population = (Population) input.getAdapter(Class.forName("es.ulpgc.dis.heuriskein.model.solver.Population"));
            setPartName(population.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    public String getSelectedInitializer() {
        return combo.getText();
    }

    public Population getPopulation() {
        return population;
    }

    public void createPopupMenu(Composite parent) {
        menuManager = new MenuManager("#PopupMenu");
        ((PopulationContributor) (getEditorSite().getActionBarContributor())).fillContextMenu(menuManager);
        parent.setMenu(menuManager.createContextMenu(parent));
    }

    public Object getAdapter(Class className) {
        if (className == INameable.class) {
            return population;
        }
        return null;
    }

    public void dispose() {
        population.deleteObserver(this);
        super.dispose();
    }
}
