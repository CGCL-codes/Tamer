package net.taylor.mda.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.uml2.common.edit.command.ChangeCommand;
import org.eclipse.uml2.common.edit.provider.IItemQualifiedTextProvider;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.util.UMLSwitch;

/**
 * The section used to Apply/Unapply a Profile on a given Element
 */
public class ProfilesSection extends AbstractPropertySection {

    /**
	 * The current selected object or the first object in the selection when
	 * multiple objects are selected.
	 */
    private EObject eObject;

    private ILabelProvider labelProvider;

    private TableViewer availableElementsTableViewer;

    private TableViewer selectedElementsTableViewer;

    private Button addButton;

    private Button removeButton;

    private Button upButton;

    private Button downButton;

    private IDoubleClickListener availableElementsTableDoubleClickListener = new IDoubleClickListener() {

        public void doubleClick(DoubleClickEvent event) {
            if (addButton.isEnabled()) {
                addButton.notifyListeners(SWT.Selection, null);
            }
        }
    };

    private IDoubleClickListener selectedElementsTableDoubleClickListener = new IDoubleClickListener() {

        public void doubleClick(DoubleClickEvent event) {
            if (removeButton.isEnabled()) {
                removeButton.notifyListeners(SWT.Selection, null);
            }
        }
    };

    private SelectionAdapter addButtonSelectionAdapter = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            if (availableElementsTableViewer != null) {
                final List newElements = new ArrayList();
                IStructuredSelection selection = (IStructuredSelection) availableElementsTableViewer.getSelection();
                for (Iterator i = selection.iterator(); i.hasNext(); ) {
                    Object value = i.next();
                    ((ItemProvider) selectedElementsTableViewer.getInput()).getChildren().add(value);
                    ((ItemProvider) availableElementsTableViewer.getInput()).getChildren().remove(value);
                    newElements.add(value);
                }
                getEditingDomain().getCommandStack().execute(new ChangeCommand(getEditingDomain(), new Runnable() {

                    public void run() {
                        for (Iterator itNewElements = newElements.iterator(); itNewElements.hasNext(); ) {
                            ((Package) eObject).applyProfile((Profile) itNewElements.next());
                        }
                    }
                }, ""));
            }
        }
    };

    private SelectionAdapter removeButtonSelectionAdapter = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            IStructuredSelection selection = (IStructuredSelection) selectedElementsTableViewer.getSelection();
            Object firstValue = null;
            final List oldElements = new ArrayList();
            for (Iterator i = selection.iterator(); i.hasNext(); ) {
                Object value = i.next();
                if (firstValue == null) {
                    firstValue = value;
                }
                ((ItemProvider) availableElementsTableViewer.getInput()).getChildren().add(value);
                ((ItemProvider) selectedElementsTableViewer.getInput()).getChildren().remove(value);
                oldElements.add(value);
            }
            getEditingDomain().getCommandStack().execute(new ChangeCommand(getEditingDomain(), new Runnable() {

                public void run() {
                    for (Iterator itOldElements = oldElements.iterator(); itOldElements.hasNext(); ) {
                        ((Package) eObject).unapplyProfile((Profile) itOldElements.next());
                    }
                }
            }, ""));
        }
    };

    private SelectionAdapter upButtonSelectionAdapter = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            IStructuredSelection selection = (IStructuredSelection) selectedElementsTableViewer.getSelection();
            int minIndex = 0;
            for (Iterator i = selection.iterator(); i.hasNext(); ) {
                Object value = i.next();
                int index = ((ItemProvider) selectedElementsTableViewer.getInput()).getChildren().indexOf(value);
                ((ItemProvider) selectedElementsTableViewer.getInput()).getChildren().move(Math.max(index - 1, minIndex++), value);
            }
        }
    };

    private SelectionAdapter downButtonSelectionAdapter = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            IStructuredSelection selection = (IStructuredSelection) selectedElementsTableViewer.getSelection();
            int maxIndex = ((ItemProvider) selectedElementsTableViewer.getInput()).getChildren().size() - selection.size();
            for (Iterator i = selection.iterator(); i.hasNext(); ) {
                Object value = i.next();
                int index = ((ItemProvider) selectedElementsTableViewer.getInput()).getChildren().indexOf(value);
                ((ItemProvider) selectedElementsTableViewer.getInput()).getChildren().move(Math.min(index + 1, maxIndex++), value);
            }
        }
    };

    /**
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        composite.setLayout(new GridLayout(3, false));
        Composite choiceComposite = getWidgetFactory().createComposite(composite, SWT.NONE);
        choiceComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        choiceComposite.setLayout(new GridLayout());
        Label choiceLabel = getWidgetFactory().createLabel(choiceComposite, "Available Profiles", SWT.NONE);
        choiceLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final Table choiceTable = getWidgetFactory().createTable(choiceComposite, SWT.MULTI | SWT.BORDER);
        choiceTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        availableElementsTableViewer = new TableViewer(choiceTable);
        Composite controlButtons = getWidgetFactory().createComposite(composite, SWT.NONE);
        controlButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        GridLayout controlsButtonGridLayout = new GridLayout();
        controlButtons.setLayout(controlsButtonGridLayout);
        new Label(controlButtons, SWT.NONE);
        addButton = getWidgetFactory().createButton(controlButtons, "Add >", SWT.PUSH);
        addButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        removeButton = getWidgetFactory().createButton(controlButtons, "< Remove", SWT.PUSH);
        removeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        Label spaceLabel = new Label(controlButtons, SWT.NONE);
        GridData spaceLabelGridData = new GridData();
        spaceLabelGridData.verticalSpan = 2;
        spaceLabel.setLayoutData(spaceLabelGridData);
        upButton = getWidgetFactory().createButton(controlButtons, "Up", SWT.PUSH);
        upButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        downButton = getWidgetFactory().createButton(controlButtons, "Down", SWT.PUSH);
        downButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        Composite featureComposite = getWidgetFactory().createComposite(composite, SWT.NONE);
        featureComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        featureComposite.setLayout(new GridLayout());
        Label featureLabel = getWidgetFactory().createLabel(featureComposite, "Applied Profiles", SWT.NONE);
        featureLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        final Table featureTable = getWidgetFactory().createTable(featureComposite, SWT.MULTI | SWT.BORDER);
        featureTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        selectedElementsTableViewer = new TableViewer(featureTable);
    }

    /**
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#setInput(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        Object ssel = ((IStructuredSelection) selection).getFirstElement();
        eObject = Activator.adaptObject(ssel);
        AdapterFactory adapterFactory = new ComposedAdapterFactory(Collections.EMPTY_LIST);
        availableElementsTableViewer.setLabelProvider(getLabelProvider());
        availableElementsTableViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
        availableElementsTableViewer.setInput(new ItemProvider(adapterFactory, getChoiceOfValues()));
        AdapterFactory adapterFactory2 = new ComposedAdapterFactory(Collections.EMPTY_LIST);
        selectedElementsTableViewer.setLabelProvider(getLabelProvider());
        selectedElementsTableViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory2));
        selectedElementsTableViewer.setInput(new ItemProvider(adapterFactory2, getAppliedProfiles()));
    }

    /**
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#aboutToBeShown()
	 */
    public void aboutToBeShown() {
        availableElementsTableViewer.addDoubleClickListener(availableElementsTableDoubleClickListener);
        selectedElementsTableViewer.addDoubleClickListener(selectedElementsTableDoubleClickListener);
        addButton.addSelectionListener(addButtonSelectionAdapter);
        removeButton.addSelectionListener(removeButtonSelectionAdapter);
        upButton.addSelectionListener(upButtonSelectionAdapter);
        downButton.addSelectionListener(downButtonSelectionAdapter);
    }

    /**
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#aboutToBeHidden()
	 */
    public void aboutToBeHidden() {
        availableElementsTableViewer.removeDoubleClickListener(availableElementsTableDoubleClickListener);
        selectedElementsTableViewer.removeDoubleClickListener(selectedElementsTableDoubleClickListener);
        if (!addButton.isDisposed()) {
            addButton.removeSelectionListener(addButtonSelectionAdapter);
            removeButton.removeSelectionListener(removeButtonSelectionAdapter);
            upButton.removeSelectionListener(upButtonSelectionAdapter);
            downButton.removeSelectionListener(downButtonSelectionAdapter);
        }
    }

    /**
	 * Obtains the currently active workbench page.
	 * 
	 * @return the active page, or <code>null</code> if none is active
	 */
    public IWorkbenchPage getActivePage() {
        IWorkbenchPage result = null;
        IWorkbench bench = PlatformUI.getWorkbench();
        if (bench != null) {
            IWorkbenchWindow window = bench.getActiveWorkbenchWindow();
            if (window != null) {
                result = window.getActivePage();
            }
        }
        return result;
    }

    protected TransactionalEditingDomain getEditingDomain() {
        return TransactionUtil.getEditingDomain(eObject);
    }

    /**
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
	 */
    public boolean shouldUseExtraSpace() {
        return true;
    }

    /**
	 * Get the LabelProvider to use to display the Object
	 * 
	 * @return ILabelProvider
	 */
    protected ILabelProvider getLabelProvider() {
        if (labelProvider == null) {
            labelProvider = new AdapterFactoryLabelProvider(Activator.getDefault().getItemProvidersAdapterFactory()) {

                public String getColumnText(Object object, int columnIndex) {
                    IItemQualifiedTextProvider itemQualifiedTextProvider = (IItemQualifiedTextProvider) adapterFactory.adapt(object, IItemQualifiedTextProvider.class);
                    return itemQualifiedTextProvider != null ? itemQualifiedTextProvider.getQualifiedText(object) : super.getColumnText(object, columnIndex);
                }

                public String getText(Object object) {
                    IItemQualifiedTextProvider itemQualifiedTextProvider = (IItemQualifiedTextProvider) adapterFactory.adapt(object, IItemQualifiedTextProvider.class);
                    return itemQualifiedTextProvider != null ? itemQualifiedTextProvider.getQualifiedText(object) : super.getText(object);
                }
            };
        }
        return labelProvider;
    }

    /**
	 * Get the list of available Profiles for the current selected Element, then
	 * we remove those which are already applied.
	 * 
	 * @return List the list of Profiles that can be applied
	 */
    protected List getChoiceOfValues() {
        final List choiceOfValues = new ArrayList();
        Resource eResource = eObject.eResource();
        ResourceSet resourceSet = eResource == null ? null : eResource.getResourceSet();
        if (resourceSet != null) {
            try {
                resourceSet.getResource(URI.createURI(UMLResource.STANDARD_PROFILE_URI), true);
                resourceSet.getResource(URI.createURI(UMLResource.ECORE_PROFILE_URI), true);
            } catch (Exception e) {
            }
            for (Iterator resources = resourceSet.getResources().iterator(); resources.hasNext(); ) {
                Iterator allContents = ((Resource) resources.next()).getAllContents();
                while (allContents.hasNext()) {
                    new UMLSwitch() {

                        public Object caseProfile(Profile profile) {
                            if (profile.isDefined()) {
                                ProfileApplication profileApplication = ((Package) eObject).getProfileApplication(profile);
                                if (profileApplication == null || profileApplication.getAppliedDefinition() != profile.getDefinition()) {
                                    choiceOfValues.add(profile);
                                }
                            }
                            return profile;
                        }
                    }.doSwitch((EObject) allContents.next());
                }
            }
        }
        return choiceOfValues;
    }

    /**
	 * Get the list of stereotypes that are applied on the current selected
	 * Element
	 * 
	 * @return List the list of Profiles already applied
	 */
    protected List getAppliedProfiles() {
        List choiceOfValues = new ArrayList(((Package) eObject).getAppliedProfiles());
        return choiceOfValues;
    }
}
