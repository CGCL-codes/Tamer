package leeon.bbsbrower.views;

import ipworks.IPWorksException;
import leeon.bbsbrower.actions.BBSBoardAction;
import leeon.bbsbrower.models.BoardObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class AllBoardView extends ViewPart {

    public static final String ID = "bbsbrower.view.allboard";

    private List list = null;

    private static BoardObject[] boards = null;

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    public void createPartControl(Composite parent) {
        new Label(parent, SWT.NONE);
        list = new List(parent, SWT.V_SCROLL);
        list.setFont(ApplicationWorkbenchWindowAdvisor.listFont);
        list.select(0);
        list.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.ARROW_LEFT) {
                    actionToMain();
                    e.doit = false;
                } else if (e.keyCode == SWT.ARROW_RIGHT || e.keyCode == 13) {
                    actionBoardsOfAll();
                    e.doit = false;
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        new Label(parent, SWT.NONE);
    }

    /**
	 * 回主选单
	 */
    private void actionToMain() {
        IWorkbenchPage page = this.getViewSite().getPage();
        page.hideView(this);
        try {
            MainView main = (MainView) page.showView(MainView.ID);
            main.refreshData(4);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    /**
	 * 进入所有
	 */
    private void actionBoardsOfAll() {
        int bd = list.getSelectionIndex();
        String url = AllBoardView.boards[bd].getURL();
        String bn = AllBoardView.boards[bd].getName();
        IWorkbenchPage page = this.getViewSite().getPage();
        page.hideView(this);
        try {
            DocView doc = (DocView) page.showView(DocView.ID);
            doc.refreshData(bd, bn, url, 0, 2, null);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    public void refreshData(int index) {
        try {
            boards = BBSBoardAction.getAllBoards();
        } catch (IPWorksException e) {
            MessageDialog.openError(this.getViewSite().getShell(), "错误信息", e.toString());
            e.printStackTrace();
        }
        list.removeAll();
        if (boards != null) {
            for (int i = 0; i < boards.length; i++) {
                list.add(boards[i].getCh());
            }
            list.select(index);
        }
        ApplicationActionBarAdvisor.setStatusLine();
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        list.setFocus();
    }
}
