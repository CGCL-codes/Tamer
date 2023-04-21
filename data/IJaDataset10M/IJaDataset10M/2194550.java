package net.sf.jtreemap.ktreemap.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.LinkedHashMap;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.*;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import net.sf.jtreemap.ktreemap.*;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */
public class KTreeMapView extends ViewPart {

    private static final String ID_BUNDLE = "net.sf.jtreemap.ktreemap";

    private TreeViewer viewer;

    private DrillDownAdapter drillDownAdapter;

    private Action openXmlAction;

    private Action openTM3Action;

    private Action selectionChangedAction;

    private KTreeMap kTreeMap;

    private LinkedHashMap<String, SplitStrategy> strategies = new LinkedHashMap<String, SplitStrategy>();

    private LinkedHashMap<String, ITreeMapColorProvider> colorProviders = new LinkedHashMap<String, ITreeMapColorProvider>();

    private Combo cmbStrategy;

    private Combo cmbColorProvider;

    private Combo cmbTM3Weight;

    private Combo cmbTM3Value;

    private Composite legend;

    private XMLTreeMapProvider xmlProvider;

    private TM3TreeMapProvider tm3Provider;

    private BuilderTM3 builderTM3;

    private Group grpTM3Params;

    /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
    @Override
    public void createPartControl(Composite parent) {
        SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
        sash.setLayout(new FillLayout());
        TreeMapNode root = null;
        try {
            root = getDefaultRoot();
        } catch (ParseException e) {
            MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Parse Error", e.getMessage());
            e.printStackTrace();
        }
        createLeftComp(sash);
        createKTreeMapComp(sash, root);
        createRightComp(sash);
        viewer.setInput(root);
        sash.setWeights(new int[] { 15, 70, 15 });
        makeActions();
        hookContextMenu();
        hookSelectionChangedAction();
        contributeToActionBars();
    }

    @Override
    public void dispose() {
        super.dispose();
        ResourceManager.dispose();
    }

    /**
   * Passing the focus request to the viewer's control.
   */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    void updateColorProvider(Composite comp) {
        String key = this.cmbColorProvider.getText();
        ITreeMapColorProvider cp = this.colorProviders.get(key);
        this.kTreeMap.setColorProvider(cp);
        if (legend != null) {
            legend.dispose();
        }
        legend = cp.getLegend(comp, SWT.NONE);
        legend.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        comp.layout();
    }

