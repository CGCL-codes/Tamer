package edu.ucdavis.genomics.metabolomics.binbase.gui.swt.action;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;

/**
 * ermittelt alle views dieser perspective
 * 
 * @author wohlgemuth
 */
public class SAOV extends Action implements IWorkbenchWindowPulldownDelegate, SelectionListener {

    /**
	 * DOCUMENT ME!
	 * 
	 * @uml.property name="window"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    private IWorkbenchWindow window;

    private Logger logger = Logger.getLogger(this.getClass());

    /**
	 * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt.widgets.Control)
	 */
    public Menu getMenu(Control parent) {
        Menu menu = new Menu(parent);
        IWorkbenchPage[] p = window.getPages();
        for (int i = 0; i < p.length; i++) {
            IViewReference[] r = p[i].getViewReferences();
            for (int x = 0; x < r.length; x++) {
                MenuItem item = new MenuItem(menu, SWT.BORDER);
                item.setText(r[x].getTitle());
                item.setData(r[x]);
                item.addSelectionListener(this);
            }
        }
        return menu;
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
    public void dispose() {
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    /**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
    public void run(IAction action) {
    }

    /**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    /**
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e) {
        IViewReference r = (IViewReference) e.widget.getData();
        IViewPart v = r.getView(true);
        logger.info("activating: " + v.getTitle());
        this.window.setActivePage(v.getViewSite().getPage());
        this.window.getActivePage().activate(v);
        logger.info("done: " + v.getTitle());
    }
}
