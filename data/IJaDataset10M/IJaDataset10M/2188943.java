package com.aptana.ide.editors.views.outline;

import java.text.Collator;
import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.WorkbenchJob;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import com.aptana.ide.core.IdeLog;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.editors.UnifiedEditorsPlugin;
import com.aptana.ide.editors.unified.ContributedOutline;
import com.aptana.ide.editors.unified.IUnifiedEditor;
import com.aptana.ide.editors.unified.UnifiedInformationControl;
import com.aptana.ide.parsing.IParseState;

/**
 * @author Paul Colton
 */
public class UnifiedOutlinePage extends ContentOutlinePage implements ISelectionChangedListener, IDoubleClickListener {

    private IUnifiedEditor _editor;

    private ISourceViewer _sourceViewer;

    private boolean _isDisposing = false;

    private OutlineManager _outlineManager = UnifiedEditorsPlugin.getDefault().getOutlineManager();

    private static HashMap _imageCache = new HashMap();

    private static ImageDescriptor _sortIconDescriptor = getImageDescriptor("icons/sort.gif");

    private static ImageDescriptor _collapseIconDescriptor = getImageDescriptor("icons/collapse.gif");

    private static ImageDescriptor _syncIconDescriptor = getImageDescriptor("icons/sync.gif");

    private static ImageDescriptor _hidePrivateIconDescriptor = getImageDescriptor("icons/method_public.gif");

    private static ImageDescriptor _splitIconDescriptor = getImageDescriptor("icons/split_outlines.gif");

    private ActionContributionItem _sortItem;

    private ActionContributionItem _publicItem;

    private ActionContributionItem _collapseItem;

    private ActionContributionItem _splitItem;

    /**
	 * fTreeViewer
	 */
    protected TreeViewer fTreeViewer = null;

    /**
	 * comp
	 */
    protected Composite comp;

    /**
	 * outlineTabs
	 */
    protected CTabFolder outlineTabs;

    /**
	 * outlineSash
	 */
    protected SashForm outlineSash;

    /**
	 * searchBox
	 */
    protected Text searchBox;

    /**
	 * filter
	 */
    protected PatternFilter filter;

    /**
	 * outlines
	 */
    protected HashMap outlines;

    private Job refreshJob;

    /**
	 * @param editor
	 */
    public UnifiedOutlinePage(IUnifiedEditor editor) {
        setCurrentEditor(editor);
    }

    /**
	 * setCurrentEditor
	 * 
	 * @param editor
	 */
    public void setCurrentEditor(IUnifiedEditor editor) {
        this._editor = editor;
        this._sourceViewer = editor.getViewer();
        outlines = new HashMap();
    }

    /**
	 * getEditor
	 * 
	 * @return IUnifiedEditor
	 */
    public IUnifiedEditor getEditor() {
        return this._editor;
    }

    /**
	 * Provides the content for this tree
	 */
    class TreeContentProvider implements ITreeContentProvider {

        /**
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
        public Object[] getElements(Object inputElement) {
            Object[] rootElements = _outlineManager.getElements(inputElement);
            return rootElements;
        }

        /**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
        public Object[] getChildren(Object parentElement) {
            Object[] children = _outlineManager.getChildren(parentElement);
            return children;
        }

        /**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
        public Object getParent(Object element) {
            return _outlineManager.getParent(element);
        }

        /**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
        public boolean hasChildren(Object element) {
            boolean result = _outlineManager.hasChildren(element);
            return result;
        }

        /**
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
        public void dispose() {
        }

        /**
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    /**
	 * TreeLabelProvider
	 * 
	 * @author Ingo Muschenetz
	 */
    class TreeLabelProvider extends LabelProvider {

        /**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
        public String getText(Object element) {
            if (element instanceof OutlineItem) {
                return ((OutlineItem) element).getLabel();
            } else {
                return super.getText(element);
            }
        }

        /**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
		 */
        public Image getImage(Object obj) {
            if (obj instanceof OutlineItem) {
                OutlineItem oi = ((OutlineItem) obj);
                String iconPath = oi.getIconPath();
                if (iconPath != null) {
                    if (_imageCache.containsKey(iconPath)) {
                        return (Image) _imageCache.get(iconPath);
                    } else {
                        Image image = UnifiedEditorsPlugin.getImage(iconPath);
                        if (image != null) {
                            _imageCache.put(iconPath, image);
                            return image;
                        }
                    }
                }
            }
            return super.getImage(obj);
        }
    }

