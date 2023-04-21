package thermologviewer;

import java.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import thermologviewer.view.*;

/**
* 
*/
public class MainWnd implements Listener {

    public MainWnd(int width, int height, String title) {
        display = new Display();
        shell = new Shell(display);
        shell.setText(title);
        shell.setSize(width, height);
        shell.addListener(SWT.KeyDown, this);
        document = new Document(this);
        FormLayout layout = new FormLayout();
        shell.setLayout(layout);
        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);
        Menu file_menu = createMenu(menu, "&File");
        file_open = createMenuItem(file_menu, "&Open...", null, SWT.PUSH, SWT.None);
        FormData layout_control = new FormData();
        layout_control.left = new FormAttachment(0);
        layout_control.right = new FormAttachment(30);
        layout_control.top = new FormAttachment(0);
        layout_control.bottom = new FormAttachment(30);
        view_control = new ControlView(this);
        view_control.setLayoutData(layout_control);
        FormData layout_filelist = new FormData();
        layout_filelist.left = new FormAttachment(0);
        layout_filelist.right = new FormAttachment(30);
        layout_filelist.top = new FormAttachment(view_control);
        layout_filelist.bottom = new FormAttachment(100);
        view_filelist = new FileListView(this);
        view_filelist.setLayoutData(layout_filelist);
        FormData layout_graph = new FormData();
        layout_graph.left = new FormAttachment(view_filelist);
        layout_graph.right = new FormAttachment(100);
        layout_graph.top = new FormAttachment(0);
        layout_graph.bottom = new FormAttachment(100);
        view_graph = new GraphView(this);
        view_graph.setLayoutData(layout_graph);
        document.init();
    }

    public void update(int event) {
        view_graph.update(event);
        view_control.update(event);
        view_filelist.update(event);
    }

    public Document getDocument() {
        return document;
    }

    /**
	 * 
	 */
    public void dispose() {
        document.finish();
        display.dispose();
    }

    /**
	 * 
	 */
    public void display() {
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        dispose();
    }

    /**
	 * 
	 */
    public Shell getShell() {
        return shell;
    }

    /**
	 * 
	 */
    public Display getDisplay() {
        return display;
    }

    /**
	 * 
	 */
    public void getFocus() {
        display.asyncExec(new Runnable() {

            public void run() {
                shell.forceFocus();
            }
        });
    }

    /**
	 * 
	 */
    public void handleEvent(Event event) {
        if (event.widget == file_open) {
            FileDialog dlg = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
            String open = dlg.open();
            if (open == null) return;
            File path = new File(open).getParentFile();
            for (String filename : dlg.getFileNames()) document.addFile(path.getAbsolutePath() + "/" + filename);
            update(Document.UPDATE_ADD_FILE);
        }
    }

    /** */
    protected Menu createMenu(Menu menu, String name) {
        Menu m = new Menu(shell, SWT.DROP_DOWN);
        MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setMenu(m);
        item.setText(name);
        return m;
    }

    /** */
    protected MenuItem createMenuItem(Menu menu, String name, Image icon, int style, int accelerator) {
        MenuItem m = new MenuItem(menu, style);
        m.setText(name);
        m.setAccelerator(accelerator);
        m.addListener(SWT.Selection, this);
        return m;
    }

    protected Shell shell;

    protected Display display;

    protected Document document;

    protected MenuItem file_open;

    protected GraphView view_graph;

    protected ControlView view_control;

    protected FileListView view_filelist;
}
