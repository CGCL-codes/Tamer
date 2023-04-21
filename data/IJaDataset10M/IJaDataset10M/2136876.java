package com.halware.nakedide.eclipse.ext.annot.action;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.halware.eclipseutil.util.Generics;
import com.halware.eclipseutil.util.SelectionUtil;
import com.halware.nakedide.eclipse.ext.Activator;
import com.halware.nakedide.eclipse.ext.annot.action.params.NakedObjectActionParameter;
import com.halware.nakedide.eclipse.ext.annot.action.params.NakedObjectActionParametersCellModifier;
import com.halware.nakedide.eclipse.ext.annot.action.params.NakedObjectActionParametersContentProvider;
import com.halware.nakedide.eclipse.ext.annot.action.params.NakedObjectActionParametersLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNode;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeView;
import com.halware.nakedide.eclipse.ext.annot.mdd.DelegatingMetadataDescriptorSetOwner;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;

public class NakedObjectActionsView extends AbstractNodeView {

    public static final String ID = NakedObjectActionsView.class.getCanonicalName();

    private static final Logger LOGGER = Logger.getLogger(NakedObjectActionsView.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    public NakedObjectActionsView() {
        super(XP_ACTIONS_METADATA_DESCRIPTORS_PROVIDER);
    }

    private static final String XP_ACTIONS_METADATA_DESCRIPTORS_PROVIDER = Activator.getPluginId() + ".actionsMetadataDescriptorsProviders";

    private static final String XP_ACTION_PARAMETERS_METADATA_DESCRIPTORS_PROVIDER = Activator.getPluginId() + ".actionParametersMetadataDescriptorsProviders";

    private TableViewer paramsViewer;

    /**
	 * Create the GUI and actions, set up providers and initial input.
	 */
    public void createPartControl(Composite parent) {
        SashForm sash = new SashForm(parent, SWT.VERTICAL | SWT.SMOOTH);
        getLOGGER().info("createPartControl: started");
        try {
            super.createPartControl(sash);
            createParamsViewer(sash);
            configureParamsViewer();
            paramsViewer.setContentProvider(new NakedObjectActionParametersContentProvider());
        } finally {
            getLOGGER().info("createPartControl: completed");
        }
    }

    private TableViewer createParamsViewer(SashForm sash) {
        ViewForm trayForm = new ViewForm(sash, SWT.NONE);
        Label label = new Label(trayForm, SWT.NONE);
        label.setText("Parameters:");
        trayForm.setTopLeft(label);
        paramsViewer = createViewer(trayForm);
        trayForm.setContent(paramsViewer.getTable());
        return paramsViewer;
    }

    private void hookSelectionListener() {
        getTableViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                Object singleSelection = SelectionUtil.singleSelection(event.getSelection());
                getLOGGER().debug("selected " + singleSelection);
                paramsViewer.setInput(singleSelection);
            }
        });
    }

    private MetadataDescriptorSet configureParamsViewer() {
        MetadataDescriptorSet metadataDescriptorSet = createParamsPropertyDescriptors();
        paramsViewer.setCellModifier(metadataDescriptorSet.getCellModifier());
        paramsViewer.setCellEditors(metadataDescriptorSet.getCellEditors());
        paramsViewer.setColumnProperties(metadataDescriptorSet.getColumnProperties());
        paramsViewer.setLabelProvider(metadataDescriptorSet.getLabelProvider());
        hookSelectionListener();
        return metadataDescriptorSet;
    }

    private MetadataDescriptorSet createParamsPropertyDescriptors() {
        MetadataDescriptorSet metadataDescriptorSet = new MetadataDescriptorSet(new DelegatingMetadataDescriptorSetOwner(this) {

            private ICellModifier cellModifier;

            public <T extends ICellModifier> T getCellModifier(MetadataDescriptorSet set) {
                if (cellModifier == null) {
                    cellModifier = new NakedObjectActionParametersCellModifier(set);
                }
                return Generics.asT(cellModifier);
            }

            private IBaseLabelProvider labelProvider;

            public <T extends IBaseLabelProvider> T getLabelProvider(MetadataDescriptorSet set) {
                if (labelProvider == null) {
                    labelProvider = new NakedObjectActionParametersLabelProvider(set);
                }
                return Generics.asT(labelProvider);
            }

            public TableViewer getTableViewer() {
                return paramsViewer;
            }
        });
        for (IMetadataDescriptorsProvider provider : loadMetadataDescriptorProviders(XP_ACTION_PARAMETERS_METADATA_DESCRIPTORS_PROVIDER)) {
            metadataDescriptorSet.add(provider);
        }
        return metadataDescriptorSet;
    }

    public void resetView(CompilationUnit root, IStructuredSelection selectionHint) {
        super.resetView(root, selectionHint);
        if (selectionHint == null) {
            return;
        }
        Object[] selections = selectionHint.toArray();
        if (selections.length != 2 || !(selections[1] instanceof NakedObjectActionParameter)) {
            return;
        }
        NakedObjectActionParameter nakedObjectActionParameter = (NakedObjectActionParameter) selections[1];
        SingleVariableDeclaration searchDeclaration = (SingleVariableDeclaration) nakedObjectActionParameter.getDeclaration();
        CompilationUnit parsedCompilationUnit = getEditorTracker().getParsedCompilationUnit();
        if (parsedCompilationUnit == null) {
            return;
        }
        SingleVariableDeclaration newDeclaration = AstUtils.findDeclaration(parsedCompilationUnit, searchDeclaration);
        if (newDeclaration == null) {
            return;
        }
        AbstractNode<ASTNode> node = find(parsedCompilationUnit, newDeclaration);
        if (node != null) {
            paramsViewer.setSelection(new StructuredSelection(new Object[] { node }), true);
        }
    }

    protected IContentProvider createContentProvider() {
        return new NakedObjectActionsContentProvider(this);
    }

    private ICellModifier cellModifier;

    public <T extends ICellModifier> T getCellModifier(MetadataDescriptorSet set) {
        if (cellModifier == null) {
            cellModifier = new NakedObjectActionsCellModifier(set);
        }
        return Generics.asT(cellModifier);
    }

    private IBaseLabelProvider labelProvider;

    public <T extends IBaseLabelProvider> T getLabelProvider(MetadataDescriptorSet set) {
        if (labelProvider == null) {
            labelProvider = new NakedObjectActionsLabelProvider(set);
        }
        return Generics.asT(labelProvider);
    }
}