    /**
	 * textChanged
	 */
    protected void textChanged() {
        refreshJob.cancel();
        refreshJob.schedule(200);
        Iterator outlineIter = outlines.values().iterator();
        while (outlineIter.hasNext()) {
            ((ContributedOutline) outlineIter.next()).setFilterText(searchBox.getText());
        }
    }

    private void createRefreshJob() {
        refreshJob = new WorkbenchJob("Refresh Filter") {

            public IStatus runInUIThread(IProgressMonitor monitor) {
                if (fTreeViewer.getControl().isDisposed()) {
                    return Status.CANCEL_STATUS;
                }
                String text = searchBox.getText();
                if (text == null) {
                    return Status.OK_STATUS;
                }
                filter.setPattern(text);
                try {
                    fTreeViewer.getControl().setRedraw(false);
                    fTreeViewer.refresh(true);
                    if (text.length() > 0) {
                        IStructuredContentProvider provider = (IStructuredContentProvider) fTreeViewer.getContentProvider();
                        Object[] elements = provider.getElements(fTreeViewer.getInput());
                        for (int i = 0; i < elements.length; i++) {
                            if (monitor.isCanceled()) {
                                return Status.CANCEL_STATUS;
                            }
                            fTreeViewer.expandToLevel(elements[i], AbstractTreeViewer.ALL_LEVELS);
                        }
                        TreeItem[] items = fTreeViewer.getTree().getItems();
                        if (items.length > 0) {
                            fTreeViewer.getTree().showItem(items[0]);
                        }
                    }
                } finally {
                    fTreeViewer.getControl().setRedraw(true);
                }
                return Status.OK_STATUS;
            }
        };
        refreshJob.setSystem(true);
    }