    void updateStrategy() {
        String key = this.cmbStrategy.getText();
        SplitStrategy strat = this.strategies.get(key);
        this.kTreeMap.setStrategy(strat);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void createColorProviders(Composite comp) {
        createColorProviders();
        final Group grp = new Group(comp, SWT.NONE);
        grp.setText("Color Provider");
        grp.setLayout(new GridLayout());
        grp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        this.cmbColorProvider = new Combo(grp, SWT.NONE);
        this.cmbColorProvider.removeAll();
        for (String key : this.colorProviders.keySet()) {
            this.cmbColorProvider.add(key);
        }
        this.cmbColorProvider.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        this.cmbColorProvider.select(0);
        this.cmbColorProvider.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                updateColorProvider(grp);
            }
        });
        updateColorProvider(grp);
    }

    private void createColorProviders() {
        this.colorProviders.put("HSB linear", new HSBTreeMapColorProvider(kTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.Linear, Display.getDefault().getSystemColor(SWT.COLOR_GREEN), Display.getDefault().getSystemColor(SWT.COLOR_RED)));
        this.colorProviders.put("HSB log", new HSBTreeMapColorProvider(kTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.Log, Display.getDefault().getSystemColor(SWT.COLOR_GREEN), Display.getDefault().getSystemColor(SWT.COLOR_RED)));
        this.colorProviders.put("HSB SquareRoot", new HSBTreeMapColorProvider(kTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.SquareRoot, Display.getDefault().getSystemColor(SWT.COLOR_GREEN), Display.getDefault().getSystemColor(SWT.COLOR_RED)));
        this.colorProviders.put("HSB CubicRoot", new HSBTreeMapColorProvider(kTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.CubicRoot, Display.getDefault().getSystemColor(SWT.COLOR_GREEN), Display.getDefault().getSystemColor(SWT.COLOR_RED)));
        this.colorProviders.put("HSB exp", new HSBTreeMapColorProvider(kTreeMap, HSBTreeMapColorProvider.ColorDistributionTypes.Exp, Display.getDefault().getSystemColor(SWT.COLOR_GREEN), Display.getDefault().getSystemColor(SWT.COLOR_RED)));
    }

    private void createKTreeMapComp(SashForm sash, TreeMapNode root) {
        kTreeMap = new KTreeMap(sash, SWT.NONE, root);
        kTreeMap.setTreeMapProvider(xmlProvider);
    }

    private void createLeftComp(SashForm sash) {
        Composite comp = new Composite(sash, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 2;
        layout.marginWidth = 2;
        comp.setLayout(layout);
        viewer = new TreeViewer(comp, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        drillDownAdapter = new DrillDownAdapter(viewer);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private void createRightComp(SashForm sash) {
        Composite comp = new Composite(sash, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 2;
        layout.marginWidth = 2;
        comp.setLayout(layout);
        createStrategies(comp);
        createColorProviders(comp);
        createTM3Params(comp);
    }

    private void createTM3Params(Composite comp) {
        grpTM3Params = new Group(comp, SWT.NONE);
        grpTM3Params.setText("TM3 params");
        grpTM3Params.setLayout(new GridLayout());
        grpTM3Params.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        grpTM3Params.setVisible(false);
        this.cmbTM3Value = new Combo(grpTM3Params, SWT.NONE);
        this.cmbTM3Value.setToolTipText("Select the value field");
        this.cmbTM3Value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        this.cmbTM3Value.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                Combo cmb = (Combo) e.getSource();
                String field = cmb.getText();
                TM3TreeMapProvider.setValueField(field);
                createColorProviders();
                updateColorProvider(cmbColorProvider.getParent());
                kTreeMap.redraw();
            }
        });
        this.cmbTM3Weight = new Combo(grpTM3Params, SWT.NONE);
        this.cmbTM3Weight.setToolTipText("Select the weight field");
        this.cmbTM3Weight.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        this.cmbTM3Weight.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                Combo cmb = (Combo) e.getSource();
                String field = cmb.getText();
                BuilderTM3.setFieldWeight(field);
                builderTM3.setWeights();
                kTreeMap.calculatePositions();
                kTreeMap.redraw();
            }
        });
    }

    private void createStrategies(Composite comp) {
        this.strategies.put("Squarified", new SplitSquarified());
        this.strategies.put("Sorted Weight", new SplitBySortedWeight());
        this.strategies.put("Weight", new SplitByWeight());
        this.strategies.put("Slice", new SplitBySlice());
        this.strategies.put("Equal Weight", new SplitByNumber());
        Group grp = new Group(comp, SWT.NONE);
        grp.setText("Strategy");
        grp.setLayout(new GridLayout());
        grp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        this.cmbStrategy = new Combo(grp, SWT.NONE);
        this.cmbStrategy.removeAll();
        for (String key : this.strategies.keySet()) {
            this.cmbStrategy.add(key);
        }
        this.cmbStrategy.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        this.cmbStrategy.select(0);
        this.cmbStrategy.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                updateStrategy();
            }
        });
        updateStrategy();
    }

    private void fillContextMenu(IMenuManager manager) {
        TreeMapNode orig = kTreeMap.getDisplayedRoot();
        ITreeMapProvider provider = kTreeMap.getTreeMapProvider();
        TreeMapNode cursor = orig;
        String id = "separator";
        manager.add(new Separator(id));
        while (cursor.getParent() != null) {
            TreeMapNode parent = cursor.getParent();
            ZoomAction action = new ZoomAction(provider.getLabel(parent), AbstractUIPlugin.imageDescriptorFromPlugin(ID_BUNDLE, "icons/unzoom.gif"), parent);
            manager.insertBefore(id, action);
            cursor = parent;
            id = action.getId();
        }
        cursor = orig;
        while (cursor.getChild(kTreeMap.getCursorPosition()) != null) {
            TreeMapNode child = cursor.getChild(kTreeMap.getCursorPosition());
            if (!child.isLeaf()) {
                ZoomAction action = new ZoomAction(provider.getLabel(child), AbstractUIPlugin.imageDescriptorFromPlugin(ID_BUNDLE, "icons/zoom.gif"), child);
                manager.add(action);
            }
            cursor = child;
        }
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(openXmlAction);
        manager.add(openTM3Action);
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(openXmlAction);
        manager.add(openTM3Action);
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
    }

    private TreeMapNode getDefaultRoot() throws ParseException {
        URL url = FileLocator.find(Platform.getBundle(ID_BUNDLE), new Path("/TreeMap.xml"), null);
        try {
            url = FileLocator.toFileURL(url);
        } catch (IOException e) {
            MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Error", e.getMessage());
            e.printStackTrace();
        }
        BuilderXML builder = new BuilderXML(new File(url.getPath()));
        xmlProvider = new XMLTreeMapProvider();
        tm3Provider = new TM3TreeMapProvider();
        return builder.getRoot();
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                KTreeMapView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(kTreeMap);
        kTreeMap.setMenu(menu);
    }

    private void hookSelectionChangedAction() {
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                selectionChangedAction.run();
            }
        });
    }

    private void setTM3Fields() {
        String[] numberFields = TM3Bean.getNumberFields();
        String[] cmbValues = new String[numberFields.length + 1];
        cmbValues[0] = "";
        for (int i = 1; i < cmbValues.length; i++) {
            cmbValues[i] = numberFields[i - 1];
        }
        this.cmbTM3Weight.removeAll();
        this.cmbTM3Value.removeAll();
        for (int i = 0; i < cmbValues.length; i++) {
            String item = cmbValues[i];
            this.cmbTM3Weight.add(item);
            this.cmbTM3Value.add(item);
        }
    }

    private void makeActions() {
        openXmlAction = new OpenXMLAction();
        openTM3Action = new OpenTM3Action();
        selectionChangedAction = new Action() {

            @Override
            public void run() {
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                if (obj instanceof TreeMapNode) {
                    TreeMapNode dest = ((TreeMapNode) obj).getParent();
                    if (dest == null) {
                        return;
                    }
                    kTreeMap.zoom(dest);
                    kTreeMap.redraw();
                }
            }
        };
    }

    class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

        public void dispose() {
        }

        public Object[] getChildren(Object parent) {
            if (parent instanceof TreeMapNode) {
                return ((TreeMapNode) parent).getChildren().toArray();
            }
            return new Object[0];
        }

        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            if (child instanceof TreeMapNode) {
                return ((TreeMapNode) child).getParent();
            }
            return null;
        }

        public boolean hasChildren(Object parent) {
            if (parent instanceof TreeMapNode) return !((TreeMapNode) parent).isLeaf();
            return false;
        }

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
    }

    class ViewLabelProvider extends LabelProvider {

        @Override
        public Image getImage(Object obj) {
            return null;
        }

        @Override
        public String getText(Object obj) {
            if (obj instanceof TreeMapNode) {
                TreeMapNode node = (TreeMapNode) obj;
                ITreeMapProvider provider = KTreeMapView.this.kTreeMap.getTreeMapProvider();
                return provider.getLabel(node);
            }
            return obj.toString();
        }
    }

    private class OpenXMLAction extends Action {

        private final String[] EXTENTIONS = new String[] { "*.xml" };

        /**
     * Constructor
     */
        public OpenXMLAction() {
            super("Open XML File");
            setToolTipText(getText());
            setId(getText());
        }

        @Override
        public ImageDescriptor getImageDescriptor() {
            return AbstractUIPlugin.imageDescriptorFromPlugin(ID_BUNDLE, "icons/XMLFile.gif");
        }

        @Override
        public void run() {
            Display display = PlatformUI.getWorkbench().getDisplay();
            final FileDialog dialog = new FileDialog(display.getActiveShell(), SWT.OPEN);
            dialog.setFilterExtensions(EXTENTIONS);
            String path = dialog.open();
            if (path != null) {
                BuilderXML builder;
                try {
                    builder = new BuilderXML(new File(path));
                } catch (ParseException e) {
                    MessageDialog.openError(display.getActiveShell(), "Parse error", e.getMessage());
                    return;
                }
                TreeMapNode root = builder.getRoot();
                kTreeMap.setTreeMapProvider(xmlProvider);
                kTreeMap.setRoot(root);
                viewer.setInput(root);
                createColorProviders();
                updateColorProvider(cmbColorProvider.getParent());
                grpTM3Params.setVisible(false);
            }
        }
    }

    private class OpenTM3Action extends Action {

        private final String[] EXTENTIONS = new String[] { "*.tm3" };

        /**
     * Constructor
     */
        public OpenTM3Action() {
            super("Open TM3 File");
            setToolTipText(getText());
            setId(getText());
        }

        @Override
        public ImageDescriptor getImageDescriptor() {
            return AbstractUIPlugin.imageDescriptorFromPlugin(ID_BUNDLE, "icons/TM3File.gif");
        }

        @Override
        public void run() {
            Display display = PlatformUI.getWorkbench().getDisplay();
            final FileDialog dialog = new FileDialog(display.getActiveShell(), SWT.OPEN);
            dialog.setFilterExtensions(EXTENTIONS);
            String path = dialog.open();
            if (path != null) {
                try {
                    builderTM3 = new BuilderTM3(new File(path));
                } catch (IOException e) {
                    MessageDialog.openError(display.getActiveShell(), "Parse error", e.getMessage());
                    return;
                }
                TreeMapNode root = builderTM3.getRoot();
                kTreeMap.setTreeMapProvider(tm3Provider);
                kTreeMap.setRoot(root);
                viewer.setInput(root);
                createColorProviders();
                updateColorProvider(cmbColorProvider.getParent());
                setTM3Fields();
                grpTM3Params.setVisible(true);
            }
        }
    }

    private class ZoomAction extends Action {

        private TreeMapNode node;

        /**
     * Constructor
     *
     * @param text text of the action
     * @param image image
     * @param node destination TreeMapNode of the zoom
     */
        public ZoomAction(String text, ImageDescriptor image, TreeMapNode node) {
            super(text, image);
            this.node = node;
            setId(text);
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public void run() {
            kTreeMap.zoom(this.node);
            kTreeMap.redraw();
        }
    }
}
