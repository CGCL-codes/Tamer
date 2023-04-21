package astcentric.editor.eclipse.plain;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import astcentric.editor.common.control.tree.ASTView;
import astcentric.editor.common.control.tree.TreeView;
import astcentric.editor.common.dialog.FormDialogEngine;
import astcentric.editor.common.view.graphic.ToolTipManagerProxy;
import astcentric.editor.common.view.tree.TreeNodeFrameFactory;
import astcentric.editor.common.view.tree.frame.DefaultTreeConfiguration;
import astcentric.editor.common.view.tree.frame.TreeConfiguration;
import astcentric.editor.common.view.tree.frame.fanzy.FancyTreeNodeFrameFactory;
import astcentric.editor.eclipse.AsynchExecutor;
import astcentric.editor.eclipse.EclipseToolTipManager;
import astcentric.editor.eclipse.dialog.DialogEngine;
import astcentric.editor.plain.ASTEditorEnvironment;
import astcentric.editor.plain.DefaultPlainRenderingParameters;
import astcentric.editor.plain.PlainRenderingParameters;
import astcentric.structure.filesystem.RealFile;

public class ASTViewer {

    private TreeCanvas _canvas;

    public ASTViewer(Composite parent, ToolTipManagerProxy toolTipProxy) {
        _canvas = new TreeCanvas(parent);
        toolTipProxy.setToolTip(new EclipseToolTipManager(_canvas));
    }

    public TreeCanvas getCanvas() {
        return _canvas;
    }

    public static void main(String[] args) {
        File baseDir = new File(args.length == 0 ? "." : args[0]);
        Display display = new Display();
        TreeConfiguration treeConfiguration = new DefaultTreeConfiguration();
        TreeNodeFrameFactory frameFactory = new FancyTreeNodeFrameFactory(treeConfiguration);
        PlainRenderingParameters parameters = new DefaultPlainRenderingParameters(frameFactory);
        ToolTipManagerProxy toolTipProxy = new ToolTipManagerProxy();
        ASTEditorEnvironment environment = new ASTEditorEnvironment(new RealFile(baseDir), new TreeViewerSWT(display), parameters, new AsynchExecutor(display), "unknown", "unknown");
        Shell shell = createShell(display, environment, toolTipProxy);
        shell.open();
        while (shell.isDisposed() == false) {
            if (display.readAndDispatch() == false) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private static Shell createShell(Display display, final ASTEditorEnvironment environment, final ToolTipManagerProxy toolTipProxy) {
        final Shell shell = new Shell(display);
        final ASTViewer viewerSWT = new ASTViewer(shell, toolTipProxy);
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);
        MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
        fileMenu.setText("&File");
        Menu menu = new Menu(shell, SWT.DROP_DOWN);
        fileMenu.setMenu(menu);
        MenuItem loadItem = new MenuItem(menu, SWT.PUSH);
        loadItem.setText("L&oad AST...");
        loadItem.setAccelerator(SWT.CTRL + 'O');
        loadItem.addSelectionListener(new SelectionAdapter() {

            private final FormDialogEngine _dialogEngine = new DialogEngine(shell);

            public void widgetSelected(SelectionEvent e) {
                ASTView astView = environment.loadAST(_dialogEngine, toolTipProxy);
                TreeView view = astView.getView();
                viewerSWT.getCanvas().setTreeView(view);
            }
        });
        return shell;
    }
}