    /**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createControl(Composite parent) {
        comp = new Composite(parent, SWT.NONE);
        GridLayout contentAreaLayout = new GridLayout();
        contentAreaLayout.numColumns = 1;
        contentAreaLayout.makeColumnsEqualWidth = false;
        contentAreaLayout.marginHeight = 0;
        contentAreaLayout.marginWidth = 0;
        contentAreaLayout.verticalSpacing = 0;
        contentAreaLayout.horizontalSpacing = 0;
        comp.setLayout(contentAreaLayout);
        comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Composite top = new Composite(comp, SWT.NONE);
        contentAreaLayout = new GridLayout();
        contentAreaLayout.numColumns = 1;
        contentAreaLayout.makeColumnsEqualWidth = false;
        contentAreaLayout.marginHeight = 3;
        contentAreaLayout.marginWidth = 0;
        contentAreaLayout.verticalSpacing = 0;
        contentAreaLayout.horizontalSpacing = 0;
        top.setLayout(contentAreaLayout);
        top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        filter = new PatternFilter() {

            protected boolean isLeafMatch(Viewer viewer, Object element) {
                if (element instanceof OutlineItem) {
                    return this.wordMatches(((OutlineItem) element).getLabel());
                }
                return true;
            }
        };
        createRefreshJob();
        Composite searchArea = new Composite(top, SWT.NONE);
        contentAreaLayout = new GridLayout();
        contentAreaLayout.numColumns = 2;
        contentAreaLayout.makeColumnsEqualWidth = false;
        contentAreaLayout.marginHeight = 0;
        contentAreaLayout.marginWidth = 0;
        contentAreaLayout.verticalSpacing = 0;
        contentAreaLayout.horizontalSpacing = 0;
        searchArea.setLayout(contentAreaLayout);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = 1;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = false;
        searchArea.setLayoutData(data);
        Label searchLabel = new Label(searchArea, SWT.NONE);
        searchLabel.setText(Messages.UnifiedOutlinePage_Filter);
        searchBox = new Text(searchArea, SWT.SINGLE | SWT.BORDER);
        searchBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        searchBox.setEditable(true);
        searchBox.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                textChanged();
            }
        });
        outlineSash = new SashForm(comp, SWT.VERTICAL);
        outlineSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentAreaLayout = new GridLayout();
        contentAreaLayout.numColumns = 1;
        contentAreaLayout.makeColumnsEqualWidth = false;
        contentAreaLayout.marginHeight = 0;
        contentAreaLayout.marginWidth = 0;
        contentAreaLayout.verticalSpacing = 0;
        contentAreaLayout.horizontalSpacing = 0;
        outlineSash.setLayout(contentAreaLayout);
        ((GridData) outlineSash.getLayoutData()).exclude = true;
        outlineSash.setVisible(false);
        outlineTabs = new CTabFolder(comp, SWT.BOTTOM);
        outlineTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        CTabItem sourceTab = new CTabItem(outlineTabs, SWT.NONE);
        sourceTab.setText(Messages.UnifiedOutlinePage_Source);
        outlineTabs.setSelection(sourceTab);
        contentAreaLayout = new GridLayout();
        contentAreaLayout.numColumns = 1;
        contentAreaLayout.makeColumnsEqualWidth = false;
        contentAreaLayout.marginHeight = 0;
        contentAreaLayout.marginWidth = 0;
        contentAreaLayout.verticalSpacing = 0;
        contentAreaLayout.horizontalSpacing = 0;
        Composite sourceComp = new Composite(outlineSash, SWT.NONE);
        contentAreaLayout = new GridLayout();
        contentAreaLayout.numColumns = 1;
        contentAreaLayout.makeColumnsEqualWidth = false;
        contentAreaLayout.marginHeight = 0;
        contentAreaLayout.marginWidth = 0;
        contentAreaLayout.verticalSpacing = 0;
        contentAreaLayout.horizontalSpacing = 0;
        sourceComp.setLayout(contentAreaLayout);
        sourceComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Label sourceLabel = new Label(sourceComp, SWT.NONE);
        sourceLabel.setText(Messages.UnifiedOutlinePage_SourceOutline);
        sourceLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        SashForm sourceForm = new SashForm(outlineTabs, SWT.NONE);
        sourceForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fTreeViewer = createTreeViewer(sourceForm);
        sourceTab.setControl(sourceForm);
        Iterator iter = outlines.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            ContributedOutline outline = (ContributedOutline) outlines.get(key);
            CTabItem tab = new CTabItem(outlineTabs, SWT.NONE);
            Composite previewComp = new Composite(outlineSash, SWT.NONE);
            contentAreaLayout = new GridLayout();
            contentAreaLayout.numColumns = 1;
            contentAreaLayout.makeColumnsEqualWidth = false;
            contentAreaLayout.marginHeight = 0;
            contentAreaLayout.marginWidth = 0;
            contentAreaLayout.verticalSpacing = 0;
            contentAreaLayout.horizontalSpacing = 0;
            previewComp.setLayout(contentAreaLayout);
            previewComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            Label outlineLabel = new Label(previewComp, SWT.NONE);
            outlineLabel.setText(StringUtils.format(Messages.UnifiedOutlinePage_Outline, key));
            outlineLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            tab.setText(key);
            SashForm preForm = new SashForm(outlineTabs, SWT.NONE);
            contentAreaLayout = new GridLayout();
            contentAreaLayout.numColumns = 1;
            contentAreaLayout.makeColumnsEqualWidth = false;
            contentAreaLayout.marginHeight = 0;
            contentAreaLayout.marginWidth = 0;
            contentAreaLayout.verticalSpacing = 0;
            contentAreaLayout.horizontalSpacing = 0;
            preForm.setLayout(contentAreaLayout);
            preForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            outline.createControl(preForm);
            tab.setControl(preForm);
        }
        fTreeViewer.addFilter(filter);
        fTreeViewer.setContentProvider(new TreeContentProvider());
        fTreeViewer.setLabelProvider(new TreeLabelProvider());
        String lang = Messages.UnifiedOutlinePage_Unknown;
        IParseState parseState = _editor.getFileContext().getParseState();
        if (parseState != null) {
            lang = parseState.getLanguage();
        }
        if (lang.equals(Messages.UnifiedOutlinePage_TextHtml)) {
            fTreeViewer.setAutoExpandLevel(3);
        } else if (lang.equals(Messages.UnifiedOutlinePage_TextJavascript)) {
            fTreeViewer.setAutoExpandLevel(2);
        } else {
            fTreeViewer.setAutoExpandLevel(1);
        }
        fTreeViewer.setInput(new OutlineItem(lang));
        getSite().setSelectionProvider(fTreeViewer);
        fTreeViewer.addSelectionChangedListener(this);
        fTreeViewer.addDoubleClickListener(this);
        updateTree();
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), "com.aptana.ide.js.ui.JsContentOutlinePage");
    }

    /**
	 * addOutline
	 *
	 * @param outline
	 * @param name
	 */
    public void addOutline(ContributedOutline outline, String name) {
        outlines.put(name, outline);
    }

