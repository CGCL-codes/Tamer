package fr.univartois.cril.alloyplugin.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.xml.sax.InputSource;
import edu.mit.csail.sdg.alloy4.Computer;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4graph.GraphViewer;
import edu.mit.csail.sdg.alloy4viz.VizGUI;
import fr.univartois.cril.alloyplugin.AlloyPlugin;
import fr.univartois.cril.alloyplugin.XMLEditor.XMLEditor;
import fr.univartois.cril.alloyplugin.api.Util;
import fr.univartois.cril.alloyplugin.preferences.AlloyPreferencePage;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener {

    public static final String EDITOR_ID = "fr.univartois.cril.alloyplugin.editors.MultiPageEditor";

    public static final String PDF_GRAPH = "pdf";

    public static final String PNG_GRAPH = "png";

    /** The text editor used in page 0. */
    private XMLEditor editor;

    private Map<String, VizGUI> vizMap = new HashMap<String, VizGUI>();

    private Map<Integer, VizGUI> vizTable = new HashMap<Integer, VizGUI>();

    private Map<Integer, URL> thmTable = new HashMap<Integer, URL>();

    public VizGUI getVizGUI(final String pageName) {
        return vizMap.get(pageName);
    }

    public String getCurrentVizGUIName() {
        final VizGUI viz = getCurrentVizGUI();
        for (Map.Entry<String, VizGUI> entry : vizMap.entrySet()) {
            if (viz.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return "<NULL>";
    }

    public int getCurrentVizGUIIndex() {
        return getActivePage();
    }

    public VizGUI getCurrentVizGUI() {
        return vizTable.get(getActivePage());
    }

    public MultiPageEditor() {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.ctor()");
    }

    /**
	 * @return The MultiPageEditorContributor where we can add, for example,
	 *         additional actions.
	 */
    public MultiPageEditorContributor getMultiPageEditorContributor() {
        final IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
        if (contributor != null && contributor instanceof MultiPageEditorContributor) {
            return (MultiPageEditorContributor) contributor;
        }
        return null;
    }

    @Override
    protected void pageChange(int newPageIndex) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.pageChange(newPageIndex=" + newPageIndex + ").begin");
        MultiPageEditorContributor contributor = getMultiPageEditorContributor();
        if (null != contributor) {
            contributor.setMultiPageEditor(this);
        }
        super.pageChange(newPageIndex);
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.pageChange(newPageIndex=" + newPageIndex + ").end");
    }

    public VizGUI applyAlloyVisualizationToCurrentPage(final URL alloyVisualizationTheme) {
        int index = getCurrentVizGUIIndex();
        VizGUI viz = getCurrentVizGUI();
        viz.loadThemeFile(alloyVisualizationTheme.getFile());
        thmTable.put(index, alloyVisualizationTheme);
        return viz;
    }

    public VizGUI addAlloyVisualizationPage(final String pageName, final URL alloyVisualizationTheme) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addAlloyVisualizationPage(page=" + pageName + ",url=" + alloyVisualizationTheme + ")");
        int index = addPage(pageName, alloyVisualizationTheme);
        return vizTable.get(index);
    }

    public void setActivePage(final String activePageName) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.setActivePage(activePageName=" + activePageName + ").begin");
        int c = getPageCount();
        for (int i = 0; i < c; i++) {
            final String pageName = getPageText(i);
            if (activePageName.equals(pageName)) {
                setActivePage(i);
            }
        }
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.setActivePage(activePageName=" + activePageName + ").end");
    }

    /**
	 * Creates page 1 of the multi-page editor, which contains a text editor.
	 */
    void createPage1() {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createPage1().begin");
        try {
            editor = new XMLEditor();
            int index = addPage(editor, getEditorInput());
            setPageText(index, editor.getTitle());
        } catch (PartInitException e) {
            AlloyPlugin.getDefault().log(e);
        }
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createPage1().end");
    }

    void showSWTControl(Control swtControl, int prefix) {
        AlloyPlugin.getDefault().logInfo("<< " + prefix + " => " + swtControl.hashCode() + " " + swtControl);
        AlloyPlugin.getDefault().logInfo(" - " + prefix + " bkgd color = " + swtControl.getBackground());
        AlloyPlugin.getDefault().logInfo(" - " + prefix + " fore color = " + swtControl.getForeground());
        AlloyPlugin.getDefault().logInfo(" - " + prefix + " visible = " + swtControl.isVisible());
    }

    void showSWTInfo(Composite swtComposite, int prefix) {
        AlloyPlugin.getDefault().logInfo(" - " + prefix + " swtComposite: " + swtComposite);
        showSWTControl(swtComposite, prefix);
        Control[] children = swtComposite.getChildren();
        AlloyPlugin.getDefault().logInfo(" - " + prefix + " " + children.length + " children");
        for (Control child : children) {
            if (child instanceof Composite) {
                Composite cchild = (Composite) child;
                showSWTInfo(cchild, prefix + 1);
            } else {
                showSWTControl(child, prefix + 1);
            }
        }
        AlloyPlugin.getDefault().logInfo(">> " + prefix);
    }

    /**
	 * Creates page 2 of the multi-page editor, which contains a frame with the
	 * graph.
	 * 
	 * @author Nicolas.Rouquette@jpl.nasa.gov
	 * @per Using the SWT/AWT Bridge
	 * @see http://www.eclipse.org/articles/article.php?file=Article-Swing-SWT-Integration/index.html
	 * @see edu.mit.csail.sdg.alloy4viz.VizGUI.updateDisplay()
	 */
    void createPage2() {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createPage2().begin");
        addPage("graph", lookForDefaultThemeFile());
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createPage2().end");
    }

    /**
	 * Look for a default theme file. For the moment, the theme file must be the
	 * same as the als file launching the command.
	 * 
	 * @author leberre@cril.univ-artois.fr
	 */
    private URL lookForDefaultThemeFile() {
        IEditorInput input;
        input = editor.getEditorInput();
        String filename = Util.getFileLocation((IResource) input.getAdapter(IResource.class));
        try {
            XPathFactory fabrique = XPathFactory.newInstance();
            XPath environnement = fabrique.newXPath();
            URL url = new URL("file:" + filename);
            InputSource source = new InputSource(url.openStream());
            XPathExpression expression;
            expression = environnement.compile("/alloy/instance/@filename");
            String resultat = expression.evaluate(source);
            AlloyPlugin.getDefault().logInfo("Solution coming from " + resultat);
            IPath path = new Path(resultat);
            IPath themePath = path.removeFileExtension().addFileExtension("thm");
            File themeFile = themePath.toFile();
            if (themeFile.exists()) {
                AlloyPlugin.getDefault().logInfo("Found default theme " + themeFile);
                return themeFile.toURI().toURL();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * @see http://www.eclipse.org/articles/article.php?file=Article-Swing-SWT-Integration/index.html
	 */
    int addPage(final String pageName, final URL alloyVisualizationTheme) {
        final IEditorInput input = editor.getEditorInput();
        final VizGUI[] viz = new VizGUI[1];
        final Composite a4Component = swingintegration.example.Platform.createComposite(getContainer(), editor.getEditorSite().getShell().getDisplay(), new swingintegration.example.SwingComponentConstructor() {

            public JComponent createSwingComponent() {
                boolean standalone = false;
                String xmlFileName = Util.getFileLocation((IResource) input.getAdapter(IResource.class));
                JMenu windowmenu = null;
                Computer enumerator = null;
                Computer evaluator = null;
                boolean makeWindow = false;
                viz[0] = new VizGUI(standalone, xmlFileName, windowmenu, enumerator, evaluator, makeWindow);
                viz[0].doShowViz();
                if (alloyVisualizationTheme != null) {
                    viz[0].loadThemeFile(alloyVisualizationTheme.getFile());
                }
                return viz[0].getPanel();
            }
        });
        int index = addPage(a4Component);
        setActivePage(index);
        setPageText(index, pageName);
        vizMap.put(pageName, viz[0]);
        vizTable.put(index, viz[0]);
        thmTable.put(index, alloyVisualizationTheme);
        final JMenuItem a4eInfo = new JMenuItem("A4E info...");
        final GraphViewer viewer = viz[0].getViewer();
        viewer.pop.add(a4eInfo);
        ActionListener act = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final Object highlighted = viewer.alloyGetHighlightedAnnotation();
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        AlloyPlugin.getDefault().logInfo("selection: " + highlighted);
                    }
                });
            }
        };
        a4eInfo.addActionListener(act);
        return index;
    }

    /**
	 * Creates the pages of the multi-page editor.
	 */
    protected void createPages() {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createPages().begin");
        createPage1();
        createPage2();
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createPages().end");
    }

    /**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }

    /**
	 * Saves the multi-page editor's document.
	 */
    public void doSave(IProgressMonitor monitor) {
        getEditor(0).doSave(monitor);
    }

    /**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
    public void doSaveAs() {
        IEditorPart editor = getEditor(0);
        editor.doSaveAs();
        setPageText(0, editor.getTitle());
        setInput(editor.getEditorInput());
    }

    private boolean refreshProjectManager(IPath path) throws CoreException {
        IWorkspaceRoot wksroot = ResourcesPlugin.getWorkspace().getRoot();
        IResource dotResource = wksroot.getContainerForLocation(path);
        if (null != dotResource && dotResource.getProject().isAccessible()) {
            IContainer dotFolder = dotResource.getParent();
            dotFolder.refreshLocal(IResource.DEPTH_ONE, null);
            return true;
        }
        return false;
    }

    public IPath saveCurrentVisualizationAsImageFile() throws IOException, CoreException, Err {
        VizGUI viz = getCurrentVizGUI();
        if (null == viz) return null;
        String conversion = AlloyPreferencePage.getShowGraphConversionMessagesPreference();
        return dotConvert(new Path(viz.getXMLfilename()), conversion, viz);
    }

    public IPath dotConvert(IPath xmlFile, String conversion, VizGUI viz) throws IOException, CoreException {
        String[] nonSupported = { "ps", "dia", "jpeg", "gif" };
        for (String format : nonSupported) {
            if (format.equals(conversion)) {
                throw new IllegalArgumentException("Format non longer supported: " + format);
            }
        }
        IPath graphFile = xmlFile.removeFileExtension().addFileExtension(conversion);
        if (PDF_GRAPH.equals(conversion)) {
            viz.getViewer().alloySaveAsPDF(graphFile.toString(), AlloyPreferencePage.getResolutionGraph());
        } else if (PNG_GRAPH.equals(conversion)) {
            double myScale = AlloyPreferencePage.getWidthGraph() / viz.getViewer().getWidth();
            viz.getViewer().alloySaveAsPNG(graphFile.toString(), myScale, AlloyPreferencePage.getResolutionGraph(), AlloyPreferencePage.getResolutionGraph());
        } else {
            throw new IllegalArgumentException("Format unknown " + conversion);
        }
        IWorkspaceRoot wksroot = ResourcesPlugin.getWorkspace().getRoot();
        IResource graphResource = wksroot.getContainerForLocation(graphFile);
        if (null != graphResource && graphResource.getProject().isAccessible()) {
            IContainer dotFolder = graphResource.getParent();
            dotFolder.refreshLocal(IResource.DEPTH_ONE, null);
        }
        return graphFile;
    }

    public void gotoMarker(IMarker marker) {
        setActivePage(0);
        IDE.gotoMarker(getEditor(0), marker);
    }

    /**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
    public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.init(site=" + site + ",editorInput=" + editorInput + ").begin");
        if (!(editorInput instanceof IFileEditorInput)) throw new PartInitException("Invalid Input: Must be IFileEditorInput");
        super.init(site, editorInput);
        setPartName(editorInput.getName());
        IResource resource = (IResource) editorInput.getAdapter(IResource.class);
        setContentDescription(resource.getProjectRelativePath().toPortableString());
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.init(site=" + site + ",editorInput=" + editorInput + ").end");
    }

    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
	 * Closes all project files on project close.
	 */
    public void resourceChanged(final IResourceChangeEvent event) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.resourceChanged(event=" + event + ").begin");
        if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
                    for (int i = 0; i < pages.length; i++) {
                        if (((FileEditorInput) editor.getEditorInput()).getFile().getProject().equals(event.getResource())) {
                            IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
                            pages[i].closeEditor(editorPart, true);
                        }
                    }
                }
            });
        }
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.resourceChanged(event=" + event + ").end");
    }

    @Override
    public int addPage(Control control) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addPage(control=" + control + ").begin");
        try {
            return super.addPage(control);
        } finally {
            if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addPage(control=" + control + ").end");
        }
    }

    @Override
    public int addPage(IEditorPart editor, IEditorInput input) throws PartInitException {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addPage(editor=" + editor + ", input=" + input + ").begin");
        try {
            return super.addPage(editor, input);
        } finally {
            if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addPage(editor=" + editor + ", input=" + input + ").end");
        }
    }

    @Override
    public void addPage(int index, Control control) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addPage(index=" + index + ",control=" + control + ").begin");
        super.addPage(index, control);
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addPage(index=" + index + ",control=" + control + ").end");
    }

    @Override
    public void addPage(int index, IEditorPart editor, IEditorInput input) throws PartInitException {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addPage(index=" + index + ",editor=" + editor + ", input=" + input + ").begin");
        super.addPage(index, editor, input);
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.addPage(index=" + index + ",editor=" + editor + ", input=" + input + ").end");
    }

    @Override
    protected Composite createPageContainer(Composite parent) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createPageContainer(parent=" + parent + ").begin");
        try {
            return super.createPageContainer(parent);
        } finally {
            if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createPageContainer(parent=" + parent + ").end");
        }
    }

    @Override
    protected IEditorSite createSite(IEditorPart editor) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createSite(editor=" + editor + ").begin");
        try {
            return super.createSite(editor);
        } finally {
            if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.createSite(editor=" + editor + ").end");
        }
    }

    @Override
    protected void setControl(int pageIndex, Control control) {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.setControl(pageIndex=" + pageIndex + ",control=" + control + ").begin");
        super.setControl(pageIndex, control);
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.setControl(pageIndex=" + pageIndex + ",control=" + control + ").end");
    }

    public void doSaveAsTheme(IPath path) throws CoreException {
        if (AlloyPreferencePage.getShowDebugMessagesPreference()) AlloyPlugin.getDefault().logInfo("MultiPageEditor.doSaveThemeAs=" + path);
        VizGUI viz = getCurrentVizGUI();
        if (null == viz) return;
        viz.saveThemeFile(path.toString());
        refreshProjectManager(path);
    }
}