    /**
	 * createTreeViewer
	 * 
	 * @param parent
	 * @return IUnifiedEditor
	 */
    protected TreeViewer createTreeViewer(Composite parent) {
        final TreeViewer treeViewer = new TreeViewer(new Tree(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL));
        treeViewer.setComparer(new IElementComparer() {

            public boolean equals(Object a, Object b) {
                if (a instanceof OutlineItem && b instanceof OutlineItem) {
                    OutlineItem item1 = (OutlineItem) a;
                    OutlineItem item2 = (OutlineItem) b;
                    return item1.equals(item2);
                } else {
                    return a == b;
                }
            }

            public int hashCode(Object element) {
                return 0;
            }
        });
        Listener tableListener = new Listener() {

            UnifiedInformationControl info = null;

            public void handleEvent(Event event) {
                switch(event.type) {
                    case SWT.Dispose:
                    case SWT.KeyDown:
                    case SWT.MouseMove:
                        {
                            if (info == null || info.getShell() == null) {
                                break;
                            }
                            info.getShell().dispose();
                            info = null;
                            break;
                        }
                    case SWT.MouseHover:
                        {
                            break;
                        }
                    default:
                        break;
                }
            }
        };
        treeViewer.getTree().addListener(SWT.Dispose, tableListener);
        treeViewer.getTree().addListener(SWT.KeyDown, tableListener);
        treeViewer.getTree().addListener(SWT.MouseMove, tableListener);
        treeViewer.getTree().addListener(SWT.MouseHover, tableListener);
        return treeViewer;
    }

    /**
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
    public Control getControl() {
        return comp;
    }

    /**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
    public ISelection getSelection() {
        if (fTreeViewer == null) {
            return StructuredSelection.EMPTY;
        }
        return fTreeViewer.getSelection();
    }

    /**
	 * @see org.eclipse.ui.part.IPage#setActionBars(org.eclipse.ui.IActionBars)
	 */
    public void setActionBars(IActionBars actionBars) {
        super.setActionBars(actionBars);
        if (outlines.size() > 0) {
            SplitOutlinesAction splitAction = new SplitOutlinesAction(Messages.UnifiedOutlinePage_SplitView, Action.AS_CHECK_BOX);
            splitAction.setEnabled(comp.isReparentable());
            _splitItem = new ActionContributionItem(splitAction);
            actionBars.getToolBarManager().add(_splitItem);
        }
        SortAction sortAction = new SortAction(fTreeViewer);
        _sortItem = new ActionContributionItem(sortAction);
        CollapseTreeAction collapseAction = new CollapseTreeAction(fTreeViewer);
        _collapseItem = new ActionContributionItem(collapseAction);
        HidePrivateAction privateAction = new HidePrivateAction(fTreeViewer);
        _publicItem = new ActionContributionItem(privateAction);
        actionBars.getToolBarManager().add(_sortItem);
        actionBars.getToolBarManager().add(_publicItem);
        actionBars.getToolBarManager().add(_collapseItem);
    }

    /**
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
    public void setFocus() {
        fTreeViewer.getControl().setFocus();
    }

    /**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
    public void setSelection(ISelection selection) {
        if (fTreeViewer != null) {
            fTreeViewer.setSelection(selection, true);
        }
    }

    /**
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        if (selection.size() == 1) {
            OutlineItem contentElement = (OutlineItem) selection.getFirstElement();
            this._outlineManager.fireOutlineChangeEvent(contentElement);
        } else {
            _sourceViewer.removeRangeIndication();
        }
    }

    /**
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
    public void doubleClick(DoubleClickEvent event) {
    }

    /**
	 * Retrieves the image descriptor associated with resource from the image descriptor registry. If the image
	 * descriptor cannot be retrieved, attempt to find and load the image descriptor at the location specified in
	 * resource.
	 * 
	 * @param imageFilePath
	 *            the image descriptor to retrieve
	 * @return The image descriptor associated with resource or the default "missing" image descriptor if one could not
	 *         be found
	 */
    private static ImageDescriptor getImageDescriptor(String imageFilePath) {
        ImageDescriptor imageDescriptor = UnifiedEditorsPlugin.getImageDescriptor(imageFilePath);
        if (imageDescriptor == null) {
            imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
        }
        return imageDescriptor;
    }

    /**
	 * updateTree
	 */
    public void updateTree() {
        Display display = Display.getDefault();
        display.syncExec(new Runnable() {

            public void run() {
                try {
                    if (fTreeViewer.getControl().isDisposed() == false) {
                        fTreeViewer.setSelection(null);
                        fTreeViewer.refresh();
                    }
                } catch (Exception e) {
                    IdeLog.logError(UnifiedEditorsPlugin.getDefault(), Messages.UnifiedOutlinePage_Error, e);
                }
            }
        });
    }

    /**
	 * Splits the outline and stacks them vertically
	 */
    protected class SplitOutlinesAction extends Action {

        /**
		 * SplitOutlinesAction
		 * 
		 * @param name
		 * @param style
		 */
        public SplitOutlinesAction(String name, int style) {
            super(name, style);
            setImageDescriptor(_splitIconDescriptor);
            setToolTipText(Messages.UnifiedOutlinePage_SplitOutlinePane);
        }

        /**
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            super.run();
            if (this.isChecked()) {
                Control[] tabs = outlineTabs.getTabList();
                Control[] sections = outlineSash.getTabList();
                for (int i = 0; i < tabs.length; i++) {
                    tabs[i].setParent((Composite) sections[i]);
                    tabs[i].setVisible(true);
                }
                outlineSash.setMaximizedControl(null);
                ((GridData) outlineTabs.getLayoutData()).exclude = true;
                ((GridData) outlineSash.getLayoutData()).exclude = false;
                outlineTabs.setVisible(false);
                outlineSash.setVisible(true);
                comp.layout(true, true);
                sections = outlineSash.getTabList();
                for (int i = 0; i < sections.length; i++) {
                    sections[i].getParent().layout(true, true);
                }
            } else {
                outlineTabs.setVisible(true);
                outlineSash.setVisible(false);
                CTabItem[] ctabs = outlineTabs.getItems();
                fTreeViewer.getControl().getParent().setParent(outlineTabs);
                ctabs[0].setControl(fTreeViewer.getControl().getParent());
                for (int i = 1; i < ctabs.length; i++) {
                    ContributedOutline outline = (ContributedOutline) outlines.get(ctabs[i].getText());
                    outline.getParent().setParent(outlineTabs);
                    ctabs[i].setControl(outline.getParent());
                }
                outlineTabs.getItem(0).getControl().setVisible(true);
                outlineTabs.setSelection(0);
                ((GridData) outlineSash.getLayoutData()).exclude = true;
                ((GridData) outlineTabs.getLayoutData()).exclude = false;
                comp.layout(true, true);
            }
        }
    }

    /**
	 * SyncTreeAction
	 */
    protected class SyncTreeAction extends Action {

        private TreeViewer treeViewer;

        /**
		 * SyncTreeAction
		 * 
		 * @param viewer
		 */
        public SyncTreeAction(TreeViewer viewer) {
            super(Messages.UnifiedOutlinePage_SyncWithCursor, Action.AS_PUSH_BUTTON);
            setImageDescriptor(_syncIconDescriptor);
            setToolTipText(getText());
            treeViewer = viewer;
        }

        /**
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            super.run();
            int offset = _editor.getViewer().getTextWidget().getCaretOffset();
            TreeItem[] treeItems = treeViewer.getTree().getItems();
            TreeItem itemToSelect = null;
            while (treeItems != null && treeItems.length > 0) {
                boolean foundNewItem = false;
                for (int i = 0; i < treeItems.length; i++) {
                    TreeItem treeItem = treeItems[i];
                    Object data = treeItem.getData();
                    if (data instanceof OutlineItem) {
                        OutlineItem outlineItem = (OutlineItem) data;
                        int startingOffset = outlineItem.getStartingOffset();
                        int endingOffset = outlineItem.getEndingOffset();
                        if (startingOffset <= offset && offset < endingOffset) {
                            itemToSelect = treeItem;
                            StructuredSelection selectedItem = new StructuredSelection(itemToSelect.getData());
                            treeViewer.setSelection(selectedItem, true);
                            foundNewItem = true;
                            break;
                        }
                    }
                }
                if (foundNewItem) {
                    treeItems = itemToSelect.getItems();
                } else {
                    treeItems = null;
                }
            }
        }
    }

    /**
	 * CollapseTreeAction
	 */
    protected class CollapseTreeAction extends Action {

        private TreeViewer treeViewer;

        /**
		 * CollapseTreeAction
		 * 
		 * @param viewer
		 */
        public CollapseTreeAction(TreeViewer viewer) {
            super(Messages.UnifiedOutlinePage_CollapseAll, Action.AS_PUSH_BUTTON);
            setImageDescriptor(_collapseIconDescriptor);
            setToolTipText(getText());
            treeViewer = viewer;
        }

        /**
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            super.run();
            treeViewer.collapseAll();
        }
    }

    /**
	 * HidePrivateAction
	 */
    protected class HidePrivateAction extends Action {

        /**
		 * HidePrivateAction
		 * 
		 * @param viewer
		 */
        public HidePrivateAction(TreeViewer viewer) {
            super(Messages.UnifiedOutlinePage_HidePrivateMembers, Action.AS_CHECK_BOX);
            setImageDescriptor(_hidePrivateIconDescriptor);
            setToolTipText(getText());
        }

        /**
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            super.run();
            _outlineManager.setHidePrivateMembers(_outlineManager.getHidePrivateMembers() == false);
            _outlineManager.refresh();
        }
    }

    /**
	 * SortAction
	 */
    protected class SortAction extends Action {

        private TreeViewer treeViewer;

        /**
		 * SortAction
		 * 
		 * @param viewer
		 */
        public SortAction(TreeViewer viewer) {
            super(Messages.UnifiedOutlinePage_Sort, Action.AS_CHECK_BOX);
            setImageDescriptor(_sortIconDescriptor);
            setToolTipText(getText());
            treeViewer = viewer;
            if (isChecked()) {
                treeViewer.setSorter(new CategorySorter(Collator.getInstance()));
            }
        }

        /**
		 * @see org.eclipse.jface.action.Action#run()
		 */
        public void run() {
            super.run();
            treeViewer.getControl().setVisible(false);
            if (isChecked()) {
                treeViewer.setSorter(new CategorySorter(Collator.getInstance()));
            } else {
                treeViewer.setSorter(null);
            }
            _outlineManager.refresh();
            treeViewer.getControl().setVisible(true);
        }

        /**
		 * CategorySorter
		 */
        protected class CategorySorter extends ViewerSorter {

            /**
			 * CategorySorter
			 */
            public CategorySorter() {
                super();
            }

            /**
			 * CategorySorter
			 * 
			 * @param collator
			 */
            public CategorySorter(Collator collator) {
                super(collator);
            }

            /**
			 * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
			 */
            public int category(Object element) {
                return 0;
            }

            /**
			 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object,
			 *      java.lang.Object)
			 */
            public int compare(Viewer view, Object e1, Object e2) {
                int cat1 = category(e1);
                int cat2 = category(e2);
                int result;
                if (cat1 == cat2) {
                    OutlineItem item1 = (OutlineItem) e1;
                    OutlineItem item2 = (OutlineItem) e2;
                    result = item1.getLabel().toLowerCase().compareTo(item2.getLabel().toLowerCase());
                } else {
                    result = cat1 - cat2;
                }
                return result;
            }

            /**
			 * TODO: implement as another SortAction
			 * 
			 * @param viewer
			 * @param e1
			 * @param e2
			 * @return int
			 */
            public int compare2(Viewer viewer, Object e1, Object e2) {
                int cat1 = category(e1);
                int cat2 = category(e2);
                int result;
                if (cat1 == cat2) {
                    OutlineItem item1 = (OutlineItem) e1;
                    OutlineItem item2 = (OutlineItem) e2;
                    result = item1.getEndingOffset() - item2.getEndingOffset();
                    if (result == 0) {
                        result = item2.getStartingOffset() - item1.getStartingOffset();
                    }
                } else {
                    result = cat1 - cat2;
                }
                return result;
            }
        }
    }

    /**
	 * dispose
	 */
    public void dispose() {
        if (_isDisposing) {
            return;
        }
        _isDisposing = true;
        super.dispose();
        if (fTreeViewer != null) {
            fTreeViewer.removeDoubleClickListener(this);
            fTreeViewer.removeSelectionChangedListener(this);
        }
        if (this.getSite() != null) {
            this.getSite().setSelectionProvider(null);
        }
        if (_collapseItem != null) {
            _collapseItem.dispose();
        }
        if (_sortItem != null) {
            _sortItem.dispose();
        }
        this._editor = null;
        this._sourceViewer = null;
        this._collapseItem = null;
        this._sortItem = null;
    }
}
